import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual GoToJail square in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/23/2013
 */
public class GoToJail extends Square
{
    //CONSTRUCTOR
    /**
     * Constructor for objects of class GoToJail
     * @param manager The SquaresManager in charge of this square
     */
    public GoToJail(SquaresManager manager)
    {
        super(manager); //calls the parent's method
    }

    //METHODS
    /**
     * Sends the player landing on this square to jail.
     * @param player The current player on this square
     * @return A boolean of whether it worked or not
     */
    public boolean sendToJail(Player player)
    {
        Jail jail = (Jail)manager.getSquare("jail"); //get the jail square
        if(jail.addPlayer(player)) //if this person isn't in jail, add them
        {
            Object[] options = {"Filthy Coppers"}; //custom button names
            int n = JOptionPane.showOptionDialog(pane, "You're Going to Jail Bucko!",
                    "They Found You!", JOptionPane.OK_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //An optionpane that tells players they're going to jail
            return true;
        }
        return false;
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of whether they could go to jail or not
     */
    public boolean useSquare(Player player)
    {
        if(this.sendToJail(player))
        {
            return true;
        }
        return false;
    }
}