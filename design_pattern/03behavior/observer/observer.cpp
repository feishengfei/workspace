#include "observer.h"
#include "subject.h"
#include <iostream>
#include <string>

using namespace std;
//~Observer
Observer::Observer()
{
	_st = '\0';
}

Observer::~Observer()
{
}

//~ConcreteObserverA
ConcreteObserverA::ConcreteObserverA(Subject* sub)
{
	_sub = sub;
	_sub->attach(this);
}

ConcreteObserverA::~ConcreteObserverA()
{
	_sub->detach(this);
	if(_sub!=0)
		delete _sub;
}

Subject* ConcreteObserverA::getSubject()
{
	return _sub;
}

void ConcreteObserverA::printInfo()
{
	cout << "ConcreteObserverA observer..." << _sub->getState() << endl;
}

void ConcreteObserverA::update(Subject* sub)
{
	_st = sub->getState();
	printInfo();
}

//~ConcreteObserverB
ConcreteObserverB::ConcreteObserverB(Subject* sub)
{
	_sub = sub;
	_sub->attach(this);
}

ConcreteObserverB::~ConcreteObserverB()
{
	_sub->detach(this);
	if(_sub!=0)
		delete _sub;
}

Subject* ConcreteObserverB::getSubject()
{
	return _sub;
}

void ConcreteObserverB::printInfo()
{
	cout << "ConcreteObserverB observer..." << _sub->getState() << endl;
}

void ConcreteObserverB::update(Subject* sub)
{
	_st = sub->getState();
	printInfo();
}
