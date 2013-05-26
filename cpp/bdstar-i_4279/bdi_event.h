#ifndef BDI_EVENT_H
#define BDI_EVENT_H

#include <string>

#include "bdi_define.h"
#include "bdi_util.h"
#include "bdi_event_codec.h"

class BDI_Event : public BDI::RefBase
{
public:
    enum Type {
    Register = 0x02010000,
    RegisterResp = 0x82010000,
    Login = 0x02010001, 
    LoginResp = 0x82010001, 
    LinkHold = 0x02010002, 
    LinkHoldResp = 0x82010002, 
    RedirectInd = 0x02010003, 
    RedirectResp = 0x82010003, 
    Logout = 0x02010010, 
    LogoutResp = 0x82010010, 
    RTData = 0x02010100, 
    RTDataResp = 0x82010100, 
    Qurey = 0x02010200, 
    Reply = 0x82010200, 
    User = 0xFF000000,
    };

    static const Type RT_Push = RTData;
    static const Type RT_Submit = RTData;

    static const Type UserErrorEvent = static_cast<Type>(User + 1);
    static const Type UserMobileEvent = static_cast<Type>(User + 2);
    static const Type UserGpsEvent = static_cast<Type>(User + 3);

private:
    Type _type;
    uint32 _sn; // Serial number of the event
    int _ver; // Protocol version
    int _compEncryp;
    int _status;

protected:
    uint32 _bid; // Business ID
    uint32 _pid; // Packet ID
    int _priority;
    uint8 *_data; // Raw data of the frame body
    unsigned int _dataSize;

private:
    // no copys
    BDI_Event(const BDI_Event &);
    BDI_Event &operator=(const BDI_Event &);

    void encodeHeader(BDIEncoder &) const;

protected:
    // Encode frame body.
    // The default implementation do nothing.
    virtual unsigned int encodeBody(uint8 **pbody) const;

    // Format frame header string
    std::string fh_string() const;
    // Format data header string
    std::string dh_string() const;

public:
    BDI_Event(Type type);
    BDI_Event(const uint8 *frame, unsigned int sz);
    virtual ~BDI_Event();

    // Creates a BDI_Event object corresponding to the protocol frame.
    static BDI::Ref<BDI_Event> decode(const uint8 *frame, unsigned int sz);

    // Encodes the event to protocol frame.
    // Returns the frame size, or 0 if cannot alloc memory.
    // The caller is responsible for deleting the frame buffer.
    unsigned int encode(uint8 **pframe) const;

    virtual std::string toString() const;

    Type type() const;
    uint32 sn() const;
    uint32 bid() const;
    uint32 pid() const;
    int version() const;
    int compEncryp() const;
    int status() const;
    int priority() const;

    void setSn(uint32);
    void setBid(uint32);
    void setPid(uint32);
    void setVersion(int);
    void setCompEncryp(int);
    void setStatus(int);
    void setPriority(int);

    // Set the frame body(_data) pointer to the data buffer
    // The new data must be alloced on the heap, as it is deleted
    // when the object has been destructed.
    // Only empty event can call this function
    void setFrameData(uint8 *buf, unsigned int sz);

#ifndef NDEBUG
    friend class BDI_Daemon;
#endif
};

// BDI_Event inline functions

inline BDI_Event::Type BDI_Event::type() const
{ return _type; }

inline uint32 BDI_Event::sn() const
{ return _sn; }

inline uint32 BDI_Event::bid() const
{ return _bid; }

inline uint32 BDI_Event::pid() const
{ return _pid; }

inline int BDI_Event::version() const
{ return _ver; }

inline int BDI_Event::compEncryp() const
{ return _compEncryp; }

inline int BDI_Event::status() const
{ return _status; }

inline int BDI_Event::priority() const
{ return _priority; }

inline void BDI_Event::setSn(uint32 sn)
{ _sn = sn; }

inline void BDI_Event::setBid(uint32 bid)
{ _bid = bid; }

inline void BDI_Event::setPid(uint32 pid)
{ _pid = pid; }

inline void BDI_Event::setVersion(int v)
{ _ver = v; }

inline void BDI_Event::setCompEncryp(int ce)
{ _compEncryp = ce; }

inline void BDI_Event::setStatus(int st)
{ _status = st; }

inline void BDI_Event::setPriority(int p)
{ _priority = p; }

// Register 

class BDI_CYTRegister : public BDI_Event 
{
private:
	uint32 _usrId;
	char _account[17];
	char _pwd[21];
	char _usrName[21];
	int _idType;
	char _idNum[21];
	char _mobileNum[21];
	char _telNum[21];
	char _homeAdd[41];
	char _zipCode[9];
	char _eMail[21];
	uint32 _createTime;
	int _accountStatus;
	int _photoLen;	
	char *_photo;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_CYTRegister();
    ~BDI_CYTRegister();

