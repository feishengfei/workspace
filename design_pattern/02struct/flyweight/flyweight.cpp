#include "flyweight.h"

#include <iostream>
using namespace std;

//~FlyWeight
FlyWeight::FlyWeight(string intrinsicState)
{
	_intrinsicState = intrinsicState;
}

FlyWeight::~FlyWeight()
{
}

void FlyWeight::operation(const string& extrinsicState)
{
}

string FlyWeight::getIntrinsicState()
{
	return _intrinsicState;
}

//~ConcreteFlyWeight
ConcreteFlyWeight::ConcreteFlyWeight(string intrinsicState)
	:FlyWeight(intrinsicState)
{
	cout << "ConcreteFlyWeight Build..." << intrinsicState << endl;
}

ConcreteFlyWeight::~ConcreteFlyWeight()
{
}

void ConcreteFlyWeight::operation(const string& extrinsicState)
{
	cout << "ConcreteFlyWeight:内蕴[" << getIntrinsicState()
	<< "]外蕴[" << extrinsicState << "]" << endl;
}
