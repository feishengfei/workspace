#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <sys/sysinfo.h>

#include <mapi/mapi.h>
#include <mapi/cdma_req.h>
#include <mapi/gsm_req.h>

#include "debug_alloc.h"
#include "rbglobal.h"
#include "bdi_event.h"
#include "bdi_event_list.h"
#include "bdi_unix_stream.h"
#include "bdi_daemon.h"
#include "bdi_message.h"
#include "bdi_mobile_thread.h"

using namespace BDI;

// BDI_MobileThread members

void BDI_MobileThread::run()
{
	RB_DEBUG("mobile-thread start running... (%ld)", static_cast<long>(_tid));

	BDI_UnixStreamLink us; // file socket read/write
	uint8 hdrbuf[32];
	const int URC_HEADER_LEN = sizeof(urc_hdr_t);

	for (;;) {
		// connects to mapi server
		_mapifd = mapi_get_urcserver(-1);

		if (_mapifd < 0) {
			RB_WARNING("mobile|Fail to register mapi.");
			goto CLEAR_CONTINUE;
		} else
			RB_DEBUG("mobile|Attached to mapi (%d)", _mapifd);

		// get module type
		if (_mtype == MT_Unknown) {
			switch (mapi_get_urc_type()) {
			case 1:
				_mtype = MT_Cdma;
				cdma_set_opt(CDMAOPT_BLOCK, 0);
				RB_DEBUG("mobile|CDMA module");
				break;

			case 2:
				_mtype = MT_Gsm;
				gsm_set_opt(GSMOPT_BLOCK, 0);
				RB_DEBUG("mobile|GSM module");
				break;

			default:
				RB_WARNING("mobile|Cannot read module!");
				goto CLEAR_CONTINUE;
				break;
			}
		}

		// initialize module
		if (!_moduleInit) {
			if (initModule()) {
				_moduleInit = true;
				RB_DEBUG("mobile|Module init done!");
			} else {
				RB_WARNING("mobile|Cannot init module!");
				goto CLEAR_CONTINUE;
			}
		}

        // get signal level
        getCsq();
	    BDI_MSignalEvent *sig = NEW BDI_MSignalEvent(CELL_NSIGV, _sigLevel);
	    if (sig != NULL) {
	    	RB_DEBUG("+++ %s", sig->toString().c_str());
	    	_d->rtdataList()->addEvent(sig);
	    }

		// handle SMS
		popSms();

		_ready = 1;
		RB_DEBUG("mobile|Ready!");

		for (;;) {
			fd_set fdSet;
			FD_ZERO(&fdSet);
			FD_SET(_mapifd, &fdSet);

			int ret = select(_mapifd + 1, &fdSet, NULL, NULL, NULL);

			if (ret < 0 && errno != EINTR) {
				perror("mobile|select()");
				break;
			}

			// get URC header
			if (us.getBlock(_mapifd, hdrbuf, URC_HEADER_LEN) != 0) {
				RB_WARNING("mobile|Fail to read mapi URC header.");
				break;
			}

			urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

			if (phdr->magic != URC_MAGIC) {
				RB_WARNING("mobile|Bad mapi URC.");
				break;
			}

			// get the URC frame body
			const int bodylen = phdr->len - URC_HEADER_LEN;
			uint8 *bodybuf = NEW uint8[bodylen];

			if (bodybuf == NULL)
				break;

			if (us.getBlock(_mapifd, bodybuf, bodylen) != 0) {
				DELETE_ARR(bodybuf);
				RB_WARNING("mobile|Fail to read mapi URC.");
				break;
			}

			if (phdr->type == URC_CDMA) {
				onCdmaUrc(hdrbuf, bodybuf);
			} else if (phdr->type == URC_GSM) {
				onGsmUrc(hdrbuf, bodybuf);
			}

			DELETE_ARR(bodybuf);
		}

CLEAR_CONTINUE:

		if (_mapifd != -1) {
			RB_DEBUG("mobile|Detached from mapi! (%d)", _mapifd);
			mapi_put_urcserver(_mapifd);
			_mapifd = -1;
		}

		sleep(3);
	}
}

