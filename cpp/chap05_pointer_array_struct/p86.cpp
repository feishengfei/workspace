#include <iostream>

using namespace std;
const int c4 = 4;
int foo(int i)
{
	return i*i;
}

int main()
{
	const int c1 = 1;
	const int c2 = 2;
	const int c3 = foo(3);
	extern const int c4;
	const int* p = &c2;

	cout << c1 << c2 << c3 << c4 << *p << endl;
}
