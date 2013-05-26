#include <iostream>
using namespace std;

void foo( char *p )
{
	char s[] = "apple";
	
	const char* pc = s;		//指向常量
//	pc[3] = 'g';			//错误:pc是指向常量的
	pc = p;					//ok
	cout << "pc:" << pc << endl;


	char *const cp = s;		//常量指针
	cp[3] = 'g';			//ok
//	cp = p;					//错误:cp是常量指针
	cout << "cp:" << cp << endl;


	const char *const cpc = s;//到const的const指针
//	cpc[3] = 'g';			//错误:cpc指向常量
//	cpc = p;					//错误:cpc本身是常量
	cout << "cpc:" << cpc << endl;

}

int main(void)
{
	foo("banana");
	return 0;
}

