#ifndef _CONTEXT_H_
#define _CONTEXT_H_
class State;

class Context
{
public:
	Context();
	Context(State* state);
	~Context();
	void operationInterface();
	void operationChangeState();
	bool changeState(State* state);
protected:

private:
	friend class State;

private:
	State* _state;
};

#endif//~_CONTEXT_H_
