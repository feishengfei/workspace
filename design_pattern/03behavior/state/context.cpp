#include "context.h"
#include "state.h"

//~Context
Context::Context()
{
}

Context::Context(State* state)
{
	_state=state;
}

Context::~Context()
{
	delete _state;
}

void Context::operationInterface()
{
	_state->operationInterface(this);
}

bool Context::changeState(State* state)
{
	_state=state;
	return true;
}

void Context::operationChangeState()
{
	_state->operationChangeState(this);
}
