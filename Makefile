all:
	javac -d classes -sourcepath src src/Driver.java

run:
	java -cp classes src/Driver.java

clean:
	rm -rf classes