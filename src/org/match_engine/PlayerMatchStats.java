package org.match_engine;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStats {
    public List<Integer> scores;
    public List<Integer> first9scores;
    public List<Integer> checkouts;
    public int dartsThrown;
    public int dartsThrownLeg;
    public int doublesAttempted;
    public int doublesSucceeded;
    public int bestLeg;
    public int worstLeg;

    public PlayerMatchStats() {
        this.scores = new ArrayList<>();
        this.first9scores = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.dartsThrownLeg = 0;
        this.dartsThrown = 0;
        this.doublesAttempted = 0;
        this.doublesSucceeded = 0;
        this.bestLeg = 0;
        this.worstLeg = 0;
        
    }


    public void addScore(int pointsScored, int dartsThrown) {
        this.scores.add(pointsScored);
        if (this.dartsThrownLeg < 9) {
            this.first9scores.add(pointsScored);
        }
        this.dartsThrown += dartsThrown;
        this.dartsThrownLeg += dartsThrown;
    }

    public void addCheckout(int pointsScored, int dartsAtCheckout) {
        this.scores.add(pointsScored);
        if (this.dartsThrownLeg < 9) {
            this.first9scores.add(pointsScored);
        }
        this.doublesSucceeded++;
        this.dartsThrown += dartsAtCheckout;
        this.dartsThrownLeg += dartsAtCheckout;
        this.checkouts.add(pointsScored);
    }

    public double getListAverage(List<Integer> list) {
        double sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return Math.round(sum / list.size() * 10) / 10;
    }


    public String getCheckoutSplit() {
        return this.doublesSucceeded + "/" + this.doublesAttempted;
    }

    public double getCheckoutRate() {
        if (this.doublesAttempted == 0) {
            return 0;
        }
        return Math.round(((double)this.doublesSucceeded / this.doublesAttempted) * 10000) / 100;
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
        boolean first9scoresCheck = this.first9scores.equals(other.first9scores);
        boolean checkoutsCheck = this.checkouts.equals(other.checkouts);
        boolean dartsThrownCheck = this.dartsThrown == other.dartsThrown;
        boolean doublesAttemptedCheck = this.doublesAttempted == other.doublesAttempted;
        boolean doublesSucceededCheck = this.doublesSucceeded == other.doublesSucceeded;
        boolean bestLegCheck = this.bestLeg == other.bestLeg;
        boolean worstLegCheck = this.worstLeg == other.worstLeg;

        return scoresCheck && first9scoresCheck && checkoutsCheck && dartsThrownCheck && doublesAttemptedCheck 
               && doublesSucceededCheck && bestLegCheck && worstLegCheck;
    }

    @Override
    public int hashCode() {
        return this.scores.hashCode() + this.checkouts.hashCode() + this.dartsThrown
               + this.doublesAttempted + this.doublesSucceeded;
    }

    @Override
    public String toString() {
        return "\n3-dart Average: " + (this.getListAverage(scores)) + "\nFirst 9 avr.: " + this.getListAverage(first9scores)
             + "\nCheckout Rate: " + this.getCheckoutRate() + " %" + "\nCheckouts: " + this.getCheckoutSplit()
             + "\nHighest Score: " + this.getHighestFromList(scores) + "\nHighest Checkout: " + this.getHighestFromList(checkouts)
             + "\nBest Leg: " + this.bestLeg + " darts" + "\nWorst Leg: " + this.worstLeg + " darts";
    }
}
