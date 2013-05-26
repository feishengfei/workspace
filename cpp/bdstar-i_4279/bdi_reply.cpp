#include <stdio.h>
#include <string.h>
#include <assert.h>

#include <strstream>

#include "debug_alloc.h"
#include "bdi_reply.h"

using namespace std;
using namespace BDI;

// BDI_Reply members

BDI_Reply::BDI_Reply(int errCode, BDI_EventRef sube)
    : BDI_Event(BDI_Event::Reply), _errCode(errCode), _sube(sube)
{
}

BDI_Reply::BDI_Reply(const uint8 *frame, unsigned int sz)
    : BDI_Event(frame, sz), _errCode(0)
{
    if (_data != NULL) {
        BDIDecoder dec(_data, _dataSize);
        _bid = dec.getInteger(4);
        _priority = dec.getOctet();
    }
}

BDI_Reply::~BDI_Reply()
{
}

string BDI_Reply::toString() const
{
    if (_errCode == 0)
        return BDI_Event::toString();

    ostrstream os;
    os << _errCode << ','
       << "0X" << hex << _sube->type() << ','
       << "0X" << _sube->bid() << '!'
       << ends;
    char *ss = os.str();
    string result(ss);
    delete[] ss;
    return fh_string() + result;
}

Ref<BDI_Event> BDI_Reply::decode(const uint8 *frame, unsigned int sz)
{
    assert(frame != NULL);
    assert(sz > BDI::FRAME_HEADER_LEN + BDI::DATA_HEADER_LEN);

    Ref<BDI_Event> res;
    BDIDecoder dec(frame + BDI::FRAME_HEADER_LEN, sz - BDI::FRAME_HEADER_LEN);
    const uint32 replyType = dec.getInteger(4);

    switch (replyType) {
    case BDI::TrackReplyEvent:
        res = NEW BDI_TrackLogReply(frame, sz);
        break;
    default:
        fprintf(stderr, "Unknown reply event: 0x%x\n", replyType);
        assert(0);
        break;
    }

    return res;
}

// BDI_TrackLogReply members

BDI_TrackLogReply::BDI_TrackLogReply(const uint8 *frame, unsigned int sz)
    : BDI_Reply(frame, sz)
{
    _track = NULL;
    _posCount = 0;

    // TODO: decode QDC_STPositions frame here
	// add by hzzhou
	BDIDecoder dec(_data, _dataSize);
	_pid = dec.getInteger(4);
	uint8 buf[20];
	dec.getOctets(buf, 20);
	dec.getOctets((uint8 *)_devName, 20);
	_devName[20] = 0;
	_posCount = dec.getInteger(4);
	_track = NEW GPosition[_posCount];
	for(int i = 0; i < _posCount; i++){
		_track[i] = dec.getGPosition();
	}
	// add end
}

BDI_TrackLogReply::~BDI_TrackLogReply()
{
    if (_track != NULL)
        DELETE_ARR(_track);
}

string BDI_TrackLogReply::toString() const
{
    // TODO: textualize here
	// add by hzzhou
	ostrstream os;
	os << _pid << ','
	   << '\"' << _devName << '\"'
	   << _posCount;
	for (int i = 0; i < _posCount; i++)
        os << _track[i].toString();
	os << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
	// add end
}
