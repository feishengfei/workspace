#include "state.h"
#include "context.h"

#include <iostream>
using namespace std;

//~State

State::State()
{
}

State::~State()
{
}

void State::operationInterface(Context* con)
{
	cout << "State::..." << endl;
}

bool State::changeState(Context* con, State* st)
{
	con->changeState(st);
	return true;
}

void State::operationChangeState(Context* con)
{

}

//~ConcreteStateA
ConcreteStateA::ConcreteStateA()
{
}

ConcreteStateA::~ConcreteStateA()
{
}

void ConcreteStateA::operationInterface(Context* con)
{
	cout << "ConcreteStateA::operationInterface..." << endl;
}

void ConcreteStateA::operationChangeState(Context* con)
{
	operationInterface(con);
	changeState(con, new ConcreteStateB());
}

//~ConcreteStateB
ConcreteStateB::ConcreteStateB()
{
}

ConcreteStateB::~ConcreteStateB()
{
}

void ConcreteStateB::operationInterface(Context* con)
{
	cout << "ConcreteStateB::operationInterface..." << endl;
}

void ConcreteStateB::operationChangeState(Context* con)
{
	operationInterface(con);
	changeState(con, new ConcreteStateB());
}

