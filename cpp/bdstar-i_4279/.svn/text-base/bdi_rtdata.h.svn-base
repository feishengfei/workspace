#ifndef BDI_PUSH_H
#define BDI_PUSH_H

#include <string>

#include "bdi_event.h"

// This class represent a real-time data frame, it may be a asynchronous
// event pushed by server, or somekind a receipt.
class BDI_RTData : public BDI_Event
{
public:
    BDI_RTData(int dataType);
    BDI_RTData(const uint8 *frame, unsigned int sz);
    virtual ~BDI_RTData();

    // Creates a BDI_RTData object corresponding to the protocol frame.
    static BDI::Ref<BDI_Event> decode(const uint8 *frame, unsigned int sz);

    // Create a BDI_Event with event type BDI_Event::PushResp
    // to acknowledge the push event.
    virtual BDI::Ref<BDI_Event> ack() const;
};

// Terminal list pushed by server when CYT login 
// doc name: QDC_STList
// mf08b xzhou

class BDI_TermListPush : public BDI_RTData
{
private:
    int _errCode;
    int _termCount;
    BDTermInfo *_terms;

public:
    BDI_TermListPush(const uint8 *frame, unsigned int sz);
    ~BDI_TermListPush();

    std::string toString() const;
	
	int errCode() const;
    const BDTermInfo *termList() const;
    int termCount() const;
};

inline int BDI_TermListPush::errCode() const
{ return _errCode; }

inline const BDTermInfo *BDI_TermListPush::termList() const
{ return _terms; }

inline int BDI_TermListPush::termCount() const
{ return _termCount; }

// CYT invite ST JOINTO GROUP Reply
// doc name: RTDC_InviteMSTJoinToGroup_Reply
// mf08b xzhou

class BDI_TermJoinPush : public BDI_RTData
{
private:
    bool _isAgree;
    BDDevName _oa;
    uint32 _groupNum;

public:
    BDI_TermJoinPush(const uint8 *frame, unsigned int sz);
    ~BDI_TermJoinPush();

    std::string toString() const;


    bool isAgree() const;
    const BDDevName &origAddr() const;
    const uint32 groupNum() const;
};

inline bool BDI_TermJoinPush::isAgree() const
{ return _isAgree; }

inline const BDDevName &BDI_TermJoinPush::origAddr() const
{ return _oa; }


inline const uint32 BDI_TermJoinPush::groupNum() const
{ return _groupNum; }


// FOS got GetPos from CYT then get PosReply from db, 
// and send data back to CYT
// doc: RTDC_Pos_Reply
// mf08b xzhou

class BDI_TermPosReplyPush : public BDI_RTData
{
private:
	BDDevName _oa;
	BDTermStatus _status;
	GPosition _pos;
			
public:
	BDI_TermPosReplyPush(const uint8 *frame, unsigned int sz);
	~BDI_TermPosReplyPush();

	std::string toString() const;
	
	BDDevName origAddr() const;
	BDTermStatus getTermStatus() const;
	GPosition gpos() const;
};

inline BDDevName BDI_TermPosReplyPush::origAddr() const
{ return _oa; }

inline BDTermStatus BDI_TermPosReplyPush::getTermStatus() const
{ return _status; }

inline GPosition BDI_TermPosReplyPush::gpos() const
{ return _pos; }

// FOS got GetPos from CYT then get PosReport from ST, 
// and ST AUTO report data to FOS, then resend back to CYT
// doc: RTDC_Pos_Report
// mf08b xzhou

class BDI_TermPosReportPush : public BDI_RTData
{
public:
    static const int MAX_POS_ITEMS = 100;
private:
	BDDevName _oa;
	int _termNum;
	BDTermStatus _status;
	GPosition *_posp;
			
public:
	BDI_TermPosReportPush(const uint8 *frame, unsigned int sz);
	~BDI_TermPosReportPush();

	std::string toString() const;
	BDDevName origAddr() const;
	int posCount() const;
	BDTermStatus getTermStatus() const;
	GPosition *gposArray() const;
};

inline BDDevName BDI_TermPosReportPush::origAddr() const
{ return _oa; }

inline int BDI_TermPosReportPush::posCount() const { return _termNum; } 
inline BDTermStatus BDI_TermPosReportPush::getTermStatus() const
{ return _status; }

inline GPosition *BDI_TermPosReportPush::gposArray() const
{ return _posp; }

// Terminal SOS event
// doc name: RTDC_Emergence_Report
// mf08b xzhou

class BDI_TermSosPush : public BDI_RTData
{
public:
	enum SOSType {
		SOS_UnknownReason = 0x00, 
		SOS_Crash = 0x01, 
		SOS_Storm = 0x02, 
		SOS_Fire = 0x03, 
		SOS_Grounded = 0x04, 
		SOS_Sick = 0x05, 
		SOS_Detained = 0x06, 
		SOS_EngineTrouble = 0x07,
		SOS_Enemy = 0x30,
		SOS_Cancel = 0xA0,
	};

private:
    BDDevName _oa;
    time_t _ts;
    unsigned int _sosType;
    BDTermStatus _termStat;
    GPosition _gpos;
    char _site[21];
    BDMessageRef _msg;



public:
    BDI_TermSosPush();
    BDI_TermSosPush(const uint8 *frame, unsigned int sz);
    ~BDI_TermSosPush();

