package org.tournament;

import java.util.*;
import org.drivers.MatchDriver;
import org.match_engine.*;
import org.match_engine.dartbot.*;

public class Tournament {
    public String name;
    private int playerCount;
    private final List<Deque<DartPlayer>> players;
    private final List<DartPlayer> eliminated;
    private final List<MatchLogic> rulesets;
    private final List<Integer> prize_money;
    
    public Tournament(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets, List<Integer> prize_money) {
        this.name = name;
        this.playerCount = pCount;
        this.players = players;
        this.eliminated = new ArrayList<>();
        this.rulesets = rulesets;
        this.prize_money = prize_money;
    }

    public void simRound(Scanner sc, int roundNr, Deque<DartPlayer> competingPlayers, 
                    Deque<DartPlayer> nextRound) {
        String roundName;
        if (playerCount == 8) {
            roundName = "Quarter Final";
        } else if (playerCount == 4) {
            roundName = "Semi-Final";
        } else if (playerCount == 2) {
            roundName = "Final";
        } else {
            roundName = roundNr + "";
        }

        System.out.println("-------------");
        System.out.println("Current Round: " + roundName);
        int matchCount = competingPlayers.size() / 2;
        this.printMatchList(competingPlayers, matchCount);
        for (int i = 0; i < matchCount; i++) {
            DartPlayer p1 = competingPlayers.removeFirst();
            DartPlayer p2 = competingPlayers.removeLast();
            System.out.println("-------------");
            this.printMatchInfo(p1.name, p2.name);
            int input = this.getIntInRange(sc, 0, 3);
            switch(input) {
                case 0:
                    p1 = new DartBot(p1.name, p1.rating);
                    p2 = new DartBot(p2.name, p2.rating);
                    break;
                case 1:
                    p2 = new DartBot(p2.name, p2.rating);
                    break;
                case 2:
                    p1 = new DartBot(p1.name, p1.rating);
                    break;
            }
            MatchDriver matchDriver = new MatchDriver(p1, p2, rulesets.get(roundNr-1));
            DartPlayer winner = matchDriver.runMatch();
            if (p1.equals(winner)) {
                nextRound.add(p1);
                eliminated.addFirst(p2);
            } else {
                nextRound.add(p2);
                eliminated.addFirst(p1);
            }
            this.playerCount--;
        }
        if (nextRound.size() == 1) {
            eliminated.addFirst(nextRound.getFirst());
        } else {
            Deque<DartPlayer> followingRound;
            if (this.players.size() > (roundNr + 1)) {
                followingRound = this.players.get(roundNr + 1);
            } else {
                followingRound = new ArrayDeque<>();
            }
            simRound(sc, ++roundNr, nextRound, followingRound);
        }  

    }

    public int getIntInRange(Scanner sc, int minLimit, int maxLimit) {
        int input = -1;
        while (input < minLimit || input > maxLimit) {
            if (sc.hasNextInt()) {
                input = sc.nextInt();
            } else {
                sc.next();
            }
        }
        return input;
    }

    public void printMatchInfo(String name1, String name2) {
        System.out.println("Match: " + name1 + " vs " + name2);
        System.out.println("Select option: ");
        System.out.println("0: Sim Match (Bot vs Bot)");
        System.out.println("1: Play as " + name1);
        System.out.println("2: Play as " + name2);
        System.out.println("3: Play as both players");
    }

    public void printMatchList(Deque<DartPlayer> players, int matchCount) {
        Deque<DartPlayer> copy = new ArrayDeque<>();
        for (DartPlayer p : players) {
            copy.add(p);
        }
        for (int i = 0; i < matchCount; i++) {
            System.out.println("Match " + (i+1) + ": " + copy.removeFirst().name + " vs " + copy.removeLast().name);
        }
    }

    public static void main(String[] args) {
        DartPlayer p1 = new DartPlayer("L. Humphries", 10);
        DartPlayer p2 = new DartPlayer("L. Littler", 10);
        DartPlayer p3 = new DartPlayer("M. van Gerwen", 9);
        DartPlayer p4 = new DartPlayer("R. Cross", 8);
        DartPlayer p5 = new DartPlayer("S. Bunting", 8);
        DartPlayer p6 = new DartPlayer("D. Chisnall", 7);
        DartPlayer p7 = new DartPlayer("G. Price", 7);



        Deque<DartPlayer> round1 = new ArrayDeque<>(Arrays.asList(p6, p7));
        Deque<DartPlayer> round2 = new ArrayDeque<>(Arrays.asList(p3, p4, p5));
        Deque<DartPlayer> round3 = new ArrayDeque<>(Arrays.asList(p1, p2));
        

        MatchLogic rules1 = new MatchLogic(501, 3, false, 0, true, false);
        MatchLogic rules2 = new MatchLogic(501, 4, false, 0, true, false);
        MatchLogic rules3 = new MatchLogic(501, 5, false, 0, true, false);
        MatchLogic rules4 = new MatchLogic(501, 6, false, 0, true, false);

        ArrayList<Deque<DartPlayer>> players = new ArrayList<>(Arrays.asList(round1, round2, round3));     
        ArrayList<MatchLogic> rulesets = new ArrayList<>(Arrays.asList(rules1, rules2, rules3, rules4));
        
        Tournament testCup = new Tournament("Test Cup", 7, players, rulesets, null);

        Scanner sc = new Scanner(System.in);

        testCup.simRound(sc, 1, round1, round2);
        
    }


}
