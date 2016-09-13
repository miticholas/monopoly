import java.util.HashMap;
import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual Jail square in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/23/2013
 */
public class Jail extends Square
{
    //DATA MEMBERS    
    private HashMap<Player, Integer> playerAndTime;

    //CONSTRUCTOR
    /**
     * Constructor for the Jail Class.
     * @param manager The SquareManager in charge of this square
     */
    public Jail(SquaresManager manager)
    {
        super(manager);
        playerAndTime = new HashMap<Player, Integer>();
    }

    //METHODS
    /**
     * Adds a player to the jailing HashMap and the turn # they entered jail.
     * @param player The player getting sent to jail
     * @return A boolean of whether or not they're in jail
     */
    public boolean addPlayer(Player player)
    {
        if(!playerAndTime.containsKey(player))
        {
            playerAndTime.put(player, player.getTurn());
            player.setPosition(this.getPosition());
            player.getToken().setDimension(this.getPosition());
            manager.getGame().getBoard().repaint();
            return true;
        }
        return false;
    }

    /**
     * Opens an OptionPane that allows players their way out pay out of jail.
     * @param player The player who's in jail
     * @return A boolean of whether or not they accepted the bribery
     */
    public boolean applyBribery(Player player)
    {
        Object[] options = {"Deal!", "My Morals Prevent Me"}; //custom button names
        int n = JOptionPane.showOptionDialog(pane,
                "Listen, I'll let you go clean if you give me $50", "An Offer You Can Refuse",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //an optionpane that allows players to break out of jail or not

        if(n == JOptionPane.YES_OPTION) //if they chose yes
        {
            if(player.removeMoney(50)) //if removing 50 bucks works
            {
                this.removePlayer(player);
                Object[] moreOptions = {"You Bet"}; //custom button names
                n = JOptionPane.showOptionDialog(pane,
                    "Nice Doing Business With Ya", "An Offer You Didn't Refuse", JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, moreOptions, options[0]);
                //an optionpane that tells players they broke out
                return true;
            }
            else //if removing 50 bucks was impossible
            {
                Object[] moreOptions = {"You Bet"}; //custom button names
                n = JOptionPane.showOptionDialog(pane,
                    "Whoa Punk! You Don't Have The Money for my Deal!", "An Offer You Shouldn't Abuse",
                    JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, moreOptions, options[0]);
                //an optionpane that tells players they couldn't afford to break out
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the HashMap describing all those in jail and their turns inside so far.
     * @return The HashMap<Player, Integer> of jailed players and turns inside
     */
    public HashMap<Player, Integer> getJail()
    {
        return playerAndTime;
    }

    /**
     * Gets the number of turns the parametered player has been in jail
     * @param player The player who we're checking is in jail
     * @return the Integer associated with that player, # of turns in jail
     */
    public int getJailTime(Player player)
    {
        if(playerAndTime.containsKey(player)) //if the player is in jail
        {
            return playerAndTime.get(player); //return the turn they got into jail
        }
        return -1; //else they weren't and return this impossible turn number
    }

    /**
     * Checks whether the parametered player has served his time (3 turns) in jail.
     * @param player The player we're checking out
     * @return A boolean of whether or not he has been in jail for 3 days.
     */
    public boolean hasServedTime(Player player)
    {
        if(player.getTurn() == this.getJailTime(player)+3) //if they've been in jail 3 turns
        {
            removePlayer(player); //remove them from jail
            player.removeMoney(50); //remove 50 bucks
            Object[] options = {"Golly Okay"};
            int n = JOptionPane.showOptionDialog(pane,
                    "You've Served Your Time, Now Hand Me $50",
                    "An Offer You Can't Refuse", JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            //an optionpane that tells players they broke out
            return true;
        }
        return false;
    }

    /**
     * Checks whether the parametered player is in jail or not.
     * @param player The player who we're checking is in jail
     * @return A boolean of whether or not they're in jail
     */
    public boolean isInJail(Player player)
    {
        if(playerAndTime.containsKey(player))
        {
            return true;
        }
        return false;
    }

    /**
     * Removes a player from the jailing HashMap.
     * @param player The player who we're removing from jail
     * @return A boolean of whether or not they're in jail
     */
    public boolean removePlayer(Player player)
    {
        if(playerAndTime.containsKey(player))
        {
            playerAndTime.remove(player);
            return true;
        }
        return false;
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is using the square's ability
     * @return A boolean of true, because they
     */
    public boolean useSquare(Player player)
    {
        return true;
    }
}