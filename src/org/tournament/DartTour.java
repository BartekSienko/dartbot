package org.tournament;

import java.util.*;
import org.match_engine.*;

public class DartTour {
    public String fileName;
    public List<String> tournaments;
    public List<DartPlayer> players;

    public DartTour(String fileName) {
        this.fileName = fileName;
        this.tournaments = new ArrayList<>();
        this.players = new ArrayList<>();
    }


    public List<DartPlayer> getTopXPlayers(int x) {
        List <DartPlayer> topXPlayers = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            topXPlayers.add(this.players.get(i));
        }
        return topXPlayers;
    }

    private class TotalOoMComparator implements Comparator<DartPlayer> {
        @Override 
        public int compare(DartPlayer firstPlayer, DartPlayer secondPlayer) {
            return -1 * Integer.compare(firstPlayer.prizeMoney.getTotalOoM(), secondPlayer.prizeMoney.getTotalOoM());
        }
    }

    public void sortByOoM() {
        this.players.sort(new TotalOoMComparator());
    }

    public List<DartPlayer> getOverallTopX (int x) {
        this.sortByOoM();
        return getTopXPlayers(x);
    }



    private class ProTourOoMComparator implements Comparator<DartPlayer> {
        @Override 
        public int compare(DartPlayer firstPlayer, DartPlayer secondPlayer) {
            return -1 * Integer.compare(firstPlayer.prizeMoney.getTotalProOoM(), secondPlayer.prizeMoney.getTotalProOoM());
        }
    }

    public void sortByProTourOoM() {
        this.players.sort(new ProTourOoMComparator());
    }

    public List<DartPlayer> getProTourTopX (int x) {
        this.sortByProTourOoM();
        return getTopXPlayers(x);
    }

    private class EuroTourOoMComparator implements Comparator<DartPlayer> {
        @Override 
        public int compare(DartPlayer firstPlayer, DartPlayer secondPlayer) {
            return -1 * Integer.compare(firstPlayer.prizeMoney.getTotalETOoM(), secondPlayer.prizeMoney.getTotalETOoM());
        }
    }

    public void sortByEuroTourOoM() {
        this.players.sort(new EuroTourOoMComparator());
    }

    public List<DartPlayer> getEuroTourTopX (int x) {
        this.sortByEuroTourOoM();
        return getTopXPlayers(x);
    }

    private class PCTourOoMComparator implements Comparator<DartPlayer> {
        @Override 
        public int compare(DartPlayer firstPlayer, DartPlayer secondPlayer) {
            return -1 * Integer.compare(firstPlayer.prizeMoney.getTotalPCOoM(), secondPlayer.prizeMoney.getTotalPCOoM());
        }
    }

    public void sortByPCOoM() {
        this.players.sort(new PCTourOoMComparator());
    }

    public List<DartPlayer> getPCTourTopX (int x) {
        this.sortByPCOoM();
        return getTopXPlayers(x);
    }

}
