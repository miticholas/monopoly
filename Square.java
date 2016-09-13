import javax.swing.JOptionPane;
/**
 * The variables and methods shared by all squares in Monopoly.
 * 
 * @author Nicholas Smit
 * @version 4/20/2013
 */
public abstract class Square
{
    //DATA MEMBERS   
    protected SquaresManager manager;    
    protected String name;
    protected int position;
    protected JOptionPane pane;
    protected boolean affordable;
    protected CardTypes type;

    //CONSTRUCTOR
    /**
     * Constructor for the Square Class.
     * @param manager The SquaresManager in charge of all squares on the board
     */
    public Square(SquaresManager manager)
    {
        this.manager = manager;
        name = null;
        position = -1;
        pane = new JOptionPane();
    }

    //METHODS
    /**
     * Gets the name of the square.
     * @return The String representation of the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the tile position of this Square on the board.
     * @return The integer associated with this tile
     */
    public int getPosition()
    {
        return position;
    }
    
    /**
     * Sets the name of the square.
     * @param name The String name to be given to the square
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the tile number of this Square on the board.
     * @param tile The integer position this square will be in on the board
     */
    public void setPosition(int tile)
    {
        this.position = tile;
    }

    /**
     * A method to be overriden where the specific square says what is to happen on when it's used.
     * @param player The player who is using the square
     * @return A boolean of whether it worked or not
     */
    public abstract boolean useSquare(Player player);
}