    std::string toString() const;

    void setUsrId(uint32 usrId);
    void setAccount(const char *account);
    void setPwd(const char *pwd);
    void setUsrName(const char *usrName);
    void setIdType(int idType);
    void setIdNum(const char *idNum);
    void setMobileNum(const char *mobileNum);
    void setTelNum(const char *telNum);
    void setHomeAdd(const char *homeAdd);
    void setZipCode(const char *zipCode);
    void setEMail(const char *eMail);
    void setCreateTime(uint32 createTime);
    void setAccountStatus(int accountStatus);
    void setPhotoLen(int photoLen);
    void setPhoto(const char *photo);
};

inline void BDI_CYTRegister::setUsrId(uint32 usrId)
{ _usrId = usrId; }

inline void BDI_CYTRegister::setIdType(int idType)
{ _idType = idType; }

inline void BDI_CYTRegister::setCreateTime(uint32 createTime)
{ _createTime = createTime; }

inline void BDI_CYTRegister::setAccountStatus(int accountStatus)
{ _accountStatus = accountStatus; }

inline void BDI_CYTRegister::setPhotoLen(int photoLen)
{ _photoLen = photoLen; }

// RegisterResp

class BDI_CYTRegisterResp : public BDI_Event
{
private:
    int _errCode;
    uint32 _id;
    char _account[17];
    int _accountStatus;
    uint32 _createTime;

public:
    BDI_CYTRegisterResp();
	BDI_CYTRegisterResp(const uint8 *frame, unsigned int sz);
    ~BDI_CYTRegisterResp();

    std::string toString() const;

    int errCode() const;
    uint32 id() const;
    const char *account() const;
    uint32 createTime() const; 
    int accountStatus() const;
};

inline int BDI_CYTRegisterResp::errCode() const
{ return _errCode; }

inline uint32 BDI_CYTRegisterResp::id() const
{ return _id; }

inline const char * BDI_CYTRegisterResp::account() const
{ return _account; }

inline uint32 BDI_CYTRegisterResp::createTime() const
{ return _createTime; }

inline int BDI_CYTRegisterResp::accountStatus() const
{ return _accountStatus; }

// ActiveTest 

class BDI_ActiveTest : public BDI_Event
{
private:
	uint32 _cytSecs;
 	uint32 _cytMicroseconds;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
	BDI_ActiveTest();
	~BDI_ActiveTest();
    std::string toString() const;
};

//ActiveTestResp

class BDI_ActiveTestResp : public BDI_Event
{
private:
	uint32 _cytSecs;
	uint32 _cytMicroseconds;
	uint32 _fosSecs;
	uint32 _fosMicroseconds;

public:
    BDI_ActiveTestResp(const uint8 *frame, unsigned int sz);

    std::string toString() const;

    uint32 cytSecs() const;
    uint32 cytMicroseconds() const;
    uint32 fosSecs() const;
    uint32 fosMicroseconds() const;
};

inline uint32 BDI_ActiveTestResp::cytSecs() const
{ return _cytSecs; }

inline uint32 BDI_ActiveTestResp::cytMicroseconds() const
{ return _cytMicroseconds; }

inline uint32 BDI_ActiveTestResp::fosSecs() const
{ return _fosSecs; }

inline uint32 BDI_ActiveTestResp::fosMicroseconds() const
{ return _fosMicroseconds; }

//Login

class BDI_Login : public BDI_Event
{
public:
    enum Mode { SendOnly, ReceiveOnly, SendReceive };

private:
    char _usr[17];
    char _pwd[21];
    uint32 _ts;
    Mode _mode;
    int _minVer;
    int _maxVer;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_Login(Mode m = SendReceive);
    ~BDI_Login();

    std::string toString() const;

    Mode mode() const;
    const char * userName() const;
    const char * password() const;
    uint32 timestamp() const;
    int minVer() const;
    int maxVer() const;

    void setMode(Mode);
    void setUserName(const char *);
    void setPassword(const char *);
    void setTimestamp(uint32);
    void setVerRange(int minVer, int maxVer);
};

inline BDI_Login::Mode BDI_Login::mode() const
{ return _mode; }

inline const char * BDI_Login::userName() const
{ return _usr; }

inline const char * BDI_Login::password() const
{ return _pwd; }

inline uint32 BDI_Login::timestamp() const
{ return _ts; }

inline int BDI_Login::minVer() const
{ return _minVer; }

