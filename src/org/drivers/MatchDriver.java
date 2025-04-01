package org.drivers;
import java.util.Scanner;
import org.match_engine.*;


public class MatchDriver {
    private final MatchEngine match;

    public MatchDriver(DartPlayer player1, DartPlayer player2, MatchLogic rules) {
        this.match = new MatchEngine(player1, player2, rules);
    }


    public DartPlayer runMatch(boolean ifQuickSim) {
        DartPlayer winner = null;
        int legCount = 0;
        boolean matchFinished = false;

        System.out.println("Game on!");
        while (!matchFinished) {
            if (match.matchRules.isSetPlay && match.player1.legs == 0 && match.player2.legs == 0) {
                legCount = 0;
            }
            if (!ifQuickSim) System.out.println("Leg " + ++legCount + ":");
            match.playLeg(ifQuickSim);

            if (match.ifWinner(match.player1) != null) {
                System.out.println(match.player1.name + " has won the match!");
                matchFinished = true;
                winner = match.player1;
            } else if (match.ifWinner(match.player2) != null) {
                System.out.println(match.player2.name + " has won the match!");
                matchFinished = true;
                winner = match.player2;
            }
        }

        if (!ifQuickSim) {
        System.out.println("Match Stats:");
        System.out.println();
        System.out.println(match.player1.toStringStats());
        System.out.println();
        System.out.println(match.player2.toStringStats());
        }
        return winner;
    }

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        System.out.print("Input Player 1 name: ");
        DartPlayer player1 = new DartPlayer(sc.nextLine(), 10);
        System.out.print("Input Player 2 name: ");
        DartPlayer player2 = new DartPlayer(sc.nextLine(), 10);

        MatchDriver md = new MatchDriver(player1, player2, new MatchLogic(sc));
        
        md.runMatch(false);
    }
}
