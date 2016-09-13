/**
 * The variables and methods of an individual RailRoad tile of Monopoly Property.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class RailRoad extends Property
{
    //CONSTRUCTOR
    /**
     * Constructor for the RailRoad Class. Borrows parent Constructor, but changes the
     * prices array to length 4.
     * @param manager The SquareManager in charge of this square
     */
    public RailRoad(SquaresManager manager)
    {
        super(manager);
        prices = new int[3];
        /**
         * 0 = buying price         1 = rent
         * 2 = mortgage price
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
     * Overriding method that gets the cost for purchasing railroad property.
     * @return An integer price associated with the railroad
     */
    public int getWorth()
    {
        return prices[0];
    }

    /**
     * Makes the player pay a rent dependent on who owns the other properties.
     * @param factor A boolean of whether or not someone is using the railroad card.
     * @return The integer amount of rent
     */
    public int payRent(boolean factor)
    {
        double rentDouble = -1;
        Player[] owners = manager.getOwners(this.color);
        for(int i = 0; i < owners.length -1; i++)
        {
            if(owners[i] != null)
            {
                if(owners[i] == this.getOwner())
                {
                    rentDouble++;
                }
            }
        }
        rentDouble = 25*Math.pow(2, rentDouble);
        if(factor)
        {
            rentDouble *= 2;
        }
        int rent = (int)rentDouble;     
        return rent;
    }

    /**
     * A second payRent() method that calls the first with a default false.
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