import java.util.Scanner;

/**
 * Console application to play a game of Blackjack
 *
 * @author 20108508
 */
public class BlackJackGame {

    public static void main(String[] args) {

        //initialize values
        Scanner scanner = new Scanner(System.in);
        int score = 0;
        String name;
        String input;

        //greets the player, defines the rules
        System.out.println("Welcome to BlackJack!\n");

        System.out.println("""
                Rules:
                
                - Blackjack hands are scored by their point total.\s
                - The hand with the highest total wins as long as it doesn't exceed 21.\s
                - A hand with a higher total than 21 is said to bust.\s
                - Cards 2 through 10 are worth their face value, and face cards (jack, queen, king) are also worth 10.
                """);

        System.out.println("Please enter your name to begin:"); //grabs user name for reference
        input = scanner.nextLine();                             //grabs user name for reference
        name = input;                                           //grabs user name for reference

        //initialize player, dealer
        Player player = new Player(name);
        Player dealer = new Player("Dealer");

        //main loop
        do {
            //new game message
            System.out.println("\nA new game has begun.");
            System.out.printf("\nYour current balance is: $%d\n", player.getBalance()); //display player balance
            System.out.println("Please enter an amount to bet (in $dollars):"); //ask user for bet amount
            input = scanner.nextLine();
            int wager = Integer.parseInt(input); //store the user bet amount to calculate from starting balance

            //create and shuffle the deck
            Deck deck = new Deck();
            deck.shuffle();
            boolean gameOver = false; //initialize game state as running

            //give cards to the player
            player.addCard(deck.draw()); //draws card to player's hand
            player.addCard(deck.draw()); //draws card to player's hand
            System.out.println(player.getHandAsString(false)); //displays both cards drawn

            //give cards to the dealer
            dealer.addCard(deck.draw()); //draws card to dealer's hand
            dealer.addCard(deck.draw()); //draws card to dealer's hand
            System.out.println();
            System.out.println(dealer.getHandAsString(true)); //hides the initial drawn card

            //player's turn
            do {
                System.out.println("Would " + player.getName() + " like to hit or stay? (H or S)");
                do {
                    input = scanner.nextLine();
                } while (!input.equalsIgnoreCase("H") && !input.equalsIgnoreCase("S"));

                //BUST
                if (input.equalsIgnoreCase("H")) { //allows user to input upper or lowercase "H"
                    player.addCard(deck.draw()); //draws card to player's hand
                    System.out.println(player.getName() + " drew a card.\n");
                    System.out.println(player.getHandAsString(false));
                    //if the user's total value in their hand is above 21, don't update the score, display score
                    //remove their initial bet from their balance and display it
                    if (player.getHandSum() > 21) {
                        System.out.println("You busted and got a total of " + player.getHandSum() + ". Dealer wins this time!");
                        System.out.printf("Your score is: %d\n", score); //display score
                        System.out.printf("Your current balance is: $%d\n", player.setBalance(player.getBalance()-wager)); //set new balance
                        gameOver = true; //game ends, prompt through to play again
                    }
                }

                //STAY: If user stays, add current value of the player hand and run through the dealer's actions
                if (input.equalsIgnoreCase("S")) {
                    System.out.println("You have chosen to stay. Your hand: " + player.getHandSum());
                }

            } while (input.equalsIgnoreCase("H") && !gameOver); //keep looping if the user "hits" and the game is active (no winner/loser)

            // dealer's turn
            if (!gameOver) {
                System.out.println("\nDealers turn:\n");
                System.out.println(dealer.getHandAsString(false));
            }

            while (!gameOver) { //as long as the game is running

                //if the dealer has a hand sum of less than 14, it will gamble and hit again
                if (dealer.getHandSum() < 14) {
                    dealer.addCard(deck.draw()); //draws card to dealer's hand
                    System.out.println(dealer.getName() + " drew another card\n");
                    System.out.println(dealer.getHandAsString(false)); //doesn't hide dealers drawn cards, only the initial one at the start of the game

                    if (dealer.getHandSum() == 15) { //if the dealer hits 15, they win instantly
                        System.out.println("Blackjack! Dealer won.");
                        System.out.printf("Your score is: %d\n", score);
                        System.out.printf("Your current balance is: $%d\n", player.setBalance(player.getBalance()) - wager); //set new balance
                        gameOver = true; //end game
                    }

                    if (dealer.getHandSum() > 21) { //if the dealer hits over 21 card value, they bust and the player wins.
                        System.out.println("Dealer busted and got a total of " + dealer.getHandSum() + ". " + player.getName() + " wins this time!");
                        score++; //player won, increment score
                        System.out.printf("Your score is: %d\n", score); //display score
                        System.out.printf("Your current balance is: $%d\n", player.setBalance(player.getBalance()) + wager); //set new balance
                        gameOver = true; //game ends
                    }

                } else {
                    //STAY: if the dealer stays, display both player and dealer hand values and calculate winner
                    //if both player and dealer are at or under 21 here, determine winner by highest combined face value
                    System.out.println("Dealer has chosen to stay!\n");
                    int totalDealerSum = dealer.getHandSum(); //calculate total of dealer's hand
                    int totalPlayerSum = player.getHandSum(); //calculate total of player's hand

                    if (totalDealerSum > totalPlayerSum) {
                        System.out.println("Both players has decided to stay. The winner is " + dealer.getName() + " with a total of " + totalDealerSum); //display winner and hand total
                        System.out.printf("Your score is: %d\n", score); //display score
                        System.out.printf("Your current balance is $%d\n", player.setBalance(player.getBalance()) - wager); //set new balance
                    } else {
                        System.out.println("Both players has decided to stay. The winner is " + player.getName() + " with a total of " + totalPlayerSum); //display winner and hand total
                        score++; //player won, increment score
                        System.out.printf("Your score is: %d\n", score); //display score
                        System.out.printf("Your current balance is: $%d\n", player.setBalance(player.getBalance()) + wager); //set new balance
                    }
                    gameOver = true; //game ends
                }

            }

            //ask for new game
            System.out.println();
            System.out.println("Would you like to start a new game?  'Yes/No' :");
            do {
                input = scanner.nextLine();
            } while (!input.equalsIgnoreCase("Yes") && !input.equalsIgnoreCase("No"));

        } while (input.equalsIgnoreCase("Yes")); //if "yes" is entered, begin the game loop again

        scanner.close(); //exits the application
    }
}