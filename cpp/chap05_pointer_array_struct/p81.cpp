#include <vector>
#include <iostream>

using namespace std;

void foo()
{
	char *p = "Tomato";
	p[4] = 'e';	//XXX
}

int main(void)
{
	foo();
	return 0;
}
