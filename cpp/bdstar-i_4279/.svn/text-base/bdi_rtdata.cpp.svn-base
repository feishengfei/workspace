#include <stdio.h>
#include <string.h>
#include <assert.h>

#include <strstream>

#include "debug_alloc.h"
#include "rbglobal.h"
#include "bdi_message.h"
#include "bdi_rtdata.h"

using namespace std;
using namespace BDI;

// BDI_RTData members

BDI_RTData::BDI_RTData(int dataType)
    : BDI_Event(BDI_Event::RTData)
{
    _bid = dataType;
}

BDI_RTData::BDI_RTData(const uint8 *frame, unsigned int sz)
    : BDI_Event(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN, sz - BDI::FRAME_HEADER_LEN);
    _bid = d.getInteger(4);
    _priority = d.getOctet();
}

BDI_RTData::~BDI_RTData()
{
}

Ref<BDI_Event> BDI_RTData::decode(const uint8 *frame, unsigned int sz)
{
    assert(frame != NULL);
    assert(sz > BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN);

    Ref<BDI_Event> res;
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN, sz - BDI::FRAME_HEADER_LEN);
    const uint32 bid = d.getInteger(4);

    switch (bid) {
    case BDI::TermListEvent:
        res = NEW BDI_TermListPush(frame, sz);
        break;
    case BDI::InviteTermJoinGroupReply:
        res = NEW BDI_TermJoinPush(frame, sz);
        break;
    case BDI::TermPosReply:
        res = NEW BDI_TermPosReplyPush(frame, sz);
        break;
    case BDI::TermPosReport:
        res = NEW BDI_TermPosReportPush(frame, sz);
        break;
    case BDI::TermSosEvent:
        res = NEW BDI_TermSosPush(frame, sz);
        break;

    case BDI::ReceiptMessageEvent:
    case BDI::DeliverMessageEvent:
    case BDI::SubmitMessageEvent:
        res = BDI_MessageEvent::decode(frame, sz);
        break;
    default:
        res = NEW BDI_RTData(frame, sz);
        break;
    }

    return res;
}

Ref<BDI_Event> BDI_RTData::ack() const
{
    Ref<BDI_Event> res = NEW BDI_Event(BDI_Event::RTDataResp);
    if (!res.isNull()) {
        res->setSn(sn());
        res->setBid(bid() | 0x80000000);
        res->setPid(pid());
        res->setPriority(priority());
    }
    return res;
}

// BDI_TermListPush members

BDI_TermListPush::BDI_TermListPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    _terms = NULL;
    _termCount = 0;
    _errCode = -1;

    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
	_pid = d.getInteger(4);
	d.getInteger(4);	//RESERVED
	d.getInteger(4);	//RESERVED
	d.getInteger(2);	//RESERVED
	_errCode = d.getOctet();
	/*
    if (_errCode != 0)
        return;
    */
	_termCount = d.getOctet();
	_terms = NEW BDTermInfo[_termCount];
    if (_terms == NULL)
        _termCount = 0;
	for(int i = 0; i < _termCount; i++)
		_terms[i] = d.getBDTermInfo();
}

BDI_TermListPush::~BDI_TermListPush()
{
    if (_terms != NULL)
        DELETE_ARR(_terms);
}

string BDI_TermListPush::toString() const
{
	ostrstream os;
	os << _pid << ',' 
       << _errCode << ','
       << _termCount << '\n';
	for (int i = 0; i < _termCount; i++){
		os << _terms[i].toString() << ",\n";
	}
	os << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

// BDI_TermJoinPush members

BDI_TermJoinPush::BDI_TermJoinPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    _isAgree = false;

    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
	_pid = d.getInteger(4);
    _oa = d.getBDDevName();
    _groupNum = d.getInteger(4);
    uint8 agree = d.getOctet();
    if (0x01 == agree) {
		_isAgree = true;
    }
    else if(0x02 == agree) {
		_isAgree = false;
    }
	d.getOctet();		//RESERVED
	d.getOctet();		//RESERVED
	d.getOctet();		//RESERVED
}

BDI_TermJoinPush::~BDI_TermJoinPush()
{
}

