all:
	javac -d classes -sourcepath src src/org/drivers/MatchDriver.java
	javac -d classes -sourcepath src src/org/test/testDriver.java

run: all
	java -cp classes src/org/drivers/MatchDriver.java

test: all
	java -ea -cp classes src/org/test/TestDriver.java

tournament: all
	java -ea -cp classes src/org/tournament/Tournament.java

clean:
	rm -rf classes