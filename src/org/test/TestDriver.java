package org.test;

import java.lang.StackWalker.StackFrame;
import java.util.stream.Collectors;
import org.match_engine.*;

public class TestDriver {
    private static int numberOfTests = 0;
    private static int passedTests = 0;


    private static void printStatistics() {
        System.out.println();
        int failedTests = numberOfTests - passedTests;
        if(failedTests == 0) {
            System.out.println("All tests passed! (" + numberOfTests + ")");
        } else {
            System.out.println("Passed " + passedTests + "/" + numberOfTests + " tests");
            System.out.println("Failed " + failedTests + " test" + (failedTests == 1? "":"s") + " (see line numbers above)");
        }
    }

    private static void printErrorMessage(String msg) {
        StackFrame frame = StackWalker.getInstance().walk(s ->
            s.limit(3).collect(Collectors.toList())).get(2);

        String className = frame.getClassName();
        String fileName = frame.getFileName();
        int line = frame.getLineNumber();
        String method = frame.getMethodName();
        System.err.println(className + "." + method +"(" + fileName + ":" + line + ")\n" + msg);
    }

    private static void testGameState(MatchEngine current, MatchEngine expected) {
        numberOfTests++;
        if (current.equals(expected)) {
            passedTests++;
        } else {
            printErrorMessage("\nExpected Game State " + current.toString() + "\nto be " + expected.toString());
            System.err.println("-------");
        }
    }

    private static void testEquality(Object current, Object expected) {
        numberOfTests++;
        if (current.equals(expected)) {
            passedTests++;
        } else {
            printErrorMessage("\nExpected " + current.toString() + "\nto be " + expected.toString());
            System.err.println("-------");
        }
    }

    private static void test9DartGame() {
        
        Player p1 = new Player("L. Humphries");
        Player p2 = new Player("L. Littler");
        Player p1Expected = new Player("L. Humphries");
        Player p2Expected = new Player("L. Littler");
        MatchLogic rules = new MatchLogic(501, 1, true);
        MatchEngine match = new MatchEngine(p1, p2, rules);
        MatchEngine expectedMatch = new MatchEngine(p1Expected, p2Expected, rules);
        testGameState(match, expectedMatch);
        boolean ifDoubleOut = match.matchRules.ifDoubleOut();
        p1.dartThrow(180, ifDoubleOut);
        p2.dartThrow(100, ifDoubleOut);
        p1Expected.score -= 180;
        p1Expected.stats.scores.add(180);
        p1Expected.stats.dartsThrown += 3;
        p2Expected.score -= 100;
        p2Expected.stats.scores.add(100);
        p2Expected.stats.dartsThrown += 3;
        testGameState(match, expectedMatch);
        
        p1.dartThrow(180, ifDoubleOut);
        p2.dartThrow(177, ifDoubleOut);
        p1Expected.score -= 180;
        p1Expected.stats.scores.add(180);
        p1Expected.stats.dartsThrown += 3;
        p2Expected.score -= 177;
        p2Expected.stats.scores.add(177);
        p2Expected.stats.dartsThrown += 3;
        testGameState(match, expectedMatch);

        p1.dartThrow(141, ifDoubleOut);
        p1Expected.score -= 141;
        p1Expected.stats.scores.add(141);
        p1Expected.stats.addCheckout(141, 1);
        p1Expected.stats.dartsThrown += 3;
        testGameState(match, expectedMatch);
        match.ifWonLeg().equals(p1);
        p1Expected.legs++;
        testGameState(match, expectedMatch);
        
        testEquality(p1, p1Expected);
        testEquality(match.ifWinner(p1), match.ifWinner(p1Expected));
    }



    private static void runTests() {
        test9DartGame();
    }

    public static void main(String[] args) {

        runTests();
        printStatistics();
    }
}
