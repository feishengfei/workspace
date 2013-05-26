#include <iostream>
using namespace std;

void f(void)
{
	int i = 1;
	int& r = i;
	int x = r;

	cout << "i = " << i << endl;
	cout << "r = " << r << endl;
	cout << "x = " << x << endl;
}

void g(void)
{
	int ii = 0;
	int& rr = ii;

	cout << "ii = " << ii << endl;
	cout << "rr = " << rr << endl;

	rr++;
	int* pp = &rr;

	cout << "ii = " << ii << endl;
	cout << "rr = " << rr << endl;
	cout << "pp = " << *pp << endl;
}

int main(void)
{
	f();
	cout << endl;
	g();
}
