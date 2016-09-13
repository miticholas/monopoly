import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual Income Tax square in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class IncomeTax extends Square
{
    //DATA MEMBERS    
    private int taxes;

    //CONSTRUCTOR
    /**
     * Constructor for the IncomeTax Class.
     * @param manager The SquareManager in charge of this square
     */
    public IncomeTax(SquaresManager manager)
    {
        super(manager); //calls parent's constructor
        taxes = 0; //sets to default 0
    }

    //METHODS
    /**
     * A popup Option Menu makes players choose between paying $200 or 10% of their assets.
     * @param player The player who is faced with the fee
     * @return A boolean of whether they could pay the taxes or not
     */
    public boolean payTax(Player player)
    {   
        affordable = true;
        do
        {
            taxes = (int)Math.round(player.getAssets() * 0.10); //calculates 10% of player's assets

            Object[] options = {"$200", "Income Tax"}; //custom button names
            int n = JOptionPane.showOptionDialog(pane, "Will you pay $200 or the Income Tax ( $"
                    + taxes + ")?", "INCOME TAXES DUE!", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //an optionpane that allows players to either pay 10% taxes or $200

            if(n == JOptionPane.YES_OPTION) //if they select the $200
            {
                taxes = 200; //switch taxes to be 200
            }
            if(player.removeMoney(taxes)) //now remove that money from the player if possible
            {
                FreeParking.addToPot(taxes); //and add it to the middle
                return true; //return that it worked
            }
            else
            {
                affordable = false;
            }
        }while(!affordable);
        return false; //else they didn't have the money and it didn't
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of whether they could pay or not
     */
    public boolean useSquare(Player player)
    {
        if(payTax(player))
        {
            return true;
        }
        return false;
    }
}