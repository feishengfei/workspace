#include <stdio.h>
#include <errno.h>
#include <math.h>
#include <capi/capi_define.h>
#include <capi/ais.h>

#include <capi/capi.h>
#include <capi/bd_urc.h>
#include <capi/bd.h>

#include "debug_alloc.h"
#include "rbglobal.h"
#include "bdi_event_list.h"
#include "bdi_daemon.h"
#include "bdi_unix_stream.h"
#include "bdi_gps_event.h"
#include "bdi_gps_thread.h"
#include "bdi_posopt.h"
#include "config.h"

// BDI_GpsThread members
void BDI_GpsThread::onUrcGpsx(void *ptr)
{
	bd_gpsx_t *xbuf = reinterpret_cast<bd_gpsx_t *>(ptr);
	Ref<BDI_GpsPosEvent> gpe = NEW BDI_GpsPosEvent;

	if (gpe.isNull())
		return;

	gpe->setPosition(xbuf->lon.deg + xbuf->lon.min / 60.0 + xbuf->lon.sec / 3600.0 + xbuf->lon.tenth / 36000.0,
	                 xbuf->lat.deg + xbuf->lat.min / 60.0 + xbuf->lat.sec / 3600.0 + xbuf->lat.tenth / 36000.0,
	                 xbuf->v * 0.36,
	                 xbuf->dir * 2,
	                 xbuf->alt,
	                 GPosition::WGS84,
	                 xbuf->time);
	gpe->setPrecision(xbuf->precision);
	gpe->setError(xbuf->error);
	gpe->setFixed(xbuf->state == 0);

	gpsxFilter(gpe.getPtr());

	/*if (!gpe->fixed())
	    ; // do nothing
	else if (!_gpsFixed)
	    gpe->mark();
	else {
	    // check if is a reportable position
	    if (fabsf(gpe->gpos()._speed - _markedPos._speed) >= _thv_speed)
	        gpe->mark();
	    else if (gpe->gpos()._speed > 5
	             && _markedPos._speed > 5
	             && abs(gpe->gpos()._course - _markedPos._course) >= _thv_course)
	        gpe->mark();
	    else {
	        int secs = _thv_time;
	        if (gpe->gpos()._speed > 0.001)
	            secs = static_cast<int>(_thv_distance / gpe->gpos()._speed * 3.6 + 0.5);

	        if (secs <= 0)
	            secs = 1;
	        else if (secs > _thv_time)
	            secs = _thv_time;

	        if (gpe->gpos()._ts - _markedPos._ts >= secs)
	            gpe->mark();
	    }
	}

	_gpsFixed = gpe->fixed();

	// Save the marked position
	if (gpe->marked())
	    _markedPos = gpe->gpos();

	if (gpe->marked() || (gpe->gpos()._ts - timeSave >= GUI_REFRESH_TIME)) {
	    _d->rtdataList()->addEvent(gpe.getPtr(), NULL);
	    timeSave = gpe->gpos()._ts;
	}*/
}

void BDI_GpsThread::onUrcGpsv(void *ptr)
{
	static time_t timeSave = 0;
	int i;
	bd_gpsv_t *vbuf = reinterpret_cast<bd_gpsv_t *>(ptr);
	Ref<BDI_GpsViewEvent> ve = NEW BDI_GpsViewEvent;

	if (ve.isNull())
		return;

	if (_view != NULL) {
		DELETE_ARR(_view);
		_view = NULL;
	}

	if (vbuf->nr_sv > 0) {
		_view = NEW BDI_GpsViewEvent::SateItem[vbuf->nr_sv];
		_gpsSateCount = vbuf->nr_sv;
	}

	if (_view == NULL) {
		_gpsSateCount = 0;
		return;
	}

	for (i = 0; i < vbuf->nr_sv; ++i)
		ve->addSateItem(vbuf->sv[i].id, vbuf->sv[i].el, vbuf->sv[i].az, vbuf->sv[i].sn);

	BDI_GpsViewEvent::Iterator it = ve->begin();
	i = 0;

	for (; it != ve->end(); ++it, ++i)
		_view[i] = *it;

	time_t tv = time(NULL);

	if (tv - timeSave >= _colpara.ref_GpsFix) {
		_d->rtdataList()->addEvent(ve.getPtr(), NULL);
		timeSave = tv;
	}
}

