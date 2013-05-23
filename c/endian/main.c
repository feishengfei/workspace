#include <stdio.h>
#include <arpa/inet.h>

int main(void)
{
	unsigned int x = 100;
	unsigned int xx = htonl(x);
	unsigned int xxx = ntohl(x);
	printf("x = %d, xx = %d, xxx = %d \r\n", x, xx, xxx);
	printf("x = %d, xx = %d, xxx = %d \r\n", x, htonl(x), htonl(htonl(x)));
}
