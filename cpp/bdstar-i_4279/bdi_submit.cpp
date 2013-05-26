#include <stdio.h>
#include <assert.h>

#include <strstream>

#include "debug_alloc.h"
#include "bdi_submit.h"
#include "bdi_util.h"

using namespace std;

// BDI_Submit members

BDI_Submit::BDI_Submit(BDI_Event::Type type, int submitType)
    : BDI_Event(type) 
{ 
    _bid = submitType; 
    _pid = BDI::getNextPacketNo(); 
	memset(_oa, 0, 21);
	memset(_da, 0, 21);
}

BDI_Submit::~BDI_Submit()
{
}

void BDI_Submit::setOrigAddress(const char * s)
{ 
	assert(NULL != s);
	memset(_oa, 0, 21);
	strncpy(_oa, s, 20);
}

void BDI_Submit::setDestAddress(const char * s)
{ 
	assert(NULL != s);
	memset(_da, 0, 21);
	strncpy(_da, s, 20);
}

// BDI_InviteSubmit members

BDI_InviteSubmit::BDI_InviteSubmit()
    : BDI_Submit(BDI_Event::RT_Submit, BDI::InviteTermJoinGroup)
{
    _isJoin = false;
    _groupType = 0xff;
    _groupNum = 0xffffffff;
    memset( _groupName, 0, 25 );
    _len = 0;
    _gbkCodes = NULL;
}

BDI_InviteSubmit::~BDI_InviteSubmit()
{
    if (_gbkCodes != NULL)
        DELETE_ARR(_gbkCodes);
}

unsigned int BDI_InviteSubmit::encodeBody(uint8 **pbody) const
{
    BDIEncoder enc;

    // encode RT data header
    enc.setInteger(_bid, 4);
    enc.setOctet(_priority);
    enc.setOctet(0);
    enc.setOctet(0);
    enc.setOctet(0);

	//encode RT data body
	enc.setInteger(_pid, 4);
    enc.setBDDevName(_daName);
	enc.setOctets((uint8 *)_da, 20);

	enc.setOctet(_isJoin ? 0x01 : 0x02);
	enc.setInteger(0, 2);		//RESERVED
	enc.setOctet(_groupType);
	enc.setInteger(_groupNum, 4);
    enc.setOctets(reinterpret_cast<const uint8 *>(_groupName), 24);
	enc.setInteger(0, 2);		//RESERVED
	enc.setOctet(0);			//RESERVED

	uint8 l;
	if(_len > 128)
		l = 255;
	else
		l = _len * 2;
	enc.setOctet(l);
	uint8 * pm = (uint8 *)(_gbkCodes);
	for (int i = 0; i < l; i += 2){
		enc.setOctet(pm[i + 1]);
		enc.setOctet(pm[i]);
	}

    return enc.getFrameData(pbody);
}

string BDI_InviteSubmit::toString() const
{
	ostrstream os;
	os << _pid << ','
	<< '\"' << _da << "\" "
	<< (_isJoin ? "join" : "quit")<< ", "
	<< _groupType << ", "
	<< _groupNum << ", "
	<< _len << "," << '\"';
	uint8 l;
	if(_len > 128)
		l = 255;
	else
		l = _len * 2;
	os << hex;
	for(int i = 0; i < l; i++){
		os << _gbkCodes;
		os<< "0X" << _gbkCodes[i] << " ";
	}
	os << "\"" << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
	//add end
}

void BDI_InviteSubmit::setInvitingWords(const uint16 *codes, unsigned int nchars)
{
    assert(codes != NULL);

    if (_gbkCodes != NULL)
        DELETE_ARR(_gbkCodes);
    _gbkCodes = NULL;
    _len = 0;

    if (nchars > 0) {
        _gbkCodes = NEW uint16[nchars];
        if (_gbkCodes != NULL) {
            memcpy(_gbkCodes, codes, sizeof(uint16) * nchars);
            _len = nchars;
        }
    }
}

void BDI_InviteSubmit::setGroupName( const char * name )
{
	assert(name != NULL);
	memset( _groupName, 0, 25 );
	strncpy( _groupName, name, 24 );
}

void BDI_InviteSubmit::setDestAddr( BDDevName *das )
{
	assert(NULL != das);
	_daName.setDevName(das->type(), das->style(), das->number());
}

// BDI_GetPosSubmit members

BDI_GetPosSubmit::BDI_GetPosSubmit()
	: BDI_Submit(BDI_Event::RT_Submit, BDI::GetPosReport)
{
	_sendT = time(NULL);
	_locateMode = 0x10; 	//any mode
	_reportMode = 0xFF;
	_sT = time(NULL);
	_eT = time(NULL);
	_interval = 0xFFFF;
	_maxReportNum = 0xFFFF;
	_daNum = 0;
	_das = NULL;
}

BDI_GetPosSubmit::~BDI_GetPosSubmit()
{
	if( NULL != _das ) {
        DELETE_ARR(_das);
	}
}

unsigned int BDI_GetPosSubmit::encodeBody(uint8 **pbody)const
{
    BDIEncoder enc;

    // encode RT data header
    enc.setInteger(_bid, 4);
    enc.setOctet(_priority);
    enc.setOctet(0);
    enc.setOctet(0);
    enc.setOctet(0);

	//encode RT data body
	enc.setInteger(_pid, 4);
	enc.setInteger(_sendT, 4);
	enc.setOctet(_locateMode);
	enc.setOctet(_reportMode);
	enc.setInteger(0,4);		//RESERVED
	enc.setInteger(0,2);		//RESERVED
	enc.setInteger(_sT, 4);
	enc.setInteger(_eT, 4);
	enc.setInteger(_interval, 2);
	enc.setInteger(_maxReportNum, 2);
	enc.setInteger(0,2);		//RESERVED
	enc.setOctet(0);			//RESERVED
	enc.setOctet(_maxReportNum);
	for(int i = 0; i< _maxReportNum; i++ ) {
		enc.setBDDevName(*(_das+i));		
	}

    return enc.getFrameData(pbody);
}

std::string BDI_GetPosSubmit::toString() const
{
	ostrstream os;
	os << _pid << ','
	<< '(' << BDI::timevToStr(_sendT) << ')'
	<< ',' << _locateMode
	<< ',' << _reportMode
	<< '[' << BDI::timevToStr(_sT) 
	<< ',' << BDI::timevToStr(_eT) << ']'
	<< ',' << _interval
	<< ',' << _maxReportNum
	<< '(';
	for (int i=0; i<_daNum; i++) {
		os << (_das+i)->toString() << ',';
	}
	os<< ')' << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

void BDI_GetPosSubmit::setDestAddrs( int num, BDDevName *das )
{
    assert(das != NULL);

    if (_das != NULL)
        DELETE_ARR(_das);
    _das = NULL;
    _daNum = 0;

    if (num > 0) {
        _das= NEW BDDevName[num];
        if (_das != NULL) {
            memcpy(_das, das, sizeof(BDDevName) * num);
            _daNum = num;
        }
    }
}

