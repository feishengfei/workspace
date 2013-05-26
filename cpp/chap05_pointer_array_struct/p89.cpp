#include <iostream>
using namespace std;

void f(void)
{
	const double& dr = 1;
	//double& dr = 1;	//XXX
}

void increment(int& aa)
{
	aa++;
}

int next(int p)
{
	return p+1;
}

void incr(int *p)
{
	(*p)++;
}

void g(void)
{
	int x = 1;
	cout << "x = " << x << endl;

	increment(x);
	cout << "x = " << x << endl;

	x = next(x);
	cout << "x = " << x << endl;

	incr(&x);
	cout << "x = " << x << endl;

}

int main(void)
{
	g();
}
