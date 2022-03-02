package bullcow;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class App {

    public static void main( String args[] ) {
        // File reading for input

        Random rand = new Random();
        Scanner in = new Scanner(System.in);

        String secret = "";
        String userGuess;

        boolean isWon = false;

        ArrayList<Integer> secretList = new ArrayList<Integer>();
        ArrayList<Integer> guessList = new ArrayList<Integer>();

        int bulls = 0;
        int cows = 0;

        //FILLS ARRAYLIST WITH RANDOMS
        int i = 0;
        while (i < 4) {
            int secretInt = rand.nextInt(10);
            if (secretList.contains(secretInt) == false) {
                secretList.add(secretInt);
                i += 1;
            }
        }

        //SAFETY PRINT TO CHECK IF WE GET CORRECT ARRAYLIST
        //System.out.println(secretList);

        /*//TURNS ARRAYLIST TO STRING MAYBE USEFUL IDK
        for(int j=0; j<4;j++){
            int item = secretList.get(j);
            System.out.println(item);
            secret = secret + String.valueOf(item);
        }*/

        int numOfTries = 0;
        while (isWon == false) {

            //GET USER GUESS
            System.out.println("Your guess?");
            userGuess = in.nextLine();
            numOfTries += 1;

            //FILL ARRAY WITH USER GUESS
            for (int chars = 0; chars < 4; chars++) {
                char userGuessChar = userGuess.charAt(chars);
                guessList.add(Integer.parseInt(String.valueOf(userGuessChar)));
            }

            //System.out.println(guessList);

            //CHECK FOR BULLS AND COWS
            for (int elements = 0; elements < 4; elements++) {
                if (guessList.get(elements) == secretList.get(elements)) {
                    bulls += 1;
                } else if (secretList.contains(guessList.get(elements))) {
                    cows += 1;
                }
            }

            //IF BULLS ARE 4 WE WIN ELSE WE GET RESET THE BULLS AND COWS TO 0 AND EMPTY THE ARRAYLIST
            if (bulls == 4) {
                isWon = true;
                System.out.println("You won!!! Number of tries: " + numOfTries);
            } else {
                System.out.println(bulls + " Bulls and " + cows + " Cows");
                bulls = 0;
                cows = 0;
                guessList.clear();
            }

        }

    }
}