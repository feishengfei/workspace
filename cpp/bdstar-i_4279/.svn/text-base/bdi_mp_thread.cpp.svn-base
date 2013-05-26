
#include <stdio.h>
#include <stdlib.h>
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
#include <mapi/gsm_sms.h>
#include <bdi_unix_stream.h>

#include "rbglobal.h"
#include "bdi_mp_thread.h"
#include "debug_alloc.h"
#include "bdi_mobile_event.h"
#include "bdi_daemon.h"
#include "bdi_message.h"
#include "config.h"

BDI_MP_Thread::BDI_MP_ModuleStat::BDI_MP_ModuleStat()
{
	_s = MobileReady;
	_t = time(NULL);
}

BDI_MP_Thread::BDI_MP_ModuleStat::~BDI_MP_ModuleStat()
{
}

void BDI_MP_Thread::BDI_MP_ModuleStat::setStat(BDI_MP_Thread::ModuleStat s)
{
	if(_s == s)
		return;

	_t = time(NULL);
	_s = s;
}

inline BDI_MP_Thread::ModuleStat BDI_MP_Thread::BDI_MP_ModuleStat::stat() const
{
	return _s;
}

bool BDI_MP_Thread::BDI_MP_ModuleStat::timeOut()
{
	if(MobileBusy == _s || PPPConnected == _s)
		return false;

	int ct = time(NULL);

	if((ct - _t) > 10) {
		_t = ct;
		return true;
	} else {
		return false;
	}
}

BDI_MP_Thread::BDI_MP_Thread(BDI_Daemon * d)
	: BDI_Thread(d)
{
	_mapifd = -1;
	_mtype = MT_Unknown;
	_status.setStat(MobileReady);
	_pppEvent = NEW BDI_MP_PPPEvent(this);
	_dialEvent = NULL;
	_hungupEvent = NULL;
	_answerEvent = NULL;
	_getsigEvent = NULL;
	_sendMsgEvent = NULL;

	if(NULL == _pppEvent){
		RB_FATAL("BDI_MP_Thread|line[%d] malloc error!", __LINE__);
	}
	else{
		_pppEvent->setEnable(true);
	}

	_csqTime = 0;
	_sigLevel = 0;
	_sigSteadyCount = 5;
	_pppErrorCount = 5;

	_msgEventList = NEW BDI_EventList;

	if(NULL == _msgEventList)
		RB_DEBUG("BDI_MP_thread|line[%d] malloc error!", __LINE__);

	_ce = NULL;
	memset(_apt, 0, 12);
	strcpy(_apt, "\"CMNET\"");
}

BDI_MP_Thread::~BDI_MP_Thread()
{
	if(NULL != _pppEvent)
		DELETE(_pppEvent);

	if(NULL != _msgEventList)
		DELETE(_msgEventList);
}

void BDI_MP_Thread::eventHandle()
{
	if(NULL != _ce) {
		if(_ce->handle()) {
			BDI_Event * rete = _ce->retEvent();

			if(NULL != rete)
				_d->rtdataList()->addEvent(rete);

			_status.setStat(_ce->stat());

			if(NULL != _sendMsgEvent)
				_msgEventList->removeEvent(_tid, &_sendMsgEvent);
			else
				DELETE(_ce);

			_ce = NULL;
		} else {
			_status.setStat(_ce->stat());
		}

	} else {
		switch(_status.stat()) {
		case MobileReady:
			if(_status.timeOut()) {
				if(_sigSteadyCount >= 3) {
					_pppEvent->setEnable(true);
				}
			}

		case PPPConnected:
			if(NULL != _dialEvent) {
				_ce = _dialEvent;
				_dialEvent = NULL;
			}

			if(NULL != _hungupEvent) {
				_ce = _hungupEvent;
				_hungupEvent = NULL;
			}

			if(0 != _msgEventList->count()) {
				_sendMsgEvent = _msgEventList->getFreeEvent(_tid, 0);
				BDI_EventRef ref = _sendMsgEvent->event();
				_ce = reinterpret_cast<BDI_MP_Event*>(ref.getPtr());
			}

			if(NULL != _getsigEvent) {
				_ce = _getsigEvent;
				_getsigEvent = NULL;
			}

			break;

		case PPPConnecting:
			if(_status.timeOut()) {
				_status.setStat(MobileReady);
				_pppEvent->setEnable(true);
				_pppErrorCount++;
			}

			break;

		case PPPDisConnecting:
			if(_status.timeOut()) {
				_status.setStat(PPPConnected);
				_pppEvent->setEnable(false);
			}

			break;

		case MobileBusy:
			if(NULL != _dialEvent) {
				_ce = _dialEvent;
				_dialEvent = NULL;
			}

			if(NULL != _hungupEvent) {
				_ce = _hungupEvent;
				_hungupEvent = NULL;
			}

			if(NULL != _answerEvent) {
				_ce = _answerEvent;
				_answerEvent = NULL;
			}

			break;

		default:
			break;
		}

		if(NULL != _ce)
			return;

		_pppEvent->handle();
		_status.setStat(_pppEvent->stat());
	}
}

