#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>
#include <pthread.h>

#include "config.h"
#include "debug_alloc.h"
#include "rbglobal.h"
#include "bdi_util.h"
#include "bdi_event_list.h"
#include "bdi_thread.h"
#include "bdi_mp_thread.h"
#include "bdi_gps_thread.h"
#include "bdi_imsg.h"
#include "bdi_host.h"
#include "bdi_daemon.h"

using namespace BDI;

#ifdef arm
static const char *BDI_CONF_FILE = "/mnt/user/config/bdstar-i.conf";
#else
static const char *BDI_CONF_FILE = "bdstar-i.conf";
#endif

static pthread_mutex_t usr_mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_mutex_t pwd_mutex = PTHREAD_MUTEX_INITIALIZER;

static bool g_runEnabled = true;
/*
// BDI_BusinessSwitch

#define SW_BASE_PERIOD 30
#define SWD_TIMES 20
#define SWM_TIMES 3

class BDI_BusinessSwitch
{
public:
    enum SwitchMode { Auto, ToData, ToMobile };
    enum BusinessType { DataBusiness, MobileBusiness };

private:
    BDI_Daemon *_d;
    time_t _t0; // Channel begin time
    time_t _t1; // Expected end time
    time_t _t2; // Actual end time, may later than _t0
    BusinessType _curBnss; // Current channel

    int _curTimeZone;
    int _nextSwitchZone;

private:
    bool tryExpand();
    void onBusinessBegin(BusinessType type);

public:
    BDI_BusinessSwitch(BDI_Daemon *d);

    void reset();

    bool autoSwitch();
    bool trySwitchData();
    bool trySwitchMobile();
};

bool BDI_BusinessSwitch::tryExpand()
{
    bool enabled = false;
    if (_t2 == _t1) {
        enabled = true;
        _t2 += SW_BASE_PERIOD;
    }
    return enabled;
}

void BDI_BusinessSwitch::onBusinessBegin(BusinessType bt)
{
    int nTimes = (bt == BDI_BusinessSwitch::DataBusiness ? SWD_TIMES : SWM_TIMES);

    _t0 = time(NULL);
    _t2 = _t1 = _t0 + SW_BASE_PERIOD * nTimes;
    _curBnss = bt;
    _curTimeZone = 0;
    _nextSwitchZone = -1;
}

BDI_BusinessSwitch::BDI_BusinessSwitch(BDI_Daemon *d)
    : _d(d)
{
    reset();
}

void BDI_BusinessSwitch::reset()
{
    _t0 = 0;
    _t1 = 0;
    _t2 = 0;
    _curBnss = BDI_BusinessSwitch::MobileBusiness;
    _curTimeZone = 0;
    _nextSwitchZone = -1;
}

bool BDI_BusinessSwitch::autoSwitch()
{
    time_t curTime = time(NULL);

    if (_t0 != 0)
        _curTimeZone = (curTime - _t0) / SW_BASE_PERIOD;

    if (curTime < _t2 && _curTimeZone != _nextSwitchZone) {
        if (_curBnss == BDI_BusinessSwitch::DataBusiness) {
            // pppd maybe not up because of a call coming in, 
            // so here check it again.
            if (_d->_dlink->status() == BDI_Link::Disabled)
                _d->_dlink->setEnabled(true);
            _d->checkDataThreads();
        }
#ifdef _BDI_MOBILE
        else
            _d->checkMobileThread();
#endif
        return false;
    }

    // do switch
    bool result = false;

    // Switch to data business
    if (_curBnss == BDI_BusinessSwitch::MobileBusiness) {
#ifdef _BDI_MOBILE
        if (!_d->mobileBusy()) {
            _d->stopMobileThread();
#else
        {
#endif
            _d->_dlink->setEnabled(true);
            onBusinessBegin(BDI_BusinessSwitch::DataBusiness);
            result = true;
        }
    }
#ifdef _BDI_MOBILE
    // Switch to mobile business
    else {
        if (_d->dataBusy() && tryExpand()) {
            RB_DEBUG("bsw_auto|Data business expects running more time...");
        }
        else {
            RB_DEBUG("bsw_auto|Closing data business...");
            _d->stopDataThreads();
            RB_DEBUG("bsw_auto|Closing data link...");
            _d->_dlink->setEnabled(false);
            onBusinessBegin(BDI_BusinessSwitch::MobileBusiness);
            result = true;
        }
    }
#endif

    if (!result)
        _nextSwitchZone = _curTimeZone + 1;

    return result;
}

bool BDI_BusinessSwitch::trySwitchData()
{
#ifdef _BDI_MOBILE
    time_t curTime = time(NULL);
    bool result = false;

    if (_curBnss == BDI_BusinessSwitch::DataBusiness) {
        if (_t2 - curTime >= SW_BASE_PERIOD)
            result = true;
        else if (tryExpand()) {
            RB_DEBUG("bsw_data|Data business expects running more time...");
            result = true;
        }
        else
            result = (_t2 - curTime > 1);
    }
    else {
        if ((curTime - _t0 < SW_BASE_PERIOD) || _d->mobileBusy()) {
            RB_DEBUG("bsw_data|Mobile business cann't be swapped out!");
            _nextSwitchZone = _curTimeZone + 1;
        }
        else {
            _d->stopMobileThread();
            _d->_dlink->setEnabled(true);
            onBusinessBegin(BDI_BusinessSwitch::DataBusiness);
            result = true;
        }
    }

    return result;
#else
    return true;
#endif
}

bool BDI_BusinessSwitch::trySwitchMobile()
{
#ifdef _BDI_MOBILE
    time_t curTime = time(NULL);
    bool result = false;

    if (_curBnss == BDI_BusinessSwitch::MobileBusiness) {
        if (_t2 - curTime >= SW_BASE_PERIOD)
            result = true;
        else if (tryExpand()) {
            RB_DEBUG("bsw_mobile|Mobile business expects running more time...");
            result = true;
        }
        else
            result = (_t2 - curTime > 1);
    }
    else {
        if ((curTime - _t0 < SW_BASE_PERIOD) || _d->dataBusy()) {
            RB_DEBUG("bsw_mobile|Data business cann't be swapped out!");
            _nextSwitchZone = _curTimeZone + 1;
        }
        else {
            _d->stopDataThreads();
            _d->_dlink->setEnabled(false);
            onBusinessBegin(BDI_BusinessSwitch::MobileBusiness);
            result = true;
        }
    }

    return result;
#else
    return false;
#endif
}
*/

