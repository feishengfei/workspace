#ifndef _BUILDER_H_
#define _BUILDER_H_
#include <string>
using namespace std;

class Product;

class Builder
{
public:
	virtual ~Builder();
	virtual void buildPartA(const string &buildPara)=0;
	virtual void buildPartB(const string &buildPara)=0;
	virtual void buildPartC(const string &buildPara)=0;
	virtual Product* getProduct()=0;
protected:
	Builder();
private:
};

class ConcreteBuilder:public Builder
{
public:
	ConcreteBuilder();
	~ConcreteBuilder();
	void buildPartA(const string &buildPara);
	void buildPartB(const string &buildPara);
	void buildPartC(const string &buildPara);
	Product* getProduct();
protected:
private:
};

#endif//~_BUILDER_H_
