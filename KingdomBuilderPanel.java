import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.imageio.*;

public class KingdomBuilderPanel extends JPanel implements MouseMotionListener, MouseListener {
    static final int WIDTH = 1800, HEIGHT = 1030;
    static int GAMEBOARD_MARGIN_X = 1720 / 2 - Gameboard.SMALL_WIDTH - 35 / 2, GAMEBOARD_MARGIN_Y = HEIGHT / 2 - Gameboard.SMALL_HEIGHT;
    static final int BORDER_WIDTH = 5;
    static final int RHS_START_X = 1150;

    private Gameboard board;
    private Gameboard [] boards = new Gameboard[4];

    private ArrayList<ObjectiveCard> objectivesList = new ArrayList<ObjectiveCard> ();
    private ObjectiveCard objective1, objective2, objective3;
    private ArrayList<TerrainCard> deck = new ArrayList<TerrainCard> ();
    private ArrayList<TerrainCard> discardPile = new ArrayList<TerrainCard> ();
    private TerrainCard cardBack = new TerrainCard(-1);

    private Player [] players = new Player[4];
    private int currentPlayerID = -1;

    private BufferedImage summary1, summary2, summary3, summary4;
    private BufferedImage background, frame, interior, strip;
    private BufferedImage [] objectiveIcons;

    private Button continueButton, endTurnButton;

