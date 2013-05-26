#include "abstraction.h"
#include "abstractionimp.h"

#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	AbstractionImp* imp = new ConcreteAbstractionImpA();

	Abstraction* abs = new RefinedAbstraction(imp);	
	abs->operation();

	return 0;
}
