#include "adapter.h"

#include <iostream>

using namespace std;

int main(int argc, char* argv[])
{
	Adaptee *ade = new Adaptee();
	Adapter *ad = new Adapter(ade);
	ad->request();
}
