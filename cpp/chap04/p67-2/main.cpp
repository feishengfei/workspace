#include <iostream>
using namespace std;

int main(void)
{
	double a[] = {
		1.23,
		.23,
		0.23,
		1.,
		1.0,
		1.2e10,
		1.23e-15,
	};

	for( int i = 0; i < sizeof(a)/sizeof(a[0]); i++ ) {
		cout << a[i] << endl;
	}
}
