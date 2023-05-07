import java.util.*;

import javax.imageio.ImageIO;

import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class Player {
    private int score;
    private int mandatorySettlementsLeft, turnSettlementsLeft, totalSettlementsLeft;
    private int id;
    private TerrainCard terrainCard;
    private ArrayList<LocationTile> locationTiles = new ArrayList<> (), totalLocationTiles = new ArrayList<> ();
    private ArrayList<int []> settlementLocations = new ArrayList<> ();
    private boolean firstPlayer;
    
    static Color [] colors = new Color[] {new Color(0, 151, 229), new Color(1, 187, 0), new Color(238, 136, 0), new Color(120, 0, 220)};
    static BufferedImage playerBox;
    static BufferedImage firstPlayerIcon;

    public Player(int id) {
        totalSettlementsLeft = 40; setID(id);
        resetSettlementCounts();
        
        try {
            playerBox = ImageIO.read(this.getClass().getResource("/Images/Light Wood Box.png"));
            firstPlayerIcon = ImageIO.read(this.getClass().getResource("/Images/FirstPlayerIcon.png"));
        } catch (Exception e) { e.printStackTrace(); }

//        int row = id / 2, col = id % 2;
//        int width = 225, height = Gameboard.SMALL_HEIGHT + 20;
//        int x = KingdomBuilderPanel.GAMEBOARD_MARGIN_X - width + col * (width + Gameboard.LARGE_WIDTH + 35), y = KingdomBuilderPanel.GAMEBOARD_MARGIN_Y - 28 + row * height;
//        for (int i = 0; i < 6; i++) {
//            int row1 = i / 4, col1 = i % 4;
//            int x1 = x + col1 * 50 + 15;
//            int y1 = 170 + row * height + TerrainCard.HEIGHT + row1 * 53 + 10;
//            int [] vals = new int[] {0, 1, 2, 3, 4, 5, 6, 7}; 
//            locationTiles.add(new LocationTile(0, 0, vals[(int) (Math.random() * vals.length)]));
//            locationTiles.get(i).setCoords((int) (x1 + Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2)), (int) Math.round(y1 + Hexagon.SIDE_LENGTH));
//        }
    }

    public int getScore() { return score; }
    public int getID() { return id; }
    public int getMandatorySettlementsLeft() { return mandatorySettlementsLeft = Math.min(mandatorySettlementsLeft, totalSettlementsLeft); }
    public int getTurnSettlementsLeft() { return turnSettlementsLeft = Math.min(turnSettlementsLeft, totalSettlementsLeft); }
    public int getTotalSettlementsLeft() { return totalSettlementsLeft; }
    public TerrainCard getTerrainCard() { return terrainCard; }
    public ArrayList<LocationTile> getLocationTiles() { return locationTiles; }
    public ArrayList<LocationTile> getTotalLocationTiles() { return totalLocationTiles; }
    public ArrayList<int []> getSettlementLocations() { return settlementLocations; }
    public boolean isFirstPlayer() { return firstPlayer; }

    public void addLocationTile(LocationTile tile) {
        int row = id / 2, col = id % 2;
        int width = 225, height = Gameboard.SMALL_HEIGHT + 20;
        int x = KingdomBuilderPanel.GAMEBOARD_MARGIN_X - width + col * (width + Gameboard.LARGE_WIDTH + 35), y = KingdomBuilderPanel.GAMEBOARD_MARGIN_Y - 28 + row * height;
        int i = locationTiles.size();
        int row1 = i / 4, col1 = i % 4;
        int x1 = x + col1 * 50 + 15;
        int y1 = 170 + row * height + TerrainCard.HEIGHT + row1 * 53 + 10;
        int [] vals = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
        tile.setCoords((int) (x1 + Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2)), (int) Math.round(y1 + Hexagon.SIDE_LENGTH));
        tile.setDarkened(true);
        if (!locationTiles.contains(tile)) locationTiles.add(tile);
        totalLocationTiles.add(tile);
    }

    public void resetUsed() {
        terrainCard.isDarkened = false;
        for (int i = 0; i < locationTiles.size(); i++) locationTiles.get(i).setDarkened(false);
    }
    public void resetSettlementCounts() {
        mandatorySettlementsLeft = Math.min(3, totalSettlementsLeft);
        turnSettlementsLeft = mandatorySettlementsLeft;
        for (LocationTile tile: getLocationTiles()) {
            if (!tile.isDarkened) turnSettlementsLeft++;
        } turnSettlementsLeft = Math.min(turnSettlementsLeft, totalSettlementsLeft);
    }
    public void useTerrainCard() {
        turnSettlementsLeft --;
        totalSettlementsLeft --;
        if (--mandatorySettlementsLeft == 0) terrainCard.isDarkened = true;
    }
    public void useLocationTile(LocationTile t) {
        turnSettlementsLeft --;
        totalSettlementsLeft --;
        t.setDarkened(true);
    }

    public void setTerrainCard(TerrainCard terrainCard) { this.terrainCard = terrainCard; }
    public void setFirstPLayer(boolean firstPlayer) { this.firstPlayer = firstPlayer; }
    public void addScore(int amount) { score += amount; }
    public void setScore(int score) { this.score = score; }
    public void setID(int id) { this.id = id; }

    public void display(Graphics g) {
        // Player Box
        int row = id / 2, col = id % 2;
        int width = 225, height = Gameboard.SMALL_HEIGHT + 20;
        int x = KingdomBuilderPanel.GAMEBOARD_MARGIN_X - width + col * (width + Gameboard.LARGE_WIDTH + 35), y = KingdomBuilderPanel.GAMEBOARD_MARGIN_Y - 28 + row * height;
        g.drawImage(playerBox, x, y, width, height, null);
        g.setColor(new Color(242, 235, 205, 200));
        g.fillRect(x, y, width, height);
        Color c = (id == KingdomBuilderPanel.currentPlayerID ? colors[id] : new Color(119, 47, 47));
        g.setColor(c);
        g.drawRect(x, y, width, height);
        g.setColor(Color.BLACK);

        for (int i = 0; i < locationTiles.size(); i++) {
            locationTiles.get(i).display(g);
        }

        // First Player Icon
        if(isFirstPlayer()) {
            g.drawImage(firstPlayerIcon, x+185, y+11, (int) Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) * .75), (int) Math.round(2 * Hexagon.SIDE_LENGTH * .75), null);
        }

        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        // Settlements Left
        KingdomBuilderPanel.drawCenteredString(g, "Settlements Left: " + totalSettlementsLeft, f, x, x + width, y + height - 20);

        // Player text
        KingdomBuilderPanel.drawCenteredString(g, toString(), f, x, x + width, y + 40);
        // Card
        x = x + width / 2 - TerrainCard.WIDTH * 3 / 5; y += 50;
        terrainCard.setCoords(x, y);
        terrainCard.setDimensions(TerrainCard.WIDTH * 6 / 5, TerrainCard.HEIGHT * 6 / 5);
        if (id == KingdomBuilderPanel.currentPlayerID) terrainCard.displayFront(g);
        else terrainCard.displayBack(g);
    }
    public String toString() {
        if(isFirstPlayer()) return "Player " + (id + 1) + ": " + score + " Gold    ";
        else return "Player " + (id + 1) + ": " + score + " Gold"; }
}
