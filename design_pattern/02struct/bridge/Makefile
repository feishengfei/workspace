all: demo
demo:main.cpp abstraction.o abstractionimp.o
	g++ -o demo main.cpp abstraction.o abstractionimp.o

abstraction.o:abstraction.cpp abstraction.h
	g++ -c  abstraction.cpp

abstractionimp.o:abstractionimp.cpp abstractionimp.h
	g++ -c abstractionimp.cpp

clean:
	rm -fr *.o *.gch demo
