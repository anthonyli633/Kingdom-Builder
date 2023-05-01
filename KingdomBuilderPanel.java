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

    static Color SHADE = new Color(0, 0, 0, 100);

    private Gameboard board;
    private Gameboard [] boards = new Gameboard[4];

    private ArrayList<ObjectiveCard> objectivesList = new ArrayList<ObjectiveCard> ();
    private ObjectiveCard objective1, objective2, objective3;
    private ArrayList<TerrainCard> deck = new ArrayList<TerrainCard> ();
    private ArrayList<TerrainCard> discardPile = new ArrayList<TerrainCard> ();
    private TerrainCard cardBack = new TerrainCard(-1);

    public static Player [] players = new Player[4];
    static int currentPlayerID = 0;
 
    private BufferedImage summary1, summary2, summary3, summary4;
    private BufferedImage background, frame, interior, strip, leftArrow, rightArrow;
    private BufferedImage [] objectiveIcons;
    
    private ArrayList<String> gameLog = new ArrayList<String> ();   
    private Button endTurnButton;

    private boolean isObjectiveExpanded = false;
    private Rectangle expandPanel;

    static GameState state, prevState;

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
        objective1.setCoords(1263 - ObjectiveCard.WIDTH, 30);
        objective2.setCoords(1263 - ObjectiveCard.WIDTH / 2, 30);
        objective3.setCoords(1263, 30);

        // Read Images
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
            BufferedImage img2 = ImageIO.read(this.getClass().getResource("/Images/button (1).png"));
            BufferedImage img3 = ImageIO.read(this.getClass().getResource("/Images/button (2).png"));
            endTurnButton = new Button(img1, img2, img3);
            endTurnButton.setWidth(140); endTurnButton.setHeight(50);
            endTurnButton.setCenterCoords(1600, 965);
            endTurnButton.setEnabled(false);
        } catch (Exception e) { e.printStackTrace(); }
        try {
            BufferedImage img1 = ImageIO.read(this.getClass().getResource("/Images/button.png"));
            BufferedImage img2 = ImageIO.read(this.getClass().getResource("/Images/button (2).png"));
            BufferedImage img3 = ImageIO.read(this.getClass().getResource("/Images/button (1).png"));
        } catch (Exception e) { e.printStackTrace(); }
        try {
            background = ImageIO.read(this.getClass().getResource("/Images/Light Wood Background.jpg"));
            frame = ImageIO.read(this.getClass().getResource("/Images/Dark Wood Background.png"));
            interior = ImageIO.read(this.getClass().getResource("/Images/Ocean Background.png"));
            strip = ImageIO.read(this.getClass().getResource("/Images/Light Wood Strip.png"));
            leftArrow = ImageIO.read(this.getClass().getResource("/Images/Left Arrow Vector 1.png"));
            rightArrow = ImageIO.read(this.getClass().getResource("/Images/Right Arrow Vector 1.png"));
            objectiveIcons = new BufferedImage[ObjectiveCard.names.length];
            for (int i = 0; i < ObjectiveCard.names.length; i++) {
                objectiveIcons[i] = ImageIO.read(this.getClass().getResource("/Images/" + ObjectiveCard.names[i] + " Icon.png"));
            }
            TerrainCard.cardImages = new BufferedImage[7];
            for (int i = 0; i < 7; i++) {
                try {
                    TerrainCard.cardImages[i] = ImageIO.read(this.getClass().getResource("/Images/" + TerrainCard.names[i] + " Card.png"));
                } catch (Exception e) { }
            }
            Settlement.images = new BufferedImage[Settlement.colors.length];
            for (int i = 0; i < Settlement.colors.length; i++) {
                Settlement.images[i] = ImageIO.read(this.getClass().getResource("/Images/" + Settlement.colors[i] + " Settlement.png"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/src/Font/Kingdom Builder Font.ttf")));
        } catch (IOException|FontFormatException e) { e.printStackTrace(); }

        // Init Deck
        for (int i = 0; i < 5; i++)
            for (int j: new int[] {0, 1, 2, 3, 5})
                deck.add(new TerrainCard(j));
        Collections.shuffle(deck);

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
            summary1 = ImageIO.read(this.getClass().getResource("/Images/Summary Location - " + LocationTile.names[locationTilesList.get(0)] + ".png"));
            summary2 = ImageIO.read(this.getClass().getResource("/Images/Summary Location - " + LocationTile.names[locationTilesList.get(1)] + ".png"));
            summary3 = ImageIO.read(this.getClass().getResource("/Images/Summary Location - " + LocationTile.names[locationTilesList.get(2)] + ".png"));
            summary4 = ImageIO.read(this.getClass().getResource("/Images/Summary Location - " + LocationTile.names[locationTilesList.get(3)] + ".png"));
        } catch(Exception e) { e.printStackTrace(); }

        // Initiating players
        for (int i = 0; i < 4; i++) players[i] = new Player(i);
        for (int i = 0; i < 4; i++) players[i].setTerrainCard(deck.remove(0));
        // players[0].setTerrainCard(new TerrainCard(1));

        state = GameState.cardOrLocationTileSelection;
    }

    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(BORDER_WIDTH));

        Font f = new Font("Thunder Kingdom", Font.BOLD, 40);

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
        g.drawImage(strip, 5, 300, 130, 400, null);
        g.setColor(new Color(242, 235, 205, 200));
        g.fillRect(5, 300, 130, 400);
        g.setColor(new Color(119, 47, 47));
        g.drawRect(5, 300, 130, 200);
        g.drawRect(5, 500, 130, 200);
        g.setColor(Color.BLACK);
        // Deck and Discard text
        f = new Font("Thunder Kingdom", Font.BOLD, 16);
        drawCenteredString(g, "Deck", f, 5, 135, 330);
        drawCenteredString(g, "Discard Pile", f, 5, 135, 530);
        // Deck and Discard images
        if (!deck.isEmpty()) { cardBack.setCoords(20, 340); cardBack.displayBack(g); }
        if (!discardPile.isEmpty()) { 
        	TerrainCard card = discardPile.get(discardPile.size() - 1);
        	card.isDarkened = false;
        	card.setCoords(20, 540);
        	card.setDimensions(100, 150);
        	card.displayFront(g);
        }

        // Player Boxes Display
        for (int i = 0; i < 4; i++) 
        	if (i != currentPlayerID && (!isObjectiveExpanded || i % 2 == 0)) players[i].display(g);
        players[currentPlayerID].display(g);
        if (tempSettlement != null) tempSettlement.display(g);

