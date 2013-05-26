#include <iostream>
using namespace std;

struct address {
	char *name;
	long int number;
	char* street;
	char* town;
	char state[2];
	long zip;
};

void f(int *pi)
{
	void *pv = pi;
//	*pv;				//XXX
//	pv++;				//XXX
	int *pi2 = static_cast<int*>(pv);

//	double *pd1 = pv;	//XXX
//	double *pd2 = pi;	//XXX
	double *pd3 = static_cast<double *>(pv); //TODO unsafe!!!
}

void g(void)
{
	cout << sizeof(address) << endl;

}

int main(void)
{
	int a = 0;
	f(&a);

	g();

	return 0;
}
