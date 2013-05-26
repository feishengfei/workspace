#include "template.h"
#include <iostream>

using namespace std;

//~AbstractClass
AbstractClass::AbstractClass()
{
}

AbstractClass::~AbstractClass()
{
}

void AbstractClass::templateMethod()
{
	primitiveOperation1();
	primitiveOperation2();
}

//~ConcreteClass1
ConcreteClass1::ConcreteClass1()
{
}

ConcreteClass1::~ConcreteClass1()
{
}

void ConcreteClass1::primitiveOperation1()
{
	cout << "ConcreteClass1... primitiveOperation1" << endl;
}

void ConcreteClass1::primitiveOperation2()
{
	cout << "ConcreteClass1... primitiveOperation2" << endl;
}

//~ConcreteClass2
ConcreteClass2::ConcreteClass2()
{
}

ConcreteClass2::~ConcreteClass2()
{
}

void ConcreteClass2::primitiveOperation1()
{
	cout << "ConcreteClass2... primitiveOperation1" << endl;
}

void ConcreteClass2::primitiveOperation2()
{
	cout << "ConcreteClass2... primitiveOperation2" << endl;
}