void BDI_MP_Thread::run()
{
	BDI_UnixStreamLink us;
	uint8 hdrbuf[32];
	const int URC_HEADER_LEN = sizeof(urc_hdr_t);

	RB_DEBUG("BDI_MP_Thread start running... (%ld)", static_cast<long>(_tid));

	while(1) {
		if(initModule())
			break;
		else {
			RB_DEBUG("BDI_MP_Thread|initModule error!");
			sleep(1);
		}
	}

	for (;;) {
		int health = 1;

		_mapifd = mapi_get_urcserver(-1);

		if (_mapifd < 0) {
			health = -1;
			RB_WARNING("BDI_MP_Thread|Fail to register mapi.");
		} else {
			RB_DEBUG("BDI_MP_Thread|Attached to mapi (%d)", _mapifd);
		}

		while (health > 0) {
			//if(_isUpdatePara)
			//updatePara();
			eventHandle();
			/*
			if(NULL != _ce) {
				BDI_EventRef ref = _ce->event();
				BDI_MP_Event * e = reinterpret_cast<BDI_MP_Event*>(ref.getPtr());

				if( 1 == e->handle()) {
					BDI_Event * rete = e->retEvent();
					if(NULL != rete)
						_d->rtdataList()->addEvent(rete);
					_status.setStat(e->stat());
					_mobileEventList->removeEvent(_tid, &_ce);

					if(0 != _mobileEventList->count()) {
						_ce = _mobileEventList->getFreeEvent(_tid, 0);
					} else
						_ce = NULL;
				} else {
					_status.setStat(e->stat());
				}
			} else {
				if(0 != _mobileEventList->count()) {
					_ce = _mobileEventList->getFreeEvent(_tid, 0);
				} else {
					if(_status.timeOut()) {
						//RB_DEBUG("BDI_MP_Thread| Auto switch!");

						if(MobileReady == _status.stat()||PPPConnecting == _status.stat()){
							//printStat();
							if(_sigSteadyCount > 3){
								_status.setStat(MobileReady);
								_pppEvent->setEnable(true);
							}else{
								//RB_DEBUG("bdi_mp_thread|_pppErrorCount++");
								_pppErrorCount++;
							}
						}else if(PPPDisConnecting == _status.stat()) {
							RB_DEBUG("To disenable ppp");
							_status.setStat(PPPConnected);
							_pppEvent->setEnable(false);
						}
					}

					int dt = (_pppErrorCount <= 3)? 60 *10 : 10;
					//RB_DEBUG("bdi_mp_thread| dt = %d, _pppErrorCount = %d", dt, _pppErrorCount);
					if(time(NULL) - dt > _csqTime){
						BDI_Event * e = NEW BDI_MP_GetSigEvent(this);
						if(NULL != e){
							_mobileEventList->addEvent(e);
							_csqTime = time(NULL);
						}

					}else{
						_pppEvent->handle();
						_status.setStat(_pppEvent->stat());
					}
				}
			}*/

			//printStat();

			// handle mapi URC
			struct timeval timeout;
			timeout.tv_sec = 0;
			timeout.tv_usec = SELECTTIME * 1000;
			fd_set fdSet;
			FD_ZERO(&fdSet);
			FD_SET(_mapifd, &fdSet);

			int ret = select(_mapifd + 1, &fdSet, NULL, NULL, &timeout);

			if (ret < 0 && errno != EINTR) {
				RB_WARNING("BDI_MP_Thread|select(): %s", strerror(errno));
				health = 0;
				break;
			} else if (ret > 0) {
				// get urc header
				if (us.getBlock(_mapifd, hdrbuf, URC_HEADER_LEN) != 0) {
					RB_WARNING("BDI_MP_Thread|Fail to read mapi URC header.");
					health = 0;
					break;
				}

				urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

				if (phdr->magic != URC_MAGIC) {
					RB_WARNING("BDI_MP_Thread|Bad mapi URC.");
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

				if (us.getBlock(_mapifd, bodybuf, bodylen) != 0) {
					RB_WARNING("BDI_MP_Thread|Fail to read mapi URC.");
					DELETE_ARR(bodybuf);
					health = 0;
					break;
				}

				if(MT_Cdma == _mtype)
					onCdmaUrc(hdrbuf, bodybuf);
				else if (MT_Gsm == _mtype)
					onGsmUrc(hdrbuf, bodybuf);

				DELETE_ARR(bodybuf);
			}
		}

		if (_mapifd > 0) {
			RB_DEBUG("BDI_MP_Thread|Detached from mapi (%d)", _mapifd);
			mapi_put_urcserver(_mapifd);
			_mapifd = -1;
		}

		sleep(2);
	}

	RB_DEBUG("BDI_MP_Thread|Thread stopped!");
	_running = false;
	pthread_exit(NULL);
}

bool BDI_MP_Thread::initModule()
{
	int t = mapi_get_urc_type();

	if (1 == t) {
		_mtype = MT_Cdma;
		RB_DEBUG("BDI_MP_Thread|Module is CDMA");
	} else if(2 == t) {
		_mtype = MT_Gsm;
		RB_DEBUG("BDI_MP_Thread|Module is GSM");
	} else {
		return false;
	}

	//if (!access("/var/run/ppp0.pid", F_OK))
	//return true;

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

		cdma_pppcfg_t r;
		memset(&r, 0, sizeof(cdma_pppcfg_t));
		strcpy(r.userid,"\"card\"");
		strcpy(r.password,"\"card\"");
		int ret;
		ret = mapi_cdma_pppcfg(&r);

		if (ret) {
			RB_WARNING("BDI_MP_Thread|mapi_cdma_pppcfg error, ret=%d", ret);
			mapi_cdma_hangup();
			return false;
		}

		cdma_cimi_t cimi;
		ret = mapi_cdma_cimi(&cimi);

		if (ret) {
			RB_WARNING("BDI_MP_Thread|mapi_cdma_cimi error, ret=%d", ret);
			return false;
		}
/*
		char *pu = cimi.imsi + strlen(cimi.imsi) - 8;
		ret = _d->setUserName(pu);

		if (ret) {
			RB_WARNING("BDI_MP_Thread|setUserName error, ret = %d", ret);
			return false;
		}
*/
	} else if (_mtype == MT_Gsm) {
		int ret;
		//set sms receiving act
		gsm_cnmi_t cnmi;
		cnmi.mode = 1;
		cnmi.mt = 1;
		cnmi.bm = 0;
		cnmi.ds = 1;
		cnmi.bfr = 0;
		ret = mapi_gsm_cnmi(&cnmi);

		if (0 != ret) {
			RB_DEBUG("BDI_MP_Thread| mapi_gsm_cnmi() error! ret = %d", ret);
			return false;
		}

		RB_DEBUG("BDI_MP_Thread| mapi_gsm_cnmi() ok!");


		gsm_clip_t clip;
		clip.n = 1;
		ret = mapi_gsm_clip(&clip);

		if (0 != ret) {
			RB_DEBUG("BDI_MP_Thread| mapi_gsm_clip() error!, ret = %d", ret);
			return false;
		}

		RB_DEBUG("BDI_MP_Thread| mapi_gsm_clip() ok!");

		Config conf;

		if(conf.open(BDI_APPARA_FILE)) {
			conf.setGroup("NetWork");
			memset(_apt, 0, 12);
			sprintf(_apt, "\"%s\"", conf.getValue("AccessType"));
		}
		RB_DEBUG("bdi_mp_thread|AP type:%s", _apt);

		gsm_cgdcont_t cr;
		cr.cid = 1;
		strcpy(cr.PDP_type, "\"IP\"");
		strcpy(cr.APN, _apt);
		ret = mapi_gsm_cgdcont(&cr);

		if (0 != ret) {
			RB_WARNING("BDI_MP_Thread|mapi_gsm_cgdcont error, ret=%d", ret);
			mapi_gsm_hangup();
			return false;
		}

		RB_WARNING("BDI_MP_Thread|mapi_gsm_cgdcont ok, ret=%d", ret);

		gsm_cimi_t cimi;
		ret = mapi_gsm_cimi(&cimi);

		if (ret) {
			RB_WARNING("BDI_MP_Thread|mapi_cdma_cimi error, ret=%d", ret);
			return false;
		}

/*
		char *pu = cimi.imsi + strlen(cimi.imsi) - 8;
		ret = _d->setUserName(pu);

		if (ret) {
			RB_WARNING("BDI_MP_Thread|setUserName error, ret = %d", ret);
			return false;
		}
*/
	}

	return true;
}

