package org.match_engine;

public class MatchLogic {
    private final int startScore;
    private final int legLimit;
    public boolean isSetPlay;
    private final int setLimit;
    private final boolean doubleOut;
    private final boolean doubleIn;

    public MatchLogic(int score, int legs, boolean isSetPlay, int sets, boolean doubleOut, boolean doubleIn) {
        this.startScore = score;
        this.legLimit = legs;
        this.isSetPlay = isSetPlay;
        this.setLimit = sets;
        this.doubleOut = doubleOut;
        this.doubleIn = doubleIn;
    }

    public int getStartScore() {
        return this.startScore;
    }
    
    public int getLegLimit() {
        return this.legLimit;
    }

    public int getSetLimit() {
        return this.setLimit;
    }

    public boolean ifDoubleOut() {
        return this.doubleOut;
    }

    public boolean ifDoubleIn() {
        return this.doubleIn;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MatchLogic otherLogic) {
            return this.startScore == otherLogic.startScore &&
                   this.legLimit == otherLogic.legLimit &&
                   this.isSetPlay == otherLogic.isSetPlay &&
                   this.setLimit == otherLogic.setLimit &&
                   this.doubleOut == otherLogic.doubleOut &&
                   this.doubleIn == otherLogic.doubleIn;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.startScore + this.legLimit;
    }
    
}
