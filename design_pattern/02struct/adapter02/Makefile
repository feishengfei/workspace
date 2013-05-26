all: demo
demo:main.cpp adapter.o
	g++ -o demo main.cpp adapter.o


adapter.o:adapter.cpp adapter.h
	g++ -c adapter.cpp

clean:
	rm -fr *.o *.gch demo
