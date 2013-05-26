#ifndef _ABSTRACT_FACTORY_H_
#define _ABSTRACT_FACTORY_H_

class AbstractProductA;
class AbstractProductB;

class AbstractFactory
{
public:
	virtual ~AbstractFactory();
	virtual AbstractProductA* createProductA()=0;
	virtual AbstractProductB* createProductB()=0;
protected:
	AbstractFactory();
private:
};

class ConcreteFactory1 : public AbstractFactory
{
public:
	ConcreteFactory1();
	~ConcreteFactory1();
	AbstractProductA* createProductA();
	AbstractProductB* createProductB();

protected:
private:

};

class ConcreteFactory2 : public AbstractFactory
{
public:
	ConcreteFactory2();
	~ConcreteFactory2();
	AbstractProductA* createProductA();
	AbstractProductB* createProductB();

protected:
private:

};

#endif//~_ABSTRACT_FACTORY_H_
