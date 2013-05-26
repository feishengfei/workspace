#ifndef _SUBJECT_H_
#define _SUBJECT_H_
#include <list>
#include <string>
using namespace std;

typedef string State;
class Observer;
class Subject
{
public:
	virtual ~Subject();
	virtual void attach(Observer* obv);
	virtual void detach(Observer* obv);
	virtual void notify();
	virtual void setState(const State& )=0;
	virtual State getState()=0;

protected:
	Subject();

private:
	list<Observer*>* _obvs;
};

class ConcreteSubject:public Subject
{
public:
	ConcreteSubject();
	~ConcreteSubject();
	State getState();
	void setState(const State& state);
protected:
private:
	State _st;
};


#endif//~_SUBJECT_H_
