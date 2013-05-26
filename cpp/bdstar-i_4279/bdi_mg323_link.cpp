#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <unistd.h>
#include <errno.h>

#include <mapi/mapi.h>
#include <mapi/gsm.h>
#include <mapi/gsm_req.h>
#include "bdi_mg323_link.h"

using namespace BDI;
void dump(void *buf, int len)
{
	unsigned char *p = (unsigned char *)buf;
	int i;

	for (i = 0; i < len; i++) {
		if (i % 16 == 0) printf("%04x:", i);
		if (i % 16 == 8) printf(" -");
		printf(" %02x", p[i]);
		if (i % 16 == 15) printf("\n");
	}
	if (i % 16) printf("\n");
}

LinkBuf::LinkBuf()
{
	_in = 0;
	_out = 0;
	pthread_mutex_init(&_lock, NULL);
	pthread_mutex_init(&_cond_wait_lock, NULL);
	pthread_cond_init(&_cond_wait, NULL);
	pthread_mutex_init(&_cond_read_lock, NULL);
	pthread_cond_init(&_cond_read, NULL);
}

LinkBuf::~LinkBuf()
{
}

int LinkBuf::writeData(unsigned char *pd, int sz)
{
	assert(NULL != pd && sz > 0 && sz < LINKBUFSIZE);

	int ss = spaceSize();
	if (sz > ss) {
		printf("Link Buf don't have enough space, space = %d\n", ss);
		return -1;
	}
	pthread_mutex_lock(&_lock);
	if ((LINKBUFSIZE - (_in % LINKBUFSIZE)) > sz) {
		memcpy(&(_db[_in % LINKBUFSIZE]), pd, sz);
	} else {
		int es = (LINKBUFSIZE - (_in % LINKBUFSIZE));
		memcpy(&(_db[_in % LINKBUFSIZE]), pd, es);
		memcpy(_db, pd + es, sz - es);
	}
	_in += sz;
	if(_rs != -1 && _rs <= (_in - _out)){
		pthread_cond_signal(&_cond_read);
		_rs = -1;
	}

	pthread_mutex_unlock(&_lock);
	dump(pd, sz);

	pthread_cond_signal(&_cond_wait);
	return sz;
}

int LinkBuf::readData(unsigned char *pd, int sz, int t)
{
	assert(NULL != pd);
	assert(sz <= LINKBUFSIZE);
	/*int ds = dataSize();
	if(ds < sz){
		printf("linkbuf don't have enough data, datasize = %d\n", ds);
		return -1;
	}*/
	pthread_mutex_lock(&_lock);
	if ((_in - _out) < sz) {
		_rs = sz;
		pthread_mutex_lock(&_cond_read_lock);
		pthread_mutex_unlock(&_lock);
		struct timespec ct;
		ct.tv_sec = time(NULL) + t;
		if( ETIMEDOUT == pthread_cond_timedwait(&_cond_read, &_cond_read_lock, &ct)){
			pthread_mutex_unlock(&_cond_read_lock);
			return -2;
		}
		pthread_mutex_unlock(&_cond_read_lock);
	} else {
		pthread_mutex_unlock(&_lock);
	}
	pthread_mutex_lock(&_lock);
	if ((LINKBUFSIZE - (_out % LINKBUFSIZE)) >= sz) {
		memcpy(pd, &(_db[_out % LINKBUFSIZE]), sz);
	} else {
		int ed = LINKBUFSIZE - (_out % LINKBUFSIZE);
		memcpy(pd, &(_db[_out % LINKBUFSIZE]), ed);
		memcpy(pd + ed, _db, sz - ed);
	}
	_out += sz;
	if (_in == _out) {
		_in = 0;
		_out = 0;
	}
	pthread_mutex_unlock(&_lock);
	return 0;
}

int LinkBuf::dataSize()
{
	int i, o;
	pthread_mutex_lock(&_lock);
	i = _in;
	o = _out;
	pthread_mutex_unlock(&_lock);

	return i - o;
}

