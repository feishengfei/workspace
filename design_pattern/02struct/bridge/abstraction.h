#ifndef _ABSTRACTION_H_
#define _ABSTRACTION_H_

class AbstractionImp;

class Abstraction
{
public:
	virtual ~Abstraction();
	virtual void operation()=0;

protected:
	Abstraction();

private:

};

class RefinedAbstraction : public Abstraction
{
public:
	RefinedAbstraction(AbstractionImp *imp);
	~RefinedAbstraction();

	void operation();
protected:
	
private:
	AbstractionImp* _imp;
};

#endif//~_ABSTRACTION_H_