// BDI_Daemon members

void BDI_Daemon::loadSettings()
{
    char *ss;

    Config conf(BDI_CONF_FILE);
    conf.setGroup("Server");
    ss = conf.getValue("rr_server_host");
    if (ss != NULL) {
        memset(_rrs_host, 0, 16);
        strncpy(_rrs_host, ss, 15);
        FREE(ss);
    }
    ss = conf.getValue("rt_server_host");
    if (ss != NULL) {
        memset(_rts_host, 0, 16);
        strncpy(_rts_host, ss, 15);
        FREE(ss);
    }
    _rrs_port = conf.getIntValue("rr_server_port");
    _rts_port = conf.getIntValue("rt_server_port");

    conf.setGroup("Authentication");
    ss = conf.getValue("username");
    if (ss != NULL) {
	pthread_mutex_lock(&usr_mutex);
        memset(_usr, 0, 21);
        strncpy(_usr, ss, 20);
	pthread_mutex_unlock(&usr_mutex);
        FREE(ss);
    }
    ss = conf.getValue("password");
    if (ss != NULL) {
	pthread_mutex_lock(&pwd_mutex);
        memset(_pwd, 0, 21);
        strncpy(_pwd, ss, 20);
	pthread_mutex_unlock(&pwd_mutex);
        FREE(ss);
    }
}

void BDI_Daemon::saveAccount()
{
	Config conf(BDI_CONF_FILE);
	conf.setGroup("Authentication");

	pthread_mutex_lock(&usr_mutex);
	conf.setValue("username", _usr);
	pthread_mutex_unlock(&usr_mutex);

/*
	//TODO unmark following to enable auto login
	pthread_mutex_lock(&pwd_mutex);
	conf.setValue("password", _pwd);
	pthread_mutex_unlock(&pwd_mutex);
*/
}

int BDI_Daemon::setUserName(const char * num)
{
	assert(NULL != num);	

	if(20 < strlen(num))
		return -1;

	pthread_mutex_lock(&usr_mutex);
	memset(_usr, 0, 21);
	strncpy(_usr, num, 20);
	pthread_mutex_unlock(&usr_mutex);

	return 0;
}

int BDI_Daemon::setPassWord(const char *pwd)
{
	assert(NULL != pwd);
	if(20 < strlen(pwd))
		return -1;
	pthread_mutex_lock(&pwd_mutex);
	memset(_pwd, 0, 21);
	strncpy(_pwd, pwd, 20);
	pthread_mutex_unlock(&pwd_mutex);

	return 0;
}

