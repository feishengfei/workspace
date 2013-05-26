#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "debug_alloc.h"

#define FROM_MALLOC	1
#define FROM_STRDUP	2
#define FROM_NEW	3
#define FROM_NEW_ARR	4
#define FROM_NEW_OBJARR	5

#define BE_REALLOCATED	0x10

class SrcModule;

class BlkEntry
{
public:
    int _flags;
    void *_addr;
    size_t _size;
    int _lno;
    BlkEntry *_next;
    SrcModule *_parent;

public:
    BlkEntry(SrcModule *parent);

    int from() const;
    bool isReallocated() const;
    const char *toString(bool format) const;
};

class SrcModule
{
public:
    char *_src;
    BlkEntry *_l;
    int _childCount;
    SrcModule *_sibling;

public:
    SrcModule(const char *src);
    ~SrcModule();

    void addEntry(int f, void *p, size_t sz, int lno);
    BlkEntry *findEntry(void *p);
    BlkEntry *popEntry(void *p);

    const char *srcName() const;
    int allocCount() const;
};

// BlkEntry members

BlkEntry::BlkEntry(SrcModule *parent)
{
    _flags = 0;
    _addr = NULL;
    _size = 0;
    _lno = 0;
    _next = NULL;
    _parent = parent;
}

inline int BlkEntry::from() const
{ return _flags & 0xf; }

inline bool BlkEntry::isReallocated() const
{ return _flags & 0xf0; }

const char *BlkEntry::toString(bool alignment) const
{
    static char buf[256];
    static char *fromstr[] = { "MALLOC", "STRDUP", "NEW", "NEW_ARR", "NEW_OBJARR" };
    const char *fmt = alignment ? "%-20s%6d  %-10p%8u  %s%s" : "%s@%d %p %u %s%s";
    sprintf(buf, fmt, 
            _parent->srcName(), _lno, _addr, _size, 
            isReallocated() ? "*" : "", 
            fromstr[from() - 1]);
    return buf;
}

// ~

// SrcModule members

SrcModule::SrcModule(const char *src)
{
    _src = strdup(src);
    _l = NULL;
    _childCount = 0;
    _sibling = NULL;
}

SrcModule::~SrcModule()
{
    if (_src != NULL)
        free(_src);

    while (_l != NULL) {
        BlkEntry *pent = _l;
        _l = pent->_next;
        delete pent;
    }
}

void SrcModule::addEntry(int from, void *p, size_t sz, int lno)
{
    BlkEntry *ent = new BlkEntry(this);
    if (ent == NULL) {
        fprintf(stderr, "Lack of Memory\n");
        exit(1);
    }
    ent->_flags = from;
    ent->_addr = p;
    ent->_size = sz;
    ent->_lno = lno;
    ent->_next = _l;
    _l = ent;
    ++_childCount;
}

BlkEntry *SrcModule::findEntry(void *p)
{
    BlkEntry *pent = _l;
    int i = 0;
    while (pent != NULL && pent->_addr != p && i < _childCount) {
        pent = pent->_next;
        ++i;
    }
    if (pent != NULL && pent->_addr != p) {
        fprintf(stderr, "\"%s\" %p %p %d\n", _src, pent, p, _childCount);
        assert(0);
    }
    return pent;
}

BlkEntry *SrcModule::popEntry(void *p)
{
    BlkEntry *pent = _l;
    BlkEntry **pprev = &_l;
    int i = 0;
    while (pent != NULL && pent->_addr != p && i < _childCount) {
        pprev = &(pent->_next);
        pent = pent->_next;
        ++i;
    }
    assert(pent == NULL || pent->_addr == p);

    if (pent != NULL) {
        *pprev = pent->_next;
        --_childCount;
    }

    return pent;
}

inline const char *SrcModule::srcName() const
{
    static const char *EmptyName = "";
    return _src != NULL ? _src : EmptyName;
}

inline int SrcModule::allocCount() const
{ return _childCount; }

// ~

static SrcModule *g_modq = NULL;

#ifdef _REENTRANT
#include <pthread.h>
pthread_mutex_t g_mutex = PTHREAD_MUTEX_INITIALIZER;
#endif

static SrcModule *setModule(const char *src)
{
    SrcModule *pmod = g_modq;
    while (pmod != NULL && strcmp(pmod->srcName(), src) != 0)
        pmod = pmod->_sibling;

    if (pmod == NULL) {
        pmod = new SrcModule(src);
        if (pmod == NULL) {
            fprintf(stderr, "Lack of Memory\n");
            exit(1);
        }
        pmod->_sibling = g_modq;
        g_modq = pmod;
    }

    return pmod;
}

static BlkEntry *findPtr(void *ptr)
{
    BlkEntry *res = NULL;
    SrcModule *pmod = g_modq;
    while (pmod != NULL && (res = pmod->findEntry(ptr)) == NULL)
        pmod = pmod->_sibling;
    return res;
}

// ~

