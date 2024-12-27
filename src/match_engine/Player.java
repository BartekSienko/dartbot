package match_engine;

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



    private int getScore(Scanner sc) {
        Set<Integer> impossibleScores = new HashSet<>(Arrays.asList(179, 178, 176, 175, 173, 172, 169, 166, 163));
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
            }
            if (impossibleScores.contains(pointsScored) || pointsScored > 181) {
                System.out.println("Impossible score inputed");
                legalScore = false;
            } else if (pointsScored > this.score) {
                System.out.println("BUST! Can't score more than is left!");
                pointsScored = 0;
            }
        }
        return pointsScored;
    }

    public void visitThrow(Scanner sc) {
        int pointsScored = getScore(sc);
        if ((this.score - pointsScored) == 0) {
            //Stub checkout
            pointsScored = this.score;
            this.stats.addCheckout(pointsScored, 1);
            // Stub, could throw less than 3
            this.stats.addScore(pointsScored, 3);
            this.score = 0;
        } else {
            this.score = this.score - pointsScored;
            this.stats.addScore(pointsScored, 3);
        }
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
