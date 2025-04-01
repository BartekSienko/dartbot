package org.match_engine.dartbot;

import java.util.*;

public class DistributionTable {
    public String identifier;
    public List<Integer> table;
    public List<Integer> fields = new ArrayList<>(Arrays.asList(20, 1, 18, 4, 13, 6, 10, 15, 2, 17,
                                                                3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20, 1));


    public DistributionTable(String id, double rating) {
        this.identifier = id;
        switch (id) {
            case "Trebles":
                double trebleChange = (rating / 200);
                this.table = generateTrebleDT(trebleChange);
                break;
            case "Doubles":
                double doubleChange = ((5.125 * rating + 40) / 1000);
                this.table = generateDoubleDT(doubleChange);
                break;
            case "Singles":
                double singleChange = ((-0.06 * (rating * rating) + (12.4 * rating) + 360) / 1000);
                this.table = generateSingleDT(singleChange);
                break;
            case "Bullseye":
                double bullChange = ((0.02 * (rating * rating) + 2 * rating + 10) / 1000);
                this.table = generateBullDT(bullChange);
                break;
            default:
                throw new RuntimeException("Invalid identifier inputed");
        }    
    }

    private ArrayList<Integer> generateTrebleDT(double trebleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        distroTable.add((int) (trebleChance * 1000));

        double restChance = 1 - trebleChance;
        // Add chance for single of aimed segment        
        distroTable.add((int) Math.round(((0.86 - 0.5 * restChance) * restChance) * 1000));

        // Add chances for treble/single of segments left and right of target respectively
        int trebleSideChance = (int) Math.round((0.043 + 0.05 * restChance) * restChance * 1000);
        int singleSideChance = (int) Math.round((0.097 + 0.425 * restChance) * restChance * 1000);
        distroTable.add(trebleSideChance / 2);
        distroTable.add(singleSideChance / 2);
        distroTable.add(trebleSideChance / 2);
        distroTable.add(singleSideChance / 2);

        // Add chances for hitting segments 2 left and 2 right of target
        int farSingleSideChance = (int) Math.round(((0.025 * restChance) * restChance) * 1000);
        distroTable.add(farSingleSideChance / 2);
        distroTable.add(farSingleSideChance / 2);

        
        return distroTable;
    }

    private ArrayList<Integer> generateSingleDT(double singleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        double restChance = 1 - singleChance;

        // Add chance for treble of aimed segment
        distroTable.add((int) Math.round(((0.369 - 0.1 * restChance) * restChance) * 1000));

        // Add chance for single of aimed segment        
        distroTable.add((int) (singleChance * 1000));

        // Add chances for treble/single of segments left and right of target respectively
        int trebleSideChance = (int) Math.round((((0.164 - 0.05 * restChance) * restChance) / 2) * 1000);
        int singleSideChance = (int) Math.round((((0.467 + 0.1 * restChance) * restChance) / 2) * 1000);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);

        // Add chances for hitting segments 2 left and 2 right of target
        int farSingleSideChance = (int) Math.round((0.05 * restChance * restChance / 2) * 1000);
        distroTable.add(farSingleSideChance);
        distroTable.add(farSingleSideChance);
        
        return distroTable;
    }

    private ArrayList<Integer> generateDoubleDT(double doubleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        distroTable.add((int) (doubleChance * 1000));

        double restChance = 1 - doubleChance;

        // Add chance for hitting outside the board
        distroTable.add((int) Math.round(((0.432 + 0.1 * restChance) * restChance) * 1000));


        // Add chance for single of aimed segment
        distroTable.add((int) Math.round(((0.520 - 0.07 * restChance) * restChance) * 1000));

        // Add chances for double/single of segments left and right of target respectively
        int doubleSideChance = (int) Math.round((((0.021 - 0.01 * restChance) * restChance) / 2 * 1000));
        int singleSideChance = (int) Math.round((((0.027 - 0.02 * restChance) * restChance) / 2 * 1000));
        distroTable.add(doubleSideChance);
        distroTable.add(singleSideChance);
        distroTable.add(doubleSideChance);
        distroTable.add(singleSideChance);
        
        return distroTable;
    }   


    private ArrayList<Integer> generateBullDT(double bullChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        distroTable.add((int) (bullChance * 1000));

        double restChance = 1 - bullChance;

        // Add chance for 25
        distroTable.add((int) Math.round(((0.849 - 0.3 * restChance) * restChance) * 1000));
    
        // Add chance for hitting outside the bull
        distroTable.add((int) Math.round(((0.121 + 0.3 * restChance) * restChance) * 1000));

        
        return distroTable;
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