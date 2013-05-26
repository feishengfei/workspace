#include <iostream>
using namespace std;

float sqrt(float a)
{
	cout << "float sqrt(float a);" << endl;
}

double sqrt(double a)
{
	cout << "double sqrt(double a);" << endl;
}

int main(void)
{
	float f = 1.1;
	double d = 1.1;
	float fr = sqrt(d);		
	double dr = sqrt(d);		
	fr = sqrt(f);
	dr = sqrt(f);
}