void BDI_Daemon::initDBusConnection()
{
    DBusError err;
    dbus_error_init(&err);

    // connect to the bus
    _busConn = dbus_bus_get(DBUS_BUS_SYSTEM, &err);
    if (dbus_error_is_set(&err)) {
        RB_WARNING("dbus|Connection Error (%s)", err.message);
        dbus_error_free(&err);
    }
    if (_busConn == NULL) {
        RB_WARNING("dbus|Connection Null");
        return;
    }

    // request our name on the bus
    int ret = dbus_bus_request_name(_busConn, BDI::BUSNAME_BOI, 
                                    DBUS_NAME_FLAG_REPLACE_EXISTING , 
                                    &err);
    if (dbus_error_is_set(&err)) {
        RB_WARNING("dbus|Name Error (%s)", err.message);
        dbus_error_free(&err);
    }
    if (ret != DBUS_REQUEST_NAME_REPLY_PRIMARY_OWNER) {
        RB_WARNING("dbus|Not Primary Owner (%d)", ret);
        return;
    }
}

bool BDI_Daemon::matchResponse(const BDI_Event *e, const BDI_Event *eToMatch)
{
    if (e->type() == BDI_Event::Register 
        && eToMatch->type() == BDI_Event::RegisterResp)
        return true;

    if (e->type() == BDI_Event::Login 
        && eToMatch->type() == BDI_Event::LoginResp)
        return true;

    if (e->type() == BDI_Event::Logout 
        && eToMatch->type() == BDI_Event::LogoutResp)
        return true;

    if (e->type() == BDI_Event::RTData 
        && eToMatch->type() == BDI_Event::RTDataResp 
        && e->sn() == eToMatch->sn())
        return true;

    if (e->type() == BDI_Event::Qurey 
        && eToMatch->type() == BDI_Event::Reply 
        && e->pid() == eToMatch->pid())
        return true;

    return false;
}

bool BDI_Daemon::sendEvent(int sock, const BDI_Event *e)
{
    RB_ASSERT(e != NULL);

    uint8 *frbuf = NULL;
    unsigned int frlen = e->encode(&frbuf);
    if (frbuf == NULL)
        return false/*SYS_SYSTEM_FAULT*/;

    // The frame length be real only after encode
    RB_DEBUG("\033[32m%s @%d\033\[0m|<-- %s", 
             timevToStr(time(NULL)).c_str(), sock, e->toString().c_str());

    //TODO debug
	//dump(frbuf, frlen);

    int ret = _dlink->putBlock(sock, frbuf, frlen);
    DELETE_ARR(frbuf);

    return ret == 0;
    /*
    if (ret == 0)
        return 0;
    else if (ret == -2)
        return SYS_SERVER_NO_ANSWER;
    else
        return SYS_SYSTEM_FAULT;
    */
}

BDI_EventRef BDI_Daemon::recvEvent(int sock)
{
    // get the frame header
    uint8 frheader[FRAME_HEADER_LEN];
    if (_dlink->getBlock(sock, frheader, FRAME_HEADER_LEN) != 0)
        return NULL; 

    BDIDecoder dec(frheader, FRAME_HEADER_LEN);
    const uint32 frlen = dec.getInteger(4);
    if (frlen < FRAME_HEADER_LEN)
        return NULL;
    uint8 *frbuf = NEW uint8[frlen];
    if (frbuf == NULL)
        return NULL;

    // get the whole frame
    memcpy(frbuf, frheader, FRAME_HEADER_LEN);
    const uint32 bodylen = frlen - FRAME_HEADER_LEN;
	if (_dlink->getBlock(sock, frbuf + FRAME_HEADER_LEN, bodylen) != 0) {
		DELETE_ARR(frbuf);
		return NULL;
	}

	//TODO debug
	//BDI::dump(frbuf, frlen);

    // decode and handle the recived event
    BDI_EventRef res = BDI_Event::decode(frbuf, frlen);
    DELETE_ARR(frbuf);


    if (!res.isNull())
        RB_DEBUG("\033[32m%s @%d\033\[0m|--> %s", 
                 timevToStr(time(NULL)).c_str(), sock, res->toString().c_str());

    return res;
}

