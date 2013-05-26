#include "abstraction.h"
#include "abstractionimp.h"

#include <iostream>
using namespace std;

//~Abstraction
Abstraction::Abstraction()
{
}

Abstraction::~Abstraction()
{
}

//~RefinedAbstraction
RefinedAbstraction::RefinedAbstraction(AbstractionImp* imp)
{
	_imp = imp;
}

RefinedAbstraction::~RefinedAbstraction()
{
}

void RefinedAbstraction::operation()
{
	_imp->operation();
}
