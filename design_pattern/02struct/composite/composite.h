#ifndef _COMPOSITE_H_
#define _COMPOSITE_H_

#include "component.h"
#include <vector>
using namespace std;

class Composite:public Component
{
public:
	Composite();
	~Composite();

public:
	void operation();
	void add(Component *);
	void remove(Component *);
	Component* getChild(int);

protected:
private:
	vector<Component*> _comVec;
};

#endif//~_COMPOSITE_H_
