import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 * The variables and methods of an individual RepairsCard in Monopoly. These cards cost the
 * player money for each house they own... yikes.
 * 
 * @author Nicholas Smit
 * @version 04/22/2013
 */
public class RepairsCard extends Card
{
    //DATA MEMBERS   
    private int house;
    private int hotel;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class HouseCard.
     * @param cardSorter The CardSorter in charge of this card
     */
    public RepairsCard(CardSorter cardSorter)
    {
        super(cardSorter); //calls the parent's constructor
        house = 0; //sets default house price
        hotel = 0; //sets default hotel price
    }

    //METHODS
    /**
     * Sets the price of the house fee.
     * @param price The integer fee associated with a house
     */
    public void setHouse(int price)
    {
        this.house = price;
    }

    /**
     * Sets the price of the hotel fee.
     * @param price The integer fee associated with a hotel
     */
    public void setHotel(int price)
    {
        this.hotel = price;
    }

    /**
     * The special ability of this card. The player who drew this card must pay for the number of hotels and
     * houses they own.
     * @return A boolean of whether it worked or not
     */
    public boolean play()
    {
        int fee = 0; //makes a price int and sets as 0
        if(player != null) //if the player returned isn't nobody
        {
            ArrayList<Property> property = player.getProperty(); //get that players property list
            for(Property prop : property) //go through it with a foreach loop
            {
                if(prop.getHouses() == 5) //if the property has 5 houses (a hotel)
                {
                    fee += hotel; //then add the cost of a hotel to the free
                }
                else //else it's a house fee
                {
                    fee += prop.getHouses()*house; //and add the fee associated with number of houses
                }
            }
            Object[] options = {"My Word!"}; //custom button name
            int n = JOptionPane.showOptionDialog(pane, "" + this.getDescription(), "ROFLOL!",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            //An optionpane telling the player the message on the card

            if(player.removeMoney(fee)) //then apply that fee to the player
            {
                ((FreeParking)cardSorter.getGame().getManager().getSquare("Free Parking")).addToPot(fee);
                return true; //say that it worked
            }
        }
        return false; //else it didn't
    }
}