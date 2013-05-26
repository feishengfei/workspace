#ifndef BDI_IMSG_H
#define BDI_IMSG_H

#include <dbus/dbus.h>

class BDI_GpsPosEvent;
class BDI_GpsViewEvent;
class BDI_MCallEvent;
class BDI_DeliverMessageEvent;
class BDI_PosReportEvent;
class BDI_CYTRegisterResp;
class BDI_TermJoinPush;
class BDI_TermPosReportPush;
class BDI_TermPosReplyPush;
class BDI_TermSosPush;

namespace BDI
{
    const char BUSNAME_BOI[] = "org.bdstar.BoI";

    const char OBJPATH_BOI[] = "/org/bdstar/BoI";
    const char OBJPATH_GPOSITION[] = "/org/bdstar/GPosition";
    const char OBJPATH_MOBILE[] = "/org/bdstar/Mobile";

    const char INTF_DLINK[] = "org.bdstar.BoI.DataLink";
    const char SGNL_DLINK_STATUSCHANGED[] = "StatusChanged"; // "i"
    const char MCAL_DLINK_TRYTOCLOSE[] = "TryToClose"; // no args
    const char MCAL_DLINK_REGISTER[] = "Register"; // "ss"
    const char MCAL_DLINK_SWITCHACCOUNT[] = "SwitchAccount"; // "ss"
    const char SGNL_DLINK_REGISTERRESP[] = "RegisterResp"; // iisi

    const char INTF_MOBILE[] = "org.bdstar.Mobile";
    const char SGNL_MOBILE_SIGNALCHANGED[] = "SignalChanged"; // "i"
    const char SGNL_MOBILE_CALLINGIN[] = "CallingIn"; // "s"
    const char SGNL_MOBILE_CALLINGUP[] = "CallingUp"; // "i"
    const char SGNL_MOBILE_HUNGUP[] = "Hungup"; // no args
    const char SGNL_MOBILE_DIALOK[] = "DialOk"; // no args
    const char SGNL_MOBILE_DIALERR[] = "DialErr"; // no args
    const char MCAL_MOBILE_GET_MODULETYPE[] = "GetModuleType";
    const char MCAL_MOBILE_DIAL[] = "Dial"; // "is"
    const char MCAL_MOBILE_ANSWER[] = "Answer"; // no args
    const char MCAL_MOBILE_HANGUP[] = "Hangup"; // no args

    const char INTF_GPOS[] = "org.bdstar.GPosition";
    const char SGNL_GPOS_GPSUNFIXED[] = "GpsUnfixed"; // no args
    const char SGNL_GPOS_POSITION[] = "Position"; // "iiiii"
    const char SGNL_GPOS_SATEVIEW[] = "SateView"; // "ia(iiii)"

    const char INTF_RTDATA[] = "org.bdstar.BoI.RTData";
    const char SGNL_RTDATA_ERROR[] = "Error"; // "uui"
    const char SGNL_RTDATA_LONGIN_STATUSCHANGED[] = "LoginStatusChanged"; // "i"
    const char SGNL_RTDATA_NEWMESSAGEIND[] = "NewMessageInd"; // "isiaq"
    const char SGNL_RTDATA_TERMPOSREPORT[] = "TermPosReport"; // "iis i a(iiiiii)"
    const char SGNL_RTDATA_TERMPOSREPLY[] = "TermPosReply"; // "iis iiiiii"
    const char SGNL_RTDATA_INVITEREPLY[] = "InviteReply"; // "iis ii"
    const char SGNL_RTDATA_TERMSOS[] = "TermSos"; // iis i i a(s) a(iiiiii) i a(s)
    const char MCAL_RTDATA_SOS[] = "Sos"; // "bis"
    const char MCAL_RTDATA_SENDMESSAGE[] = "SendMessage"; // "saq"
    const char MCAL_RTDATA_IMPORTREPORT[] = "ImportReport";
    const char MCAL_RTDATA_OUTPORTREPORT[] = "OutportReport";
    const char MCAL_RTDATA_INVITETERM[] = "RTDC_CYTInviteSTJoinToGroup";

    dbus_uint32_t imsg_i32(DBusConnection *, 
                           const char *path, 
                           const char *intf, 
                           const char *memb, 
                           int v);

    dbus_uint32_t imsg_error_reply(DBusConnection *, DBusMessage *req, int errCode);

    // Type Signature: "iiiii"
    //   Longitude in degree * 1000000
    //   Latitude in degree * 1000000
    //   Speed in Km/h * 100
    //   Course in degree
    //   Altitude in meter
    dbus_uint32_t imsg_gpos(DBusConnection *, const BDI_GpsPosEvent *);

    // Type Signature: "ia(iiii)"
    //   Number of satellites
    //   Array of satellite infomation structure:
    //     Satellite ID
    //     Elevation in degree
    //     Azimuth in arc degree
    //     Signal to noise ratio in dB 
    dbus_uint32_t imsg_sateview(DBusConnection *, const BDI_GpsViewEvent *);

    dbus_uint32_t imsg_mobile_call(DBusConnection *, const BDI_MCallEvent *);

    dbus_uint32_t imsg_newmessageind(DBusConnection *, const BDI_DeliverMessageEvent *);

	dbus_uint32_t imsg_termposreport(DBusConnection *, const BDI_TermPosReportPush *); 

	dbus_uint32_t imsg_termposreply(DBusConnection *, const BDI_TermPosReplyPush *); 

    dbus_uint32_t imsg_register_resp(DBusConnection *, const BDI_CYTRegisterResp *);

    dbus_uint32_t imsg_invite_resp(DBusConnection *, const BDI_TermJoinPush *);

    dbus_uint32_t imsg_term_sos(DBusConnection *, const BDI_TermSosPush *);
};

#endif
