package org.match_engine;

import java.util.*;


public class Player {
    public String name;
    public int score;
    public int legs;
    //public int sets;
    public PlayerMatchStats stats;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.legs = 0;
        this.stats = new PlayerMatchStats();
    }

    public void dartThrow(int pointsScored, boolean isDoubleOut, int dartsAtDouble) {
        int remainingScore = this.score - pointsScored;
        if (remainingScore == 0) {
            this.stats.addCheckout(pointsScored, dartsAtDouble);
            // TODO: Stub, could throw in less than 3
        } else if (remainingScore <= 40) {
            this.stats.addScore(pointsScored, 3, dartsAtDouble);
        } else {
            this.stats.addScore(pointsScored, 3, dartsAtDouble);
        }
        this.score = remainingScore;
        //this.stats.addScore(pointsScored, 3);
    }


    public void visitThrow(Scanner sc, boolean isDoubleOut, boolean isDoubleIn) {
        int pointsScored = 0;
        boolean legalScore = false;
        System.out.println(this.name + " to throw: ");
        while (!legalScore) {
            if (sc.hasNextInt()) {
                pointsScored = sc.nextInt();
                legalScore = true;
            } else {
                sc.next();
                legalScore = false;
                continue;
            }
            legalScore = legalScore && checkLegalScore(pointsScored, isDoubleOut);
            if (this.score == pointsScored) {
                legalScore = legalScore && checkLegalDoubleScore(pointsScored, true);
            } else if (this.stats.dartsThrownLeg == 0 && isDoubleIn) {
                legalScore = legalScore && checkLegalDoubleScore(pointsScored, false);
            }
        }

        int dartsAtDouble = this.visitDoubles(sc, pointsScored);


        this.dartThrow(pointsScored, isDoubleOut, dartsAtDouble);

    }

    public int visitDoubles(Scanner sc, int pointsScored) {
        //Set<Integer> possibleDartsAtCheckout = getPoissbleDartsForCheckout(pointsScored);
        Set<Integer> possibleDartsAtDouble = getPossibleDartsAtDouble(pointsScored);
        if ((this.score - pointsScored) >= 50) {
            return 0;
        } else if (this.score - pointsScored == 0) {
            possibleDartsAtDouble.remove(0);
        }
        int dartsAtDouble = 99;
        if (possibleDartsAtDouble.size() == 1) {
            dartsAtDouble = (Integer)possibleDartsAtDouble.toArray()[0];
        } 
        else {
            System.out.println("How many darts at double? " + possibleDartsAtDouble.toString());
            while (!possibleDartsAtDouble.contains(dartsAtDouble)) {
                if (sc.hasNextInt()) {
                    dartsAtDouble = sc.nextInt();
                } else {
                    sc.next();
                }
            }
        }
        return dartsAtDouble;
    }


    public boolean checkLegalScore(int pointsScored, boolean isDoubleOut) {
        Set<Integer> impossibleScores = new HashSet<>(Arrays.asList(179, 178, 176, 175, 173, 172, 169, 166, 163));
        if (impossibleScores.contains(pointsScored) || pointsScored > 180) {
            System.out.println("Impossible score inputed");
            return false;
        } else if (pointsScored > this.score) {
            System.out.println("BUST! Too large score inputed (please input 0)");
            return false;
        } else if (isDoubleOut && (this.score - pointsScored == 1)) {
            System.out.println("BUST! You cannot checkout 1 (please input 0)");
            return false;
        }
        return true;
    }

    public boolean checkLegalDoubleScore(int pointsScored, boolean outNotIn) {
        Set<Integer> impossibleCheckouts = new HashSet<>(Arrays.asList( 168, 165, 162, 159));
        if (impossibleCheckouts.contains(pointsScored) || pointsScored > 170) {
            if (outNotIn) {
                System.out.println("Impossible checkout inputed");
            } else {
                System.out.println("Impossible start score inputed");
            }
            return false;
        }
        return true;
    }    

/**
    public Set<Integer> getPoissbleDartsForCheckout(int checkoutScore) {
        Set<Integer> onlyThreeDartOuts = new HashSet<>(Arrays.asList(109, 108, 106, 105, 104, 103, 102, 101, 99));
        if (checkoutScore > 110 || onlyThreeDartOuts.contains(checkoutScore)) {
            return new HashSet<>(Arrays.asList(3));
        } else if (checkoutScore != 50 && (checkoutScore > 40 || checkoutScore % 2 == 1)) {
            return new HashSet<>(Arrays.asList(2,3));
        } else {
            return new HashSet<>(Arrays.asList(1,2,3));
        }
    }
*/

    public Set<Integer> getPossibleDartsAtDouble(int pointsScored) {
        Set<Integer> onlyThreeDartOuts = new HashSet<>(Arrays.asList(109, 108, 106, 105, 104, 103, 102, 101, 99));
        if (pointsScored > 110 || onlyThreeDartOuts.contains(pointsScored)) {
            return new HashSet<>(Arrays.asList(0,1));
        } else if (pointsScored != 50 && (pointsScored > 40 || pointsScored % 2 == 1)) {
            return new HashSet<>(Arrays.asList(0,1,2));
        } else {
            return new HashSet<>(Arrays.asList(0,1,2,3));
        }
    }

    @Override
    public String toString() {
        return "(" + this.legs + ")  " + this.score + "  " + this.name;
    }

    public String toStringStats() {
        return this.name + this.stats.toString();
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Player player) {
            return this.equals(player);
        }
        return false;
    }

    public boolean equals(Player other) {
        boolean nameCheck = this.name.equals(other.name);
        boolean scoreCheck = this.score == other.score;
        boolean legCheck = this.legs == other.legs;
        boolean setCheck = true; //this.sets == other.sets;
        boolean statCheck = this.stats.equals(other.stats);
        return nameCheck && scoreCheck && legCheck && setCheck && statCheck;
    }
}
