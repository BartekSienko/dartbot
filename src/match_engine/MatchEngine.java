package match_engine;

public class MatchEngine {
    public Player player1;
    public Player player2;
    private final MatchLogic matchRules;
    public int onThrow;


    public MatchEngine(Player player1, Player player2, MatchLogic matchRules) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchRules = matchRules;
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

        while (this.player1.score > 0 && this.player2.score > 0) {
            if (playerToThrow == 1) {
                this.player1.visitThrow();
                playerToThrow = 2;
            } else {
                this.player2.visitThrow();
                playerToThrow = 1;
            }
            System.out.println(this.player1.toString());
            System.out.println(this.player2.toString());
            System.out.println();

        }

        if (this.player1.score <= 0) {
            this.player1.legs++;
        } else {
            this.player2.legs++;
        }
    }


    public Player ifWinner(Player player) {
        if (player.legs >= matchRules.getLegLimit()) {
            return player;
        }
        return null;
    }

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
        System.out.println("Result: " + player1.name + " " + player1.legs + 
                           ":" + player2.legs + " " + player2.name);
        
    }
}