import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * Left panel shows all those pretty numbers like your name, your money, and your properties
 * 
 * @author Nicholas Smit and Lukas Edman
 * @version 05/04/2013
 */
public class LeftPanel extends JPanel implements ActionListener
{
    //DATA MEMBERS  
    public static final Color Bcolor = new Color(203, 233, 225);

    private JLabel moneyLabel, cardLabel, cardLabel2, nameLabel, propLabel;
    private JPanel cardPanel, oPropPanel, iPropPanel, cardFlow;
    private JScrollPane scroller;
    private JButton[] cardButton;
    private ArrayList<Card> cards;
    private Player player;
    private Game game;
    private Timer timer;
    private int x, y;
    private Color color1, color2, switcher;

    //CONSTRUCTOR
    /**
     * Constructor for objects of class LeftPanel
     */
    public LeftPanel(Game game)
    {
        super();
        this.game = game;
        this.setPreferredSize(new Dimension(200, 500));
        timer = new Timer(0, this);

        color1 = new Color(0, 195, 0);
        color2 = new Color(195, 0, 0);
        switcher = color1;

        JPanel innerPanel = new JPanel();
        innerPanel.setPreferredSize(new Dimension(200, 500));
        innerPanel.setLayout(new GridLayout(4, 0, 50, 10)); //a new grid layout of 3 rows, 1 column
        innerPanel.setBackground(LeftPanel.Bcolor);

        JPanel namePanel = new JPanel();               //makes the panel showing the name
        namePanel.setLayout(new FlowLayout());
        namePanel.setBackground(LeftPanel.Bcolor);
        nameLabel = new JLabel(" ");
        Font font = new Font("Serif", Font.BOLD, 40);
        nameLabel.setFont(font); 
        nameLabel.setForeground(new Color(0, 0, 139));
        namePanel.add(nameLabel);
        innerPanel.add(namePanel);

        JPanel moneyPanel = new JPanel();               //makes the money panel
        moneyPanel.setBackground(LeftPanel.Bcolor);
        moneyPanel.setLayout(new FlowLayout());
        font = new Font("Serif", Font.BOLD, 15);
        JLabel moneyLabel2 = new JLabel("Your Money:");
        moneyLabel2.setFont(font);
        moneyPanel.add(moneyLabel2);

        moneyLabel = new JLabel("1500");
        font = new Font("Serif", Font.BOLD, 50);
        moneyLabel.setFont(font);
        moneyPanel.add(moneyLabel);
        innerPanel.add(moneyPanel);

        oPropPanel = new JPanel();                      //makes the outer property panel
        oPropPanel.setBackground(LeftPanel.Bcolor);
        oPropPanel.setPreferredSize(new Dimension (50, 150));
        oPropPanel.setLayout(new BorderLayout());
        propLabel = new JLabel("Property List");        //with the title
        oPropPanel.add(propLabel, BorderLayout.NORTH);

        iPropPanel = new JPanel();                      //makes the inner property panel
        scroller = new JScrollPane(iPropPanel);         //that lists properties
        oPropPanel.add(scroller, BorderLayout.CENTER);
        innerPanel.add(oPropPanel);

        cardPanel = new JPanel();                       //makes the card display
        cardPanel.setBackground(LeftPanel.Bcolor);
        cardPanel.setPreferredSize(new Dimension(50, 50));
        cardPanel.setLayout(new BorderLayout());
        cardLabel = new JLabel("Card List");
        cardPanel.add(cardLabel, BorderLayout.NORTH);
        cardLabel2 = new JLabel();
        cardPanel.add(cardLabel2, BorderLayout.SOUTH);
        cardFlow = new JPanel();
        cardFlow.setLayout(new FlowLayout());
        cardPanel.add(cardFlow, BorderLayout.CENTER);
        innerPanel.add(cardPanel);

        this.setLayout(new BorderLayout());             //adds all these jpanels to this panel
        this.add(innerPanel, BorderLayout.CENTER);
        addSidePanels(this);
    }

