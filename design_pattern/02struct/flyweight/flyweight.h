#ifndef _FLYWEIGHT_H_
#define _FLYWEIGHT_H_

#include <string>
using namespace std;

class FlyWeight
{
public:
	virtual ~FlyWeight();
	virtual void operation(const string& extrinsicState);	
	string getIntrinsicState();

protected:
	FlyWeight(string intrinsicState);

private:
	string _intrinsicState;	
};

class ConcreteFlyWeight:public FlyWeight
{
public:
	ConcreteFlyWeight(string intrinsicState);
	~ConcreteFlyWeight();

	void operation(const string& extrinsicState);
protected:
private:

};

#endif//~_FLYWEIGHT_H_
