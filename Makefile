all:
	javac -d classes -sourcepath src src/match_engine/MatchEngine.java

run:
	java -cp classes src/match_engine/MatchEngine.java

clean:
	rm -rf classes