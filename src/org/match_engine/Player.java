package org.match_engine;

import java.util.*;


public class Player {
    public String name;
    public int score;
    public int legs;
    public int sets;
    public PlayerMatchStats stats;


    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.legs = 0;
        this.sets = 0;
        this.stats = new PlayerMatchStats();
    }

    public void dartThrow(int pointsScored, boolean isDoubleOut, int dartsAtCheckout) {
        if (this.score == 0) {
            this.stats.addCheckout(pointsScored, dartsAtCheckout);
        } else {
            this.stats.addScore(pointsScored, 3);
        }
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
        this.stats.doublesAttempted += dartsAtDouble;
        int dartsAtCheckout = this.visitCheckout(sc, pointsScored, dartsAtDouble);
        

        this.score -= pointsScored;
        this.dartThrow(pointsScored, isDoubleOut, dartsAtCheckout);

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

    public int visitCheckout(Scanner sc, int pointsScored, int dartsAtDouble) {
        if (this.score != pointsScored) {
            return 0;
        }
        Set<Integer> possibleDartsAtCheckout = getPoissbleDartsForCheckout(pointsScored, dartsAtDouble);
        if (possibleDartsAtCheckout.size() == 1) {
            return 3;
        } else {
            int dartsAtCheckout = 99;
            System.out.println("How many darts at checkout? " + possibleDartsAtCheckout.toString());
            while (!possibleDartsAtCheckout.contains(dartsAtCheckout)) {
                if (sc.hasNextInt()) {
                    dartsAtCheckout = sc.nextInt();
                } else {
                    sc.next();
                }
            }
            return dartsAtCheckout;
        }

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


    public Set<Integer> getPoissbleDartsForCheckout(int checkoutScore, int dartsAtDouble) {
        Set<Integer> onlyThreeDartOuts = new HashSet<>(Arrays.asList(109, 108, 106, 105, 104, 103, 102, 101, 99));
        if (checkoutScore > 110 || onlyThreeDartOuts.contains(checkoutScore)) {
            return new HashSet<>(Arrays.asList(3));
        } else if (checkoutScore != 50 && (checkoutScore > 40 || checkoutScore % 2 == 1)) {
            Set<Integer> result = new HashSet<>(Arrays.asList(2,3));
            // On a 2-dart finish, if 2 shots are needed on the double, the checkout has to take 3 darts
            if (dartsAtDouble == 2) {
                result.remove(2);
            }
            return result;
        } else {
            switch (dartsAtDouble) {
                case 3 -> { // If we had 3 darts at double, the checkout took 3 darts
                    return new HashSet<>(Arrays.asList(3));
                }
                case 2 -> { // If we had 2 darts at double, the checkout could take 3 darts (double, setup, double)
                    return new HashSet<>(Arrays.asList(2, 3));
                }
                case 1 -> { // If we needed 1 dart at double, the checkout took only 1 dart
                    return new HashSet<>(Arrays.asList(1));
                }
                default -> throw new IllegalArgumentException("dartsAtDouble wasn't 1,2 or 3");
            }
    
        }
        
    }


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

    public void updateBestWorstLegs() {
        int dartsThrownLeg = this.stats.dartsThrownLeg;
        PlayerMatchStats legStats = this.stats;
        if (legStats.bestLeg == 0 && legStats.worstLeg == 0) {
            legStats.bestLeg = dartsThrownLeg;
            legStats.worstLeg = dartsThrownLeg;
        } else if (legStats.bestLeg > dartsThrownLeg) {
            legStats.bestLeg = dartsThrownLeg;
        } else if (legStats.worstLeg < dartsThrownLeg) {
            legStats.worstLeg = dartsThrownLeg;
        }
    }

    @Override
    public String toString() {
        return "(" + this.legs + ")  " + this.score + "  " + this.name;
    }

    public String toStringSetPlay() {
        return "(" + this.sets + ") " + "(" + this.legs + ")  " + this.score + "  " + this.name;
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