void BDI_MP_Thread::setStat(BDI_MP_Thread::ModuleStat s)
{
	_status.setStat(s);
}

void BDI_MP_Thread::onStopped()
{
}

void BDI_MP_Thread::pppEnable(bool enable)
{
}

int BDI_MP_Thread::dial(int lineId, const char *num)
{
	if(NULL != _dialEvent)
		return SES_REQUEST_UNEXEC;

	_dialEvent = NEW BDI_MP_DialEvent(this, lineId, num, time(NULL));

	RB_DEBUG("BDI_MP_Thread| add DialEvent");
	return 0;
}

int BDI_MP_Thread::answer()
{
	if(NULL != _answerEvent)
		return 0;

	_answerEvent = NEW BDI_MP_AnswerEvent(this);

	//if(!_mobileEventList->addEvent(e))
	//return SES_REQUEST_UNEXEC;

	RB_DEBUG("BDI_MP_Thread| add AnswerEvent");
	return 0;
}

int BDI_MP_Thread::hangup()
{
	if(NULL != _hungupEvent)
		return 0;

	_hungupEvent = NEW BDI_MP_HangupEvent(this);

	RB_DEBUG("BDI_MP_Thread| add HangupEvent");
	return 0;
}

int BDI_MP_Thread::sendMsg(const char * num, const char * unicodes, int len)
{
	BDI_EventRef e(NEW BDI_MP_SendMsgEvent(this, num, unicodes, len));

	if(!_msgEventList->addEvent(e))
		return SES_REQUEST_UNEXEC;

	RB_DEBUG("BDI_MP_Thread| add SendMsgEvent");
	return 0;
}