    public KingdomBuilderPanel() {
    	System.out.println(GAMEBOARD_MARGIN_X);
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
        objective1.setCoords(1263 - ObjectiveCard.WIDTH, 30);
        objective2.setCoords(1263 - ObjectiveCard.WIDTH / 2, 30);
        objective3.setCoords(1263, 30);

        try { TerrainCard.CARD_BACK = ImageIO.read(this.getClass().getResource("/Images/Card Back.png")); }
        catch (Exception e) { e.printStackTrace(); }
        cardBack.setDimensions(100, 150);
        try {
            for (int i = 0; i < LocationTile.names.length; i++) {
                LocationTile.images[i] = ImageIO.read(this.getClass().getResource("/Images/Location Tile - " + LocationTile.names[i] + ".png"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        try {
            Hexagon.castle = ImageIO.read(this.getClass().getResource("/Images/Castle.png"));
        } catch (Exception e) { e.printStackTrace(); }
        try {
            BufferedImage img1 = ImageIO.read(this.getClass().getResource("/Images/button.png"));
            BufferedImage img2 = ImageIO.read(this.getClass().getResource("/Images/button (2).png"));
            BufferedImage img3 = ImageIO.read(this.getClass().getResource("/Images/button (1).png"));
            continueButton = new Button(img1, img2, img3);
            continueButton.setWidth(140); continueButton.setHeight(50);
            continueButton.setCenterCoords(1700, 965);
            continueButton.setEnabled(true);
        } catch (Exception e) { e.printStackTrace(); }
        try {
            background = ImageIO.read(this.getClass().getResource("/Images/Light Wood Background.jpg"));
            frame = ImageIO.read(this.getClass().getResource("/Images/Dark Wood Background.png"));
            interior = ImageIO.read(this.getClass().getResource("/Images/Ocean Background.png"));
            strip = ImageIO.read(this.getClass().getResource("/Images/Light Wood Strip.png"));
            objectiveIcons = new BufferedImage[ObjectiveCard.names.length];
            for (int i = 0; i < ObjectiveCard.names.length; i++) {
            	objectiveIcons[i] = ImageIO.read(this.getClass().getResource("/Images/" + ObjectiveCard.names[i] + " Icon.png"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        for (int i = 0; i < 4; i++) players[i] = new Player(i);

        // Set location Tiles
        ArrayList<Integer> locationTilesList = new ArrayList<Integer> ();
        for (int i = 0; i < LocationTile.names.length; i++) locationTilesList.add(i);
        Collections.shuffle(locationTilesList);
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                if (board.board[i][j].getType() == 7) {
                    if (i < Gameboard.SMALL_SIZE && j < Gameboard.SMALL_SIZE) board.board[i][j].locationTile = new LocationTile(i, j, locationTilesList.get(0));
                    if (i < Gameboard.SMALL_SIZE && j >= Gameboard.SMALL_SIZE) board.board[i][j].locationTile = new LocationTile(i, j, locationTilesList.get(1));
                    if (i >= Gameboard.SMALL_SIZE && j < Gameboard.SMALL_SIZE) board.board[i][j].locationTile = new LocationTile(i, j, locationTilesList.get(2));
                    if (i >= Gameboard.SMALL_SIZE && j >= Gameboard.SMALL_SIZE) board.board[i][j].locationTile = new LocationTile(i, j, locationTilesList.get(3));
                }
            }
        }
        try {
            summary1 = ImageIO.read(this.getClass().getResource("/Images/Summary Location Tile - " + LocationTile.names[locationTilesList.get(0)] + ".png"));
            summary2 = ImageIO.read(this.getClass().getResource("/Images/Summary Location Tile - " + LocationTile.names[locationTilesList.get(1)] + ".png"));
            summary3 = ImageIO.read(this.getClass().getResource("/Images/Summary Location Tile - " + LocationTile.names[locationTilesList.get(2)] + ".png"));
            summary4 = ImageIO.read(this.getClass().getResource("/Images/Summary Location Tile - " + LocationTile.names[locationTilesList.get(3)] + ".png"));
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 48);

        // Directions display (rectangle)
        g.setColor(new Color(100, 100, 255));
        g.fillRoundRect(40, 5, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH, GAMEBOARD_MARGIN_Y - 45, 30, 30);
        g.setColor(new Color(255, 215, 0));
        g.drawRoundRect(40, 5, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH, GAMEBOARD_MARGIN_Y - 45, 30, 30);
        // Directions text display
        drawCenteredString(g, "Directions", f, 40, 40 + GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH, (GAMEBOARD_MARGIN_Y - 25) * 2 / 3);

        // Gameboard Display
        g.drawImage(frame, GAMEBOARD_MARGIN_X, GAMEBOARD_MARGIN_Y - 30, Gameboard.LARGE_WIDTH + 35, Gameboard.LARGE_HEIGHT + 45, null);
        g.setColor(new Color(255, 255, 255, 25));
        g.fillRect(GAMEBOARD_MARGIN_X, GAMEBOARD_MARGIN_Y - 30, Gameboard.LARGE_WIDTH + 35, Gameboard.LARGE_HEIGHT + 45);
        g.drawImage(interior, GAMEBOARD_MARGIN_X + 10, GAMEBOARD_MARGIN_Y - 20, Gameboard.LARGE_WIDTH + 15, Gameboard.LARGE_HEIGHT + 25, null);
        board.display(g, GAMEBOARD_MARGIN_X + 30, GAMEBOARD_MARGIN_Y);

        // Deck and Discard Pile Panel Rectangle
        g.setColor(Color.BLACK);
        g.drawRect(5, 300, 130, 200);
        g.drawRect(5, 500, 130, 200);
        // Deck and Discard text
        f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        drawCenteredString(g, "Deck", f, 5, 135, 330);
        drawCenteredString(g, "Discard Pile", f, 5, 135, 530);
        // Deck and Discard images
        if (true || !deck.isEmpty()) cardBack.display(g, 20, 340);

        // Player Boxes Display
        for (int i = 0; i < 4; i++) players[i].display(g);

//        // Objective Cards Box (Panel) display
        f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
//        // Objective Cards text
//        g.setColor(Color.BLACK);
//        drawCenteredString(g, "Objective Cards", f, RHS_START_X, RHS_START_X + 225, 25);
//        // Objective Cards Display
//        objective3.display(g); objective2.display(g); objective1.display(g);
//        if (objective3.isEnlarged()) objective3.display(g);
//        if (objective2.isEnlarged()) objective2.display(g);
//        if (objective1.isEnlarged()) objective1.display(g);
        
        // Summary Location Cards
        int width = 100, height = 93;
        g.drawImage(summary1, 75 - width / 2, 80, width, height, null);
        g.drawImage(summary2, 75 - width / 2, 85 + height, width, height, null);
        g.drawImage(summary3, 75 - width / 2, 710, width, height, null);
        g.drawImage(summary4, 75 - width / 2, 715 + height, width, height, null);

        // Continue Button Display
        continueButton.display(g);
        
        g.drawImage(strip, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45, null);
        g.setColor(new Color(119, 47, 47));
        g.drawRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45);
        g.setColor(new Color(242, 235, 205, 200));
        g.fillRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45);
        
        int gap = (Gameboard.LARGE_HEIGHT + 45) / 3;
        g.setColor(Color.BLACK);
        ObjectiveCard [] objectives = new ObjectiveCard[] { objective1, objective2, objective3 };
        for (int i = 0; i < 3; i++) {
        	int y = getX(g, ObjectiveCard.names[objective1.getID()], f, GAMEBOARD_MARGIN_Y - 30 + gap * i, GAMEBOARD_MARGIN_Y - 30 + gap * (i + 1), 0) - 10;
        	g.drawImage(objectiveIcons[objectives[i].getID()], GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 280, y, 30, 30, null);
        }
        
        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 2);
        g2.setTransform(at);
        
        for (int i = 0; i < 3; i++) {
        	int x = getX(g, ObjectiveCard.names[objective1.getID()], f, GAMEBOARD_MARGIN_Y - 30 + gap * i, GAMEBOARD_MARGIN_Y - 30 + gap * (i + 1), 0) + 30;
        	g.drawString(objectives[i].toString(), x, -(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 285));
        }
    }

    public static void drawCenteredString(Graphics g, String s, Font f, int x1, int x2, int y) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int x = (((x2 - x1) - fm.stringWidth(s)) / 2) + x1;
        g.drawString(s, x, y);
    }
    public static int getX(Graphics g, String s, Font f, int x1, int x2, int y) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int x = (((x2 - x1) - fm.stringWidth(s)) / 2) + x1;
        return x;
    }

    public void clickButton(int x, int y) {
        if (continueButton.isClicked(x, y)) {
            continueButton.setEnabled(false);
            continueButton.setHovering(false);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX(), y = e.getY();

        clickButton(x, y);
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                board.board[i][j].setHighlighted(false);
            }
        }
        out:
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                if (board.board[i][j].contains(x, y)) {
                    board.board[i][j].setHighlighted(true);
                    break out;
                }
            }
        }
        repaint();
    }
    public void mouseClicked(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {
        if (!continueButton.isEnabled()) {
            continueButton.setEnabled(true);
        }
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
//        int x = e.getX(), y = e.getY();
//        objective1.reset(); objective2.reset(); objective3.reset();
//        if (objective1.contains(x, y)) objective1.enlarge();
//        else if (objective2.contains(x, y)) objective2.enlarge();
//        else if (objective3.contains(x, y)) objective3.enlarge();
//
//        continueButton.setHovering(continueButton.contains(x, y));
//        repaint();
    }

    public void mouseDragged(MouseEvent e) { }
}
