all:demo
demo:main.o subject.o observer.o
	g++ -o demo main.o subject.o observer.o

main.o:main.cpp
	g++ -c main.cpp

subject.o:subject.cpp subject.h
	g++ -c subject.cpp

observer.o:observer.cpp observer.h
	g++ -c observer.cpp

clean:
	rm -fr demo *.o
