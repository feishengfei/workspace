#include "director.h"
#include "builder.h"

Director::Director(Builder *bld)
{
	_bld = bld;
}

Director::~Director()
{
}

void Director::construct()
{
	_bld->buildPartA("user-defined");
	_bld->buildPartB("user-defined");
	_bld->buildPartC("user-defined");
}
