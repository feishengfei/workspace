#include "memento.h"
#include <iostream>
using namespace std;

typedef string State;

//~Originator
Originator::Originator()
{
	_st = "";
	_mt = 0;
}

Originator::Originator(const State& st)
{
	_st = st;
	_mt = 0;
}

Originator::~Originator()
{

}

Memento* Originator::createMemento()
{
	return new Memento(_st);
}

State Originator::getState()
{
	return _st;
}

void Originator::setState(const State& st)
{
	_st = st;
}

void Originator::printState()
{
	cout << _st << "..." << endl;
}

void Originator::setMemento(Memento* mem)
{
	//TODO
}

void Originator::restore2Memento(Memento* mem)
{
	_st = mem->getState();
}

//~Memento

Memento::Memento()
{
}

Memento::Memento(const State& st)
{
	_st = st;
}

State Memento::getState()
{
	return _st;
}

void Memento::setState(const State& st)
{
	_st = st;
}

