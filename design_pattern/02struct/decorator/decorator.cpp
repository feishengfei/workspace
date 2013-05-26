#include "decorator.h"
#include <assert.h>
#include <iostream>

//~Component
Component::Component()
{
}

Component::~Component()
{
}

void Component::operation()
{
}

//~ConcreteComponent

ConcreteComponent::ConcreteComponent()
{
}

ConcreteComponent::~ConcreteComponent()
{
}

void ConcreteComponent::operation()
{
	std::cout << "ConcreteComponent operation..." << std::endl;
}

//~Decorator

Decorator::Decorator(Component *com)
{
	assert(NULL != com);
	this->_com = com;
}

Decorator::~Decorator()
{
	if(NULL != _com)
		delete _com;
}

void Decorator::operation()
{

}

//~ConcreteDecorator
ConcreteDecorator::ConcreteDecorator(Component *com)
	:Decorator(com)
{
}

ConcreteDecorator::~ConcreteDecorator()
{
}

void ConcreteDecorator::addedBehavior()
{
	std::cout << "ConcreteDecorator::addedBehavior..." << std::endl;
}

void ConcreteDecorator::operation()
{
	_com->operation();
	this->addedBehavior();
}

