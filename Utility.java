/**
 * The variables and methods of an individual Utility tile of Monopoly Property.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class Utility extends Property
{
    //CONSTRUCTOR
    /**
     * Constructor for the Utility Class. Borrows parent Constructor, but changes the
     * prices array to length 3.
     * @param manager The SquareManager in charge of this square
     */
    public Utility(SquaresManager manager)
    {
        super(manager);
        prices = new int[4];
        /*for all the price specifics: 0 = buying price; 1 = rent; 2 = monopoly rent;
         * 3 = mortgage;
         */
    }

    //OVERRIDING METHODS
    /**
     * Overrides parent method so no houses can be added.
     * @return A boolean always of false
     */
    public boolean addHouse()
    {
        return false;
    }

    /**
     * Overriding method that gets the worth for purchasing utility property.
     * @return An integer price associated with this property
     */
    public int getWorth()
    {
        return prices[0];
    }

    /**
     * Returns what the current price of renting this square would be.
     * @param factor An integer of whether it should be factored by 10
     * @return An integer rental cost
     */
    public int payRent(boolean factor)
    {
        int rent = 0;
        Dice[] dice  =  manager.getGame().getDice();
        for(int i = 0; i < dice.length; i++)
        {
            rent += dice[i].getRoll();
        }

        if(manager.checkMonopoly(this.color) || factor)
        {    
            rent *= 10;
        }
        else
        {
            rent *= 4;
        }
        return rent;
    }

    /**
     * A second payRent() method that calls the first with a default 0.
     * @return An integer rental
     */
    public int payRent()
    {
        return payRent(false);
    }

    /**
     * Overrides parent method so no houses can be subtracted.
     * @return A boolean always of false
     */
    public boolean removeHouse()
    {
        return false;
    }
}