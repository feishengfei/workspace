#include <iostream>
using namespace std;

inline int fac(int n)
{
	return (n<2) ? 1 : n * fac(n-1);
}

int main(void)
{
	cout << "fact(6) = " << fac(6) << endl;
	return 0;
}
