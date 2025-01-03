package org.match_engine;

public class MatchLogic {
    private final int startScore;
    private final int legLimit;
    //public int setLimit;
    private final boolean doubleOut;
    //public bool doubleIn;

    public MatchLogic(int score, int legs, boolean doubleOut) {
        this.startScore = score;
        this.legLimit = legs;
        this.doubleOut = doubleOut;
    }

    public int getStartScore() {
        return this.startScore;
    }
    
    public int getLegLimit() {
        return this.legLimit;
    }

    public boolean ifDoubleOut() {
        return this.doubleOut;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MatchLogic otherLogic) {
            return this.startScore == otherLogic.startScore &&
                   this.legLimit == otherLogic.legLimit &&
                   this.doubleOut == otherLogic.doubleOut;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.startScore + this.legLimit;
    }
    
}
