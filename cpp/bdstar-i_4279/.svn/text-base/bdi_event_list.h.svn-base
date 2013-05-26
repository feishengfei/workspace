#ifndef BDI_EVENT_LIST_H
#define BDI_EVENT_LIST_H

#include <time.h>
#include <pthread.h>

#include "bdi_event.h"
#include "bdi_util.h"

class BDI_EventListItem
{
private:
    pthread_t _processor;
    time_t _bornTime;
    BDI_EventRef _eref;
    void *_ptr;
    BDI_EventListItem *_next;

private:
    // Constructor for BDI_EventList
    BDI_EventListItem(BDI_EventListItem **after);

public:
    pthread_t processor() const;
    time_t bornTime() const;
    BDI_EventRef event() const;
    void *data() const;

    friend class BDI_EventList;
};

// BDI_EventListItem inline functions

inline pthread_t BDI_EventListItem::processor() const
{ return _processor; }

inline time_t BDI_EventListItem::bornTime() const
{ return _bornTime; }

inline BDI_EventRef BDI_EventListItem::event() const
{ return _eref; }

inline void *BDI_EventListItem::data() const
{ return _ptr; }

// ~

class BDI_EventList : public BDI::RefBase
{
public:
    static const int MAX_ITEMS = 100;

private:
    BDI_EventListItem *_p;
    volatile int _nitems;
    pthread_mutex_t _mutex;
    pthread_cond_t _cond;

public:
    BDI_EventList();
    ~BDI_EventList();

    int count() const;

    // Add a event to the list, and then wakeup the blocking threads.
    // On success true is returned, or returns false if cann't alloc memory.
    bool addEvent(BDI_EventRef e, void *ptr = NULL);

    // Get a foremost and unhandled event.
    // The timeout time is given in millisecond, -1 means wait forever.
    // The returned value may be null because of timedout.
    const BDI_EventListItem *getFreeEvent(pthread_t tid, long time = -1);

    void removeEvent(pthread_t tid, const BDI_EventListItem **);

    void resetEvents();
};

// BDI_EventList inline functions

inline int BDI_EventList::count() const
{ return _nitems; }

// ~

typedef BDI::Ref<BDI_EventList> BDI_EventListRef;

#endif
