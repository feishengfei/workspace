#ifndef _FLYWEIGHTFACTORY_H_
#define _FLYWEIGHTFACTORY_H_

#include "flyweight.h"
#include <string>
#include <vector>
using namespace std;

class FlyWeightFactory
{
public:
	FlyWeightFactory();
	~FlyWeightFactory();
	FlyWeight* getFlyWeight(const string& key);
protected:
private:
	vector<FlyWeight*> _fly;
};

#endif
