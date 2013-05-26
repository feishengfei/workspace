#ifndef BDI_EVENT_CODEC_H
#define BDI_EVENT_CODEC_H

#include <string>

#include "bdi_define.h"
#include "bdi_util.h"

class GPosition
{
public:
    enum CoordType { 
    	BJ54 = 0x01, 
    	WGS84 = 0x02
    };

    enum LocMode {
		FIX_GPS = 0x00,
		FIX_BD1 = 0x01,
		FIX_BD2 = 0x02,
		FIX_GALILEO = 0x03,
		FIX_GLONASS = 0x04,
		FIX_GPSONE = 0X0A,
		FIX_ANY = 0x10,
		FIX_OTHER = 0x21,
    };

    enum LocStatus {
		FIXED = 0x01,
		UNFIXED = 0x02,
    };

    enum PointType {
		UNPROCESS = 0x00,
		GOODP = 0x01,
		FLYP = 0x02,
    };

public:
    uint32 _ts; // timestamp of the position, 0 for not applicable.
    double _lon; // longitude in degrees
    double _lat; // latitude in degrees
    float _speed; // speed in km/h 
    int _course; // course in degrees
    float _turnRate; // turnRate in degrees/sec
    int _alt; // altitude in meters
    LocMode _locMode;
    LocStatus _locStatus;
    CoordType _coordType; // GCoordType value, BJ54 or WGS84
    PointType _pType;

public:
    GPosition();
    bool valid() const;
    std::string toString() const;
};

// GPosition inline functions

inline bool GPosition::valid() const
{ return (_coordType == GPosition::BJ54 || _coordType == GPosition::WGS84); }

// ~

class BDDevName
{
public:
    int _addrType;
    int _addrStyle;
    char _devNum[19];

public:
    BDDevName();
    void setDevName(int, int, const char *);
    std::string toString() const;

    int type() const;
    int style() const;
    const char *number() const;

    friend bool operator==(const BDDevName &, const BDDevName &);
    friend bool operator!=(const BDDevName &, const BDDevName &);
};

// BDDevName inline functions

inline int BDDevName::type() const
{ return _addrType; }

inline int BDDevName::style() const
{ return _addrStyle; }

inline const char *BDDevName::number() const
{ return _devNum; }

// ~

class BDTermInfo
{
public:
    int _type;
    int _status;
    bool _hasCarrier;
    char _id[17];
    char _alias[25];
    int _carrierType;
    uint32 _carrierId;
    char _carrierName[25];
    char _carrierAlias[25];
   	uint32 _carrierOwnerOrg; 
   	char _carrierOwnerName[25];
   	int _carrierCapacity;

public:
    BDTermInfo();
    std::string toString() const;
};

// ~

class BDTermStatus
{
public:
    int _power; // Power state, 0xff: external power source
    int _sat1;
    int _sat2;
    int _cellSignal;

public:
    BDTermStatus();
    std::string toString() const;
};

// ~

class BDMessage : public BDI::RefBase
{
public:
    int _codec;
    uint16 *_wcodes;
    int _nchars;

public:
    BDMessage();
    BDMessage(int codec, const uint16 *codes, int nchars);
    ~BDMessage();

    std::string toString() const;

    void setMessage(int codec, const uint16 *codes, int nchars);
};

typedef BDI::Ref<BDMessage> BDMessageRef;

// ~

class BDIDecoder
{
private:
    const uint8 *_p; // pointer to the frame buffer
    const uint8 *_op; // current octet pointer
    const uint8 *_maxop; // pointer to last byte after _p

public:
    BDIDecoder(const uint8 *frame, unsigned int sz);
    ~BDIDecoder();

    // Get one octet
    uint8 getOctet();
    // Get string of octets of specified length
    void getOctets(uint8 *octets, unsigned int length);
    // Get integer with length number of bytes
    uint32 getInteger(int length);
    // Get GPosition
    GPosition getGPosition();
    // get BDDevName
    BDDevName getBDDevName();
    // Get BDTermInfo
    BDTermInfo getBDTermInfo();
    // Get BDTermStatus
    BDTermStatus getBDTermStatus();
    // Get BDMessage
    BDMessageRef getBDMessage(bool mayOddBytes = false);
};

class BDIEncoder
{
private:
    uint8 *_p; // buffer to hold frame
    unsigned int _bufsz; // buffer size
    uint8 *_op; // current octet pointer

private:
    bool assureFreeSize(int len);

public:
    BDIEncoder();
    ~BDIEncoder();

    // Set one octet
    void setOctet(uint8 octet);
    // Set string of octets of specified length
    void setOctets(const uint8 *octets, unsigned int length);
    // Set integer with length number of bytes
    void setInteger(uint32 v, int length);

    void setGPosition(const GPosition &);
    void setBDDevName(const BDDevName &);
    void setBDTermInfo(const BDTermInfo &);
    void setBDTermStatus(const BDTermStatus &);
    void setBDMessage(BDMessageRef);

    // Get frame data string
    // The caller should delete the buffer
    unsigned int getFrameData(uint8 **pframe);

    // Return current length of the data
    unsigned int getLength();
};

#endif
