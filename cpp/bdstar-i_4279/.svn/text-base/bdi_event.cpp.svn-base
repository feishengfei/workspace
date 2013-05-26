#include <stdio.h>
#include <stdlib.h>

#include <sys/time.h>

#include <strstream>
#include <math.h>

#include "rbglobal.h"
#include "bdi_rtdata.h"
#include "bdi_submit.h"
#include "bdi_reply.h"
#include "bdi_event.h"

using namespace std;
using namespace BDI;

// BDI_Event members

void BDI_Event::encodeHeader(BDIEncoder &enc) const
{
	enc.setInteger(_dataSize + BDI::FRAME_HEADER_LEN, 4);
	enc.setInteger(_type, 4);
	enc.setInteger(_sn, 4);
	enc.setOctet(_ver);
	enc.setOctet(_compEncryp);
	enc.setOctet(_status);
	enc.setOctet(0);
}

unsigned int BDI_Event::encodeBody(uint8 **) const
{
    // do nothing
    return _dataSize;
}

string BDI_Event::fh_string() const
{
	ostrstream os;
	os << '[' << _dataSize + FRAME_HEADER_LEN << ',';
	switch (_type) {
	case Register: 
		os << "Register";
		break;
	case RegisterResp: 
		os << "RegisterResp";
		break;
	case Login:
		os << "Login";
		break;
	case LoginResp:
		os << "LoginResp";
		break;
	case Logout:
		os << "Logout";
		break;
	case LogoutResp:
		os << "LogoutResp";
		break;
	case RedirectInd:
		os << "RedirectInd";
		break;
	case RTData:
		os << "RTData";
		break;
	case RTDataResp:
		os << "RTDataResp";
		break;
	case Qurey:
		os << "Qurey";
		break;
	case Reply:
		os << "Reply";
		break;
	case LinkHold:
		os << "LinkHold";
		break;
	case LinkHoldResp:
		os << "LinkHoldResp";
		break;
	default:
		os << hex << "0X" << _type << dec;
		break;
	}
	os << ','
	   << _sn << ','
	   << "0x" << hex << _ver << dec << ','
	   << _compEncryp << ','
	   << "0x" << hex << _status << dec << ']'
	   << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return res;
}

string BDI_Event::dh_string() const
{
	ostrstream os;
	os << '['
	   << "0x" << hex << _bid << ',' << dec
	   << _priority << ']'
	   << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return res;
}

BDI_Event::BDI_Event(BDI_Event::Type t)
{
	_type = t;
	_sn = 0;
	_bid = 0;
	_pid = 0;
	_ver = 0x10;
	_compEncryp = 0;
	_status = 0x80;
	_priority = BDI::PRIORITY_NOMAL;
	_data = NULL;
	_dataSize = 0;
}

BDI_Event::BDI_Event(const uint8 *frame, unsigned int sz)
{
	RB_ASSERT(frame != NULL && sz >= BDI::FRAME_HEADER_LEN); // must have frame header

	BDIDecoder dec(frame, sz);
	_dataSize = dec.getInteger(4) - BDI::FRAME_HEADER_LEN;
	_type = static_cast<Type>(dec.getInteger(4));
	_sn = dec.getInteger(4);
	_ver = dec.getOctet();
	_compEncryp = dec.getOctet();
	_status = dec.getOctet();
	dec.getOctet();

	_bid = 0;
	_pid = 0;
	_priority = BDI::PRIORITY_NOMAL;
	_data = NULL;

	if (_dataSize > 0) {
		_data = NEW uint8[_dataSize];
		if (_data != NULL)
			dec.getOctets(_data, _dataSize);
	}
}

BDI_Event::~BDI_Event()
{
	if (_data != NULL)
		DELETE_ARR(_data);
}

