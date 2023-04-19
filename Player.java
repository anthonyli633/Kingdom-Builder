import java.util.*;
import java.io.*;
import java.awt.*;

public class Player {
	private int score;
	private int mandatorySettlementsLeft, turnSettlementsLeft, totalSettlementsLeft;
	private int id;
	private TerrainCard terrainCard;
	private ArrayList<LocationTile> locationTiles = new ArrayList<> ();
	private ArrayList<int []> settlementLocations = new ArrayList<> ();
	private boolean firstPlayer;
	
	public Player(int id) { 
		totalSettlementsLeft = 40; setID(id); 
		for (int i = 0; i < 6; i++) {
			locationTiles.add(new LocationTile(0, 0, (int) (Math.random() * LocationTile.names.length)));
		}
	}
	
	public int getScore() { return score; }
	public int getID() { return id; }
	public int getMandatorySettlementsLeft() { return mandatorySettlementsLeft; }
	public int getTurnSettlementsLeft() { return turnSettlementsLeft; }
	public int getTotalSettlementsLeft() { return totalSettlementsLeft; }
	public TerrainCard getTerrainCard() { return terrainCard; }
	public ArrayList<LocationTile> getlocationTiles() { return locationTiles; }
	public ArrayList<int []> getSettlementLocations() { return settlementLocations; }
	public boolean isFirstPlayer() { return firstPlayer; }
	
	public void setTerrainCard(TerrainCard terrainCard) { this.terrainCard = terrainCard; }
	public void setFirstPLayer(boolean firstPlayer) { this.firstPlayer = firstPlayer; }
	public void addScore(int amount) { score += amount; }
	public void setScore(int score) { this.score = score; }
	public void setID(int id) { this.id = id; }
	
	public void display(Graphics g) {
		// Player Box
		Color c = Color.BLACK;
		int row = id / 2, col = id % 2;
		int width = 225, height = Gameboard.SMALL_HEIGHT + 20;
		int x = KingdomBuilderPanel.GAMEBOARD_MARGIN_X - width + col * (width + Gameboard.LARGE_WIDTH + 35), y = KingdomBuilderPanel.GAMEBOARD_MARGIN_Y - 28 + row * height;
		g.drawRect(x, y, width, height);
		
		for (int i = 0; i < locationTiles.size(); i++) {
			int row1 = i / 4, col1 = i % 4;
			int x1 = x + col1 * 50 + 15;
			int y1 = 170 + row * height + TerrainCard.HEIGHT + row1 * 53 + 10;
			locationTiles.get(i).display(g, x1, y1);
		}
		
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		// Settlements Left
		KingdomBuilderPanel.drawCenteredString(g, "Settlements Left: " + totalSettlementsLeft, f, x, x + width, y + height - 20);
		
		// Player text
		KingdomBuilderPanel.drawCenteredString(g, toString(), f, x, x + width, y + 40);
		// Card
		x = x + width / 2 - TerrainCard.WIDTH * 3 / 5; y += 50;
		g.drawImage(TerrainCard.CARD_BACK, x, y, TerrainCard.WIDTH * 6 / 5, TerrainCard.HEIGHT * 6 / 5, null);
	}
	public String toString() { return "Player " + (id + 1) + ": " + score + " Gold"; }
}
