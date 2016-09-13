import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual EachPlayerCard in Monopoly. These cards make a
 * player gain or give money from/to each player.
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class EachPlayerCard extends Card
{
    //DATA MEMBERS    
    private int cost;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class EachPlayerCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public EachPlayerCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls parent's constructor
        cost = 0; //sets cost to default zero
    }

    //METHODS
    /**
     * Sets the cost to the parametered amount.
     * @param cost The integer cost of this specfic card
     */
    public void setCost(int cost)
    {
        this.cost = cost;
    }

    /**
     * The special ability of this card. Takes/gives money from/to all other players and gives/takes to/away
     * the player who drew the card.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Player[] players = cardSorter.getGame().getPlayers(); //get all the players in the game
            int amount = 0; //make an int amount starting at zero

            Object[] options = {"Oh Shucks!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "Well Lookie Here!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            //An optionpane telling the player the message on the card
            for(int i = 0; i < players.length; i++) //go through the list of players
            {
                if(players[i] != player) //if the player isn't this player
                {
                    if(!players[i].addMoney(-cost)) //give them the cost
                    {
                        return false;
                    }
                    amount += cost; //increase the amount by the cost to charge to the player
                }
            }
            if(player.addMoney(amount))
            {
                return true; //return that it worked
            }
        }
        return false; //otherwise it didn't
    }
}