int BDI_Daemon::doSubmit(int sock, const BDI_Event *req, BDI_EventRef &reply)
{
    int health = 3;
    bool replyDone = false;

    RB_ASSERT(req != NULL);

    // send the request frame data
    if (!sendEvent(sock, req))
        return SYS_SYSTEM_FAULT;


    // waiting for reply
    while (!replyDone && health > 0) {
        struct timeval timeout;
        timeout.tv_sec = 10;
        timeout.tv_usec = 0;
        if (_dlink->wait(sock, &timeout)) {
            BDI_EventRef eRcv = recvEvent(sock);
            if (eRcv.isNull())
                return SYS_SYSTEM_FAULT;

            if (eRcv->type() == BDI_Event::RedirectInd) {
                BDI_Event ack(BDI_Event::RedirectResp);
                ack.setSn(eRcv->sn());
                sendEvent(sock, &ack);
                // buffer the redirect event
                _rtdq->addEvent(eRcv);
                return LOGIN_SER_REDIRECT;
            }

            if (matchResponse(req, const_cast<BDI_Event *>(eRcv.getPtr()))) {
                replyDone = true;
                reply = eRcv;
            }
        }
        else if (--health > 0) {
        	BDI_ActiveTest linkHold;
            linkHold.setSn(getNextSerialNo());
            if (!sendEvent(sock, &linkHold)) {
                health = 0;
            }
        }
    }

    return replyDone ? 0 : SYS_SERVER_NO_ANSWER;
}

int BDI_Daemon::doConnect(int channel, int *sock)
{
	int ret = 0;
    const char *addr;
    unsigned short port;
    switch (channel) {
    case BDI_Thread::RT_PEER:
        addr = _rts_host;
        port = _rts_port;
        break;
    case BDI_Thread::RR_PEER:
        addr = _rrs_host;
        port = _rrs_port;
        break;
    default:
        RB_ASSERT(0);
        break;
    }

    for (;;) {
        ret = _dlink->connectHost(addr, port, sock);
        if (ret == 0)
            break;
    }
    return ret;
}

int BDI_Daemon::doLogin(int channel, int *sock)
{
    int ret = 0;
    BDI_EventRef resp;

    BDI_Login login;

	pthread_mutex_lock(&usr_mutex);
    login.setUserName(_usr);
	pthread_mutex_unlock(&usr_mutex);

	pthread_mutex_lock(&pwd_mutex);
    login.setPassword(_pwd);
	pthread_mutex_unlock(&pwd_mutex);

    const char *addr;
    unsigned short port;
    switch (channel) {
    case BDI_Thread::RT_PEER:
        addr = _rts_host;
        port = _rts_port;
        break;
    case BDI_Thread::RR_PEER:
        addr = _rrs_host;
        port = _rrs_port;
        break;
    default:
        RB_ASSERT(0);
        break;
    }

    for (;;) {
        ret = _dlink->connectHost(addr, port, sock);
        if (ret != 0)
            break;

        login.setSn(getNextSerialNo());
        login.setTimestamp(time(NULL));
        ret = doSubmit(*sock, &login, resp);
        if (ret != 0)
            break;

        if (resp->type() != BDI_Event::LoginResp) {
            resp.unref();
            _dlink->close(*sock);
            *sock = -1;
            continue;
        }

        BDI_LoginResp *loginResp = reinterpret_cast<BDI_LoginResp *>(resp.getPtr());
        loginResp->setChannel(channel);

        // buffer this longin response event
        _rtdq->addEvent(resp);

        ret = loginResp->errCode();
        break;
    }

    if (ret != 0 && *sock != -1) {
        _dlink->close(*sock);
        *sock = -1;
    }

    if (resp.isNull() && ret != LOGIN_SER_REDIRECT) {
        // buffer a man made response event
        resp = NEW BDI_LoginResp(ret);
        if (!resp.isNull()) {
            RB_DEBUG("+++ %s", resp->toString().c_str());
            _rtdq->addEvent(resp);
        }
    }

    return ret;
}

void BDI_Daemon::doLogout(int s)
{
    BDI_EventRef resp;
    BDI_Event logout(BDI_Event::Logout);
    logout.setSn(getNextSerialNo());
    doSubmit(s, &logout, resp);
}

#if 0
void BDI_Daemon::checkDataThreads()
{
    /*
    if (_dlink->status() == BDI_Link::Connected) {
        // checks rt-thread
        if (!_rtd_thread->running() 
            && _loginStat != LOGIN_INVALID_USR 
            && _loginStat != LOGIN_AUTHENTICATION_FAIL)
            _rtd_thread->start();
        // checks rr-thread
        if (!_sub_thread->running() && _loginStat == 0)
            _sub_thread->start();
    }
    */
    BDI_Link::Status lnkst = _dlink->status();

    if (!_rtd_thread->running() 
        && lnkst == BDI_Link::Connected 
        && _loginStat != LOGIN_INVALID_USR 
        && _loginStat != LOGIN_AUTHENTICATION_FAIL)
        _rtd_thread->start();

    if (!_sub_thread->running() 
        && (lnkst == BDI_Link::Connected || lnkst == BDI_Link::NoConnection))
        _sub_thread->start();
}
#endif

