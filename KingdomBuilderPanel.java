import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class KingdomBuilderPanel extends JPanel implements MouseMotionListener, MouseListener {
	static final int WIDTH = 1600, HEIGHT = 1000;
	static final int GAMEBOARD_MARGIN_X = 160, GAMEBOARD_MARGIN_Y = 125;
	static final int BORDER_WIDTH = 5;
	
	private Gameboard board;
	private Gameboard [] boards = new Gameboard[4];
	
	private ArrayList<ObjectiveCard> objectivesList = new ArrayList<ObjectiveCard> ();
	private ObjectiveCard objective1, objective2, objective3;
	private ArrayList<TerrainCard> deck = new ArrayList<TerrainCard> ();
	private ArrayList<TerrainCard> discardPile = new ArrayList<TerrainCard> ();
	
	private int x1, y1, x2, y2;
	
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
		g.fillRoundRect(40, 10, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH - 80, GAMEBOARD_MARGIN_Y - 20, 30, 30);
		g.setColor(new Color(255, 215, 0));
		g.drawRoundRect(40, 10, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH - 80, GAMEBOARD_MARGIN_Y - 20, 30, 30);
		// Directions text display
		drawCenteredString(g, "Directions", f, 0, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH, GAMEBOARD_MARGIN_Y * 2 / 3);
		
		// Gameboard Display
		board.display(g, GAMEBOARD_MARGIN_X, GAMEBOARD_MARGIN_Y);
		
		// Deck and Discard Pile Panel Rectangle
		g.setColor(Color.BLACK);
		g.drawRect(5, 250, 150, 250);
		g.drawRect(5, 500, 150, 250);
		// Deck and Discard text
		f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		drawCenteredString(g, "Deck", f, 0, 155, 280);
		drawCenteredString(g, "Discard Pile", f, 0, 155, 530);
		// Deck and Discard images
		BufferedImage cardBack = null;
		try { cardBack = ImageIO.read(this.getClass().getResource("/Images/Card Back.png")); }
		catch (IOException e) { e.printStackTrace(); }
		if (true || !deck.isEmpty()) g.drawImage(cardBack, 20, 300, 120, 180, null);
		
		// Objective Cards Box (Panel) display
		f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		// Objective Cards text
		g.setColor(Color.BLACK);
		drawCenteredString(g, "Objective Cards", f, 1150, 1150 + 225, 25);
		// Objective Cards Display
		objective1.display(g, 1263, 30);
		objective2.display(g, 1263 - ObjectiveCard.WIDTH / 2, 30);
		objective3.display(g, 1263 - ObjectiveCard.WIDTH, 30);
		
		// Player Boxes Display
		int x = 1150;
		g.drawLine(x, 210, WIDTH, 210);
		g.drawLine(x, 210 + 375, WIDTH, 210 + 375);
		g.drawLine(x, 210, x, HEIGHT);
		g.drawLine((x + WIDTH) / 2, 210, (x + WIDTH) / 2, HEIGHT);
		
		// End Turn Display
		
	}
	
	public void drawCenteredString(Graphics g, String s, Font f, int x1, int x2, int y) {
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int x = (((x2 - x1) - fm.stringWidth(s)) / 2) + x1;
		g.drawString(s, x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < board.LARGE_SIZE; i++) {
			for (int j = 0; j < board.LARGE_SIZE; j++) {
				if (board.board[i][j].contains(x, y)) {
					board.board[i][j].setHighlighted(true);
				}
			}
		} 
		x1 = x; y1 = y; x2 = x + 100; y2 = y + 100;
		repaint();
	}
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) {

	}
	public void mouseDragged(MouseEvent e) { }
}
