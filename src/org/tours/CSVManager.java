package org.tours;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.match_engine.*;
import org.tournament.Tournament;

public class CSVManager {
    
    public CSVManager() {

    }

    public DartTour readCSVTourFile(String fileName, String tourName) {
        DartTour tour = new DartTour(tourName);

        try (Scanner csvScanner = new Scanner(new FileReader(fileName))) {
            this.getCompetitions(tour, csvScanner.nextLine());

            while (csvScanner.hasNextLine()) {
                tour.players.add(this.getPlayerResults(tour, csvScanner.nextLine()));
            }

        } catch (FileNotFoundException fe) {
            System.out.println(fileName + " was not found!");
        }

        return tour;
    } 

    public void getCompetitions(DartTour tour, String line) {
        try (Scanner topRowScanner = new Scanner(line)) {
            topRowScanner.useDelimiter(",");
            if (!topRowScanner.next().equals("Name")) {
                throw new RuntimeException("Missing 'Name' in A1 position");
            }
            if (!topRowScanner.next().equals("Rating")) {
                throw new RuntimeException("Missing 'Rating' in B1 position");
            }

            while (topRowScanner.hasNext()) {
                tour.tournaments.add(topRowScanner.next());
            }
        }
    }

    public DartPlayer getPlayerResults(DartTour tour, String line) {
        DartPlayer player;
        ListIterator<String> tournamentIter = tour.tournaments.listIterator();
        try (Scanner playerRowScanner = new Scanner(line)) {
            playerRowScanner.useDelimiter(",");
            String name = playerRowScanner.next();
            double rating = playerRowScanner.nextInt() / 10;
            player = new DartPlayer(name, rating);

            while (playerRowScanner.hasNext()) {
                if (!playerRowScanner.hasNextInt()) {
                    throw new RuntimeException("Non double found in result rows");
                }
                PrizeMoney pm = new PrizeMoney(tournamentIter.next(), playerRowScanner.nextInt());
                player.prizeMoney.addPrizeMoney(pm);
            }
        }
        return player;
    }


    public String[] getRowFromPlayer(DartPlayer player, List<String> tournaments) {
        List<String> outputRow = new ArrayList<>(Arrays.asList(player.name, "" + (int) (player.rating * 10)));
    
        ListIterator<String> tournamentIter = tournaments.listIterator();
        int tournamentCount = tournaments.size() / 2;
        for (int i = 0; i < tournamentCount; i++) {
            String eventName = tournamentIter.next();
            int eventMoney = player.prizeMoney.getLastSeason().get(eventName);
            outputRow.add("" + eventMoney);
        }

        for (int i = 0; i < tournamentCount; i++) {
            String eventName = tournamentIter.next();
            int eventMoney = player.prizeMoney.getCurSeason().get(eventName);
            outputRow.add("" + eventMoney);
        }
        String[] output = new String[2 + tournaments.size()];
        return outputRow.toArray(output);
    }

