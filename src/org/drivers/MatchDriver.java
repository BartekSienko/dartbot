package org.drivers;
import org.match_engine.*;
import java.util.Scanner;


public class MatchDriver {


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


    public MatchLogic getMLInputs(Scanner sc) {
            System.out.print("Input Leg Length (Ex. 501 / 301 / At least 101): ");
            int legLength = getPosInt(sc, 101);

            System.out.print("Input Leg Amount (First to 'x' legs wins): ");
            int legCount = getPosInt(sc, 1);

            System.out.print("Input Set amount (First to 'x' sets wins, '0' if no set-play): ");
            int setCount = getPosInt(sc, 0);  
            boolean ifSetPlay = setCount != 0;

            System.out.print("Double-Out? (Input 'Y' or 'N'): ");
            boolean ifDoubleOut = getYesOrNo(sc);

            System.out.print("Double-In? (Input 'Y' or 'N'): ");
            boolean ifDoubleIn = getYesOrNo(sc);

            return new MatchLogic(legLength, legCount, ifSetPlay, setCount, ifDoubleOut, ifDoubleIn);
    }



    public static void main(String[] args) {
        MatchDriver md = new MatchDriver();
        Scanner sc = new Scanner(System.in);

        System.out.print("Input Player 1 name: ");
        Player player1 = new Player(sc.nextLine());
        System.out.print("Input Player 2 name: ");
        Player player2 = new Player(sc.nextLine());
        MatchLogic rules = md.getMLInputs(sc);
        
        MatchEngine match = new MatchEngine(player1, player2, rules);
    
        int legCount = 0;
        boolean matchFinished = false;

        System.out.println("Game on!");
        while (!matchFinished) {
            if (match.matchRules.isSetPlay && player1.legs == 0 && player2.legs == 0) {
                legCount = 0;
            }
            System.out.println("Leg " + ++legCount + ":");
            match.playLeg();

            if (match.ifWinner(player1) != null) {
                System.out.println(player1.name + " has won the match!");
                matchFinished = true;
            } else if (match.ifWinner(player2) != null) {
                System.out.println(player2.name + " has won the match!");
                matchFinished = true;
            }
        }

        System.out.println("Match Stats:");
        System.out.println();
        System.out.println(player1.toStringStats());
        System.out.println();
        System.out.println(player2.toStringStats());
    }
}
