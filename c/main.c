#include <stdio.h>
#include <string.h>
#include <time.h>

#define COOKIE_KEY "kuki"
#define COOKIE_KEY_LEN ((int)strlen(COOKIE_KEY)+1)

int main(int argc, char * argv[])
{
	if (argc < 1)
		return -1;

//	printf("arg: %s\r\n", argv[1]);
	time_t t = atoi(argv[1]);	
	struct tm *lt = localtime(&t);
	printf("%04d-%02d-%02d %02d:%02d:%02d"
		,lt->tm_year + 1900
		,lt->tm_mon  + 1
		,lt->tm_mday 

		,lt->tm_hour        
		,lt->tm_min         
		,lt->tm_sec     
	);

	
	return 0;
}
