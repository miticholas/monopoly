import java.util.HashMap;
import javax.swing.JOptionPane;
import java.awt.Color;
/**
 * The variables and methods of an individual UtilityCard in Monopoly. These cards move
 * a player to the nearest utility and make them pay a factor of the rent!
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class UtilityCard extends Card
{
    //DATA MEMBERS    
    private int factor;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class UtilityCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public UtilityCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
        factor = 0; //sets factor to default zero
    }

    //METHODS
    /**
     * Sets the factor price to be removed from the player.
     * @param factor The integer factor to be associated with this card
     */
    public void setFactor(int factor)
    {
        this.factor = factor;
    }

    /**
     * The special ability of this card. Moves the player to the Nearest utility then charges
     * them a factor of the next dice roll if the don't own it.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null) //if the player returned isn't nobody
        {
            Property[] prop = cardSorter.getManager().getProperty(new Color(205, 198, 115)); //fetches the utility propers
            Utility[] util = new Utility[prop.length];
            for(int i = 0; i < util.length; i++)
            {
                util[i] = (Utility)prop[i]; //makes utility objects from property objects
            }

            HashMap<Integer, Utility> distanceTo = new HashMap<Integer, Utility>();
            //make a new HashMap for relating our player's distance from it and the utility
            int[] distance = new int[util.length]; //a new int[] of the same size as the # of utility tiles
            for(int i = 0; i < util.length; i++) //goes through the utilities
            {
                if(util[i].getPosition() - player.getPosition() < 0)
                //if the utility's position minus player's position is less than zero
                {
                    distance[i] = (util[i].getPosition() + SquaresManager.SQUARES) - player.getPosition();
                    //add 40 (size of gameboard) to the distance before finding distance between player and it
                }
                else
                {
                    distance[i] = util[i].getPosition() - player.getPosition(); //just find distance between
                }
                distanceTo.put(distance[i], util[i]); //add the distance as a key to the utility
            }
            int king = 100; //for a king of the hill search
            for(int i = 0; i < distance.length; i++) //go through the length of of the distance int[]
            {
                if(distance[i] < king) //if this index of distance is less than current king
                {
                    king = distance[i]; //replace it as new king
                }
            }
            Utility utility = distanceTo.get(king); //now find which utility is associated with this distance

            Object[] options = {"Heck Yeah!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "Speedy Gonzales!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            //An optionpane telling the player the message on the card

            player.setPosition(utility.getPosition()); //move that player to this utility square
            cardSorter.getGame().moveToken();
            if(utility.getOwner() != player) //if the player isn't the current owner
            {
                Dice[] dice = cardSorter.getGame().getDice(); //get the dice objects
                for(int i = 0; i < dice.length; i++) //re-roll all the dice
                {
                    dice[i].roll();  //by calling their roll method
                }
                if(player.removeMoney(utility.payRent(true))) //remove this amount of money from the player if possible
                {
                    return true; //return it worked!
                }
            }
            return true; //return that it worked!
        }
        return false; //otherwise it didn't
    }
}