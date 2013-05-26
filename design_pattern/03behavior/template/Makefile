all:demo
demo:main.o template.o
	g++ -o demo main.o template.o

main.o:main.cpp
	g++ -c main.cpp

template.o:template.cpp template.h
	g++ -c template.cpp 

clean:
	rm -fr demo *.o
