#ifndef _DECORATOR_H_
#define _DECORATOR_H_

class Component
{
public:
	virtual ~Component();
	virtual void operation();
protected:
	Component();
private:
};

class ConcreteComponent : public Component
{
public:
	ConcreteComponent();
	~ConcreteComponent();
	void operation();
protected:
private:
};

class Decorator:public Component
{
public:
	Decorator(Component* com);
	virtual ~Decorator();
	void operation();
protected:
	Component *_com;
private:
};

class ConcreteDecorator:public Decorator
{
public:
	ConcreteDecorator(Component *com);
	~ConcreteDecorator();
	void operation();
	void addedBehavior();
protected:
private:
};

#endif//~_DECORATOR_H_
