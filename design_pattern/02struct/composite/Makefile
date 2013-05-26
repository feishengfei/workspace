all: demo
demo:main.cpp component.o composite.o leaf.o
	g++ -o demo main.cpp component.o composite.o leaf.o

component.o:component.cpp component.h
	g++ -c  component.cpp

composite.o:composite.cpp composite.h
	g++ -c  composite.cpp

leaf.o:leaf.cpp leaf.h
	g++ -c  leaf.cpp

clean:
	rm -fr *.o *.gch demo