void BDI_GpsThread::run()
{
	BDI_UnixStreamLink us; // file socket read/write
	uint8 hdrbuf[32];
	const int URC_HEADER_LEN = sizeof(urc_hdr_t);

	RB_DEBUG("gps-thread start running... (%ld)", static_cast<long>(_tid));

	for (;;) {
		int health = 1;
		// connects to capi server
		_capifd = capi_get_urcserver(-1);

		if (_capifd < 0) {
			health = -1;
			RB_WARNING("gps-thread|Fail to register capi.");
		}

		while (health > 0) {
			if(_isUpdatePara){
				loadGpsColPara();
			}

			struct timeval minute;
			minute.tv_sec = 60;
			minute.tv_usec = 0;
			fd_set fdSet;
			FD_ZERO(&fdSet);
			FD_SET(_capifd, &fdSet);

			int ret = select(_capifd + 1, &fdSet, NULL, NULL, &minute);

			if (ret == 0) {
				_gpsFixed = 0;
				_d->rtdataList()->addEvent(NEW BDI_GpsPosEvent, NULL);
				continue;
			} else if (ret < 0 && errno != EINTR)
				break;

			// get URC header
			if (us.getBlock(_capifd, hdrbuf, URC_HEADER_LEN) != 0) {
				RB_WARNING("gps-thread|Fail to read capi URC header.");
				break;
			}

			urc_hdr_t *phdr = reinterpret_cast<urc_hdr_t *>(hdrbuf);

			if (phdr->magic != URC_MAGIC) {
				RB_WARNING("gps-thread|Bad capi URC.");
				break;
			}

			// get the URC frame body
			const int bodylen = phdr->len - URC_HEADER_LEN;
			uint8 *bodybuf = NEW uint8[bodylen];

			if (bodybuf == NULL)
				break;

			if (us.getBlock(_capifd, bodybuf, bodylen) != 0) {
				DELETE_ARR(bodybuf);
				RB_WARNING("gps-thread|Fail to read capi URC.");
				break;
			}

			if (phdr->type == URC_GPS) {
				switch (phdr->event) {
				case URC_BD_GPSX:
					onUrcGpsx(bodybuf);
					break;

				case URC_BD_GPSV:
					onUrcGpsv(bodybuf);
					break;

				default:
					RB_DEBUG("gps-thread|Unhandled capi URC: %d", phdr->type);
					break;
				}
			}

			DELETE_ARR(bodybuf);
		}

		if (_capifd > 0) {
			capi_put_urcserver(_capifd);
			_capifd = -1;
		}

		sleep(2);
	}
}

void BDI_GpsThread::onStopped()
{
	BDI_Thread::onStopped();

	if (_capifd != -1) {
		capi_put_urcserver(_capifd);
		_capifd = -1;
	}

	if (_view != NULL) {
		DELETE_ARR(_view);
		_view = NULL;
	}

	_gpsFixed = 0;
	_gpsSateCount = 0;
}

int BDI_GpsThread::loadGpsColPara()
{
	Config conf;

	//while(_colpara.isUsing != 0) {
		//usleep(10);
	//}

	_colpara.isUsing = 1;
	if(!conf.open(BDI_SYSPARA_FILE)) {
		initParaConfFile();
	}

	if(conf.open(BDI_SYSPARA_FILE)) {
		conf.setGroup("SYSPARA");
		_colpara.serveState = int(conf.getIntValue("serveState"));
		_colpara.sleepState = int(conf.getIntValue("sleepState"));
		_colpara.ref_PosReport = int(conf.getIntValue("ref_PosReport"));
		_colpara.ref_PosCol1 = int(conf.getIntValue("ref_PosCol1"));
		_colpara.ref_PosCol2 = int(conf.getIntValue("ref_PosCol2"));
		_colpara.ref_GpsFix = int(conf.getIntValue("ref_GpsFix"));
		_colpara.thv_course = int(conf.getIntValue("thv_course"));
		_colpara.thv_speedZero = int(conf.getIntValue("thv_speedZero"));
		_colpara.thv_speedRun = int(conf.getIntValue("thv_speedRun"));
		_colpara.thv_posDiff = int(conf.getIntValue("thv_posDiff"));
	}
	_colpara.isUsing = 0;
	_isUpdatePara = false;
	printGpsColPara();
	return 0;
}