void *dbg_calloc(size_t nmemb, size_t size, const char *src, int lno)
{
    void *ptr = calloc(nmemb, size);
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *m = setModule(src);
    m->addEntry(FROM_MALLOC, ptr, size, lno);
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
    return ptr;
}

void *dbg_malloc(size_t size, const char *src, int lno)
{
    void *ptr = malloc(size);
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *m = setModule(src);
    m->addEntry(FROM_MALLOC, ptr, size, lno);
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
    return ptr;
}

void *dbg_realloc(void *ptr, size_t size, const char *src, int lno)
{
    void *res = NULL;

    if (ptr == NULL)
        return dbg_malloc(size, src, lno);

#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    BlkEntry *ent = findPtr(ptr);
    if (ent == NULL) {
        fprintf(stderr, "Unmatched realloc: %s@%d %p\n", src, lno, ptr);
        exit(1);
    }
    if (ent->from() != FROM_MALLOC) {
        fprintf(stderr, "Misused realloc: %s@%d [%s]\n", src, lno, ent->toString(false));
        exit(1);
    }

    if (size == 0) {
        ent = ent->_parent->popEntry(ptr);
        assert(ent != NULL);
        delete ent;
        free(ptr);
        res = NULL;
    }
    else {
        ent->_addr = realloc(ptr, size);
        ent->_size = size;
        ent->_flags |= BE_REALLOCATED;
        res = ent->_addr;
    }
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif

    return res;
}

char *dbg_strdup(const char *s, const char *src, int lno)
{
    char *ptr = strdup(s);
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *m = setModule(src);
    m->addEntry(FROM_STRDUP, ptr, strlen(s) + 1, lno);
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
    return ptr;
}

void dbg_free(void *ptr, const char *src, int lno)
{
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    BlkEntry *ent = findPtr(ptr);
    if (ent == NULL) {
        fprintf(stderr, "Unmatched free: %s@%d %p\n", src, lno, ptr);
        exit(1);
    }
    if (ent->from() != FROM_MALLOC && ent->from() != FROM_STRDUP) {
        fprintf(stderr, "Misused free: %s@%d [%s]\n", src, lno, ent->toString(false));
        exit(1);
    }
    ent = ent->_parent->popEntry(ptr);
    assert(ent != NULL);
    delete ent;
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif

    // do the real free
    free(ptr);
}

#ifdef __cplusplus
void *operator new(size_t size, const char *src, int lno, bool)
{
    void *ptr = malloc(size);
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *m = setModule(src);
    m->addEntry(FROM_NEW, ptr, size, lno);
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
    return ptr;
}

void *operator new[](size_t size, const char *src, int lno, bool oarr)
{
    void *res = malloc(size);
    void *ptr = oarr ? reinterpret_cast<void *>(reinterpret_cast<unsigned long>(res) + 4) : res;
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *mod = setModule(src);
    mod->addEntry(oarr ? FROM_NEW_OBJARR : FROM_NEW_ARR, ptr, size, lno);
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
    return res;
}

void operator delete(void *ptr)
{
    // do the real delete
    free(ptr);
}

void operator delete[](void *ptr)
{
    // do the real delete
    free(ptr);
}

void removeEntry(void *ptr, const char *src, int lno, int flag)
{
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    BlkEntry *ent = findPtr(ptr);
    if (ent == NULL) {
        fprintf(stderr, "Unmatched delete: %s@%d %p\n", src, lno, ptr);
        exit(1);
    }

    bool misused = false;
    switch (flag) {
        case 0:
            misused = ent->from() != FROM_NEW;
            break;
        case 1:
            misused = ent->from() != FROM_NEW_ARR;
            break;
        case 2:
            misused = ent->from() != FROM_NEW_OBJARR;
            break;
        default:
            break;
    }
    if (misused) {
        fprintf(stderr, "Misused delete: %s@%d [%s]\n", src, lno, ent->toString(false));
        exit(1);
    }

    ent = ent->_parent->popEntry(ptr);
    assert(ent != NULL);
    delete ent;
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
}

#endif /* __cplusplus */

void assureNoLeaks(const char *src)
{
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *mod = setModule(src);
    if (mod->allocCount() > 0) {
        fprintf(stderr, "Memory leak: %d alloc(s) unrelease.\n", mod->allocCount());
        exit(1);
    }
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
}

void printAllocStats()
{
    printf("----------------------------------------------------\n");
    printf("%-20s%6s  %-10s%8s  %s\n", "Source", "Line", "Ptr", "Size", "From");
#ifdef _REENTRANT
    pthread_mutex_lock(&g_mutex);
#endif
    SrcModule *pmod = g_modq;
    for (; pmod != NULL; pmod = pmod->_sibling) {
        BlkEntry *pent = pmod->_l;
        for (; pent != NULL; pent = pent->_next)
            printf("%s\n", pent->toString(true));
        if (pmod->allocCount() > 0)
            printf("   Total: %d\n", pmod->allocCount());
    }
#ifdef _REENTRANT
    pthread_mutex_unlock(&g_mutex);
#endif
}
