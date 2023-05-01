import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Hexagon {
    static double SIDE_LENGTH; /* Actual side length of the hexagon */
    static int HIGHLIGHT_MARGIN = 5; /* the margin (pixels) that is highlighted within the hexagon */
    static BufferedImage castle;

    private int row, col; /* The row and col of the hexagon in the game board */
    private int centerX, centerY; /* the coordinates of the center of the hexagon */
    public boolean isHighlighted; /* whether or not the hexagon is highlighted */
    public boolean isDarkened;
    private Polygon hexagon; /* A Polygon-Object representing the hexagon */
    private int type; /* what type of hexagon it is */
    private Settlement settlement;
    public LocationTile locationTile;
    private int numLocationTiles;

    /* Key:
     * File:
     * 0 - Desert
     * 1 - Water
     * 2 - Forest
     * 3 - Plains
     * 4 - Canyon
     * 5 - Flowers
     * 6 - Mountains
     * 7 - Location Tile
     * 8 - Castle
     * Actual:
     * 0 - Canyon
     * 1 - Desert
     * 2 - Flowers
     * 3 - Forest
     * 4 - Mountains
     * 5 - Plains
     * 6 - Water
     * 7 - Location Tile
     * 8 - Castle
     */
    static int [] conversion = new int[] {1, 6, 3, 5, 0, 2, 4, 7, 8};

    /* Initializes a hexagon with the given center coordinates */
    public Hexagon(int row, int col, int type) {
        setLocation(row, col);
        Point p = Gameboard.getCoords(row, col);
        setCoords(p.x, p.y);
        this.type = type;
        if (type == 7) numLocationTiles = 2;
        initPolygon();
    }

    /* Sets the row and col of the hexagon */
    public void setLocation(int r, int c) {
        row = r; col = c;
    }
    /* Sets the center coordinates of the hexagon */
    public void setCoords(int x, int y) {
        centerX = x; centerY = y;
        initPolygon();
    }
    /* Sets the specific side length of this hexagon */
    public void setSideLength(int sideLength) {
        SIDE_LENGTH = sideLength;
    }
    /* Sets whether or not the hexagon is highlighted */
    public void setHighlighted(boolean bool) {
        isHighlighted = bool;
    }
    public void setDarkened(boolean bool) {
        isDarkened = bool;
    }
    /* Gets the row of the hexagon */
    public int getRow() {
        return row;
    }
    /* Gets the col of the hexagon */
    public int getCol() {
        return col;
    }
    /* Gets the center coords of the hexagon */
    public Point getCenterCoords() {
        return new Point(centerX, centerY);
    }
    /* Gets the coordinates of the hexagon */
    public Point [] getCoords() {
        Point [] coords = new Point[6];
        double theta = Math.PI / 6;
        for (int i = 0; i < 6; i++) {
            coords[i] = new Point((int) Math.round(SIDE_LENGTH * Math.cos(theta) + centerX), (int) Math.round(SIDE_LENGTH * Math.sin(theta) + centerY));
            theta += Math.PI / 3;
        } return coords;
    }
    public static Point [] getCoords(int x, int y) {
        Point [] coords = new Point[6];
        double theta = Math.PI / 6;
        for (int i = 0; i < 6; i++) {
            coords[i] = new Point((int) Math.round(SIDE_LENGTH * Math.cos(theta) + x), (int) Math.round(SIDE_LENGTH * Math.sin(theta) + y));
            theta += Math.PI / 3;
        } return coords;
    }
    public int getType() {
        return type;
    }
    /* Initializes the polygon */
    public void initPolygon() {
        hexagon = new Polygon();
        for (Point p: getCoords()) hexagon.addPoint(p.x, p.y);
    }
    /* Returns a polygon representing the hexagon */
    public Polygon getPolygon() { 
        return hexagon;
    }
    /* Returns whether this hexagon contains the point (x, y) */
    public boolean contains(int x, int y) {
        return getPolygon().contains(x, y);
    }
    /* Returns whether this hexagon is on the edge of the game board */
    public boolean isOnBorder() {
        return row == 0 || row == Gameboard.LARGE_SIZE - 1 || col == 0 || col == Gameboard.LARGE_SIZE - 1;
    }
    public Settlement getSettlement() {
        return settlement;
    }
    public void setSettlement(Gameboard board, Settlement settlement) {
        Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
        this.settlement = settlement;
        if (settlement == null) return;
        int [] dx = row % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
        int [] dy = row % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
        for (int i = 0; i < 6; i++) {
            int r = row + dx[i], c = col + dy[i];
            if (Gameboard.isValid(r, c) && board.board[r][c].getNumLocationTiles() > 0) {
                boolean hasObtainedBefore = false;
                for (LocationTile other: p.getLocationTiles())
                    if (other.getRow() == r && other.getCol() == c) hasObtainedBefore = true;
                if (!hasObtainedBefore) {
                    board.board[r][c].useLocationTile();
                    int type = board.board[r][c].locationTile.getType();
                    p.addLocationTile(new LocationTile(r, c, type));
                }
            }
        }
    }
    public void removeSettlement(Gameboard board) {
        Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
        settlement = null;
        int [] dx = row % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
        int [] dy = row % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
        for (int i = 0; i < 6; i++) {
            int r = row + dx[i], c = col + dy[i];
            if (Gameboard.isValid(r, c) && board.board[r][c].getNumLocationTiles() > 0) {
                boolean hasAdjacentSettlements = false;
                dx = r % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
                dy = r % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
                for (int j = 0; j < 6; j++) {
                    int r1 = r + dx[j], c1 = c + dy[j];
                    if (board.board[r1][c1].getSettlement() != null && board.board[r1][c1].getSettlement().getOwnerID() == KingdomBuilderPanel.currentPlayerID)
                        hasAdjacentSettlements = true;
                }
                System.out.println(hasAdjacentSettlements);
                System.out.println(p.getLocationTiles() + " " + board.board[r][c].locationTile);
                if (p.getLocationTiles().contains(board.board[r][c].locationTile) && !hasAdjacentSettlements)
                    p.getLocationTiles().remove(board.board[r][c].locationTile);
            }
        }
    }
    public int getNumLocationTiles() {
        return numLocationTiles;
    }
    public void useLocationTile() {
        numLocationTiles --;
    }

    /* Displays the hexagon (highlighted portion) */
    public void display(Graphics g) {
        if (locationTile != null) {
            locationTile.display(g);
        }
        if (type == 8) {
            int topLeftX = (int) Math.round(centerX - SIDE_LENGTH * Math.sqrt(3) / 2), topLeftY = (int) Math.round(centerY - SIDE_LENGTH);
            g.drawImage(castle, topLeftX, topLeftY - 1, (int) Math.round(SIDE_LENGTH * Math.sqrt(3)), (int) Math.round(2 * SIDE_LENGTH), null);
        }

        if (isDarkened) {
            g.setColor(KingdomBuilderPanel.SHADE);
            g.fillPolygon(getPolygon());
        }
        if (isHighlighted) {
            g.setColor(Color.YELLOW);
            g.drawPolygon(getPolygon());
        }
        if (settlement != null) settlement.display(g);
    }
}