inline void BDI_Daemon::stopDataThreads()
{
	_rtd_thread->terminate();
	_rtd_thread->wait();
	_sub_thread->terminate();
	_sub_thread->wait();
}

inline bool BDI_Daemon::dataBusy() const
{
    return (_rtd_thread->busy() || _sub_thread->busy());
}

#ifdef _BDI_GPS
void BDI_Daemon::stopGpsThread()
{
    _gps_thread->terminate();
    _gps_thread->wait();
}
#endif

void BDI_Daemon::onLoginResponse(const BDI_LoginResp *resp)
{
    int loginstat = resp->errCode();
    if (loginstat != _loginStat 
    	|| BDI::LOGIN_INVALID_USR == _loginStat
    	|| BDI::LOGIN_AUTHENTICATION_FAIL == _loginStat
    	|| BDI::LOGIN_RELOGIN == _loginStat ) { //on

        // TODO: Tell gui login status changed.
        dbus_uint32_t serial = imsg_i32(_busConn,
                 OBJPATH_BOI, INTF_RTDATA, SGNL_RTDATA_LONGIN_STATUSCHANGED, 
                 loginstat);
        _loginStat = loginstat;
        RB_DEBUG("Login status changed (%d) (%lu)", _loginStat, serial);
		//if(0 == loginstat) 
            //imsg_i32(_busConn, OBJPATH_BOI, INTF_DLINK, SGNL_DLINK_STATUSCHANGED, loginstat);
    }

    if (loginstat == LOGIN_INVALID_USR 
        || loginstat == LOGIN_AUTHENTICATION_FAIL) {
        _rtd_thread->wait();
        
        switch (resp->channel()) {
        case BDI_Thread::RT_PEER:
            _rtd_thread->wait();
            break;
        case BDI_Thread::RR_PEER:
            _sub_thread->wait();
            _subq->resetEvents(); // or clear ?
            break;
        default:
            RB_WARNING("Unknown business channel: %d", resp->channel());
            break;
        }
    }
    else if (loginstat == LOGIN_TIME_OUTERSYNC) {
        //_switcher->reset();
        switch (resp->channel()) {
        case BDI_Thread::RT_PEER:
			_rtd_thread->terminate();
            _rtd_thread->wait();
			_rtd_thread->start();
            break;
        case BDI_Thread::RR_PEER:
            _sub_thread->wait();
            _subq->resetEvents(); // or clear ?
            break;
        default:
            break;
        }
    }
    else if (loginstat == LOGIN_SER_REDIRECT) {
        Config conf(BDI_CONF_FILE);
        conf.setGroup("Server");
        switch (resp->channel()) {
        case 1:
            memset(_rts_host, 0, 16);
            strncpy(_rts_host, resp->redirectHost1(), 15);
            _rts_port = resp->redirectPort1();
            conf.setValue("rt_server_host", _rts_host);
            conf.setValue("rt_server_port", _rts_port);
            _rtd_thread->wait();
            break;
        case 3:
            memset(_rrs_host, 0, 16);
            strncpy(_rrs_host, resp->redirectHost1(), 15);
            _rrs_port = resp->redirectPort1();
            conf.setValue("rr_server_host", _rrs_host);
            conf.setValue("rr_server_port", _rrs_port);
            _sub_thread->wait();
            _subq->resetEvents();
            break;
        default:
            break;
        }
    }
}

void BDI_Daemon::onRedirectInd(const BDI_RedirectInd *redir)
{
    _rtd_thread->wait();
    _sub_thread->wait();
    _subq->resetEvents();

    // reset server address 
    memset(_rts_host, 0, 16);
    strncpy(_rts_host, redir->host1(), 15);
    memset(_rrs_host, 0, 16);
    strncpy(_rrs_host, redir->host3(), 15);
    _rts_port = redir->port1();
    _rrs_port = redir->port3();

    Config conf(BDI_CONF_FILE);
    conf.setGroup("Server");
    conf.setValue("rt_server_host", _rts_host);
    conf.setValue("rt_server_port", _rts_port);
    conf.setValue("rr_server_host", _rrs_host);
    conf.setValue("rr_server_port", _rrs_port);
}

void BDI_Daemon::onRegisterResp(const BDI_CYTRegisterResp *msg, DBusConnection *conn)
{
	imsg_register_resp(conn, msg);	
	//TODO
}

