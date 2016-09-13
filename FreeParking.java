import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual FreeParking sqaure in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class FreeParking extends Square
{
    //DATA MEMBERS    
    private static int jackPot;

    //CONSTRUCTOR
    /**
     * The Constructor for the FreeParking Class.
     * @param manager The SquareManager that is in control of this square
     */
    public FreeParking(SquaresManager manager)
    {
        super(manager); //calls the parent's method
        jackPot = 0; //sets jackpot to zero
    }

    //METHODS
    /**
     * Adds the parametered amount to the free parking jackpot.
     * @param amount The integer amount added to the jackpot
     */
    public static void addToPot(int amount)
    {
        jackPot += amount;
    }

    /**
     * Gives the jackpot to the parametered player. A popup Option menu tells people to
     * confirm they just won big.
     * @param player The player receiving the jackpot
     */
    public void jackPot(Player player)
    {
        Object[] options = {"Yipee!"}; //button will be Yipee!
        int n = JOptionPane.showOptionDialog(pane, "You Landed on Free Parking - you got $"
                + jackPot + "!", "JACKPOT!", JOptionPane.OK_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //An optionpane that tells them they won money!

        player.addMoney(jackPot); //adds jackpot money to the player's account
        jackPot = 0; //resets jackpot
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of whether they could pay or not
     */
    public boolean useSquare(Player player)
    {
        this.jackPot(player);
        return true;

    }
}