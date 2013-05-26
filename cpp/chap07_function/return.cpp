#include <iostream>
using namespace std;

void g(int a)
{
	return;
}

void f(int b)
{
	return g(b);
}

int main(void)
{
	int i = 0;
	f(i);
		
	return 0;
}
