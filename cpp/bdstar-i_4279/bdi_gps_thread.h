#ifndef BDI_GPS_THREAD_H
#define BDI_GPS_THREAD_H

#include "bdi_thread.h"
#include "bdi_gps_event.h"

struct PosColPara
{
	int isUsing;
	int serveState;  //服务状态
	int sleepState;  //休眠状态
	int ref_PosReport; //多位置数据包报告频度
	int ref_PosCol1; //航线记录参考采样频度1
	int ref_PosCol2; //航线记录参考采样频度2
	int ref_GpsFix;  //GPS定位频度
	int thv_course;  //航向改变采样门限值
	int thv_speedZero;//速度零值门限值
	int thv_speedRun;//航行速度门限值
	int thv_posDiff;//定位点区分门限值
};

class BDI_GpsThread : public BDI_Thread
{
private:
    int _capifd;

    volatile int _gpsFixed;
    GPosition * _markedPos;

    BDI_GpsViewEvent::SateItem *_view;
    volatile int _gpsSateCount;

	PosColPara _colpara;
    // Threshold values to difference reporting position.
    int _thv_distance; // distance in meters
    int _thv_speed; // speed in Km/h
    int _thv_course; // course in degrees
    int _thv_time; // time in seconds
	bool _isUpdatePara;

private:
    void onUrcGpsx(void *);
    void onUrcGpsv(void *);
	void gpsxFilter(BDI_GpsPosEvent * e);

protected:
    void run();
    void onStopped();

public:
    BDI_GpsThread(BDI_Daemon *d);
    ~BDI_GpsThread();

    bool gpsFixed() const;
    int gpsSateCount() const;
	int loadGpsColPara();
	void updatePara();
	void printGpsColPara();
};

// BDI_GpsThread inline functions

inline bool BDI_GpsThread::gpsFixed() const
{ return _gpsFixed; }

inline int BDI_GpsThread::gpsSateCount() const
{ return _gpsSateCount; }

#endif
