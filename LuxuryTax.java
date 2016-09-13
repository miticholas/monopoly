import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual GO square of Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class LuxuryTax extends Square
{
    //DATA MEMBERS    
    private int tax;

    //CONSTRUCTOR
    /**
     * The Constructor for the LuxuryTax Class.
     * @param manager The SquareManager in charge of this square
     */
    public LuxuryTax(SquaresManager manager)
    {
        super(manager); //calls the parent's method
        tax = 75; //sets Luxury tax to default 75
    }

    //METHODS
    /**
     * A popup Option Menu tells players to confirm they just lost for living luxurious.
     * @param player The player who is faced with the fee
     * @return A boolean of whether they could pay or not
     */
    public boolean payTax(Player player)
    {
        Object[] options = {"Gross!"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "You landed on Luxury Tax! - Pay $75!",
                "HOLY COMOLLI", JOptionPane.YES_NO_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //An optionpane telling the player they had to fork over $75!

        if(player.removeMoney(tax)) //remove that money from the player if possible
        {
            FreeParking.addToPot(tax); //add that money to the pot
            return true; //return it worked
        }
        return false; //else they didn't have enough and it didn't work
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of whether they could pay or not
     */
    public boolean useSquare(Player player)
    {
        if(this.payTax(player))
        {
            return true;
        }
        return false;
    }
}