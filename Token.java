import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
/**
 * The variables and methods of an individual token in Monopoly.
 * 
 * @author Lukas Edman and Nicholas Smittington III
 * @version 05/03/2013
 */
public class Token
{
    //DATA MEMBER     
    private BoardDisplay board;
    private BufferedImage image;
    private Player player;
    private int xPos, yPos, position, dimension;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class Token
     */
    public Token()
    {
        this.board = null;
        this.image = null;
        this.position = 0;
        this.xPos = 0;
        this.yPos = 0;
    }

    //METHODS
    /**
     * Moves the token up one square, called often enough to make it's own method.
     */
    public void advanceOne()
    {
        this.setDimension(this.getDimension() + 1);
    }
    
    /**
     * Gets the dimensions the token is currently at.
     * @return The dimension where the token is
     */
    public int getDimension()
    { 
        return this.dimension;
    }

    /**
     * Gets the bufferedImage image of the Token.
     * @return The BufferedImage that is the token's image
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Gets the owner of this Token.
     * @return The player who owns this Token
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Gets the dimensions the token is currently at.
     * @return The dimension where the token is
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Gets the X position of the Token.
     * @return The integer x position of the token
     */
    public int getX()
    { 
        return xPos;
    }

    /**
     * Gets the Y position of the Token.
     * @return The integer y position of the token
     */
    public int getY()
    { 
        return yPos;
    }

    /**
     * Loads all the token images.
     * @param imageNumber An integer that is associated with an image to uplad.
     * @return The BufferedImage associated with the number
     */
    public BufferedImage loadImage(int imageNumber) 
    {
        try
        {
            if(imageNumber == 0)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/cannon.png"));            
            else if(imageNumber == 1)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/car.png"));
            else if(imageNumber == 2)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/dog.png"));
            else if(imageNumber == 3)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/hat.png"));
            else if(imageNumber == 4)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/iron.png"));
            else if(imageNumber == 5)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/ship.png")); 
            else if(imageNumber == 6)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/shoe.png"));
            else if(imageNumber == 7)
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/thimble.png"));
            else
                return ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/wheelbarrow.png"));
        }  
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,  " File read error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Sets the x and y coordinates of the Token to the parametered tile's.
     * @param tile The square who's coordinates we're using
     */
    public void setDimension(int position)
    {
        xPos = (int)board.getRect(position).x;
        yPos = (int)board.getRect(position).y;
        this.dimension = position;
    }

    /**
     * Sets the token's icon.
     * @param image The BufferedImage to be that of the token
     */
    public void setImage(int imageNumber)
    {
        this.image = loadImage(imageNumber);
    }

    /**
     * Gets the dimensions the token is currently at.
     * @return The dimension where the token is
     */
    public void setPosition(int position)
    { 
        this.position = position;
    }

    /**
     * Sets the owner of this Token.
     * @param player The player who will own this Token
     */
    public void setPlayer(Player player)
    {
        this.player = player;
    }

    /**
     * Sets the owner of this Token.
     * @param player The player who will own this Token
     */
    public void setupBoard(BoardDisplay board)
    {
        this.board = board;
    }
}