void BDI_GpsThread::printGpsColPara()
{
	fprintf(stderr, "GpsColPara:\r\n");
	fprintf(stderr, "serveState:%d\r\n", _colpara.serveState);
	fprintf(stderr, "sleepState:%d\r\n", _colpara.sleepState);
	fprintf(stderr, "ref_PosReport:%d\r\n", _colpara.ref_PosReport);
	fprintf(stderr, "ref_PosCol1:%d\r\n", _colpara.ref_PosCol1);
	fprintf(stderr, "ref_PosCol2:%d\r\n", _colpara.ref_PosCol2);
	fprintf(stderr, "ref_GpsFix:%d\r\n", _colpara.ref_GpsFix);
	fprintf(stderr, "thv_speedZero:%d\r\n", _colpara.thv_speedZero);
	fprintf(stderr, "thv_speedRun:%d\r\n", _colpara.thv_speedRun);
	fprintf(stderr, "thv_posDiff:%d\r\n", _colpara.thv_posDiff);
}

void BDI_GpsThread::gpsxFilter(BDI_GpsPosEvent *e )
{
	static time_t timeSave = 0;

	if (e->gpos()._ts - timeSave < (unsigned int)_colpara.ref_GpsFix)
		return;

	timeSave = e->gpos()._ts;

	if (!e->fixed())
		; // do nothing
	else if (NULL == _markedPos) {
		e->mark();
		_markedPos = NEW GPosition;
		memcpy(_markedPos, &(e->gpos()), sizeof(GPosition));
	} else {
		// check if is a reportable position
		int dd; //distance, in 1/10 meter
		int dc; //course
		int dt; //time

		dt = e->gpos()._ts - _markedPos->_ts;
		dd = get_dist(int(_markedPos->_lon * 10000), int(_markedPos->_lat * 10000), int(e->gpos()._lon * 10000), int(e->gpos()._lat * 10000));
		dc = e->gpos()._course - _markedPos->_course;

		if (dc < 0) dc = -dc;

		dc %= 360;

		if (dc > 180) dc = 360 - dc;

		//printGpsColPara();
		//fprintf(stderr, "e:%d, m%d, d:%d\r\n", e->gpos()._course, _markedPos->_course, dc);
		//RB_DEBUG("distance = %d, course = %d, dtime = %d", dd, dc, dt);

		if(dt > _colpara.ref_PosCol2 * 60)
			e->mark();
		else if(dd > _colpara.thv_posDiff * 100) {
			if(dt > _colpara.ref_PosCol1 * 60)
				e->mark();
			else if(e->gpos()._speed * 10 > _colpara.thv_speedZero && dc > _colpara.thv_course)
				e->mark();
		}
	}

	_gpsFixed = e->fixed();

	// Save the marked position
	if (e->marked()){
		RB_DEBUG("%s:%d,pos marked", __FILE__, __LINE__);
		memcpy(_markedPos, &(e->gpos()), sizeof(GPosition));
	}

	_d->rtdataList()->addEvent(e, NULL);
}

void BDI_GpsThread::updatePara()
{
	_isUpdatePara = true;
}

BDI_GpsThread::BDI_GpsThread(BDI_Daemon *d)
	: BDI_Thread(d)
{
	_capifd = -1;
	_gpsFixed = 0;
	_view = NULL;
	_gpsSateCount = 0;

	_colpara.isUsing = 1;

	_isUpdatePara = false;
	_markedPos = NULL;
	loadGpsColPara();
}

BDI_GpsThread::~BDI_GpsThread()
{
	if (_view != NULL)
		DELETE_ARR(_view);

	if (_markedPos != NULL)
		DELETE(_markedPos);
}
