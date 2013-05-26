all: demo
demo:main.cpp proxy.o 
	g++ -o demo main.cpp proxy.o 

proxy.o:proxy.cpp proxy.h
	g++ -c  proxy.cpp


clean:
	rm -fr *.o *.gch demo
