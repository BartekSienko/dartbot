package match_engine;

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

    public void visitThrow() {
        int pointsScored = 125;
        /// TODO: Remove stub with actual throwing
        if (this.score <= 130) {
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
