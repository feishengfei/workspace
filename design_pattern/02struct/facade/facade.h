#ifndef _FACADE_H_
#define _FACADE_H_
class SubSystem1
{
public:
	SubSystem1();
	~SubSystem1();
	void operation();

protected:
private:
};

class SubSystem2
{
public:
	SubSystem2();
	~SubSystem2();
	void operation();
protected:
private:
};

class Facade
{
public:
	Facade();
	~Facade();
	void operationWrapper();
protected:
private:
	SubSystem1* _sub1;
	SubSystem2* _sub2;
};

#endif
