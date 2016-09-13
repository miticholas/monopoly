import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * The variables and methods associated with the CardSorter Class. It loads all the data and sorts the 
 * Cards into Chance and Community Chest piles.
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class CardSorter
{
    //DATA MEMBERS    
    public static final int STACK_SIZE = 16; //number of cards in a deck
    private ArrayList<Card> chance; //for the Chance stack
    private ArrayList<Card> communityChest; //for the Community Chest stack
    private SquaresManager manager;
    private Game game;

    //CONSTRUCTOR
    /**
     * Constructor for the CardSorter class.
     * @param game The Game currently being played, for communication.
     */
    public CardSorter(Game game)
    {
        communityChest = new ArrayList<Card>(); //instantiates with stack size
        chance = new ArrayList<Card>();
        this.game = game; //sets this game to be that of the current game
        this.manager = game.getManager();

        loadData();
        shuffleCards();
    }

    //METHODS
    /**
     * Returns a card back to the correct pile
     * @param card the card being returned
     */
    public void addCard(Card card)
    {
        if(card.getImage().equals(new ImageIcon("images/out_of_jail_chest.png")))
        {
            communityChest.add(card);
        }
        else if(card.getImage().equals(new ImageIcon("images/out_of_jail_chance.png")))
        {
            chance.add(card);
        }
    }

    /**
     * Associates the number parametered with a certain enumerated CardTypes value.
     * @param x An integer to be associated with a CardTypes
     * @return The CardTypes associated with the integer
     */
    public CardTypes associateType(int x)
    {
        if(x == 1) //if x is equal to this number
            return CardTypes.MOVEMENT; //return this enumerated value
        else if(x == 2)
            return CardTypes.MONEY;
        else if(x == 3)
            return CardTypes.OUTA_JAIL;
        else if(x == 4)
            return CardTypes.JAIL;
        else if(x == 5)
            return CardTypes.REPAIRS;
        else if(x == 6)
            return CardTypes.UTILITY;
        else if(x == 7)
            return CardTypes.RAILROAD;
        else if(x == 8)
            return CardTypes.THREE_SPACES;
        else
            return CardTypes.EACH;
    }

    /**
     * Draws the next card in the Chance or Community pile.
     * @param player The player who is drawing the card
     * @param chanceCard A boolean, true = chance, false = community
     */
    public void drawCard(Player player, boolean chanceCard)
    {
        if(chanceCard)
        {
            this.drawCardHelper(player, chance);
        }
        else
        {
            this.drawCardHelper(player, communityChest);
        }
    }

    /**
     * A helper method which draws the next card in the parametered pile.
     * @param player The player who is drawing the card
     * @param cardList An arraylist of Cards to draw from.
     */
    private void drawCardHelper(Player player, ArrayList<Card> cardList)
    {
        Card cardToRemove = null; // a new card equal to nothing!
        cardList.get(0).setPlayer(player); //the card's player is the current player
        if(cardList.get(0).getType() != CardTypes.OUTA_JAIL) //if the card isn't an out of jail card
        {
            cardList.get(0).play(); //play it's ability
            cardToRemove = cardList.get(0); //since we can't remove while going through a foreach, we remove it outside
        }
        else //else the card must be a get out of jail card
        {
            ((OutaJailCard)cardList.get(0)).outaJail(); //if they don't we tell them its ability but don't play it
        }
        player.addCard(cardList.get(0)); //adds the top card to the hand of player who is drawing
        cardList.remove(cardList.get(0)); //removes the card from the list
        if(cardToRemove != null) //in other words, if it wasn't a get out of jail free card
        {
            cardToRemove.setPlayer(null);
            cardList.add(cardToRemove); //re-add it to the chance card pile (now last)
            player.removeCard(cardToRemove); //remove it from the player's hand
        }
        game.getBoard().getLeftPanel().card(player);
    }

    /**
     * Gets the game currently being played.
     * @return The Game being played
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * Gets the current squareManager being used.
     * @return The SquaresManager being used
     */
    public SquaresManager getManager()
    {
        return game.getManager();
    }

    /**
     * Loads all the Card data from the cards.txt file and sets them.
     */
    public void loadData()
    {
        try //try to load file
        {
            File file = new File("texts/cards.txt"); //make the cards.txt file into a new file
            Scanner scanner = new Scanner(file); //creates a new scanner
            ArrayList<Card> cards = new ArrayList<Card>(); //makes a new local cards list
            cards = communityChest; //first loads cards to the community chest array

            for(int i = 0; scanner.hasNextLine(); i++) //go through word array
            {
                String nextLine = scanner.nextLine(); //reads next line in file
                String[] section = nextLine.split(", "); //splits up a line of words into individual words

                if(nextLine.equals("")) //if the scanner hits a space
                {
                    cards = chance; //loads rest of cards to the chance pile array
                    cards.clear(); //clears the cards list so it adds only new ones to chance pile
                }
                else
                {
                    int sec0 = 0; //an integer to express section 0
                    int x = Integer.parseInt(section[sec0]); //x is the int of section 0
                    if(x == 1) //if sec0 is 1, then follow these instructions to mae a movement card
                    {
                        MovementCard move = new MovementCard(this);
                        move.setType(this.associateType(x)); //sets the type to that in section 0

                        Square square = manager.getSquare(Integer.parseInt(section[sec0+1]));
                        move.setDestination(square.getPosition());

                        //sets the destination to what's in section 1
                        move.setDescription(section[sec0+2]);
                        //sets the description to what's in section 2

                        cards.add(move); //add the set up card to the cards list
                    }
                    if(x == 2) //if 2 follow these for a money card
                    {
                        MoneyCard money = new MoneyCard(this);
                        money.setType(this.associateType(x));
                        money.setCost(Integer.parseInt(section[sec0+1]));
                        money.setDescription(section[sec0+2]);

                        cards.add(money);
                    }
                    if(x == 3) //if 3 follow these for a outajail card
                    {
                        OutaJailCard outa = new OutaJailCard(this);
                        outa.setType(this.associateType(x));
                        outa.setDescription(section[sec0+2]);
                        if(chance.size() == 0)
                        {
                            BufferedImage image = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/out_of_jail_chest.png"));
                            Image newImage = image.getScaledInstance((int)190,(int)140,Image.SCALE_SMOOTH);
                            outa.setImage(new ImageIcon(newImage));
                        }
                        else
                        {
                            BufferedImage image = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/out_of_jail_chance.png"));
                            Image newImage = image.getScaledInstance((int)190,(int)140,Image.SCALE_SMOOTH);
                            outa.setImage(new ImageIcon(newImage));
                        }
                        cards.add(outa);
                    }
                    if(x == 4) //if 4 follow these for a jail card
                    {
                        JailCard jail = new JailCard(this);
                        jail.setType(this.associateType(x));
                        jail.setDescription(section[sec0+2]);

                        cards.add(jail);
                    }
                    if(x == 5) //if 5 follow these for a repairs card
                    {
                        RepairsCard repair = new RepairsCard(this);
                        repair.setType(this.associateType(x));
                        repair.setDescription(section[sec0+3]);
                        repair.setHouse(Integer.parseInt(section[sec0+1]));
                        repair.setHotel(Integer.parseInt(section[sec0+2]));

                        cards.add(repair);
                    }
                    if(x == 6) //if 6 follow these for a utility card
                    {
                        UtilityCard util = new UtilityCard(this);
                        util.setType(this.associateType(x));
                        util.setDescription(section[sec0+2]);
                        util.setFactor(Integer.parseInt(section[sec0+1]));

                        cards.add(util);
                    }
                    if(x == 7) //if 7 follow these for a railroad card
                    {
                        RailRoadCard rail = new RailRoadCard(this);
                        rail.setType(this.associateType(x));
                        rail.setDescription(section[sec0+2]);
                        rail.setFactor(Integer.parseInt(section[sec0+1]));

                        cards.add(rail);
                    }
                    if(x == 8) //if 8 follow these for a back3 card
                    {
                        Back3Card back = new Back3Card(this);
                        back.setType(this.associateType(x));
                        back.setDescription(section[sec0+2]);

                        cards.add(back);
                    }
                    if(x == 9) //if 9 follow these for a eachplayer card
                    {
                        EachPlayerCard each = new EachPlayerCard(this);
                        each.setType(this.associateType(x));
                        each.setCost(Integer.parseInt(section[sec0+1]));
                        each.setDescription(section[sec0+2]);

                        cards.add(each);
                    }
                }
            }
        }
        catch (IOException e)//otherwise catch if any problem occurs while reading the file
        {
            System.out.println ("Error reading file: " + e.getMessage());
            e.printStackTrace(); //show the stack trace so we can track down the error
        }
    }

    /**
     * Shuffles the cards in both decks so as to randomize them.
     */
    public void shuffleCards()
    {
        Collections.shuffle(chance);
        Collections.shuffle(communityChest);
    }
}