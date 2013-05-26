#ifndef BDI_MOBILE_THREAD_H
#define BDI_MOBILE_THREAD_H

#include <pthread.h>

#include "bdi_thread.h"

class BDI_Daemon;

class BDI_MobileThread : public BDI_Thread
{
public:
    enum ModuleType { MT_Unknown = 0, MT_Cdma = 1, MT_Gsm = 2 };

protected:
    void run();
    void onStopped();

public:
    BDI_MobileThread(BDI_Daemon *d);

    ModuleType moduleType() const;

    bool ready() const;

    int getCsq() const; // CSQ value: 0~32, 99
    int signalLevel() const; // Signal level: 0~5

    int dial(int lineId, const char *number);
    int answer();
    int hangup();
    int sendMessage(const char *number, const char *unicodes, int buflen);

private:
	void onCdmaUrc(void *, void *);
	void onGsmUrc(void *, void *);

    void popSms();
    
    bool initModule();

private:
    int _mapifd;
    ModuleType _mtype;
    bool _inCalling;
    bool _moduleInit;
    volatile int _ready;

    volatile int _sigLevel;
};

// BDI_MobileThread inline functions

inline BDI_MobileThread::ModuleType BDI_MobileThread::moduleType() const
{ return _mtype; }

inline bool BDI_MobileThread::ready() const
{ return _ready; }

inline int BDI_MobileThread::signalLevel() const
{ return _sigLevel; }

#endif
