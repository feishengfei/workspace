all: demo
demo:main.cpp prototype.o
	g++ -o demo main.cpp prototype.o

prototype.o:prototype.cpp prototype.h
	g++ -c prototype.cpp
clean:
	rm -fr *.o *.gch demo
