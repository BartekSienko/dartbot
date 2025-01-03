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

    public void dartThrow(int pointsScored, boolean isDoubleOut) {
        if (this.score == pointsScored) {
            this.stats.addCheckout(pointsScored, 1);
            // TODO: Stub, could throw in less than 3
            this.score = 0;
        } else {
            this.score = this.score - pointsScored;
        }
        this.stats.addScore(pointsScored, 3);
    }


    public void visitThrow(Scanner sc, boolean isDoubleOut) {
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
                legalScore = legalScore && checkLegalCheckout(pointsScored);
            }
        }

        this.dartThrow(pointsScored, isDoubleOut);

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

    public boolean checkLegalCheckout(int pointsScored) {
        Set<Integer> impossibleCheckouts = new HashSet<>(Arrays.asList( 168, 165, 162, 159));
        if (impossibleCheckouts.contains(pointsScored)) {
            System.out.println("Impossible checkout inputed");
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return "(" + this.legs + ")  " + this.score + "  " + this.name;
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
