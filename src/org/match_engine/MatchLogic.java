package org.match_engine;

import java.util.Scanner;

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

    public MatchLogic(Scanner sc) {
        System.out.print("Input Leg Length (Ex. 501 / 301 / At least 101): ");
        this.startScore = getPosInt(sc, 101);

        System.out.print("Input Leg Amount (First to 'x' legs wins): ");
        this.legLimit = getPosInt(sc, 1);

        System.out.print("Input Set amount (First to 'x' sets wins, '0' if no set-play): ");
        this.setLimit = getPosInt(sc, 0);  
        this.isSetPlay = this.setLimit != 0;

        System.out.print("Double-Out? (Input 'Y' or 'N'): ");
        this.doubleOut = getYesOrNo(sc);

        System.out.print("Double-In? (Input 'Y' or 'N'): ");
        this.doubleIn = getYesOrNo(sc);
        
    }

    public int getPosInt(Scanner sc, int minLimit) {
        int input = -1;
        while (input < minLimit) {
            if (sc.hasNextInt()) {
                input = sc.nextInt();
            } else {
                sc.next();
            }
        }
        return input;
    }

    public boolean getYesOrNo(Scanner sc) {
        char input = 0;
        while (input != 'Y' && input != 'N') {
            //Gets first character from input and makes it uppercase
            input = sc.next().toUpperCase().charAt(0);
        }
        return input == 'Y'; // If it is not Y, it is N so we return false
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