void BDI_MobileThread::popSms()
{
	if (_mtype == MT_Cdma) {
		cdma_hcmgl_t cmgl;
		cmgl.stat = 4;

		if (mapi_cdma_hcmgl(&cmgl) != 0)
			return;

		for (int i = 0; i < cmgl.list_cnt; i++) {
			if (cmgl.list[i].tag == 0) {
				cdma_hcmgr_t cmgr;
				cmgr.index = cmgl.list[i].index;
				cmgr.mode = 0;
				mapi_cdma_hcmgr(&cmgr);
				break;
			} else {
				cdma_cmgd_t cmgd;
				cmgd.index = cmgl.list[i].index;
				cmgd.delflag = 0;
				mapi_cdma_cmgd(&cmgd);
			}
		}
	} else if (_mtype == MT_Gsm) {
	}
}

bool BDI_MobileThread::initModule()
{
	if (_mtype == MT_Cdma) {
		cdma_clip_t clip;
		clip.n = 1;

		if (0 != mapi_cdma_clip(&clip)) {
			printf("mapi_cdma_clip fail\n");
			return false;
		}

		cdma_hsmsss_t hsmsss;
		hsmsss.ack=0;
		hsmsss.prt=0;
		hsmsss.fmt=6;    //UNICODE编码
		hsmsss.prv=0;

		if (0 != mapi_cdma_hsmsss(&hsmsss)) {
			printf("mapi_cdma_hsmsss fail\n");
			return false;
		}

		/*	cdma_cnmi_t cnmi;
			cnmi.mode = 0;
			cnmi.mt = 2;
			cnmi.bfr = 1;
			if (0 != mapi_cdma_cnmi(&cnmi)) {
				printf("mapi_cdma_cnmi fail\n");
				return false;
			}*/
	} else if (_mtype == MT_Gsm) {
		//set sms receiving act
		gsm_cnmi_t cnmi;
		cnmi.mode = 1;
		cnmi.mt = 1;
		cnmi.bm = 0;
		cnmi.ds = 1;
		cnmi.bfr = 0;

		if (0 != mapi_gsm_cnmi(&cnmi))
			return false;

		//clear ME
		gsm_cmgd_t cmgd;
		cmgd.index = 1;
		cmgd.delflag = 4;
		mapi_gsm_cmgd(&cmgd);

		gsm_clip_t clip;
		clip.n = 1;
		mapi_gsm_clip(&clip);

		if (0 != mapi_gsm_clip(&clip))
			return false;
	}

	return true;
}