    std::string toString() const;

    unsigned int sosType() const;
    const BDDevName &origAddr() const;
    const char *siteStr() const;
    const BDTermStatus &termStatus() const;
    const GPosition &gpos() const;
    bool isCanceled() const;

    time_t ts() const;

    int textCodec() const;
    const uint16 *textCodes() const;
    int textLength() const;
};

inline unsigned int BDI_TermSosPush::sosType() const
{ return _sosType; }

inline const BDDevName &BDI_TermSosPush::origAddr() const
{ return _oa; }

inline const char *BDI_TermSosPush::siteStr() const
{ return _site; }

inline const BDTermStatus &BDI_TermSosPush::termStatus() const
{ return _termStat; }

inline const GPosition &BDI_TermSosPush::gpos() const
{ return _gpos; }


/*
* FOLLOWING IS UNUSED in mf08b
*/

// ~

/*
// The receipt to invition and message
// doc name: RTDC_CYTInviteSTtoJoin_Receipt
class BDI_ReceiptEvent : public BDI_RTData
{
private:
    BDDevName _oa;
    BDDevName _da;
    uint32 _receiptType;
    uint32 _messageType;
    BDMessageRef _bdmsg;

private:
    // This event cannot acknowledge
    BDI::Ref<BDI_Event> ack() const;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_ReceiptEvent();
    BDI_ReceiptEvent(const uint8 *frame, unsigned int sz);
    ~BDI_ReceiptEvent();

    std::string toString() const;

    const BDDevName &origAddr() const;
    const BDDevName &destAddr() const;
    uint32 receiptType() const;
    uint32 messageType() const;
    const uint16 *textGbkCode() const;
    int textLength() const;

    void setOrigAddr(int, int, const char *);
    void setOrigAddr(const BDDevName &);
    void setDestAddr(int, int, const char *);
    void setDestAddr(const BDDevName &);
    void setReceiptType(uint32);
    void setMessageType(uint32);
    void setMessage(const uint16 *gbkCodes, int nchars);
};

// BDI_ReceiptEvent inline functions

inline const BDDevName &BDI_ReceiptEvent::origAddr() const
{ return _oa; }

inline const BDDevName &BDI_ReceiptEvent::destAddr() const
{ return _da; }

inline uint32 BDI_ReceiptEvent::receiptType() const
{ return _receiptType; }

inline uint32 BDI_ReceiptEvent::messageType() const
{ return _messageType; }

inline void BDI_ReceiptEvent::setOrigAddr(int type, int style, const char *number)
{ _oa.setDevName(type, style, number); }

inline void BDI_ReceiptEvent::setOrigAddr(const BDDevName &name)
{ _oa = name; }

inline void BDI_ReceiptEvent::setDestAddr(int type, int style, const char *number)
{ _da.setDevName(type, style, number); }

inline void BDI_ReceiptEvent::setDestAddr(const BDDevName &name)
{ _da = name; }

inline void BDI_ReceiptEvent::setReceiptType(uint32 t)
{ _receiptType = t; }

inline void BDI_ReceiptEvent::setMessageType(uint32 t)
{ _messageType = t; }

//~
*/

// As a reply to terminal location
// doc name: RTDC_Pos_Reply
class BDI_PosReplyPush : public BDI_RTData
{
private:
    BDDevName _oa;
    BDDevName _da;
    BDTermStatus _termStat;
    GPosition _gpos;

private:
    // This push event cannot acknowledge
    BDI::Ref<BDI_Event> ack() const;

public:
    BDI_PosReplyPush(const uint8 *frame, unsigned int sz);
    ~BDI_PosReplyPush();

    std::string toString() const;

    const BDDevName &origAddr() const;
    const BDDevName &destAddr() const;
    const BDTermStatus &termStatus() const;
    const GPosition &gpos() const;
};

// BDI_PosReplyPush inline functions

inline const BDDevName &BDI_PosReplyPush::origAddr() const
{ return _oa; }

inline const BDDevName &BDI_PosReplyPush::destAddr() const
{ return _da; }

inline const BDTermStatus &BDI_PosReplyPush::termStatus() const
{ return _termStat; }

inline const GPosition &BDI_PosReplyPush::gpos() const
{ return _gpos; }

// ~

