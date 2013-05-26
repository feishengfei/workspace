objects = main.o proxy.o

all: demo
demo:$(objects)
	g++ -o demo $(objects)

proxy.o: proxy.h

.PHONY:clean
clean:
	-rm -fr demo $(objects)
print: *.cpp
	lpr -p $?
	touch print
