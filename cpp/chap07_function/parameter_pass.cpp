#include <iostream>
using namespace std;

void f(int a, int &b)
{
	a++;
	b++;
	cout << "a = " << a << ", b = " << b << endl;
}

void g(int a, const int &b)
{
	a++;
//	b++;	//XXX b is read only
	cout << "a = " << a << ", b = " << b << endl;
}

int main(void)
{
	int i = 1;	
	int j = 1;	
	cout << "i = " << i << ", j = " << j << endl;
	f(i, j);	
	cout << "i = " << i << ", j = " << j << endl;
}
