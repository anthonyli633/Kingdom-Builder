<<<<<<< HEAD
import java.awt.*;
import javax.swing.*;

public KingdomBuilderPanel extends JPanel {
    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(0, 0, 100, 100);
=======
public class KingdomBuilderPanel extends JPanel implements MouseListener
{
    private ArrayList<TerrainCard> terrainCards;
    public Player [] players;
    GameState state;
    public static Gameboard gameboard;
    public KingdomBuilderPanel()
    {

    }
    public void paint(Graphics g)
    {

    }
    public void displayBoard(Graphics g)
    {

    }
    public void displayPlayerIcons(Graphics g)
    {

    }
    public void displayDecks(Graphics g)
    {

    }
    public void displayPlayerScreen(Graphics g)
    {

    }
    public void clickHexagon(int x, int y)
    {

    }
    public void shuffleDeck()
    {

    }
    public boolean hasGameEnded()
    {

    }
    public void reset()
    {

    }
    public void clickButton(int x, int y)
    {

    }
  
}