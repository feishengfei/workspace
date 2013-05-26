#include "template.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[])
{
	AbstractClass* p1 = new ConcreteClass1();
	AbstractClass* p2 = new ConcreteClass2();
	
	p1->templateMethod();
	p2->templateMethod();

	return 0;
}
