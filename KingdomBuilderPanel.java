import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class KingdomBuilderPanel extends JPanel implements MouseMotionListener, MouseListener {
	static final int WIDTH = 1600, HEIGHT = 960;
	static final int GAMEBOARD_MARGIN_X = 200, GAMEBOARD_MARGIN_Y = 125;
	static final int BORDER_WIDTH = 5;
	
	private Gameboard board;
	private Gameboard [] boards = new Gameboard[4];
	
	private ArrayList<ObjectiveCard> objectivesList = new ArrayList<ObjectiveCard> ();
	private ObjectiveCard objective1, objective2, objective3;
	private ArrayList<TerrainCard> deck = new ArrayList<TerrainCard> ();
	
	public KingdomBuilderPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		for (int i = 0; i < 4; i++) {
			try { boards[i] = new Gameboard(new File(System.getProperty("user.dir") + "/src/GameboardFiles/" + Gameboard.names[i] + ".txt")); }
			catch (IOException e) { e.printStackTrace(); }
		} board = new Gameboard(boards[0], boards[1], boards[2], boards[3]);
		
		for (int i = 0; i < ObjectiveCard.names.length; i++) {
			objectivesList.add(new ObjectiveCard(i));
		} Collections.shuffle(objectivesList);
		objective1 = objectivesList.get(0); objective2 = objectivesList.get(1); objective3 = objectivesList.get(2);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(BORDER_WIDTH));
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 48);
		
		// Directions display (rectangle)
		g.setColor(new Color(100, 100, 255));
		g.fillRoundRect(10, 10, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH - 20, GAMEBOARD_MARGIN_Y - 20, 15, 15);
		g.setColor(new Color(255, 215, 0));
		g.drawRoundRect(10, 10, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH - 20, GAMEBOARD_MARGIN_Y - 20, 15, 15);
		// Directions text display
		drawCenteredString(g, "Directions", f, 0, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH, GAMEBOARD_MARGIN_Y * 2 / 3);
		
		// Gameboard Display
		board.display(g, GAMEBOARD_MARGIN_X, GAMEBOARD_MARGIN_Y);
		
		// Objective Cards Box (Panel) display
		f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		g2.drawRect(BORDER_WIDTH, GAMEBOARD_MARGIN_Y, GAMEBOARD_MARGIN_X - BORDER_WIDTH, 900);
		// Objective Cards text
		g.setColor(Color.CYAN);
		g2.drawRoundRect(2 * BORDER_WIDTH, GAMEBOARD_MARGIN_Y + BORDER_WIDTH, GAMEBOARD_MARGIN_X - 3 * BORDER_WIDTH, 50, 15, 15);
		g.setColor(Color.BLACK);
		drawCenteredString(g, "Objective Cards", f, 0, 200, 160);
		// Objective Cards Display
		objective1.display(g, 100 - ObjectiveCard.WIDTH / 2, GAMEBOARD_MARGIN_Y + BORDER_WIDTH + 60);
		objective2.display(g, 100 - ObjectiveCard.WIDTH / 2, GAMEBOARD_MARGIN_Y + BORDER_WIDTH + 65 + ObjectiveCard.HEIGHT);
		objective3.display(g, 100 - ObjectiveCard.WIDTH / 2, GAMEBOARD_MARGIN_Y + BORDER_WIDTH + 70 + 2 * ObjectiveCard.HEIGHT);
		
		// Player Boxes Display
		int x = GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH;
		g.drawLine(x, 210, WIDTH, 210);
		g.drawLine(x, 210 + 375, WIDTH, 210 + 375);
		g.drawLine(x, 210, x, HEIGHT);
		g.drawLine((x + WIDTH) / 2, 210, (x + WIDTH) / 2, HEIGHT);
	}
	
	public void drawCenteredString(Graphics g, String s, Font f, int x1, int x2, int y) {
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int x = (((x2 - x1) - fm.stringWidth(s)) / 2) + x1;
		g.drawString(s, x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseClicked(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) {

	}
	public void mouseDragged(MouseEvent e) { }
}