string BDI_TermJoinPush::toString() const
{
	ostrstream os;
	os << _pid << ','
	   << '\"' << _oa.toString() << "\","
	   << _groupNum << ','
	   << ( _isAgree ? "agree":"disagree" ) << ','
       << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

//BDI_TermPosReplyPush members

BDI_TermPosReplyPush::BDI_TermPosReplyPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
	_pid = d.getInteger(4);
	_oa = d.getBDDevName();
	d.getInteger(4);		//RESERVED
	d.getInteger(4);		//RESERVED
	_status = d.getBDTermStatus();
	_pos = d.getGPosition();
}

BDI_TermPosReplyPush::~BDI_TermPosReplyPush()
{
}

std::string BDI_TermPosReplyPush::toString() const
{
    ostrstream os;
    os << _pid << ','
       << _oa.toString() << ','
       << _status.toString() << ','
       << _pos.toString() << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return fh_string() + dh_string() + res;
}

//BDI_TermPosReportPush members

BDI_TermPosReportPush::BDI_TermPosReportPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
	_termNum = 0;	
	_posp = NULL;

    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
	_pid = d.getInteger(4);
	_oa = d.getBDDevName();
	d.getInteger(4);		//RESERVED
	d.getInteger(2);		//RESERVED
	d.getOctet();		//RESERVED
	_termNum = d.getOctet();

	_status = d.getBDTermStatus();
	_posp = NEW GPosition[_termNum];
    if (_posp == NULL)
        _termNum = 0;
	for(int i = 0; i < _termNum; i++)
		_posp[i] = d.getGPosition();
}

BDI_TermPosReportPush::~BDI_TermPosReportPush()
{
    if (_posp != NULL){
        DELETE_ARR(_posp);
    }
}

std::string BDI_TermPosReportPush::toString() const
{
    ostrstream os;
    os << _pid << ','
       << _oa.toString() << ','
       << _status.toString() << ','
       << _termNum << '\n';
	for(int i=0; i<_termNum; i++) {
		os << '{' << (_posp+i)->toString() << '}' << '\n';
	}
	os << ends;

    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return fh_string() + dh_string() + res;
}

// BDI_TermSosPush members
BDI_TermSosPush::BDI_TermSosPush()
	: BDI_RTData(BDI::TermSosEvent)
{
	memset( _site, 0, 21 );
}

BDI_TermSosPush::BDI_TermSosPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    memset(_site, 0, 21);

    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _pid = d.getInteger(4);
    _oa = d.getBDDevName();
    _ts = d.getInteger(4);
	d.getInteger(2);		//RESERVED
	d.getOctet();			//RESERVED
	_sosType = d.getOctet();
	_termStat = d.getBDTermStatus();
    _gpos = d.getGPosition();
    d.getOctets(reinterpret_cast<uint8 *>(_site), 20);
	d.getInteger(2);		//RESERVED
	d.getOctet();			//RESERVED
	_msg = d.getBDMessage();
}

BDI_TermSosPush::~BDI_TermSosPush()
{

}