void BDI_Daemon::handleUserRequest(DBusMessage *msg)
{
    if (msg == NULL)
        return;

    const char *intf = dbus_message_get_interface(msg);
    if (strcmp(intf, BDI::INTF_DLINK) == 0)
        onDLinkRequest(msg);
    else if (strcmp(intf, BDI::INTF_MOBILE) == 0)
        onMobileRequest(msg);
    else if (strcmp(intf, BDI::INTF_RTDATA) == 0) {
        if (_subq->count() >= BDI_EventList::MAX_ITEMS) {
            RB_DEBUG("daemon|Submit list is full!");
            if (!dbus_message_get_no_reply(msg)) 
                imsg_error_reply(_busConn, msg, SES_REQUEST_UNEXEC);
        }
        else {
            BDI_EventRef e = _bdHost->onRequest(msg, _busConn);
            if (!e.isNull()) {
                dbus_message_ref(msg); // to hold the dbus message
                _subq->addEvent(e, msg);
                //trySwitchData();
            }
        }
    }

	if(NULL != msg)
		dbus_message_unref(msg);
}

void BDI_Daemon::onDLinkRequest(DBusMessage *msg)
{
	msg = msg;
    //const char *memb = dbus_message_get_member(msg);
    //if (strcmp(memb, MCAL_DLINK_TRYTOCLOSE) == 0)
        //trySwitchMobile();
}

#ifdef _BDI_GPS
/*void BDI_Daemon::onGpsEvent(const BDI_Event *e)
{
    static time_t lastReportTime = 0;
    static BDI_PosReportEvent *reportSave = NULL;

    if (e->bid() == GPS_Position) {
        const BDI_GpsPosEvent *gpe = reinterpret_cast<const BDI_GpsPosEvent *>(e);

        // GPS unfixed, broadcast this event
        if (!gpe->fixed()) {
            imsg_i32(_busConn, 
                     OBJPATH_GPOSITION, INTF_GPOS, SGNL_GPOS_GPSUNFIXED, 
                     0);
            RB_DEBUG("GPS Unfix!");
            return;
        }

        // Position report business
        if (gpe->marked()) {
            BDTermStatus termst;
            termst._power = 255;
            termst._sat1 = gpsSateCount();
            termst._cellSignal = _mob_thread->signalLevel();
            if (reportSave == NULL) {
                reportSave = NEW BDI_PosReportEvent;
                if (reportSave == NULL)
                    return;
                reportSave->setSn(getNextSerialNo());
                reportSave->setDevName(0x12, 1, _usr);
            }
            reportSave->setTermStatus(termst);
            reportSave->addPosition(gpe->gpos());

            if (reportSave->posCount() == BDI_PosReportEvent::MAX_POS_ITEMS 
                || gpe->gpos()._ts - lastReportTime > 1800) {
                _subq->addEvent(reportSave, NULL);
                lastReportTime = gpe->gpos()._ts;
                reportSave = NULL;
            }
        }
        
        // Save current position
        _cpos = gpe->gpos();

        // D-Bus broadcast
        imsg_gpos(_busConn, gpe);
    }
    else if (e->bid() == GPS_SateView) {
        const BDI_GpsViewEvent *gve = reinterpret_cast<const BDI_GpsViewEvent *>(e);
        // D-Bus broadcast
        imsg_sateview(_busConn, gve);
    }
}
*/
void BDI_Daemon::onGpsRequest(DBusMessage *)
{
}

void BDI_Daemon::setCurrentPos(GPosition pos)
{ 
	_cpos = pos; 
	return;
}
#endif

#ifdef _BDI_MOBILE
void BDI_Daemon::onMobileEvent(const BDI_Event *e)
{
    switch (e->bid()) {
    case Mobile_Signal:
        imsg_i32(_busConn, 
                 OBJPATH_MOBILE, INTF_MOBILE, SGNL_MOBILE_SIGNALCHANGED, 
                 reinterpret_cast<const BDI_MSignalEvent *>(e)->signalLevel());
        break;
    case Mobile_CallingIn:
		RB_DEBUG("daemon|Moblib_callingin");
    case Mobile_CallingUp:
		RB_DEBUG("daemon|Moblib_callingup");
	case Mobile_DialOk:
		RB_DEBUG("daemon|Mobile_DialOk");
	case Mobile_DialErr:
		RB_DEBUG("daemon|Mobile_DialErr");
    case Mobile_Hungup:
        imsg_mobile_call(_busConn, reinterpret_cast<const BDI_MCallEvent *>(e));
        break;
	case PPP_Up:
		stopDataThreads();
		if(!_rtd_thread->running())
			_rtd_thread->start();
		if(!_sub_thread->running())
			_sub_thread->start();
		break;
	case PPP_Down:
		//stopDataThreads();
		break;
    default:
        break;
    }
}