void BDI_MobileThread::onCdmaUrc(void *hdrbuf, void *rbuf)
{
	urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

	switch (phdr->event) {
	case REQ_CDMA_RSSILVL:
		cdma_rssilvl_t *prssi = reinterpret_cast<cdma_rssilvl_t *>(rbuf);
        _sigLevel = (prssi->rssi + 1) / 20;

		BDI_MSignalEvent *sig = NEW BDI_MSignalEvent(CELL_NSIGV, _sigLevel);
		if (sig != NULL) {
			RB_DEBUG("+++ %s", sig->toString().c_str());
			_d->rtdataList()->addEvent(sig);
		}
		break;

	case REQ_CDMA_RING:
		break;
	case REQ_CDMA_CLIPA:
		_busy = 1;
		_inCalling = true;

		cdma_clipa_t clipa;
		memcpy(&clipa, rbuf, sizeof(cdma_clipa_t));

		char num[32];
		cleanPhoneNumber(clipa.number, num, 32);

		BDI_MCallEvent *callin = NEW BDI_MCallEvent(Mobile_CallingIn);

		if (callin != NULL) {
			callin->setNumber(num);
			RB_DEBUG("+++ %s", callin->toString().c_str());
			_d->rtdataList()->addEvent(callin);
		}

		break;

	case REQ_CDMA_CONN: {
		_busy = 1;
		BDI_MCallEvent *callup = NULL;
		cdma_conn_t conn;
		memcpy(&conn, rbuf, sizeof(cdma_conn_t));

		if (0 == conn.call_type)
			callup = NEW BDI_MCallEvent(Mobile_CallingUp);

		if (callup != NULL) {
			callup->setCallingLineId(conn.call_x);
			RB_DEBUG("+++ %s", callup->toString().c_str());
			_d->rtdataList()->addEvent(callup);
		}
	    }
	    break;

	case REQ_CDMA_CEND:
		_busy = 0;
		cdma_cend_t cend;
		memcpy(&cend, rbuf, sizeof(cdma_cend_t));
		BDI_MCallEvent *hungup = NEW BDI_MCallEvent(Mobile_Hungup);

		if (hungup != NULL) {
			RB_DEBUG("+++ %s", hungup->toString().c_str());
			_d->rtdataList()->addEvent(hungup);
		}

		_inCalling = false;
		break;

	case REQ_CDMA_CMTI:
		_busy = 1;
		cdma_cmti_t cmti;
		memcpy(&cmti, rbuf, sizeof(cdma_cmti_t));

		cdma_hcmgr_t cmgr;
		cmgr.index = cmti.index;
		cmgr.mode = 0;
		int ret = mapi_cdma_hcmgr(&cmgr);

		if (0 != ret && !_inCalling)
			_busy = 0;

		break;

	case REQ_CDMA_HCMGRA:
		cdma_hcmgra_t cmgra;
		memcpy(&cmgra, rbuf, sizeof(cdma_hcmgra_t));

#if 0
		fprintf(stderr, "callerID:%s\r\n", cmgra.callerID);
		fprintf(stderr, "format:  %d\r\n", cmgra.format);
		fprintf(stderr, "time:    %d-%d-%d %d:%d:%d\r\n", cmgra.year, cmgra.month, cmgra.day, cmgra.hour, cmgra.minute, cmgra.second);
		fprintf(stderr, "length:  %d\r\n", cmgra.length);
		fprintf(stderr, "lang:    %d\r\n", cmgra.lang);
		fprintf(stderr, "prt:     %d\r\n", cmgra.prt);
		fprintf(stderr, "prv:     %d\r\n", cmgra.prv);
		fprintf(stderr, "type:    %d\r\n", cmgra.type);
		fprintf(stderr, "stat:    %d\r\n", cmgra.stat);
		fprintf(stderr, "msg:");

		for(int i = 0; i < cmgra.length; i++) {
			if(0 == i % 10)
				fprintf(stderr, "\r\n");

			fprintf(stderr, "%02x,", cmgra.msg[i]);
		}

		fprintf(stderr, "\r\n");
#endif

		BDI_DeliverMessageEvent * msgevent = NEW BDI_DeliverMessageEvent;

		if (msgevent != NULL) {
			msgevent->setOrigAddr(0, 0, cmgra.callerID);
			msgevent->setMessageType(BDI::MT_SMS);

			if(6 == cmgra.format) {
				uint16 d[cmgra.length / 2];

				for (int i = 0; i < cmgra.length; i += 2)
					d[i / 2] = cmgra.msg[i] * 256 + cmgra.msg[i + 1];

				msgevent->setText(BDI::Unicode, d, cmgra.length / 2);
			}else if(1 == cmgra.format){
				uint16 d[cmgra.length];

				for (int i = 0; i < cmgra.length; i++)
					d[i] = cmgra.msg[i];

				msgevent->setText(BDI::Unicode, d, cmgra.length);
			}else{
				RB_WARNING("mobile|Unknown message format!");
			}

			RB_DEBUG("+++%s", msgevent->toString().c_str());
			_d->rtdataList()->addEvent(msgevent);
		}

		if (!_inCalling)
			_busy = 0;

		break;

	case REQ_CDMA_BTOK:
		cdma_btok_t btok;
		memcpy(&btok, rbuf, sizeof(cdma_btok_t));
		BDI_MHandsetEvent *hset = NEW BDI_MHandsetEvent;

		if (hset != NULL) {
			hset->setHandsetOn(btok.state == 0x90);
			RB_DEBUG("+++ %s", hset->toString().c_str());
			_d->rtdataList()->addEvent(hset);
		}

		break;

	default:
		RB_DEBUG("mobile|Unhandled CDMA event: 0x%x", phdr->event);
		break;
	}
}

