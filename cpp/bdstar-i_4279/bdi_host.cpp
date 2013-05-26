#include <mapi/mapi.h>
#include <stdio.h>

#include "rbglobal.h"
#include "bdi_gps_thread.h"
#include "bdi_mp_thread.h"
#include "bdi_rtdata.h"
#include "bdi_message.h"
#include "bdi_daemon.h"
#include "bdi_imsg.h"
#include "bdi_host.h"
#include "config.h"

using namespace BDI;

// BDI_Host members

BDI_Host::BDI_Host(BDI_Daemon *d)
	: _d(d)
{
	_hostCode = 2;
}

BDI_Host::~BDI_Host()
{
}

#if defined(PRO_MF08A)
// BDI_MF08aHost members

BDI_Event *BDI_MF08aHost::onRequestSos(DBusMessage *msg)
{
	// get the arguments
	dbus_bool_t sosCanceled;
	dbus_int32_t sosType;
	const char *site = "";
	DBusError err;

	dbus_error_init(&err);

	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_BOOLEAN, &sosCanceled,
	                      DBUS_TYPE_INT32, &sosType,
	                      DBUS_TYPE_STRING, &site,
	                      DBUS_TYPE_INVALID);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08a-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		return NULL;
	}

	BDI_TermSosEvent *sose = NEW BDI_TermSosEvent;

	if (sose == NULL)
		return NULL;

	sose->setSn(getNextSerialNo());
	sose->setCanceled(sosCanceled);
	sose->setSosType(static_cast<BDI_TermSosEvent::SOSType>(sosType));
	sose->setDevName(_hostCode, 1, _d->devNumber());
	sose->setTermStatus(0xff, _d->gpsSateCount(), 0);
	sose->setSiteStr(site);
	sose->setPosition(_d->currentPos());
	return sose;
}

BDI_Event *BDI_MF08aHost::onRequestSendMessage(DBusMessage *msg, int *retval)
{
	// read the arguments
	char *number = NULL;
	dbus_uint16_t *codes = NULL;
	int nchars = 0;
	//DBusMessageIter args;
	DBusError err;

	*retval = 0;

	/*
	if (!dbus_message_iter_init(msg, &args)) {
	    *retval = SES_DBUS_FAULT;
	    return NULL;
	}

	dbus_message_iter_get_basic(&args, &number);
	dbus_message_iter_next(&args);
	dbus_message_iter_get_fixed_array(&args, &codes, &nchars);
	if (number == NULL || codes == NULL || nchars <= 0) {
	    *retval = SES_DBUS_FAULT;
	    return NULL;
	}
	*/
	dbus_error_init(&err);
	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_STRING, &number,
	                      DBUS_TYPE_ARRAY, DBUS_TYPE_UINT16, &codes, &nchars,
	                      DBUS_TYPE_INVALID);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08a-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		*retval = SES_DBUS_FAULT;
		return NULL;
	}

////////////////////////////////////////////////////////////////
	if (strlen(number) > 6) {
		if (0){//!_d->_mob_thread->running() || !_d->_mob_thread->ready()) {
			_d->trySwitchMobile();
			*retval = SES_REQUEST_UNEXEC;
			return NULL;
		} else {
			char *unibuf = NEW char[nchars * 2];

			if (unibuf == NULL) {
				*retval = SYS_SYSTEM_FAULT;
				return NULL;
			}

			char *p = unibuf;

			for (int i = 0; i < nchars; ++i) {
				*p++ = codes[i] / 256;
				*p++ = codes[i] % 256;
			}

			*retval = _d->_mp_thread->sendMsg(number, unibuf, p - unibuf);
			return NULL;
		}
	}

////////////////////////////////////////////////////////////////

	BDI_SubmitMessageEvent *me = NEW BDI_SubmitMessageEvent;

	if (me == NULL) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}

	me->setSn(getNextSerialNo());
	me->setPid(getNextPacketNo());

	// TODO: set destination name correctly
	me->setDestAddr(0x02, 0xff, number);
	me->setMessageType(0);
	me->setText(BDI::GB2312_SBC, codes, nchars);
	return me;
}