void BDI_Daemon::onMobileRequest(DBusMessage *msg)
{
    DBusError err;
    int ret = -1;

    dbus_error_init(&err);
    
    const char *memb = dbus_message_get_member(msg);
    if (memb == NULL) {
        ret = SYS_SYSTEM_FAULT;
        goto DO_REPLY;
    }

    /*if (!_mob_thread->running() || !_mob_thread->ready()) {
        trySwitchMobile();
		stopDataThreads();
        ret = SES_REQUEST_UNEXEC;
        goto DO_REPLY;
    }*/

    if (strcmp(memb, BDI::MCAL_MOBILE_GET_MODULETYPE) == 0) {
        ret = _mp_thread->moduleType();
    }
    else if (strcmp(memb, BDI::MCAL_MOBILE_DIAL) == 0) {
        dbus_int32_t lineId;
        const char *number = "";
        dbus_message_get_args(msg, &err, 
                    DBUS_TYPE_INT32, &lineId, 
                    DBUS_TYPE_STRING, &number, 
                    DBUS_TYPE_INVALID);
        if (dbus_error_is_set(&err)) {
            RB_WARNING("dbus|Get Args Error (%s)", err.message);
            dbus_error_free(&err);
            ret = SES_REQUEST_UNEXEC;
            goto DO_REPLY;
        }
        ret = _mp_thread->dial(lineId, number);
    }
    else if (strcmp(memb, BDI::MCAL_MOBILE_ANSWER) == 0) {
        ret = _mp_thread->answer();
    }
    else if (strcmp(memb, BDI::MCAL_MOBILE_HANGUP) == 0) {
        ret = _mp_thread->hangup();
    }

DO_REPLY:
    imsg_error_reply(_busConn, msg, ret);
}
#endif

BDI_Daemon::BDI_Daemon()
{
    initDBusConnection();

    memset(_rrs_host, 0, 16);
    memset(_rts_host, 0, 16);
	pthread_mutex_lock(&usr_mutex);
    memset(_usr, 0, 21);
	pthread_mutex_unlock(&usr_mutex);

	pthread_mutex_lock(&pwd_mutex);
    memset(_pwd, 0, 21);
	pthread_mutex_unlock(&pwd_mutex);
    loadSettings();

    _loginStat = -1;

    _rtdq = NEW BDI_EventList;
    _subq = NEW BDI_EventList;

//#ifdef arm
    //_dlink = NEW BDI_MP_Thread;
//#else
    _dlink = NEW BDI_UnixStreamLink;
//#endif

#if defined(PRO_MF08A)
    _bdHost = NEW BDI_MF08aHost(this);
#elif defined(PRO_MF08B)
    _bdHost = NEW BDI_MF08bHost(this);
#else
    #error "MQPROJECT not set!"
#endif

    _rtd_thread = NEW BDI_RTDataThread(this);
    _sub_thread = NEW BDI_SubmitThread(this);
    //_switcher = NEW BDI_BusinessSwitch(this);
	_mp_thread = NEW BDI_MP_Thread(this);
#ifdef _BDI_GPS
    _gps_thread = NEW BDI_GpsThread(this);
#endif
}

BDI_Daemon::~BDI_Daemon()
{
    if (_rtd_thread != NULL)
        DELETE(_rtd_thread);
    if (_sub_thread != NULL)
        DELETE(_sub_thread);
	if (_mp_thread != NULL)
		DELETE(_mp_thread);
#ifdef _BDI_GPS
    if (_gps_thread != NULL)
        DELETE(_gps_thread);
#endif
    if (_bdHost != NULL)
        DELETE(_bdHost);
}

