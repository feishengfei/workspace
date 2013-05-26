#include "builder.h"
#include "product.h"
#include "director.h"
#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	Director *d = new Director(new ConcreteBuilder());
	d->construct();

	return 0;
}
