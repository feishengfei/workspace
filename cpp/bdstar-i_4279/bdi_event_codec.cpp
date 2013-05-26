#include <string.h>

#include <strstream>
#include <iomanip>

#include "debug_alloc.h"
#include "rbglobal.h"
#include "bdi_util.h"
#include "bdi_event_codec.h"

using namespace std;
using namespace BDI;

// GPosition members

GPosition::GPosition()
{
    _ts = 0;
    _lon = 0.0;
    _lat = 0.0;
    _speed = 0.0;
    _course = 0;
    _turnRate = 0.0;
    _alt = 0;

    _locMode = static_cast<GPosition::LocMode>(-1);
    _locStatus = static_cast<GPosition::LocStatus>(-1);
    _coordType = static_cast<GPosition::CoordType>(-1);
    _pType = static_cast<GPosition::PointType>(-1);
}

string GPosition::toString() const
{
    ostrstream os;
    os << '(' 
       << '\"' << timevToStr(_ts) << "\","
       << setprecision(5) << _lon << ',' << _lat << ','
       << setprecision(1) << _speed << "km/h,"
       << _course << ','
       << _turnRate << "deg/s,"
       << _alt << "m,";

	switch( _locMode ) {
		case FIX_GPS:
			os << "GPS";
			break;
		case FIX_BD1:
			os << "BD1";
			break;
		case FIX_BD2:
			os << "BD2";
			break;
		case FIX_GALILEO:
			os << "GALILEO";
			break;
		case FIX_GLONASS:
			os << "GLONASS";
			break;
		case FIX_GPSONE:
			os << "GPSOne";
			break;
		case FIX_ANY:
			os << "ANY";
			break;
		case FIX_OTHER:
			os << "OTHER";
			break;
		default:
			os << _locMode;
			break;
	}
	os << ',';

    if (_locStatus == FIXED)
        os << "FIXED";
    else if (_locStatus == UNFIXED)
        os << "UNFIXED";
    else 
        os << _locStatus;
	os << ',';

    if (_coordType == GPosition::BJ54)
        os << "BJ54";
    else if (_coordType == GPosition::WGS84)
        os << "WGS84";
    else 
        os << _coordType;
	os << ',';

    if (_pType == UNPROCESS)
        os << "UNPROCESS";
    else if (_pType == GOODP)
        os << "GOODPOINT";
    else 
        os << _pType;

    os << ')' << ends;

    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

// BDDevName members

BDDevName::BDDevName()
{
    _addrType = -1;
    _addrStyle = -1;
    memset(_devNum, 0, 19);
}

void BDDevName::setDevName(int type, int style, const char *num)
{
    _addrType = type;
    _addrStyle = style;
    memset(_devNum, 0, 19);
    strncpy(_devNum, num, 18);
}

string BDDevName::toString() const
{
    ostrstream os;
    os << '(' 
       << _addrType << ',' 
       << _addrStyle << ',' 
       << '\"' << _devNum << "\")" 
       << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

bool operator==(const BDDevName &dn1, const BDDevName &dn2)
{
    return ((dn1._addrType == dn2._addrType)
            && (dn1._addrStyle == dn2._addrStyle)
            && strncmp(dn1._devNum, dn2._devNum, 18) == 0);
}

bool operator!=(const BDDevName &dn1, const BDDevName &dn2)
{
    return !operator==(dn1, dn2);
}

// BDTermInfo members

BDTermInfo::BDTermInfo()
{
    _type = -1;
    _status = -1;
    _hasCarrier = false;
    memset(_id, 0, 17);
    memset(_alias, 0, 25);
    _carrierType = -1;
    _carrierId = 0;
    memset(_carrierName, 0, 25);
    memset(_carrierAlias, 0, 25);
    _carrierOwnerOrg = 0;
    memset(_carrierOwnerName, 0, 25);
    _carrierCapacity = 0;
}

string BDTermInfo::toString() const
{
    ostrstream os;
    os << '(' 
       << _type << ',' 
       << _status << ',' 
       << _hasCarrier << ',' 
       << '\"' << _id << "\","
       << '\"' << _alias << "\","
       << _carrierType << ',' 
       << _carrierId << ','
       << '\"' << _carrierName << "\"," 
       << '\"' << _carrierAlias << "\"," 
       << _carrierOwnerOrg << ','
       << '\"' << _carrierOwnerName << "\"," 
       << _carrierCapacity << ")"
       << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

// BDTermStatus members

BDTermStatus::BDTermStatus()
{
    _power = -1;
    _sat1 = 0;
    _sat2 = 0;
    _cellSignal = 99;
}

string BDTermStatus::toString() const
{
    ostrstream os;
    os << '(' << _power << ',' 
       << _sat1 << ',' 
       << _sat2 << ','
       << _cellSignal << ')' 
       << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

// BDMessage members

BDMessage::BDMessage()
{
    _codec = BDI::GB2312_SBC;
    _wcodes = NULL;
    _nchars = 0;
}

BDMessage::BDMessage(int codec, const uint16 *codes, int nchars)
{
    _wcodes = NULL;
    _nchars = 0;
    setMessage(codec, codes, nchars);
}

BDMessage::~BDMessage()
{
    if (_wcodes != NULL)
        DELETE_ARR(_wcodes);
}

string BDMessage::toString() const
{
    int i = 0;
    ostrstream os;
    os << endl << _nchars << ',';
    if (_codec == BDI::Unicode)
    switch(_codec) {
    case BDI::Unicode:
        os << "Unicode";
        break;
    case BDI::GB2312_SBC:
        os << "GB2312_SBC";
        break;
    case BDI::ANSI:
    	os << "ANSI";
    	break;
    default:
        os << "Unknown Codec";
        break;
    }
    os << ',' << endl << hex;
    for (i = 0; i < _nchars; ++i) {
        os << _wcodes[i] << ' ';
        if (i != 0 && i % 20 == 0)
            os << endl;
    }
    if (i % 20 != 0)
        os << endl;
    os << ends;

    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return result;
}

void BDMessage::setMessage(int codec, const uint16 *codes, int nchars)
{
    if (_wcodes != NULL)
        DELETE_ARR(_wcodes);

    _wcodes = NULL;
    _nchars = 0;

    if (codes == NULL || nchars <= 0)
        return;

    _wcodes = NEW uint16[nchars];
    if (_wcodes == NULL)
        return;
    memcpy(_wcodes, codes, sizeof(uint16) * nchars);
    _nchars = nchars;
    _codec = codec;
}

// BDIDecoder members

BDIDecoder::BDIDecoder(const uint8 *frame, unsigned int sz)
    : _p(frame), _op(_p), _maxop(_p + sz)
{
}

BDIDecoder::~BDIDecoder()
{
}

uint8 BDIDecoder::getOctet()
{
    if (_op == NULL || _op >= _maxop)
        return 0;
    return *_op++;
}

void BDIDecoder::getOctets(uint8 *octets, unsigned int length)
{
    for (unsigned int i = 0; i < length && _op < _maxop; ++i)
        *octets++ = *_op++;
}

uint32 BDIDecoder::getInteger(int length)
{
    uint32 res = 0;
    for (int i = 0; i < length && _op < _maxop; ++i) {
        res <<= 8;
        res |= *_op++;
    }
    return res;
}

GPosition BDIDecoder::getGPosition()
{
    GPosition gp;
    gp._ts = getInteger(4);
    gp._lon = getInteger(4) / 1000000.0;
    gp._lat = getInteger(4) / 1000000.0;
    gp._speed = getInteger(2) * 0.036;
    gp._course = getInteger(2);
    gp._turnRate = getInteger(2) * 0.01;
    gp._alt = getInteger(2);
	gp._locMode = static_cast<GPosition::LocMode>(getOctet());
	gp._locStatus = static_cast<GPosition::LocStatus>(getOctet());
	gp._coordType = static_cast<GPosition::CoordType>(getOctet());
	gp._pType = static_cast<GPosition::PointType>(getOctet());

    return gp;
}

BDDevName BDIDecoder::getBDDevName()
{
    BDDevName dn;
    dn._addrType = getOctet();
    dn._addrStyle = getOctet();
    getOctets(reinterpret_cast<uint8 *>(dn._devNum), 18);
    return dn;
}

BDTermInfo BDIDecoder::getBDTermInfo()
{
    BDTermInfo ti;
	ti._hasCarrier = false;

    ti._type = getOctet();
    ti._status = getOctet();
	int hasCarrier = getOctet();
	if ( 0x00 == hasCarrier ) {
		ti._hasCarrier = false;
	}
	else if ( 0x01 == hasCarrier ) {
		ti._hasCarrier = true;
	}
    getInteger(4);		//RESERVED
    getOctet();			//RESERVED
    getOctets(reinterpret_cast<uint8 *>(ti._id), 16);
    getOctets(reinterpret_cast<uint8 *>(ti._alias), 24);
    getInteger(2);		//RESERVED
    getOctet();			//RESERVED
    ti._carrierType = getOctet();
    ti._carrierId = getInteger(4);
    getOctets(reinterpret_cast<uint8 *>(ti._carrierName), 24);
    getOctets(reinterpret_cast<uint8 *>(ti._carrierAlias), 24);
    getInteger(4);		//RESERVED
    ti._carrierOwnerOrg = getInteger(4);
    getOctets(reinterpret_cast<uint8 *>(ti._carrierOwnerName), 24);
    ti._carrierCapacity = getInteger(2);
    getInteger(4);		//RESERVED
    getInteger(4);		//RESERVED
    getInteger(4);		//RESERVED
    getInteger(4);		//RESERVED
    getInteger(4);		//RESERVED
    getInteger(2);		//RESERVED
    return ti;
}

BDTermStatus BDIDecoder::getBDTermStatus()
{
    BDTermStatus ts;
    ts._power = getOctet();
    ts._sat1 = getOctet();
    ts._sat2 = getOctet();
    //ts._cellSignal = getOctet();
    _op += 5;
    return ts;
}

BDMessageRef BDIDecoder::getBDMessage(bool mayOddBytes)
{

    BDMessageRef mr = NEW BDMessage;
	
    if (mr.isNull())
        return NULL;

    int nbytes = getOctet();
    if (nbytes == 0)
        return mr;

	uint16 *codes = 0;

	if ( !mayOddBytes ) {
		codes = NEW uint16[nbytes / 2];
		if (codes != NULL) {
			int nchars = nbytes / 2;
			const uint8 *p = _op;
			for (int i = 0; i < nchars; ++i) {
				codes[i] = (static_cast<uint16>(*p) * 256) + *(p + 1);
				p += 2;
			}
			mr->setMessage(BDI::GB2312_SBC, codes, nchars);
			DELETE_ARR(codes);
		}
	}
	else {
		codes = NEW uint16[nbytes];
		if (codes != NULL) {
			int nchars = nbytes;
			const uint8 *p = _op;
			for (int i = 0; i < nchars; ++i) {
				codes[i] = static_cast<uint16>(*p);
				p++;
			}
			mr->setMessage(BDI::ANSI, codes, nchars);
			DELETE_ARR(codes);
		}
	}

    _op += nbytes;
    return mr;
}

// BDIEncoder members

bool BDIEncoder::assureFreeSize(int len)
{
    const unsigned int dataSize = _op - _p;
    if (_bufsz - dataSize >= static_cast<unsigned int>(len))
        return true;

    unsigned int newsz = _bufsz + 1024;
    uint8 *newbuf = NEW uint8[newsz];
    if (newbuf == NULL)
        return false;

    if (_p != NULL) {
        memcpy(newbuf, _p, _bufsz);
        DELETE_ARR(_p);
    }
    _p = newbuf;
    _bufsz = newsz;
    _op = _p + dataSize;
    return true;
}

BDIEncoder::BDIEncoder()
{
    _p = NULL;
    _bufsz = 0;
    _op = NULL;
}

BDIEncoder::~BDIEncoder()
{
    if (_p != NULL)
        DELETE_ARR(_p);
}

void BDIEncoder::setOctet(uint8 octet)
{
    if (!assureFreeSize(1))
        return;
    *_op++ = octet;
}

void BDIEncoder::setOctets(const uint8 *octets, unsigned int length)
{
	RB_ASSERT(NULL != octets);

    if (!assureFreeSize(length))
        return;
    for (unsigned int i = 0; i < length; ++i)
        *_op++ = octets[i];
}

void BDIEncoder::setInteger(uint32 v, int length)
{
    if (!assureFreeSize(length))
        return;

    uint32 mask = 0xff;
    for (int i = 0; i < length - 1; ++i)
        mask <<= 8;

    for (int i = 0; i < length; ++i) {
        *_op++ = ((v & mask) >> ((length - i - 1) * 8));
        mask >>= 8;
    }
}

void BDIEncoder::setGPosition(const GPosition &gp)
{
    setInteger(gp._ts, 4);
    setInteger(static_cast<int32>(gp._lon * 1000000), 4);
    setInteger(static_cast<int32>(gp._lat * 1000000), 4);
    setInteger(static_cast<uint16>(gp._speed / 0.036), 2);
    setInteger(static_cast<uint16>(gp._course), 2);
    setInteger(static_cast<int16>(gp._turnRate / 0.01), 2);
    setInteger(static_cast<int16>(gp._alt), 2);
    setOctet(gp._locMode);
    setOctet(gp._locStatus);
    setOctet(gp._coordType);
    setOctet(gp._pType);
}

void BDIEncoder::setBDDevName(const BDDevName &dn)
{
    setOctet(dn._addrType);
    setOctet(dn._addrStyle);
    setOctets(reinterpret_cast<const uint8 *>(dn._devNum), 18);
}

void BDIEncoder::setBDTermInfo(const BDTermInfo &ti)
{
    setOctet(ti._type);
    setOctet(ti._status);
    setOctet(ti._hasCarrier);
    setInteger(0, 4);		//RESERVED
    setOctet(0);			//RESERVED
    setOctets(reinterpret_cast<const uint8 *>(ti._id), 16);
    setOctets(reinterpret_cast<const uint8 *>(ti._alias), 24);
    setInteger(0, 2);		//RESERVED
    setOctet(0);			//RESERVED
    setOctet(ti._carrierType);
    setInteger(ti._carrierId, 4);
    setOctets(reinterpret_cast<const uint8 *>(ti._carrierName), 24);
    setOctets(reinterpret_cast<const uint8 *>(ti._carrierAlias), 24);
    setInteger(0, 4);		//RESERVED
    setInteger(ti._carrierOwnerOrg, 4);
    setOctets(reinterpret_cast<const uint8 *>(ti._carrierOwnerName), 24);
    setInteger(ti._carrierCapacity, 2);

	uint8 buf[22] = {0};
	setOctets(buf, 22);	//RESERVED
}

void BDIEncoder::setBDTermStatus(const BDTermStatus &ts)
{
    setOctet(ts._power);
    setOctet(ts._sat1);
    setOctet(ts._sat2);
    setOctet(ts._cellSignal);
    for (int i = 0; i < 12; ++i)
        setOctet(0);
}

void BDIEncoder::setBDMessage(BDMessageRef mr)
{
    RB_ASSERT(mr->_codec == BDI::GB2312_SBC);

    if (mr.isNull()) {
        setOctet(0);
        for( int i = 0; i < 256; i++) {
			setOctet(0);
        }
        return;
    }

    setOctet(mr->_nchars * 2);
    for (int i = 0; i < 128/*mr->_nchars*/; ++i) {
    	if ( i < mr->_nchars ) {
			setOctet(mr->_wcodes[i] / 256);
			setOctet(mr->_wcodes[i] % 256);
        }
        else {
			setOctet(0);
			setOctet(0);
        }
    }
}

unsigned int BDIEncoder::getFrameData(uint8 **pframe)
{
    const int dataSize = _op - _p;
    unsigned int sz = 0;
    *pframe = NULL;

    if (dataSize > 0) {
        *pframe = NEW uint8[dataSize];
        if (*pframe != NULL) {
            memcpy(*pframe, _p, dataSize);
            sz = dataSize;
        }
    }

    return sz;
}

unsigned int BDIEncoder::getLength()
{
    return _op - _p;
}
