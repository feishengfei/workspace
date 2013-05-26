#include "strategy.h"
#include <iostream>

using namespace std;

//~Strategy
Strategy::Strategy()
{
}

Strategy::~Strategy()
{
	cout << "~Strategy()..." << endl;
}

void Strategy::AlgrithmInterface()
{
}

//~ConcreteStrategyA
ConcreteStrategyA::ConcreteStrategyA()
{
}

ConcreteStrategyA::~ConcreteStrategyA()
{
}

void ConcreteStrategyA::AlgrithmInterface()
{
	cout << "test ConcreteStrategyA..." << endl;
}

//~ConcreteStrategyB
ConcreteStrategyB::ConcreteStrategyB()
{
}

ConcreteStrategyB::~ConcreteStrategyB()
{
}

void ConcreteStrategyB::AlgrithmInterface()
{
	cout << "test ConcreteStrategyB..." << endl;
}