BDI_Event *BDI_MF08aHost::onRequestImportOutportReport(DBusMessage *msg)
{
	bool isImport = false;

	const char *memb = dbus_message_get_member(msg);

	if (strcmp(memb, MCAL_RTDATA_IMPORTREPORT) == 0)
		isImport = true;

	BDI_ImportOutportEvent *ioport = NEW BDI_ImportOutportEvent;

	if (ioport == NULL)
		return NULL;

	ioport->setSn(getNextSerialNo());
	ioport->setIsImport(isImport);
	ioport->setOrigAddr(_hostCode, 1, _d->devNumber());
	ioport->setPosition(_d->currentPos());
	return ioport;
}

void BDI_MF08aHost::onEventError(const BDI_ErrorEvent *e, DBusMessage *carrier, DBusConnection *conn)
{
	RB_ASSERT(conn != NULL);

	DBusMessage *sgnl = NULL;
	dbus_uint32_t serial = 0;
	dbus_uint32_t bid = e->bid();
	dbus_int32_t errCode = e->errorCode();

	if (carrier != NULL)
		serial = dbus_message_get_serial(carrier);

	// What type of event we process?
	switch (bid) {
	case ImportOutportEvent:
	case SubmitMessageEvent:
		break;

	default:
		return;
	}

	sgnl = dbus_message_new_signal(OBJPATH_BOI, INTF_RTDATA, SGNL_RTDATA_ERROR);

	if (sgnl == NULL)
		return;

	if (dbus_message_append_args(sgnl,
	                             DBUS_TYPE_UINT32, &serial,
	                             DBUS_TYPE_UINT32, &bid,
	                             DBUS_TYPE_INT32, &errCode,
	                             DBUS_TYPE_INVALID))
		dbus_connection_send(conn, sgnl, &serial);

	dbus_message_unref(sgnl);
}

BDI_MF08aHost::BDI_MF08aHost(BDI_Daemon *d)
	: BDI_Host(d)
{
	int t = mapi_get_urc_type();

	switch (t) {
	case 1:
		_hostCode = 0x12;
		break;

	case 2:
		_hostCode = 0x13;
		break;

	default:
		break;
	}

	loadPara();
}

BDI_MF08aHost::~BDI_MF08aHost()
{
}

BDI_EventRef BDI_MF08aHost::onRequest(DBusMessage *carrier, DBusConnection *conn)
{
	BDI_EventRef res;
	bool needReply = false;
	int retval = 0;

	RB_ASSERT(carrier != NULL);
	RB_ASSERT(conn != NULL);

	const char *intf = dbus_message_get_interface(carrier);
	const char *memb = dbus_message_get_member(carrier);

	if (intf == NULL || memb == NULL)
		return NULL;

	fprintf(stderr, "<BDI_MF08aHost::onRequest> [%s] [%s]\n", intf, memb);

	if (strcmp(memb, MCAL_RTDATA_SOS) == 0) {
		if (_d->gpsFixed())
			res = onRequestSos(carrier);
	} else if (strcmp(memb, MCAL_RTDATA_SENDMESSAGE) == 0) {
		needReply = true;
		res = onRequestSendMessage(carrier, &retval);
	} else if (strcmp(memb, MCAL_RTDATA_IMPORTREPORT) == 0
	           || strcmp(memb, MCAL_RTDATA_OUTPORTREPORT) == 0) {
		needReply = true;
		retval = 0;

		if (!_d->gpsFixed())
			retval = SES_REQUEST_UNEXEC;
		else {
			res = onRequestImportOutportReport(carrier);

			if (res.isNull())
				retval = SYS_SYSTEM_FAULT;
		}
	}

	if (needReply)
		imsg_error_reply(conn, carrier, retval);

	return res;
}

