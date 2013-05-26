#ifndef BDI_DAEMON_H
#define BDI_DAEMON_H

#include <dbus/dbus.h>

#include "bdi.h"
#include "bdi_event_list.h"

#ifdef PRO_MF08A
    #define _BDI_MOBILE
    #define _BDI_GPS
#elif defined(PRO_MF08B)
    #define _BDI_MOBILE
#endif

class BDI_EventList;
class BDI_RTDataThread;
class BDI_SubmitThread;
//class BDI_BusinessSwitch;
class BDI_GpsThread;
class BDI_Host;
class BDI_MF08aHost;
class BDI_MF08bHost;
class BDI_MP_Thread;

class BDI_Daemon
{
private:
    DBusConnection *_busConn;

    BDI_EventListRef _rtdq; // Real-time data event list
    BDI_EventListRef _subq; // Submitting event list

    BDI_RTDataThread *_rtd_thread;
    BDI_SubmitThread *_sub_thread;
    //BDI_BusinessSwitch *_switcher;
#ifdef _BDI_GPS
    BDI_GpsThread *_gps_thread;
    GPosition _cpos;
#endif

    BDI_LinkRef _dlink;
	BDI_MP_Thread *_mp_thread;;

    //BDI_Host *_bdHost;
#if defined(PRO_MF08A)
    BDI_MF08aHost *_bdHost;
#elif defined(PRO_MF08B)
    BDI_MF08bHost *_bdHost;
#else
    #error "MQPROJECT not set!"
#endif

    // settings
    char _rts_host[16]; // Real-time business server IPv4 address, '\0' terminated.
    char _rrs_host[16]; // Request/reply server IPv4 address, '\0' terminated.
    unsigned short _rts_port; // Real-time business server port
    unsigned short _rrs_port; // Request/reply server port

    // login autentication
    char _usr[21]; // Login user name, '\0' terminated.
    char _pwd[21]; // Login authentication code, '\0' terminated.

    int _loginStat; // Current login status

private:
    void initDBusConnection();

    bool matchResponse(const BDI_Event *e, const BDI_Event *eToMatch);

    // Call BDI_Link::putBlock() to send the event frame
    // Send the event on link sock.
    // On success, returns true, otherwise returns false.
    bool sendEvent(int sock, const BDI_Event *e);

    // Read a frame from the link, and return the event.
    // NULL returned means some system error occured.
    BDI_EventRef recvEvent(int sock);

    // Send a event on a connection and get the reply.
    // On success, 0 is returned.
    // Errors:
    //      SYS_SYSTEM_FAULT
    //      SYS_SERVER_NO_ANSWER
    //      LOGIN_SER_REDIRECT
    // Note that, the reply not must a BDI_Reply, maybe LoginResp 
    // or LogoutResp
    int doSubmit(int sock, const BDI_Event *req, BDI_EventRef &reply);

    // Connect to server and login.
    // On success, 0 is returned.
    // Errors:
    //      LOGIN_*
    //      SYS_SYSTEM_FAULT
    //      SYS_SERVER_NO_ANSWER
    //      SYS_NETWORK_UNREACH
    int doConnect(int channel, int *sock);
    int doLogin(int channel, int *sock);
    void doLogout(int sock);

    //void checkDataThreads();
    void stopDataThreads();
    bool dataBusy() const;

#ifdef _BDI_GPS
    void stopGpsThread();
#endif

private:
    void onLoginResponse(const BDI_LoginResp *);
    void onRedirectInd(const BDI_RedirectInd *);
	void onRegisterResp(const BDI_CYTRegisterResp *, DBusConnection *);

    void handleUserRequest(DBusMessage *);
    void onDLinkRequest(DBusMessage *);

#ifdef _BDI_GPS
    //void onGpsEvent(const BDI_Event *);
    void onGpsRequest(DBusMessage *);
#endif

#ifdef _BDI_MOBILE
    void onMobileEvent(const BDI_Event *);
    void onMobileRequest(DBusMessage *);
#endif

public:
    BDI_Daemon();
    ~BDI_Daemon();

    void run();

    BDI_EventListRef submitList() const;
    BDI_EventListRef rtdataList() const;

    BDI_LinkRef dlink() const;

    const char *devNumber() const;
	int setUserName(const char *);
	int setPassWord(const char *);

    bool trySwitchData();
    bool trySwitchMobile();
    void loadSettings();
    void saveAccount();

#ifdef _BDI_GPS
    bool gpsFixed() const;
    const GPosition &currentPos() const;
	void setCurrentPos(GPosition pos);
    int gpsSateCount() const;
#endif

    friend class BDI_Thread;
    friend class BDI_RTDataThread;
    friend class BDI_SubmitThread;
    //friend class BDI_BusinessSwitch;
    friend class BDI_MF08aHost;
    friend class BDI_MF08bHost;
};

// BDI_Daemon inline functions

inline BDI_EventListRef BDI_Daemon::submitList() const
{ return _subq; }

inline BDI_EventListRef BDI_Daemon::rtdataList() const
{ return _rtdq; }

inline BDI_LinkRef BDI_Daemon::dlink() const
{ return _dlink; }

inline const char *BDI_Daemon::devNumber() const
{ return _usr; }

#ifdef _BDI_GPS
inline const GPosition &BDI_Daemon::currentPos() const
{ return _cpos; }
#endif

#endif
