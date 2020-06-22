JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Tema2AA.java 

build: $(CLASSES:.java=.class)

run: build
	java Tema2AA
clean:
	$(RM) *.class