inline int BDI_Login::maxVer() const
{ return _maxVer; }

inline void BDI_Login::setMode(BDI_Login::Mode m)
{ _mode = m; }

inline void BDI_Login::setTimestamp(uint32 ts)
{ _ts = ts; }

inline void BDI_Login::setVerRange(int minVer, int maxVer)
{ _minVer = minVer; _maxVer = maxVer; }

// LoginResp

class BDI_LoginResp : public BDI_Event
{
private:
    int _errCode;
    int _maxVer;
    int _minVer;

    char _r_family[3]; // Redirect server family 1
    char _r_host[3][41]; // Redirect host 1
    int _r_port[3]; // Redirect port 1

	uint32 _fosSecs;
	uint32 _fosMicroseconds;


    int _channel; // Which channel the response received

    char _authServer[21];

public:
    BDI_LoginResp(int errCode);
    BDI_LoginResp(const uint8 *frame, unsigned int sz);
    ~BDI_LoginResp();

    std::string toString() const;

    int errCode() const;
    int minVer() const;
    int maxVer() const;
    char redirectFamily1() const;
    char redirectFamily2() const;
    char redirectFamily3() const;
    const char *redirectHost1() const;
    const char *redirectHost2() const;
    const char *redirectHost3() const;
    int redirectPort1() const;
    int redirectPort2() const;
    int redirectPort3() const;
    const char *authServer() const;

    int channel() const;
    void setChannel(int);
};

inline int BDI_LoginResp::errCode() const
{ return _errCode; }

inline int BDI_LoginResp::minVer() const
{ return _minVer; }

inline int BDI_LoginResp::maxVer() const
{ return _maxVer; }

inline char BDI_LoginResp::redirectFamily1() const
{ return _r_family[0]; }

inline char BDI_LoginResp::redirectFamily2() const
{ return _r_family[1]; }

inline char BDI_LoginResp::redirectFamily3() const
{ return _r_family[2]; }

inline const char * BDI_LoginResp::redirectHost1() const
{ return _r_host[0]; }

inline const char * BDI_LoginResp::redirectHost2() const
{ return _r_host[1]; }

inline const char * BDI_LoginResp::redirectHost3() const
{ return _r_host[2]; }

inline int BDI_LoginResp::redirectPort1() const
{ return _r_port[0]; }

inline int BDI_LoginResp::redirectPort2() const
{ return _r_port[1]; }

inline int BDI_LoginResp::redirectPort3() const
{ return _r_port[2]; }

inline const char * BDI_LoginResp::authServer() const
{ return _authServer; }

inline int BDI_LoginResp::channel() const
{ return _channel; }

inline void BDI_LoginResp::setChannel(int c)
{ _channel = c; }

// Redirect		Server->CYT

class BDI_RedirectInd : public BDI_Event
{
private:
    int _families[3]; // Server family. see bdi_define.h (IPFamily)
    char _hosts[3][41];
    int _ports[3];

public:
    BDI_RedirectInd(const uint8 *frame, unsigned int sz);
    ~BDI_RedirectInd();

    std::string toString() const;

    int family1() const;
    const char * host1() const;
    int port1() const;

    int family2() const;
    const char * host2() const;
    int port2() const;

    int family3() const;
    const char * host3() const;
    int port3() const;
};

// BDI_RedirectInd inline functions

inline int BDI_RedirectInd::family1() const
{ return _families[0]; }

inline const char *BDI_RedirectInd::host1() const
{ return _hosts[0]; }

inline int BDI_RedirectInd::port1() const
{ return _ports[0]; }

inline int BDI_RedirectInd::family2() const
{ return _families[1]; }

inline const char *BDI_RedirectInd::host2() const
{ return _hosts[1]; }

inline int BDI_RedirectInd::port2() const
{ return _ports[1]; }

inline int BDI_RedirectInd::family3() const
{ return _families[2]; }

inline const char *BDI_RedirectInd::host3() const
{ return _hosts[2]; }

inline int BDI_RedirectInd::port3() const
{ return _ports[2]; }

// ~

class BDI_ErrorEvent : public BDI_Event
{
private:
    int _errCode;

public:
    BDI_ErrorEvent();
    BDI_ErrorEvent(int errCode, uint32 sn, uint32 bid, uint32 pid);

    std::string toString() const;

    void setErrorCode(int);
    int errorCode() const;
};

inline void BDI_ErrorEvent::setErrorCode(int e)
{ _errCode = e; }

inline int BDI_ErrorEvent::errorCode() const
{ return _errCode; }

// ~

typedef BDI::Ref<BDI_Event> BDI_EventRef;

#endif
