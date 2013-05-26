#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <signal.h>
#include <linux/if_tun.h>
#include <netinet/in.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <linux/if.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <mapi/mapi.h>
#include <mapi/cdma_req.h>
#include <mapi/gsm_req.h>
#include <bdi_unix_stream.h>

#include "rbglobal.h"
#include "bdi_ppp_link.h"

BDI_PPPLink::BDI_PPPLink()
{
	_mapifd = -1;
	_running = false;
	_pppConfiged = false;
	_mtype = MT_Unknown;
	_status = BDI_Link::Disabled;
	_siglvl = 255;
	_ppp_inst = Req_None;
	_timeoutCount = 1;
}

BDI_PPPLink::~BDI_PPPLink()
{
}

void BDI_PPPLink::startURCThread()
{
	pthread_attr_t attr;

	if (0 != pthread_attr_init(&attr)) {
		RB_WARNING("ppp|Init phtread attr error!");
		return;
	}

	if (0 != pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED)) {
		RB_WARNING("ppp|Set phtread detached error!");
		return;
	}

	if (0 != pthread_create(&_urcTid, &attr, &urcThread, this))
		RB_WARNING("ppp|create urcThread error!");
	else
		_running = true;

	pthread_attr_destroy(&attr);
}

void *BDI_PPPLink::urcThread(void *pthis)
{
	BDI_PPPLink *thiz = reinterpret_cast<BDI_PPPLink *>(pthis);
	BDI_UnixStreamLink us;
	uint8 hdrbuf[32];
	const int URC_HEADER_LEN = sizeof(urc_hdr_t);

	RB_DEBUG("ppp-thread start running... (%ld)", static_cast<long>(thiz->_urcTid));

	if (MT_Unknown == thiz->_mtype) {
		int t = mapi_get_urc_type();

		if (1 == t) {
			thiz->_mtype = BDI_PPPLink::MT_Cdma;
			RB_DEBUG("ppp|CDMA");
		} else if(2 == t) {
			thiz->_mtype = BDI_PPPLink::MT_Gsm;
			RB_DEBUG("ppp|GSM");
		} else {
			thiz->_running = false;
			pthread_exit(NULL);
		}
	}

	for (;;) {
		int health = 1;

		if (access("/var/run/ppp0.pid", F_OK))
			thiz->_status = BDI_Link::Disabled;
		else
			thiz->_status = BDI_Link::Connected;

		thiz->_mapifd = mapi_get_urcserver(-1);

		if (thiz->_mapifd < 0) {
			health = -1;
			RB_WARNING("ppp|Fail to register mapi.");
		} else {
			RB_DEBUG("ppp|Attached to mapi (%d)", thiz->_mapifd);
		}

		while (health > 0) {
			// handle ppp request
			switch (thiz->_mtype) {
			case MT_Cdma:
				thiz->cdma_PppOpt();
				break;

			case MT_Gsm:
				thiz->gsm_PppOpt();
				break;

			default:
				break;
			}

			if (thiz->_status == BDI_Link::Disabled) {
				//health = -1;
				break;
			}

			// handle mapi URC
			struct timeval timeout;
			timeout.tv_sec = 0;
			timeout.tv_usec = SELECTTIME * 1000;
			fd_set fdSet;
			FD_ZERO(&fdSet);
			FD_SET(thiz->_mapifd, &fdSet);

			int ret = select(thiz->_mapifd + 1, &fdSet, NULL, NULL, &timeout);

			if (ret < 0 && errno != EINTR) {
				RB_WARNING("ppp|select(): %s", strerror(errno));
				health = 0;
				break;
			} 
			else if (ret > 0) {
				// get urc header
				if (us.getBlock(thiz->_mapifd, hdrbuf, URC_HEADER_LEN) != 0) {
					RB_WARNING("ppp|Fail to read mapi URC header.");
					health = 0;
					break;
				}

				urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

				if (phdr->magic != URC_MAGIC) {
					RB_WARNING("ppp|Bad mapi URC.");
					health = 0;
					break;
				}

				//get the URC frame body
				const int bodylen = phdr->len - URC_HEADER_LEN;
				uint8 *bodybuf = NEW uint8[bodylen];

				if (NULL == bodybuf) {
					health = 0;
					break;
				}

				if (us.getBlock(thiz->_mapifd, bodybuf, bodylen) != 0) {
					RB_WARNING("ppp|Fail to read mapi URC.");
					DELETE_ARR(bodybuf);
					health = 0;
					break;
				}

				bool pppIsDown = false;
				thiz->decode_urc(hdrbuf, bodybuf, &pppIsDown);

				if (pppIsDown)
					//health = -1;

				DELETE_ARR(bodybuf);
			}
		}

		if (thiz->_mapifd > 0) {
			RB_DEBUG("ppp|Detached from mapi (%d)", thiz->_mapifd);
			mapi_put_urcserver(thiz->_mapifd);
			thiz->_mapifd = -1;
		}

		if (health != -1)
			sleep(2);
		else
			break;
	}

	RB_DEBUG("ppp|Thread stopped!");
	thiz->_running = false;
	pthread_exit(NULL);
}

