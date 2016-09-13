import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.*;
import java.awt.Color;
/**
 * The variables and methods of the SquaresManager Class in Monopoly. Controls player interactions
 * with Squares on the board.
 * 
 * @author Nicholas Smit
 * @version 05/13/2013
 */
public class SquaresManager
{
    //DATA MEMBERS   
    public static final int SQUARES = 40;
    private JOptionPane pane;
    private Property[] property;
    private Square[] squares;
    private Game game;

    //CONSTRUCTOR
    /**
     * Constructor for the SquaresManager Class.
     * @param game The Game currently being played
     */
    public SquaresManager(Game game)
    {
        squares = new Square[SQUARES];
        property = new Property[SQUARES];
        for(int i = 0; i < property.length; i++)
        {
            property[i] = null;
        }
        this.game = game;
        this.loadData();
    }

    //METHODS
    /**
     * Deals with additions of house numbers on a given property.
     * @param player The player buying houses
     * @param tile The property being affected
     * @param housesArray An int array of houses on properties
     * @return A boolean of whether it worked or not
     */
    private boolean addHouseHelper(Player player, int tile, int[] housesArray)
    {
        boolean works = true; //a boolean initially saying it (adding a house) can be done
        for(int i = 0; i < housesArray.length; i++) //goes through the properties # of houses
        {
            if(Math.abs((property[tile].getHouses() + 1) - housesArray[i]) > 1)
            //if adding a house will make it have 2 more than another
            {
                works = false; //it can't be done
            }
        }
        if(works) //if it can be done
        {
            if(game.areSure()) //and if they are sure they still want to
            {
                if(property[tile].addHouse()) //if adding house was successful
                {
                    int lost = property[tile].getPrice(8); //we subtract the price of the house
                    player.removeMoney(lost); //and we remove it from the player's money

                    JOptionPane.showMessageDialog(pane,"You bought a house and spent $" + lost + "!", "GOOD FOR YOU!",
                        JOptionPane.WARNING_MESSAGE);

                    game.getBoard().getLeftPanel().money(player); //updates players money
                    return true;
                }
                else //they have the max number of houses
                {
                    JOptionPane.showMessageDialog(pane, "This tile is already at its max", "UM HELLO", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else //other properties in this monopoly need houses first
        {
            JOptionPane.showMessageDialog(pane, "You need to add houses to the other properties first.", "NU-HUH!",
                JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    /**
     * Adds a specified property to a player's property list, if they choose to.
     * @param player The player who's buying the property
     * @param prop The property to be added to the player's list
     */
    public void addProperty(Player player, Property prop)
    {
        prop.setOwner(player);
        player.addProperty(prop);
    }

    /**
     * Deals with changes to house numbers on a given property.
     * @param player The player buying/selling houses
     * @param tile The property being affected
     * @param more whether you are adding or subracting, adding = true, subtracting = false
     * @return A boolean of whether it worked or not
     */
    public boolean changeHouse(Player player, int tile, boolean more)
    {
        ArrayList<Integer> housesList = new ArrayList<Integer>(); //makes a new array list for for integers (number of houses on tile)
        Color color = property[tile].getColor(); //gets the color group of the property
        pane = new JOptionPane();
        for(int i = 0; i < property.length; i++) //goes through the length of property array attribute
        {
            if(property[i] != null)
            {
                if(color.equals(property[i].getColor()))  //if the property has the chosen color
                {
                    housesList.add(property[i].getHouses()); //add the current # of houses to the new arraylist
                }
            }
        }
        int[] housesArray = convertIntegers(housesList); //turns the arrayList into an array for easier handling

        if(whoseMonopoly(color) == player) //if the selected player has a monopoly for this color
        {
            if(more) //if the user selected adding a house use this method helper
            {
                return addHouseHelper(player, tile, housesArray);
            }
            if(!more) //if they selected subtracting use this one
            {
                return subtractHouseHelper(player, tile, housesArray);
            }
        }
        else //otherwise they don't own a monopoly on this color group and can't add houses
        {
            JOptionPane.showMessageDialog(pane, "Nuh-uh That Ain't Yoz to Build No House On!", "EXCUSE ME",
                JOptionPane.WARNING_MESSAGE);
        }
        return false; //otherwise a house wasn't added
    }

    /**
     * Returns whether or not the property color set in question is Monopoly owned.
     * @param color The specific property color being checked
     * @return A boolean of whether or not it is a Monopoly
     */
    public boolean checkMonopoly(Color color)
    {
        if(whoseMonopoly(color) == null) //finds who owns a monopoly on this color, if the returned player is null
        {
            return false; //then there isn't a monopoly
        }
        return true; //otherwise it is a monopoly
    }

    /**
     * A simple method that changes an integer list into an int array, apparently a multi-step task.
     * @param integers An ArrayList<Integer> of numbers to convert
     * @return An int array of returned numbers
     */
    private int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    /** 
     * Gets all the properties in this game.
     * @return An array of Property objects
     */
    public Property[] getAllProperty()
    {
        return this.property;
    }

    /**
     * Gets all the squares in this game.
     * @return An array of Square objects
     */
    public Square[] getAllSquares()
    {
        return this.squares;
    }

    /**
     * Gets the Color associated with each int.
     * @param x An integer
     * @return A Color associated with the number
     */
    public Color getColor(int x)
    {
        if(x == 0) //if x is equal to this number
            return new Color(148, 0, 211); //purple
        else if(x == 1)
            return new Color(135, 206, 250); //light blue
        else if(x == 2)
            return new Color(255, 20, 147); //pinkish
        else if(x == 3)
            return new Color(255, 140, 0); //orange
        else if(x == 4)
            return new Color(250, 10, 10); //red
        else if(x == 5)
            return new Color(255, 230, 0); //yellow
        else if(x == 6)
            return new Color(50, 205, 50); //green
        else if(x == 7)
            return new Color(0, 0, 128); //darker blue
        else if(x == 8)
            return new Color(0, 0, 0); //black - railroads
        else if(x == 9)
            return new Color(205, 198, 115); //khaki - utilities
        else
        {
            return null;  
        }
    }

    /**
     * Gets the Game currently being played.
     * @return The game being played
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * Returns which players own property of the specified color.
     * @param color The specified property color being checked
     * @return A Player array of who owns property
     */
    public Player[] getOwners(Color color)
    {
        Property[] propArray = getProperty(color); //returns an array of property with the specified color

        Player[] owners = new Player[propArray.length]; //makes an array of players with the length of the property array
        for(int i = 0; i < owners.length; i++)
        {
            owners[i] = propArray[i].getOwner(); //fills in this players array with the owners of each property in the prop array
        }
        return owners;
    }

    /**
     * Returns all properties of the desired color.
     * @param color the color of the property
     * @return A property array of properties with that color
     */
    public Property[] getProperty(Color color)
    {
        ArrayList<Property> colorProp = new ArrayList<Property>(); //makes a new arraylist for properties of one color group
        for(int i = 0; i < property.length; i++)
        {
            if(property[i] != null)
            {
                if(color.equals(property[i].getColor())) //if the property has the color
                {
                    colorProp.add(property[i]); //add it to the arraylist
                }
            }
        }
        return colorProp.toArray(new Property[0]); //return that arralist as an array
    }

    /**
     * Get a property at a specified location
     * @param tile the location of the property
     * @return the property corresponding with this tile
     */
    public Property getProperty(int tile)
    {
        return property[tile];
    }

    /**
     * Gets the Square on the parametered tile.
     * @param tile The integer position of the square on the board
     * @return The specific square corresponding with this tile
     */
    public Square getSquare(int tile)
    {
        return squares[tile];
    }

    /**
     * Gets the square that corresponds with the input String name.
     * @param name The String name of the square
     * @return A Square of monopoly who has that name
     */
    public Square getSquare(String name)
    {
        for(int i = 0; i < property.length; i++)
        {
            if(squares[i] != null)
            {
                if(squares[i].getName().equalsIgnoreCase(name)) //if the square has a name equaling the parametered name
                {
                    return squares[i]; //return that square
                }
            }
        }
        return null;
    }

    /**
     * Loads all the data on the Monopoly squares into the Game from a text file.
     */
    public void loadData()
    {
        try //if loading the file doesn't work, catch it at the bottom
        {
            File file = null;
            file = new File("texts/squares.txt"); //find the "property.txt" file in the "texts" folder
            Scanner scanner = new Scanner(file); //scanner for scanning document

            for(int i = 0; scanner.hasNextLine(); i++) //go through document as long as there's another line
            {
                String nextLine = scanner.nextLine(); //next line is a variable
                String[] section = nextLine.split("; "); //splits up the line into sections based on "; "

                int tile = Integer.parseInt(section[0]); //the 1st section denotes it's "tile" or square on board
                int s = section.length; //variable s is the number of sections
                if(s == 6 || s == 7 || s == 12) //if any of these sections long
                {
                    squares[tile] = new Property(this); //make this tile a Property class
                }
                if(s == 6) //furthermore make sections 6 long into a Utility subclass
                {
                    squares[tile] = new RailRoad(this);
                }
                if(s == 7) //furthermore make sections 7 long into a RailRoad subclass
                {
                    squares[tile] = new Utility(this);
                }
                if(s == 6 || s == 7 || s == 12) //if it's any of these sections long
                {
                    for(int j = 0; j < s; j++) //go through up to the size of the section
                    {
                        if(j != 2) //if the cycle isn't at 2
                        {
                            int x = Integer.parseInt(section[j]); //transform section "j" to an int
                            if(j == 1) //if section 1
                                ((Property)squares[tile]).setColor(this.getColor(x)); //set color to the associated int
                            if(j > 2) //if it's greater than section 2
                                ((Property)squares[tile]).setPrice(j - 3, x); //start setting prices
                        }
                        if(j == 2) //when it is
                            squares[tile].setName(section[j]); //set the name to whatever section 2 said
                    }
                    property[tile] = (Property)squares[tile];
                    squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                }
                if(s == 2) //if 2 sections long
                {
                    String x = section[1].toLowerCase(); //take section 2, save as a string in lower case
                    if(x.equals("community chest")) //if the string reads this
                    {
                        squares[tile] = new CardSquare(this, false); //then make this subclass
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("chance"))
                    {
                        squares[tile] = new CardSquare(this, true);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("free parking"))
                    {
                        squares[tile] = new FreeParking(this);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("luxury tax"))
                    {
                        squares[tile] = new LuxuryTax(this);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("income tax"))
                    {
                        squares[tile] = new IncomeTax(this);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("go to jail"))
                    {
                        squares[tile] = new GoToJail(this);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("jail"))
                    {
                        squares[tile] = new Jail(this);
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                    if(x.equals("go")) //if the string reads this
                    {
                        squares[tile] = new Go(this); //then make this subclass
                        squares[tile].setName(x);
                        squares[tile].setPosition(tile); //sets the square's position on the board to that of the tile #
                    }
                }
            }
        }
        catch (IOException e)//catch if any problem occurs while reading the file
        {
            System.out.println ("Error reading file: " + e.getMessage());
            e.printStackTrace(); //show the stack trace so we can track down the error
        }
    }

    /**
     * Deals with mortgaging a player's Property. Uses JOptionPanes.
     * @param player The player who is mortgaging property
     * @param prop The property they want to mortgage
     * @return A boolean of whether it worked or not
     */
    public boolean mortgageProperty(Player player, Property prop)
    {
        pane = new JOptionPane();
        if(player.hasProperty(prop)) //if the player has the property
        {
            if(!prop.isMortgaged) //and it isn't mortgaged
            {
                if(prop.getHouses() == 0) //and there aren't any houses on it
                {
                    if(game.areSure()) //if they're sure they want to do this
                    {
                        prop.setMortgage(true); //then set the property's status to mortgaged
                        int made = prop.getPrice(7); //get the price of mortgaging
                        player.addMoney(made); //give the player that much money
                        JOptionPane.showMessageDialog(pane,
                            "You've Just Mortgaged " + prop.getName() + " and got $" + made + "!", "YAY!", JOptionPane.WARNING_MESSAGE);

                        game.getBoard().getLeftPanel().money(player); //updates players money display
                        return true;
                    }
                }
                else //else they have houses on it and can't sell
                {
                    JOptionPane.showMessageDialog(pane, "Can't Remove While Houses. Foo!", "DUMBY", JOptionPane.WARNING_MESSAGE);
                }
            }
            else //else the property is mortgaged already and they can then unmortgage it
            {
                Object[] options = {"Yes", "No!"};
                int n = JOptionPane.showOptionDialog(pane, "Would You Like to Unmortgage This Property? - costs $"
                        + (Math.round(prop.getPrice(7)*1.10)) + "!", "A Tough Decision", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

                if(n == JOptionPane.YES_OPTION) //if they choose to unmortgage
                {
                    int lost = (int)(prop.getPrice(7)*1.10); //set the umortgaging price to the properties mortgage price times fee
                    player.removeMoney(lost); //remove that money from the player
                    JOptionPane.showMessageDialog(pane, "You've Just Bought Back " + prop.getName() + " and spent $" + lost + "!",
                        "YIPPEE!", JOptionPane.WARNING_MESSAGE);

                    prop.setMortgage(false);
                    game.getBoard().getLeftPanel().money(player); //updates players money display
                    return true;
                }
            }
        }
        else //else the player doesn't own the property and cant therefore mortgage it
        {
            JOptionPane.showMessageDialog(pane, "You Don't Own This Property!", "EXCUSE ME", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    /**
     * Deducts money from the player who must pay the rent on a square.
     * @param player The player who's paying the charge
     * @param prop The property charging the player
     * @return A boolean of whether it worked or not
     */
    public boolean payRent(Player player, Property prop)
    {
        int price;
        if(new Color(255, 250, 205).equals(prop.getColor())) //if the property is has the utility square's color
        {
            price = ((Utility)prop).payRent(); //then return an integer from the utility payRent method
        }
        else if(new Color(0, 0, 0).equals(prop.getColor())) //if the property is has the railroad square's color
        {
            price = ((RailRoad)prop).payRent(); //then return an integer from the railroad payRent method
        }
        else //else it's a normal property
        {
            price = prop.payRent(); //calls the payRent() method of the specific property, returning an int
        }

        if(prop.getOwner() != player) //if the property isn't owned by that player themself
        {
            if (!prop.getMortgage())
            {
                if(player.removeMoney(price)) //and they can pay the money
                {
                    this.rentDialog(price);
                    prop.getOwner().addMoney(price);
                    return true; //return that it worked
                }
                else
                {
                    return false; //if he cant afford the cost chaos happens!
                }
            }
        }
        return true; //otherwise its fine he didn't have to pay anything
    }

    /**
     * Removes a specified property from a player's property list.
     * @param player The player who we're removing property from
     * @param prop The property to be removed from the player's list
     * @return A boolean of whether it worked or not
     */
    public boolean removeProperty(Player player, Property prop)
    {
        if(player.hasProperty(prop)) //if the player has the property in his list
        {
            player.removeProperty(prop); //remove it
            prop.setOwner(null); //set the properties owner to null
            while(prop.removeHouse()); //removes all houses
            return true;
        }
        return false;
    }

    /**
     * Opens a pane telling how much money a player must now pay as Rent.
     * @param rent The integer rent of this property
     */
    public void rentDialog(int rent)
    {
        Object[] options = {"Yes"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "You Must Pay a Rent of $"
                + rent + ".", "Pay Up", JOptionPane.OK_OPTION, 
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        //An optionpane that tells them they lost money
    }

    /**
     * Resets all the properties in a player's control who is quiting.
     * @param player The player who is quitting and needs the properties reset
     */
    public void resetProperty(Player player)
    {
        Property[] property = player.getProperty().toArray(new Property[0]); //gets all the properties in the players property list
        for(int i = 0; i < property.length; i++)
        {
            removeProperty(player, property[i]);
        }
    }

    /**
     * Deals with subtractions of house numbers on a given property.
     * @param player The player selling houses
     * @param tile The property being affected
     * @param housesArray An int array of houses on properties
     * @return A boolean of whether it worked or not
     */
    private boolean subtractHouseHelper(Player player, int tile, int[] housesArray)
    {
        boolean works = true;
        for(int i = 0; i < housesArray.length; i++)
        {
            if(Math.abs((property[tile].getHouses() - 1) - housesArray[i]) > 1)
            {
                works = false;
            }
        }
        if(works) //if they are allowed to subtract houses from this property
        {
            if(game.areSure()) //and if they are sure they still want to
            {
                if(property[tile].removeHouse()) //if subtracting house was successful
                {
                    int made = property[tile].getPrice(8)/2; //gets the price of the house divided by two
                    player.addMoney(made); //gives it to the player
                    JOptionPane.showMessageDialog(pane, "You sold a house and made $" + made + "!", "NICE GOING!",
                        JOptionPane.WARNING_MESSAGE);

                    game.getBoard().getLeftPanel().money(player); //updates players money display
                    return true; //return that it worked
                }
                else //else they don't have any houses to remove
                {
                    JOptionPane.showMessageDialog(pane, "There are no houses to remove", "HEY!", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else //else they aren't allowed to subtract houses from this property because others need houses gone first
        {
            JOptionPane.showMessageDialog(pane, "You need to remove houses from the other properties first.",
                "SOMETHING AIN'T RIGHT", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    /**
     * Returns whether or not the property color set in question is Monopoly owned.
     * @param color The specific property Color being checked
     * @return The player who owns the monopoly
     */
    public Player whoseMonopoly(Color color)
    {
        Player[] players = getOwners(color);
        int counter = 0; //new int counter
        for(int i = 0; i < players.length - 1; i++) //cycles through the length of the array
        {
            if(players[i] != players[i+1] || players[i] == null) //if owners aren't the same or are null
            {
                counter++;
            }
        }
        if(counter == 0) //if all players were the same
        {
            return players[0]; //then it's a monopoly
        }
        return null; //otherwise it's false
    }
}