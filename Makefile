all: Klex

Klex:
	javac -d bin -cp ojdbc8.jar -sourcepath ./ Klex.java

exeKlex: 
	java -classpath bin:ojdbc8.jar  Klex


clean:
	rm -rf bin/*.class
