package org.tournament;

import java.util.*;

public class OverallPrizeMoney {
    private final HashMap<String, Integer> currentSeason;
    private final HashMap<String, Integer> lastSeason;
    private int totalOoM;
    private int totalProOoM;
    private int totalPCOoM;
    private int totalETOoM;
    
    public OverallPrizeMoney() {
        this.currentSeason = new HashMap<>();
        this.lastSeason = new HashMap<>();
        this.totalOoM = 0;
        this.totalProOoM = 0;
        this.totalPCOoM = 0;
        this.totalETOoM = 0;
    }

    public HashMap<String, Integer> getCurSeason() {
        return this.currentSeason;
    }
    
    public HashMap<String, Integer> getLastSeason() {
        return this.lastSeason;
    }

    public int getTotalOoM() {
        return this.totalOoM;
    }

    public int getTotalProOoM() {
        return this.totalProOoM;
    }

    public int getTotalPCOoM() {
        return this.totalPCOoM;
    }

    public int getTotalETOoM() {
        return this.totalETOoM;
    }

    public void addPrizeMoney(PrizeMoney pm) {
        String tournament = pm.getTournament();
        int prize = pm.getPrize();

        //Takes current prize money for said event and moves it to
        //last seasons prize money as that season concluded
        int lostPrizeMoney = 0;
        if (this.currentSeason.containsKey(tournament)) {
            lostPrizeMoney = this.lastSeason.put(tournament, this.currentSeason.get(tournament));
        } else {
            this.lastSeason.put(tournament, 0);
        }
        this.currentSeason.put(tournament, prize);

        this.totalOoM -= lostPrizeMoney;
        this.totalOoM += prize;

        if (tournament.contains("PC") && !tournament.equals("PC Finals")) {
            this.totalProOoM -= lostPrizeMoney;
            this.totalProOoM += prize;
            this.totalPCOoM -= lostPrizeMoney;
            this.totalPCOoM += prize;
        } else if (tournament.contains("BT") && !tournament.equals("BT Finals")) {
            this.totalProOoM -= lostPrizeMoney;
            this.totalProOoM += prize;
            this.totalETOoM -= lostPrizeMoney;
            this.totalETOoM += prize;
        }
    }

    
}
