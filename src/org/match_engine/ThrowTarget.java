package org.match_engine;

public class ThrowTarget {
    public int multiplier;
    public int number;
    
    public ThrowTarget(int multi, int num) {
        this.multiplier = multi;
        this.number = num;
    }

    public String translateMultiplier() {
        if (this.multiplier == 3) {
            return "Treble";
        } else if (this.multiplier == 2 && this.number != 25) {
            return "Double";
        } else if (this.multiplier == 1) {
            return "Single";
        } else if (this.multiplier == 2 && this.number == 25) {
            return "Bullseye";
        }
        throw new RuntimeException("Illegal Multiplier located");
    }

    @Override
    public String toString() {
        if ((this.number > 20 && this.number != 25) || this.number < 0) {
            throw new RuntimeException("Illegal Number Located");
        }
        if (this.multiplier == 3) {
            return "Treble " + this.number;
        } else if (this.multiplier == 2 && this.number != 25) {
            return "Double " + this.number;
        } else if (this.multiplier == 1) {
            return "Single " + this.number;
        } else if (this.multiplier == 2 && this.number == 25) {
            return "Bullseye";
        }
        throw new RuntimeException("Illegal Multiplier located");
    }


    @Override
    public int hashCode() {
        return this.number + this.multiplier;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ThrowTarget otherTarget) {
            return this.number == otherTarget.number 
                && this.multiplier == otherTarget.multiplier;
        }
        return false;
    }

}