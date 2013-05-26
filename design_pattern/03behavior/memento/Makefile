all:demo
demo:main.o memento.o 
	g++ -o demo main.o memento.o 

main.o:main.cpp
	g++ -c main.cpp

memento.o:memento.cpp memento.h
	g++ -c memento.cpp

clean:
	rm -fr demo *.o
