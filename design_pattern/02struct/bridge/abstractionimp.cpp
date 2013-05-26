#include "abstractionimp.h"
#include <iostream>

using namespace std;
//~AbstractionImp
AbstractionImp::AbstractionImp()
{
}

AbstractionImp::~AbstractionImp()
{
}

void AbstractionImp::operation()
{
	cout << "Abstraction...imp..." << endl;
}

//~ConcreteAbstractionImpA
ConcreteAbstractionImpA::ConcreteAbstractionImpA()
{
}

ConcreteAbstractionImpA::~ConcreteAbstractionImpA()
{
}

void ConcreteAbstractionImpA::operation()
{
	cout << "ConcreteAbstractionA..." << endl;
}

//~ConcreteAbstractionImpB
ConcreteAbstractionImpB::ConcreteAbstractionImpB()
{
}

ConcreteAbstractionImpB::~ConcreteAbstractionImpB()
{
}

void ConcreteAbstractionImpB::operation()
{
	cout << "ConcreteAbstractionB..." << endl;
}