void BDI_MF08aHost::onEvent(BDI_EventRef e, DBusMessage *carrier, DBusConnection *conn)
{
	if (e->type() == BDI_Event::UserErrorEvent)
		onEventError(reinterpret_cast<BDI_ErrorEvent *>(e.getPtr()), carrier, conn);
	else if (e->bid() == BDI::DeliverMessageEvent){
		const BDI_DeliverMessageEvent *msg = reinterpret_cast<BDI_DeliverMessageEvent *>(e.getPtr());
#if 1
		const uint16 * text = msg->textCodes();
		RB_DEBUG("textcodec = %d, codes:", msg->textCodec());
		for(int i = 0; i < msg->textLength() * 2; i++){
			printf("0x%02x ", ((char *)text)[i]);
			if(!(i%9))
				printf("\r\n");
		}
		printf("\r\n");
		/*
		 * 短信设置报位参数的格式如下：
		 * $BDCGP:位置数据包报告频度,参考采样频度一,参考采样频度二,GPS定位频度,航向改变采样门限值,速度零值门限值,航行速度门限值,定位点区分门限值,
		 * 以$BDCGP:为开头，每个数据中间以逗号隔开，最后的逗号是必须的
		 */
		uint16 h[]={'$', 'B', 'D', 'C', 'G', 'P', ':'};
		if(0 == strncmp((const char *)h, (const char *)text, 14)){
			RB_DEBUG("======get a conf cmd========");
			char buf[msg->textLength() - 7 + 1];
			for(int i = 14; i < msg->textLength() * 2; i += 2){
				buf[(i - 14) / 2] = ((char *)text)[i];
			}
			buf[msg->textLength() - 7] = 0;
			char *h = buf;
			char *e = h;
			
			char data[8];
			int i;
			for(i = 0; i < 8 && (0 != *e); i++){
				data[i] = (char)strtol(h, &e, 10);
				if(0x2c != *e)
					break;
				h = e + 1;
				RB_DEBUG("data[%d] = %d", i, data[i]);
			}

			if(8 != i){
				RB_DEBUG("conf para err");
				char answer[] = {0, 'E', 0, 'r', 0, 'r', 0, 'o', 0, 'r'};
				_d->_mp_thread->sendMsg(msg->origAddr().number(), answer, 10);
				RB_DEBUG("send to :%s", msg->origAddr().number());
			}else{
				BDI::Config conf;
				conf.open(BDI_SYSPARA_FILE);
				conf.setGroup("SYSPARA");
				conf.setValue("ref_PosReport", data[0]); //位置数据包报告频度
				conf.setValue("ref_PosCol1", data[1]);//参考采样频度一
				conf.setValue("ref_PosCol2", data[2]);//参考采样频度二
				conf.setValue("ref_GpsFix", data[3]);//GPS定位频度
				conf.setValue("thv_course", data[4]);//航向改变采样门限值
				conf.setValue("thv_speedZero", data[5]);//速度零值门限值
				conf.setValue("thv_speedRun", data[6]);//航行速度门限值
				conf.setValue("thv_posDiff", data[7]);//定位点区分门限值//天天天天
				conf.save();
				_d->loadSettings();
				_d->_gps_thread->loadGpsColPara();
				RB_DEBUG("conf para ok");
				char answer[] = {0, 'O', 0, 'k'};
				_d->_mp_thread->sendMsg(msg->origAddr().number(), answer, 4);
				RB_DEBUG("send to :%s", msg->origAddr().number());
			}
		}
		else
#endif
			imsg_newmessageind(conn, msg);
	}
}

