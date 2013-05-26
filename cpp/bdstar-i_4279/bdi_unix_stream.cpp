#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <assert.h>

#include "bdi_unix_stream.h"

// BDI_UnixStreamLink members

BDI_UnixStreamLink::BDI_UnixStreamLink()
{
    _timeout = 10;
}

BDI_UnixStreamLink::~BDI_UnixStreamLink()
{
}

int BDI_UnixStreamLink::connectHost(const char *host, uint16 port, int *sock)
{
    assert(host != NULL);
    assert(sock != NULL);

    struct addrinfo *infohdr = NULL;
    struct addrinfo *pinfo, *prev;
    int flag = 0;
    int ret = -1;

    *sock = -1;

    // resolve the host
    if (getaddrinfo(host, NULL, NULL, &infohdr) != 0) {
        perror("US|getaddrinfo()");
        return SYS_SERVER_NOT_FOUND;
    }
    pinfo = infohdr;
    prev = NULL;

    while (pinfo != NULL) {
        //ret = -1; // reset error code value

        // Should I ignore the same ip address ?
        if (prev != NULL
            && memcmp(prev->ai_addr, pinfo->ai_addr, pinfo->ai_addrlen) == 0) {
            prev = pinfo;
            pinfo = pinfo->ai_next;
            continue;
        }

        if ((*sock = socket(PF_INET, SOCK_STREAM, 0)) == -1) {
            perror("US|socket()");
            //ret = SYS_SYSTEM_FAULT;
            break;
        }

        struct sockaddr_in da;
        memcpy(&da, pinfo->ai_addr, pinfo->ai_addrlen);
        da.sin_port = htons(port);

#ifndef NDEBUG
        printf("Connecting server \"%s:%d\" [%d.%d.%d.%d]\n", host, port, 
               ((unsigned char*)(&da.sin_addr.s_addr))[0],
               ((unsigned char*)(&da.sin_addr.s_addr))[1],
               ((unsigned char*)(&da.sin_addr.s_addr))[2], 
               ((unsigned char*)(&da.sin_addr.s_addr))[3]);
#endif

        // set the socket to non-blocking
        flag = fcntl(*sock, F_GETFL, NULL);
        fcntl(*sock, F_SETFL, flag | O_NONBLOCK);
        /*
        if ((flag = fcntl(*sock, F_GETFL, NULL)) == -1) {
            perror("US|fcntl()");
            //ret = SYS_SYSTEM_FAULT;
            break;
        }
        if (fcntl(*sock, F_SETFL, flag | O_NONBLOCK) == -1) {
            perror("US|fcntl()");
            //ret = SYS_SYSTEM_FAULT;
            break;
        }
        */

        // connecting
        ::connect(*sock, reinterpret_cast<struct sockaddr *>(&da), sizeof(struct sockaddr_in));

        // waiting for connection completely or timeout
        int health = 15;
        int optval = errno;
        socklen_t optlen = sizeof(optval);
        while (health > 0 && optval == EINPROGRESS) {
            struct timeval timeout;
            timeout.tv_sec = 1;
            timeout.tv_usec = 0;
            fd_set fdSet;
            FD_ZERO(&fdSet);
            FD_SET(*sock, &fdSet);

            switch (select(*sock + 1, NULL, &fdSet, NULL, &timeout)) {
            case 1:
                if (getsockopt(*sock, SOL_SOCKET, SO_ERROR, &optval, &optlen) < 0) {
                    perror("US|getsockopt()");
                    //ret = SYS_SYSTEM_FAULT;
                    health = -1;
                }
                break;
            case 0:
                --health;
                break;
            default:
                if (errno != EINTR) {
                    perror("US|select()");
                    //ret = SYS_SYSTEM_FAULT;
                    health = -1;
                }
                break;
            }
        }

        if (health < 0)
            ret = SYS_SYSTEM_FAULT;
        else if (health == 0)
            ret = SYS_SERVER_NO_ANSWER;
        else 
            switch (optval) {
            case 0:
                ret = 0; // connected!
                break;
            case ECONNREFUSED:
            case EINPROGRESS:
            case ETIMEDOUT:
                ret = SYS_SERVER_NO_ANSWER;
                break;
            case ENETUNREACH:
                ret = SYS_NETWORK_UNREACH;
                break;
            default:
                ret = SYS_SYSTEM_FAULT;
                break;
            }

        if (ret == 0)
            break; // Connected, stopping try.

        // try next
        ::close(*sock);
        *sock = -1;

        prev = pinfo;
        pinfo = pinfo->ai_next;
    }

    freeaddrinfo(infohdr);

    if (ret == 0) {
        // Set the socket back to blocking
        fcntl(*sock, F_SETFL, flag & ~O_NONBLOCK);
        /*
        if (fcntl(*sock, F_SETFL, flag & ~O_NONBLOCK) == -1) {
            perror("US|fcntl()");
            ret = SYS_SYSTEM_FAULT;
        }
        */
    }
    else if (*sock != -1) {
        ::close(*sock);
        *sock = -1;
    }

    return ret;
}

