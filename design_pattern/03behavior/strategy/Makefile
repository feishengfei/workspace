all:demo
demo:main.o strategy.o context.o
	g++ -o demo main.o strategy.o context.o

main.o:main.cpp
	g++ -c main.cpp

strategy.o:strategy.cpp strategy.h
	g++ -c strategy.cpp 

context.o:context.cpp context.h
	g++ -c context.cpp 

clean:
	rm -fr demo *.o 	
