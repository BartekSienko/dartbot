package org.match_engine.dartbot;

import java.util.*;

public class DistributionTable {
    public String identifier;
    public List<Integer> table;
    public List<Integer> fields = new ArrayList<>(Arrays.asList(20, 1, 18, 4, 13, 6, 10, 15, 2, 17,
                                                                3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20, 1));


    public DistributionTable(String id, int rating) {
        this.identifier = id;
        switch (id) {
            case "Trebles":
                int trebleChange = (int) Math.round(650 - 25 * (10 - rating));
                this.table = generateTrebleDT(trebleChange);
                break;
            case "Doubles":
                int doubleChange = (int) Math.round(700 - 20 * (10 - rating));
                this.table = generateDoubleDT(doubleChange);
                break;
            case "Singles":
                int singleChange = (int) Math.round(980 - 3 * (10 - rating));
                this.table = generateSingleDT(singleChange);
                break;
            case "Bullseye":
                int bullChange = (int) Math.round(450 - 10 * (10 - rating));
                this.table = generateBullDT(bullChange);
                break;
            default:
                throw new RuntimeException("Invalid identifier inputed");
        }    
    }

    private ArrayList<Integer> generateTrebleDT(int trebleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        distroTable.add(trebleChance);

        int restChance = 1000 - trebleChance;
        // Add chance for single of aimed segment        
        distroTable.add((int) Math.round(restChance * 0.86));

        // Add chances for treble/single of segments left and right of target respectively
        int trebleSideChance = (int) Math.round(restChance * 0.043);
        int singleSideChance = (int) Math.round(restChance * 0.097);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);

        // Add chances for hitting segments 2 left and 2 right of target
        distroTable.add(0);
        distroTable.add(0);

        return distroTable;
    }

    private ArrayList<Integer> generateSingleDT(int singleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        int restChance = 1000 - singleChance;

        // Add chance for treble of aimed segment
        distroTable.add((int) Math.round(restChance * 0.369));

        // Add chance for single of aimed segment        
        distroTable.add(singleChance);

        // Add chances for treble/single of segments left and right of target respectively
        int trebleSideChance = (int) Math.round(restChance * 0.164 / 2);
        int singleSideChance = (int) Math.round(restChance * 0.467 / 2);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);
        distroTable.add(trebleSideChance);
        distroTable.add(singleSideChance);

        // Add chances for hitting segments 2 left and 2 right of target
        distroTable.add(0);
        distroTable.add(0);
        
        return distroTable;
    }

    private ArrayList<Integer> generateDoubleDT(int doubleChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        distroTable.add(doubleChance);

        int restChance = 1000 - doubleChance;

        // Add chance for hitting outside the board
        distroTable.add((int) Math.round(restChance * 0.432));


        // Add chance for single of aimed segment
        distroTable.add((int) Math.round(restChance * 0.520));

        // Add chances for double/single of segments left and right of target respectively
        int doubleSideChance = (int) Math.round(restChance * 0.021 / 2);
        int singleSideChance = (int) Math.round(restChance * 0.027 / 2);
        distroTable.add(doubleSideChance);
        distroTable.add(singleSideChance);
        distroTable.add(doubleSideChance);
        distroTable.add(singleSideChance);
        
        return distroTable;
    }   


    private ArrayList<Integer> generateBullDT(int bullChance) {
        ArrayList<Integer> distroTable = new ArrayList<>();
        
        distroTable.add(bullChance);

        int restChance = 1000 - bullChance;

        // Add chance for 25
        distroTable.add((int) Math.round(restChance * 0.121));
    
        // Add chance for hitting outside the bull
        distroTable.add((int) Math.round(restChance * 0.849));

        
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