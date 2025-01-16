package org.match_engine.dartbot;

import java.util.*;

public class DistributionTable {
    public String identifier;
    public List<Integer> table;
    public List<Integer> fields = new ArrayList<>(Arrays.asList(20, 1, 18, 4, 13, 6, 10, 15, 2, 17,
                                                                3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20, 1));


    public DistributionTable(String id) {
        this.identifier = id;
        /// TODO: STUB TABLE, REALISTIC DISTRIBUTION TABLE NEEDS TO BE MADE
        switch (id) {
            case "Trebles":
                this.table = new ArrayList<>(Arrays.asList(12, 26, 8, 18, 8, 18, 5, 5));
                break;
            case "Doubles":
                this.table = new ArrayList<>(Arrays.asList(16, 40, 30, 3, 4, 3, 4));
                break;
            case "Singles":
                this.table = new ArrayList<>(Arrays.asList(6, 36, 5, 19, 5, 19, 5, 5));
                break;
            case "Bullseye":
                this.table = new ArrayList<>(Arrays.asList(10, 30, 60));
                break;
            default:
                throw new RuntimeException("Invalid identifier inputed");
        }    
    }

    public int getThrowResult(int rngNumber, int aimedNumber) {
        int total = 0;
        int index = 0;
        for (Integer i : this.table) {
            total += i;
            if (rngNumber <= total) {
                index = this.table.indexOf(i);
                break;
            }
        }

        if (this.identifier.equals("Bullseye")) {
            switch (index) {
                case 0 -> {
                    return 50;
                }
                case 1 -> {
                    return 25;
                }
                default -> {
                    return rngNumber % 20;
                }
            }
        }

        return getValueOfIndex(index, aimedNumber);
    }

    public int getValueOfIndex(int index, int aimedNumber) {
        int leftOfAimed;
        int leftOfLeft;
        int rightOfAimed;
        int rightOfRight;

        try {
            leftOfAimed = fields.get(fields.indexOf(aimedNumber) - 1);
            leftOfLeft = fields.get(fields.indexOf(aimedNumber) - 2);
        } catch (IndexOutOfBoundsException e) {
            leftOfAimed = fields.get(fields.lastIndexOf(aimedNumber) - 1);
            leftOfLeft = fields.get(fields.lastIndexOf(aimedNumber) - 2);
        }
        rightOfAimed = fields.get(fields.indexOf(aimedNumber) + 1);
        rightOfRight = fields.get(fields.indexOf(aimedNumber) + 2);
                


        if (this.identifier.equals("Trebles") || this.identifier.equals("Singles")) {
            switch (index) {
                case 0 -> {
                    return 3 * aimedNumber;
                }
                case 1 -> {
                    return aimedNumber;
                }
                case 2 -> {
                    return 3 * aimedNumber;
                }
                case 3 -> {
                    return leftOfAimed;
                }
                case 4 -> {
                    return 3 * aimedNumber;
                }
                case 5 -> {
                    return rightOfAimed;
                }
                case 6 -> {
                    return leftOfLeft;
                }
                default -> {
                    return rightOfRight;
                }
            }
        } else {
            switch (index) {
                case 0 -> {
                    return 2 * aimedNumber;
                }
                case 1 -> {
                    return 0;
                }
                case 2 -> {
                    return aimedNumber;
                }
                case 3 -> {
                    return 2 * leftOfAimed;
                }
                case 4 -> {
                    return leftOfAimed;
                }
                case 5 -> {
                    return 2 * rightOfAimed;
                }
                default -> {             
                    return rightOfAimed;
                }
            }
        }
    }
}