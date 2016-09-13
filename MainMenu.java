import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;


/**
 * The main menu where the user's input sets up a Game of Monopoly.
 * 
 * @author Nicholas Smit and Lukas Edman 
 * @version 5/01/2013
 */
public class MainMenu extends JFrame implements ActionListener
{
    public final static int WIDTH = 860;
    public final static int HEIGHT = 545;
    private JLayeredPane layer;
    private JButton startB;
    private JLabel backG;
    private Player[] players;
    private Token[] tokens;
    

    /**
     * The Constructor of the MainMenu.
     */
    public MainMenu()
    {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layer = new JLayeredPane(); //adds a layered pane to place button in front of background
        layer.setPreferredSize(new Dimension(WIDTH, HEIGHT)); //sets the size to these constants
        this.add(layer); //adds the layeredPane to the frame

        BufferedImage image = null;
        try //uploads this image
        {
            image = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/menu.jpg"));
        } catch (IOException e) //catches if it isn't there
        {
            e.printStackTrace();
        }
        backG = new JLabel(new ImageIcon(image)); //turns this uploaded image into a JLabel
        backG.setSize(this.getPreferredSize()); //we set the size of this image to the frame's preferred size
        backG.setLocation(0, 0); //we put the location in the top right corner
        layer.add(backG, JLayeredPane.DEFAULT_LAYER);

        startB = new JButton("START GAME");
        startB.addActionListener(this);
        startB.setSize(550, 80);
        startB.setLocation((backG.getWidth() - startB.getWidth())/2, (backG.getHeight() - startB.getHeight())/2+70);
        Font font = new Font("Serif", Font.BOLD, 40);
        startB.setFont(font); 

        layer.add(startB, JLayeredPane.MODAL_LAYER);
        this.add(layer);
        this.setResizable(false);
        this.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();           // location code CREDIT to:
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);       // jjnguy on stackoverflow.com
        Point newLocation = new Point(middle.x - (this.getWidth() / 2), 
                middle.y - (this.getHeight() / 2));
        this.setLocation(newLocation);

        this.setVisible(true);
    }

    /**
     * Starts setting up the game by calling setup methods.
     */
    public void startGame()
    {
        

        int x = numberOfPlayers(); //calls a method to find the number of players
        if(x != 0) //if the number isn't zero
        {
            players = new Player[x]; //we set the players array to hold this many player
            tokens = new Token[x];
            if(namePlayers()) //then if the players were named (by calling the method to do that)
            {
                chooseTokens(); //we tell them to choose a token for each player
                this.setVisible(false); //then we set the menu to invisibility 
                Game newGame = new Game(this); //then we create the game with the input players
            }
        }
    }

    /**
     * Catches ActionEvents and has them perform these actions
     * @param e The ActionEvent being caught
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == startB) //if the actionEvent came from the start button
        {
            this.startGame();
        }
    }

    /**
     * Makes the user select which tokens are being used.
     */
    public void chooseTokens()
    {
        HashMap<Integer, Player> tokensChosen = new HashMap<Integer, Player>();
        //a hashmap relating players to tokens
        for(int i = 0; i < players.length; i++)
        {
            boolean keepGoing = true;
            String message = " Which Token Do You Choose?";
            while(keepGoing)
            {
                Object[] choices = {"Cannon", "Car", "Dog", "Hat", "Iron", "Ship", "Shoe", "Thimble", "Wheelbarrow"};
                String number = (String)JOptionPane.showInputDialog( this, players[i].getName()
                        + message, "THE CHOICE", JOptionPane.PLAIN_MESSAGE, null, choices,
                        choices[0]);

                //If a string was returned, say so.
                for(int j = 0; j < choices.length; j++)
                {
                    if (number == choices[j] && !tokensChosen.containsKey(j))
                    {
                        tokens[i] = new Token();
                        tokens[i].setImage(j);
                        tokensChosen.put(j, players[i]);
                        players[i].setToken(tokens[i]);
                        keepGoing = false;
                    }
                }
                if(players[i].getToken() == null)
                {
                    message = " Sorry, That Token has Already Been Chosen";
                }
            }
        }
    }
    
    /**
     * Gets the players decided upon.
     * @return An array of players who are playing.
     */
    public Player[] getPlayers()
    {
        return players;
    }
    
    /**
     * Gets the tokens decided upon.
     * @return An array of tokens being used.
     */
    public Token[] getTokens()
    {
        return tokens;
    }

    /**
     * Makes the user enter the names of the players.
     * @return boolean Whether they answered or not
     */
    public boolean namePlayers()
    {
        for(int i = 0; i < players.length; i++) //the players are instantiated
        {
            players[i] = new Player();
        }

        boolean keepGoing = true;
        for(int i = 0; i < players.length; i++)
        {
            String message = "Player " + (i+1) + " Type in Your Player Name";
            int type = JOptionPane.PLAIN_MESSAGE;
            while(keepGoing)
            {
                String name = (String)JOptionPane.showInputDialog(
                        this, message, "THE NAMES", type, null, null, null);

                //If a string was returned, say so.
                if ((name != null) && (name.length() > 1)) {
                    players[i].setName(name);
                    keepGoing = false;
                }
                else
                {
                    message = "That's an Invalid Name! Try Again Player " + (i+1);
                    type = JOptionPane.ERROR_MESSAGE;
                }
            }
            keepGoing = true;
        }
        return true;
    }

    /**
     * Makes the user choose the number of players playing.
     * @return An integer number of players selected
     */
    public int numberOfPlayers()
    {
        Object[] choices = {"2 - Players", "3 - Players", "4 - Players", "5 - Players"}; //the selection choices
        String number = (String)JOptionPane.showInputDialog(
                this, "How Many Players Will Attempt the Big Bucks?", "THE NUMBERS",
                JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
        //A JOptionPane telling the user to choose the numbers of players

        if (number == choices[0]) //if they chose selection one - of 2 players
        {
            return 2; //return that they chose 2
        }
        else if (number == choices[1])
        {
            return 3;
        }
        else if (number == choices[2])
        {
            return 4;
        }
        else if (number == choices[3])
        {
            return 5;
        }
        else
        {
            return 0;
        }
    }
}