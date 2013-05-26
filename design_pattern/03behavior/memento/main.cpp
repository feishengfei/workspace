#include "memento.h"

#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	Originator* o = new Originator();
	o->setState("old");
	o->printState();

	Memento* m = o->createMemento();
	o->setState("new");
	o->printState();

	o->restore2Memento(m);
	o->printState();

	return 0;
}
