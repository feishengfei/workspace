#ifndef BDI_GPS_EVENT_H
#define BDI_GPS_EVENT_H

#include <string>

#include "bdi_event.h"

class BDI_GpsPosEvent : public BDI_Event
{
private:
    GPosition _gpos;
    int _precision; // precision factor
    int _error; // estimated error

    bool _fixed;
    bool _marked;

public:
    // Constructs a unfix event
    BDI_GpsPosEvent();
    ~BDI_GpsPosEvent();

    std::string toString() const;

    const GPosition &gpos() const;
    int precision() const;
    int error() const;
    bool fixed() const;
    bool marked() const;

    void setPosition(double, double, float, int, int, int, uint32);
    void setPrecision(int);
    void setError(int);
    void setFixed(bool);
    void mark();
};

// BDI_GpsPosEvent inline functions

inline const GPosition &BDI_GpsPosEvent::gpos() const
{ return _gpos; }

inline int BDI_GpsPosEvent::precision() const
{ return _precision; }

inline int BDI_GpsPosEvent::error() const
{ return _error; }

inline bool BDI_GpsPosEvent::fixed() const
{ return _fixed; }

inline bool BDI_GpsPosEvent::marked() const
{ return _marked; }

inline void BDI_GpsPosEvent::setPrecision(int v)
{ _precision = v; }

inline void BDI_GpsPosEvent::setError(int err)
{ _error = err; }

inline void BDI_GpsPosEvent::setFixed(bool f)
{ _fixed = f; }

inline void BDI_GpsPosEvent::mark()
{ _marked = true; }

// ~

class BDI_GpsViewEvent : public BDI_Event
{
public:
    struct SateItem
    {
        int sa_id;
        int sa_elevation;
        int sa_azimuth;
        int sa_snr;
    };

private:
    struct SateItemNode
    {
        SateItem sa;
        SateItemNode *next;
    };
    SateItemNode *_p;
    int _nsates;

public:
    BDI_GpsViewEvent();
    ~BDI_GpsViewEvent();

    std::string toString() const;

    int sateCount() const;

    class Iterator;
    friend class Iterator;
    class Iterator
    {
    private:
        SateItemNode *_p;

    private:
        // Constructor for BDI_GpsViewEvent
        Iterator(SateItemNode *);

    public:
        Iterator &operator++();
        const BDI_GpsViewEvent::SateItem *operator->() const;
        const BDI_GpsViewEvent::SateItem &operator*() const;
        bool operator==(const Iterator &) const;
        bool operator!=(const Iterator &) const;

        friend class BDI_GpsViewEvent;
    };

    Iterator begin() const;
    Iterator end() const;

    void addSateItem(int, int, int, int);
    void addSateItem(const SateItem &);
};

// BDI_GpsViewEvent::Iterator inline functions

inline int BDI_GpsViewEvent::sateCount() const
{ return _nsates; }

inline BDI_GpsViewEvent::Iterator::Iterator(BDI_GpsViewEvent::SateItemNode *p)
{ _p = p; }

inline BDI_GpsViewEvent::Iterator &BDI_GpsViewEvent::Iterator::operator++()
{ _p = _p->next; return *this; }

inline const BDI_GpsViewEvent::SateItem *BDI_GpsViewEvent::Iterator::operator->() const
{ return &(_p->sa); }

inline const BDI_GpsViewEvent::SateItem &BDI_GpsViewEvent::Iterator::operator*() const
{ return _p->sa; }

inline bool BDI_GpsViewEvent::Iterator::operator==(const Iterator &it) const
{ return _p == it._p; }

inline bool BDI_GpsViewEvent::Iterator::operator!=(const Iterator &it) const
{ return _p != it._p; }

// BDI_GpsViewEvent inline functions

inline BDI_GpsViewEvent::Iterator BDI_GpsViewEvent::begin() const
{ return BDI_GpsViewEvent::Iterator(_p); }

inline BDI_GpsViewEvent::Iterator BDI_GpsViewEvent::end() const
{ return BDI_GpsViewEvent::Iterator(NULL); }

#endif
