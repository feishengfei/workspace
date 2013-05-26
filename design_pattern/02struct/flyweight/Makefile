all: demo
demo:main.cpp flyweight.o flyweightfactory.o
	g++ -o demo main.cpp flyweight.o flyweightfactory.o

flyweight.o:flyweight.cpp flyweight.h
	g++ -c  flyweight.cpp

flyweightfactory.o:flyweightfactory.cpp flyweightfactory.h
	g++ -c  flyweightfactory.cpp

clean:
	rm -fr *.o *.gch demo
