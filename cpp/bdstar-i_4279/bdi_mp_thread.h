#ifndef BDI_MP_THREAD_H
#define BDI_MP_THREAD_H

#include <bdi_unix_stream.h>
#include <pthread.h>

#include "bdi_mobile_event.h"
#include "bdi_thread.h"
#include "bdi_event_list.h"

#define SELECTTIME 200
#define BDI_APPARA_FILE "/mnt/user/config/bdi-AP.conf"

class BDI_MP_Event;
class BDI_MP_PPPEvent;
class BDI_MP_DialEvent;
class BDI_MP_AnswerEvent;
class BDI_MP_HangupEvent;
class BDI_MP_SendMsgEvent;
class BDI_MP_GetSigEvent;

class BDI_MP_Thread : public BDI_Thread
{
public:
	enum ModuleType { MT_Unknown = 0, MT_Cdma = 1, MT_Gsm = 2 };
	enum ModuleStat { MobileReady, PPPConnecting, PPPConnected, PPPDisConnecting, MobileBusy};

protected:
	void run();
	void onStopped();

public:
	BDI_MP_Thread(BDI_Daemon *d);
	virtual ~BDI_MP_Thread();

	void pppEnable(bool);
	int dial(int lineId, const char *num);
	int answer();
	int hangup();
	int sendMsg(const char *num, const char *unicodes, int len);
	BDI_MP_Thread::ModuleStat status() const;
	BDI_MP_Thread::ModuleType moduleType() const;
	void eventHandle();
	void setSigLevel(int l);
	char sigLevel() const;

private:
	class BDI_MP_ModuleStat
	{
	private:
		volatile ModuleStat _s;
		time_t _t;
	public:
		BDI_MP_ModuleStat();
		~BDI_MP_ModuleStat();
		ModuleStat stat() const;
		void setStat(ModuleStat s);
		bool timeOut();
	};

	int _mapifd;
	int _csqTime;
	char _sigLevel;
	int _sigSteadyCount;
	int _pppErrorCount;
	ModuleType _mtype;
	BDI_MP_ModuleStat _status;
	BDI_MP_PPPEvent *_pppEvent;
	BDI_MP_DialEvent *_dialEvent;
	BDI_MP_HangupEvent *_hungupEvent;
	BDI_MP_AnswerEvent *_answerEvent;
	BDI_MP_GetSigEvent *_getsigEvent;
	const BDI_EventListItem *_sendMsgEvent;
	BDI_EventList * _msgEventList;
	BDI_MP_Event * _ce; //current Event
	char _apt[12];

private:
	void onGsmUrc(void *header, void *body);
	void onCdmaUrc(void *header, void *body);
	bool initModule();
	void setStat(BDI_MP_Thread::ModuleStat s);
	void printStat();
	//void getSignalLevel();
};

class BDI_MP_Event : public BDI_Event
{
public:
	BDI_MP_Event(BDI_MP_Thread * mpt);
	virtual ~BDI_MP_Event();

	virtual int handle();
	BDI_MP_Thread::ModuleStat stat() const;
	BDI_Event * retEvent();

protected:
	volatile BDI_MP_Thread::ModuleStat _st;
	BDI_MP_Thread * _mpt;
	int _errCount;
	BDI_Event * _e;
};

class BDI_MP_PPPEvent : public BDI_MP_Event
{
private:
	enum PPPREQ { PPP_None, PPP_Enable, PPP_Disable};
	volatile PPPREQ _req;

public:
	BDI_MP_PPPEvent(BDI_MP_Thread * p);
	virtual ~BDI_MP_PPPEvent();

	void setEnable(bool req);
	int handle();
};

class BDI_MP_DialEvent : public BDI_MP_Event
{
public:
	BDI_MP_DialEvent(BDI_MP_Thread * mpt, int lineId, const char * num, time_t t);
	virtual ~BDI_MP_DialEvent();

	int handle();

private:
	int _lineId;
	char * _num;
	time_t _t;
};

class BDI_MP_AnswerEvent : public BDI_MP_Event
{
public:
	BDI_MP_AnswerEvent(BDI_MP_Thread * mpt);
	virtual ~BDI_MP_AnswerEvent();

	int handle();
};

class BDI_MP_HangupEvent : public BDI_MP_Event
{
public:
	BDI_MP_HangupEvent(BDI_MP_Thread * mpt);
	virtual ~BDI_MP_HangupEvent();

	int handle();
};

class BDI_MP_SendMsgEvent: public BDI_MP_Event
{
public:
	BDI_MP_SendMsgEvent(BDI_MP_Thread * mpt, const char * num, const char *unicodes, int len);
	virtual ~BDI_MP_SendMsgEvent();

	int handle();
private:
	char * _num;
	char * _unicodes;
	int _len;
};

class BDI_MP_GetSigEvent: public BDI_MP_Event
{
public:
	BDI_MP_GetSigEvent(BDI_MP_Thread * mpt);
	virtual ~BDI_MP_GetSigEvent();

	int handle();
};

#endif

