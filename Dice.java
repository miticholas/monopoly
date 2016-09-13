import java.util.Random;
/**
 * The variables and methods of an individual die in Monopoly.
 * 
 * @author Nicholas Smit and a Moist Sandal
 * @version 04/25/2013
 */
public class Dice
{
    public static final int SIDES = 6;
    private Random random;
    private int number;

    /**
     * Constructor for the Dice class.
     */
    public Dice()
    {
        random = new Random();
        number = 1;
    }

    /**
     * Gets the roll associated with the die.
     * @return The integer roll value
     */
    public int getRoll()
    {
        return number;
    }

    /**
     * Rolls the die, returns the roll value.
     * @return The integer roll value
     */
    public int roll()
    {
        number = random.nextInt(SIDES) + 1;
        return number;
    }
}