void BDI_MP_Thread::onGsmUrc(void *header, void *body)
{
	urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(header);

	switch (phdr->event) {
	case REQ_GSM_RING:
		_status.setStat(MobileBusy);
		break;

	case REQ_GSM_CLIPA:

		gsm_clipa_t *clipa = reinterpret_cast<gsm_clipa_t *>(body);

		char num[32+1];
		sscanf(clipa->number, "\"%s\"", num);

		BDI_MCallEvent *callin = NEW BDI_MCallEvent(Mobile_CallingIn);

		callin->setNumber(num);
		RB_DEBUG("BDI_MP_Thread| +++ %s", callin->toString().c_str());
		_d->rtdataList()->addEvent(callin);

		break;

	case REQ_GSM_CONN: {
		BDI_MCallEvent *callup = NULL;
		gsm_conn_t * conn = reinterpret_cast<gsm_conn_t *>(body);

		if (0 == conn->call_type)
			callup = NEW BDI_MCallEvent(Mobile_CallingUp);

		if (callup != NULL) {
			callup->setCallingLineId(1);
			RB_DEBUG("+++ %s", callup->toString().c_str());
			_d->rtdataList()->addEvent(callup);
		}
	}
	break;

	case REQ_GSM_CEND:
		//gsm_cend_t * cend = reinterpret_cast<gsm_cend_t *>(body);
		BDI_MCallEvent *hungup = NEW BDI_MCallEvent(Mobile_Hungup);

		if (hungup != NULL) {
			RB_DEBUG("+++ %s", hungup->toString().c_str());
			_d->rtdataList()->addEvent(hungup);
		}

		_status.setStat(MobileReady);
		if(NULL != _hungupEvent){
			DELETE(_hungupEvent);
			_hungupEvent = NULL;
		}
		break;

	case REQ_GSM_PPPSTATE:
		gsm_pppstate_t *pppst = (gsm_pppstate_t *)body;
		RB_DEBUG("BDI_MP_Thread|cdma REQ_GSM urc rssi = %d", pppst->val);

		if (5 == pppst->val) {
			_status.setStat(PPPConnected);
			BDI_MCallEvent *e = NEW BDI_MCallEvent(PPP_Up);

			if(NULL != e)
				_d->rtdataList()->addEvent(e);
		} else if(0 == pppst->val) {
			if(MobileBusy != _status.stat())
				_status.setStat(MobileReady);

			BDI_MCallEvent *e = NEW BDI_MCallEvent(PPP_Down);

			if(NULL != e)
				_d->rtdataList()->addEvent(e);
		}

		if(0 <= pppst->val)
			break;

	case REQ_GSM_CMTI:
		_status.setStat(MobileReady);
		RB_DEBUG("++++++++++++++++++++Gsm got a message++++++++++++++++++++++++");
		gsm_cmgl_t cmgl;
		memset(&cmgl, 0, sizeof(gsm_cmgl_t));
		cmgl.stat = 4;

		int ret = mapi_gsm_cmgl(&cmgl);

		if(0 == ret) {

			for(int i = 0; i < cmgl.list_cnt; i++) {
				//RB_DEBUG("..............................................................i = %d", i);
				//RB_DEBUG("..............................................................cmgl.list_cnt = %d", cmgl.list_cnt);
				//RB_DEBUG("..............................................................index = %d", cmgl.list[i].index);
				//RB_DEBUG("..............................................................stat  = %d", cmgl.list[i].stat);

				if(0 == cmgl.list[i].stat) {
					gsm_cmgr_t cmgr;
					memset(&cmgr, 0, sizeof(gsm_cmgr_t));
					cmgr.index = cmgl.list[i].index;

					if(0 == mapi_gsm_cmgr(&cmgr)) {
						BDI_DeliverMessageEvent * msgevent = NEW BDI_DeliverMessageEvent;

						if (msgevent != NULL) {
							struct ST_SMS_DELIVER pdu;
#if 0
							printf("pdu:\r\n");

							for(int n = 0; cmgr.content.pdu[n] != 0; n++) {
								printf("%c ", cmgr.content.pdu[n]);

								if(9 == (n%10))
									printf("\r\n");
							}

							printf("\r\n");
#endif

							if(!pdu_decode(cmgr.content.pdu, &pdu)) {
#if 0
								printf("smsc=[%s]\n", pdu.smsc);
								printf("fo=[%02X]\n", pdu.fo);
								printf("sender=[%s]\n", pdu.sender);
								printf("pid=[%02X]\n", pdu.tp_pid);
								printf("dcs=[%02X]\n", pdu.tp_dcs);
								printf("scts=[%s]\n", pdu.tp_scts);
								printf("udl=[%d]\n", pdu.tp_udl);
								printf("data:\r\n");

								for(int i = 0; i < pdu.tp_udl; i++) {
									printf("0x%02X ", pdu.tp_ud[i]);
								}

								printf("\r\n");
#endif
							} 
							else {
								fprintf(stderr, "PDU decode error!\r\n");
							}


							msgevent->setOrigAddr(0, 0, (const char *)pdu.sender);
							msgevent->setMessageType(BDI::MT_SMS);

							if(0x08 == pdu.tp_dcs) {
								uint16 d[pdu.tp_udl / 2];

								for (int i = 0; i < pdu.tp_udl; i += 2)
									d[i / 2] = pdu.tp_ud[i] * 256 + pdu.tp_ud[i + 1];

								msgevent->setText(BDI::Unicode, d, pdu.tp_udl/2);
								RB_DEBUG("+++%s", msgevent->toString().c_str());
								_d->rtdataList()->addEvent(msgevent);
							} else if(0x00 == pdu.tp_dcs) {
								uint16 d[pdu.tp_udl];

								for (int m = 0; m < pdu.tp_udl; m++)
									d[m] = pdu.tp_ud[m];

								msgevent->setText(BDI::Unicode, d, pdu.tp_udl);
								RB_DEBUG("+++%s", msgevent->toString().c_str());
								_d->rtdataList()->addEvent(msgevent);
							} else {
								RB_DEBUG("Unkown sms code");
							}
						}
					} else {
						RB_DEBUG("++++++++++++++++++++++++++++++++++++++++++++++mapi_gsm_cmgr error");
					}
				}
			}

			RB_DEBUG("BDI_MP_Thread|to delete sms");
			gsm_cmgd_t cmgd;
			cmgd.index = 1;
			cmgd.delflag = 4;
			mapi_gsm_cmgd(&cmgd);
		} else {
			RB_DEBUG("BDI_MP_Thread|mapi_gsm_cmgl error!,ret = %d", ret);
		}

		break;

	default:
		RB_DEBUG("mobile|Unhandled GSM event: 0x%x", phdr->event);
		break;
	}
}

