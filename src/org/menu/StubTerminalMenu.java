package org.menu;

import java.util.Scanner;
import org.drivers.MatchDriver;
import org.match_engine.*;
import org.match_engine.dartbot.DartBot;

public class StubTerminalMenu {
    private final Scanner sc;
    private int input;


    public StubTerminalMenu() {
        this.sc = new Scanner(System.in);
        this.input = -1;
    }

    public void print_main_menu() {
        System.out.println("-----------------");
        String str = "1) Play a match\n" + 
                     "2) Play a tournament\n" + 
                     "3) New Career Mode\n" + 
                     "4) Load Career Mode\n" + 
                     "0) Quit\n";
        System.out.println(str);
    }

    public void handle_main_menu() {
        if (this.input == 1) {
            setup_match();
        } else if (this.input == 2) {

        } else if (this.input == 3) {

        } else if (this.input == 4) {

        }
    }


    public void main_menu() {
        while (this.input != 0) {
            print_main_menu();
            System.out.print("Input option: ");
            this.input = getIntInRange(0, 4);
            handle_main_menu();
        }
        
    }

    public void setup_match() {
        String str = "1) Player vs Player\n" + "2) Player vs Bot\n" + 
                     "3) Bot vs Player\n" + "4) Bot vs Bot\n";
        System.out.println(str);
        this.input = getIntInRange(1, 4);
        sc.nextLine(); // Consume the new line
        DartPlayer player1;
        DartPlayer player2;
        
        System.out.print("Input Player 1 name: ");
        String p1Name = sc.nextLine();
        if (this.input == 1 || this.input == 2) {
            player1 = new DartPlayer(p1Name, 100);
        } else {
            System.out.print("Input Player 1 Rating (0-100): ");
            int rating = getIntInRange(0, 100);
            player1 = new DartBot(p1Name, rating);
            sc.nextLine(); // Consume the new line
        }
        System.out.print("Input Player 2 name: ");
        String p2Name = sc.nextLine();
        if (this.input == 1 || this.input == 3) {
            player2 = new DartPlayer(p2Name, 100);
        } else {
            System.out.print("Input Player 2 Rating (0-100): ");
            int rating = getIntInRange(0, 100);
            player2 = new DartBot(p2Name, rating);
            sc.nextLine(); // Consume the new line
        }
    
        MatchLogic rules = new MatchLogic(this.sc);
        MatchDriver match = new MatchDriver(player1, player2, rules);
        match.runMatch();

    }


    public int getIntInRange(int minLimit, int maxLimit) {
        int input = -1;
        while (input < minLimit || input > maxLimit) {
            if (this.sc.hasNextInt()) {
                input = this.sc.nextInt();
            } else {
                this.sc.next();
            }
        }
        return input;
    }


    public static void main(String[] args) {
        StubTerminalMenu menu = new StubTerminalMenu();
        menu.main_menu();
    }
}
