#include <iostream>

using namespace std;

enum keyword{ 
	TYPE1,
	TYPE2,
	TYPE3
};

void f(keyword key)
{
	switch (key) {
		case TYPE1:
			break;
		case TYPE2:
			break;
	}
}


int main(void)
{
	f(TYPE1);
}