void BDI_MP_Thread::onCdmaUrc(void *header, void *body)
{
	urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(header);

	switch (phdr->event) {
	case REQ_CDMA_RSSILVL:
		/*cdma_rssilvl_t *prssi = reinterpret_cast<cdma_rssilvl_t *>(body);
		_sigLevel = (prssi->rssi + 1) / 20;

		BDI_MSignalEvent *sig = NEW BDI_MSignalEvent(CELL_NSIGV, _sigLevel);
		if (sig != NULL) {
			RB_DEBUG("BDI_MP_Thread| %s", sig->toString().c_str());
			_d->rtdataList()->addEvent(sig);
		}*/
		break;

	case REQ_CDMA_RING:
		_status.setStat(MobileBusy);
		break;

	case REQ_CDMA_CLIPA:

		_status.setStat(MobileBusy);
		cdma_clipa_t *clipa;
		clipa = reinterpret_cast<cdma_clipa_t *>(body);

		char num[32];
		cleanPhoneNumber(clipa->number, num, 32);

		BDI_MCallEvent *callin = NEW BDI_MCallEvent(Mobile_CallingIn);

		if (callin != NULL) {
			callin->setNumber(num);
			RB_DEBUG("BDI_MP_Thread|+++ %s", callin->toString().c_str());
			_d->rtdataList()->addEvent(callin);
		}

		break;

	case REQ_CDMA_CONN: {
		RB_DEBUG("%s:%d", __FILE__, __LINE__);
		BDI_MCallEvent *callup = NULL;
		cdma_conn_t *conn;
		conn = reinterpret_cast<cdma_conn_t *>(body);

		if (0 == conn->call_type)
			callup = NEW BDI_MCallEvent(Mobile_CallingUp);

		if (callup != NULL) {
			callup->setCallingLineId(conn->call_x);
			RB_DEBUG("BDI_MP_Thread|+++ %s", callup->toString().c_str());
			_d->rtdataList()->addEvent(callup);
		}
	}
	break;

	case REQ_CDMA_CEND:

		BDI_MCallEvent *hungup = NEW BDI_MCallEvent(Mobile_Hungup);

		if (hungup != NULL) {
			RB_DEBUG("BDI_MP_Thread|+++ %s", hungup->toString().c_str());
			_d->rtdataList()->addEvent(hungup);
		}

		_status.setStat(MobileReady);
		if(NULL != _hungupEvent){
			DELETE(_hungupEvent);
			_hungupEvent = NULL;
		}
		break;

	case REQ_CDMA_CONNECT:
		//setStat(PPPConnected);
		break;

	case REQ_CDMA_PPPSTATE:
		cdma_pppstate_t *pppst = (cdma_pppstate_t *)body;
		RB_DEBUG("BDI_MP_Thread|cdma REQ_CDMA_PPPSTATE urc rssi = %d", pppst->val);

		if (5 == pppst->val) {
			_status.setStat(PPPConnected);
			BDI_MCallEvent *e = NEW BDI_MCallEvent(PPP_Up);

			if(NULL != e)
				_d->rtdataList()->addEvent(e);

			_pppErrorCount = 0;
		} else if(0 == pppst->val) {
			if(MobileBusy != _status.stat())
				_status.setStat(MobileReady);
		}

		if(0 <= pppst->val)
			break;

	case REQ_CDMA_CMTI:
		_status.setStat(MobileReady);
		RB_DEBUG("++++++++++++++++++++++++++++++++++++++++got a massage!!!++++++++++++++++++++++++++++++++++++++++");
		cdma_hcmgl_t cmgl;
		memset(&cmgl, 0, sizeof(cdma_hcmgl_t));
		cmgl.stat = 4;
		cmgl.list_cnt = 0;
		int ret = mapi_cdma_hcmgl(&cmgl);

		if(0 != ret) {
			RB_DEBUG("BDI_MP_Thread|mapi_cdma_hcmgl error ret = %d", ret);
		} else {
			for(int n = 0; n < cmgl.list_cnt; n++) {
				//RB_DEBUG("BDI_MP_Thread | ++++++++++++++++++++++list_cnt = %d", cmgl.list_cnt);
				//RB_DEBUG("index = %d, stat = %d", cmgl.list[n].index, cmgl.list[n].tag);

				if(0 == cmgl.list[n].tag) {
					cdma_hcmgr_t cmgr;
					memset(&cmgr, 0, sizeof(cdma_hcmgr_t));
					cmgr.index = cmgl.list[n].index;
					cmgr.mode = 0;
					int ret = mapi_cdma_hcmgr(&cmgr);

					if (0 != ret)
						break;

#if 0
					fprintf(stderr, "callerID:%s\r\n", cmgr.content.callerID);
					fprintf(stderr, "format:  %d\r\n", cmgr.content.format);
					fprintf(stderr, "time:    %d-%d-%d %d:%d:%d\r\n", cmgr.content.year, cmgr.content.month, cmgr.content.day, cmgr.content.hour, cmgr.content.minute, cmgr.content.second);
					fprintf(stderr, "length:  %d\r\n", cmgr.content.length);
					fprintf(stderr, "lang:    %d\r\n", cmgr.content.lang);
					fprintf(stderr, "prt:     %d\r\n", cmgr.content.prt);
					fprintf(stderr, "prv:     %d\r\n", cmgr.content.prv);
					fprintf(stderr, "type:    %d\r\n", cmgr.content.type);
					fprintf(stderr, "stat:    %d\r\n", cmgr.content.stat);
					fprintf(stderr, "msg:");

					for(int i = 0; i < cmgr.content.length; i++) {
						if(0 == i % 10)
							fprintf(stderr, "\r\n");

						fprintf(stderr, "%02x,", cmgr.content.msg[i]);
					}

					fprintf(stderr, "\r\n");
#endif

					BDI_DeliverMessageEvent * msgevent = NEW BDI_DeliverMessageEvent;

					if (msgevent != NULL) {
						msgevent->setOrigAddr(0, 0, cmgr.content.callerID);
						msgevent->setMessageType(BDI::MT_SMS);

						if(6 == cmgr.content.format) {
							uint16 d[cmgr.content.length / 2];

							for (int i = 0; i < cmgr.content.length; i += 2)
								d[i / 2] = cmgr.content.msg[i] * 256 + cmgr.content.msg[i + 1];

							msgevent->setText(BDI::Unicode, d, cmgr.content.length / 2);
							RB_DEBUG("+++%s", msgevent->toString().c_str());
							_d->rtdataList()->addEvent(msgevent);
						} else if(1 == cmgr.content.format) {//utf8
							uint16 d[cmgr.content.length];

							for (int i = 0; i < cmgr.content.length; i++)
								d[i] = cmgr.content.msg[i];

							msgevent->setText(BDI::Unicode, d, cmgr.content.length);
							RB_DEBUG("+++%s", msgevent->toString().c_str());
							_d->rtdataList()->addEvent(msgevent);
						} else {
							RB_WARNING("BDI_MP_Thread|Unknown message format! type = %d", cmgr.content.format);
						}

					}
				}

				cdma_cmgd_t cmgd;
				cmgd.index = cmgl.list[n].index;
				cmgd.delflag = 3;
				mapi_cdma_cmgd(&cmgd);
			}
		}

		break;

	default:
		RB_DEBUG("BDI_MP_Thread|Unhandled CDMA event: 0x%x", phdr->event);
		break;
	}

}

