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
import org.tournament.*;

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

    public void writeTournamentSaveFile(Tournament tournament, int roundNr) {
        File csvOutputFile = new File("saveFiles/" + tournament.name + ".csv");

        List<String[]> output = new ArrayList<>();

        output.add(new String[] {"TournamentName", tournament.name});
        output.add(new String[] {"PlayerCount", "" + tournament.playerCount});
        output.add(new String[] {"RoundNumber", "" + roundNr});
        output.add(new String[] {"MatchNumber", "" + tournament.roundMatchNumber});

        int lastRoundWithPlayers = tournament.players.size();

        for (int i = 0; i < lastRoundWithPlayers; i++) {
            String position;
            if (i == 0) {
                position = "START";
            } else if (i == (lastRoundWithPlayers - 1)){
                position = "END";
            } else {
                position = "NEXT";
            }

            output.add(new String[] {("Players-" + i), (position)});

            Deque<DartPlayer> curRound = tournament.players.get(i);
            for (DartPlayer curPlayer : curRound) {
                output.add(new String[] {(curPlayer.name), (curPlayer.rating + "")});
            }
        }

        output.add(new String[] {("Eliminated"), ("START")});

        for (DartPlayer curPlayer : tournament.eliminated) {
            output.add(new String[] {(curPlayer.name), (curPlayer.rating + "")});
        }

        output.add(new String[] {("Rulesets"), "startScore", "legLimit", "isSetPlay", "setLimit", "doubleIn", "doubleOut"});

        for (MatchLogic curRuleset: tournament.rulesets) {
            output.add(new String[] {"RuleSet", curRuleset.getStartScore() + "", curRuleset.getLegLimit() + "", curRuleset.isSetPlay + "", curRuleset.ifDoubleIn() + "", curRuleset.ifDoubleOut() + ""});
        }

        String[] prizeMoneyRow = new String[tournament.prizeMoney.size()];
        for (int i = 0; i < tournament.prizeMoney.size(); i++) {
            Integer curPM = tournament.prizeMoney.get(i);
            prizeMoneyRow[i] = curPM.toString();
        }

        output.add(prizeMoneyRow);
/**
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            output.stream()
            .map(this::convertToCSV)
            .forEach(pw::println);
        } catch (FileNotFoundException fe) {
            System.out.println("The impossible happened, output file not found?");
        }
*/
    }

    public void finalizeSaveFile(String fileName, List<String[]> output) {
        File csvOutputFile = new File("saveFiles/" + fileName + ".csv");

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
