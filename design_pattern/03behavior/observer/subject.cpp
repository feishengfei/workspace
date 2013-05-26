#include "subject.h"
#include "observer.h"

#include <iostream>
#include <list>
using namespace std;
typedef string state;

//~Subject
Subject::Subject()
{
	_obvs = new list<Observer*>;
}

Subject::~Subject()
{
}

void Subject::attach(Observer* obv)
{
	_obvs->push_front(obv);
}

void Subject::detach(Observer* obv)
{
	if(NULL != obv)
		_obvs->remove(obv);
}

void Subject::notify()
{
	list<Observer*>::iterator it;
	it = _obvs->begin();
	for(;it != _obvs->end();it++){
		(*it)->update(this);
	}
}

//~ConcreteSubject
ConcreteSubject::ConcreteSubject()
{
	_st = '\0';
}

ConcreteSubject::~ConcreteSubject()
{
}

State ConcreteSubject::getState()
{
	return _st;
}

void ConcreteSubject::setState(const State& st)
{
	_st = st;
}