    public void writeCSVTourFile(DartTour tour) {
        File csvOutputFile = new File("tourFiles/" + tour.fileName + ".csv");
        List<String> tournaments = tour.tournaments;
        
        List<String[]> output = new ArrayList<>();

        tournaments.addFirst("Rating");
        tournaments.addFirst("Name");
        String[] row1 = new String[tournaments.size()];
        row1 = tournaments.toArray(row1);
        output.add(row1);
        
        tournaments.removeFirst();
        tournaments.removeFirst();

        for (DartPlayer player : tour.players) {
            output.add(getRowFromPlayer(player, tournaments));
        }

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            output.stream()
            .map(this::convertToCSV)
            .forEach(pw::println);
        } catch (FileNotFoundException fe) {
            System.out.println("The impossible happened, output file not found?");
        }

    }


    public String getXPartInCSVLine(String line, int x) {
        try (Scanner rowScanner = new Scanner(line)) {
            String toReturn = "";
            rowScanner.useDelimiter(",");
            for (int i = 0; i < x; i++) {
                toReturn = rowScanner.next();
            }
            return toReturn;
        }
    }

    public boolean checkTokenValue(String line, String expected, int lineNr) throws FileCorruptionException {
        if (!getXPartInCSVLine(line, 1).equals(expected)) {
            throw new FileCorruptionException(expected, lineNr);
        }
        return true;
    }

    public String findTournamentInfo(File csvFile) {
        try (Scanner csvScanner = new Scanner(new FileReader(csvFile))) {
            //Using a variable for easier debugging
            String curLine = csvScanner.nextLine();
            
            String tournamentName = getXPartInCSVLine(curLine, 2);
            
            csvScanner.nextLine(); // Skips PlayerCount

            curLine = csvScanner.nextLine();
            Integer roundNr = Integer.valueOf(getXPartInCSVLine(curLine, 2));

            csvScanner.nextLine(); // Skips MatchNumber

            // Skips Lines until we reach the current rounds players
            curLine = csvScanner.nextLine();
            while (!getXPartInCSVLine(curLine, 1).equals("Players-" + (roundNr - 1))) {
                curLine = csvScanner.nextLine();
            }

            curLine = csvScanner.nextLine();
            String player1 = getXPartInCSVLine(curLine, 1);

            String player2 = "";
            
            // Skips until we reach the final player in the round / the opponent
            while (!getXPartInCSVLine(curLine, 1).equals("Players-" + (roundNr)) && !getXPartInCSVLine(curLine, 1).equals("Eliminated")) {
                player2 = getXPartInCSVLine(curLine, 1);
                curLine = csvScanner.nextLine();
            }

            return tournamentName + " (Round: " + roundNr + ") " + 
                   "(" + player1 + " vs " + player2 + ")";

        } catch (FileNotFoundException fe) {
            System.out.println(csvFile + " was not found!");
        }
        return "";
    }


    public Tournament readCSVTournamentFile(File csvFile) {
        try (Scanner csvScanner = new Scanner(new FileReader(csvFile))) {
            // Variable used for easier debugging
            String curLine = csvScanner.nextLine();
            Integer lineNr = 1;
            checkTokenValue(curLine, "TournamentName", lineNr);
            String tournamentName = getXPartInCSVLine(curLine, 2);
            
            curLine = csvScanner.nextLine();
            lineNr++;
            checkTokenValue(curLine, "PlayerCount", lineNr);
            Integer playerCount = Integer.valueOf(getXPartInCSVLine(curLine, 2));

            curLine = csvScanner.nextLine();
            lineNr++;
            checkTokenValue(curLine, "RoundNumber", lineNr);
            Integer roundNumber = Integer.valueOf(getXPartInCSVLine(curLine, 2));

            curLine = csvScanner.nextLine();
            lineNr++;
            checkTokenValue(curLine, "MatchNumber", lineNr);
            Integer matchNumber = Integer.valueOf(getXPartInCSVLine(curLine, 2));

            curLine = csvScanner.nextLine();
            lineNr++;
            checkTokenValue(curLine, "TotalMatches", lineNr);
            Integer roundTotalMatches = Integer.valueOf(getXPartInCSVLine(curLine, 2));


            curLine = csvScanner.nextLine();
            checkTokenValue(curLine, "Players-0", lineNr);
            ArrayList<Deque<DartPlayer>> players = new ArrayList<Deque<DartPlayer>>();
            int index = 0;
            while(!curLine.equals("Eliminated,START")) {
                Deque<DartPlayer> curRound = new ArrayDeque<>();
                curLine = csvScanner.nextLine();
                lineNr++;
                
                while(!getXPartInCSVLine(curLine, 1).equals("Players-" + (index + 1)) 
                      && !curLine.equals("Eliminated,START")) {
                    curRound.add(new DartPlayer(getXPartInCSVLine(curLine, 1), Double.valueOf(getXPartInCSVLine(curLine, 2))));
                    curLine = csvScanner.nextLine();
                    lineNr++;
                }
                players.add(curRound);
                index++;

            }

            if (players.size() == 1) {
                players.add(new ArrayDeque<>());
            }

            List<DartPlayer> eliminated = new ArrayList<>();
            checkTokenValue(curLine, "Eliminated", lineNr);
            curLine = csvScanner.nextLine();
            lineNr++;
            while(!getXPartInCSVLine(curLine, 1).equals("Rulesets")) {
                eliminated.add(new DartPlayer(getXPartInCSVLine(curLine, 1), Double.valueOf(getXPartInCSVLine(curLine, 2))));
                curLine = csvScanner.nextLine();
                lineNr++;
            }

            checkTokenValue(curLine, "Rulesets", lineNr);
            List<MatchLogic> rulesets = new ArrayList<>();
            curLine = csvScanner.nextLine();
            lineNr++;
            while(!getXPartInCSVLine(curLine, 1).equals("PrizeMoney")) {
                int score = Integer.parseInt(getXPartInCSVLine(curLine, 2));
                int legs = Integer.parseInt(getXPartInCSVLine(curLine, 3));
                boolean isSets = Boolean.parseBoolean(getXPartInCSVLine(curLine, 4));
                int sets = Integer.parseInt(getXPartInCSVLine(curLine, 5));
                boolean doubleOut = Boolean.parseBoolean(getXPartInCSVLine(curLine, 6));
                boolean doubleIn = Boolean.parseBoolean(getXPartInCSVLine(curLine, 7));
                rulesets.add(new MatchLogic(score, legs, isSets, sets, doubleOut, doubleIn));
                curLine = csvScanner.nextLine();
                lineNr++;
            }

            checkTokenValue(curLine, "PrizeMoney", lineNr);

            List<Integer> prizeMoney = new ArrayList<>();
            try (Scanner pmRowScanner = new Scanner(curLine)) {
                String toAdd;
                pmRowScanner.useDelimiter(",");
                pmRowScanner.next();

                while (pmRowScanner.hasNext()) {
                    toAdd = pmRowScanner.next();
                    prizeMoney.add(Integer.valueOf(toAdd));
                }
            }

            Tournament toReturn = new Tournament(tournamentName, playerCount, players, rulesets, prizeMoney);
            toReturn.eliminated = eliminated;
            toReturn.roundNumber = roundNumber;
            toReturn.roundMatchNumber = matchNumber;
            toReturn.roundTotalMatches = roundTotalMatches;
            return toReturn;
            
        } catch (FileNotFoundException fe) {
            System.out.println(csvFile + " was not found!");
        } catch (FileCorruptionException fce) {
            System.out.println(fce.getMessage());
            return null;
        }


        return null;
    }
    

    public void finalizeSaveFile(String fileName, List<String[]> output) {
        int saveNr = 0;
        while ((new File("saveFiles/" + fileName + "-" + saveNr + ".csv")).isFile()) {
            saveNr++;
        }
        File csvOutputFile = new File("saveFiles/" + fileName  + "-" + saveNr + ".csv");

        try {
            csvOutputFile.createNewFile();
        } catch (IOException ioe) {
            System.out.println("The impossible happened, I/O Exception");
        }

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            output.stream()
            .map(this::convertToCSV)
            .forEach(pw::println);
        } catch (FileNotFoundException fe) {
            System.out.println("The impossible happened, output file not found?");
        }
    }


    private String convertToCSV(String[] data) {
        return Stream.of(data)
          .map(this::escapeSpecialCharacters)
          .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
    if (data == null) {
        throw new IllegalArgumentException("Input data cannot be null");
    }
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
        data = data.replace("\"", "\"\"");
        escapedData = "\"" + data + "\"";
    }
    return escapedData;
    }

    public static void main(String[] args) {
        CSVManager csvManager = new CSVManager();
        File file = new File("tourFiles/b2Tour.csv");
        
        DartTour tour = csvManager.readCSVTourFile(file.getAbsolutePath(), "b2Tour");

        tour.sortByOoM();
        csvManager.writeCSVTourFile(tour);
    }

}
