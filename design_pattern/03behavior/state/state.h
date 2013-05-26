#ifndef _STATE_H_
#define _STATE_H_

class Context;

class State
{
public:
	State();
	virtual ~State();
	virtual void operationInterface(Context *)=0;
	virtual void operationChangeState(Context *)=0;
protected:
	bool changeState(Context* con, State* st);
private:
};

class ConcreteStateA:public State
{
public:
	ConcreteStateA();
	virtual ~ConcreteStateA();
	virtual void operationInterface(Context*);
	virtual void operationChangeState(Context*);
protected:
private:
};

class ConcreteStateB:public State
{
public:
	ConcreteStateB();
	virtual ~ConcreteStateB();
	virtual void operationInterface(Context*);
	virtual void operationChangeState(Context*);
protected:
private:
};

#endif//~_STATE_H_
