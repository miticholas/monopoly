import javax.swing.JOptionPane;
import java.awt.*;
/**
 * The variables and methods of an individual tile of Monopoly Property.
 * 
 * @author Nicholas Smit
 * @version 4/15/2013
 */
public class Property extends Square
{
    //DATA MEMBERS
    protected Color color;
    protected int houses;
    protected Player owner;
    protected int[] prices;
    protected int worth;
    protected boolean isMortgaged;
    protected String name;

    //CONSTRUCTOR
    /**
     * The Constructor for the Property Class.
     * @param manager The SquareManager in charge of this square
     */
    public Property(SquaresManager manager)
    {
        super(manager);
        owner = null;
        houses = 0;
        color = null;
        isMortgaged = false;
        prices = new int[9];
        /*for all the price specifics: 0 = buying price; 1 = rent;
         * 2 = one, 3 = two, 4 = three, 5 = four house rent, 6 = hotel rent;
         * and 7 = mortgage price; 8 = cost of house */
    }

    //METHODS
    /**
     * Adds houses to the current number of houses on the property.
     * @return a boolean checking whether it's possible to add more houses
     */
    public boolean addHouse()
    {
        if(houses + 1 <= 5) //if adding a house to this property is equal to or less than five
        {
            houses ++; //then add away
            return true;
        }
        return false;
    }

    /**
     * Gets the color group (as an integer) of the property.
     * @return the integer property group number
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Gets the number of houses on the property.
     * @return the integer number of houses
     */
    public int getHouses()
    {
        return houses;
    }

    /**
     * Gets whether or not the property is mortgaged.
     * @return A boolean of whether it's mortgaged or not
     */
    public boolean getMortgage()
    {
        return isMortgaged;
    }

    /**
     * gets the current owner of the property
     * @return the player currently owning the property
     */
    public Player getOwner()
    {
        return owner;
    }

    /**
     * Gets whichever price is at the parametered index.
     * @param i an integer index variable to go to that spot in the prices array
     * @return an integer, some monetary cost unique to the property
     */
    public int getPrice(int i)
    {
        return prices[i];
    }

    /**
     * Gets the whole array of prices associated with the property.
     * @return an int array of all the prices
     */
    public int[] getPriceArray()
    {
        return prices;
    }

    /**
     * Gets the worth of the property, including houses. Dependent on mortgaged or not.
     * @return The integer worth of the property
     */
    public int getWorth()
    {
        return ((houses * prices[8]) + prices[0]);
    }

    /**
     * Gets the amount the player landing on the property must pay as rent.
     * @return An integer amount they must pay
     */
    public int payRent()
    {
        int rent;

        if(houses == 0) //if there are no houses on the property
        {
            if(manager.checkMonopoly(color)) //check if it's a monopoly, if yes
            {
                rent = prices[1]*2; //rent is times two
            }
            else
            {
                rent = prices[1]; //else it's just normal rent
            }
        }
        else //if there are houses
        {
            rent = prices[houses + 1]; //get the price associated with that number of houses
        }
        return rent;
    }

    /**
     * Subtracts houses from the current number of houses on the property.
     * @return a boolean checking whether it's possible to subtract more houses
     */
    public boolean removeHouse()
    {
        if(houses - 1 >= 0) //if subtracting a house still leads it to be above or equal to zero
        {
            houses --; //subtract away
            return true;
        }
        return false;
    }

    /**
     * Sets the Color group this property belongs to.
     * @param color The String to be the new color of the property
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Sets whether or not the property is mortgaged.
     * @param mortgage A boolean of whether or it is mortgaged
     */
    public void setMortgage(boolean mortgage)
    {
        isMortgaged = mortgage;
    }

    /**
     * Sets the owner of the property.
     * @param owner The player to be the new owner of the property
     */
    public void setOwner(Player owner)
    {
        this.owner = owner;
    }

    /**
     * Sets the price at index i of the array to the input parametered price.
     * @param i The integer index point for in the price array
     * @param price The integer price to have it set to
     */
    public void setPrice(int i, int price)
    {
        this.prices[i] = price;
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player currently using the square
     * @return A boolean of whether it worked or not
     */
    public boolean useSquare(Player player)
    {
        if(manager.payRent(player, this))
        {
            return true;
        }
        return false;
    }
}