void BDI_UnixStreamLink::close(int sock)
{
    ::close(sock);
}

int BDI_UnixStreamLink::getBlock(int sock, uint8 *buf, unsigned int sz)
{
    struct timeval oneSec;
    int ntries = 0;
    ssize_t bytesRead = 0;
    ssize_t br;

    while (bytesRead < static_cast<ssize_t>(sz) && ntries < _timeout) {
        oneSec.tv_sec = 1;
        oneSec.tv_usec = 0;
        fd_set fds;
        FD_ZERO(&fds);
        FD_SET(sock, &fds);

        switch (select(sock + 1, &fds, NULL, NULL, &oneSec)) {
        case 1:
            br = read(sock, buf + bytesRead, sz - bytesRead);
            if (br <= 0) {
                fprintf(stderr, "US::getBlock|read()=%d: %s\n", br, strerror(errno));
                return -1;
            }
            bytesRead += br;
            break;
        case 0:
            ++ntries;
            break;
        default:
            if (errno != EINTR) {
                perror("US::getBlock|select()");
                return -1;
            }
            break;
        }
    }

    if (ntries >= _timeout) {
        fprintf(stderr, "Timeout when reading from server\n");
        return -2;
    }
    else
        return 0;
}

int BDI_UnixStreamLink::putBlock(int sock, uint8 *buf, unsigned int sz)
{
    struct timeval oneSec;
    int ntries = 0;
    ssize_t bytesWritten = 0;
    ssize_t bw;

    while (bytesWritten < static_cast<ssize_t>(sz) && ntries < _timeout) {
        oneSec.tv_sec = 1;
        oneSec.tv_usec = 0;
        fd_set fds;
        FD_ZERO(&fds);
        FD_SET(sock, &fds);

        switch (select(sock + 1, NULL, &fds, NULL, &oneSec)) {
        case 1:
            bw = write(sock, buf + bytesWritten, sz - bytesWritten);
            if (bw < 0) {
                perror("US::putBlock|send()");
                return -1;
            }
            bytesWritten += bw;
            break;
        case 0:
            ++ntries;
            break;
        default:
            if (errno != EINTR) {
                perror("US::putBlock|select()");
                return -1;
            }
            break;
        }
    }

    if (ntries >= _timeout) {
        fprintf(stderr, "Timeout when writing to server\n");
        return -2;
    }
    else
        return 0;
}

void BDI_UnixStreamLink::setTimeOut(int secs)
{
    _timeout = secs;
}

bool BDI_UnixStreamLink::wait(int sock, struct timeval *tv)
{
    fd_set fdSet;
    FD_ZERO(&fdSet);
    FD_SET(sock, &fdSet);
    return select(sock + 1, &fdSet, NULL, NULL, tv) != 0;
}

void BDI_UnixStreamLink::setEnabled(bool)
{
}

BDI_Link::Status BDI_UnixStreamLink::status()
{
    return BDI_Link::Connected;
}

int BDI_UnixStreamLink::signalLevel()
{
    return 255;
}