Ref<BDI_Event> BDI_Event::decode(const uint8 *frame, unsigned int sz)
{
	RB_ASSERT(frame != NULL && sz >= BDI::FRAME_HEADER_LEN); // must have frame header

	Ref<BDI_Event> res;
	BDIDecoder dec(frame, sz);
	dec.getInteger(4);
	const uint32 type = dec.getInteger(4);
	switch (type) {
	case LinkHoldResp:
		res = NEW BDI_ActiveTestResp(frame, sz);
		break;
	case RegisterResp:
		res = NEW BDI_CYTRegisterResp(frame, sz);
		break;
	case LoginResp:
		res = NEW BDI_LoginResp(frame, sz);
		break;
	case RedirectInd:
		res = NEW BDI_RedirectInd(frame, sz);
		break;
	case RTData:
		res = BDI_RTData::decode(frame, sz);
		break;
	case Reply:
		res = BDI_Reply::decode(frame, sz);
		break;
	default:
		res = NEW BDI_Event(frame, sz);
		break;
	}
	return res;
}

unsigned int BDI_Event::encode(uint8 **pframe) const
{
    BDI_Event *self = const_cast<BDI_Event *>(this);
    /*
    if (self->_data != NULL) {
        DELETE_ARR(self->_data);
        self->_data = NULL;
    }
    self->_dataSize = encodeBody(&(self->_data));
    */
    if (self->_data == NULL)
        self->_dataSize = encodeBody(&(self->_data));

	BDIEncoder enc;
	encodeHeader(enc);
	if (_data != NULL)
		enc.setOctets(_data, _dataSize);
	return enc.getFrameData(pframe);
}

string BDI_Event::toString() const
{
	string s = fh_string();
	if (_data != NULL)
		s.append("...");
	return s;
}

void BDI_Event::setFrameData(uint8 *buf, unsigned int sz)
{
	RB_ASSERT(_data == NULL); // Only empty event may call this function

	_data = buf;
	_dataSize = sz;
}

// BDI_CYTRegister member

BDI_CYTRegister::BDI_CYTRegister()
    : BDI_Event(BDI_Event::Register)
{
	_usrId = 0xFFFFFFFF;
	memset( _account, 0, 17 );
	strncpy( _account, "@@@@@@@@@@@@@@@@", 16 );
	memset( _pwd, 0, 21 );
	memset( _usrName, 0, 21 );
	_idType = 0xFF;				
	memset( _idNum, 0, 21 );
	memset( _mobileNum, 0, 21 );
	memset( _telNum, 0, 21 );
	memset( _homeAdd, 0, 41 );
	memset( _zipCode, 0, 9 );
	memset( _eMail, 0, 21 );
	_createTime = time(NULL);
	_accountStatus = 0x00;		//初始注册
	_photoLen = 0;
	_photo = NULL;
}

BDI_CYTRegister::~BDI_CYTRegister()
{
	if (NULL != _photo) {
        DELETE_ARR(_photo);
	}
}

unsigned int BDI_CYTRegister::encodeBody(uint8 **pbody) const
{
    BDIEncoder e;
    e.setInteger(_usrId, 4);
    e.setOctets(reinterpret_cast<const uint8 *>(_account), 16);
    e.setOctets(reinterpret_cast<const uint8 *>(_pwd), 20);
    e.setOctets(reinterpret_cast<const uint8 *>(_usrName), 20);
    e.setInteger(0, 2);
    e.setOctet(0);
	e.setOctet(_idType);
    e.setOctets(reinterpret_cast<const uint8 *>(_idNum), 20);

    e.setOctets(reinterpret_cast<const uint8 *>(_mobileNum), 20);
    e.setOctets(reinterpret_cast<const uint8 *>(_telNum), 20);
    e.setOctets(reinterpret_cast<const uint8 *>(_homeAdd), 40);
    e.setOctets(reinterpret_cast<const uint8 *>(_zipCode), 8);
    e.setOctets(reinterpret_cast<const uint8 *>(_eMail), 20);
    e.setInteger(_createTime, 4);
    e.setOctet(_accountStatus);
    e.setInteger(0, 4);
    e.setOctet(0);
    e.setInteger(_photoLen, 2);

    if( 0 != _photoLen ) {
		e.setOctets(reinterpret_cast<const uint8 *>(_photo), _photoLen);
	}
    return e.getFrameData(pbody);
}

