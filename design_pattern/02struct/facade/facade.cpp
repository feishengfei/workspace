#include "facade.h"
#include <iostream>

using namespace std;

//~SubSystem1
SubSystem1::SubSystem1()
{
}

SubSystem1::~SubSystem1()
{
}

void SubSystem1::operation()
{
	cout << "Subsystem1 operation..." << endl;	
}

//~SubSystem2
SubSystem2::SubSystem2()
{
}

SubSystem2::~SubSystem2()
{
}

void SubSystem2::operation()
{
	cout << "Subsystem2 operation..." << endl;	
}

//~Facade
Facade::Facade()
{
	_sub1 = new SubSystem1();
	_sub2 = new SubSystem2();
}

Facade::~Facade()
{
	delete _sub1;
	delete _sub2;
}

void Facade::operationWrapper()
{
	_sub1->operation();
	_sub2->operation();
}