    /**
     * A static method that creates a buffer zone of Jpanels around the specified JPanel.
     * @param panel The JPanel who we're adding sidePanels to
     */
    public static void addSidePanels(JPanel panel)
    {
        JPanel eastP = new JPanel();                    //adds a blank area to the east of the buttons
        eastP.setPreferredSize(new Dimension(10, 200));
        eastP.setBackground(LeftPanel.Bcolor);
        JLabel label1 = new JLabel();
        eastP.add(label1);

        JPanel westP = new JPanel();                    //adds a blank area to the west
        westP.setPreferredSize(new Dimension(10, 200));
        westP.setBackground(LeftPanel.Bcolor);
        JLabel label2 = new JLabel();
        westP.add(label2);

        JPanel northP = new JPanel(); 
        northP.setPreferredSize(new Dimension(75, 30));
        northP.setBackground(LeftPanel.Bcolor);//to the north
        JLabel label3 = new JLabel();
        northP.add(label3);

        JPanel southP = new JPanel();                   //to the south
        southP.setPreferredSize(new Dimension(75, 30));
        southP.setBackground(LeftPanel.Bcolor);
        JLabel label4 = new JLabel();
        southP.add(label4);

        panel.setBackground(LeftPanel.Bcolor); //adds them and colors
        panel.add(southP, BorderLayout.SOUTH);
        panel.add(northP, BorderLayout.NORTH);
        panel.add(westP, BorderLayout.WEST);
        panel.add(eastP, BorderLayout.EAST);

    }

    /**
     * When a player clicks a card button they activate that cards play ability.
     * @param e An ActionEvent that gets sent
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == timer)
        {
            if(x > 0) //if the difference is above zero
            {                
                x--;
                moneyLabel.setText("" + (player.getMoney() + x));
                moneyLabel.setForeground(color2);
            }
            else if(x < 0) //if the difference is below zero
            {
                x++; //add one
                moneyLabel.setText("" + (player.getMoney() + x));
                moneyLabel.setForeground(color1);
            }

            else //until x == 0
            {
                moneyLabel.setForeground(Color.BLACK);
                timer.stop();
            }
        }
        else //else it's not the time and is a button car
        {
            for(int i = 0; i < cardButton.length; i++)
            {
                if(e.getSource() == cardButton)
                {
                    if(cards.get(i).play())
                    {
                        cardPanel.remove(cardButton[i]);
                    }
                }
            }
        }
    }

    /**
     * Updates the Card label to display the cards in the current player's hand.
     * @param player The current player whose cards we're showing
     */
    public void card(Player player)
    {
        cardFlow.removeAll();
        this.player = player;
        cards = player.getCards();
        //put the cards in his hand into an array
        if(cards.isEmpty())
        {
            cardLabel2.setText("No Cards in Hand");
        }
        else
        {
            cardButton = new JButton[cards.size()];
            for(int i = 0; i < cards.size(); i++)
            {
                cardButton[i] = new JButton(cards.get(i).getImage());
                cardButton[i].addActionListener(this);
                cardFlow.add(cardButton[i]);
            }
        }
    }

    /**
     * Updates the money label to display the money the player has.
     */
    public void money(Player player)
    {
        this.player = player;
        if(player.getPrevMoney() == player.getMoney()) //if new turn this'll happen
        {
            moneyLabel.setText("" + player.getMoney()); //shows current money of current player
        }
        else
        {
            x = player.getPrevMoney() - player.getMoney();
            //difference between last and current money amounts
            y = 0;

            int delay = Math.abs(Math.round(2000/x));
            timer.setDelay(delay); //so the time changes based on the difference in money

            timer.start();
        }
    }

    /**
     * Updates the name label to display the current player's name.
     */
    public void name(Player player)
    {
        nameLabel.setText(player.getName());
    }

    /**
     * Updates the property label to display the property the current player owns.
     */
    public void property(Player player)
    {
        this.player = player;
        Property[] property = player.getProperty().toArray(new Property[0]);
        iPropPanel.removeAll();
        iPropPanel.setLayout(new GridLayout(property.length, 2));
        if(property.length == 0)
        {
            JLabel label = new JLabel("NONE");
            Font font = new Font("Serif", Font.BOLD, 20);
            label.setFont(font);
            iPropPanel.add(label);
        }
        else
        {
            iPropPanel.removeAll();
            JLabel[] labels = new JLabel[property.length];
            for(int i = 0; i < property.length; i++)
            {
                labels[i] = new JLabel(property[i].getName());
                labels[i].setForeground(property[i].getColor());
                iPropPanel.add(labels[i]);
            }
        }
    }
}