string BDI_CYTRegister::toString() const
{
	ostrstream os;
	os << _pid << ','
       << _usrId << ','
       << "\"" << _account << "\","
       << "\"" << _pwd << "\","
       << "\"" << _usrName << "\","
       << _idType << ','
       << "\"" << _idNum << "\","
       << '\n'
       << "\"" << _mobileNum << "\","
       << "\"" << _telNum << "\","
       << "\"" << _homeAdd << "\","
       << "\"" << _zipCode << "\","
       << "\"" << _eMail << "\","
       << '\n'
       << timevToStr(_createTime) << ','
       << _accountStatus << ','
       << _photoLen 
       << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + dh_string() + res;
}

void BDI_CYTRegister::setAccount(const char *account)
{ 
	memset( _account, 0, 17 );
	strncpy( _account, account, 16 ); 
}

void BDI_CYTRegister::setPwd(const char *pwd)
{ 
	memset( _pwd, 0, 21 );
	strncpy( _pwd, pwd, 20 ); 
}

void BDI_CYTRegister::setUsrName(const char *usrName)
{ 
	memset( _usrName, 0, 21 );
	strncpy( _usrName, usrName, 20 ); 
}

void BDI_CYTRegister::setIdNum(const char *idNum)
{ 
	memset( _idNum, 0, 21 );
	strncpy( _idNum, idNum, 20); 
}

void BDI_CYTRegister::setMobileNum(const char *mobileNum)
{ 
	memset( _mobileNum, 0, 21 );
	strncpy( _mobileNum, mobileNum, 20); 
}

void BDI_CYTRegister::setTelNum(const char *telNum)
{ 
	memset( _telNum, 0, 21 );
	strncpy( _telNum, telNum, 20); 
}

void BDI_CYTRegister::setHomeAdd(const char *homeAdd)
{
	memset( _homeAdd, 0, 41 );
	strncpy( _homeAdd, homeAdd, 40);
}

void BDI_CYTRegister::setZipCode(const char *zipCode)
{
	memset( _zipCode, 0, 9 );
	strncpy( _zipCode, zipCode, 8); 
}

void BDI_CYTRegister::setEMail(const char *eMail)
{ 
	memset( _eMail, 0, 21 );
	strncpy( _eMail, eMail, 20 );
}

void BDI_CYTRegister::setPhoto(const char *photo)
{ 	
	memcpy(_photo, photo, _photoLen); 
}

// BDI_CYTRegisterResp members

BDI_CYTRegisterResp::BDI_CYTRegisterResp()
	: BDI_Event(BDI_Event::RegisterResp)
{
	_errCode = 0xFF;
	_id = 0xFFFFFFFF;
	memset(_account, 0, 17);
	_createTime = 0xFFFFFFFF;
	_accountStatus = 0xFF;
}

BDI_CYTRegisterResp::BDI_CYTRegisterResp(const uint8 *frame, unsigned int sz)
	: BDI_Event(frame, sz)
{
	BDIDecoder dec(_data, _dataSize);
	_errCode = dec.getOctet();
	dec.getInteger(4);	//RESERVED
	dec.getInteger(2);	//RESERVED
	dec.getOctet();		//RESERVED
	_id = dec.getInteger(4);
    dec.getOctets(reinterpret_cast<uint8 *>(_account), 16);
	_createTime = dec.getInteger(4);
	_accountStatus = dec.getOctet();
}

BDI_CYTRegisterResp::~BDI_CYTRegisterResp()
{
}

