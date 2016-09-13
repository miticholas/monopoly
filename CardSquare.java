/**
 * The variables and methods of an individual CardSquare square in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 04/23/2013
 */
public class CardSquare extends Square
{
    //DATA MEMBERS    
    private boolean chanceCard;
    private Card card;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class CardSquare.
     * @param manger The SquareManager associated with this square
     * @param chaneCard A boolean of whether or not this is a chance or community chest pile
     */
    public CardSquare(SquaresManager manager, boolean chanceCard)
    {
        super(manager); //calls the parent's constructor
        this.chanceCard = chanceCard; //sets whether it's a chance or community chest square
    }

    //METHODS
    /**
     * A player landing on this square will draw a card depending on whether it's a Chance or 
     * Community Chest square.
     * @param player The player who landed on this square
     */
    public void drawCard(Player player)
    {
        if(chanceCard) //draws the card from the chance pile if this square is a chance square
        {
            manager.getGame().getSorter().drawCard(player, true);
        }
        else //otherwise will call from the community chest pile
        {
            manager.getGame().getSorter().drawCard(player, false);
        }
        manager.getGame().getBoard().getLeftPanel().card(player); //afterwards update the cards in hand
    }

    /**
     * Directs the overriden useSquare method to the specific square's ability.
     * @param player The player who is currently using this square
     * @return The boolean of whether it worked or not
     */
    public boolean useSquare(Player player)
    {
        this.drawCard(player);
        manager.getGame().getBoard().repaint();
        return true;
    }
}