#ifndef BDI_UNIX_STREAM_H
#define BDI_UNIX_STREAM_H

#include "bdi_link.h"

// Unix stream socket interface
class BDI_UnixStreamLink : public BDI_Link
{
private:
    int _timeout; // timeout seconds for getBlock/putBlock

public:
    BDI_UnixStreamLink();
    virtual ~BDI_UnixStreamLink();

    // inherits BDI_Link
    int connectHost(const char *ipaddr, uint16 port, int *sock);
    void close(int);
    int getBlock(int, uint8 *buf, unsigned int sz);
    int putBlock(int, uint8 *buf, unsigned int sz);
    void setTimeOut(int secs);
    bool wait(int, struct timeval *tv);
    void setEnabled(bool);
    BDI_Link::Status status();
    int signalLevel();
};

#endif
