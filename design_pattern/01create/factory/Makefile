all: demo
demo:main.cpp product.o factory.o
	g++ -o demo main.cpp product.o factory.o

product.o:product.cpp product.h
	g++ -c  product.cpp

factory.o:factory.cpp factory.h
	g++ -c factory.cpp

clean:
	rm -fr *.o *.gch demo
