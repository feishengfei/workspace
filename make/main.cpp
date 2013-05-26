#include "proxy.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[])
{
	Subject* sub = new ConcreteSubject();
	Proxy* p = new Proxy(sub);
	p->request();

	return 0;
}