void BDI_MobileThread::onGsmUrc(void *hdrbuf, void *rbuf)
{
	urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

	switch (phdr->event) {
	case REQ_GSM_RING:
		break;

	case REQ_GSM_CLIPA:
		_busy = 1;
		_inCalling = true;

		gsm_clipa_t clipa;
		memcpy(&clipa, rbuf, sizeof(gsm_clipa_t));

		char num[32+1];
		sscanf(clipa.number, "\"%s\"", num);

		BDI_MCallEvent *callin = NEW BDI_MCallEvent(Mobile_CallingIn);

		if (callin != NULL) {
			callin->setNumber(num);
			RB_DEBUG("+++ %s", callin->toString().c_str());
			_d->rtdataList()->addEvent(callin);
		}

		break;

	case REQ_GSM_CONN: {
		BDI_MCallEvent *callup = NULL;
		gsm_conn_t conn;
		memcpy(&conn, rbuf, sizeof(gsm_conn_t));

		if (0 == conn.call_type)
			callup = NEW BDI_MCallEvent(Mobile_CallingUp);

		if (callup != NULL) {
			callup->setCallingLineId(1);
			RB_DEBUG("+++ %s", callup->toString().c_str());
			_d->rtdataList()->addEvent(callup);
		}
	}
	break;

	case REQ_GSM_CEND:
		gsm_cend_t cend;
		memcpy(&cend, rbuf, sizeof(gsm_cend_t));
		BDI_MCallEvent *hungup = NEW BDI_MCallEvent(Mobile_Hungup);

		if (hungup != NULL) {
			RB_DEBUG("+++ %s", hungup->toString().c_str());
			_d->rtdataList()->addEvent(hungup);
		}

		_inCalling = false;
		_busy = 0;
		break;

	case REQ_GSM_CMTI:
		_busy = 1;

		gsm_cmti_t cmti;
		memcpy(&cmti, rbuf, sizeof(gsm_cmti_t));
		gsm_cmgd_t cmgd;
		cmgd.index = cmti.index;
		mapi_gsm_cmgd(&cmgd);

		if (!_inCalling)
			_busy = 0;

		break;

	case REQ_GSM_BTOK:
		gsm_btok_t btok;
		memcpy(&btok, rbuf, sizeof(gsm_btok_t));
		BDI_MHandsetEvent *hset = NEW BDI_MHandsetEvent;

		if (hset != NULL) {
			hset->setHandsetOn(btok.state == 0x90);
			RB_DEBUG("+++ %s", hset->toString().c_str());
			_d->rtdataList()->addEvent(hset);
		}

		break;

	default:
		RB_DEBUG("mobile|Unhandled GSM event: 0x%x", phdr->event);
		break;
	}
}

void BDI_MobileThread::onStopped()
{
	BDI_Thread::onStopped();

	BDI_MSignalEvent *sig = NEW BDI_MSignalEvent(CELL_NSIGV, CELL_NSIGL);

	if (sig != NULL) {
		RB_DEBUG("+++ %s", sig->toString().c_str());
		_d->rtdataList()->addEvent(sig);
	}

	if (_mapifd != -1) {
		RB_DEBUG("mobile|Detached from mapi! (%d)", _mapifd);
		mapi_put_urcserver(_mapifd);
		_mapifd = -1;
	}

	_ready = 0;
    _sigLevel = CELL_NSIGL;
}

BDI_MobileThread::BDI_MobileThread(BDI_Daemon *d)
	: BDI_Thread(d)
{
	_mapifd = -1;
	_mtype = MT_Unknown;
	_inCalling = false;
	_moduleInit = false;
	_ready = 0;
    _sigLevel = CELL_NSIGL;
}

