#include <stdio.h>
#include <string.h>

#include <strstream>

#include "bdi_mobile_event.h"

using namespace std;
using namespace BDI;

// BDI_MCallEvent members

BDI_MCallEvent::BDI_MCallEvent(int bid)
    : BDI_Event(UserMobileEvent)
{
    _bid = bid;
    memset(_number, 0, 32);
}

string BDI_MCallEvent::toString() const
{
    ostrstream os;
    os << '[' << "MobileEvent" << ',';
    switch (_bid) {
    case Mobile_CallingIn:
        os << "CallingIn";
        break;
    case Mobile_CallingUp:
        os << "CallingUp";
        break;
    case Mobile_Hungup:
        os << "Hungup";
        break;
	case Mobile_DialOk:
		os << "DialOk";
		break;
	case Mobile_DialErr:
		os << "DialErr";
		break;
	case Mobile_MsgSendOk:
		os << "MsgSendOk";
		break;
	case Mobile_MsgSendErr:
		os << "MsgSendErr";
		break;
	case PPP_Up:
		os << "pppUp";
		break;
	case PPP_Down:
		os << "pppDown";
		break;
    default:
        os << _bid;
        break;
    }
    os << ']';
    if (_bid == Mobile_CallingUp)
        os << _callingLineId << ',';
    if (strlen(_number) != 0)
       os << '\"' << _number << '\"';
    os << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return res;
}

void BDI_MCallEvent::setNumber(const char *number)
{
    memset(_number, 0, 32);
    strncpy(_number, number, 31);
}

/*
// BDI_MSmsEvent members

BDI_MSmsEvent::BDI_MSmsEvent(int bid)
    : BDI_Event(UserMobileEvent)
{
    _bid = bid;
    _unicodes = NULL;
    _nchars = 0;
    memset(_number, 32, 0);
}

BDI_MSmsEvent::~BDI_MSmsEvent()
{
    if (_unicodes != NULL)
        DELETE_ARR(_unicodes);
}

string BDI_MSmsEvent::toString() const
{
    ostrstream os;
    os << '[' << "MobileEvent" << ',';
    switch (_bid) {
    case Mobile_NewMessageInd:
        os << "NewMessageIndication";
        break;
    default:
        os << _bid;
    }
    os << ']' << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return res;
}

void BDI_MSmsEvent::setNumber(const char *number)
{
    memset(_number, 0, 32);
    strncpy(_number, number, 31);
}

void BDI_MSmsEvent::setText(const uint16 *unicodes, int nchars)
{
    if (_unicodes != NULL)
        DELETE_ARR(_unicodes);

    _unicodes = NULL;
    _nchars = 0;

    if (unicodes == NULL || nchars <= 0)
        return;

    _unicodes = NEW uint16[nchars];
    if (_unicodes == NULL)
        return;
    memcpy(_unicodes, unicodes, sizeof(uint16) * nchars);
    _nchars = nchars;
}
*/

// BDI_MHandsetEvent members

BDI_MHandsetEvent::BDI_MHandsetEvent(bool isOn)
    : BDI_Event(UserMobileEvent)
{
    _bid = Mobile_HandsetTrigger;
    _isOn = isOn;
}

string BDI_MHandsetEvent::toString() const
{
    ostrstream os;
    os << '[' << "MobileEvent" << ','
       << (_isOn ? "HandsetOn" : "HandsetOff") << ']'
       << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return res;
}

// BDI_MSignalEvent members

BDI_MSignalEvent::BDI_MSignalEvent(int v, int l)
    : BDI_Event(UserMobileEvent)
{
    _bid = Mobile_Signal;
    _sigValue = v;
    _sigLevel = l;
}

string BDI_MSignalEvent::toString() const
{
    ostrstream os;
    os << '[' << "MobileEvent" << ',' << "Signal" << ']'
       << _sigValue << ','
       << _sigLevel
       << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return res;
}
