package org.match_engine;

import java.util.Scanner;

public class MatchEngine {
    public Player player1;
    public Player player2;
    private final MatchLogic matchRules;
    public int onThrow;


    public MatchEngine(Player player1, Player player2, MatchLogic matchRules) {
        this.matchRules = matchRules;
        this.player1 = player1;
        this.player1.score = this.matchRules.getStartScore();
        this.player2 = player2;
        this.player2.score = this.matchRules.getStartScore();
        this.onThrow = 1;
    }


    public void newLeg() {
        this.player1.score = matchRules.getStartScore();
        this.player2.score = matchRules.getStartScore();
        if (this.onThrow == 1) {
            this.onThrow = 2;
        } else {
            this.onThrow = 1;
        }
    }


    public void playLeg() {
        this.newLeg();
        int playerToThrow = this.onThrow;
        Scanner sc = new Scanner(System.in);
        while (this.player1.score > 0 && this.player2.score > 0) {
            if (playerToThrow == 1) {
                this.player1.visitThrow(sc);
                playerToThrow = 2;
            } else {
                this.player2.visitThrow(sc);
                playerToThrow = 1;
            }
            System.out.println(this.player1.toString());
            System.out.println(this.player2.toString());
            System.out.println();
            
        }

        if (this.ifWonLeg() != null) {
            if (this.ifWonLeg().equals(this.player1)) {
                System.out.println(this.player1.name + " has won the leg!");
            } else {
                System.out.println(this.player2.name + " has won the leg!");
            }
            System.out.println(this.toString());
        }

    }

    public Player ifWonLeg() {
        if (this.player1.score <= 0) {
            this.player1.legs++;
            return this.player1;
        } else if (this.player2.score <= 0) {
            this.player2.legs++;
            return this.player2;
        }
        return null;
    }

    public Player ifWinner(Player player) {
        if (player.legs >= matchRules.getLegLimit()) {
            return player;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Current Result: " + player1.name + " " + player1.legs + " (" + player1.score + ")" +
                           " : " + "(" + player2.score + ") " + player2.legs + " " + player2.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MatchEngine otherMatch) {
            return this.player1.equals(otherMatch.player1) &&
                   this.player2.equals(otherMatch.player2) &&
                   this.matchRules.equals(otherMatch.matchRules);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.player1.hashCode() + this.player2.hashCode();
    }
}