inline BDI_MP_Thread::ModuleStat BDI_MP_Thread::status() const
{
	return _status.stat();
}

inline BDI_MP_Thread::ModuleType BDI_MP_Thread::moduleType() const
{
	return _mtype;
}

void BDI_MP_Thread::setSigLevel(int l)
{
	_sigLevel = l;

	if(l > 1) {
		_sigSteadyCount++;
	} else {
		_sigSteadyCount = 0;
	}

	if(_sigSteadyCount >= 3) {
		_pppErrorCount = 0;
	}
}

char BDI_MP_Thread::sigLevel() const
{
	return _sigLevel;
}

void BDI_MP_Thread::printStat()
{
	switch(_status.stat()) {
	case MobileReady:
		printf("status == MobileReady\r\n");
		break;

	case MobileBusy:
		printf("status == MobileBusy\r\n");
		break;

	case PPPConnected:
		printf("status == PPPConnected\r\n");
		break;

	case PPPConnecting:
		printf("status == PPPConnecting\r\n");
		break;

	case PPPDisConnecting:
		printf("status == PPPDisConnecting\r\n");
		break;

	default:
		printf("Unknown status\r\n");
	}
}

BDI_MP_Event::BDI_MP_Event(BDI_MP_Thread * mpt)
	: BDI_Event(static_cast<BDI_Event::Type>(0))
{
	_mpt = mpt;
	_errCount = 0;
	_e = NULL;
}

BDI_MP_Event::~BDI_MP_Event()
{
}

int BDI_MP_Event::handle()
{
	return 1;
}

inline BDI_MP_Thread::ModuleStat BDI_MP_Event::stat() const
{
	return _st;
}

inline BDI_Event * BDI_MP_Event::retEvent()
{
	return _e;
}

BDI_MP_PPPEvent::BDI_MP_PPPEvent(BDI_MP_Thread * mpt)
	: BDI_MP_Event(mpt)
{
	_req = BDI_MP_PPPEvent::PPP_None;
}

BDI_MP_PPPEvent::~BDI_MP_PPPEvent()
{
}

void BDI_MP_PPPEvent::setEnable(bool req)
{
	if(req) {
		RB_DEBUG("BDI_MP_PPPEvent::setEnable|set to ppp_enable");
		_req = BDI_MP_PPPEvent::PPP_Enable;
		_errCount = 0;
	} else {
		RB_DEBUG("BDI_MP_PPPEvent::setEnable|set to ppp_disable");
		_req = BDI_MP_PPPEvent::PPP_Disable;
	}
}

int BDI_MP_PPPEvent::handle()
{
	_st = _mpt->status();

	if(PPP_None == _req)
		return 0;

	if(BDI_MP_Thread::MobileBusy == _st) {
		_req = PPP_None;
		return 1;
	}

	switch (_req) {
	case BDI_MP_PPPEvent::PPP_Enable:
		if(BDI_MP_Thread::PPPConnected == _st) {
			_req = BDI_MP_PPPEvent::PPP_None;
			return 1;
		}

		if(BDI_MP_Thread::MobileReady == _st) { //|| BDI_MP_Thread::PPPConnecting == _mpt->status()) {
			RB_DEBUG("+++++++++++++to up ppp++++++++");

			if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
				int ret;

				ret = mapi_cdma_pppup();

				if (0 == ret || _errCount > 3) {
					_st = BDI_MP_Thread::PPPConnecting;
					_req = BDI_MP_PPPEvent::PPP_None;
					return 1;
				} else {
					RB_WARNING("BDI_MP_Thread|mapi_cdma_pppup error, ret=%d", ret);
					_errCount++;
					return 0;
				}
			}

			if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
				int ret;
				gsm_pppup_t r;
				r.cid = 1;
				ret = mapi_gsm_pppup(&r);

				if (0 == ret || _errCount > 3) {
					_st = BDI_MP_Thread::PPPConnecting;
					_req = BDI_MP_PPPEvent::PPP_None;
					return 1;
				} else {
					RB_WARNING("BDI_MP_Thread|mapi_gsm error, ret=%d", ret);
					_errCount++;
					return 0;
				}
			}
		}

		break;

	case BDI_MP_PPPEvent::PPP_Disable:
		if(BDI_MP_Thread::MobileReady == _st) {
			_req = BDI_MP_PPPEvent::PPP_None;
			return 0;
		}

		if(BDI_MP_Thread::PPPConnected == _st) { // || BDI_MP_Thread::PPPConnecting == _mpt->status()) {
			RB_DEBUG("+++++++++++++to down ppp++++++++");

			if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
				int ret;

				ret = mapi_cdma_pppdown();

				if (ret < 0) {
					RB_WARNING("BDI_MP_Thread|mapi_cdma_pppdown error, ret=%d", ret);
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					_req = BDI_MP_PPPEvent::PPP_None;
					return 1;
				}
			}

			if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
				gsm_pppdown_t r;
				r.n = 0;
				int ret;
				ret = mapi_gsm_pppdown(&r);

				if(ret < 0) {
					RB_WARNING("BDI_MP_Thread|mapi_gsm_pppdown error, ret = %d", ret);
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					_req = BDI_MP_PPPEvent::PPP_None;
					return 1;
				}
			}
		}

		break;

	default:
		_req = BDI_MP_PPPEvent::PPP_None;
		break;
	}

	return 1;
}

BDI_MP_DialEvent::BDI_MP_DialEvent(BDI_MP_Thread * mpt, int lineId, const char * num, time_t t)
	: BDI_MP_Event(mpt)
{
	_t = t;
	_lineId = lineId;
	int len = strlen(num);
	_num = NEW char[len + 1];

	if(NULL == _num)
		RB_FATAL("BDI_MP_DialEvent| malloc error len = %d", len);

	memcpy(_num, num, len);
	_num[len] = '\0';
}

