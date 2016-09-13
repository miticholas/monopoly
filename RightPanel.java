import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Panel that includes lots of cool buttons: roll dice button, quit button, trading button, mortgage button, house button, and end turn button
 * 
 * @author Nicholas Smit and Lukas Edman
 */
public class RightPanel extends JPanel implements ActionListener
{
    private JButton rollButton, quitButton, tradeButton, mortgageButton, houseButton, endTurnButton;
    private Game game;
    private JOptionPane pane;

    /**
     * Constructor for objects of class RightPanel
     * @param game the game class that has the methods for the buttons
     */
    public RightPanel(Game game)
    {
        super();
        this.game = game;
        this.setPreferredSize(new Dimension(200, 500));
        this.setBackground(LeftPanel.Bcolor);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(6, 1, 20, 10));
        innerPanel.setBackground(LeftPanel.Bcolor);

        rollButton = new JButton(loadIcons()[0]);
        rollButton.addActionListener(this);

        quitButton = new JButton(loadIcons()[1]);
        quitButton.addActionListener(this);

        endTurnButton = new JButton(loadIcons()[2]);
        endTurnButton.addActionListener(this);

        tradeButton = new JButton(loadIcons()[3]);
        tradeButton.addActionListener(this);

        mortgageButton = new JButton(loadIcons()[4]);
        mortgageButton.addActionListener(this);

        houseButton = new JButton(loadIcons()[5]);
        houseButton.addActionListener(this);

        innerPanel.add(mortgageButton);
        innerPanel.add(houseButton);
        innerPanel.add(tradeButton);
        innerPanel.add(rollButton);
        innerPanel.add(endTurnButton);
        innerPanel.add(quitButton);

       

        this.setLayout(new BorderLayout());             //adds all these jpanels to this panel
        this.add(innerPanel, BorderLayout.CENTER);
        LeftPanel.addSidePanels(this);
    }

    /**
     * Gives a function to all the buttons
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == rollButton)
        {
            if(game.mustSell())
            {
                this.mustSellPane();
            }
            else
            {
                game.rollDice();  
            }
        }
        else if(e.getSource() == houseButton) //tells you how to add/remove a house
        {
            Object[] options = {"Okay"}; 
            int n = JOptionPane.showOptionDialog(pane, "LEFT CLICK on the Property that You would like to Add or Remove a House", "Adding/Removing Houses",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        }
        else if(e.getSource() == mortgageButton) //tells you how to mortgage
        {       
            Object[] options = {"Okay"}; 
            int n = JOptionPane.showOptionDialog(pane, "RIGHT CLICK on the Property that You would like to Mortgage/Unmortgage", "Mortgaging Properties",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        }
        else if(e.getSource() == quitButton) //quits game
        {
            game.playerQuit();
        }
        else if(e.getSource() == tradeButton)
        {
            Object[] options = {"Shucks"}; 
            int n = JOptionPane.showOptionDialog(pane, "This option is only available in the DeeLucks™ verson", "Sorry bro",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        }
        else if(e.getSource() == endTurnButton)
        {
            if(game.haveToRoll()) //you cant end your turn before rolling!
            {
                game.getBoard().haventRolled();
            }
            else
            {
                if(game.mustSell()) //you cant end your turn owing money
                {
                    this.mustSellPane();
                }
                else
                {
                    game.nextTurn();
                }
            }
        }
    }

    /**
     * Prompts a pane that says you must sell stuff before doing something else
     */
    public void mustSellPane()
    {
        Object[] options = {"Oooohhh"}; //custom button name
        int n = JOptionPane.showOptionDialog(pane, "LOCKDOWN MODE" +
                "\nYou Can't Do This Until You Pay Your Debt!", "HA",
                JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        //an optionpane that informs the player they already rolled the die, and not doubles
    }
    
    

    /**
     * Loads icons for buttons
     */
    public ImageIcon[] loadIcons()
    {

        {
            ImageIcon[] icons = new ImageIcon[6];

            try
            {
                BufferedImage rollDice = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/roll_dice_button.png"));
                Image newRollDice = rollDice.getScaledInstance((int)75,(int)75,Image.SCALE_SMOOTH);
                icons[0] = new ImageIcon(newRollDice);

                BufferedImage quitGame = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/quit_game.png"));
                Image newQuitGame = quitGame.getScaledInstance((int)75,(int)75,Image.SCALE_SMOOTH);
                icons[1] = new ImageIcon(newQuitGame);

                BufferedImage endTurn = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/end_turn.png"));
                Image newEndTurn = endTurn.getScaledInstance((int)75,(int)75,Image.SCALE_SMOOTH);
                icons[2] = new ImageIcon(newEndTurn);

                BufferedImage trade = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/trade_button.png"));
                Image newTrade = trade.getScaledInstance((int)75,(int)75,Image.SCALE_SMOOTH);
                icons[3] = new ImageIcon(newTrade);

                BufferedImage mortgage = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/mortgage_button.png"));
                Image newMortgage = mortgage.getScaledInstance((int)75,(int)75,Image.SCALE_SMOOTH);
                icons[4] = new ImageIcon(newMortgage);

                BufferedImage houseB = ImageIO.read(new Object() { }.getClass().getEnclosingClass().getResource("images/addremovehouse.png"));
                Image newHouseB = houseB.getScaledInstance((int)113,(int)75,Image.SCALE_SMOOTH);
                icons[5] = new ImageIcon(newHouseB);

                return icons;
            }  
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null,  " File read error: " + e.getMessage());
            }
            return null;
        }
    }
}
