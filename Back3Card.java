import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual RailRoadCard in Monopoly. These cards move
 * a player to the nearest railroad square and make them pay double the rent!
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class Back3Card extends Card
{
    //CONSTRUCTOR    
    /**
     * Constructor for objects of class Back3Spaces
     * @param cardSorter The CardSorter in charge of this card
     */
    public Back3Card(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
    }

    //METHODS
    /**
     * The special ability of this card. Moves the player back three squares.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Object[] options = {"Dang"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "Bother!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    //An optionpane telling the player the message on the card
            player.setPosition(player.getPosition() - 3); //moves that player's position back three squares
            player.getToken().setPosition(player.getPosition());
            player.getToken().setDimension(player.getPosition());
            cardSorter.getGame().getBoard().repaint();
            return true; //return that it worked
        }
        return false; //else it didn't
    }
}