#ifndef BDI_MOBILE_EVENT_H
#define BDI_MOBILE_EVENT_H

#include <string>

#include "bdi_event.h"

class BDI_MCallEvent : public BDI_Event
{
private:
    int _callingLineId;
    char _number[32];

public:
    // Constructs a mobile call event.
    // The bid must be Mobile_CallingIn, Mobile_CallingUp or Mobile_Hungup.
    BDI_MCallEvent(int bid);

    std::string toString() const;

    void setCallingLineId(int);
    void setNumber(const char *);

    int callingLineId() const;
    const char *number() const;
};

// BDI_MCallEvent inline functions

inline void BDI_MCallEvent::setCallingLineId(int id)
{ _callingLineId = id; }

inline int BDI_MCallEvent::callingLineId() const
{ return _callingLineId; }

inline const char *BDI_MCallEvent::number() const
{ return _number; }

// ~

/*
class BDI_MSmsEvent : public BDI_Event
{
private:
    char _number[32];
    uint16 *_unicodes;
    int _nchars;

public:
    BDI_MSmsEvent(int bid);
    ~BDI_MSmsEvent();

    std::string toString() const;

    void setNumber(const char *number);
    void setText(const uint16 *unicodes, int nchars);

    const char *number() const;
    const uint16 *unicodes() const;
    int textLength() const;
};

// BDI_MSmsEvent inline functions

inline const char *BDI_MSmsEvent::number() const
{ return _number; }

inline const uint16 *BDI_MSmsEvent::unicodes() const
{ return _unicodes; }

inline int BDI_MSmsEvent::textLength() const
{ return _nchars; }

// ~
*/

class BDI_MHandsetEvent : public BDI_Event
{
private:
    bool _isOn;

public:
    BDI_MHandsetEvent(bool isOn = false);

    std::string toString() const;

    void setHandsetOn(bool isOn);
    bool isOn() const;
};

// BDI_MHandsetEvent inline functions

inline void BDI_MHandsetEvent::setHandsetOn(bool isOn)
{ _isOn = isOn; }

inline bool BDI_MHandsetEvent::isOn() const
{ return _isOn; }

// ~

class BDI_MSignalEvent : public BDI_Event
{
private:
    int _sigValue;
    int _sigLevel;

public:
    BDI_MSignalEvent(int v, int l);

    std::string toString() const;

    void setSignalValue(int);
    void setSignalLevle(int);

    int signalValue() const;
    int signalLevel() const;
};

// BDI_MSignalEvent inline functions

inline void BDI_MSignalEvent::setSignalValue(int v)
{ _sigValue = v; }

inline void BDI_MSignalEvent::setSignalLevle(int l)
{ _sigLevel = l; }

inline int BDI_MSignalEvent::signalValue() const
{ return _sigValue; }

inline int BDI_MSignalEvent::signalLevel() const
{ return _sigLevel; }

#endif