std::string BDI_TermSosPush::toString() const
{
	ostrstream os;
	os << _oa.toString() << ','
	   << timevToStr(_ts) << ','
	   << _sosType << ','
	   << _termStat.toString() << ','
	   << '\"' << _site << "\","
       << _gpos.toString() << ",\n"
	   << _msg->toString()
	   << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

int BDI_TermSosPush::textCodec() const
{
    int res = BDI::GB2312_SBC;
    if (!_msg.isNull())
        res = _msg->_codec;
    return res;
}

const uint16 *BDI_TermSosPush::textCodes() const
{
    if (_msg.isNull())
        return NULL;
    else
        return _msg->_wcodes;
}

int BDI_TermSosPush::textLength() const
{
    if (_msg.isNull())
        return 0;
    else
        return _msg->_nchars;
}

time_t BDI_TermSosPush::ts() const
{
	return static_cast<time_t>(_ts);
}

/*
// BDI_ReceiptEvent members

Ref<BDI_Event> BDI_ReceiptEvent::ack() const
{
    return NULL;
}

unsigned int BDI_ReceiptEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setOctet(_priority);
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

BDI_ReceiptEvent::BDI_ReceiptEvent()
    : BDI_RTData(BDI::ReceiptEvent)
{
    _receiptType = 0xff;
}

BDI_ReceiptEvent::BDI_ReceiptEvent(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    const uint8 *data = frame + FRAME_HEADER_LEN + DATA_HEADER_LEN;
    unsigned int dataLen = sz - FRAME_HEADER_LEN - DATA_HEADER_LEN;

	BDIDecoder d(data, dataLen);
    _pid = d.getInteger(4);
    _oa = d.getBDDevName();
    _da = d.getBDDevName();
    _receiptType = d.getInteger(4);
    _messageType = d.getInteger(4);
    _bdmsg = d.getBDMessage();
}

BDI_ReceiptEvent::~BDI_ReceiptEvent()
{
}

string BDI_ReceiptEvent::toString() const
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

const uint16 *BDI_ReceiptEvent::textGbkCode() const
{
    if (_bdmsg.isNull())
        return NULL;
    else
        return _bdmsg->_codes;
}

int BDI_ReceiptEvent::textLength() const
{
    if (_bdmsg.isNull())
        return 0;
    else
        return _bdmsg->_nchars;
}

void BDI_ReceiptEvent::setMessage(const uint16 *gbkCodes, int nchars)
{
    if (_bdmsg.isNull())
        _bdmsg = NEW BDMessage(gbkCodes, nchars);
    else
        _bdmsg->setMessage(gbkCodes, nchars);
}
*/

// BDI_PosReplyPush members

Ref<BDI_Event> BDI_PosReplyPush::ack() const
{
    return NULL;
}

BDI_PosReplyPush::BDI_PosReplyPush(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _oa = d.getBDDevName();
    _da = d.getBDDevName();
	d.getInteger(4);
	_termStat = d.getBDTermStatus();
	_gpos = d.getGPosition();
}

BDI_PosReplyPush::~BDI_PosReplyPush()
{
}

string BDI_PosReplyPush::toString() const
{
	ostrstream os;
	os << _pid << ','
       << _oa.toString() << ','
       << _da.toString() << ','
       << _termStat.toString() << ','
	   << _gpos.toString() 
       << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

/*
// BDI_PosReportEvent members

unsigned int BDI_PosReportEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setOctet(_priority);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    // encode data body
    e.setBDDevName(_oa);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    e.setInteger(_posCount, 1);
    e.setBDTermStatus(_termStat);
    for (int i = 0; i < _posCount; ++i)
        e.setGPosition(_gpos[i]);
    return e.getFrameData(pbody);
}

BDI_PosReportEvent::BDI_PosReportEvent()
    : BDI_RTData(PosReportEvent)
{
    _posCount = 0;
}

BDI_PosReportEvent::BDI_PosReportEvent(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _oa = d.getBDDevName();
	d.getOctet();
	d.getOctet();
	d.getOctet();
	_posCount = d.getOctet();
	_termStat = d.getBDTermStatus();
	for (int i = 0; i < _posCount && i < MAX_POS_ITEMS; ++i)
		_gpos[i] = d.getGPosition();
}

BDI_PosReportEvent::~BDI_PosReportEvent()
{
}

string BDI_PosReportEvent::toString() const
{
	ostrstream os;
	os << _oa.toString() << ','
	   << _termStat.toString() << ',' << endl
	   << _posCount << ',';
	for (int i = 0; i < _posCount; ++i)
        os << endl << _gpos[i].toString();
	os << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

void BDI_PosReportEvent::setDevName(int type, int style, const char *num)
{
    _oa.setDevName(type, style, num);
}

void BDI_PosReportEvent::setTermStatus(int powlvl, int sat1, int sat2)
{
    _termStat._power = powlvl;
    _termStat._sat1 = sat1;
    _termStat._sat2 = sat2;
}

void BDI_PosReportEvent::addPosition(const GPosition &pos)
{
    if (_posCount < MAX_POS_ITEMS)
        _gpos[_posCount++] = pos;
    else
        RB_WARNING("Too many position item to add.");
}

bool BDI_PosReportEvent::unite(const BDI_PosReportEvent &pr)
{
    if (_oa != pr._oa)
        return false;
    if (_posCount + pr._posCount > MAX_POS_ITEMS)
        return false;

    _termStat = pr._termStat;
    for (int i = 0; i < pr._posCount; ++i)
        _gpos[_posCount++] = pr._gpos[i];
    return true;
}
*/

// BDI_TermSosEvent members

unsigned int BDI_TermSosEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setOctet(_priority);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    // encode data body
    e.setBDDevName(_oa);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(_sosType);
    e.setBDTermStatus(_termStat);
    e.setOctets(reinterpret_cast<const uint8 *>(_site), 24);
    e.setGPosition(_gpos);
    return e.getFrameData(pbody);
}

BDI_TermSosEvent::BDI_TermSosEvent()
    : BDI_RTData(TermSosEvent)
{
    memset(_site, 0, 25);
    _canceled = false;
}

BDI_TermSosEvent::BDI_TermSosEvent(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    memset(_site, 0, 25);
    _canceled = (_bid == TermSosCanceledEvent);

    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _oa = d.getBDDevName();
	d.getOctet();
	d.getOctet();
	d.getOctet();
	_sosType = static_cast<BDI_TermSosEvent::SOSType>(d.getOctet());
	_termStat = d.getBDTermStatus();
    d.getOctets(reinterpret_cast<uint8 *>(_site), 24);
    _gpos = d.getGPosition();
}

BDI_TermSosEvent::~BDI_TermSosEvent()
{
}

string BDI_TermSosEvent::toString() const
{
	ostrstream os;
	os << _oa.toString() << ','
	   << _sosType << ','
	   << _termStat.toString() << ','
	   << '\"' << _site << "\","
       << _gpos.toString() 
	   << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

void BDI_TermSosEvent::setDevName(int type, int style, const char *num)
{
    _oa.setDevName(type, style, num);
}

void BDI_TermSosEvent::setSiteStr(const char *s)
{
    memset(_site, 0, 25);
    strncpy(_site, s, 24);
}

void BDI_TermSosEvent::setTermStatus(int powlvl, int sat1, int sat2)
{
    _termStat._power = powlvl;
    _termStat._sat1 = sat1;
    _termStat._sat2 = sat2;
}

void BDI_TermSosEvent::setCanceled(bool canceled)
{
    _canceled = canceled;
    _bid = canceled ? TermSosCanceledEvent : TermSosEvent;
}

// BDI_ImportOutportEvent members

unsigned int BDI_ImportOutportEvent::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    // encode user data header
    e.setInteger(_bid, 4);
    e.setOctet(_priority);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    // encode data body
    e.setBDDevName(_oa);
    e.setOctet(_isImport ? 1 : 0);
    e.setOctet(0);
    e.setOctet(0);
    e.setOctet(0);
    e.setGPosition(_gpos);
    e.setBDMessage(_bdmsg);
    return e.getFrameData(pbody);
}

BDI_ImportOutportEvent::BDI_ImportOutportEvent()
    : BDI_RTData(ImportOutportEvent)
{
}

BDI_ImportOutportEvent::BDI_ImportOutportEvent(const uint8 *frame, unsigned int sz)
    : BDI_RTData(frame, sz)
{
    BDIDecoder d(frame + BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN, 
                 sz - BDI::FRAME_HEADER_LEN - BDI::DATA_HEADER_LEN);
    _oa = d.getBDDevName();
    _isImport = (d.getOctet() == 1);
    d.getOctet();
    d.getOctet();
    d.getOctet();
    _gpos = d.getGPosition();
    _bdmsg = d.getBDMessage();
}

BDI_ImportOutportEvent::~BDI_ImportOutportEvent()
{
}

string BDI_ImportOutportEvent::toString() const
{
	ostrstream os;
	os << _oa.toString() << ','
       << (_isImport ? 1 : 0) << ','
       << _gpos.toString() << ','
       << _bdmsg->toString()
       << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}