void BDI_MF08aHost::onGpsEvent(const BDI_Event *e, DBusConnection * dbusconn)
{
	static time_t lastReportTime = 0;
	static BDI_PosReportEvent *reportSave = NULL;

	if (e->bid() == GPS_Position) {
		const BDI_GpsPosEvent *gpe = reinterpret_cast<const BDI_GpsPosEvent *>(e);

		// GPS unfixed, broadcast this event
		if (!gpe->fixed()) {
			imsg_i32(dbusconn,
			         OBJPATH_GPOSITION, INTF_GPOS, SGNL_GPOS_GPSUNFIXED,
			         0);
			RB_DEBUG("GPS Unfix!");
			return;
		}

#if 0
		for (int i = 0; i < NR_MAXAREA; i++) {
			if(_areas[i].mask)
				continue;

			bdi_pt_t pt;
			pt.lon = int(gpe->gpos()._lon * 10000);
			pt.lat = int(gpe->gpos()._lat * 10000);
			int ret = pt_in_area(pt, _areas[i]);

			if((AW_IN == _areas[i].type && ret >= 0)
			        || (AW_OUT == _areas[i].type && ret <= 0)
			        || (AW_OP == _areas[i].type && ret > 0 && gpe->gpos()._speed > _thv_speedRun/10)) {

				if(0 == _area_st[i].isalarmst) {
					_area_st[i].isalarmst = 1;
					_area_st[i].time = gpe->gpos()._ts;
					_area_st[i].isreport = 1;
				} else {
					if(gpe->gpos()._ts - _area_st[i].time >= 60 * 10) {
						_area_st[i].time = gpe->gpos()._ts;
						_area_st[i].isreport = 1;
					} else {
						_area_st[i].isreport = 0;
					}
				}

				//TODO make submit event
				if(_area_st[i].isreport == 1)
					fprintf(stderr, "!!!!!!!!!!!!!!!!!!area[%d] waring!!!!!!!!!!!!!!!\r\n", i);
			} else {
				if(0 != _area_st[i].isalarmst) {
					_area_st[i].isalarmst = 0;
					_area_st[i].isreport = 0;
					//TODO make submit event
				}
			}
		}
#endif

		//TODO add submit event to list

		// Position report business
		if (gpe->marked()) {
			BDTermStatus termst;
			termst._power = 255;
			termst._sat1 = _d->gpsSateCount();

			//termst._cellSignal = _mob_thread->signalLevel();
			if (reportSave == NULL) {
				reportSave = NEW BDI_PosReportEvent;

				if (reportSave == NULL)
					return;

				reportSave->setSn(getNextSerialNo());
				reportSave->setDevName(0x12, 1, _d->devNumber());
			}

			reportSave->setTermStatus(termst);
			GPosition pos = gpe->gpos();
			pos._mobileSigLevel = _d->_mp_thread->sigLevel();
			reportSave->addPosition(gpe->gpos());

			if (reportSave->posCount() == BDI_PosReportEvent::MAX_POS_ITEMS
			        || gpe->gpos()._ts - lastReportTime > _ref_PosReport * 10 * 60) {
				_d->submitList()->addEvent(reportSave, NULL);
				lastReportTime = gpe->gpos()._ts;
				reportSave = NULL;
			}
		}

		// Save current position
		_d->setCurrentPos(gpe->gpos());

		// D-Bus broadcast
		imsg_gpos(dbusconn, gpe);
	} else if (e->bid() == GPS_SateView) {
		const BDI_GpsViewEvent *gve = reinterpret_cast<const BDI_GpsViewEvent *>(e);
		// D-Bus broadcast
		imsg_sateview(dbusconn, gve);
	}
}

int BDI_MF08aHost::loadPara()
{
	Config conf;

	if(!conf.open(BDI_SYSPARA_FILE)) {
		initParaConfFile();
	}

	if(conf.open(BDI_SYSPARA_FILE)) {
		conf.setGroup("SYSPARA");
		_ref_PosReport = int(conf.getIntValue("ref_PosReport"));
		_thv_speedRun = int(conf.getIntValue("thv_speedRun"));
	}

	if (0 != access(BDI_AREACONF_FILE, F_OK))
		initAreaConfFile();

	FILE *f = fopen(BDI_AREACONF_FILE, "r");

	if(NULL == f) {
		fprintf(stderr, "open data config file error [%s]\r\n", BDI_AREACONF_FILE);
		return -1;
	}

	bdi_area_t a;

	for(int i = 0; i < NR_MAXAREA; i++) {
		if(0 == fread(&a, sizeof(bdi_area_t), 1, f)) {
			fprintf(stderr, "read data config file error [%s]\r\n",BDI_AREACONF_FILE);
			break;
		}

		if(memcmp(&a, &(_areas[i]), sizeof(bdi_area_t)) == 0)
			continue;

		_areas[i] = a;
		bzero(&(_area_st[i]), sizeof(bdi_area_st));
	}
#if 0

	_areas[0].mask = 0;
	_areas[0].type = AW_OUT;
	_areas[0].npt = 5;
	_areas[0].pt[0].lat = int(38.363598 * 10000);
	_areas[0].pt[0].lon = int(118.290566 * 10000);

	_areas[0].pt[1].lat = int(40.093896 * 10000);
	_areas[0].pt[1].lon = int(121.031662 * 10000);

	_areas[0].pt[2].lat = int(39.100797 * 10000);
	_areas[0].pt[2].lon = int(121.090566 * 10000);

	_areas[0].pt[3].lat = int(38.290103 * 10000);
	_areas[0].pt[3].lon = int(121.025713 * 10000);

	_areas[0].pt[4].lat = int(37.263598 * 10000);
	_areas[0].pt[4].lon = int(119.315407 * 10000);

	_areas[1].mask = 0;
	_areas[1].type = AW_IN;
	_areas[1].npt = 5;
	_areas[1].pt[0].lat = int(38.363598 * 10000);
	_areas[1].pt[0].lon = int(118.290566 * 10000);

	_areas[1].pt[1].lat = int(40.093896 * 10000);
	_areas[1].pt[1].lon = int(121.031662 * 10000);

	_areas[1].pt[2].lat = int(39.100797 * 10000);
	_areas[1].pt[2].lon = int(121.090566 * 10000);

	_areas[1].pt[3].lat = int(38.290103 * 10000);
	_areas[1].pt[3].lon = int(121.025713 * 10000);

	_areas[1].pt[4].lat = int(37.263598 * 10000);
	_areas[1].pt[4].lon = int(119.315407 * 10000);

	_areas[2].mask = 0;
	_areas[2].type = AW_OUT;
	_areas[2].npt = 5;
	_areas[2].pt[0].lat = int(38.363598 * 10000);
	_areas[2].pt[0].lon = int(118.290566 * 10000);

	_areas[2].pt[1].lat = int(40.093896 * 10000);
	_areas[2].pt[1].lon = int(121.031662 * 10000);

	_areas[2].pt[2].lat = int(39.100797 * 10000);
	_areas[2].pt[2].lon = int(121.090566 * 10000);

	_areas[2].pt[3].lat = int(38.290103 * 10000);
	_areas[2].pt[3].lon = int(121.025713 * 10000);

	_areas[2].pt[4].lat = int(37.263598 * 10000);
	_areas[2].pt[4].lon = int(119.315407 * 10000);
#endif

	return 0;
}

