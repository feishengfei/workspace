#include <stdio.h>
#include <assert.h>

#include <strstream>

#include "bdi_message.h"

using namespace std;
using namespace BDI;

// BDI_MessageEvent members

inline void BDI_MessageEvent::init()
{
	_ts = time(NULL);
    _messageType = 255;
    _needReceipt = false;
}

BDI_MessageEvent::BDI_MessageEvent(int dataType)
    : BDI_RTData(dataType)
{
    init();
}

BDI_MessageEvent::BDI_MessageEvent(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    init();
}

BDI_MessageEvent::~BDI_MessageEvent()
{
}

Ref<BDI_Event> BDI_MessageEvent::decode(const uint8 *frame, unsigned int sz)
{
    assert(frame != NULL);
    assert(sz > BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN);

    Ref<BDI_Event> res;
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN, sz - BDI::FRAME_HEADER_LEN);
    const uint32 bid = d.getInteger(4);

    switch (bid) {
    case BDI::ReceiptMessageEvent:
        res = NEW BDI_ReceiptMessageEvent(frame, sz);
        break;
    case BDI::DeliverMessageEvent:
        res = NEW BDI_DeliverMessageEvent(frame, sz);
        break;
    default:
        assert(0);
        break;
    }

    return res;
}

int BDI_MessageEvent::textCodec() const
{
    int res = BDI::GB2312_SBC;
    if (!_bdmsg.isNull())
        res = _bdmsg->_codec;
    return res;
}

const uint16 *BDI_MessageEvent::textCodes() const
{
    if (_bdmsg.isNull())
        return NULL;
    else
        return _bdmsg->_wcodes;
}

int BDI_MessageEvent::textLength() const
{
    if (_bdmsg.isNull())
        return 0;
    else
        return _bdmsg->_nchars;
}

void BDI_MessageEvent::setText(int codec, const uint16 *codes, int nchars)
{
    if (_bdmsg.isNull())
        _bdmsg = NEW BDMessage(codec, codes, nchars);
    else
        _bdmsg->setMessage(codec, codes, nchars);
}

time_t BDI_MessageEvent::ts() const
{
	return static_cast<time_t>(_ts);
}

// BDI_ReceiptMessageEvent members

Ref<BDI_Event> BDI_ReceiptMessageEvent::ack() const
{
    return NULL;
}

unsigned int BDI_ReceiptMessageEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setInteger(_priority, 1);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    // encode data body
    e.setInteger(_pid, 4);
    e.setBDDevName(_oa);
    e.setBDDevName(_da);
    e.setInteger(_receiptType, 4);
    e.setInteger(_messageType, 4);
    e.setBDMessage(_bdmsg);
    return e.getFrameData(pbody);
}

BDI_ReceiptMessageEvent::BDI_ReceiptMessageEvent()
    : BDI_MessageEvent(ReceiptMessageEvent)
{
    _needReceipt = false;
}

BDI_ReceiptMessageEvent::BDI_ReceiptMessageEvent(const uint8 *frame, unsigned int sz)
    : BDI_MessageEvent(frame, sz)
{
	BDIDecoder d(frame + FRAME_HEADER_LEN + DATA_HEADER_LEN, 
                 sz - FRAME_HEADER_LEN - DATA_HEADER_LEN);
    _pid = d.getInteger(4);
    _oa = d.getBDDevName();
    _da = d.getBDDevName();
    _receiptType = d.getInteger(4);
    _messageType = d.getInteger(4);
    _bdmsg = d.getBDMessage();
    _needReceipt = false;
}

string BDI_ReceiptMessageEvent::toString() const
{
    ostrstream os;
    os << _pid << ','
       << _oa.toString() << ','
       << _da.toString() << ','
       << _receiptType << ','
       << _messageType << ",\n\t"
       << _bdmsg->toString() 
       << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return fh_string() + dh_string() + res;
}

// BDI_DeliverMessageEvent members

BDI_DeliverMessageEvent::BDI_DeliverMessageEvent()
    : BDI_MessageEvent(DeliverMessageEvent)
{
}

BDI_DeliverMessageEvent::BDI_DeliverMessageEvent(const uint8 *frame, unsigned int sz)
    : BDI_MessageEvent(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _pid = d.getInteger(4);
    _oa = d.getBDDevName();
    _ts = d.getInteger(4);
    _messageType = d.getOctet();
    _needReceipt = d.getOctet();
    d.getOctet();		//RESERVED
    _bdmsg = d.getBDMessage(true);
}

string BDI_DeliverMessageEvent::toString() const
{
    ostrstream os;
    os << _oa.toString() << ','
       << timevToStr(static_cast<time_t>(_ts)) << ','
       << _messageType << ','
       << _needReceipt << ",\n\t"
       << _bdmsg->toString()
       << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return fh_string() + dh_string() + res;
}

/*
Ref<BDI_Event> BDI_DeliverMessageEvent::ack() const
{
    if (!_needReceipt)
        return BDI_RTData::ack();

    BDI_ReceiptMessageEvent *ae = NEW BDI_ReceiptMessageEvent;
    if (ae == NULL)
        return NULL;
    ae->setPid(pid());
    ae->setOrigAddr(_da);
    ae->setDestAddr(_oa);

    ae->setReceiptType(0x21);

    return ae;
}
*/

// BDI_SubmitMessageEvent members

Ref<BDI_Event> BDI_SubmitMessageEvent::ack() const
{
    return NULL;
}

unsigned int BDI_SubmitMessageEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setInteger(_priority, 1);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    // encode data body
    e.setInteger(_pid, 4);
	e.setInteger(time(NULL), 4);
    e.setOctet(_messageType);
    e.setOctet(_needReceipt);
    e.setInteger(0, 4);			//RESERVED
    e.setOctet(0);				//RESERVED

    e.setBDMessage(_bdmsg);
	e.setInteger(0, 2);			//RESERVED
	e.setOctet(0);				//RESERVED
    e.setOctet(_daCount);
    e.setBDDevName(_da);
    return e.getFrameData(pbody);
}

BDI_SubmitMessageEvent::BDI_SubmitMessageEvent()
    : BDI_MessageEvent(SubmitMessageEvent)
{
	_daCount = 1;
}

string BDI_SubmitMessageEvent::toString() const
{
    ostrstream os;
    os << '[' << _oa.toString() << "],"
       << _messageType << ','
       << _needReceipt << ','
       << _daCount << ','
       << _bdmsg->toString() << ','
       << _da.toString()
       << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return fh_string() + dh_string() + res;
}
