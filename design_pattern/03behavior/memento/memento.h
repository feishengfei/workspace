#ifndef _MEMENTO_H_
#define _MEMENTO_H_

#include <string>
using namespace std;

class Memento;

class Originator
{
public:
	typedef string State;
	Originator();
	Originator(const State& st);
	~Originator();
	Memento* createMemento();
	void setMemento(Memento* mem);
	void restore2Memento(Memento* mem);
	State getState();
	void setState(const State& st);
	void printState();
protected:
private:
	State _st;
	Memento* _mt;
};

class Memento
{
public:
protected:
private:
	friend class Originator;
	typedef string State;
	Memento();
	Memento(const State& st);
	~Memento();
	void setState(const State& st);
	State getState();
private:
	State _st;
};

#endif //~_MEMENTO_H_