void BDI_PPPLink::setEnabled(bool enabled)
{
	if (enabled) {
		_ppp_inst = Req_Enable;

		if (!_running) {
			startURCThread();
			usleep(200000);
		}
	} else {
		_ppp_inst = Req_Disable;
	}
}

void BDI_PPPLink::cdma_PppOpt()
{
	int ret;
	Ppp_inst inst = _ppp_inst;
	_ppp_inst = Req_None;

	// startup pppd
	if (inst == Req_Enable && _status == BDI_Link::Disabled) {
		if (access("/var/run/ppp0.pid", F_OK) == 0) {
			_status = BDI_Link::Connected;
			return;
		}

		if (!_pppConfiged) {
			cdma_pppcfg_t r;
			memset(&r, 0, sizeof(cdma_pppcfg_t));
			strcpy(r.userid,"\"card\"");
			strcpy(r.password,"\"card\"");
			ret = mapi_cdma_pppcfg(&r);

			if (ret) {
				RB_WARNING("ppp|mapi_cdma_pppcfg error, ret=%d", ret);
				mapi_cdma_hangup();
				_status = BDI_Link::NoConnection;
				return;
			} else
				_pppConfiged = true;
		}

		ret = mapi_cdma_pppup();

		if (ret < 0) {
			RB_WARNING("ppp|mapi_cdma_pppup error, ret=%d", ret);
			mapi_cdma_hangup();
			mapi_cdma_pppdown();
			_status = BDI_Link::NoConnection;
		} else
			_status = BDI_Link::Connecting;
	}

	// stop pppd
	if (inst == Req_Disable) {
		if (_status == BDI_Link::Connected || _status == BDI_Link::Connecting) {
			mapi_cdma_pppdown();
			_status = BDI_Link::NoConnection;
		} else
			_status = BDI_Link::Disabled;
	}
}

void BDI_PPPLink::gsm_PppOpt()
{
	int ret;
	Ppp_inst inst = _ppp_inst;
	_ppp_inst = Req_None;

	// startup pppd
	if (inst == Req_Enable && _status == BDI_Link::Disabled) {
		if (access("/var/run/ppp0.pid", F_OK) == 0) {
			_status = BDI_Link::Connected;
			return;
		}

		if (!_pppConfiged) {
			gsm_cgdcont_t cr;
			cr.cid = 1;
			strcpy(cr.PDP_type, "\"IP\"");
			strcpy(cr.APN, "\"CMNET\"");
			ret = mapi_gsm_cgdcont(&cr);

			if (0 != ret) {
				RB_WARNING("ppp|mapi_gsm_cgdcont error, ret=%d", ret);
				mapi_gsm_hangup();
				_status = BDI_Link::NoConnection;
				return;
			} else
				_pppConfiged = true;
		}

		gsm_pppup_t r;
		r.cid = 1;
		ret = mapi_gsm_pppup(&r);

		if (ret < 0) {
			RB_WARNING("ppp|mapi_gsm_pppup error, ret=%d", ret);
			mapi_gsm_hangup();
			gsm_pppdown_t r;
			r.n = 3;
			mapi_gsm_pppdown(&r);
			_status = BDI_Link::NoConnection;
		} else
			_status = BDI_Link::Connecting;
	}

	// stop pppd
	if (inst == Req_Disable) {
		if (_status == BDI_Link::Connected || _status == BDI_Link::Connecting) {
			gsm_pppdown_t r;
			r.n = 0;
			mapi_gsm_pppdown(&r);
			_status = BDI_Link::NoConnection;
		} else
			_status = BDI_Link::Disabled;
	}

	return;
}

