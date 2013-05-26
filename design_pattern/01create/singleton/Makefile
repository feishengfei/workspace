all: demo
demo:main.cpp singleton.o
	g++ -o demo main.cpp singleton.o 
singleton.o:singleton.cpp singleton.h
	g++ -c  singleton.cpp
clean:
	rm -fr *.o *.gch demo
