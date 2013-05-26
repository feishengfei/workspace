#include <string>
#include <iostream>
using namespace std;

void foo(int i)
{
	cout << "foo :" << i << endl;
}


void f1(string)
{
	cout << "void f1(string)" << endl;
}

int f2(string)
{
	return 0;
}

void f3(int*)
{

}

void g()
{
	void (*pf)(string);
	pf = &f1;	
}


int main(void)
{
	void (*func)(int) = &foo;
	func(100);
//	*func(100);	XXX
	(*func)(100);
}
