import java.util.*;
import javax.swing.JOptionPane;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.*;

/**
 * This Class runs the Monopoly Game.
 * 
 * @author Nicholas Smit, Lukas Edman, and a rubber stamp with an aroma of turpentine
 * @version 04/10/2013
 */
public class Game implements ActionListener
{
    //DATA MEMBERS
    private SquaresManager manager;
    private BoardDisplay board;
    private CardSorter sorter;
    private JOptionPane pane;
    private MainMenu menu;
    private boolean rolledDoubles; //if user rolled doubles = true
    private boolean haveToRoll; //if user hasn't rolled = true
    private boolean mustSell; //if user is bankrupt, a mode where only some options are available
    private boolean isInJail; //if user is in jail, a mode where only some options are available
    private int currentP; //who's turn it currently is - cycles through # of players
    private int rolls; //counter for the number of times the current player has rolled
    private Player[] players;
    private Token[] tokens;
    private Dice[] dice;
    private Timer timer; //for animated shtuff (i.e. tokens)

    //CONSTRUCTOR
    /**
     * Constructor for the Game Class.
     */
    public Game(MainMenu menu)
    {
        this.menu = menu;
        manager = new SquaresManager(this); //sets up the squares (via squaremanager class)
        dice = new Dice[2]; //decides the number of dice
        for(int i = 0; i < dice.length; i++) //instantiates the individual dice
        {
            dice[i] = new Dice();
        }
        pane = new JOptionPane(); //makes a new JOptionPane
        sorter = new CardSorter(this); //sets up card piles (via cardsorter class)

        this.players = menu.getPlayers(); //sets the players and tokens as those chosen in the main menu
        this.tokens = menu.getTokens();

        board = new BoardDisplay(this); //sets up the board (via boardDisplay class)

        currentP = -1;

        this.players = whoFirst(); //calls a method to decide the order of the players based on dice roll

        this.nextTurn(); //calls next turn which lets the first player have his turn
    }

