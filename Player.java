import java.util.*;
import java.io.*;
import java.awt.*;

public class Player {
	private int score;
	private int mandatorySettlementsLeft, turnSettlementsLeft, totalSettlementsLeft;
	private int id;
	private TerrainCard terrainCard;
	private ArrayList<ActionTile> actionTiles;
	private ArrayList<int []> settlementLocations;
	private boolean firstPlayer;
	
	public Player(int id) { totalSettlementsLeft = 40; setID(id); }
	
	public int getScore() { return score; }
	public int getID() { return id; }
	public int getMandatorySettlementsLeft() { return mandatorySettlementsLeft; }
	public int getTurnSettlementsLeft() { return turnSettlementsLeft; }
	public int getTotalSettlementsLeft() { return totalSettlementsLeft; }
	public TerrainCard getTerrainCard() { return terrainCard; }
	public ArrayList<ActionTile> getActionTiles() { return actionTiles; }
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
		int width = (KingdomBuilderPanel.WIDTH - 1150) / 2, height = 375;
		int x = 1150 + col * width, y = 210 + row * height;
		g.drawRect(x, y, width, height);
		
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		// Settlements Left
		KingdomBuilderPanel.drawCenteredString(g, "Settlements Left: " + totalSettlementsLeft, f, x, x + width, y + height - 20);
		
		// Player text
		KingdomBuilderPanel.drawCenteredString(g, toString(), f, x, x + width, y + 40);
		x = x + width / 2 - TerrainCard.WIDTH / 2; y += 50;
		g.drawImage(TerrainCard.CARD_BACK, x, y, TerrainCard.WIDTH, TerrainCard.HEIGHT, null);
		
		
	}
	public String toString() { return "Player " + (id + 1) + ": " + score + " Gold"; }
}
