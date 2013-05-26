#ifndef BDI_PPP_LINK_H
#define BDI_PPP_LINK_H

#include <bdi_unix_stream.h>
#include <pthread.h>

#define SELECTTIME 200
#define GETSIGLELTIME 10 * 1000
#define TIMEOUTCOUNT (GETSIGLELTIME)/SELECTTIME

class BDI_PPPLink : public BDI_UnixStreamLink
{
public:
    enum ModuleType { MT_Unknown = 0, MT_Cdma = 1, MT_Gsm = 2 };
	enum Ppp_inst { Req_None, Req_Enable, Req_Disable};

public:
	BDI_PPPLink();
	virtual ~BDI_PPPLink();

	void setEnabled(bool);
	BDI_Link::Status status();
	int signalLevel();

private:
	int _mapifd;
	volatile bool _running;
    bool _pppConfiged;
	pthread_t _urcTid;
	volatile BDI_Link::Status _status;
	ModuleType _mtype;
	volatile Ppp_inst _ppp_inst;
	volatile int _siglvl;
	int _timeoutCount;

private:
	void startURCThread();
	static void * urcThread(void *pthis);
	void cdma_PppOpt();
	void gsm_PppOpt();
	void decode_urc(void *phdr, void *body, bool *pppIsDown);
	void getSignalLevel();
};

inline BDI_Link::Status BDI_PPPLink::status()
{ return _status; }

inline int BDI_PPPLink::signalLevel()
{ return _siglvl; }

#endif
