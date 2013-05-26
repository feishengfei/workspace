#include "subject.h"
#include "observer.h"

#include <iostream>
#include <string>


using namespace std;

int main(int argc, char* argv[])
{
	Subject* sub = new ConcreteSubject();
	Observer* o1 = new ConcreteObserverA(sub);
	Observer* o2 = new ConcreteObserverB(sub);
	sub->setState("old");
	sub->notify();
	sub->setState("new");
	sub->notify();

	return 0;
}
