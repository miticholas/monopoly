import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
/**
 * The variables and methods of an individual card in Monopoly. The abstract parent class to all
 * Monopoly Cards.
 * 
 * @author Nicholas Smit
 * @version 05/07/2013
 */
public abstract class Card
{
    //DATA MEMBERS    
    protected String description;
    protected CardSorter cardSorter;
    protected CardTypes type;
    protected JOptionPane pane;
    protected ImageIcon image;
    protected Player player;

    //CONSTRUCTOR
    /**
     * The Constructor for the Card Class.
     * @param cardSorter The CardSorter in control of the cards for the game for communication
     */
    public Card(CardSorter cardSorter)
    {
        description = null; //sets default description to nothing
        this.cardSorter = cardSorter; //sets this cardSorter to be the parametered one
        pane = new JOptionPane();
        image = null;
        player = null;
    }

    //METHODS
    /**
     * Gets the String description associated with the card.
     * @return The String description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Gets the Player who is currently holding the card.
     * @return The Player holding the card
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Gets the enumerated CardTypes associated with the card.
     * @return The enumerated CardTypes value
     */
    public CardTypes getType()
    {
        return this.type;
    }

    /**
     * An abstract play method to be described differently in each child Card class.
     * @return A boolean of whether it worked or not
     */
    public abstract boolean play();

    /**
     * Gets the image icon associated with the card
     * @return the ImageIcon associated
     */
    public ImageIcon getImage()
    {
        return this.image;
    }

    /**
     * Sets the description associated with the card.
     * @param description The String association
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets the icon image of the card.
     * @param image The image icon to be associated with the card
     */
    public void setImage(ImageIcon image)
    {
        this.image = image;
    }

    /**
     * Sets the player who currently is holding this card.
     * @param player The player holding the card
     */
    public void setPlayer(Player player)
    {
        this.player = player;
    }

    /**
     * Sets the type of card it is, based on enumerated values.
     * @param type An enumerated CardTypes value
     */
    public void setType(CardTypes type)
    {
        this.type = type;
    }
}