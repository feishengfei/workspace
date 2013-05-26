#include "singleton.h"

#include <iostream>
using namespace std;

Singleton *Singleton::_instance = 0;

Singleton::Singleton()
{
	cout << "Singleton..." << endl;
}

Singleton* Singleton::Instance()
{
	if(0== _instance) {
		_instance = new Singleton();
	}

	return _instance;
}
