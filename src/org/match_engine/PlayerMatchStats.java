package org.match_engine;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStats {
    public List<Integer> scores;
    public List<Integer> checkouts;
    public int dartsThrown;
    public int checkoutsAttempted;
    public int checkoutsSuceeded;

    public PlayerMatchStats() {
        this.scores = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.dartsThrown = 0;
        this.checkoutsAttempted = 0;
        this.checkoutsSuceeded = 0;
    }


    public void addScore(int pointsScored, int dartsThrown) {
        this.dartsThrown += dartsThrown;
        this.scores.add(pointsScored);
    }

    public void addCheckout(int pointsScored, int dartsAtDouble) {
        this.checkoutsSuceeded++;
        this.checkoutsAttempted += dartsAtDouble;
        this.checkouts.add(pointsScored);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PlayerMatchStats playerStats) {
            return this.equals(playerStats);
        }
        return false;
    }

    public boolean equals(PlayerMatchStats other) {
        boolean scoresCheck = this.scores.equals(other.scores);
        boolean checkoutsCheck = this.checkouts.equals(other.checkouts);
        boolean dartsThrownCheck = this.dartsThrown == other.dartsThrown;
        boolean checkoutsAttemptedCheck = this.checkoutsAttempted == other.checkoutsAttempted;
        boolean checkoutsSuceededCheck = this.checkoutsSuceeded == other.checkoutsSuceeded;

        return scoresCheck && checkoutsCheck && dartsThrownCheck && checkoutsAttemptedCheck && checkoutsSuceededCheck;
    }

    @Override
    public int hashCode() {
        return this.scores.hashCode() + this.checkouts.hashCode() + this.dartsThrown
               + this.checkoutsAttempted + this.checkoutsSuceeded;
    }
}