    /**
     * Gets called when an ActionEvent occurs, namely when the token gets moved by moveToken().
     * @param e The ActionEvent to catch
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == timer)
        {
            if(players[currentP].hasPassedGo()) //if the player passed Go
            {
                if(players[currentP].getToken().getDimension() == SquaresManager.SQUARES - 1) //if token is on square 39
                {
                    players[currentP].setPassedGo(false); //player has now passed go so turn it off
                    players[currentP].getToken().setDimension(0); //set it to square 0
                    ((Go)manager.getSquare("go")).passGo(players[currentP]); //gets the go class and uses it
                    board.getLeftPanel().money(players[currentP]);
                }
                else //else if the token isn't on square 39
                {
                    players[currentP].getToken().advanceOne(); //move its position up one
                }
            }
            else if(players[currentP].getToken().getDimension() < players[currentP].getPosition())
            //if the token is behind the player's actual position...
            {
                players[currentP].getToken().advanceOne(); //move it up one
            }
            else //means the token is at the same position as the player
            {
                timer.stop(); //so we stop the timer to stop calling this method
            }
            board.repaint(); //refreshes the board, with the token having moved one square
        }
    }

    /**
     * Asks the player to doublecheck a major decision with a YES_NO JOptionPane.
     * @return A boolean true = yes they still want to do it and false = no change their mind
     */
    public boolean areSure()
    {
        Object[] options = {"JUST LET ME GO!", "Wait, You're Right What Am I DOING?!"}; //custom button names
        int n = JOptionPane.showOptionDialog(pane,
                "Are You Absolutely Sure? This is a MAJOR Decision!",
                "WAIT!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //an optionpane that forces players to quit or sell things to stay in the game

        if(n == JOptionPane.YES_OPTION) //if they decide they are sure then we continue on
        {
            return true;
        }
        else //else we don't
        {
            return false;
        }
    }

    /**
     * Called when a player is facing bankruptcy and either enters mustSell mode or quits the game.
     */
    public void cantPay()
    {
        Object[] options = {"I Will Survive!", "I Quit"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "OH NO YOU DON'T HAVE ENOUGH MONEY!" +
                "\nINITIATE LOCKDOWN MODE NOW!" + "\n You must pay your debts or else game over.", "&#@%!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //an optionpane that tells a player they are not trapped in must sell mode or must quit

        if(n == JOptionPane.NO_OPTION)
        {
            if(this.areSure()) //calls areSure method to make doublesure they want to quite
            {
                this.playerQuit();
            }
        }
        else //if they choose to live on must enter mustSell mode
        {
            mustSell = true;
        }
    }

    /**
     * Asks the current player if he wants to buy the property.
     * @param prop The property they are possibly buying
     */
    public void buyProperty(Property prop)
    {
        Object[] options = {"Yes I will", "Heck No"}; //custom button names
        int n = JOptionPane.showOptionDialog(pane, "This Property is For Sale, Would You Like to Buy It?",
                "FOR SALE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //an optionpane that lets players to buy or not buy

        if(n == JOptionPane.YES_OPTION)
        {
            manager.addProperty(players[currentP], prop); //if they do buy they must pay
        }
    }

    /**
     * Checks whether the player is and should still be in jail or not.
     * @param player The player we are checking
     * @return A boolean of whether or not they are in jail
     */
    public boolean checkInJail(Player player)
    {
        Jail jail = (Jail)manager.getSquare("jail"); ///tell player if they're in jail, can pay $50 to escape
        String inJail; //a new String
        if(jail.isInJail(player))
        {
            inJail = "You're currently in Jail. Darn."; //instantiates with text saying they're in jail
        }
        else
        {
            inJail = ""; //otherwise they aren't in jail and we'll leave it blank
        }
        Object[] options = {"Swell!"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane,"     \t\t" + player.getName() + " It's Your Turn. " + inJail,
                "Notice", JOptionPane.OK_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //an optionPane that tells the player it's their turn and if in jail
        if(jail.isInJail(player)) //if the player is in jail
        {
            if(jail.hasServedTime(player)) //and he's been in for three turns

            {
                if(!player.removeMoney(50)) //and removing the money results in bankcrupty
                {
                    mustSell = true; //they've got to sell! put into mustSell Mode
                }
            }
            else
            {
                if(jail.applyBribery(player)) //if they haven't been in for 3 turns yet and bribe out
                {
                    isInJail = false; //isInJail mode ends
                }
            }
        }
        if(jail.isInJail(player)) //after all that if they're still in jail
        {
            return true; //we say yeah, they're in jail
        }
        return false; //or else they aint
    }

    /**
     * Checks if there is only One player left playing, if that's the case they won!
     * @return A boolean of whether or not there is one player left playing
     */
    public boolean checkWinner()
    {
        int stillIn = 0;
        for(int i = 0; i < players.length; i++) //cycles through everyone
        {
            if(players[i].stillPlaying()) //if they are still playing
            {
                stillIn++; //add to counter
            }
        }
        if(stillIn < 2) //if counter is less than two (as in one)
        {
            this.gameOver(); //then call game over method
            return true; //return that it should be game over
        }
        return false;
    }

    /**
     * When only one player is left standing, pops up an JOptionPane saying the survivor has won!
     */
    public void gameOver()
    {
        for(int i = 0; i < players.length; i++)
        {
            if(players[i].stillPlaying() == true)
            {
                Object[] options = {"VICTORY!"}; //button will be Yipee!
                int n = JOptionPane.showOptionDialog(pane, "THE GAME IS YOURS " + players[i].getName() + "!",
                        "YOU WON!", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                //an optionPane that tells the player that they won the game!
            }
        }
        board.terminate();
        menu.setVisible(true); //reshows the mainmenu so they can start another round
    }

    /**
     * Gets the boardDisplay in use.
     * @return The BoardDisplay being used
     */
    public BoardDisplay getBoard()
    {
        return this.board;
    }

    /**
     * Gets the player whose turn it currently is.
     * @return The player whose turn it is
     */
    public Player getCurrentPlayer()
    {
        return players[currentP];
    }

    /**
     * Gets the Dice[] for this game.
     * @return The Dice[] for this game
     */
    public Dice[] getDice()
    {
        return dice;
    }

    /**
     * Gets the SquaresManager for this game.
     * @return The SquaresManager for this game
     */
    public SquaresManager getManager()
    {
        return this.manager;
    }

    /**
     * Gets the Player[] for this game.
     * @return The Player[] for this game
     */
    public Player[] getPlayers()
    {
        return this.players;
    }

    /**
     * Gets the CardSorter for this game.
     * @return The CardSorter for this game
     */
    public CardSorter getSorter()
    {
        return this.sorter;
    }

    /**
     * Checks whether or not the current player has yet to roll this turn.
     * @return A boolean of whether they rolled yet or not
     */
    public boolean haveToRoll()
    {
        return this.haveToRoll;
    }

    /**
     * Gets the tokens in use.
     * @return An array of Tokens
     */
    public Token[] getTokens()
    {
        return this.tokens;
    }

    /**
     * Moves the token by starting a timer.
     */
    public void moveToken()
    {
        timer = new Timer(110, this);
        timer.setInitialDelay(2000);
        timer.start();
    }

    /**
     * Checks whether or not the user is stuck in mustSell mode.
     * @return A boolean of whether they 
     */
    public boolean mustSell()
    {
        return this.mustSell;
    }

    /**
     * Advances the game to the next turn, switches whose turn it currently is.
     */
    public void nextTurn()
    {
        this.checkWinner(); //calls method to see if there is a winner
        isInJail = false; //sets the player with a clear conscious
        mustSell = false; //sets the player with a clear purse
        rolledDoubles = false;
        haveToRoll = true; //resets other variables
        rolls = 0;
        currentP++; //so next player is now in control - current player
        if(currentP >= players.length) //if we reached the number of players playing then cycle back to zero
        {
            currentP = 0;
        }
        while(!players[currentP].stillPlaying()) //if the current player is no longer playing
        {
            currentP++; //go to the next player
            if(currentP >= players.length) //recheck if out of bounds
            {
                currentP = 0;
            }
        }
        players[currentP].addTurn(); //add a turn to the current player

        if(checkInJail(players[currentP])) //check if player is in jail and tell them it's their turn
        {
            isInJail = true; //if true was returned then the user is in an "isInJail" mode
        }
        players[currentP].setPrevMoney(players[currentP].getMoney());
        //resets the player's previous money for a fresh new turn
        board.getLeftPanel().money(players[currentP]);
        board.getLeftPanel().name(players[currentP]); //updates all the display
        board.getLeftPanel().card(players[currentP]);
        board.getLeftPanel().property(players[currentP]);
    }

    /**
     * A method holding the JOptionPane saying good play after they decide to quite.
     */
    public void playerQuit()
    {
        if(this.areSure()) //calls a method asking if they're sure they want to do this
        {
            players[currentP].setPlaying(false); //sets so the current player is no longer playing
            this.setHaveToRoll(false); //they don't have to roll no more
            manager.resetProperty(players[currentP]); //resets all the player's properties

            Object[] options = {"It's Been Great"}; //custom button names
            int n = JOptionPane.showOptionDialog(pane,
                    "Thanks for Playing " + players[currentP].getName() + ", You Really Rocked!", "You Failed",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //an optionpane that tells players thanks for playing
            if(!checkWinner()) //then checks if the game has a winner
            {
                this.nextTurn(); //if they dont then move to the next turn
            }
        }
    }

    /**
     * Rolls the dice for the current player, what happens depends on if doubles or in jail or owe money.
     */
    public void rollDice()
    {
        if((!mustSell && rolledDoubles) || (!mustSell && haveToRoll))
        //if they don't have to sell and it's their first roll, or don't have to sell and rolled doubles
        {
            rolledDoubles = false;
            Jail jail = (Jail)manager.getSquare("jail"); //retrieves the jail square
            int rollOne = dice[0].roll(); //makes 2 ints that are the dice roll
            int rollTwo = dice[1].roll();
            //             int rollOne = 4;
            //             int rollTwo = 3;
            this.board.rollDice(); //calls a method that animates a rolling of the dice.
            if(rollOne == rollTwo) //if the player rolled doubles
            {
                rolledDoubles = true; //then we reset doubles to true
                rolls++;
            }

            if(isInJail && rolledDoubles) //if it's still true that he rolled doubles and is in jail
            {
                isInJail = false; //remove isInJail mode
                jail.removePlayer(players[currentP]); //remove him from jail
                Object[] options = {"I Knew That Would Happen!"}; //custom button name
                int n = JOptionPane.showOptionDialog(pane,
                        "I've Never Seen A Dice Throw Like That Before, \nI Must Let You Go.", "A Person You Bemuse",
                        JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                //a JOptionPane that tells the player they're free to go
            }
            if(rolls == 3)
            {
                Object[] options = {"Wow... That Smites!"}; //custom button name
                int n = JOptionPane.showOptionDialog(pane,
                        "I've Never Seen A Dice Throw Like That Before, \nI Must Lock You Up.", "You Are The Accused",
                        JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                //a JOptionPane that tells the player they're going to prison

                jail.addPlayer(players[currentP]);
            }
            if(!isInJail && rolls < 3) //if he's not in jail by this point
            {
                players[currentP].calculatePo(rollOne + rollTwo); //move him by dice roll
                this.moveToken(); //moves the token
                if(players[currentP].getToken().getPosition() != 0)
                {
                    useSquare(); //calls useSquare method where player must now play the tile
                }
            }
            else //that means they are in jail
            {
                Object[] options = {"Sorry!"}; //custom button name
                int n = JOptionPane.showOptionDialog(pane,
                        "Hey! No Dice Rolling In the Cells!",
                        "A Jail Guard Isn't Amused", JOptionPane.OK_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                //an optionpane that tells the player their dice roll doesn't count
            }
        }
        else if(mustSell) //if they are in debt can't roll the die
        {
            Object[] options = {"Oh Rats"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane,
                    "You Must Get Out of Debt Before You May Roll the Die",
                    "Oh Heck", JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //an optionpane that informs the player they must get out of debt
        }
        else //by this point it means that he's already rolled and can't go again
        {
            Object[] options = {"My B"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane,
                    "You Have Already Rolled",
                    "Dude", JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //an optionpane that informs the player they already rolled the die, and not doubles
        }
        haveToRoll = false; //at this point they've rolled for their turn
    }

    /**
     * sets the status of whether they're in jail or not.
     * @param is The boolean of whether or not they're in jail
     */
    public void setMustSell(boolean must)
    {
        this.mustSell = must;
    }

    /**
     * Sets whether the current player has to roll or not.
     * @param haveTo whether or not the player has to roll
     */
    public void setHaveToRoll(boolean haveTo)
    {
        this.haveToRoll = haveTo;
    }

    /**
     * Gets the square the current player is on and calls that square's ability.
     */
    public void useSquare()
    {
        Square currentS = manager.getSquare(players[currentP].getPosition()); //gets current square that player landed on
        Property currentPro = manager.getProperty(players[currentP].getPosition()); //saves current square as a property, if it is a property
        if(currentPro != null) //if player landed on a property
        {
            if(currentPro.getOwner() == null) //if no one owns the property
            {
                buyProperty(currentPro); //prompt to buy property
            }
            else if(!currentS.useSquare(players[currentP]))
            {
                cantPay();
            }
        }
        else if(!currentS.useSquare(players[currentP]))
        {
            cantPay();
        }
        board.getLeftPanel().money(players[currentP]);
        board.getLeftPanel().card(players[currentP]); //updates display afterwards
        board.getLeftPanel().property(players[currentP]);
    }

    /**
     * Figures out the order of the players based on a preliminary dice roll for each.
     * @return An array of Players in the new order
     */
    public Player[] whoFirst()
    {
        Dice[] dice = new Dice[players.length]; //an array of dice, one for each player
        for(int i = 0; i < dice.length; i++) //instantiates all the dice
        {
            dice[i] = new Dice();
        }

        for(int i = 0; i < dice.length; i++) //a for loop going through the dice array
        {
            boolean keepGoing = true; //a boolean for cycling through a while loop
            while(keepGoing) //while the boolean is true
            {
                keepGoing = false; //set it to false immediately
                dice[i].roll(); //roll the dice
                for(int j = 0; j < dice.length; j++) //then another for loop for comparing two dice rolls
                {
                    if(dice[i].getRoll() == dice[j].getRoll() && i != j)
                    //if die 1 equals die 2 and they aren't the same die
                    {
                        keepGoing = true; //then reset keepGoing for a re-roll
                    }
                }
            }
        }

        HashMap<Integer, Player> orderByRoll = new HashMap<Integer, Player>();
        //a hashmap to relate a dice roll (an integer) with a player
        for(int i = 0; i < players.length; i++) //we then assign a dice roll to a player
        {
            orderByRoll.put(dice[i].getRoll(), players[i]);
        }

        Dice temp = new Dice(); //we make a temporary dice for the bubble sorting method
        boolean keepGoing = true; //we need another boolean for another while loop
        while(keepGoing)
        {
            keepGoing = false;
            for(int i = 0; i < dice.length - 1; i++) //go through the dice array (minus 1 because we add 1 later)
            {
                if(dice[i].getRoll() > dice[i+1].getRoll()) //if the dice roll at i is greater than at i+1
                {
                    temp = dice[i];
                    dice[i] = dice[i+1]; //we replace the lower index with the lower dice roll
                    dice[i+1] = temp;
                    keepGoing = true; //go and check through list again
                }
            }
        }
        Player[] newPlayers = players; //we then make a new players array
        for(int i = 0; i < dice.length; i++) //go through the dice length (same as player length)
        {
            newPlayers[i] = orderByRoll.get(dice[i].getRoll()); //and put the players in order by smallest roll
        }

        String message = "";  //a new string
        for(int i = 0; i < dice.length; i++)  //concate a new String of the order of players with their names
        {
            message = message + "\n \t       \t  Player #" + (i+1) + " is: " + newPlayers[i].getName();
        }

        Object[] options = {"That's Just the Way it Is!"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "After a Roll of the Die... " + message, "DICE ROLLS",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //An optionpane telling the players their turn order by inserting the concatenated string

        return newPlayers; //return this new order of players
    }

}