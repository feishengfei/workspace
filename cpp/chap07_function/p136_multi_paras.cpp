#include <iostream>
#include <stdarg.h>
//#include <cstdarg>

using namespace std;

void pow(double, double)
{
}

void pow(int, int)
{
}

void error(int severity...)
{
	va_list ap;
	va_start(ap, severity);		//arg开始
	for(;;){
		char *p = va_arg(ap, char*);
		if (p == NULL)break;
		cerr << p << ' '<< endl;
	}
	va_end(ap);
	
	cerr << endl;
	if (severity){
		exit(severity);			//arg清理
	}
}

int main(void)
{
//	pow(2.0, 2);	//XXX
	pow(2, 2);
	pow(2., 2.);

	error(1, "tomorrow", "yesterday", "now");
}