// Terminal report its position automatically to CYT
// doc name: RTDC_Pos_Auto_Report
/*
class BDI_PosReportEvent : public BDI_RTData
{
public:
    static const int MAX_POS_ITEMS = 5;

private:
    BDDevName _oa;
    BDTermStatus _termStat;
    GPosition _gpos[MAX_POS_ITEMS];
    int _posCount;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_PosReportEvent();
    BDI_PosReportEvent(const uint8 *frame, unsigned int sz);
    ~BDI_PosReportEvent();

    std::string toString() const;

    const BDDevName &origAddr() const;
    const BDTermStatus &termStatus() const;
    const GPosition *gposArray() const;
    int posCount() const;

    void setDevName(int, int, const char *);
    void setTermStatus(int, int, int);
    void setTermStatus(const BDTermStatus &);
    void addPosition(const GPosition &);

    //bool unite(const BDI_PosReportEvent &);
};

// BDI_PosReportEvent inline functions

inline const BDDevName &BDI_PosReportEvent::origAddr() const
{ return _oa; }

inline const BDTermStatus &BDI_PosReportEvent::termStatus() const
{ return _termStat; }

inline const GPosition *BDI_PosReportEvent::gposArray() const
{ return _gpos; }

inline int BDI_PosReportEvent::posCount() const
{ return _posCount; }

inline void BDI_PosReportEvent::setTermStatus(const BDTermStatus &tst)
{ _termStat = tst; }
*/

// ~

// Terminal SOS event
// doc name: RTDC_Emergence/RTDC_EmergenceRelease_Report
class BDI_TermSosEvent : public BDI_RTData
{
public:
    enum SOSType {
		SOS_UnknownReason = 0, 
		SOS_Crash = 1, 
		SOS_Storm = 2, 
		SOS_Fire = 3, 
		SOS_Grounded = 4, 
		SOS_Sick = 5, 
		SOS_Detained = 6, 
		SOS_EngineTrouble = 7
    };

private:
    SOSType _sosType;
    BDDevName _oa;
    char _site[25];
    GPosition _gpos;
    BDTermStatus _termStat;
    bool _canceled;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_TermSosEvent();
    BDI_TermSosEvent(const uint8 *frame, unsigned int sz);
    ~BDI_TermSosEvent();

    std::string toString() const;

    SOSType sosType() const;
    const BDDevName &origAddr() const;
    const char *siteStr() const;
    const BDTermStatus &termStatus() const;
    const GPosition &gpos() const;
    bool isCanceled() const;

    void setSosType(SOSType);
    void setDevName(int type, int style, const char *num);
    void setSiteStr(const char *);
    void setTermStatus(int, int, int);
    void setPosition(const GPosition &);
    void setCanceled(bool);
};

// BDI_TermSosEvent inline functions

inline BDI_TermSosEvent::SOSType BDI_TermSosEvent::sosType() const
{ return _sosType; }

inline const BDDevName &BDI_TermSosEvent::origAddr() const
{ return _oa; }

inline const char *BDI_TermSosEvent::siteStr() const
{ return _site; }

inline const BDTermStatus &BDI_TermSosEvent::termStatus() const
{ return _termStat; }

inline const GPosition &BDI_TermSosEvent::gpos() const
{ return _gpos; }

inline bool BDI_TermSosEvent::isCanceled() const
{ return _canceled; }

inline void BDI_TermSosEvent::setSosType(SOSType t)
{ _sosType = t; }

inline void BDI_TermSosEvent::setPosition(const GPosition &gp)
{ _gpos = gp; }

// ~

// Import/Outport report event
// doc name: RTDC_ImportOutport_Report

class BDI_ImportOutportEvent : public BDI_RTData
{
private:
    bool _isImport;
    BDDevName _oa;
    GPosition _gpos;
    BDMessageRef _bdmsg;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_ImportOutportEvent();
    BDI_ImportOutportEvent(const uint8 *frame, unsigned int sz);
    ~BDI_ImportOutportEvent();

    std::string toString() const;

    bool isImport() const;
    const BDDevName &origAddr() const;
    const GPosition &gpos() const;
    BDMessageRef message() const;

    void setIsImport(bool);
    void setOrigAddr(int, int, const char *);
    void setOrigAddr(const BDDevName &);
    void setPosition(const GPosition &);
};

// BDI_ImportOutportEvent inline functions

inline bool BDI_ImportOutportEvent::isImport() const
{ return _isImport; }

inline const BDDevName &BDI_ImportOutportEvent::origAddr() const
{ return _oa; }

inline const GPosition &BDI_ImportOutportEvent::gpos() const
{ return _gpos; }

inline BDMessageRef BDI_ImportOutportEvent::message() const
{ return _bdmsg; }

inline void BDI_ImportOutportEvent::setIsImport(bool x)
{ _isImport = x; }

inline void BDI_ImportOutportEvent::setOrigAddr(int type, int style, const char *number)
{ _oa.setDevName(type, style, number); }

inline void BDI_ImportOutportEvent::setOrigAddr(const BDDevName &name)
{ _oa = name; }

inline void BDI_ImportOutportEvent::setPosition(const GPosition &gp)
{ _gpos = gp; }

// ~

#endif