std::string BDI_CYTRegisterResp::toString() const
{
	ostrstream os;
	
	os << '(' << _errCode << ')' << ','
	<< _id << ','
	<< '\"' << _account << "\","
	<< timevToStr(_createTime) << ','
	<< _accountStatus << ','
	<< ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

// BDI_ActiveTest members

BDI_ActiveTest::BDI_ActiveTest()
	: BDI_Event(BDI_Event::LinkHold)
{
	struct timeval t;
	gettimeofday(&t, NULL);
	_cytSecs = t.tv_sec;
	_cytMicroseconds = t.tv_usec / 1000;
}

BDI_ActiveTest::~BDI_ActiveTest()
{
}

string BDI_ActiveTest::toString() const
{
	ostrstream os;
	os << '\"' << timevToStr(static_cast<time_t>(_cytSecs)) << '\"' 
	   << "," << _cytMicroseconds << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}


unsigned int BDI_ActiveTest::encodeBody(uint8 **pbody) const
{
	BDIEncoder enc;
	enc.setInteger(_cytSecs, 4);
	enc.setInteger(_cytMicroseconds, 4);
	return enc.getFrameData(pbody);
}

// BDI_ActiveTestResp members

BDI_ActiveTestResp::BDI_ActiveTestResp(const uint8 *frame, unsigned int sz)
 : BDI_Event(frame, sz)
{
	BDIDecoder dec(_data, _dataSize);	
	_cytSecs = dec.getInteger(4);
	_cytMicroseconds = dec.getInteger(4);
	_fosSecs = dec.getInteger(4);
	_fosMicroseconds = dec.getInteger(4);

	if( fabs(difftime( _fosSecs, _cytSecs )) > 60 ) {
		time_t fos_t = _fosSecs;
		stime(&fos_t);
	}
}

string BDI_ActiveTestResp::toString() const
{
	ostrstream os;
	os << '\"' << timevToStr(static_cast<time_t>(_cytSecs)) << '\"' 
	   << "," << _cytMicroseconds << ','
	   << '\"' << timevToStr(static_cast<time_t>(_fosSecs)) << '\"' 
	   << "," << _fosMicroseconds 
	   << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

// BDI_Login members

unsigned int BDI_Login::encodeBody(uint8 **pbody) const
{
	BDIEncoder enc;

	enc.setOctets((const uint8 *)_usr, 16);
	enc.setOctets((const uint8 *)_pwd, 20);
	enc.setInteger(_ts, 4);
	enc.setOctet(_mode);
	enc.setOctet(_maxVer);
	enc.setOctet(_minVer);
	uint8 buf[9];
	memset(buf, 0, 9);
	enc.setOctets(buf, 9);

	return enc.getFrameData(pbody);
}

BDI_Login::BDI_Login(BDI_Login::Mode m)
    : BDI_Event(BDI_Event::Login)
{
	_mode = m;
	_ts = 0;
	_minVer = 0x10;
	_maxVer = 0x10;
	memset(_usr, 0, 17);
	memset(_pwd, 0, 21);
}

BDI_Login::~BDI_Login()
{
}

void BDI_Login::setUserName(const char *s)
{
	memset(_usr, 0, 17);
	strncpy(_usr, s, 16);
}

void BDI_Login::setPassword(const char *s)
{
	memset(_pwd, 0, 21);
	strncpy(_pwd, s, 20);
}

string BDI_Login::toString() const
{
	ostrstream os;
	os << '\"' << _usr << '\"' << ','
	   << '\"' << _pwd << '\"' << ','
	   << '\"' << timevToStr(static_cast<time_t>(_ts)) << '\"' << ','
	   << _mode << ','
	   << "0x" << hex << _maxVer << dec << ','
	   << "0x" << hex << _minVer << dec << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

// BDI_LoginResp members

BDI_LoginResp::BDI_LoginResp(int errCode)
	: BDI_Event(BDI_Event::LoginResp)
{
	_fosSecs = 0;
	_fosMicroseconds = 0;
	_errCode = errCode;
	_minVer = 0;
	_maxVer = 0;
	for(int i = 0; i < 3; i++){
		_r_family[i] = 0;
		_r_port[i] = 0;
	}
	memset(_r_host, 0, 41*3);

    _channel = -1;
	memset(_authServer, 0, 21);
}

BDI_LoginResp::BDI_LoginResp(const uint8 *frame, unsigned int sz)
	: BDI_Event(frame, sz)
{
	BDIDecoder dec(_data, _dataSize);
	_fosSecs = dec.getInteger(4);
	_fosMicroseconds = dec.getInteger(4);
	_errCode = dec.getOctet();
	_maxVer = dec.getOctet();
	_minVer = dec.getOctet();
	dec.getOctet();				//RESERVED

	_r_family[0] = dec.getOctet();
	dec.getInteger(3);			//RESERVED
	_r_port[0] = dec.getInteger(4);
	dec.getOctets((uint8 *)_r_host[0], 40);

	_r_family[1] = dec.getOctet();
	dec.getInteger(3);			//RESERVED
	_r_port[1] = dec.getInteger(4);
	dec.getOctets((uint8 *)_r_host[1], 40);

	_r_family[2] = dec.getOctet();
	dec.getInteger(3);			//RESERVED
	_r_port[2] = dec.getInteger(4);
	dec.getOctets((uint8 *)_r_host[2], 40);

	dec.getOctets((uint8 *)_authServer, 20);

	dec.getInteger(8);			//RESERVED

    _channel = -1;
	
	uint32 cytT = time(NULL);
	if( fabs(difftime( _fosSecs, cytT )) > 60 ) {
		time_t fos_t = static_cast<time_t>(_fosSecs);
		stime(&fos_t);
	}
}

BDI_LoginResp::~BDI_LoginResp()
{
}

string BDI_LoginResp::toString() const
{
	ostrstream os;

	os << '\"' << timevToStr(static_cast<time_t>(_fosSecs)) << '\"' 
	   << "," << _fosMicroseconds 
	   << _errCode << ','
	   << _maxVer << ','
	   << _maxVer << ','
	   << "["
	   << _r_family[0] << ','
	   << _r_port[0] << ','
	   << '\"' << _r_host[0] << "\","
	   << _r_family[1] << ','
	   << _r_port[1] << ','
	   << '\"' << _r_host[1] << "\","
	   << _r_family[2] << ','
	   << _r_port[2] << ','
	   << '\"' << _r_host[2] << "\","
	   << "]"
       << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

// BDI_RedirectInd members

BDI_RedirectInd::BDI_RedirectInd(const uint8 *frame, unsigned int sz)
		: BDI_Event(frame, sz)
{
	BDIDecoder dec(_data, _dataSize);
	uint8 buf[3];
	for (int i = 0; i < 3; i++) {
		_families[i] = dec.getOctet();
		dec.getOctets(buf, 3); 			//Reserved
		_ports[i] = dec.getInteger(4);
		dec.getOctets((uint8 *)(_hosts[i]), 40);
		_hosts[i][40] = 0;
	}
}

BDI_RedirectInd::~BDI_RedirectInd()
{
}

string BDI_RedirectInd::toString() const
{
	ostrstream os;
	for(int i = 0; i < 3; i++){
		os << _families[i] << ','
		   << _ports[i] << ','
		   << _hosts[i];
		if (i != 2)
			os << ',';
	}
	os << ends;

	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return fh_string() + res;
}

// BDI_ErrorEvent members

BDI_ErrorEvent::BDI_ErrorEvent()
    : BDI_Event(BDI_Event::UserErrorEvent), _errCode(0)
{
}

BDI_ErrorEvent::BDI_ErrorEvent(int e, uint32 sn, uint32 bid, uint32 pid)
    : BDI_Event(BDI_Event::UserErrorEvent), _errCode(e)
{
    setSn(sn);
    setBid(bid);
    setPid(pid);
}

string BDI_ErrorEvent::toString() const
{
	ostrstream os;
	os << "[Error," << _errCode << ']'
       << sn() << ','
       << hex << "0X" << bid() << dec << ','
       << pid()
       << ends;
	char *ss = os.str();
	string res(ss);
	delete[] ss;
	return res;
}
