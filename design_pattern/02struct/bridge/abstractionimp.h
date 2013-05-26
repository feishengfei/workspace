#ifndef _ABSTRACTIONIMP_H_
#define _ABSTRACTIONIMP_H_

class AbstractionImp
{
public:
	virtual ~AbstractionImp();
	virtual void operation()=0;

protected:
	AbstractionImp();

private:
};

class ConcreteAbstractionImpA : public AbstractionImp
{
public:
	ConcreteAbstractionImpA();
	~ConcreteAbstractionImpA();
	virtual void operation();
protected:

private:
};

class ConcreteAbstractionImpB : public AbstractionImp
{
public:
	ConcreteAbstractionImpB();
	~ConcreteAbstractionImpB();
	virtual void operation();

protected:

private:
};

#endif//~_ABSTRACTIONIMP_H_
