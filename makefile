all: tests target
 
tests: routing.class Graph.class BinaryTrie.class FabonacciHeap.class

target: ssp.class FabonacciHeap.class Graph.class

ssp.class: ssp.java
	javac -d . -classpath . ssp.java

FabonacciHeap.class: FabonacciHeap.java
	javac -d . -classpath . FabonacciHeap.java

Graph.class: Graph.java
	javac -d . -classpath . Graph.java

BinaryTrie.class: BinaryTrie.java
	javac -d . -classpath . BinaryTrie.java

routing.class: routing.java
	javac -d . -classpath . routing.java

clean:
	rm -f *.class 
