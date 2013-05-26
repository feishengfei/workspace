#include "adapter.h"
#include <iostream>

//~Target
Target::Target()
{
}

Target::~Target()
{
}

void Target::request()
{
	std::cout << "Target::request()" << std::endl;
}

//~Adaptee
Adaptee::Adaptee()
{
}

Adaptee::~Adaptee()
{
}

void Adaptee::specificRequest()
{
	std::cout << "Adaptee::specificRequest" << std::endl;
}

//~Adapter

Adapter::Adapter(Adaptee *ade)
{
	_ade = ade;
}

Adapter::~Adapter()
{
}

void Adapter::request()
{
	_ade->specificRequest();
}