//        // Objective Cards Box (Panel) display
        f = new Font("Thunder Kingdom", Font.BOLD, 20);
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

        // End Turn Button Display
        endTurnButton.display(g);

        g.drawImage(strip, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 310, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45, null);
        g.setColor(new Color(119, 47, 47));
        g.drawRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 310, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45);
        g.setColor(new Color(242, 235, 205, 200));
        g.fillRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 310, GAMEBOARD_MARGIN_Y - 30, 50, Gameboard.LARGE_HEIGHT + 45);

        if (!isObjectiveExpanded) {
            expandPanel = new Rectangle(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45);
            g.drawImage(strip, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45, null);
            g.setColor(new Color(119, 47, 47));
            g.drawRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45);
            g.setColor(new Color(242, 235, 205, 200));
            g.fillRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45);
            height = 40;
            g.drawImage(leftArrow, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270, (GAMEBOARD_MARGIN_Y + Gameboard.LARGE_HEIGHT + 25) / 2 + height / 2, 40, height, null);
        } else {
        	g.drawImage(strip, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH +50 , GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45, null);
            g.setColor(new Color(119, 47, 47));
            g.drawRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 50 , GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45);
            g.setColor(new Color(242, 235, 205, 200));
            g.fillRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 50 , GAMEBOARD_MARGIN_Y - 30, 40, Gameboard.LARGE_HEIGHT + 45);

            g.drawImage(strip, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 90, GAMEBOARD_MARGIN_Y - 30, 200, Gameboard.LARGE_HEIGHT + 45, null);
            g.setColor(new Color(119, 47, 47));
            g.drawRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 90, GAMEBOARD_MARGIN_Y - 30, 200, Gameboard.LARGE_HEIGHT + 45);
            g.setColor(new Color(242, 235, 205, 200));
            g.fillRect(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 90, GAMEBOARD_MARGIN_Y - 30, 200, Gameboard.LARGE_HEIGHT + 45);
            height = 40;
            objective1.setCoords(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH+95, 100);
            objective2.setCoords(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH+95, 375);
            objective3.setCoords(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH+95, 650);
            objective1.display(g);
            objective2.display(g);
            objective3.display(g);
            g.drawImage(rightArrow, GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH +50, (GAMEBOARD_MARGIN_Y + Gameboard.LARGE_HEIGHT + 25) / 2 + height / 2, 40, height, null);
        }

        int gap = (Gameboard.LARGE_HEIGHT + 45) / 3;
        g.setColor(Color.BLACK);
        ObjectiveCard [] objectives = new ObjectiveCard[] { objective1, objective2, objective3 };
        for (int i = 0; i < 3; i++) {
            int y = getX(g, ObjectiveCard.names[objective1.getID()], f, GAMEBOARD_MARGIN_Y - 30 + gap * i, GAMEBOARD_MARGIN_Y - 30 + gap * (i + 1), 0) - 10;
            g.drawImage(objectiveIcons[objectives[i].getID()], GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 320, y, 30, 30, null);
        }

        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 2);
        g2.setTransform(at);

        for (int i = 0; i < 3; i++) {
            int x = getX(g, ObjectiveCard.names[objective1.getID()], f, GAMEBOARD_MARGIN_Y - 30 + gap * i, GAMEBOARD_MARGIN_Y - 30 + gap * (i + 1), 0) + 30;
            g.drawString(objectives[i].toString(), x, -(GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 325));
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
    
    public void updateScore() {
    	Player p = players[currentPlayerID];
    	int score1 = objective1.score(board, p), score2 = objective2.score(board, p), score3 = objective3.score(board, p);
    	int score = score1 + score2 + score3;
    	System.out.printf("%s: %d, %s: %d, %s: %d\n", objective1.toString(), score1, objective2.toString(), score2, objective3.toString(), score3);
    	p.setScore(score);
    }

    private boolean advanceState, isMovable;
    private Settlement tempSettlement;
    private LocationTile chosenLocationTile;
    private int selectedRow, selectedCol;
    private int diffX, diffY;
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        if (isBeingDragged) return;
        
        if (endTurnButton.isEnabled() && endTurnButton.contains(x, y)) {
        	endTurnButton.click();
        }
        
        int locX = GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 270;
        int locY = GAMEBOARD_MARGIN_Y - 30;
        if (!isObjectiveExpanded && x >= locX && y >= locY && x <= locX + 40 && y <= locY + Gameboard.LARGE_HEIGHT + 45){
            isObjectiveExpanded=true;
        } 
        locX = GAMEBOARD_MARGIN_X + Gameboard.LARGE_WIDTH + 50;
        locY = GAMEBOARD_MARGIN_Y - 30;
        if (isObjectiveExpanded && x >= locX && y >= locY && x <= locX + 40 && y <= locY + Gameboard.LARGE_HEIGHT + 45){
            isObjectiveExpanded = false;
        } repaint();
        
        switch (state) {
            case cardOrLocationTileSelection:
                boolean hasOneSelected = false;
                if (players[currentPlayerID].getTerrainCard().contains(x, y) && !players[currentPlayerID].getTerrainCard().isDarkened) {
                    players[currentPlayerID].getTerrainCard().setHighlighted(true);
                    hasOneSelected = true;
                } else players[currentPlayerID].getTerrainCard().setHighlighted(false);

                for (LocationTile tile: players[currentPlayerID].getLocationTiles()) {
                    if (tile.contains(x, y) && !tile.isDarkened) { tile.setHighlighted(true); hasOneSelected = true; }
                    else tile.setHighlighted(false);
                }
                if (hasOneSelected) advanceState = true;
                if (advanceState) {
                    for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                        for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                            board.board[i][j].setHighlighted(false);
                        }
                    }
                    if (players[currentPlayerID].getTerrainCard().isHighlighted) { isMovable = false; darkenHexagons(players[currentPlayerID].getTerrainCard().getID()); }
                    for (LocationTile tile: players[currentPlayerID].getLocationTiles()) {
                        if (tile.isHighlighted) {
                            chosenLocationTile = tile;
                            switch (tile.getName()) {
                                case "Barn":
                                    isMovable = true;
                                    darkenNonSettlements();
                                    break;
                                case "Harbor":
                                    isMovable = true;
                                    darkenNonSettlements();
                                    break;
                                case "Paddock":
                                    isMovable = true;
                                    darkenNonSettlements();
                                    break;
                                case "Oracle":
                                    isMovable = false;
                                    darkenHexagons(players[currentPlayerID].getTerrainCard().getID());
                                    break;
                                case "Farm":
                                    isMovable = false;
                                    darkenHexagons(5);
                                    break;
                                case "Oasis":
                                    isMovable = false;
                                    darkenHexagons(1);
                                    break;
                                case "Tower":
                                    isMovable = false;
                                    darkenNonTowerHexagons();
                                    break;
                                case "Tavern":
                                    isMovable = false;
                                    darkenNonTavernHexagons();
                                    break;
                            }
                        }
                    }
                    return;
                }
                break;
            case cardOrLocationTileSelectionOrsettlementPlacement:
                hasOneSelected = false;
                if (players[currentPlayerID].getTerrainCard().contains(x, y) && !players[currentPlayerID].getTerrainCard().isDarkened)
                    hasOneSelected = true;
                for (LocationTile tile: players[currentPlayerID].getLocationTiles()) {
                    if (tile.contains(x, y) && !tile.isDarkened) hasOneSelected = true;
                } if (hasOneSelected) { state = GameState.cardOrLocationTileSelection; mousePressed(e); break; }
                hasOneSelected = false;
                System.out.println(isMovable);
                out:
                for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                    for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                        if (!isMovable && board.board[i][j].contains(x, y) && !board.board[i][j].isDarkened && board.board[i][j].getSettlement() == null) {
                            board.board[i][j].setHighlighted(true);
                            tempSettlement = new Settlement(currentPlayerID, board.board[i][j]);
                            selectedRow = i; selectedCol = j;
                            hasOneSelected = true;
                            break out;
                        }
                        if (isMovable && board.board[i][j].contains(x, y) && !board.board[i][j].isDarkened) {
                            board.board[i][j].setHighlighted(true);
                            // board.board[i][j].isDarkened = true;
                            tempSettlement = board.board[i][j].getSettlement();
                            selectedRow = i; selectedCol = j;
                            hasOneSelected = true;
                            break out;
                        }
                    }
                } if (hasOneSelected) { state = state.nextState(); mousePressed(e); break; }
                break;
            case settlementPlacement:
                for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                    for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                        board.board[i][j].setHighlighted(false);
                    }
                }
                hasOneSelected = false;
                out:
                for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                    for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                        if (!isMovable && board.board[i][j].contains(x, y) && !board.board[i][j].isDarkened) {
                            board.board[i][j].setHighlighted(true);
                            tempSettlement = new Settlement(currentPlayerID, board.board[i][j]);
                            selectedRow = i; selectedCol = j;
                            hasOneSelected = true;
                            break out;
                        }

                        if (isMovable && board.board[i][j].contains(x, y) && !board.board[i][j].isDarkened) {
                            System.out.println("Fikky");
                            tempSettlement = board.board[i][j].getSettlement();
                            if (tempSettlement != null) board.board[i][j].setHighlighted(true);
                            board.board[i][j].removeSettlement(board);
                            selectedRow = i; selectedCol = j;
                            hasOneSelected = true;
                            break out;
                        }
                    }
                }
                if (!hasOneSelected) tempSettlement = null;
                if (tempSettlement != null) {
                    diffX = x - tempSettlement.getTopLeftX(); diffY = y - tempSettlement.getTopLeftY();
                    if (chosenLocationTile != null) {
                        switch (chosenLocationTile.getName()) {
                            case "Barn":
                                darkenHexagons(players[currentPlayerID].getTerrainCard().getID());
                                board.board[selectedRow][selectedCol].setDarkened(false);
                                break;
                            case "Harbor":
                                darkenHexagons(6);
                                board.board[selectedRow][selectedCol].setDarkened(false);
                                break;
                            case "Paddock":
                                darkenNonPaddock();
                                board.board[selectedRow][selectedCol].setDarkened(false);
                                break;
                        }
                    }
                }
                break;
        } repaint();
    }

    public void undarkenHexagons() {
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                board.board[i][j].setDarkened(false);
            }
        }
    }
    public void darkenHexagons(int type) {
        ArrayList<int []> positions = new ArrayList<> ();
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                boolean hasAdj = false;
                int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                for (int k = 0; k < 6; k++) {
                    int r = i + dx[k], c = j + dy[k];
                    if (Gameboard.isValid(r, c) && board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == currentPlayerID) {
                        hasAdj = true;
                    }
                }
                if (hasAdj && board.board[i][j].getSettlement() == null && board.board[i][j].getType() == type && (i != selectedRow || j != selectedCol)) positions.add(new int[] {i, j});
            }
        }

        if (positions.isEmpty()) {
            for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                    if (!isMovable) board.board[i][j].setDarkened(board.board[i][j].getType() != type || board.board[i][j].getSettlement() != null);
                    else board.board[i][j].setDarkened(board.board[i][j].getType() != type || board.board[i][j].getSettlement() != null);
                }
            }
        } else {
            for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                    board.board[i][j].setDarkened(true);
                }
            }
            for (int [] arr: positions) {
                board.board[arr[0]][arr[1]].setDarkened(false);
            }
        }
    }
    public void darkenNonTowerHexagons() {
        ArrayList<int []> positions = new ArrayList<> ();
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                boolean hasAdj = false;
                int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                for (int k = 0; k < 6; k++) {
                    int r = i + dx[k], c = j + dy[k];
                    if (Gameboard.isValid(r, c) && board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == currentPlayerID) {
                        hasAdj = true;
                    }
                }
                boolean isOnBorder = i == 0 || j == 0 || i == Gameboard.LARGE_SIZE - 1 || j == Gameboard.LARGE_SIZE - 1;
                if (hasAdj && board.board[i][j].getSettlement() == null && isOnBorder) positions.add(new int[] {i, j});
            }
        }

        if (positions.isEmpty()) {
            for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                    boolean isOnBorder = i == 0 || j == 0 || i == Gameboard.LARGE_SIZE - 1 || j == Gameboard.LARGE_SIZE - 1;
                    board.board[i][j].setDarkened(!isOnBorder || board.board[i][j].getSettlement() != null);
                }
            }
        } else {
            for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                    board.board[i][j].setDarkened(true);
                }
            }
            for (int [] arr: positions) {
                board.board[arr[0]][arr[1]].setDarkened(false);
            }
        }
    }
    public void darkenNonTavernHexagons() {
        ArrayList<int []> positions = new ArrayList<> ();
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                boolean any = false;
                for (int k = 0; k < 6; k++) {
                    int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                    int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                    int r1 = i + dx[k], c1 = j + dy[k];
                    dx = r1 % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                    dy = r1 % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                    int r2 = r1 + dx[k], c2 = c1 + dy[k];
                    dx = r2 % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                    dy = r2 % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                    int r3 = r2 + dx[k], c3 = c2 + dy[k];
                    int [] rows = { r1, r2, r3 }, cols = { c1, c2, c3 };
                    boolean all = false;
                    if (Gameboard.isValid(r3, c3)) {
                        all = true;
                        for (int x = 0; x < 3; x++) {
                            int r = rows[x], c = cols[x];
                            if (!(board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == currentPlayerID))
                                all = false;
                        }
                    } if (all) any = true;
                }
                int type = board.board[i][j].getType();
                if (any && board.board[i][j].getSettlement() == null && type != 4 && type != 6) positions.add(new int[] {i, j});
            }
        }

        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                board.board[i][j].setDarkened(true);
            }
        }
        for (int [] arr: positions) {
            board.board[arr[0]][arr[1]].setDarkened(false);
        }
    }
    public void darkenNonSettlements() {
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                Settlement s = board.board[i][j].getSettlement();
                board.board[i][j].setDarkened(s == null || s.getOwnerID() != currentPlayerID);
            }
        }
    }
    public void darkenNonPaddock() {
        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                board.board[i][j].setDarkened(true);
            }
        }

        for (int k = 0; k < 6; k++) {
            int [] dx = selectedRow % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
            int [] dy = selectedRow % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
            int r1 = selectedRow + dx[k], c1 = selectedCol + dy[k];
            dx = r1 % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
            dy = r1 % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
            int r2 = r1 + dx[k], c2 = c1 + dy[k];
            if (Gameboard.isValid(r2, c2) && board.board[r2][c2].getSettlement() == null) {
                if (board.board[r2][c2].getType() != 4 && board.board[r2][c2].getType() != 6)
                    board.board[r2][c2].isDarkened = false;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        Player p = players[currentPlayerID];
        if (endTurnButton.contains(x, y) && endTurnButton.isBeingClicked()) {
        	discardPile.add(p.getTerrainCard());
        	if (deck.isEmpty()) {
        		deck = (ArrayList<TerrainCard>) discardPile.clone();
        		discardPile.clear();
        		Collections.shuffle(deck); 
        	}
        	p.setTerrainCard(deck.remove(0));
        	p.resetSettlementCounts(); p.resetUsed();
        	currentPlayerID++; currentPlayerID %= 4;
        	  
        	endTurnButton.unclick(); 
        	endTurnButton.setEnabled(false);
        }
        
        switch (state) {
            case settlementPlacement:
//	        	for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
//	                for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
//	                    board.board[i][j].setHighlighted(false);
//	                }
//	            }
            	
                if (tempSettlement != null && !isMovable) {
                    board.board[selectedRow][selectedCol].setSettlement(board, tempSettlement);
                    updateScore();
                    tempSettlement = null;
                    if (p.getTerrainCard().isHighlighted) {
                        p.useTerrainCard();
                        darkenHexagons(p.getTerrainCard().getID());
                    }
                    for (LocationTile tile: players[currentPlayerID].getLocationTiles())
                        if (tile.isHighlighted) {
                            tile.isHighlighted = false; tile.isDarkened = true;
                            p.useLocationTile(tile);
                            undarkenHexagons();
                            state = GameState.cardOrLocationTileSelection;
                        }
                    if (p.getMandatorySettlementsLeft() == 0 && p.getTerrainCard().isHighlighted) {
                    	if (!endTurnButton.isEnabled()) endTurnButton.setEnabled(true);
                        p.getTerrainCard().setHighlighted(false);
                        p.getTerrainCard().isDarkened = true;
                        state = GameState.cardOrLocationTileSelection;
                        for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                            for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                                board.board[i][j].setDarkened(false);
                            }
                        } undarkenHexagons();
                    }
                    break;
                } else if (tempSettlement != null && isMovable) {
                    //undarkenHexagons();
                    int centerX = tempSettlement.getTopLeftX() + 22, centerY = tempSettlement.getTopLeftY() + 24;
                    boolean hasMoved = false;
                    for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
                        for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
                            if (board.board[i][j].contains(centerX, centerY) && !board.board[i][j].isDarkened) {
                                if (i != selectedRow || j != selectedCol) {
                                    tempSettlement = new Settlement(currentPlayerID, board.board[i][j]);
                                    board.board[i][j].setSettlement(board, tempSettlement);
                                    updateScore();
                                    board.board[i][j].setHighlighted(true);
                                    state = GameState.cardOrLocationTileSelection;
                                    hasMoved = true;
                                }
                            }
                        }
                    } isBeingDragged = false;
                    if (!hasMoved) {
                        System.out.println("Has Not Moved");
                        tempSettlement = new Settlement(currentPlayerID, board.board[selectedRow][selectedCol]);
                        board.board[selectedRow][selectedCol].setSettlement(board, tempSettlement);
                        state = GameState.cardOrLocationTileSelectionOrsettlementPlacement;
                    } else {
                        for (LocationTile tile: players[currentPlayerID].getLocationTiles())
                            if (tile.isHighlighted) {
                                tile.isHighlighted = false; tile.isDarkened = true;
                                state = GameState.cardOrLocationTileSelection;
                            }
                        board.board[selectedRow][selectedCol].isHighlighted = false;
                        undarkenHexagons();
                    }
                    break;
                }
        }
        if (advanceState) {
            state = state.nextState();
            advanceState = false;
        } repaint();
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        endTurnButton.setHovering(endTurnButton.contains(x, y));
        repaint();
    }

    private boolean isBeingDragged = false;
    public void mouseDragged(MouseEvent e) {
        if (state == GameState.settlementPlacement && isMovable) {
            isBeingDragged = true;
            int x = e.getX(), y = e.getY();
            int topLeftX = x - diffX, topLeftY = y - diffY;
            if (tempSettlement != null) tempSettlement.setCoords(topLeftX, topLeftY);
        }
        repaint();
    }
}
