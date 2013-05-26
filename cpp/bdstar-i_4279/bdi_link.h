#ifndef BDI_LINK_H
#define BDI_LINK_H

#include <sys/time.h>

#include "bdi_define.h"
#include "bdi_util.h"

using namespace BDI;

// Abstract communication link
class BDI_Link : public RefBase
{
public:
    enum Status { 
    Disabled, 
    Connecting, 
    Connected, 
    NoConnection 
    };

public:
    virtual ~BDI_Link();

    // Connect to server specified by ipaddr and port, return 
    // the connection identifier.
    // The server address is number-and-dots notation IP address
    // Return vlaue: 
    //    0  success
    //    SYS_SYSTEM_FAULT
    //    SYS_SERVER_NOT_FOUND
    //    SYS_SERVER_NO_ANSWER
    //    SYS_NETWORK_UNREACH
    virtual int connectHost(const char *ipaddr, uint16 port, int *s) = 0;

    // Close connection
    virtual void close(int s) = 0;

    // Read up to sz bytes from the socket into the buffer buf
    // Return value:
    //    0  success
    //   -1  system fault
    //   -2  timeout
    virtual int getBlock(int s, uint8 *buf, unsigned int sz) = 0;

    // Wrtie up to sz bytes to the socket s from the buffer buf
    // Return value:
    //    0  success
    //   -1  system fault
    //   -2  timeout
    virtual int putBlock(int s, uint8 *buf, unsigned int sz) = 0;

    // Return signal level of the link
    // Note that this function must not blocking.
    // REturn value:
    //    0-5 signal level
    //    255 signal not available
    virtual int signalLevel() = 0;

    // Set timeout seconds for the getBlock() and putBlock() functions
    virtual void setTimeOut(int secs) = 0;

    // Wait on a socket until new data to become available or timeout
    // If tv == NULL, wait forever
    // Return true if data available
    virtual bool wait(int s, struct timeval *tv) = 0;

    // Enable or disable data link layer
    virtual void setEnabled(bool) = 0;

    // Return current status of the link
    virtual Status status() = 0;
};

// BDI_Link inline functions

inline BDI_Link::~BDI_Link()
{}

// ~

typedef Ref<BDI_Link> BDI_LinkRef;

#endif
