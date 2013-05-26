#ifndef _OBSERVER_H_
#define _OBSERVER_H_

#include "subject.h"
#include <string>
using namespace std;

typedef string State;
class Observer
{
public:
	virtual ~Observer();
	virtual void update(Subject* sub)=0;
	virtual void printInfo()=0;
protected:
	Observer();
	State _st;
private:
};

class ConcreteObserverA:public Observer
{
public:
	virtual Subject* getSubject();
	ConcreteObserverA(Subject* sub);
	virtual ~ConcreteObserverA();
	void update(Subject* sub);
	void printInfo();
protected:
private:
	Subject* _sub;
};

class ConcreteObserverB:public Observer
{
public:
	virtual Subject* getSubject();
	ConcreteObserverB(Subject* sub);
	virtual ~ConcreteObserverB();
	void update(Subject* sub);
	void printInfo();
protected:
private:
	Subject* _sub;
};

#endif//~_OBSERVER_H_
