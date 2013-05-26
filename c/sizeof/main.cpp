#include <iostream>

using namespace std;

void Foo (char str[100])
{
	cout << "sizeof(str)=" << sizeof(str) << endl;
}

int main(void)
{
	char str[] = "http://www.ibegroup.com/";
	char *p = str;
	int n = 10;


	cout << "sizeof(str)=" << sizeof(str) << endl;

	Foo(str);

	cout << "sizeof(p)=" << sizeof(p) << endl;
	cout << "sizeof(n)=" << sizeof(n) << endl;

	void *p1 = malloc(100);

	cout << "sizeof(p1)=" << sizeof(p1) << endl;


}
