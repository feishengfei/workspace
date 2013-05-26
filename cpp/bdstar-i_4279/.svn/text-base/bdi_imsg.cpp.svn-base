#include "bdi_gps_event.h"
#include "bdi_mobile_event.h"
#include "bdi_message.h"
#include "bdi_imsg.h"

using namespace BDI;

static dbus_uint32_t g_serial = 0;

dbus_uint32_t BDI::imsg_i32(DBusConnection *conn, 
                            const char *path, 
                            const char *intf, 
                            const char *memb, 
                            int v)
{
    dbus_uint32_t ret = 0;
    dbus_int32_t i32 = static_cast<dbus_int32_t>(v);
    DBusMessage *msg = dbus_message_new_signal(path, intf, memb);
    if (msg == NULL)
        return 0;

    if (dbus_message_append_args(msg, 
                    DBUS_TYPE_INT32, &i32, 
                    DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, msg, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_error_reply(DBusConnection *conn, DBusMessage *req, int errCode)
{
    dbus_uint32_t ret = 0;
    dbus_int32_t val = errCode;
    DBusMessage *reply = dbus_message_new_method_return(req);
    if (reply == NULL)
        return 0;

    if (dbus_message_append_args(reply, 
                        DBUS_TYPE_INT32, &val, 
                        DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, reply, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(reply);
    return ret;
}

dbus_uint32_t BDI::imsg_gpos(DBusConnection *conn, const BDI_GpsPosEvent *gpe)
{
    DBusMessageIter args;
    dbus_int32_t i32;
    dbus_uint32_t ret = 0;

    DBusMessage *msg = dbus_message_new_signal(OBJPATH_GPOSITION, 
                                  INTF_GPOS, 
                                  SGNL_GPOS_POSITION);
    if (msg == NULL)
        return 0;

    dbus_message_iter_init_append(msg, &args);

    i32 = static_cast<dbus_int32_t>(gpe->gpos()._lon * 1000000);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
    i32 = static_cast<dbus_int32_t>(gpe->gpos()._lat * 1000000);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
    i32 = static_cast<dbus_int32_t>(gpe->gpos()._speed * 100);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
    i32 = static_cast<dbus_int32_t>(gpe->gpos()._course);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
    i32 = static_cast<dbus_int32_t>(gpe->gpos()._alt);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
    dbus_connection_send(conn, msg, &g_serial);
    ret = g_serial;

END:
    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_sateview(DBusConnection *conn, const BDI_GpsViewEvent *gve)
{
    DBusMessageIter args;
    dbus_int32_t i32;
    dbus_uint32_t ret = 0;
    int nsates = gve->sateCount();

    if (nsates <= 0)
        return 0;

    DBusMessage *msg = dbus_message_new_signal(OBJPATH_GPOSITION, 
                                               INTF_GPOS, 
                                               SGNL_GPOS_SATEVIEW);
    if (msg == NULL)
        return 0;

    // get the active satellite count
    BDI_GpsViewEvent::Iterator it = gve->begin();
    int nactvs = 0;
    for (; it != gve->end(); ++it)
        //if (it->sa_snr != 0)
            ++nactvs;

    dbus_message_iter_init_append(msg, &args);

    i32 = static_cast<dbus_int32_t>(nactvs);
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;

    for (it = gve->begin(); it != gve->end(); ++it) {
        //if (it->sa_snr != 0) {
       i32 = static_cast<dbus_int32_t>(it->sa_id);
       if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
            goto END;
        i32 = static_cast<dbus_int32_t>(it->sa_elevation);
        if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
            goto END;
        i32 = static_cast<dbus_int32_t>(it->sa_azimuth);
        if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
            goto END;
        i32 = static_cast<dbus_int32_t>(it->sa_snr);
        if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
            goto END;
        //}
    }
    dbus_connection_send(conn, msg, &g_serial);
    ret = g_serial;

END:
    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_mobile_call(DBusConnection *conn, const BDI_MCallEvent *calle)
{
    DBusMessage *msg = NULL;
    dbus_uint32_t ret = 0;

    if (calle->bid() == Mobile_CallingIn) {
        msg = dbus_message_new_signal(OBJPATH_MOBILE, 
                                      INTF_MOBILE, 
                                      SGNL_MOBILE_CALLINGIN);
        if (msg == NULL)
            return 0;
        const char *number = calle->number();
        if (dbus_message_append_args(msg, 
                                     DBUS_TYPE_STRING, &number, 
                                     DBUS_TYPE_INVALID)) {
            dbus_connection_send(conn, msg, &g_serial);
            ret = g_serial;
        }
        dbus_message_unref(msg);
    }
    else if (calle->bid() == Mobile_CallingUp) {
        ret = imsg_i32(conn, 
                       OBJPATH_MOBILE, 
                       INTF_MOBILE, 
                       SGNL_MOBILE_CALLINGUP, 
                       calle->callingLineId());
    }
    else if (calle->bid() == Mobile_Hungup) {
        ret = imsg_i32(conn, 
                       OBJPATH_MOBILE, 
                       INTF_MOBILE, 
                       SGNL_MOBILE_HUNGUP, 
                       0);
    }
	else if (calle->bid() == Mobile_DialOk){
        ret = imsg_i32(conn, 
                       OBJPATH_MOBILE, 
                       INTF_MOBILE, 
                       SGNL_MOBILE_DIALOK, 
                       0);
	}
	else if (calle->bid() == Mobile_DialErr){
        ret = imsg_i32(conn, 
                       OBJPATH_MOBILE, 
                       INTF_MOBILE, 
                       SGNL_MOBILE_DIALERR, 
                       0);
	}

    return ret;
}

dbus_uint32_t BDI::imsg_newmessageind(DBusConnection *conn, const BDI_DeliverMessageEvent *me)
{
    dbus_uint32_t ret = 0;
    dbus_int32_t mt = me->messageType();
    dbus_int32_t codec = me->textCodec();
    const char *number = me->origAddr().number();
    const dbus_uint16_t *codes = me->textCodes();
    dbus_uint32_t ts = static_cast<dbus_uint32_t>(me->ts());

    DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
                                INTF_RTDATA, 
                                SGNL_RTDATA_NEWMESSAGEIND);
    if (msg == NULL || codes == NULL)
        return 0;

    if (dbus_message_append_args(msg, 
    				DBUS_TYPE_UINT32, &ts,
                    DBUS_TYPE_UINT32, &mt, 
                    DBUS_TYPE_STRING, &number, 
                    DBUS_TYPE_UINT32, &codec, 
                    DBUS_TYPE_ARRAY, DBUS_TYPE_UINT16, &codes, me->textLength(), 
                    DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, msg, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_termposreport(DBusConnection *conn, const BDI_TermPosReportPush *pose)
{
    dbus_uint32_t ret = 0;
    DBusMessageIter args;
    dbus_int32_t i32;

    DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
                                INTF_RTDATA, 
                                SGNL_RTDATA_TERMPOSREPORT);
    if (msg == NULL){
        return 0;
    }
    dbus_message_iter_init_append(msg, &args);

	//BDDevName                                
	i32 = pose->origAddr().type();
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
	i32 = pose->origAddr().style();
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
	const char *devNum = pose->origAddr().number();
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_STRING, &devNum))
        goto END;
	//BDTermStatus TODO	undefined unused

	//GPosition
	i32 = pose->posCount();
    if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
        goto END;
	const GPosition *posp = pose->gposArray();
	for (int i = 0; i < pose->posCount(); i++, posp++) {
		i32 = static_cast<dbus_int32_t>(posp->_lon * 1000000);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
		i32 = static_cast<dbus_int32_t>(posp->_lat * 1000000);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
		i32 = static_cast<dbus_int32_t>(posp->_speed * 100);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
		i32 = static_cast<dbus_int32_t>(posp->_course);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
		i32 = static_cast<dbus_int32_t>(posp->_alt);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
		i32 = static_cast<dbus_int32_t>(posp->_ts);
		if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
			goto END;
	}

    dbus_connection_send(conn, msg, &g_serial);
    ret = g_serial;
END:
    dbus_message_unref(msg);
	return ret;
}

dbus_uint32_t BDI::imsg_termposreply(DBusConnection *conn, const BDI_TermPosReplyPush *pose)
{
	dbus_uint32_t ret = 0;
	DBusMessageIter args;
	dbus_int32_t i32;

	DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
			INTF_RTDATA, 
			SGNL_RTDATA_TERMPOSREPLY);
	if (msg == NULL){
		return 0;
	}
	dbus_message_iter_init_append(msg, &args);

	//pos
	GPosition pos = pose->gpos();

	//BDDevName                                
	i32 = pose->origAddr().type();
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = pose->origAddr().style();
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	const char *devNum = pose->origAddr().number();
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_STRING, &devNum))
		goto END;
	//BDTermStatus TODO	undefined unused

	//GPosition
	i32 = static_cast<dbus_int32_t>(pos._lon * 1000000);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = static_cast<dbus_int32_t>(pos._lat * 1000000);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = static_cast<dbus_int32_t>(pos._speed * 100);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = static_cast<dbus_int32_t>(pos._course);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = static_cast<dbus_int32_t>(pos._alt);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;
	i32 = static_cast<dbus_int32_t>(pos._ts);
	if (!dbus_message_iter_append_basic(&args, DBUS_TYPE_INT32, &i32))
		goto END;

	dbus_connection_send(conn, msg, &g_serial);
	ret = g_serial;
END:
	dbus_message_unref(msg);
	return ret;
}


dbus_uint32_t BDI::imsg_register_resp(DBusConnection *conn, const BDI_CYTRegisterResp *respe)
{
    dbus_uint32_t errCode = respe->errCode();
    dbus_uint32_t id = respe->id();
    const char *account = respe->account();
    dbus_uint32_t status = respe->status();

    dbus_uint32_t ret = 0;
    DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
                                INTF_RTDATA, 
                                SGNL_DLINK_REGISTERRESP);
    if (msg == NULL) {
        return 0;
    }

    if (dbus_message_append_args(msg, 
                    DBUS_TYPE_UINT32, &errCode, 
                    DBUS_TYPE_UINT32, &id, 
                    DBUS_TYPE_STRING, &account, 
                    DBUS_TYPE_UINT32, &status, 
                    DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, msg, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_invite_resp(DBusConnection *conn, const BDI_TermJoinPush *joine)
{
	dbus_uint32_t addrType = joine->origAddr().type();
	dbus_uint32_t addrStyle = joine->origAddr().style();
	const char * number = joine->origAddr().number();
	dbus_uint32_t agree = joine->isAgree() ? 0x01 : 0x02;

    dbus_uint32_t ret = 0;
    DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
                                INTF_RTDATA, 
                                SGNL_RTDATA_INVITEREPLY);

    if (msg == NULL) {
        return 0;
    }

    if (dbus_message_append_args(msg, 
                    DBUS_TYPE_UINT32, &addrType, 
                    DBUS_TYPE_UINT32, &addrStyle, 
                    DBUS_TYPE_STRING, &number, 
                    DBUS_TYPE_UINT32, &agree, 
                    DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, msg, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(msg);
    return ret;
}

dbus_uint32_t BDI::imsg_term_sos(DBusConnection *conn, const BDI_TermSosPush *sose)
{
    dbus_uint32_t ret = 0;
    DBusMessageIter args;

    DBusMessage *msg = dbus_message_new_signal(OBJPATH_BOI, 
                                INTF_RTDATA, 
                                SGNL_RTDATA_TERMSOS);
    if (msg == NULL){
        return 0;
    }
    dbus_message_iter_init_append(msg, &args);


	//BDDevName                                
	dbus_uint32_t type  = sose->origAddr().type();
	dbus_uint32_t style = sose->origAddr().style();
	const char *devNum = sose->origAddr().number();

	//BDTermStatus TODO	undefined unused

	//SendTime
	dbus_uint32_t s_ts = static_cast<dbus_uint32_t>(sose->ts());

	//sosType
	dbus_uint32_t sos_type = static_cast<dbus_uint32_t>(sose->sosType());

	//Region
	const char *site = sose->siteStr();
	unsigned int site_nbytes = strlen(site);
	int site_nchars = site_nbytes / 2;
	dbus_uint16_t *siteu16 = new dbus_uint16_t[site_nchars];


	const char *p = site;
	for (int i = 0; i < site_nchars; ++i) {
		siteu16[i] = (static_cast<uint16>(*p) * 256) + *(p + 1);
		p += 2;
	}


	//GPosition
	GPosition pos = sose->gpos();
	dbus_int32_t lon = static_cast<dbus_int32_t>(pos._lon * 1000000);
	dbus_int32_t lat = static_cast<dbus_int32_t>(pos._lat * 1000000); 
	dbus_int32_t speed = static_cast<dbus_int32_t>(pos._speed * 100);
	dbus_int32_t course= static_cast<dbus_int32_t>(pos._course);
	dbus_int32_t alt = static_cast<dbus_int32_t>(pos._alt);
	dbus_uint32_t ts = static_cast<dbus_uint32_t>(pos._ts);

	//Msg codec len codes
	dbus_uint32_t codec = static_cast<dbus_uint32_t>(sose->textCodec());
    const dbus_uint16_t *codes = sose->textCodes();

    if (dbus_message_append_args(msg, 
    				//BDDevName
    				DBUS_TYPE_UINT32, &type,
    				DBUS_TYPE_UINT32, &style,
                    DBUS_TYPE_STRING, &devNum, 	

					//BDTermStatus TODO	undefined unused

					//SendTime
    				DBUS_TYPE_UINT32, &s_ts,	

    				//SosType
					DBUS_TYPE_UINT32, &sos_type,

					//Region TODO
                    DBUS_TYPE_ARRAY, DBUS_TYPE_UINT16, &siteu16, site_nchars,

					//GPosition
    				DBUS_TYPE_INT32, &lon,
    				DBUS_TYPE_INT32, &lat,
    				DBUS_TYPE_INT32, &speed,
    				DBUS_TYPE_INT32, &course,
    				DBUS_TYPE_INT32, &alt,
                    DBUS_TYPE_UINT32, &ts, 

					//Msg codec len codes
                    DBUS_TYPE_UINT32, &codec, 
                    DBUS_TYPE_ARRAY, DBUS_TYPE_UINT16, &codes, sose->textLength(),
                    DBUS_TYPE_INVALID)) {
        dbus_connection_send(conn, msg, &g_serial);
        ret = g_serial;
    }

    dbus_message_unref(msg);

	delete[] siteu16;
	return ret;
}
