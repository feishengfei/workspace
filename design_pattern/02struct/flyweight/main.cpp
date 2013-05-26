#include "flyweight.h"
#include "flyweightfactory.h"
#include <iostream>
using namespace std;

int main(int argc, char* argv[])
{
	FlyWeightFactory* fc = new FlyWeightFactory();

	FlyWeight* fw1 = fc->getFlyWeight("hello");
	FlyWeight* fw2 = fc->getFlyWeight("world!");
	FlyWeight* fw3 = fc->getFlyWeight("hello");
	
	fw1->operation("test");
	fw2->operation("test");
	fw3->operation("test");

	return 0;
}
