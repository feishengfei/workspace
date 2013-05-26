#include <strstream>
#include <iomanip>

#include "debug_alloc.h"
#include "bdi_gps_event.h"

using namespace std;
using namespace BDI;

// BDI_GpsPosEvent members

BDI_GpsPosEvent::BDI_GpsPosEvent()
    : BDI_Event(UserGpsEvent)
{
    _bid = GPS_Position;
    _fixed = false;
    _marked = false;
}

BDI_GpsPosEvent::~BDI_GpsPosEvent()
{
}

string BDI_GpsPosEvent::toString() const
{
    ostrstream os;
    os << "[GPSEvent,Pos]";
    if (!_fixed)
        os << '!';
    else 
        os << _gpos.toString() << ','
           << _precision << ','
           << _error
           << (_marked ? "*" : "");
    os << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

void BDI_GpsPosEvent::setPosition(double lon, double lat, float sp, int co, int alt, int ctype, uint32 ts)
{
    _gpos._lon = lon;
    _gpos._lat = lat;
    _gpos._speed = sp;
    _gpos._course = co;
    _gpos._alt = alt;
    _gpos._coordType = static_cast<GPosition::CoordType>(ctype);
    _gpos._ts = ts;
}

// BDI_GpsViewEvent members

BDI_GpsViewEvent::BDI_GpsViewEvent()
    : BDI_Event(UserGpsEvent)
{
    _bid = GPS_SateView;
    _p = NULL;
    _nsates = 0;
}

BDI_GpsViewEvent::~BDI_GpsViewEvent()
{
    SateItemNode *p = _p;
    SateItemNode *pnext = NULL;
    while (p != NULL) {
        pnext = p->next;
        DELETE(p);
        p = pnext;
    }
}

string BDI_GpsViewEvent::toString() const
{
    SateItemNode *p = _p;
    ostrstream os;
    os << "[GPSEvent,View]" << endl 
       << _nsates;
    while (p != NULL) {
        os << endl << ",("
           << p->sa.sa_id << ','
           << p->sa.sa_elevation << ','
           << p->sa.sa_azimuth << ','
           << p->sa.sa_snr << ')';
        p = p->next;
    }
    os << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

void BDI_GpsViewEvent::addSateItem(int id, int el, int az, int snr)
{
    SateItemNode *p = _p;
    SateItemNode *pprev = NULL;

    if (id <= 0)
        return;

    while (p != NULL && p->sa.sa_id < id) {
        pprev = p;
        p = p->next;
    }

    if (p != NULL && p->sa.sa_id == id) {
        p->sa.sa_elevation = el;
        p->sa.sa_azimuth = az;
        p->sa.sa_snr = snr;
    }
    else {
        SateItemNode *newItem = NEW SateItemNode;
        if (newItem == NULL)
            return;
        newItem->sa.sa_id = id;
        newItem->sa.sa_elevation = el;
        newItem->sa.sa_azimuth = az;
        newItem->sa.sa_snr = snr;
        newItem->next = p;
        if (pprev != NULL)
            pprev->next = newItem;
        else
            _p = newItem;
        ++_nsates;
    }
}

void BDI_GpsViewEvent::addSateItem(const BDI_GpsViewEvent::SateItem &sa)
{
    addSateItem(sa.sa_id, sa.sa_elevation, sa.sa_azimuth, sa.sa_snr);
}
