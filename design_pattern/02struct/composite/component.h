#ifndef _COMPONENT_H_
#define _COMPONENT_H_
class Component
{
public:
	Component();
	virtual ~Component();

public:
	virtual void operation()=0;
	virtual void add(Component*);
	virtual void remove(Component*);
	virtual Component* getChild(int);

protected:
private:

};


#endif//~_COMPONENT_H_
