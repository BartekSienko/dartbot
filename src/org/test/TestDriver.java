package org.test;

import java.lang.StackWalker.StackFrame;
import java.util.stream.Collectors;
import java.util.*;
import org.match_engine.*;
import org.match_engine.dartbot.*;

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

    private static void testEquality(Object current, Object expected) {
        numberOfTests++;
        if (current.equals(expected)) {
            passedTests++;
        } else {
            printErrorMessage("\nExpected " + current.toString() + "\nto be " + expected.toString());
            System.err.println("-------");
        }
    }

    private static void testContains(Integer current, List<Integer> expected) {
        numberOfTests++;
        if (expected.contains(current)) {
            passedTests++;
        } else {
            printErrorMessage("\nExpected " + current.toString() + "\nto be " + expected.toString());
            System.err.println("-------");
        }
    }


    private static void testPlayerMethods() {
        Player p1 = new Player("M. van Gerwen");
        Player p1Expected = new Player("M. van Gerwen");
        testEquality(p1, p1Expected);
        p1.dartThrow(180, true, 0);
        p1Expected.score -= 180;
        p1Expected.stats.scores.add(180);
        p1Expected.stats.first9scores.add(180);
        p1Expected.stats.dartsThrown += 3;
        p1Expected.stats.dartsThrownLeg += 3;
        testEquality(p1, p1Expected);
        testEquality(p1.toString(), p1Expected.toString());
    }

    private static void testDartBotTargetLocating() {
        // Test essentially just checks that we don't get a RuntimeException
        DartBot bot = new DartBot("Bot");
        
        // Some often used ThrowTargets
        ThrowTarget t20 = new ThrowTarget(3, 20);
        ThrowTarget t19 = new ThrowTarget(3, 19);
        ThrowTarget t18 = new ThrowTarget(3, 18);
        ThrowTarget tt;


        // Special case 1: Double-In with Start Score
        bot.score = 501;
        tt = bot.getThrowTarget(true);
        testEquality(tt, new ThrowTarget(2, 20));

        // Special case 2: 3 Darts 129,126,123 to go, aims for T19
        bot.score = 129;
        bot.dartsInHand = 3;
        for (; 123 <= bot.score; bot.score -= 3) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, t19);
        }
        // Special case 3: 3 Darts 128,125,122 to go, aims for T18
        bot.score = 128;
        for (; 122 <= bot.score; bot.score -= 3) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, t18);
        }
        // Special case 4: 2 Darts 110,107,104,101 to go, aims for T20,19,18,17 respectively
        bot.score = 110;
        bot.dartsInHand = 2;
        for (; 101 <= bot.score; bot.score -= 3) {
            tt = bot.getThrowTarget(false);
            int targetedTreble = (bot.score - 50) / 3;
            testEquality(tt, new ThrowTarget(3, targetedTreble));
        }
        // Case 1: Remaining Score (501-190) aims for T20
        bot.score = 501;
        bot.dartsInHand = 1;
        for (; 190 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, t20);
        }
        // Case 2: Remaining Score (189-179) aims for T19,18 to not leave a 3 dart finish
        for (; 179 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            ThrowTarget expected;
            if (bot.score == 179 || (bot.score % 3 == 0 && bot.score != 180)) {
                expected = t19;
            } else if (bot.score == 180 || bot.score % 3 == 1) {
                expected = t20;
            } else {
                expected = t18;
            }
            testEquality(tt, expected);
        }
        // Case 1.5: Remaining Score (178-100) aims for T20
        for (; 100 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, t20);
        }
        // Case 3: Remaining Score 99-98, aims for T19-T20 respectively
        tt = bot.getThrowTarget(false);
        testEquality(tt, t19);
        bot.score--;
        tt = bot.getThrowTarget(false);
        testEquality(tt, t20);
        bot.score--;
        // Case 4: Remaining Score 98-61, aims to setup D20,18,16,12,10,8
        for(; 61 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            ThrowTarget expected;
            List<Integer> t19Specials = new ArrayList<>(Arrays.asList(95, 79));
            if (t19Specials.contains(bot.score)) {
                expected = t19;
            } else if (bot.score == 80 || bot.score == 77) {
                expected = new ThrowTarget(3, (bot.score - 20) / 3);
            } else if (bot.score % 3 == 2) {
                expected = new ThrowTarget(3, (bot.score - 32) / 3);
            } else if (bot.score % 3 == 1 && 82 <= bot.score) {
                expected = new ThrowTarget(3, (bot.score - 40) / 3);
            } else if (bot.score % 3 == 1 && 82 > bot.score) {
                expected = new ThrowTarget(3, (bot.score - 16) / 3);
            } else if (bot.score % 3 == 0 && 87 <= bot.score) {
                expected = new ThrowTarget(3, (bot.score - 36) / 3);
            } else {
                expected = new ThrowTarget(3, (bot.score - 24) / 3);
            }
            testEquality(tt, expected);
        }
        // Case 5: Remaining Score 60-57, aims to setup D20
        for (; 57 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            ThrowTarget expected = new ThrowTarget(1, (bot.score - 40));
            testEquality(tt, expected);
        }
        // Case 6: Remaining Score 56-53, aims to setup D20 or D18
        for (; 53 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            List<Integer> possibleTargets = new ArrayList<>(Arrays.asList(bot.score - 40, bot.score - 36));
            testContains(tt.number, possibleTargets);
        }
        // Case 7: Remaining Score 52-41, aims to setup D20, D18, D16 or goes for bullseye
        for (; 41 <= bot.score; bot.score--) {
            tt = bot.getThrowTarget(false);
            if (bot.score == 50) {
                testEquality(tt, new ThrowTarget(2, 25));
            } else {
            List<Integer> possibleTargets = new ArrayList<>(Arrays.asList(bot.score - 40, bot.score - 36, bot.score - 32));
            testContains(tt.number, possibleTargets);
            }
        }
        // Case 8: Remaining Score 40-2 (evens), goes for Double
        for (; 2 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, new ThrowTarget(2, bot.score / 2));
        }
        // Case 9: Remaining Score 39-33 (odds), aims to setup D18, D16, D12
        bot.score = 39;
        for (; 33 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            List<Integer> possibleTargets = new ArrayList<>(Arrays.asList(bot.score - 36, bot.score - 32, bot.score - 24));
            testContains(tt.number, possibleTargets);
        }
        // Case 10: Remaining Score 31-25 (odds), aims to setup D12 or D8
        for (; 25 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            List<Integer> possibleTargets = new ArrayList<>(Arrays.asList(bot.score - 24, bot.score - 16));
            testContains(tt.number, possibleTargets);
        }
        // Case 11: Remaining Score 23-17 (odds), aims to setup D8
        for (; 17 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, new ThrowTarget(1, bot.score - 16));
        }
        // Case 12: Remaining Score 15-9 (odds), aims to setup D4
        for (; 9 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, new ThrowTarget(1, bot.score - 8));
        }
        // Case 13: Remaining Score 7-5 (odds), aims to setup D2
        for (; 5 <= bot.score; bot.score -= 2) {
            tt = bot.getThrowTarget(false);
            testEquality(tt, new ThrowTarget(1, bot.score - 4));
        }
        // Case 14: Remaining Score 3, aims to setup D1
        tt = bot.getThrowTarget(false);
        testEquality(tt, new ThrowTarget(1, 1));
    }

    private static void testOneLeg() {
        
        Player p1 = new Player("L. Humphries");
        Player p2 = new Player("L. Littler");
        Player p1Expected = new Player("L. Humphries");
        Player p2Expected = new Player("L. Littler");
        MatchLogic rules = new MatchLogic(501, 1, false, 0, true, false);
        MatchEngine match = new MatchEngine(p1, p2, rules);
        MatchEngine expectedMatch = new MatchEngine(p1Expected, p2Expected, rules);
        testEquality(match, expectedMatch);
        boolean ifDoubleOut = match.matchRules.ifDoubleOut();
        p1.dartThrow(180, ifDoubleOut, 0);
        p2.dartThrow(100, ifDoubleOut,0);
        p1Expected.score -= 180;
        p1Expected.stats.scores.add(180);
        p1Expected.stats.first9scores.add(180);
        p1Expected.stats.dartsThrown += 3;
        p1Expected.stats.dartsThrownLeg += 3;
        p2Expected.score -= 100;
        p2Expected.stats.scores.add(100);
        p2Expected.stats.first9scores.add(100);
        p2Expected.stats.dartsThrown += 3;
        p2Expected.stats.dartsThrownLeg += 3;
        testEquality(match, expectedMatch);
        
        p1.dartThrow(180, ifDoubleOut, 0);
        p2.dartThrow(177, ifDoubleOut, 0);
        p1Expected.score -= 180;
        p1Expected.stats.scores.add(180);
        p1Expected.stats.first9scores.add(180);
        p1Expected.stats.dartsThrown += 3;
        p1Expected.stats.dartsThrownLeg += 3;
        p2Expected.score -= 177;
        p2Expected.stats.scores.add(177);
        p2Expected.stats.first9scores.add(177);
        p2Expected.stats.dartsThrown += 3;
        p2Expected.stats.dartsThrownLeg += 3;
        testEquality(match, expectedMatch);

        p1.dartThrow(141, ifDoubleOut, 3);
        p1.stats.doublesAttempted++;
        p1Expected.score -= 141;
        p1Expected.stats.doublesAttempted++;
        p1Expected.stats.addCheckout(141 ,3);
        testEquality(match, expectedMatch);
        match.ifWonLeg().equals(p1);
        p1Expected.legs++;
        p2Expected.score = 0;
        testEquality(match, expectedMatch);
        
        testEquality(p1, p1Expected);
        testEquality(match.ifWinner(p1), match.ifWinner(p1Expected));
    }

    private static void testSetPlay() {
        //Make a sim one leg test that gets called on by the other ones
    }



    private static void runTests() {
        testPlayerMethods();
        testOneLeg();
        testSetPlay();
        testDartBotTargetLocating();
    }

    public static void main(String[] args) {

        runTests();
        printStatistics();
    }
}
