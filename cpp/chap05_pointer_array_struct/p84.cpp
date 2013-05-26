#include <iostream>
using namespace std;

int main()
{
	int vi[10];
	short vs[10];
	
	for( int i = 0 ; i < sizeof(vi)/sizeof(vi[0]); i++ ) {
		cout << "&(vi[" << i << "]) = " << &vi[i] << endl;
	}

	for( int i = 0 ; i < sizeof(vs)/sizeof(vs[0]); i++ ) {
		cout << "&(vs[" << i << "]) = " << &vs[i] << endl;
	}

	int v1[10];
	int v2[10];
	cout << &v1[5] - &v1[3] << endl;
	cout << &v1[5] - &v2[3] << endl;		//无意义
}
