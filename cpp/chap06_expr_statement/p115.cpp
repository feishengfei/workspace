#include <iostream>
#include <vector>
using namespace std;

void f(int n)
{
	vector<int> *p = new vector<int>(n);	
	int *q = new int[n];

	delete p;
	delete[] q;
	int *r = NULL;
	delete r;
	delete[] r;
}

void g()
{
	try {

	}
	catch(badalloc) {
		cerr << "Memory exhausted!\n" << endl;
	}
}

int main(void)
{
	f(5);
	g();
}
