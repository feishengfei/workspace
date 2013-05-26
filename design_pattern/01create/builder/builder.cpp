#include "builder.h"
#include "product.h"

#include <iostream>
using namespace std;

//~Builder
Builder::Builder()
{
}

Builder::~Builder()
{
}

//~ConcreteBuilder
ConcreteBuilder::ConcreteBuilder()
{
}

ConcreteBuilder::~ConcreteBuilder()
{
}

void ConcreteBuilder::buildPartA(const string& buildPara)
{
	cout << "Step1:Build PartA..." << buildPara << endl;
}

void ConcreteBuilder::buildPartB(const string& buildPara)
{
	cout << "Step2:Build PartB..." << buildPara << endl;
}

void ConcreteBuilder::buildPartC(const string& buildPara)
{
	cout << "Step3:Build PartC..." << buildPara << endl;
}

Product* ConcreteBuilder::getProduct()
{
	buildPartA("pre-defined");
	buildPartB("pre-defined");
	buildPartC("pre-defined");
	return new Product();
}
