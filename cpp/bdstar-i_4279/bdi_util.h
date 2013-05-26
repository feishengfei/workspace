#ifndef BDI_UTIL_H
#define BDI_UTIL_H

#include <time.h>

#include <string>

#include "debug_alloc.h"
#include "bdi_define.h"

namespace BDI
{
	void dump(void *buf, int len);
    std::string timevToStr(time_t tv);

    //void setSystemTime(time_t timeRef);

    uint32 getNextSerialNo();
    uint32 getNextPacketNo();

    void cleanPhoneNumber(const char *numToClean, char *outbuf, int buflen);

    // general-purpose pointer wrapper with reference counting
    class RefBase
    {
    private:
        int _refCount;

    public:
        RefBase();

        int ref();
        int unref();
        int refCount() const;
    };

    inline RefBase::RefBase()
        : _refCount(0) {}

    inline int RefBase::ref()
    { return _refCount++; }

    inline int RefBase::unref()
    { return --_refCount; }

    inline int RefBase::refCount() const
    { return _refCount; }

    template <class T>
    class Ref
    {
    private:
        T *_rep;

    public:
        Ref();
        Ref(T *p);
        Ref(const Ref &r);
        Ref &operator=(const Ref &r);
        ~Ref();

        T *operator->() const;
        T &operator()();
        bool operator==(const Ref &r) const;

        T *getPtr();
        bool isNull() const;

        void unref();
    };

    template <class T>
    inline Ref<T>::Ref()
        : _rep((T*)NULL) {}

    template <class T>
    inline Ref<T>::Ref(T *p)
        : _rep(p)
    { if (p != (T*)NULL) p->ref(); }

    template <class T>
    inline Ref<T>::Ref(const Ref<T> &r) 
        : _rep(r._rep)
    { if (_rep != (T*)NULL) _rep->ref(); }

    template <class T>
    inline Ref<T> &Ref<T>::operator=(const Ref<T> &r)
    {
        if (r._rep != (T*)NULL) 
            r._rep->ref();
        if (_rep != (T*)NULL && _rep->unref() == 0) 
            DELETE(_rep);
        _rep = r._rep;
        return *this;
    }

    template <class T>
    inline Ref<T>::~Ref()
    { unref(); }
    /*
    {
        if (_rep != (T*)NULL && _rep->unref() == 0) 
            DELETE(_rep);
    }
    */

    template <class T>
    inline T *Ref<T>::operator->() const
    { return _rep; }

    template <class T>
    inline T &Ref<T>::operator()()
    { return *_rep; }

    template <class T>
    inline bool Ref<T>::operator==(const Ref<T> &r) const
    { return _rep == r._rep; }

    template <class T>
        inline T *Ref<T>::getPtr() 
        { return _rep; }

    template <class T>
    inline bool Ref<T>::isNull() const 
    { return _rep == (T*)NULL; }

    template <class T>
    inline void Ref<T>::unref() 
    {
        if (_rep != (T*)NULL && _rep->unref() == 0)
            DELETE(_rep);
        _rep = NULL;
    }

    // ~
};

#endif
