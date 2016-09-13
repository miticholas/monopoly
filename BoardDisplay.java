import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * Write a description of class BoardDisplay here.
 * 
 * @author Lukas Edman and a small, hand-woven effigy with one eye missing
 * @version (a version number or a date)
 */
public class BoardDisplay extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
    //DATA MEMBERS    
    private JFrame frame;
    private RightPanel rightPanel;
    private LeftPanel leftPanel;
    private Game game;
    private JOptionPane optionPane;
    private Dice[] dieArray;
    private Rectangle2D.Double[] rects; 
    private BufferedImage[] dicePics;
    private BufferedImage board;
    private Token[] tokens;
    private Token movingToken;
    private int boardW, boardH, die1, die2, toss, counter;
    private Timer diceTimer;

    private Graphics2D g2d;
    private Polygon house;
    private Polygon[][] houses;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class BoardDisplay
     */
    public BoardDisplay(Game game)
    {
        super();
        this.game = game;
        tokens = game.getTokens();

        board = getBoard();
        boardW = board.getWidth();
        boardH = board.getHeight();
        this.setPreferredSize(new Dimension(boardW, boardH));

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addMouseListener(this);
        addMouseMotionListener(this);
        frame.setFocusable(true);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER); //adds this (JPanel with board) to center of JFrame

        JLabel label = new JLabel(new ImageIcon(getHeader())); //adds a picture to at the top
        frame.add(label, BorderLayout.NORTH);

        rightPanel = new RightPanel(game);
        frame.add(rightPanel, BorderLayout.EAST);

        leftPanel = new LeftPanel(game); //adds right and left panels with further layouts
        frame.add(leftPanel, BorderLayout.WEST);

        //THE DICE, TOKENS, HOUSES, HOTELS
        rects = loadRectangles();
        die1 = 0;
        die2 = 0;

        houses = new Polygon[40][5]; //Polygon[tileNumber][numberOfHouses]

        dieArray = game.getDice();
        dicePics = loadDice();
        for(int i = 0; i < tokens.length; i++)
        {
            tokens[i].setupBoard(this);
            tokens[i].setDimension(0);
        }

        frame.setResizable(false);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     // location code CREDIT to:
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2); // jjnguy on stackoverflow.com
        Point newLocation = new Point(middle.x - (frame.getWidth() / 2), 
                middle.y - (frame.getHeight() / 2) - 25);
        frame.setLocation(newLocation);

        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == diceTimer)
        {
            Dice[] die = game.getDice();
            Random random = new Random();
            counter++;
            if(counter == toss)
            {
                die1 = die[0].getRoll();
                die2 = die[1].getRoll();
                repaint();
                diceTimer.stop();
            }
            else
            {
                die1 = random.nextInt(6)+1;
                die2 = random.nextInt(6)+1;
                repaint();
            }
        }
    }

    /**
     * Puts the houses at the right position.
     * @param tile An integer position to place the house
     */
    public void drawHouse(int tile)
    {        
        int n = game.getManager().getProperty(tile).getHouses(); //adjusts where it is being painted

        for (int i = 0; i < n; i++)
        {
            house = new Polygon();
            house.addPoint((int)rects[tile].x+7+(i*10),(int)rects[tile].y);
            house.addPoint((int)rects[tile].x+2+(i*10),(int)rects[tile].y+5);
            house.addPoint((int)rects[tile].x+2+(i*10),(int)rects[tile].y+15);
            house.addPoint((int)rects[tile].x+12+(i*10),(int)rects[tile].y+15);
            house.addPoint((int)rects[tile].x+12+(i*10),(int)rects[tile].y+5);
            houses[tile][i] = house;
        }
        repaint(); //gotta repaint!
    }

    /**
     * Removes a house from a certain square.
     * @param tile The integer position to remove the house from
     */
    public void eraseHouse(int tile)
    {        
        int n = game.getManager().getProperty(tile).getHouses() * 10;

        houses[tile][n/10] = null;

        repaint(); //gotta repaint!
    }

    /**
     * Returns board as a BufferedImage
     * @return the monopoly board
     */
    public BufferedImage getBoard() 
    {
        try
        {
            BufferedImage board = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/monopoly_board.jpg"));            
            return board;
        }  
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,  " File read error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Gets the game currently being played.
     * @return The Game currently played
     */
    public Game getGame()
    {
        return this.game;
    }

    /**
     * Returns the header as an Image.
     * @return the Image to put into a JLabel
     */
    public Image getHeader()
    {
        BufferedImage image = null; 
        try //uploads this image
        {
            image = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/header.jpg"));
        } catch (IOException e) //catches if it isn't there
        {
            e.printStackTrace();
        }
        return image.getScaledInstance((int)1000,(int)80,Image.SCALE_SMOOTH);
    }

    /**
     * Fetches the left panel so other components may use it.
     * @return The LeftPanel
     */
    public LeftPanel getLeftPanel()
    {
        return leftPanel;
    }

    /**
     * Gets the rectangle at the position on the board.
     * @param position The integer position
     * @return The Rectangle at that position
     */
    public Rectangle2D.Double getRect(int position)
    {
        return rects[position];
    }

    /**
     * Gets the rectangles.
     * @return An array of Rectangles2D.Double objects
     */
    public Rectangle2D.Double[] getRectangles()
    {
        return rects;
    }

    /**
     * Loads the dice images and returns them.
     * @return The BufferedImage[] of all the images of dice
     */
    public BufferedImage[] loadDice()
    {
        BufferedImage[] dice = new BufferedImage[Dice.SIDES];
        try
        {
            for(int i = 0; i < dice.length; i++)
            {
                dice[i] = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/dice" + (i+1) + ".png"));            
            }
            return dice;
        }  
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,  " File read error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Helper method that loads rectangles that correspond with the squares
     * @return the monopoly board
     */
    private Rectangle2D.Double[] loadRectangles()
    {
        Rectangle2D.Double[] rectangles = new Rectangle2D.Double[SquaresManager.SQUARES];
        //makes an array of 2D rectangles with the same number as number of squares

        for(int i = 0; i < rectangles.length; i++)
        {
            rectangles[i] = new Rectangle2D.Double();
            if (i == 30)
                rectangles[i].setRect(board.getWidth()-80, 0, 80, 80);
            else if (i == 20)
                rectangles[i].setRect(0, 0, 80, 80);    
            else if (i == 10)
                rectangles[i].setRect(0,board.getHeight()-80, 80, 80); 
            else if (i == 0)
                rectangles[i].setRect(board.getWidth()-80, board.getHeight()-80, 80, 80);    
            else if (i > 0 && i < 10)
                rectangles[i].setRect(board.getWidth()-78 + (i*-49), board.getHeight()-80, 45, 80);
            else if (i > 10 && i < 20)
                rectangles[i].setRect(0,board.getHeight()-77 + -49*(i-10), 80, 45);
            else if (i > 20 && i < 30)
                rectangles[i].setRect(33 + 49*(i-20), 0, 45, 80);
            else if (i > 30 && i < 40)
                rectangles[i].setRect(board.getWidth()-80, 49*(i-30) + 33, 80, 45);
        }
        return rectangles;
    }

    /**
     * Left clicking prompts whether you want to add/remove a house
     * Right clicking prompts whether you want to mortage/unmortgage a property
     */
    public void mouseReleased(MouseEvent e)
    {
        for (int i = 0; i < rects.length; i++) //checks all the rectangles
        {
            if (rects[i].contains(e.getX(), e.getY())) //if the point of clicking is inside one of the rectangles
            {
                if (e.getButton() == MouseEvent.BUTTON1) //left click to add/remove houses
                {
                    Property prop = game.getManager().getProperty(i);
                    if(prop != null) //if what you clicked on is actually a property
                    {
                        if (prop.getMortgage()) //checks if the property is mortgaged
                        {
                            Object[] options = {"Awww"}; //custom button name
                            int x = JOptionPane.showOptionDialog(optionPane, "You can't put a house on a mortgaged property!", "Nuh uh girlfriend!",
                                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        }
                        else
                        {
                            Object[] options = {"Add House", "Remove House"}; //choosing whether to add or remove a house
                            int n = JOptionPane.showOptionDialog(optionPane, "Would you like to Add or Remove a House?", "House",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                            if(n == JOptionPane.YES_OPTION) //option to add house
                            {
                                game.getManager().changeHouse(game.getCurrentPlayer(),i,true); //adds a house to the property clicked
                                drawHouse(i);
                            }
                            else if (n == JOptionPane.NO_OPTION)//option to remove house
                            {
                                game.getManager().changeHouse(game.getCurrentPlayer(),i,false); //removes house
                                eraseHouse(i);
                            }
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) //right click to mortgage properties
                {
                    if (game.getManager().getProperty(i).getHouses() == 0)
                    {
                        Object[] options = {"Mortgage/Unmortgage Property"}; //custom button name
                        int n = JOptionPane.showOptionDialog(optionPane, "Do you really wanna Mortgage/Unmortgage this baby?", "No not the mortgages!",
                                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                        if (n == JOptionPane.OK_OPTION) //if they actually click ok
                        {
                            game.getManager().mortgageProperty(game.getCurrentPlayer(),game.getManager().getProperty(i));
                        }
                    }
                    else //so that a player can't mortgage when there are houses on the property
                    {
                        Object[] options = {"Mortgage/Unmortgage Property"}; //custom button name
                        int n = JOptionPane.showOptionDialog(optionPane, "You got some houses to get rid of first before mortgaging!", "You can't do that yet!",
                                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                    }
                }
            }
        }
    }

    /**
     * Draws the board and the tokens
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g2d = (Graphics2D)g;

        g.drawImage(board,0,0,null); 

        for (int i = 0; i < 40; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (houses[i][j] != null) //so it doesn't throw an error
                    g2d.fill(houses[i][j]);
            }
        }

        for(int i = 0; i < tokens.length; i++)
        {
            g.drawImage(tokens[i].getImage(),tokens[i].getX(),tokens[i].getY(),null);
        }

        if(die1 != 0)
        {
            g.drawImage(dicePics[die1 - 1], 300, 300, 35, 35, null);
        }
        if(die2 != 0)
        {
            g.drawImage(dicePics[die2 - 1], 250, 300, 35, 35, null);
        }
    }

    /**
     * A method which starts the dice timer that activates it's actionEvent, showing dice animation.
     */
    public void rollDice()
    {
        die1 = 0;
        die2 = 0;
        repaint();
        diceTimer = new Timer(100, this);
        diceTimer.setInitialDelay(100);

        Random random = new Random();
        toss = random.nextInt(11)+5;
        counter = 0;
        diceTimer.start();
    }

    /**
     * Disposes of the JFrame, thus closing the game.
     */
    public void terminate()
    {
        this.frame.dispose();
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void  mousePressed(MouseEvent e){}

    /**
     * Shows a JOptionPane informing players they can't end their turn until the roll the die.
     */
    public void haventRolled()
    {
        Object[] options = {"Silly Me"}; //custom button name
        int n = JOptionPane.showOptionDialog(optionPane, "I'm Afraid I Can't Do That Dave, You Haven't Rolled Yet", "Da Truth",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //An optionpane telling the player the message on the card
    }

    /**
     * When a registered key is released an event happens. Depends on the key.
     * @param e The KeyEvent caught
     */
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            game.rollDice();
        }
        else if(e.getKeyCode() == KeyEvent.VK_Q)
        {
            game.playerQuit();
        }
        else if(e.getKeyCode() == KeyEvent.VK_T)
        {
            if(!game.haveToRoll()) //a double negative, but going about making it not is too much work
            {
                game.nextTurn();
            }
            else
            {
                this.haventRolled();
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_M)
        {
            //  game.mortgage();
        }
    }

    public void keyTyped(KeyEvent e){}

    public void keyPressed(KeyEvent e){}
}