void BDI_PPPLink::decode_urc(void *header, void *body, bool *pppIsDown)
{
	urc_hdr_t * phdr = (urc_hdr_t *)header;

	if (URC_GSM == phdr->type) {
		switch ( phdr->event ) {
		case REQ_GSM_PPPSTATE:
			gsm_pppstate_t *pppst = (gsm_pppstate_t *)body;
			printf("ppp|gsm REQ_GSM_PPPSTATE URC rssi = %d\r\n", pppst->val);

			if(5 == pppst->val) {
				_status = BDI_Link::Connected;
				_siglvl = 255;
			} else {
				_status = BDI_Link::Disabled;
				*pppIsDown = true;
			}

			break;

		case REQ_GSM_SISWA:
			break;

		default:
			RB_DEBUG("ppp|Unhandled GSM event: 0x%x", phdr->event);
			break;
		}
	} else if (URC_CDMA == phdr->type) {
		switch (phdr->event) {
		case REQ_CDMA_PPPSTATE:
			cdma_pppstate_t *pppst = (cdma_pppstate_t *)body;
			RB_DEBUG("ppp|cdma REQ_CDMA_PPPSTATE urc rssi = %d", pppst->val);

			if (5 == pppst->val) {
				_status = BDI_Link::Connected;
				_siglvl = 255;
			} else {
				_status = BDI_Link::Disabled;
				*pppIsDown = true;
			}

			break;

		case REQ_CDMA_RSSILVL:
			cdma_rssilvl_t * prssi = (cdma_rssilvl_t *)body;
			_siglvl = (prssi->rssi + 1) / 20;
			break;

		case REQ_CDMA_CONNECT:
			break;

		default:
			RB_DEBUG("ppp|Unhandle CDMA event: 0x%02x", phdr->event);
			break;
		}
	} else {
		RB_DEBUG("ppp|Unexpected mapi URC type: 0x%02x", phdr->type);
	}

	return;
}

void BDI_PPPLink::getSignalLevel()
{
	//printf("in getSignalLevel(), timeoutcount = %d, T = %d", _timeoutCount, TIMEOUTCOUNT);
	int v;

	switch(_mtype) {
	case MT_Cdma:
		cdma_csq_t cr;

		if (mapi_cdma_csq(&cr)) {
			RB_WARNING("ppp|cdma Get signal level error");
			v = -1;
		} else {
			v = cr.rssi;
		}

		break;

	case MT_Gsm:
		gsm_csq_t gr;

		if (mapi_gsm_csq(&gr)) {
			RB_WARNING("ppp|cdma Get signal level error");
			v = -1;
		} else {
			v = gr.rssi;
		}

		break;

	default:
		v = -1;
		break;
	}

	if (0 == v) {
		_siglvl = 0;
	} else if (v > 0 && v <= 8) {
		_siglvl = 1;
	} else if (v >= 9 && v <= 15) {
		_siglvl = 2;
	} else if (v >= 16 && v <= 24) {
		_siglvl = 3;
	} else if (v >= 25 && v <= 30) {
		_siglvl = 4;
	} else if (v >= 31 && v <= 98) {
		_siglvl = 5;
	} else if (99 == v) {
		_siglvl = 0;
	} else {
		_siglvl = 255;
	}

	//printf("r.rssi = %d, siglvl = %d\r\n", v, _siglvl);

	return;
}

