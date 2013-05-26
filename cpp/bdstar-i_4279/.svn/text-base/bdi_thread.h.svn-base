#ifndef BDI_THREAD_H
#define BDI_THREAD_H

#include <pthread.h>

class BDI_Daemon;

class BDI_Thread
{
public:
    static const int RT_PEER = 1;
    static const int RTEX_PEER = 2;
    static const int RR_PEER = 3;

protected:
    pthread_t _tid;
    BDI_Daemon *_d;
    //int _sockfd;

    // Thread flags, bool type actually.
    volatile int _running;
    volatile int _finished;
    volatile int _busy;

protected:
    virtual void run() = 0;
    virtual void onStopped();
    
public:
    BDI_Thread(BDI_Daemon *d);
    virtual ~BDI_Thread();

    void start();
    void terminate();
    void wait();

    bool running() const;
    bool finished() const;
    bool busy() const;

    pthread_t thread() const;

    friend void *bdi_thread_func(void *);
};

// BDI_Thread inline functions

inline pthread_t BDI_Thread::thread() const
{ return _tid; }

inline bool BDI_Thread::running() const
{ return _running; }

inline bool BDI_Thread::finished() const
{ return _finished; }

inline bool BDI_Thread::busy() const
{ return _busy; }

// ~

class BDI_RTDataThread : public BDI_Thread
{
private:
    int _sockfd;

protected:
    void run();
    void onStopped();

public:
    BDI_RTDataThread(BDI_Daemon *d);
};

// ~

class BDI_SubmitThread : public BDI_Thread
{
private:
    int _sockfd;

protected:
    void run();
    void onStopped();

public:
    BDI_SubmitThread(BDI_Daemon *d);
};

#endif
