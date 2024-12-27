import match_engine.MatchEngine;
import match_engine.MatchLogic;
import match_engine.Player;

public class Driver {
    public static void main(String[] args) {
        Player player1 = new Player("L. Humphries");
        Player player2 = new Player("L. Littler");
        MatchLogic rules = new MatchLogic(501, 2, true);
        
        MatchEngine match = new MatchEngine(player1, player2, rules);
    
        int legCount = 0;
        boolean matchFinished = false;

        System.out.println("Game on!");
        while (!matchFinished) {

            match.playLeg();

            if (match.ifWinner(player1) != null) {
                System.out.println(player1.name + " has won the match!");
                matchFinished = true;
            } else if (match.ifWinner(player2) != null) {
                System.out.println(player2.name + " has won the match!");
                matchFinished = true;
            }
        }
        match.toString();
        
    }
}
