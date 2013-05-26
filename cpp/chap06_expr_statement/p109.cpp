#include <iostream>
using namespace std;

void f(int x, int y)
{
	int j = x = y;
	cout << "j = " << j << endl;
	cout << "x = " << x << endl;
	cout << "y = " << y << endl;

	int *p = &++x;
	cout << "*p = " << *p << endl;

	int *q = &++x;
	cout << "*q = " << *q << endl;

}

void g()
{
	int i = 1;
	while (i>0){
		i++;
	}
	cout << "i为负了" << i << endl;
	printf("%0x\r\n", i);
}

int main(void)
{
	f(1, 2);
	g();
}
