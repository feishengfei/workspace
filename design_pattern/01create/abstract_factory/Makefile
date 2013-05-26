all: demo
demo:main.cpp product.o abstractfactory.o
	g++ -o demo main.cpp product.o abstractfactory.o

product.o:product.cpp product.h
	g++ -c  product.cpp

abstractfactory.o:abstractfactory.cpp abstractfactory.h
	g++ -c abstractfactory.cpp

clean:
	rm -fr *.o *.gch demo
