#include <stdio.h>
#include <sys/time.h>
#include <assert.h>

#include "debug_alloc.h"
#include "bdi_rtdata.h"
#include "bdi_event_list.h"

// BDI_EventListItem members

BDI_EventListItem::BDI_EventListItem(BDI_EventListItem **after)
{
    _next = *after;
    *after = this;
}

// BDI_EventList members

BDI_EventList::BDI_EventList()
{
    _p = NULL;
    _nitems = 0;
    pthread_mutex_init(&_mutex, NULL);
    pthread_cond_init(&_cond, NULL);
}

BDI_EventList::~BDI_EventList()
{
    if (_nitems != 0)
        fprintf(stderr, "Event list not empty! (%d left)\n", _nitems);

    while (_p != NULL) {
        BDI_EventListItem *pi = _p;
        _p = _p->_next;
        pi->_eref.unref();
        DELETE(pi);
    }

    pthread_mutex_destroy(&_mutex);
    pthread_cond_destroy(&_cond);
}

/*
int BDI_EventList::count() const
{
    int res;
    BDI_EventList *self = const_cast<BDI_EventList *>(this);
    pthread_mutex_lock(&self->_mutex);
    res = _nitems;
    pthread_mutex_unlock(&self->_mutex);
    return res;
}
*/

bool BDI_EventList::addEvent(BDI_EventRef e, void *ptr)
{
    if (e.isNull() || _nitems >= MAX_ITEMS)
        return false;

    pthread_mutex_lock(&_mutex);

    BDI_EventListItem **ppi = &_p;
    BDI_EventListItem *item = NULL;
    while (*ppi != NULL && (*ppi)->_next != NULL)
        ppi = &((*ppi)->_next);

    item = NEW BDI_EventListItem(ppi);
    if (item != NULL) {
        item->_processor = static_cast<pthread_t>(0);
        item->_bornTime = time(NULL);
        item->_eref = e;
        item->_ptr = ptr;
        ++_nitems;
    }

    pthread_cond_broadcast(&_cond);
    pthread_mutex_unlock(&_mutex);
    return item != NULL;
}

const BDI_EventListItem *BDI_EventList::getFreeEvent(pthread_t tid, long time)
{
    const BDI_EventListItem *res = NULL;
    struct timeval now;
    gettimeofday(&now, NULL);

    // Waiting for a event coming
    pthread_cleanup_push(reinterpret_cast<void (*)(void *)>(pthread_mutex_unlock), 
                         reinterpret_cast<void *>(&_mutex));
    pthread_mutex_lock(&_mutex);
    while (_nitems == 0) {
        if (time < 0)
            pthread_cond_wait(&_cond, &_mutex);
		else {
            struct timespec tsp;
            long secs = time / 1000;
            long nsecs = time % 1000 * 1000000;
            tsp.tv_sec = now.tv_sec + secs;
            if (1000000000L - now.tv_usec * 1000 <= nsecs) {
                ++tsp.tv_sec;
                tsp.tv_nsec = nsecs + now.tv_usec * 1000 - 1000000000L;
            }
            else
                tsp.tv_nsec = nsecs + now.tv_usec * 1000;
            pthread_cond_timedwait(&_cond, &_mutex, &tsp);
            break;
        }
    }

    BDI_EventListItem *pi = _p;
    while (pi != NULL && !pthread_equal(pi->_processor, static_cast<pthread_t>(0)))
        pi = pi->_next;
    if (pi != NULL) {
        pi->_processor = tid;
        res = pi;
    }

    pthread_cleanup_pop(1);
    return res;
}

void BDI_EventList::removeEvent(pthread_t tid, const BDI_EventListItem **i)
{
    assert(i != NULL);

    pthread_mutex_lock(&_mutex);
    BDI_EventListItem **ppi = &_p;
    while (*ppi != NULL && !pthread_equal((*ppi)->_processor, tid))
        ppi = &((*ppi)->_next);
    assert(*ppi != NULL);
    if (*ppi != NULL) {
        BDI_EventListItem *myItem = *ppi;
        assert(myItem == *i);
        *ppi = myItem->_next;
        myItem->_eref.unref();
        DELETE(myItem);
        --_nitems;
        *i = NULL;
    }
    pthread_mutex_unlock(&_mutex);
}

void BDI_EventList::resetEvents()
{
    //pthread_mutex_lock(&_mutex);
    BDI_EventListItem *pi = _p;
    while (pi != NULL) {
        pi->_processor = static_cast<pthread_t>(0);
        pi = pi->_next;
    }
    //pthread_mutex_unlock(&_mutex);
}
