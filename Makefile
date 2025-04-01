all:
	javac -d classes -sourcepath src src/org/drivers/MatchDriver.java
	javac -d classes -sourcepath src src/org/test/testDriver.java
	javac -d classes -sourcepath src src/org/menu/StubTerminalMenu.java

run:
	java -cp classes src/org/menu/StubTerminalMenu.java

match:
	java -cp classes src/org/drivers/MatchDriver.java

test:
	java -ea -cp classes src/org/test/TestDriver.java

tournament:
	java -ea -cp classes src/org/tournament/Tournament.java

clean:
	rm -rf classes