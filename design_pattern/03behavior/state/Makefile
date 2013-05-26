all:demo
demo:main.o state.o context.o
	g++ -o demo main.o state.o context.o

main.o:main.cpp
	g++ -c main.cpp

state.o:state.cpp state.h
	g++ -c state.cpp 

context.o:context.cpp context.h
	g++ -c context.cpp 

clean:
	rm -fr demo *.o 	