#elif defined(PRO_MF08B)
// BDI_MF08bHost members

BDI_MF08bHost::BDI_MF08bHost(BDI_Daemon *d)
 : BDI_Host(d)
{
}

BDI_MF08bHost::~BDI_MF08bHost()
{
}

BDI_EventRef BDI_MF08bHost::onRequest(DBusMessage *carrier, DBusConnection *conn)
{
	BDI_EventRef res;
	bool needReply = false;
	int retval = 0;

	RB_ASSERT(carrier != NULL);
	RB_ASSERT(conn != NULL);

	const char *intf = dbus_message_get_interface(carrier);
	const char *memb = dbus_message_get_member(carrier);

	if (intf == NULL || memb == NULL)
		return NULL;

	fprintf(stderr, "<BDI_MF08bHost::onRequest> [%s] [%s]\n", intf, memb);

	if (strcmp(memb, MCAL_RTDATA_SENDMESSAGE) == 0) {
		needReply = true;
		res = onRequestSendMessage(carrier, &retval);
	}
	else if (strcmp(memb, MCAL_DLINK_REGISTER) == 0) {
		needReply = true;
		res = onRegister(carrier, &retval);
	}
	else if (strcmp(memb, MCAL_DLINK_SWITCHACCOUNT) == 0) {
		needReply = true;
		res = onSwitchAccount(carrier, &retval);
	}
	else if (strcmp(memb, MCAL_RTDATA_INVITETERM) == 0) {
		needReply = true;
		res = onInviteTerm(carrier, &retval);
	}

	if (needReply) {
		imsg_error_reply(conn, carrier, retval);
	}

	return res;
}

