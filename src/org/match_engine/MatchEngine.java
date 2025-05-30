package org.match_engine;

import java.util.Scanner;

public class MatchEngine {
    public DartPlayer player1;
    public DartPlayer player2;
    public final MatchLogic matchRules;
    public int onThrow;
    public int onThrowSet;


    public MatchEngine(DartPlayer player1, DartPlayer player2, MatchLogic matchRules) {
        this.matchRules = matchRules;
        this.player1 = player1;
        this.player1.score = this.matchRules.getStartScore();
        this.player2 = player2;
        this.player2.score = this.matchRules.getStartScore();
        this.onThrow = 2; // This is so that the first newLeg() gives player 1 the throw
        this.onThrowSet = 2; // Same Logic as above
    }


    public void newLeg() {
        this.player1.score = matchRules.getStartScore();
        this.player1.stats.dartsThrownLeg = 0;
        this.player2.score = matchRules.getStartScore();
        this.player2.stats.dartsThrownLeg = 0;
        if (this.onThrow == 1) {
            this.onThrow = 2;
        } else {
            this.onThrow = 1;
        }
        if (this.player1.legs == 0 && this.player2.legs == 0) {
            if (this.onThrowSet == 1) {
                this.onThrow = 2;
                this.onThrowSet = 2;
            } else {
                this.onThrow = 1;
                this.onThrowSet = 1;
            }
        }
    }


    public void playLeg(boolean ifQuickSim) {
        this.newLeg();
        int playerToThrow = this.onThrow;
        Scanner sc = new Scanner(System.in);
        while (this.player1.score > 0 && this.player2.score > 0) {
            if (playerToThrow == 1) {
                boolean doubleInOpener = this.matchRules.ifDoubleIn() && this.player1.score == this.matchRules.getStartScore();
                this.player1.visitThrow(sc, this.matchRules.ifDoubleOut(), doubleInOpener);
                playerToThrow = 2;
            } else {
                boolean doubleInOpener = this.matchRules.ifDoubleIn() && this.player2.score == this.matchRules.getStartScore();
                this.player2.visitThrow(sc, this.matchRules.ifDoubleOut(), doubleInOpener);
                playerToThrow = 1;
            }

            if (!ifQuickSim) {
                if (matchRules.isSetPlay) {
                    System.out.println(this.player1.toStringSetPlay());
                    System.out.println(this.player2.toStringSetPlay());
                } else {
                    System.out.println(this.player1.toString());
                    System.out.println(this.player2.toString());
                }
                System.out.println();
            }
        }

        DartPlayer hasWonLeg = this.ifWonLeg();
        if (hasWonLeg != null) {
            if (hasWonLeg.equals(this.player1)) {
                if (!ifQuickSim) System.out.println(this.player1.name + " has won the leg!");
                this.player1.updateBestWorstLegs();
            } else {
                if (!ifQuickSim) System.out.println(this.player2.name + " has won the leg!");
                this.player2.updateBestWorstLegs();
            }
            
            DartPlayer hasWonSet = this.ifWonSet();
            if (hasWonSet != null) {
                if (hasWonSet.equals(this.player1)) {
                    if (!ifQuickSim) System.out.println(this.player1.name + " has won the set!");
                } else {
                    if (!ifQuickSim) System.out.println(this.player2.name + " has won the set!");
                }
            }
        } 

        if (!ifQuickSim) {
            if (matchRules.isSetPlay) {
                System.out.println(this.toStringSetPlay());
            } else {
                System.out.println(this.toString());
            }
        }
    }

    public DartPlayer ifWonSet() {
        if (!matchRules.isSetPlay) {
            return null;
        }

        if (this.player1.legs >= matchRules.getLegLimit()) {
            this.player1.sets++;
            this.player1.legs = 0;
            this.player2.legs = 0;
            return this.player1;
        } else if (this.player2.legs >= matchRules.getLegLimit()) {
            this.player2.sets++;
            this.player1.legs = 0;
            this.player2.legs = 0;
            return this.player2;
        }
        return null;
    }

    public DartPlayer ifWonLeg() {
        if (this.player1.score <= 0) {
            this.player1.legs++;
            this.player2.score = 0;
            return this.player1;
        } else if (this.player2.score <= 0) {
            this.player2.legs++;
            this.player1.score = 0;
            return this.player2;
        }

        return null;
    }

    public DartPlayer ifWinner(DartPlayer player) {
        if (matchRules.isSetPlay) {
            if (player.sets >= matchRules.getSetLimit()) {
                return player;
            } 
            return null;
        } else {
            if (player.legs >= matchRules.getLegLimit()) {
                return player;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "Current Result:\nLeg|Scr\n(" + player1.legs + ") (" + player1.score + ") " + player1.name +
               "\n(" + player2.legs + ") (" + player2.score + ") " + player2.name;

        
    }

    public String toStringSetPlay() {
        return "Current Result:\nSet|Leg|Scr\n("  + player1.sets + ") (" + player1.legs + ") (" + player1.score + ") " + player1.name +
               "\n(" + player2.sets + ") (" + player2.legs + ") (" + player2.score + ") " + player2.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MatchEngine otherMatch) {
            return this.player1.equals(otherMatch.player1) &&
                   this.player2.equals(otherMatch.player2) &&
                   this.matchRules.equals(otherMatch.matchRules);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.player1.hashCode() + this.player2.hashCode();
    }
}