#ifndef BDI_MESSAGE_H
#define BDI_MESSAGE_H

#include <string>

#include "bdi_rtdata.h"
#include "bdi_util.h"

class BDI_MessageEvent : public BDI_RTData
{
public:
private:
    void init();

protected:
	time_t _ts;
    BDDevName _oa;
    BDDevName _da;
    int _messageType;
    BDMessageRef _bdmsg;
    bool _needReceipt;

public:
    BDI_MessageEvent(int dataType);
    BDI_MessageEvent(const uint8 *frame, unsigned int sz);
    virtual ~BDI_MessageEvent() = 0;

    static BDI::Ref<BDI_Event> decode(const uint8 *frame, unsigned int sz);

    const BDDevName &origAddr() const;
    const BDDevName &destAddr() const;
    int messageType() const;
    int textCodec() const;
    const uint16 *textCodes() const;
    int textLength() const;
    bool needReceipt() const;
    time_t ts() const;

    void setOrigAddr(int, int, const char *);
    void setOrigAddr(const BDDevName &);
    void setDestAddr(int, int, const char *);
    void setDestAddr(const BDDevName &);
    void setMessageType(int);
    void setText(int codec, const uint16 *codes, int nchars);
    void setNeedReceipt(bool);
};

// BDI_MessageEvent inline functions

inline const BDDevName &BDI_MessageEvent::origAddr() const
{ return _oa; }

inline const BDDevName &BDI_MessageEvent::destAddr() const
{ return _da; }

inline int BDI_MessageEvent::messageType() const
{ return _messageType; }

inline bool BDI_MessageEvent::needReceipt() const
{ return _needReceipt; }

inline void BDI_MessageEvent::setOrigAddr(int type, int style, const char *number)
{ _oa.setDevName(type, style, number); }

inline void BDI_MessageEvent::setOrigAddr(const BDDevName &name)
{ _oa = name; }

inline void BDI_MessageEvent::setDestAddr(int type, int style, const char *number)
{ _da.setDevName(type, style, number); }

inline void BDI_MessageEvent::setDestAddr(const BDDevName &name)
{ _da = name; }

inline void BDI_MessageEvent::setMessageType(int t)
{ _messageType = t; }

inline void BDI_MessageEvent::setNeedReceipt(bool x)
{ _needReceipt = x; }

//~

// The receipt to invition and message
// doc name: RTDC_CYTInviteSTtoJoin_Receipt
class BDI_ReceiptMessageEvent : public BDI_MessageEvent
{
private:
    uint32 _receiptType;
    uint32 _messageNo;

private:
    // This event cannot acknowledge
    BDI::Ref<BDI_Event> ack() const;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_ReceiptMessageEvent();
    BDI_ReceiptMessageEvent(const uint8 *frame, unsigned int sz);

    std::string toString() const;

    uint32 receiptType() const;
    uint32 messageNo() const;

    void setReceiptType(uint32);
    void setMessageNo(uint32);
};

inline uint32 BDI_ReceiptMessageEvent::receiptType() const
{ return _receiptType; }

inline uint32 BDI_ReceiptMessageEvent::messageNo() const
{ return _messageNo; }

inline void BDI_ReceiptMessageEvent::setReceiptType(uint32 t)
{ _receiptType = t; }

inline void BDI_ReceiptMessageEvent::setMessageNo(uint32 n)
{ _messageNo = n; }

// ~

// A message delivered by service center to CYT
// doc name: RTDC_CommInfo
// mf08b xzhou

class BDI_DeliverMessageEvent : public BDI_MessageEvent
{
public:
    BDI_DeliverMessageEvent();
    BDI_DeliverMessageEvent(const uint8 *frame, unsigned int sz);

    std::string toString() const;

//    BDI::Ref<BDI_Event> ack() const;
};

// ~

// A message submitted to service center
// doc name: RTDC_CommRequest
// mf08b xzhou

class BDI_SubmitMessageEvent : public BDI_MessageEvent
{
private:
    // This event cannot acknowledge
    BDI::Ref<BDI_Event> ack() const;
    int _daCount;

protected:
    unsigned int encodeBody(uint8 **pbody) const;

public:
    BDI_SubmitMessageEvent();

    std::string toString() const;
};

#endif