void BDI_MF08bHost::onEvent(BDI_EventRef e, DBusMessage *carrier, DBusConnection *conn)
{
	if (BDI_Event::UserErrorEvent == e->type()) {
		onEventError(reinterpret_cast<BDI_ErrorEvent *>(e.getPtr()), carrier, conn);
	}
	else if (BDI_Event::RegisterResp == e->type()) {
		const BDI_CYTRegisterResp *msg = reinterpret_cast<BDI_CYTRegisterResp *>(e.getPtr());
		imsg_register_resp(conn, msg);	
	}

	if (BDI::DeliverMessageEvent == e->bid()){
		const BDI_DeliverMessageEvent *msg = reinterpret_cast<BDI_DeliverMessageEvent *>(e.getPtr());
		imsg_newmessageind(conn, msg);
	}
	else if (BDI::TermPosReport == e->bid()) {
		const BDI_TermPosReportPush *msg = reinterpret_cast<BDI_TermPosReportPush *>(e.getPtr());
		imsg_termposreport(conn, msg);
	}
	else if (BDI::TermPosReply == e->bid()) {
		const BDI_TermPosReplyPush *msg = reinterpret_cast<BDI_TermPosReplyPush *>(e.getPtr());
		imsg_termposreply(conn, msg);
	}
	else if (BDI::TermListEvent == e->bid()) {
		//TODO
		//const BDI_TermListPush *msg = reinterpret_cast<BDI_TermListPush *>(e.getPtr());
	}
	else if (BDI::InviteTermJoinGroupReply == e->bid()) {
		const BDI_TermJoinPush *msg = reinterpret_cast<BDI_TermJoinPush *>(e.getPtr());
		imsg_invite_resp(conn, msg);
	}
	else if (BDI::TermSosEvent == e->bid()) {
		const BDI_TermSosPush *msg = reinterpret_cast<BDI_TermSosPush *>(e.getPtr());
		imsg_term_sos(conn, msg);
	}

}

void BDI_MF08bHost::onEventError(const BDI_ErrorEvent *e, DBusMessage *carrier, DBusConnection *conn)
{
	RB_ASSERT(conn != NULL);

	DBusMessage *sgnl = NULL;
	dbus_uint32_t serial = 0;
	dbus_uint32_t bid = e->bid();
	dbus_int32_t errCode = e->errorCode();

	if (carrier != NULL)
		serial = dbus_message_get_serial(carrier);

	// What type of event we process?
/*
	switch (bid) {
	case ImportOutportEvent:
	case SubmitMessageEvent:
		break;

	default:
		return;
	}
*/

	sgnl = dbus_message_new_signal(OBJPATH_BOI, INTF_RTDATA, SGNL_RTDATA_ERROR);

	if (sgnl == NULL)
		return;

	if (dbus_message_append_args(sgnl,
	                             DBUS_TYPE_UINT32, &serial,
	                             DBUS_TYPE_UINT32, &bid,
	                             DBUS_TYPE_INT32, &errCode,
	                             DBUS_TYPE_INVALID))
		dbus_connection_send(conn, sgnl, &serial);

	dbus_message_unref(sgnl);
}

BDI_Event *BDI_MF08bHost::onRequestSendMessage(DBusMessage *msg, int *retval)
{
	// read the arguments
	char *number = NULL;
	dbus_uint16_t *codes = NULL;
	int nchars = 0;
	//DBusMessageIter args;
	DBusError err;

	*retval = 0;

	dbus_error_init(&err);
	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_STRING, &number,
	                      DBUS_TYPE_ARRAY, DBUS_TYPE_UINT16, &codes, &nchars,
	                      DBUS_TYPE_INVALID);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08b-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		*retval = SES_DBUS_FAULT;
		return NULL;
	}

	int len = strlen(number);

	int type = 0;
	if( 6 == len) {
		type = 0x02;		//05, 07
	}
	else if( 11 == len ) {
		type = 0x20;		//08a, cellphone
	}

	//as sms
	if (strlen(number) < 6 || strlen(number) > 8) {
		if( nchars > 70 )
			nchars = 70;
		char *unibuf = NEW char[nchars * 2];

		if (unibuf == NULL) {
			*retval = SYS_SYSTEM_FAULT;
			return NULL;
		}

		char *p = unibuf;

		for (int i = 0; i < nchars; ++i) {
			*p++ = codes[i] / 256;
			*p++ = codes[i] % 256;
		}

		*retval = _d->_mp_thread->sendMsg(number, unibuf, p - unibuf);
		return NULL;
	}




	BDI_SubmitMessageEvent *me = NEW BDI_SubmitMessageEvent;

	if (me == NULL) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}

	me->setSn(getNextSerialNo());
	me->setPid(getNextPacketNo());
	me->setOrigAddr(_hostCode, 1, _d->devNumber());
	// TODO: set destination name correctly

	me->setDestAddr(type, 0x00, number);
	me->setMessageType(0);
	me->setText(BDI::GB2312_SBC, codes, nchars);
	return me;
}

