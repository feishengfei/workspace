#include "composite.h"
#include "component.h"
#include <iostream> 
Composite::Composite()
{
}

Composite::~Composite()
{
}

void Composite::operation()
{
	vector<Component*>::iterator it = _comVec.begin();
	for(;it!=_comVec.end();it++)
	{
		(*it)->operation();
	}
}

void Composite::add(Component* com)
{
	_comVec.push_back(com);	
}

void Composite::remove(Component* com)
{
	vector<Component*>::iterator iter;
	for(iter = _comVec.begin(); iter != _comVec.end();)
	{
		if(*iter == com) {
			iter=_comVec.erase(iter);
		}
		else {
			iter++;
		}
	} 
}

Component* Composite::getChild(int index)
{
	return _comVec[index];
}
