#ifndef BDI_SUBMIT_H
#define BDI_SUBMIT_H

#include <string>

#include "bdi_event.h"
/*
* generally speaking BDI_Submit IS A kind of BDI_RTData
*											xzhou
*/

// A event that CYT submit to server, it may be a real-time 
// business or a qurey business.
class BDI_Submit : public BDI_Event
{
protected:
    char _oa[21]; // Originator address
    char _da[21]; // Destination address

public:
    BDI_Submit(BDI_Event::Type, int submitType);
    virtual ~BDI_Submit() = 0;

    const char *origAddress() const;
    const char *destAddress() const;

    void setOrigAddress(const char *);
    void setDestAddress(const char *);
};

// BDI_Submit inline functions

inline const char *BDI_Submit::origAddress() const
{ return _oa; }

inline const char *BDI_Submit::destAddress() const
{ return _da; }

// ~

// CYT invites a terminal to join
// doc name: RTDC_CYTInviteSTtoGroup
// mf08b xzhou

class BDI_InviteSubmit : public BDI_Submit
{
private:
    bool _isJoin;
	int _groupType;
	uint32 _groupNum;
	char _groupName[25];
	BDDevName _daName;

    int _len; // Number of GBK characators
    uint16 *_gbkCodes; // Characator GBK values of inviting words

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_InviteSubmit();
    ~BDI_InviteSubmit();

    std::string toString() const;

    bool isJoin() const;
    const uint16 *wordsGbkCode() const;
    int wordsLength() const;

    void setJoin(bool);
    void setInvitingWords(const uint16 *codes, unsigned int nchars);
	void setGroupType(int type);
    void setGroupName(const char *name);
	void setGroupNum(uint32 groupNum);
	void setDestAddr( BDDevName *das );
};

inline bool BDI_InviteSubmit::isJoin() const
{ return _isJoin; }

inline const uint16 *BDI_InviteSubmit::wordsGbkCode() const
{ return _gbkCodes; }

inline int BDI_InviteSubmit::wordsLength() const
{ return _len; }

inline void BDI_InviteSubmit::setJoin(bool isJoin)
{ _isJoin = isJoin; }

inline void BDI_InviteSubmit::setGroupType(int type)
{_groupType = type;}

inline void BDI_InviteSubmit::setGroupNum(uint32 groupNum)
{ _groupNum = groupNum; }



// CYT sends position report command
// doc name: RTDC_GetPos
// mf08b xzhou

class BDI_GetPosSubmit : public BDI_Submit
{
private:
	uint32 _sendT;
	int _locateMode;
	int _reportMode;
	uint32 _sT;
	uint32 _eT;
	int _interval;
	int _maxReportNum;
	int _daNum;
	BDDevName *_das;
	

protected:
	unsigned int encodeBody(uint8 **pbody)const;

public:
	BDI_GetPosSubmit();
	~BDI_GetPosSubmit();

	std::string toString() const;

	void setDestAddrs( int num, BDDevName *das );
	void setLocateMode(int mode);
	void setReportMode(int mode);
	void setInterval(int s);
	void setMaxReportNum(int n);
	void setStartTime(time_t t);
	void setEndTime(time_t t);
};

inline void BDI_GetPosSubmit::setLocateMode(int mode)
{ _locateMode = mode; }

inline void BDI_GetPosSubmit::setReportMode(int mode)
{ _reportMode = mode; }

inline void BDI_GetPosSubmit::setInterval(int s)
{ _interval = s; }

inline void BDI_GetPosSubmit::setMaxReportNum(int n)
{ _maxReportNum = n; }

inline void BDI_GetPosSubmit::setStartTime(time_t t)
{ _sT = t; }

inline void BDI_GetPosSubmit::setEndTime(time_t t)
{ _eT = t; }

#endif
