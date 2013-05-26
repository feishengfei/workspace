#include "strategy.h"
#include "context.h"

int main(int argc, char *argv[])
{
	Strategy* stg = new ConcreteStrategyA();
	Context* pc = 0;
	pc = new Context(stg);
	pc->doAction();
	if(0 != pc)
		delete pc;

	return 0;
}
