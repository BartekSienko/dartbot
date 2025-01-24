package org.tournament;



public class PrizeMoney {
    private final String tournament;
    private final int prize;

    public PrizeMoney(String tournament, int prize) {
        this.tournament = tournament;
        this.prize = prize;
    }

    public String getTournament() {
        return this.tournament;
    }

    public int getPrize() {
        return this.prize;
    }


    @Override
    public String toString() {
        return this.tournament + " (" + this.prize + ")";
    }
}
