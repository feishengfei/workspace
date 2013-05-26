#ifndef BDI_MG323_LINK_H
#define BDI_MG323_LINK_H

#include <pthread.h>

#include "bdi_link.h"

#define LINKBUFSIZE 1502 * 2

const char * const 	MG323_APN = "CMNET";
const char * const 	MG323_CONTYPE = "GPRS0";
const char * const 	MG323_PASSWORD = "gprs";
const char * const 	MG323_SRVTYPE = "Socket";
const int 			MG323_TCPMR = 10;	//available range: 1~30
const int 			MG323_TCPOT = 60;	//available range: 1~6000
const int			MG323_LINKCOUNT = 10;

class LinkBuf
{
public:
	LinkBuf();
	~LinkBuf();
	int writeData(unsigned char * pd, int sz);
	int readData(unsigned char * pd, int sz, int t);
	int dataSize();
	int spaceSize();
	bool wait(struct timeval *tv);

private:
	unsigned char _db[LINKBUFSIZE];
	int _in;
	int _out;
	int _rs;
	pthread_mutex_t _lock;
	pthread_cond_t _cond_wait;
	pthread_mutex_t _cond_wait_lock;
	pthread_cond_t _cond_read;
	pthread_mutex_t _cond_read_lock;
};

class BDI_MG323Link : public BDI_Link
{
public:
	BDI_MG323Link();
    ~BDI_MG323Link();

    void 	close(int);
    int 	connectHost( const char *ipaddr, uint16 port, int *s);
    int 	getBlock(int s, uint8 *buf, unsigned int sz);
    int 	putBlock(int s, uint8 *buf, unsigned int sz);
    int 	signalLevel();
    void 	setTimeOut(int secs);
    bool 	wait(int s, struct timeval *tv);
    void 	setEnabled(bool);
    Status 	status();

private:
	enum SidState{
		SID_CONNECTED,
		SID_CONNECTING,
		SID_BREAK
	};
	SidState _sidSt[MG323_LINKCOUNT];
	int _sid;
	fd_set _rfds;
	struct timeval _tv;
	bool _bRun;

	pthread_t _urcT;
	pthread_mutex_t _LockSt;					
	pthread_mutex_t _LockGetBlock[MG323_LINKCOUNT];	

	pthread_mutex_t _LockMg323;				 

	//pthread_mutex_t _LockDataReady[MG323_LINKCOUNT];				
	//pthread_cond_t _bWait[MG323_LINKCOUNT];		
	//pthread_cond_t _bGetFillBuf[MG323_LINKCOUNT];			

	pthread_cond_t _bPutReady[MG323_LINKCOUNT];			
	pthread_mutex_t _bPutReady_lock[MG323_LINKCOUNT];

	int _t;
	LinkBuf _linkBuf[MG323_LINKCOUNT];	
	//bool _bDataReady[MG323_LINKCOUNT];

private:
	void initURCThread( int tv_usec = 500 * 1000 );
	static void* urcThread(void *pvoid);
	int initSICS( int conProfileId );
	int initSISS( int srvProfileId, const char *ipaddr, unsigned int port);
	int setSICS( int conProfileId, const char *conParmTag, const char *conParmValue);
	int setSISS( int srvProfileId, const char *srvParmTag, const char *srvParmValue);
	int decode_urc( void *buf, int len, BDI_MG323Link *pLink );
};

#endif
