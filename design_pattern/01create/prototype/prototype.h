#ifndef _PROTOTYPE_H_
#define _PROTOTYPE_H_
class Prototype
{
public:
	virtual ~Prototype();
	virtual Prototype* clone() const = 0;

protected:
	Prototype();

private:

};

class ConcretePrototype:public Prototype
{
public:
	ConcretePrototype();
	~ConcretePrototype();
	ConcretePrototype(const ConcretePrototype& cp);
	Prototype* clone() const;

protected:

private:
};

#endif//~_PROTOTYPE_H_

