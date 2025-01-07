all:
	javac -d classes -sourcepath src src/org/Driver.java
	javac -d classes -sourcepath src src/org/test/testDriver.java

run: all
	java -cp classes src/org/Driver.java

test: all
	java -ea -cp classes src/org/test/TestDriver.java

clean:
	rm -rf classes