BDI_MP_DialEvent::~BDI_MP_DialEvent()
{
	if(NULL != _num)
		DELETE_ARR(_num);
}

int BDI_MP_DialEvent::handle()
{
	_st = _mpt->status();
	int ct = time(NULL);

	if(10 < (ct - _t)) {
		if(NULL != _e)
			DELETE(_e);

		_e = NEW BDI_MCallEvent(Mobile_DialErr);
		return 1;
	}

	if(BDI_MP_Thread::MobileBusy == _st && 0 == _lineId) {
		if(NULL != _e)
			DELETE(_e);

		_e = NEW BDI_MCallEvent(Mobile_DialErr);
		return 1;
	}

	if(BDI_MP_Thread::MobileReady == _st || (BDI_MP_Thread::MobileBusy == _st && _lineId != 0)) {
		if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
			int ret;

			if (0 == _lineId) {
				ret = mapi_cdma_cdv(_num);
			} else {
				cdma_dtmf_t r;
				r.call_x = _lineId;	//呼叫ID，取值范围1~9
				r.dtmf_digit = *_num;
				ret = mapi_cdma_dtmf(&r);
			}

			if(0 != ret) {
				if(_errCount > 2) {
					if(NULL != _e)
						DELETE(_e);

					_e = NEW BDI_MCallEvent(Mobile_DialErr);
					return 1;
				}

				RB_DEBUG("in dialevent dial error");
				_errCount++;
				return 0;
			}

			_st = BDI_MP_Thread::MobileBusy;

			if(NULL != _e)
				DELETE(_e);

			_e = NEW BDI_MCallEvent(Mobile_DialOk);
			RB_DEBUG("in dialevent dial ok");
			return 1;
		} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
			int ret;

			if (0 == _lineId) {
				ret = mapi_gsm_dial(_num);
			} else {
				ret = mapi_gsm_vts(_num[0]);
			}

			if(0 != ret) {
				if(_errCount > 2) {
					if(NULL != _e)
						DELETE(_e);

					_e = NEW BDI_MCallEvent(Mobile_DialErr);
					return 1;
				}

				_errCount++;
				RB_DEBUG("BDI_MP_Thread|mapi_gsm_dial error, errCount = %d", _errCount);
				return 0;
			}

			_st = BDI_MP_Thread::MobileBusy;

			if(NULL != _e)
				DELETE(_e);

			_e = NEW BDI_MCallEvent(Mobile_DialOk);
			return 1;
		} else {
			if(NULL != _e)
				DELETE(_e);

			_e = NEW BDI_MCallEvent(Mobile_DialErr);
			return 1;
		}
	} else {
		if(BDI_MP_Thread::PPPConnected == _st || BDI_MP_Thread::PPPConnecting == _st) {
			if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
				int ret;
				ret = mapi_cdma_pppdown();

				if(ret < 0) {
					RB_DEBUG("BDI_MP_DialEvent->handle()| mapi_cdma_pppdown() error");
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
				int ret;
				gsm_pppdown_t r;
				r.n = 0;
				ret = mapi_gsm_pppdown(&r);

				if(ret < 0) {
					RB_DEBUG("BDI_MP_Dialevent->handle()| mapi_gsm_pppdown() error");
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else {
				if(NULL != _e)
					DELETE(_e);

				_e = NEW BDI_MCallEvent(Mobile_DialErr);
				return 1;
			}
		}
	}

	return 0;
}

BDI_MP_AnswerEvent::BDI_MP_AnswerEvent(BDI_MP_Thread *mpt)
	:BDI_MP_Event(mpt)
{
}

BDI_MP_AnswerEvent::~BDI_MP_AnswerEvent()
{
}

int BDI_MP_AnswerEvent::handle()
{
	_st = _mpt->status();

	if(BDI_MP_Thread::MobileBusy != _st)
		return 1;

	int ret;

	if (BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
		ret = mapi_gsm_answer();
	} else if (BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
		ret = mapi_cdma_answer();
	}

	if(ret < 0) {
		RB_DEBUG("BDI_MP_AnswerEvetn->handle()| mapi_answer error!");
		return 0;
	} else
		return 1;
}

BDI_MP_HangupEvent::BDI_MP_HangupEvent(BDI_MP_Thread *mpt)
	:BDI_MP_Event(mpt)
{
}

BDI_MP_HangupEvent::~BDI_MP_HangupEvent()
{
}

int BDI_MP_HangupEvent::handle()
{
	_st = _mpt->status();

	if(BDI_MP_Thread::MobileBusy != _st) {
		//if(NULL != _e)
		//DELETE(_e);
		//_e = NEW BDI_MCallEvent(Mobile_Hungup);
		RB_DEBUG("in hangupevent but _st is not busy");
		return 1;
	}

	RB_DEBUG("in hangupevent but _st is busy");

	int ret = 0;

	if (BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
		ret = mapi_gsm_hangup();
	} else if (BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
		ret = mapi_cdma_chv();
	}

	if(ret <0) {
		RB_DEBUG("BDI_MP_HangupEvetn->handle()| mapi_hangup error!");
		return 0;
	} else
		return 1;
}

BDI_MP_SendMsgEvent::BDI_MP_SendMsgEvent(BDI_MP_Thread *mpt, const char * num, const char *unicodes, int len)
	: BDI_MP_Event(mpt)
{
	_len = len;
	int l = strlen(num);
	_num = NEW char[l + 1];

	if(NULL == _num)
		RB_FATAL("BDI_MP_SendMsgEvent| malloc error!");

	memcpy(_num, num, l);
	_num[l] = '\0';

	_unicodes = NEW char[len];

	if(NULL == _unicodes)
		RB_FATAL("BDI_MP_SendMsgEvent| malloc error!");

	memcpy(_unicodes, unicodes, len);
}

BDI_MP_SendMsgEvent::~BDI_MP_SendMsgEvent()
{
	if(NULL != _num)
		DELETE_ARR(_num);

	if(NULL != _unicodes)
		DELETE_ARR(_unicodes);
}

int BDI_MP_SendMsgEvent::handle()
{
	_st = _mpt->status();

	if(BDI_MP_Thread::MobileBusy == _st) {
		return 0;
	}

	if(BDI_MP_Thread::MobileReady == _st) {
		if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
#if 1  //Set Modue for unicode
			cdma_hsmsss_t set;
			set.ack = 0;
			set.prt = 0;
			set.fmt = 6;
			set.prv = 0;
			mapi_cdma_hsmsss(&set);
#endif
			cdma_hcmgs_t data;
			data.len = _len;
			strcpy(data.da, _num);
			memcpy(data.msg, _unicodes, _len);
			int ret;
			ret = mapi_cdma_hcmgs(&data);

			if(0 == ret) {
				return 1;
			} else {
				RB_DEBUG("BDI_MP_Thread|Send Message error !!");

				if(_errCount > 2)
					return 1;
				else
					_errCount++;

				return 0;
			}
		} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
			gsm_cmgs_t r;
			int ret;
			TAPI_SMS_PDU spdu;
			gsm_csca_t csca;

			memset(&r, 0, sizeof(gsm_cmgs_t));

			ret = mapi_gsm_cscaq(&csca);

			if(0 != ret) {
				RB_DEBUG("BDI_MP_Thread| mapi_gsm_cscaq error!");

				if(_errCount > 2)
					//TODO  Send error message to desktop
					return 1;
				else
					_errCount++;

				return 0;
			}

			char scab[32 +1];
			int s = -1;
			int e = -1;

			if('\"' == csca.sca[0]) {
				int i;

				for(i = 1; (i < 32) && ('\0' != csca.sca[i]) && ('\"' != csca.sca[i]); i++) {
					scab[i - 1] = csca.sca[i];
				}

				scab[i - 1] = '\0';
			} else {
				strcpy(scab, csca.sca);
			}

			ret = gsm_sms_text2pdu(scab, _num, _unicodes, _len, &spdu);

			if(0 != ret)
				RB_DEBUG("BDI_MP_Thread| spdu error");

			r.len = spdu.length;
			strcpy(r.msg, spdu.pdu);
			ret = mapi_gsm_cmgs(&r);

			if(0 != ret) {
				RB_DEBUG("BDI_MP_Thread| gsm send msg error");

				if(_errCount > 2)
					//TODO  Send error message to desktop
					return 1;
				else
					_errCount++;

				return 0;
			} else
				return 1;
		}
	} else {
		if(BDI_MP_Thread::PPPConnected == _st || BDI_MP_Thread::PPPConnecting == _st) {
			if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
				int ret;
				ret = mapi_cdma_pppdown();

				if(ret < 0) {
					RB_DEBUG("BDI_MP_SendMsgEvent->handle()| mapi_cdma_pppdown() error");
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
				int ret;
				gsm_pppdown_t r;
				r.n = 0;
				ret = mapi_gsm_pppdown(&r);

				if(ret < 0) {
					RB_DEBUG("BDI_MP_SendMsgEvent->handle()| mapi_gsm_pppdown() error");
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else
				return 0;
		}
	}

	return 0;
}

BDI_MP_GetSigEvent::BDI_MP_GetSigEvent(BDI_MP_Thread *mpt)
	: BDI_MP_Event(mpt)
{
}

BDI_MP_GetSigEvent::~BDI_MP_GetSigEvent()
{
}

int BDI_MP_GetSigEvent::handle()
{
	_st = _mpt->status();

	if(BDI_MP_Thread::MobileReady == _st) {
		char sig = 0;

		if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
			cdma_csq_t csq;
			int ret = mapi_cdma_csq(&csq);

			if(0 == ret) {
				sig = csq.rssi;
			} else {
				RB_DEBUG("BDI_MP_Thread|Send Message error !!");

				if(_errCount > 2)
					return 1;
				else
					_errCount++;

				return 0;
			}
		} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
			gsm_csq_t csq;
			int ret;

			ret = mapi_gsm_csq(&csq);

			if(0 == ret) {
				sig = csq.rssi;
			} else {
				RB_DEBUG("BDI_MP_Thread| mapi_gsm_cscaq error!");

				if(_errCount > 2)
					//TODO  Send error message to desktop
					return 1;
				else
					_errCount++;

				return 0;
			}
		}

		if(99 == sig) {
			sig = 0;
		} else if(31 <= sig) {
			sig = 5;
		} else {
			sig = (sig / 8) + 1;
		}

		RB_DEBUG("bdi_MP_GetSigEvent| sig = %d", sig);

		if(NULL != _e)
			DELETE(_e);

		_e = NEW BDI_MSignalEvent(CELL_NSIGV, sig);
		_mpt->setSigLevel(sig);
		return 1;
	} else {
		if(BDI_MP_Thread::PPPConnected == _st || BDI_MP_Thread::PPPConnecting == _st) {
			if(BDI_MP_Thread::MT_Cdma == _mpt->moduleType()) {
				int ret;
				ret = mapi_cdma_pppdown();

				if(ret < 0) {
					RB_DEBUG("BDI_MP_GetSigEvent->handle()| mapi_cdma_pppdown() error");
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else if(BDI_MP_Thread::MT_Gsm == _mpt->moduleType()) {
				int ret;
				gsm_pppdown_t r;
				r.n = 0;
				ret = mapi_gsm_pppdown(&r);

				if(ret < 0) {
					RB_DEBUG("BDI_MP_GetSigEvent->handle()| mapi_gsm_pppdown() error");
					return 0;
				} else {
					_st = BDI_MP_Thread::PPPDisConnecting;
					return 0;
				}
			} else
				return 0;
		}
	}

	return 0;
}

