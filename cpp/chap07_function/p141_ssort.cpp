#include <iostream>
using namespace std;

typedef int (*CFT)(const void *, const void *);

/*
*	对base的n个元素按照递增顺序排序
*	用由“cmp“所指的函数做比较
*	元素的大小是"sz"
*/
void ssort(void *base, size_t n, size_t sz, CFT cmp)
{
	for (int gap = n/2; 0<gap; gap /= 2)
		for (int i = gap; i < n; i++ )
			for (int j=i-gap; 0<=j; j-=gap) {
				char *b = static_cast<char *>(base);
				char *pj = b + j*sz;
				char *pjg = b +(j+gap)*sz;

				if (cmp(pjg, pj) <0) {
					for (int k = 0; k<sz; k++) {
						char temp = pj[k];
						pj[k] = pjg[k];
						pjg[k] = temp;
					}
				}
			}
}

struct User {
	const char *name;
	const char *id;
	int dept;
};

User users[] = {
	"Ritchie D. M. ",	"dmr",		11271,
	"Sethi R. ",		"ravi",		11272,
	"Szymanski T. G. ", "tgs", 		11273,
	"Schryer N. L. ", 	"nls", 		11274,
	"Schryer N. L. ", 	"nls",		11275,
	"Kernighan B. W.",	"bwk",		11276
};

void print_id(User *puser, int n)
{
	for (int i = 0; i < n; i++) {
		cout << puser[i].name << "\t" 
			 << puser[i].id << "\t" 
			 << puser[i].dept << endl;
	}
}

int cmp1(const void *p, const void *q)
{
	return strcmp(	static_cast<const User*>(p)->name, 
					static_cast<const User*>(q)->name);
}

int cmp2(const void *p, const void *q)
{
	return static_cast<const User*>(p)->dept - 
			static_cast<const User*>(q)->dept;
}

int main(void)
{
	cout << "Users in alphabetical order:" << endl;
	ssort(users, 6, sizeof(User), cmp1);
	print_id(users, 6);
	cout << endl;

	cout << "Users in order of department number:" << endl;
	ssort(users, 6, sizeof(User), cmp2);
	print_id(users, 6);
	cout << endl;


	return 0;
}
