package org.tournament;

import java.util.*;
import org.drivers.MatchDriver;
import org.match_engine.*;
import org.match_engine.dartbot.*;
import org.tours.PrizeMoney;

public class Tournament {
    public String name;
    private int playerCount;
    private final List<Deque<DartPlayer>> players;
    private final List<DartPlayer> eliminated;
    private final List<MatchLogic> rulesets;
    private final List<Integer> prizeMoney;
    private int roundMatchNumber;
    
    public Tournament(String name, int pCount, ArrayList<Deque<DartPlayer>> players,
                      List<MatchLogic> rulesets, List<Integer> prizeMoney) {
        this.name = name;
        this.playerCount = pCount;
        this.players = players;
        this.eliminated = new ArrayList<>();
        this.rulesets = rulesets;
        this.prizeMoney = prizeMoney;
        this.roundMatchNumber = 0;
    }

    public void simTournament() {
        Scanner sc = new Scanner(System.in);
        Deque<DartPlayer> round1 = this.players.get(0);
        Deque<DartPlayer> round2;
        try {
            round2 = this.players.get(1);
        } catch (IndexOutOfBoundsException ie) {
            round2 = new ArrayDeque<>();
        }

        this.startRound(sc, 1, round1, round2);
        
        this.generatePrizeMoney();
        if (this.eliminated.size() > 0) {
            System.out.println("The winner of " + this.name + " is " + this.eliminated.get(0).name + "!");
        }

    } 

    public void startRound(Scanner sc, int roundNr, Deque<DartPlayer> competingPlayers, 
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
        for (int i = this.roundMatchNumber; i < matchCount; i++) {
            this.roundMatchNumber++;
            DartPlayer p1 = competingPlayers.removeFirst();
            DartPlayer p2 = competingPlayers.removeLast();
            System.out.println("-------------");
            this.printMatchInfo(p1.name, p2.name);
            DartPlayer p1Playing = null;
            DartPlayer p2Playing = null;
            int input = this.getIntInRange(sc, 0, 5);
            boolean ifQuickSim = false;
            switch(input) {
                case 0:
                    this.saveTournament(roundNr);
                    return;
                case 1:
                    p1Playing = new DartPlayer(p1.name, p1.rating);
                    p2Playing = new DartBot(p2.name, p2.rating);
                    break;
                case 2:
                    p1Playing = new DartBot(p1.name, p1.rating);
                    p2Playing = new DartPlayer(p2.name, p2.rating);
                    break;
                case 3:
                    p1Playing = new DartPlayer(p1.name, p1.rating);
                    p2Playing = new DartPlayer(p2.name, p2.rating);
                    break;
                case 4:
                    p1Playing = new DartBot(p1.name, p1.rating);
                    p2Playing = new DartBot(p2.name, p2.rating);
                    break;
                case 5:
                    p1Playing = new DartBot(p1.name, p1.rating);
                    p2Playing = new DartBot(p2.name, p2.rating);
                    ifQuickSim = true;
                    break;
            }
            MatchDriver matchDriver = new MatchDriver(p1Playing, p2Playing, rulesets.get(roundNr-1));
            DartPlayer winner = matchDriver.runMatch(ifQuickSim);
            if (p1Playing.equals(winner)) {
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
            this.roundMatchNumber = 0;
            startRound(sc, ++roundNr, nextRound, followingRound);
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
        System.out.println("0: Go Back");
        System.out.println("1: Play as " + name1);
        System.out.println("2: Play as " + name2);
        System.out.println("3: Play as both players");
        System.out.println("4: Sim Match (Bot vs Bot)");
        System.out.println("5: Quick Sim Match (Bot vs Bot)");
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

    public void generatePrizeMoney() {
        if (this.prizeMoney == null) {
            return;
        }
        int playersTotal = this.eliminated.size();
        this.eliminated.get(0).prizeMoney.addPrizeMoney(new PrizeMoney(this.name, this.prizeMoney.get(0)));
        for (int i = 1; i < playersTotal; i++) {
            int pm_index = (int)(Math.log(i) / Math.log(2));
            PrizeMoney pm = new PrizeMoney(this.name, this.prizeMoney.get(pm_index + 1));
            DartPlayer player = this.eliminated.get(i);
            player.prizeMoney.addPrizeMoney(pm);
        }
    }


    public void saveTournament(int roundNr) {
        
    }



    public static void main(String[] args) {

        DartPlayer p1 = new DartPlayer("L. Humphries", 10.0);
        DartPlayer p2 = new DartPlayer("L. Littler", 10.0);
        DartPlayer p3 = new DartPlayer("M. van Gerwen", 9.0);
        DartPlayer p4 = new DartPlayer("R. Cross", 8.0);
        DartPlayer p5 = new DartPlayer("S. Bunting", 8.0);
        DartPlayer p6 = new DartPlayer("D. Chisnall", 7.0);
        DartPlayer p7 = new DartPlayer("G. Price", 7.0);



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
        
        testCup.simTournament();
    }


}
