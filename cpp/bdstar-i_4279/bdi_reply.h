#ifndef BDI_REPLY_H
#define BDI_REPLY_H

#include <string>

#include "bdi_event.h"

// Reply event to a query
class BDI_Reply : public BDI_Event
{
private:
    int _errCode;
    BDI_EventRef _sube;

public:
    // Constructs a synthetic error reply
    BDI_Reply(int errCode, BDI_EventRef sube);
    // Constructs a reply from the raw frame data
    BDI_Reply(const uint8 *frame, unsigned int sz);
    virtual ~BDI_Reply();

    std::string toString() const;

    // Creates a BDI_Reply object corresponding to the protocol frame.
    static BDI::Ref<BDI_Event> decode(const uint8 *frame, unsigned int sz);

    int errCode() const;
};

// BDI_Reply inline functions

inline int BDI_Reply::errCode() const
{ return _errCode; }

// ~

// As reply event to track log query
// doc name: QDC_STPositions
class BDI_TrackLogReply : public BDI_Reply
{
private:
    char _devName[21];
    GPosition *_track;
    int _posCount;

public:
    BDI_TrackLogReply(const uint8 *frame, unsigned int sz);
    ~BDI_TrackLogReply();

    std::string toString() const;

    const char *deviceName() const;
    const GPosition *track() const;
    int posCount() const;
};

// BDI_TrackLogReply inline functions

inline const char *BDI_TrackLogReply::deviceName() const
{ return _devName; }

inline const GPosition *BDI_TrackLogReply::track() const
{ return _track; }

inline int BDI_TrackLogReply::posCount() const
{ return _posCount; }

// ~

#endif