int LinkBuf::spaceSize()
{
	int i, o;
	pthread_mutex_lock(&_lock);
	i = _in;
	o = _out;
	pthread_mutex_unlock(&_lock);

	if (i == o)
		return LINKBUFSIZE;
	return (LINKBUFSIZE - (i - o));
}

bool LinkBuf::wait(struct timeval *tv)
{
	bool ret = true;
	int len;
	pthread_mutex_lock(&_lock);
	len = _in - _out;
	pthread_mutex_unlock(&_lock);
	if (len > 0)
		return true;

	struct timespec t;
	t.tv_sec = time(NULL) + tv->tv_sec;
	t.tv_nsec = tv->tv_usec * 1000;
	pthread_mutex_lock( &_cond_wait_lock );
	if ( ETIMEDOUT==pthread_cond_timedwait(&_cond_wait, &_cond_wait_lock, &t) ) {
		ret = false;
	}
	pthread_mutex_unlock( &_cond_wait_lock );
	return ret;
}

BDI_MG323Link::BDI_MG323Link()
{
	/* init */
	_t = 30;
	pthread_mutex_init( &_LockSt, NULL );
	pthread_mutex_init( &_LockMg323, NULL );

	for ( int i = 0; i < MG323_LINKCOUNT; i++ ) {
		//pthread_cond_init( &(_bWait[i]), NULL );
		//pthread_cond_init( &(_bGetFillBuf[i]), NULL );
		//pthread_mutex_init( &_LockDataReady[i], NULL );
		pthread_mutex_init( &_LockGetBlock[i], NULL );
		pthread_mutex_init( &_bPutReady_lock[i], NULL );
		pthread_cond_init( &_bPutReady[i], NULL);
	}

	pthread_mutex_lock( &_LockSt );
	for ( int i = 0; i < MG323_LINKCOUNT; i++ ) {
		_sidSt[i] = SID_BREAK;
	}
	pthread_mutex_unlock( &_LockSt );

	initURCThread( ); //500ms

	initSICS( 0 );
	//if (ret) return ret;
}

BDI_MG323Link::~BDI_MG323Link()
{
	int ret = 0;
	ret = mapi_unregister_client();
	if (ret)
		fprintf(stderr, "mapi_unregister_client() = %d\n", ret);

}

void BDI_MG323Link::initURCThread( int tv_usec )
{

	int ret = 0;
	ret = mapi_register_client();
	printf("register to mapi ret = %d\n", ret);

	while (ret) {
		fprintf(stderr, "mapi_register_client() = %d\n", ret);
		sleep(1);
		ret = mapi_register_client();
		printf("register to mapi ret = %d\n", ret);
	}

	_sid = mapi_get_urcserver(-1);
	printf("get _sid = %d\n", _sid);
	_tv.tv_sec = 0;
	_tv.tv_usec = tv_usec;
	_bRun = true;

	if ( 0!=pthread_create(&_urcT, NULL, &urcThread, this) ) {
		fprintf(stderr, "create urcT thread error\n");
	}
}

