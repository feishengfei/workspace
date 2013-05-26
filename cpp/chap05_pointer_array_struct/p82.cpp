#include <vector>
#include <iostream>

using namespace std;

void foo()
{
	char p[] = "Tomato";
	p[4] = 'e';
}
const char *p = "Apple";
const char *q = "Apple";

int main(void)
{
	foo();
	if (p == q) {
		cout << "p==q" << endl;
	}
	cout << "beep at the end of message\a\n";

	char alpha[] = 	"abcdefg"
					"ABCDEFG";

	return 0;
}
