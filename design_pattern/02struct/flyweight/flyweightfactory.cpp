#include "flyweightfactory.h"
#include <iostream>
#include <string>
#include <cassert>

using namespace std;

FlyWeightFactory::FlyWeightFactory()
{
}

FlyWeightFactory::~FlyWeightFactory()
{
}

FlyWeight* FlyWeightFactory::getFlyWeight(const string& key)
{
	vector<FlyWeight*>::iterator it = _fly.begin();

	for(;it!= _fly.end();++it) {
		if((*it)->getIntrinsicState() == key) {
			cout << "already created by users..." << endl;
			return *it;
		}
	}

	FlyWeight* fw = new ConcreteFlyWeight(key);
	_fly.push_back(fw);

	return fw;
}
