import java.util.ArrayList;
/**
 * The variables and methods of an individual player in the game of Monopoly.
 * 
 * @author Nicholas Smit and Lukas Edman
 * @version 04/15/13
 */
public class Player
{
    //DATA MEMBERS
    private boolean stillPlaying;
    private boolean passedGo;
    private ArrayList<Property> property;
    private ArrayList<Card> cards;
    private int assets, money, prevMoney, turn, position;
    private Token token;
    private String name;

    //CONSTRUCTOR
    /**
     * The Constructor for the Player Class.
     */
    public Player()
    {
        this.cards = new ArrayList<Card>();
        this.property = new ArrayList<Property>();
        this.money = 1500;
        this.prevMoney = 1500;
        this.stillPlaying = true;
        this.passedGo = false;
        this.name = null;
        this.assets = 1500;
        this.turn = 0;
        this.position = 0;
        this.token = null;
    }

    //METHODS
    /**
     * Adds money to the player's amount of assets.
     * @param amount The integer amount to be added
     */
    public void addAssets(int assets)
    {
        this.assets += assets;
    }

    /**
     * Adds a card to the Card list of the player.
     * @param card The Card to be added to the player's list
     * @return A boolean of whether it worked
     */
    public boolean addCard(Card card)
    {
        if(!cards.contains(card)) //if the card isn't in the list
        {
            this.cards.add(card); //add it to the list
            return true; //return that it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Adds money to the player's amount of money.
     * @param amount The integer amount to be added
     */
    public boolean addMoney(int amount)
    {
        this.addAssets(amount);
        prevMoney = this.money;
        this.money += amount; //subtract the amount from the player's money
        if(this.money + amount > 0)//if player's money minus the parametered amount isn't negative
        {
            return true; //return that it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Adds a property to the property list of the player.
     * @param prop The Property to be added to the player's list
     * @return A boolean of whether it worked
     */
    public boolean addProperty(Property prop)
    {
        if(!property.contains(prop)) //if the property isn't on the list
        {
            this.property.add(prop); //add it to the list
            Property[] array = property.toArray(new Property[0]);
            Property temp = null;
            boolean flag = true;
            while(flag)
            {
                flag = false;
                for(int i = 0; i < array.length - 1; i++)
                {
                    if(array[i].getPosition() > array[i+1].getPosition())
                    {
                        temp = array[i];
                        array[i] = array[i+1];
                        array[i+1] = temp;
                        flag = true;
                    }
                }
            }
            addAssets(prop.getWorth());
            removeMoney(prop.getPrice(0));
            return true; //say it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Adds a turn to the number of turns this player has had.
     */
    public void addTurn()
    {
        this.turn++;
    }

    /**
     * Calculates the position the player will move to.
     * @param roll The integer number of squares they move based on dice roll
     */
    public void calculatePo(int roll)
    {
        if(roll + position >= SquaresManager.SQUARES)
        //if the roll plus current position is greater than 40
        {
            this.passedGo = true;
        }
        this.setPosition((roll + position)%SquaresManager.SQUARES);
        //use the modulo operator to find the remainder with the number of squares
    }

    /**
     * Gets the total assets of the player.
     * @return The integer amount of assets of the player
     */
    public int getAssets()
    {
        return this.assets;
    }

    /**
     * Gets the cards currently in the Player's hand.
     * @return An ArrayList of cards
     */
    public ArrayList<Card> getCards()
    {
        return this.cards;
    }

    /**
     * Gets the player's amount of money.
     * @return The integer amount of money
     */
    public int getMoney()
    {
        return this.money;
    }

    /**
     * Gets the name of the player.
     * @return The String name associated with the player
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the current position of the player.
     * @return An integer representation of his position on the board
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * Gets the previous amount of money the player had.
     * @return An integer amount of money they had
     */
    public int getPrevMoney()
    {
        return this.prevMoney;
    }

    /**
     * Gets the property array list of the player.
     * @return The property array list of the player
     */
    public ArrayList<Property> getProperty()
    {
        return this.property;
    }

    /**
     * Gets the parametered property from the player's list of properties.
     * @param prop The property to be returned
     * @return The property requested, null if it's not there
     */
    public Property getProperty(Property prop)
    {
        if(property.contains(prop)) //if the property is in the list
        {
            int x = this.property.indexOf(prop); //new variable that is the index of property
            return this.property.get(x); //returns the property
        }
        return null; //otherwise returns null
    }

    /**
     * Gets the token corresponding to this player.
     * @return the Token attributed
     */
    public Token getToken()
    {
        return this.token;
    }

    /**
     * Gets the number of turns this player has had.
     * @return An integer number of turns
     */
    public int getTurn()
    {
        return turn;
    }

    /**
     * Cehcks if the parametered card is in the player's list of cards.
     * @param card The card to be checked
     * @return A boolean of whether it's there or not
     */
    public boolean hasCard(Card card)
    {
        if(cards.contains(card)) //if the card list contains the card
        {
            return true; //the card is there
        }
        return false; //otherwise it isn't
    }

    /**
     * Checks if a property is in the property list of the player.
     * @param prop The Property to be checked in the player's list
     * @return A boolean of whether it worked
     */
    public boolean hasProperty(Property prop)
    {
        if(property.contains(prop)) // if the property is in the list
        {
            return true; //say it is
        }
        return false; //otherwise it isn't
    }

    /**
     * Gets whether or not the player has passed go.
     * @return A boolean of whether or not they did
     */
    public boolean hasPassedGo()
    {
        return passedGo;
    }

    /**
     * Subtracts money from the player's amount of assets.
     * @param amount The integer amount to be subtracted
     * @return A boolean of whether it worked
     */
    public boolean removeAssets(int amount)
    {
        if(this.assets - amount > 0)//if player's money minus the parametered amount isn't negative
        {
            this.assets -= amount; //subtract the amount from the player's money
            return true; //return that it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Subtracts a card from the Card list of the player.
     * @param card The Card to be subtracted from the player's list
     * @return A boolean of whether it worked
     */
    public boolean removeCard(Card card)
    {
        if(cards.contains(card)) //if the card is in the list
        {
            this.cards.remove(card); //remove it from the list
            return true; //return that it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Subtracts money from the player's amount of money.
     * @param amount The integer amount to be subtracted
     * @return A boolean of whether it worked
     */
    public boolean removeMoney(int amount)
    {
        removeAssets(amount);
        prevMoney = this.money;
        this.money -= amount; //subtract the amount from the player's money

        if(this.money - amount > 0)//if player's money minus the parametered amount isn't negative
        {
            return true; //return that it worked
        }
        else
        {
            return false;
        }
    }

    /**
     * Subtracts a property from the property list of the player.
     * @param prop The Property to be subracted from the player's list
     * @return A boolean of whether it worked
     */
    public boolean removeProperty(Property prop)
    {
        if(property.contains(prop)) //if the property is on the list
        {
            removeAssets(prop.getWorth());
            this.property.remove(prop); //remove it from the list
            return true; //say it worked
        }
        return false; //otherwise it didn't
    }

    /**
     * Sets the name of the player.
     * @param newName The String name to be associated with the player
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets whether or not the player is still playing.
     * @param stillPlaying The boolean of whether or not they're still playing
     */
    public void setPlaying(boolean stillPlaying)
    {
        this.stillPlaying = stillPlaying;
    }

    /**
     * Sets the current position of the player to a tile on the board.
     * @param tile The tile the player is setting his position to
     */
    public void setPosition(int tile)
    {
        this.position = tile;
        this.getToken().setPosition(tile);
    }

    /**
     * Sets whether or not the player has passed go.
     * @param passedGo A boolean saying they passed go or not
     */
    public void setPassedGo(boolean passedGo)
    {
        this.passedGo = passedGo;
    }

    /**
     * Sets the previous amount of money the player had.
     * @param The previous amount of money the player had
     */
    public void setPrevMoney(int prevMoney)
    {
        this.prevMoney = prevMoney;
    }

    /**
     * Attributes a token to a player.
     * @param token The token set to a player
     */
    public void setToken(Token token)
    {
        this.token = token;
    }

    /**
     * Checks if the player is still in the game.
     * @return A boolean of whether they are or not
     */
    public boolean stillPlaying()
    {
        return this.stillPlaying;    
    }
}