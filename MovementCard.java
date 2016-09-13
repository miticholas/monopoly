import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual MovementCard in Monopoly. These cards move the player
 * across the board to a specific tile.
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class MovementCard extends Card
{
    //DATA MEMBERS    
    private int destination;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class movementCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public MovementCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls parent's constructor
        destination = 0; //movement set to null
    }

    //METHODS
    /**
     * Gets the movement - where the player gets moved to - associated with the card.
     * @return A Square to have the player move to.
     */
    public int getDestination()
    {
        return destination;
    }

    /**
     * Sets the movement - where the player gets moved to - associated with the card.
     * @param movement The property to be moved to
     */
    public void setDestination(int destination)
    {
        this.destination = destination;
    }

    /**
     * The special ability of this card. Moves the player to the square described on the card. If
     * its a property they got to pay the rent.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Object[] options = {"Gee Whiz!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "Movement Good!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            //An optionpane telling the player the message on the card

            if(player.getPosition() > destination)
            {
                player.setPassedGo(true);
            }
            player.setPosition(destination); //move the player to the destination
            cardSorter.getGame().moveToken();
        }
        return false; //otherwise it didn't
    }
}