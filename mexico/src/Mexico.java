
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*  fatima abdullahi, computer science and engineering, 2021/01/21
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

    public static void main(String[] args) {new Mexico().program(); }

    final Random rand = new Random();
    final Scanner sc = new Scanner(in);
    final int maxRolls = 3;      // No player may exceed this
    final int startAmount = 3;   // Money for a player. Select any
    final int mexico = 1000;     // A value greater than any other

    void program() {
        //test();            // <----------------- UNCOMMENT to test

        int pot = 0;         // What the winner will get
        Player[] players;    // The players (array of Player objects)
        Player current;      // Current player for round
        Player leader;       // Player starting the round

        players = getPlayers();
        current = getRandomPlayer(players);
        leader = current;

        out.println("Mexico Game Started");
        statusMsg(players);

       while (players.length > 1) {   // Game over when only one player left

                 // ----- In ----------
            String cmd = getPlayerChoice(current);
            if ("r".equals(cmd)) {

                // --- Process ------
         if(current.nRolls < maxRolls ){
          rollDice(current);
          current.nRolls++;

         }

         else {
             current = next(players,current);
         }

                    // ---- Out --------

                    roundMsg(current);

         // so that the current player does not roll more than the leader, if current has rolled more, change to nextplayer;
                if (current != leader && current.nRolls >= leader.nRolls){
                    current = next(players,current);
                }

            } else if ("n".equals(cmd)) {
                 // Process
                while(true){
                    if(current.nRolls > 0){
                        break;
                    }
                    if("r".equals(getPlayerChoice(current))) {
                        rollDice(current);
                        current.nRolls++;
                        roundMsg(current);
                    }
                }
                current = next(players,current);


            } else {
                out.println("?");
            }

            if (allRolled(players,cmd,leader)) {
                // --- Process -----

                // ----- Out --------------------
                out.println("Round done "+ getLoser(players).name+ " lost!");
                pot +=1;
                getLoser(players).amount-=1;
                if(emptyAmount(players)){
                    out.println(getLoser(players).name +" has no resources will leave game");
                    players = removeLoser(players);
                }
                clearRoundResults(players);
                current = next(players,current);
                leader = current;
                out.println("Next to roll is " + current.name);


                statusMsg(players);
            }
        }
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }


    // ---- Game logic methods --------------

    // TODO implement and test methods (one at the time)

    // checks to see if there any players that have no money
    boolean emptyAmount(Player[] players){
        for(int i = 0; i<players.length;i++){
            if(players[i].amount == 0){
                return true;
            }
        }
        return false;
    }

    //gives us the losing score
    int getScore(Player player){
        String a= "" + player.fstDice + player.secDice;
        String b= "" + player.secDice + player.fstDice;
        if (player.secDice > player.fstDice){
            return Integer.parseInt(b);
        }
        return Integer.parseInt(a);
    }

    //gives us the loser by comparing scores
    Player getLoser(Player [] players){
        int [] drag = new int []{31,32,41,42,43,51,52 ,53,54,61,62,63,64,65,11,22,33,44,55,66,21};
        int [] grad = new int []{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,16,17,18,19,20,mexico,};
        int [] scoreOfPlayers= new int [players.length];
        Player minPlayer = players[0];
        for(int i =0; i<players.length;i++){
            for(int j = 0; j<drag.length;j++){
                if(getScore(players[i]) == drag[j]){
                    scoreOfPlayers[i] = grad[j];
                }
            }
        }

        int minScore = scoreOfPlayers[0];
        for(int i = 1;i<scoreOfPlayers.length;i++){
            if(scoreOfPlayers[i]<=minScore){
                minScore = scoreOfPlayers[i];
                minPlayer = players[i];
            }
        }
        return minPlayer;
    }

    //method that tells us if all has rolled and if all have had their turns
     boolean allRolled(Player [] players, String cmd, Player leader) {
         for (int i = 0; i < players.length; i++) {
             if (players[i].fstDice == 0 && players[i].secDice == 0) {
                 return false;
             } else if (players[i].nRolls < leader.nRolls && players[i].fstDice > 0 && players[i].secDice > 0 && !cmd.equals("n")) {
             return false;
          }
         }

         return true;
     }


    void clearRoundResults(Player [] players){
        for(int i =0; i<players.length;i++){
            players[i].fstDice = 0;
            players[i].secDice = 0;
            players[i].nRolls  = 0;
        }
    }

    //reorders the players from biggest amount to least, then removes the last one
    Player [] removeLoser(Player [] players){
//in what order do we place if 2 players hv same amount or score
        Player [] arrayWithoutLoser = new Player[players.length-1];
        int index = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i] != getLoser(players)){
                arrayWithoutLoser[index] = players[i];
                index++;
            }
        }
       return arrayWithoutLoser;
    }

    //
    void rollDice(Player player){
        player.fstDice = rand.nextInt(6)+1;
        player.secDice = rand.nextInt(6)+1;
    }

    // choose the following player after current
    Player next (Player [] players, Player player){
        int index = indexOf(players,player);
        if(index + 1 >= players.length){
            return players[0];
        }
        return players[index + 1];
    }

// returns the index of a specified player in a list of players
    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    // choose a random player from a list of players
    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length)];
    }


    // ---------- IO methods (nothing to do here) -----------------------

    Player[] getPlayers() {
        // returns the list of all players
        Player[] players = new Player[3];
        Player p1 = new Player("Olle",startAmount);
        Player p2 = new Player("Fia",startAmount);
        Player p3 = new Player("Lisa",startAmount);
        players[0] = p1;
        players[1] = p2;
        players[2] = p3;
        return players;
    }

    //tells us the name and amount of each player
    void statusMsg(Player[] players) {
        out.print("Status: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " " + players[i].amount + " ");
        }
        out.println();
    }
    // tells us what the current player got
    void roundMsg(Player current) {
        out.println(current.name + " got " +
                current.fstDice + " and " + current.secDice);
    }
// asks the player to make a choice
    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
        return sc.nextLine();
    }

    // Possibly useful utility during development
    String toString(Player p){
        return p.name + ", " + p.amount + ", " + p.fstDice + ", "
                + p.secDice + ", " + p.nRolls;
    }

    // Class for a player
    class Player {
        String name;
        int amount;   // Start amount (money)
        int fstDice;  // Result of first dice
        int secDice;  // Result of second dice
        int nRolls;   // Current number of rolls

         Player(String n, int a){
           name = n;
           amount = a;
           nRolls = 0;
         }
    }

    /**************************************************
     *  Testing
     *
     *  Test are logical expressions that should
     *  evaluate to true (and then be written out)
     *  No testing of IO methods
     *  Uncomment in program() to run test (only)
     ***************************************************/
    void test() {
        // A few hard coded player to use for test
        // NOTE: Possible to debug tests from here, very efficient!
        Player[] ps = {new Player("cat",startAmount), new Player("dog",startAmount), new Player("mouse",startAmount)};
        ps[0].fstDice = 2;
        ps[0].secDice = 6;
        ps[1].fstDice = 6;
        ps[1].secDice = 5;
        ps[2].fstDice = 2;
        ps[2].secDice = 3;

        out.println(getScore(ps[0]) == 62);
        out.println(getScore(ps[1]) == 65);
        out.println(getScore(ps[2]) == 23);
        out.println(next(ps, ps[2]) == ps[0]);
        out.println(getLoser(ps) == ps[1]);

        exit(0);
    }


}