int BDI_MobileThread::getCsq() const
{
    BDI_MobileThread *self = const_cast<BDI_MobileThread *>(this);
    int res;

	if (_mtype == MT_Cdma) {
		cdma_csq_t c;
		if (mapi_cdma_csq(&c) != 0)
            return CELL_NSIGV;

        res = c.rssi;

		if (c.rssi == CELL_NSIGV)
			self->_sigLevel = 0;
		else if (0 <= c.rssi && c.rssi <= 8)
			self->_sigLevel = 1;
		else if (9 <= c.rssi && c.rssi <= 15)
			self->_sigLevel = 2;
		else if (16 <= c.rssi && c.rssi <= 24)
			self->_sigLevel = 3;
		else if (25 <= c.rssi && c.rssi <= 30)
			self->_sigLevel = 4;
		else if (31 <= c.rssi && c.rssi <= 98)
			self->_sigLevel = 5;
	} 
    else if (_mtype == MT_Gsm) {
		gsm_csq_t g;
        if (mapi_gsm_csq(&g) != 0)
            return CELL_NSIGV;

        res = g.rssi;

		if (g.rssi == CELL_NSIGV)
			self->_sigLevel = 0;
		else if (0 <= g.rssi && g.rssi <= 8)
			self->_sigLevel = 1;
		else if (9 <= g.rssi && g.rssi <= 15)
			self->_sigLevel = 2;
		else if (16 <= g.rssi && g.rssi <= 24)
			self->_sigLevel = 3;
		else if (25 <= g.rssi && g.rssi <= 30)
			self->_sigLevel = 4;
		else if (31 <= g.rssi && g.rssi <= 98)
			self->_sigLevel = 5;
	}

    return res;
}

int BDI_MobileThread::dial(int lineId, const char *number)
{
	int ret = 0;

	if (_mtype == MT_Gsm) {
		if (0 == lineId) {
			ret = mapi_gsm_dial((void *)number);
		} else {
			ret = mapi_gsm_vts(*number);
		}
	} else if (_mtype == MT_Cdma) {
		if (0 == lineId) {
			ret = mapi_cdma_cdv((char *)number);
		} else {
			cdma_dtmf_t r;
			r.call_x = lineId;	//呼叫ID，取值范围1~9
			r.dtmf_digit = *number;
			ret =  mapi_cdma_dtmf(&r);
		}
	} else {
		ret = SES_REQUEST_UNEXEC;
	}

	return ret;
}

int BDI_MobileThread::answer()
{
	int ret = 0;

	if (_mtype == MT_Gsm) {
		ret = mapi_gsm_answer();
	} else if (_mtype == MT_Cdma) {
		ret = mapi_cdma_answer();
	} else
		ret = SES_REQUEST_UNEXEC;

	return ret;
}

int BDI_MobileThread::hangup()
{
	int ret = 0;

	if (_mtype == MT_Gsm) {
		ret = mapi_gsm_hangup();
	} else if (_mtype == MT_Cdma) {
		ret = mapi_cdma_chv();
	} else
		ret = SES_REQUEST_UNEXEC;

	return ret;
}

int BDI_MobileThread::sendMessage(const char *number, const char *unicodes, int buflen)
{
	fprintf(stderr, "<<<<<<<<<<<<<<<<<<<<<<  in SendMessage >>>>>>>>>>>>>>>>\r\n");
	fprintf(stderr, "SendMessage|number:%s, buflen= %d\r\n", number, buflen);
	fprintf(stderr, "unicodes:");

	for(int i = 0; i < buflen; i++) {
		fprintf(stderr,"%02x,", unicodes[i]);
	}

	fprintf(stderr, "\r\n");

	//assert(number != NULL);
	//assert(unicodes != NULL);
	//assert(buflen > 0);

	if(_mtype == MT_Cdma) { //CDMA
#if 1  //Set Modue for unicode
		cdma_hsmsss_t set;
		set.ack = 0;
		set.prt = 0;
		set.fmt = 6;
		set.prv = 0;
		mapi_cdma_hsmsss(&set);
#endif
		cdma_hcmgs_t data;
		data.len = buflen;
		strcpy(data.da, number);
		memcpy(data.msg, unicodes, buflen);
		int ret;
		ret = mapi_cdma_hcmgs(&data);

		if(0 == ret)
			return 0;
		else
			return SES_REQUEST_UNEXEC;
	} else if(_mtype == MT_Gsm) {
		fprintf(stderr, "SendMessage| is GSM\r\n");
		return SES_REQUEST_UNEXEC;
	} else {
		return SES_REQUEST_UNEXEC;
	}

	return 0;
}
