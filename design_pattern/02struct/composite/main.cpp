#include "component.h"
#include "composite.h"
#include "leaf.h"

#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	Leaf* l = new Leaf();
	l->operation();

	Composite* com = new Composite();
	com->add(l);
	com->operation();

	Component* ll = com->getChild(0);
	ll->operation();

	return 0;
}
