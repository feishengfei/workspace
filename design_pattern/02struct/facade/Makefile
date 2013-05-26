all: demo
demo:main.cpp facade.o 
	g++ -o demo main.cpp facade.o 

facade.o:facade.cpp facade.h
	g++ -c  facade.cpp


clean:
	rm -fr *.o *.gch demo
