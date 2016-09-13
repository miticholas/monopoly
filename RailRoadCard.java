import java.util.HashMap;
import javax.swing.JOptionPane;
import java.awt.Color;
/**
 * The variables and methods of an individual RailRoadCard in Monopoly. These cards move
 * a player to the nearest railroad square and make them pay double the rent!
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class RailRoadCard extends Card
{
    //DATA MEMBERS   
    private int factor;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class RailRoadCard
     * @param cardSorter The CardSorter in charge of this card
     */
    public RailRoadCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
        factor = 0; //sets the factor to default zero
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
     * The special ability of this card. Moves the player to the Nearest RailRoad then charges
     * them a factor if the don't own it.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        if(player != null)  //if the player returned isn't nobody
        {
            Property[] prop = cardSorter.getManager().getProperty(new Color(0, 0, 0)); //fetches the railroad squares
            RailRoad[] rail = new RailRoad[prop.length];
            for(int i = 0; i < rail.length; i++)
            {
                rail[i] = (RailRoad)prop[i];
            }

            HashMap<Integer, RailRoad> distanceTo = new HashMap<Integer, RailRoad>();
            //make a new HashMap for relating our player's distance from it and the railroad
            int[] distance = new int[rail.length]; //a new int[] of the same size as the # of railroad tiles
            for(int i = 0; i < rail.length; i++) //goes through the railroads
            {
                if(rail[i].getPosition() - player.getPosition() < 0)
                //if the railroads's position minus player's position is less than zero
                {
                    distance[i] = (rail[i].getPosition() + 40) - player.getPosition();
                    //add 40 (size of gameboard) to the distance before finding distance between player and it
                }
                else
                {
                    distance[i] = rail[i].getPosition() - player.getPosition(); //just find distance between
                }
                distanceTo.put(distance[i], rail[i]); //add the distance as a key to the railroad
            }
            int king = 100; //for a king of the hill search
            for(int i = 0; i < distance.length; i++) //go through the length of of the distance int[]
            {
                if(distance[i] < king) //if this index of distance is less than current king
                {
                    king = distance[i];  //replace it as new king
                }
            }
            RailRoad railRoad = distanceTo.get(king); //now find which utility is associated with this distance

            Object[] options = {"That's What I'm Talking 'bout!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "ZOOM!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            //An optionpane telling the player the message on the card

            player.setPosition(railRoad.getPosition()); //move that player to this utility square
            cardSorter.getGame().moveToken();
            
            if(railRoad.getOwner() != player)
            {
                if(player.removeMoney(railRoad.payRent(true))) //remove this amount of money from the player if possible
                {
                    return true; //return it worked!
                }
                else
                {
                    cardSorter.getGame().cantPay();
                }
            }
        }
        return false; //otherwise it didn't
    }
}