void BDI_Daemon::run()
{
    int linkStat = -1;
    pthread_t mytid = pthread_self();

#ifdef _BDI_GPS
    _gps_thread->start();
#endif
	_mp_thread->start();

    while (g_runEnabled) {
        // Data link monitor
        int linkst = _mp_thread->status();
        if (linkst != linkStat) {
            RB_DEBUG("Link Status Changed (%d)", linkst);
            imsg_i32(_busConn, 
                     OBJPATH_BOI, INTF_DLINK, SGNL_DLINK_STATUSCHANGED, 
                     linkst);
            linkStat = linkst;
        }

        // Handle user request
        dbus_connection_read_write(_busConn, 150);
        handleUserRequest(dbus_connection_pop_message(_busConn));

        // Handle the real-time event and reply event
        const BDI_EventListItem *pitem = _rtdq->getFreeEvent(mytid, 150);
        if (pitem != NULL) {
            BDI_EventRef e = pitem->event();
            DBusMessage *msg = reinterpret_cast<DBusMessage *>(pitem->data());
            printf("on deamon handle rtevent %X\r\n", e->type());

            switch (e->type()) {
            case BDI_Event::LoginResp:
                onLoginResponse(reinterpret_cast<const BDI_LoginResp *>(e.getPtr()));
                break;
            case BDI_Event::RedirectInd:
                onRedirectInd(reinterpret_cast<const BDI_RedirectInd *>(e.getPtr()));
                break;
            case BDI_Event::RegisterResp:
    			onRegisterResp(reinterpret_cast<const BDI_CYTRegisterResp *>(e.getPtr()), _busConn);
            	break;
            case BDI_Event::RTData:
            case BDI_Event::Reply:
            case BDI_Event::UserErrorEvent:
                _bdHost->onEvent(e, msg, _busConn);
                break;

#ifdef _BDI_GPS
            case BDI_Event::UserGpsEvent:
                _bdHost->onGpsEvent(e.getPtr(), _busConn);
                break;
#endif
#ifdef _BDI_MOBILE
            case BDI_Event::UserMobileEvent:
                onMobileEvent(e.getPtr());
                break;
#endif
            default:
                RB_DEBUG("daemon|Unexpected event type: 0X%X", e->type());
                break;
            }

            if (msg != NULL)
                dbus_message_unref(msg);

            _rtdq->removeEvent(mytid, &pitem);
        }
    }

    stopDataThreads();
#ifdef _BDI_GPS
    stopGpsThread();
#endif
}

/*bool BDI_Daemon::trySwitchData()
{
    return _switcher->trySwitchData();
}

bool BDI_Daemon::trySwitchMobile()
{
    return _switcher->trySwitchMobile();
}
*/

#ifdef _BDI_GPS
bool BDI_Daemon::gpsFixed() const
{
    bool result = false;
    if (_gps_thread != NULL)
        result = _gps_thread->gpsFixed();
    return result;
}

int BDI_Daemon::gpsSateCount() const
{
    int result = 0;
    if (_gps_thread != NULL)
        result = _gps_thread->gpsSateCount();
    return result;
}
#endif

// Signals handling

static void catchTerm(int sig)
{
    if (sig == SIGINT || sig == SIGTERM)
        g_runEnabled = false;
    else
        fprintf(stderr, "\033[31;47mdaemon|SIGNAL %d received, pid=%d\033[0m\n", sig, getpid());
}

// bdstar-i program entry

#ifdef __cplusplus
extern "C" {
#endif
extern int capi_register_client(void);
extern int mapi_register_client(void);
#ifdef __cplusplus
};
#endif

int main()
{
    // Install signal handlers
    struct sigaction sigact;
    sigact.sa_handler = catchTerm;
    sigact.sa_flags = 0;
    sigaction(SIGINT, &sigact, NULL);
    sigaction(SIGTERM, &sigact, NULL);
    sigaction(SIGPIPE, &sigact, NULL);
    sigaction(SIGCHLD, &sigact, NULL);

#ifdef _BDI_GPS
    capi_register_client();
#endif
	mapi_register_client();

    {
    BDI_Daemon d;
    d.run();
    }

    PRINT_ALLOC_STATS();

    return 0;
}

/*
#include <sys/wait.h>
int main()
{
    pid_t pid = fork();
    if (pid < 0)
        perror("fork()");
    else if (pid == 0) {
        // Install signal handlers
        struct sigaction sigact;
        sigact.sa_handler = catchTerm;
        sigact.sa_flags = 0;
        sigaction(SIGINT, &sigact, NULL);
        sigaction(SIGTERM, &sigact, NULL);
        sigaction(SIGPIPE, &sigact, NULL);
        sigaction(SIGCHLD, &sigact, NULL);
        sigaction(SIGHUP, &sigact, NULL);

#ifdef _BDI_GPS
        capi_register_client();
#endif
	    mapi_register_client();

        {
        BDI_Daemon d;
        d.run();
        }

        PRINT_ALLOC_STATS();
    }
    else {
        int st;
        sleep(10);
        waitpid(pid, &st, 0);
        if (WIFSIGNALED(st))
            printf("daemon|Terminated by signal: %d\n", WTERMSIG(st));
        else if (WIFSTOPPED(st))
            printf("daemon|Stopped by signal: %d\n", WSTOPSIG(st));
    }

    return 0;
}
*/
