import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
/**
 * The variables and methods of an individual OutaJailCard in Monopoly. These cards are
 * keepable and take a person out of jail when in jail! Whoo!
 * 
 * @author Nicholas Smit
 * @version 04/25/2013
 */
public class OutaJailCard extends Card
{
    //CONSTRUCTOR    
    /**
     * Constructor for objects of class OutaJailCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public OutaJailCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
    }

    //METHODS
    /**
     * The special ability of this card. Can be used to let the player out of prison when they're in
     * prison.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Jail jail = (Jail)cardSorter.getManager().getSquare("jail"); //get the jail square
            Object[] options = {"Brilliant!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "You Used the Card and Escaped from Prison!",
                    "It Worked!", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    options, options[0]); //An optionpane telling the player the message on the card
            if(jail.removePlayer(player)) //if the jail is currently holding this player remove him
            {
                this.setPlayer(null);
                return true; //say it worked
            }
        }
        return false; //else it didn't
    }

    /**
     * Shown when the player draws this card.
     */
    public void outaJail()
    {
        Object[] options = {"This is My Lucky Day!"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription() + ". This Could Be Useful.",
                "Your Lucky Day", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]); //An optionpane telling the player the message on the card
    }
}