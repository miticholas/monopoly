import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual Jail card in Monopoly. These cards send players right
 * to jail. YEAYUH!
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class JailCard extends Card
{
    //CONSTRUCTOR    
    /**
     * Constructor for objects of class JailCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public JailCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
    }

    //METHODS
    /**
     * The special ability of this card. Immediately moves the person to jail if they aren't
     * already there.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Jail jail = (Jail)cardSorter.getManager().getSquare("jail"); //get the jail square
            Object[] options = {"This is My Unlucky Day!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(),
                    "Your Unlucky Day", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    options, options[0]); //An optionpane telling the player the message on the card
            if(jail.addPlayer(player)) //if this person isn't in jail add them
            {
                return true; //say it worked!
            }
            return true; //if they were already in jail it kinda still worked since they're there
        }
        return false; //else it didn't
    }
}