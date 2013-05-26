#ifndef BDI_HOST_H
#define BDI_HOST_H

#include <dbus/dbus.h>

#include "bdi_event.h"
#include "bdi_posopt.h"

class BDI_Daemon;

class BDI_Host
{
protected:
    int _hostCode;
    BDI_Daemon *_d;

public:
    BDI_Host(BDI_Daemon *);
    virtual ~BDI_Host();

    virtual BDI_EventRef onRequest(DBusMessage *, DBusConnection *) = 0;
    virtual void onEvent(BDI_EventRef, DBusMessage *, DBusConnection *) = 0;
};

#if defined(PRO_MF08A)
class BDI_MF08aHost : public BDI_Host
{
private:
	bdi_area_t  _areas[NR_MAXAREA];
	bdi_area_st  _area_st[NR_MAXAREA];
	int _thv_speedRun;
	int _ref_PosReport;
private:
    BDI_Event *onRequestSos(DBusMessage *);
    BDI_Event *onRequestSendMessage(DBusMessage *, int *);
    BDI_Event *onRequestImportOutportReport(DBusMessage *);

    void onEventError(const BDI_ErrorEvent *, DBusMessage *, DBusConnection *);

public:
    BDI_MF08aHost(BDI_Daemon *);
    ~BDI_MF08aHost();

    BDI_EventRef onRequest(DBusMessage *, DBusConnection *);
    void onEvent(BDI_EventRef, DBusMessage *, DBusConnection *);
	void onGpsEvent(const BDI_Event *, DBusConnection *);
	int loadPara();
};

#elif defined(PRO_MF08B)
class BDI_MF08bHost : public BDI_Host
{

private:
    BDI_Event *onRequestSendMessage(DBusMessage *, int *);
    BDI_Event *onRegister(DBusMessage *, int *);
    BDI_Event *onSwitchAccount(DBusMessage *, int *);
    BDI_Event *onInviteTerm(DBusMessage *, int *);

    void onEventError(const BDI_ErrorEvent *, DBusMessage *, DBusConnection *);
public:
    BDI_MF08bHost(BDI_Daemon *);
    ~BDI_MF08bHost();
    BDI_EventRef onRequest(DBusMessage *, DBusConnection *);
    void onEvent(BDI_EventRef, DBusMessage *, DBusConnection *);

};
#endif

#endif
