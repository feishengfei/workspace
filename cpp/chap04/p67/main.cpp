#include <iostream>
using namespace std;

int main(void)
{
	int a1 = 2;
	int a2 = 02;
	int a3 = 0x02;

	int b1 = 63;
	int b2 = 077;
	int b3 = 0x3f;

	int c1 = 83;
	int c2 = 0123;
	int c3 = 0x53;

	cout << a1 << ", " << a2 << ", " << a3 << endl;
	cout << b1 << ", " << b2 << ", " << b3 << endl;
	cout << c1 << ", " << c2 << ", " << c3 << endl;
}
