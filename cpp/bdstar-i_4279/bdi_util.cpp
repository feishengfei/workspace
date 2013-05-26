#include <strstream>

#include <stdlib.h>
#include <pthread.h>

#include "bdi_util.h"

using namespace std;
using namespace BDI;

static pthread_mutex_t g_mutex = PTHREAD_MUTEX_INITIALIZER;

static uint32 g_curSerialNo = 0;
static uint32 g_curPacketNo = 0;

void BDI::dump(void *buf, int len)
{
	unsigned char *p = (unsigned char *)buf;
	int i;

	for (i = 0; i < len; i++) {
		if (i % 16 == 0) printf("%04x:", i);
		if (i % 16 == 8) printf(" -");
		printf(" %02x", p[i]);
		if (i % 16 == 15) printf("\n");
	}
	if (i % 16) printf("\n");
}

string BDI::timevToStr(time_t tv)
{
    if (tv == 0)
        return "-/-";

    ostrstream os;
    struct tm tmbuf;
    struct tm *tm = localtime_r(&tv, &tmbuf);
    os << tm->tm_mon + 1 << '/' << tm->tm_mday << '/' << tm->tm_year - 100 << ' '
       << tm->tm_hour << ':' <<  tm->tm_min << ':' << tm->tm_sec
       << ends;
    char *ss = os.str();
    string res(ss);
    delete[] ss;
    return res;
}

/*
void BDI::setSystemTime(time_t timeRef)
{
    if (pthread_mutex_trylock(&g_mutex) != 0)
        return;
    time_t tv = time(NULL);
    if (labs(timeRef - tv) > 5)
        stime(&timeRef);
    pthread_mutex_unlock(&g_mutex);
}
*/

uint32 BDI::getNextSerialNo()
{
    uint32 ret;
    pthread_mutex_lock(&g_mutex);
    ret = g_curSerialNo++;
    pthread_mutex_unlock(&g_mutex);
    return ret;
}

uint32 BDI::getNextPacketNo()
{
    uint32 ret;
    pthread_mutex_lock(&g_mutex);
    ret = g_curPacketNo++;
    pthread_mutex_unlock(&g_mutex);
    return ret;
}

void BDI::cleanPhoneNumber(const char *numToClean, char *outbuf, int buflen)
{
    const char *psrc = numToClean;
    char *pdst = outbuf;
    int i = 0;

    while (*psrc != '\0' && i < buflen - 1) {
        if (isdigit(*psrc) || *psrc == '+')
            *pdst++ = *psrc;
        ++psrc;
    }
    *pdst = '\0';
}
