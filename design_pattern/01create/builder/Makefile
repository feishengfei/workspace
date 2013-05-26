all: demo
demo:main.cpp product.o director.o builder.o
	g++ -o demo main.cpp product.o director.o builder.o

product.o:product.cpp product.h
	g++ -c  product.cpp

director.o:director.cpp director.h
	g++ -c director.cpp

builder.o:builder.cpp builder.h
	g++ -c builder.cpp

clean:
	rm -fr *.o *.gch demo
