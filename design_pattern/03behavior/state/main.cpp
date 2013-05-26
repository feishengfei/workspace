#include "state.h"
#include "context.h"

#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	State* st = new ConcreteStateA();
	Context* con = new Context(st);
	con->operationInterface();
	con->changeState(new ConcreteStateB());
	con->operationInterface();
	con->operationInterface();

	if(NULL != con)
		delete con;
	if(NULL != st)
		st = NULL;
	return 0;

}