void* BDI_MG323Link::urcThread(void *pvoid)
{
	BDI_MG323Link *link = (BDI_MG323Link*)pvoid;
	struct timeval tv;
	while ( link->_bRun ) {

		FD_ZERO( &(link->_rfds) );
		FD_SET( link->_sid, &(link->_rfds) );

		int ret = 0;
		//tv.tv_sec = link->_tv.tv_sec;
		//tv.tv_usec = link->_tv.tv_usec;
		tv.tv_sec = 1;
		tv.tv_usec = 0;
		ret = select( link->_sid + 1, &(link->_rfds), NULL, NULL, &tv );
		if (ret < 0) {/*error*/
			if (errno == EINTR || errno == EAGAIN) {
				printf("intr.\n");
			}
			printf("select: %s(fd = %d)\n", strerror(errno), link->_sid);
			//TODO
		} else if (0 == ret) {/*time out*/
			//TODO
			gsm_sisr_t r;
			pthread_mutex_lock(&(link->_LockSt));
			for (int i = 0; i < MG323_LINKCOUNT; i++) {
				if (SID_CONNECTED == link->_sidSt[i]) {
					memset( &r, 0, sizeof(gsm_sisr_t) );
					r.srvProfileId = i;
					r.reqReadLength = 0;

					printf("time out and check s[%d] datacount\n", i);
					pthread_mutex_lock( &(link->_LockMg323) );
					ret = mapi_gsm_sisr( &r );
					if (ret) {
						fprintf(stderr, "Read datacount SISR ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
					}else{
						printf("send check req ok\n");
					}
					pthread_mutex_unlock( &(link->_LockMg323) );
				}
			}
			pthread_mutex_unlock(&(link->_LockSt));
		} else {/*has something to do*/
			unsigned char tmpbuf[_IO_BUFSIZ*2];
			int len;
			urc_hdr_t *phdr = (urc_hdr_t *)tmpbuf;
			memset( tmpbuf, 0, sizeof( tmpbuf ) );
			if (ret > 0)
				len = read(link->_sid, tmpbuf, sizeof( urc_hdr_t ) );
			if (len != sizeof( urc_hdr_t ) ) {
				fprintf(stderr, "_sid read header return %d\n", len);
				break;
			}
			if ( phdr->magic != URC_MAGIC ) {
				fprintf(stderr, "not urc packet: magic = %08x\n", phdr->magic);
				read( link->_sid, tmpbuf, sizeof( tmpbuf ) );
				continue;
			}
			len = read( link->_sid, tmpbuf + sizeof( urc_hdr_t ), phdr->len - sizeof( urc_hdr_t ));
			if ( len != (int)( phdr->len - sizeof( urc_hdr_t ) ) ) {
				fprintf(stderr, "short packet %d\n", len);
				continue;
			}
			link->decode_urc( tmpbuf, phdr->len, link );
		}

	}
	pthread_exit(NULL);
}

void BDI_MG323Link::close(int s)
{
	int ret = 0;
	if ( s < 0 || s > 9) {
		s = 0;
	}
	gsm_sisc_t r;
	r.srvProfileId = s;
	pthread_mutex_lock( &_LockMg323 );
	ret = mapi_gsm_sisc( &r );
	pthread_mutex_unlock( &_LockMg323 );
	do {
		if (ret) {
			fprintf(stderr, "SISC ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
			ret = mapi_gsm_sisc(&r);
			sleep(1);
		} else {
			pthread_mutex_lock( &_LockSt);
			_sidSt[s] = SID_BREAK;
			pthread_mutex_unlock( &_LockSt);
		}
	} while (ret != 0);
}

int BDI_MG323Link::connectHost(const char *ipaddr, uint16 port, int *s)
{
	int ret = 0;
	int sid = -1;

	fprintf(stderr, "<BDI_MG323Link::connectHost> %s:%d\n", ipaddr, port);

	//get the first NoConnection
	pthread_mutex_lock( &_LockSt );
	for ( int i = 0; i < MG323_LINKCOUNT; i++) {
		if ( SID_BREAK == _sidSt[i] ) {
			_sidSt[i] = SID_CONNECTING;
			sid = i;
			break;
		}
	}
	//if no sid is free
	if (	-1 == sid ) {
		return SYS_SYSTEM_FAULT;
	}
	*s = sid;
	pthread_mutex_unlock( &_LockSt );

	pthread_mutex_lock( &_LockMg323 );
	pthread_mutex_lock( &_bPutReady_lock[sid]);
	ret = initSISS( sid, ipaddr, port );

	gsm_siso_t r;
	memset( &r, 0, sizeof(gsm_siso_t) );
	r.srvProfileId = sid;
	ret = mapi_gsm_siso(&r);
	pthread_mutex_unlock( &_LockMg323 );
	if (0 != ret) {
		fprintf(stderr, "SISO ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
		pthread_mutex_lock( &_LockSt );
		_sidSt[sid] = SID_BREAK;
		pthread_mutex_unlock( &_LockSt );
		pthread_mutex_unlock( &_bPutReady_lock[sid]);
	} else {
		struct timespec t;
		t.tv_sec = time(NULL) + 10;
		t.tv_nsec = 0;
		if ( ETIMEDOUT == pthread_cond_timedwait( &(_bPutReady[sid]), &_bPutReady_lock[sid], &t) ) {
			fprintf(stderr, "WAIT SISWA TIMEOUT\n");
			close(sid);
			ret = SYS_SYSTEM_FAULT;
		} else {
			pthread_mutex_lock( &_LockSt );
			_sidSt[sid] = SID_CONNECTED;
			pthread_mutex_unlock( &_LockSt );
		}
		pthread_mutex_unlock( &_bPutReady_lock[sid] );
	}

	//XXX return Fault, No_answer or NetworkUnreachable
	return ret;
}


int BDI_MG323Link::getBlock(int s, uint8 *buf, unsigned int sz)
{
	return _linkBuf[s].readData(buf, sz, _t);
	/*pthread_mutex_lock( &_LockDataReady[s] );
	_bDataReady[s] = false;
	pthread_mutex_unlock( &_LockDataReady[s] );
	int ret = 0;
	if ( s < 0 || s > 9) {
		s = 0;
	}
	gsm_sisr_t r;
	memset( &r, 0, sizeof(gsm_sisr_t) );
	r.srvProfileId = s;
	r.reqReadLength = sz;

	pthread_mutex_lock( &_LockMg323 );
	ret = mapi_gsm_sisr( &r );
	if (ret) {
		fprintf(stderr, "SISR ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
		return -1;
	}
	pthread_mutex_unlock( &_LockMg323 );


	// wait for filling buf
	pthread_mutex_lock( &_LockDataReady[s] );
	bool ready = _bDataReady[s];
	pthread_mutex_unlock( &_LockDataReady[s] );

	if ( !ready ) {
		pthread_mutex_lock( &_LockGetBlock[s] );
		struct timespec t;
		t.tv_sec = time(NULL) + _t;
		t.tv_nsec = 0;
		if ( ETIMEDOUT == pthread_cond_timedwait( &(_bGetFillBuf[s]), &_LockGetBlock[s], &t) ) {
			fprintf(stderr, "WAIT FILLING BUF TIMEOUT\n");
			ret = -2;
		}
		pthread_mutex_unlock( &_LockGetBlock[s] );
		if ( -2 == ret ) {
			return ret;
		}
	}

	// copying data
	memcpy( buf, _linkBuf[s], sz );
	memset( _linkBuf[s], 0, 1502 );
	return ret;*/
}

int BDI_MG323Link::putBlock(int s, uint8 *buf, unsigned int sz)
{
	int ret = 0;
	if ( s < 0 || s > 9) {
		s = 0;
	}
	if ( sz > 1500 ) sz = 1500;

	//pthread_mutex_lock( &_LockSt );
	//int st = _sidSt[s];
	//pthread_mutex_unlock( &_LockSt );

	gsm_sisw_t r;
	memset( &r, 0, sizeof(gsm_sisw_t) );
	r.srvProfileId = s;
	r.reqWriteLength = sz;
	memcpy( r.data, buf, r.reqWriteLength );
	pthread_mutex_lock( &_LockMg323);
	ret = mapi_gsm_sisw(&r);
	pthread_mutex_unlock( &_LockMg323);
	if (ret) {
		fprintf(stderr, "SISW ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
	}
	return ret;
}

int BDI_MG323Link::signalLevel()
{
	return 5;
}

void BDI_MG323Link::setTimeOut(int secs)
{
	_t = secs;
}

bool BDI_MG323Link::wait(int s, struct timeval *tv)
{
	return _linkBuf[s].wait(tv);
	/*bool ret = true;
	struct timespec t;
	t.tv_sec = time(NULL) + tv->tv_sec;
	t.tv_nsec = tv->tv_usec * 1000;
	pthread_mutex_lock( &_LockGetBlock[s] );
	if ( ETIMEDOUT==pthread_cond_timedwait(&(_bWait[s]), &_LockGetBlock[s], &t) ) {
		fprintf(stderr, "wait(%d) WAITING TIMEOUT\n", s);
		ret = false;
	}
	pthread_mutex_unlock( &_LockGetBlock[s] );
	return ret;*/
}

BDI_Link::Status BDI_MG323Link::status()
{
	//TODO
	return BDI_Link::Connected;
}

void BDI_MG323Link::setEnabled( bool b )
{
	//TODO
	b = b;
}


int BDI_MG323Link::initSICS( int id )
{
	int ret = 0;
	ret = setSICS( id, "conType", MG323_CONTYPE );
	ret = setSICS( id, "passwd", MG323_PASSWORD );
	ret = setSICS( id, "apn", MG323_APN );
	return ret;
}

int BDI_MG323Link::initSISS( int id, const char *ipaddr, unsigned int port )
{
	int ret = 0;
	char str[64];
	ret = setSISS( id, "srvType", MG323_SRVTYPE );
	sprintf(str, "%d", 0 );
	ret = setSISS( id, "conId", str );
	sprintf( str, "\"socktcp://%s:%d\"", ipaddr, port );
	ret = setSISS( id, "address", str );
	sprintf( str, "%d", MG323_TCPMR );
	ret = setSISS( id, "tcpMR", str );
	sprintf( str, "%d", MG323_TCPOT );
	ret = setSISS( id, "tcpOT", str );
	return ret;
}

int BDI_MG323Link::setSICS( int id, const char *tag, const char *value)
{
	int ret = 0;
	gsm_sics_t r;
	memset( &r, 0, sizeof(gsm_sics_t) );

	r.conProfileId = id;
	strcpy( r.conParaTag, tag );
	strcpy( r.conParmValue, value );

	ret = mapi_gsm_sics(&r);
	if (ret) {
		fprintf( stderr, "SICS ERROR:%d:"
		         "conProfileId=%d "
		         "conParaTag=%s "
		         "conParmValue=%s\n",
		         ret, r.conProfileId, r.conParaTag, r.conParmValue );
	}

	return ret;
}

int BDI_MG323Link::setSISS( int id, const char *tag, const char *value)
{
	int ret = 0;
	gsm_siss_t r;
	memset( &r, 0, sizeof(gsm_siss_t) );

	r.srvProfileId = id;
	strcpy(r.srvParmTag, tag);
	strcpy(r.srvParmValue, value);

	ret = mapi_gsm_siss(&r);
	if (ret) {
		fprintf( stderr, "SISS ERROR:%d:"
		         "srvProfileId=%d "
		         "srvParmTag=%s "
		         "srvParmValue=%s\n",
		         ret, r.srvProfileId, r.srvParmTag, r.srvParmValue);
	}
	return ret;
}

int BDI_MG323Link::decode_urc( void *buf, int len, BDI_MG323Link *pLink )
{
	urc_hdr_t *phdr = (urc_hdr_t *)buf;
	void *rbuf = (unsigned char *)buf + sizeof( urc_hdr_t );
	int rlen = len - sizeof( urc_hdr_t );
	//rlen = rlen;
	if ( phdr->type == URC_GSM ) {
		switch ( phdr->event ) {
		case REQ_GSM_SISRA:
			gsm_sisra_t sisra;
			memcpy( &sisra, rbuf, rlen);

			//dump( rbuf, sizeof(gsm_sisra_t) );
			/*	has sth to read,
				you decide how much you will read*/
#if 1
			printf(	"33sisra:\n"
			        "	srvProfileId:%d\n "
			        "	reqReadLength:%d\n "
			        "	remainUdpPacketLength:%d\n "
			        "	urdCauseDesc:%s\n "
			        "	readedLength:%d\n ",
			        //"	data:%s\n ",
			        sisra.srvProfileId,
			        sisra.reqReadLength,
			        sisra.remainUdpPacketLength,
			        sisra.urdCauseDesc,
			        sisra.readedLength);
			//sisra.data );
#endif
			if ( sisra.reqReadLength > 1
			        && sisra.readedLength == 0 ) {
				int lsz = _linkBuf[sisra.srvProfileId].spaceSize();
				if(lsz > 0){
					gsm_sisr_t r;
					memset( &r, 0, sizeof(gsm_sisr_t) );
					r.srvProfileId = sisra.srvProfileId;
					r.reqReadLength = lsz > 1500?1500:lsz;

					pthread_mutex_lock( &_LockMg323 );
					int ret;
					ret = mapi_gsm_sisr( &r );
					if (ret) {
						fprintf(stderr, "SISR ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
					} else {
						printf("sisr ok %d: srvProfileId=%d\n", ret, r.srvProfileId);
					}
					pthread_mutex_unlock( &_LockMg323 );
				} else {
					printf("linkbuf[%d] is full\n", sisra.srvProfileId);
				}
				//pthread_cond_signal(
				//&(pLink->_bWait[sisra.srvProfileId]) );
			}
			/*
				 exactly LEN data can be read
			 */
			else if ( sisra.reqReadLength == 1
			          && sisra.readedLength == 0 ) {
				//TODO
				gsm_sisr_t r;
				memset( &r, 0, sizeof(gsm_sisr_t) );
				r.srvProfileId = sisra.srvProfileId;
				r.reqReadLength = 1500;

				printf("received data and check s[%d] datacount\n", sisra.srvProfileId);
				pthread_mutex_lock( &_LockMg323 );
				int ret;
				ret = mapi_gsm_sisr( &r );
				if (ret) {
					fprintf(stderr, "Read datacount SISR ERROR %d: srvProfileId=%d\n", ret, r.srvProfileId);
				}
				pthread_mutex_unlock( &_LockMg323 );
			}
			/*
			   you got what you want finally!!!
			 */
			else if ( sisra.readedLength != 0 ) {
				_linkBuf[sisra.srvProfileId].writeData(sisra.data, sisra.readedLength);
				/*pthread_mutex_lock( &_LockDataReady[sisra.srvProfileId] );
				_bDataReady[sisra.srvProfileId] = true;
				memcpy( pLink->_linkBuf[sisra.srvProfileId],
				        sisra.data, sisra.readedLength );
				pthread_mutex_unlock( &_LockDataReady[sisra.srvProfileId] );

				pthread_cond_signal(
				    &(pLink->_bGetFillBuf[sisra.srvProfileId]) );*/
			}
			break;
		case REQ_GSM_SISWA:
			gsm_siswa_t siswa;
			memcpy( &siswa, rbuf, sizeof(gsm_siswa_t) );
#if 1
			printf( "siswa:\n"
			        "	srvProfileId:%d\n"
			        "	reqWriteLength:%d\n"
			        "	unackData:%d\n",
			        siswa.srvProfileId,
			        siswa.reqWriteLength,
			        siswa.unackData );
#endif
			//pthread_mutex_lock( &_LockSt );
			//_sidSt[siswa.srvProfileId] = SID_CONNECTED;
			//pthread_mutex_unlock( &_LockSt );
			pthread_mutex_lock( &_bPutReady_lock[siswa.srvProfileId]);
			pthread_cond_signal( &_bPutReady[siswa.srvProfileId]);
			pthread_mutex_unlock( &_bPutReady_lock[siswa.srvProfileId]);
			break;
		case REQ_GSM_SIS:
			gsm_sis_t sis;
			memcpy( &sis, rbuf, sizeof(gsm_sis_t) );
			if ( sis.urcInfoId >0 && sis.urcInfoId <= 2000 ) {
				pthread_mutex_lock( &_LockSt );
				_sidSt[siswa.srvProfileId] = SID_BREAK;
				pthread_mutex_unlock( &_LockSt );
			} else if ( sis.urcInfoId <= 4000 ) {
			} else if ( sis.urcInfoId <= 6000 ) {
			} else if ( sis.urcInfoId <= 8000 ) {
			}
#if 1
			printf( "sis:\n"
			        "	srvProfileId:%d\n"
			        "	urcCause:%d\n"
			        "	urcInfoId:%d\n"
			        "	urcInfoText:%s\n",
			        sis.srvProfileId,
			        sis.urcCause,
			        sis.urcInfoId,
			        sis.urcInfoText );
#endif
			break;
		default:
			fprintf(stderr, "unhandle GSM event %x\n", phdr->event );
			break;
		}
	}

	return 0;
}

