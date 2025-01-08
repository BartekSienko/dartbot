package org;
import org.match_engine.MatchEngine;
import org.match_engine.MatchLogic;
import org.match_engine.Player;

public class Driver {
    public static void main(String[] args) {
        Player player1 = new Player("L. Humphries");
        Player player2 = new Player("L. Littler");
        MatchLogic rules = new MatchLogic(501, 2, true, 2, true, false);
        
        MatchEngine match = new MatchEngine(player1, player2, rules);
    
        int legCount = 0;
        boolean matchFinished = false;

        System.out.println("Game on!");
        while (!matchFinished) {
            if (match.matchRules.isSetPlay && player1.legs == 0 && player2.legs == 0) {
                legCount = 0;
            }
            System.out.println("Leg " + ++legCount + ":");
            match.playLeg();

            if (match.ifWinner(player1) != null) {
                System.out.println(player1.name + " has won the match!");
                matchFinished = true;
            } else if (match.ifWinner(player2) != null) {
                System.out.println(player2.name + " has won the match!");
                matchFinished = true;
            }
        }
        //match.toString();
        System.out.println("Match Stats:");
        System.out.println();
        System.out.println(player1.toStringStats());
        System.out.println();
        System.out.println(player2.toStringStats());
    }
}
