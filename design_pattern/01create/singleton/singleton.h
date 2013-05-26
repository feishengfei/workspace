#ifndef _SINGLETON_H
#define _SINGLETON_H


class Singleton
{
public:
	static Singleton *Instance();

private:
	Singleton();

private:
	static Singleton *_instance;
};


#endif
