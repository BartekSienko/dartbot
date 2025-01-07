package org.match_engine;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStats {
    public List<Integer> scores;
    public List<Integer> checkouts;
    public int dartsThrown;
    public int dartsThrownLeg;
    public int doublesAttempted;
    public int doublesSucceeded;

    public PlayerMatchStats() {
        this.scores = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.dartsThrownLeg = 0;
        this.dartsThrown = 0;
        this.doublesAttempted = 0;
        this.doublesSucceeded = 0;
    }


    public void addScore(int pointsScored, int dartsThrown, int dartsAtDouble) {
        this.dartsThrown += dartsThrown;
        this.dartsThrownLeg += dartsThrown;
        this.doublesAttempted += dartsAtDouble;
        this.scores.add(pointsScored);
    }

    public void addCheckout(int pointsScored, int dartsAtDouble, int dartsAtCheckout) {
        this.doublesSucceeded++;
        this.doublesAttempted += dartsAtDouble;
        this.dartsThrown += dartsAtCheckout;
        this.dartsThrownLeg += dartsAtCheckout;
        this.scores.add(pointsScored);
        this.checkouts.add(pointsScored);
    }

    public double getMatchAverage() {
        double sum = 0;
        for (Integer i : this.scores) {
            sum += i;
        }
        return (sum / this.dartsThrown) * 3;
    }

    public String getCheckoutSplit() {
        return this.doublesSucceeded + "/" + this.doublesAttempted;
    }

    public double getCheckoutRate() {
        if (this.doublesAttempted == 0) {
            return 0;
        }
        return ((double)this.doublesSucceeded / this.doublesAttempted) * 100;
    }

    public int getHighestFromList(List<Integer> list) {
        int highest = 0;
        for (Integer i : list) {
            if (highest < i) {
                highest = i;
            }
        }
        return highest;
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
        boolean doublesAttemptedCheck = this.doublesAttempted == other.doublesAttempted;
        boolean doublesSucceededCheck = this.doublesSucceeded == other.doublesSucceeded;

        return scoresCheck && checkoutsCheck && dartsThrownCheck && doublesAttemptedCheck && doublesSucceededCheck;
    }

    @Override
    public int hashCode() {
        return this.scores.hashCode() + this.checkouts.hashCode() + this.dartsThrown
               + this.doublesAttempted + this.doublesSucceeded;
    }

    @Override
    public String toString() {
        return "\n3-dart Average: " + this.getMatchAverage() + "\nFirst 9 avr.: 0"
             + "\nCheckout Rate: " + this.getCheckoutRate() + " %" + "\nCheckouts: " + this.getCheckoutSplit()
             + "\nHighest Score: " + this.getHighestFromList(scores) + "\nHighest Checkout: " + this.getHighestFromList(checkouts);
    }
}
