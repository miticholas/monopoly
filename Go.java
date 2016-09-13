import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual GO square of Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/15/2013
 */
public class Go extends Square
{
    //DATA MEMBERS    
    private int bonus;

    //CONSTRUCTOR
    /**
     * The Constructor for the Property Class.
     * @param manager The SquareManager in charge of this square
     */
    public Go(SquaresManager manager)
    {
        super(manager);
        bonus = 200;
    }

    //METHODS
    /**
     * Gives $200 to the parametered player. A popup Option menu tells people to
     * confirm they just won big.
     * @param player The player who landed on the square
     */
    public void passGo(Player player)
    {
        Object[] options = {"Sweet!"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "You Passed Go - Collect $200!",
                "PASSED GO", JOptionPane.YES_NO_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //A window congratulating the player for passing go

        player.addMoney(bonus); //adds this amount of money to player's money box
    }
    
    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of whether they could pay or not
     */
    public boolean useSquare(Player player)
    {
        passGo(player);
        return true;
    }
}