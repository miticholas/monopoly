import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual Jail card in Monopoly. These cards give a player a
 * cash bonus or fee.
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class MoneyCard extends Card
{
    //DATA MEMBERS    
    private int cost;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class moneyCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public MoneyCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
        cost = 0; //sets cost to zero
    }

    //METHODS
    /**
     * Gets the cost associated with the card.
     * @return An integer cost
     */
    public int getCost()
    {
        return cost;
    }

    /**
     * Sets the cost associated with the card.
     * @param cost The integer cost to be associated
     */
    public void setCost(int cost)
    {
        this.cost = cost;
    }

    /**
     * The special ability of this card. Adds or deducts money from the player who drew the card.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Object[] options = {"Well well well!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(),
                    "Money Comes and Goes", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    options, options[0]); //An optionpane telling the player the message on the card

            if(player.addMoney(this.cost)) //adds (or subtracts) the cost of this card to the player
            {
                if(cost < 0)
                {
                    ((FreeParking)cardSorter.getGame().getManager().getSquare("Free Parking")).addToPot(-cost);
                }
                return true; //then say it worked
            }
        }
        return false; //otherwise it didn't
    }
}