all: demo
demo:main.cpp decorator.o
	g++ -o demo main.cpp decorator.o

decorator.o:decorator.cpp decorator.h
	g++ -c  decorator.cpp

clean:
	rm -fr *.o *.gch demo