BDI_Event *BDI_MF08bHost::onRegister(DBusMessage *msg, int *retval)
{
	// read the arguments
	char *name = NULL;
	char *pwd = NULL;
	int idtype;

	char *idnum = NULL;
	char *mobile = NULL;
	char *tel = NULL;
	char *addr = NULL;
	char *zip = NULL;
	char *email = NULL;


	//dbus_uint16_t *codes = NULL;
	//int nchars = 0;
	//DBusMessageIter args;
	DBusError err;
	*retval = 0;

	dbus_error_init(&err);
	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_STRING, &name,
	                      DBUS_TYPE_STRING, &pwd,
	                      DBUS_TYPE_INT32, &idtype,
	                      DBUS_TYPE_STRING, &idnum,
	                      DBUS_TYPE_STRING, &mobile,
	                      DBUS_TYPE_STRING, &tel,
	                      DBUS_TYPE_STRING, &addr,
	                      DBUS_TYPE_STRING, &zip,
	                      DBUS_TYPE_STRING, &email,
	                      DBUS_TYPE_INVALID);

	printf("onRegister:%s,%s,%d,%s\r\n", name, pwd, idtype, idnum);
	printf("onRegister:%s,%s,%s,%s,%s\r\n", mobile, tel, addr, zip, email);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08b-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		*retval = SES_DBUS_FAULT;
		return NULL;
	}

	BDI_CYTRegister *re = NEW BDI_CYTRegister;
	if (re == NULL) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}

	re->setUsrName(name);
	re->setPwd(pwd);
	re->setIdType(idtype);
	re->setIdNum(idnum);

	re->setMobileNum(mobile);
	re->setTelNum(tel);
	re->setHomeAdd(addr);
	re->setZipCode(zip);
	re->setEMail(email);

	re->setCreateTime(time(NULL));
	re->setAccountStatus(0x00);		//initial register
	re->setPhotoLen(0);			//no photo

	return re;
}

BDI_Event *BDI_MF08bHost::onSwitchAccount(DBusMessage *msg, int *retval)
{
	// read the arguments
	char *name = NULL;
	char *pwd = NULL;

	//dbus_uint16_t *codes = NULL;
	//int nchars = 0;
	//DBusMessageIter args;

	DBusError err;

	*retval = 0;

	dbus_error_init(&err);
	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_STRING, &name,
	                      DBUS_TYPE_STRING, &pwd,
	                      DBUS_TYPE_INVALID);
	printf("onSwitchAccount:%s,%s\r\n", name, pwd);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08b-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		*retval = SES_DBUS_FAULT;
		return NULL;
	}
	
	if (0 != _d->setUserName(name)) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}
	if (0 != _d->setPassWord(pwd)) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}

	_d->saveAccount();
	
	if(!_d->_rtd_thread->running()){
		printf("rtd_thread stop, restart it\r\n");
		_d->_rtd_thread->start();
	}

	return NULL;
}

BDI_Event * BDI_MF08bHost::onInviteTerm(DBusMessage *msg, int *retval)
{
	// read the arguments
	char *num = NULL;

	//DBusMessageIter args;
	DBusError err;
	*retval = 0;

	dbus_error_init(&err);
	dbus_message_get_args(msg, &err,
	                      DBUS_TYPE_STRING, &num,
	                      DBUS_TYPE_INVALID);

	if (dbus_error_is_set(&err)) {
		RB_WARNING("mf08b-host|dbus get args error (%s)", err.message);
		dbus_error_free(&err);
		*retval = SES_DBUS_FAULT;
		return NULL;
	}

	BDI_InviteSubmit *ise = NEW BDI_InviteSubmit;
	if (ise == NULL) {
		*retval = SYS_SYSTEM_FAULT;
		return NULL;
	}
	int len = strlen(num);

	int type = 0;
	if( 6 == len) {
		type = 0x02;		//05, 07
	}
	else if( 11 == len ) {
		type = 0x20;		//08a
	}
	printf("onInviteTerm:: %s\r\n", num);

	BDDevName dest;
	dest.setDevName(type, 0, num);		//TODO addr style not use yet
	ise->setDestAddr(&dest);
	ise->setJoin(true);
	ise->setGroupType(0x01);			//pos share group
	ise->setGroupNum(0x00);				//0 default
	ise->setGroupName("pos_group");		

	return ise;
}
#endif
