import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class ObjectiveCard {
	/* 0 - Citizens
	 * 1 - Discoverers
	 * 2 - Farmers
	 * 3 - Fishermen
	 * 4 - Hermits
	 * 5 - Knights
	 * 6 - Lords
	 * 7 - Merchants
	 * 8 - Miners
	 * 9 - Workers
	 */
	
	static String [] names = "Citizens Discoverers Farmers Fishermen Hermits Knights Lords Merchants Miners Workers".split(" ");
	static int WIDTH = 108, HEIGHT = 154;
	
	private int width, height;
	private int id;
	private int topLeftX, topLeftY;
	
	public ObjectiveCard(int id) {
		width = WIDTH; height = HEIGHT;
		this.id = id;
	}
	public ObjectiveCard(int id, int x, int y) {
		width = WIDTH; height = HEIGHT;
		this.id = id;
		setCoords(x, y);
	}
	
	public void display(Graphics g) {
		BufferedImage img = null;
		try { img = ImageIO.read(this.getClass().getResource("/Images/" + names[id] + "Objective.png")); }
		catch (IOException e) { e.printStackTrace(); }
		g.drawImage(img, topLeftX, topLeftY, width, height, null);
	}
	
	public void expandImage(Graphics g){
		BufferedImage img = null;
		try { img = ImageIO.read(this.getClass().getResource("/Images/" + names[id] + "Objective.png")); }
		catch (IOException e) { e.printStackTrace(); }
		g.drawImage(img, topLeftX+100, topLeftY, width*10, height*10, null);
	
	public void score() {
		
	}
	
	public int getID() { return id; }
	
	public void setCoords(int x, int y) { topLeftX = x; topLeftY = y; }
	public void enlarge() { width *= 2; height *= 2; }
	public void reset() { width = WIDTH; height = HEIGHT; }
	
	public boolean isEnlarged() { return width > WIDTH; }
	public boolean contains(int x, int y) { return x >= topLeftX && y >= topLeftY && x <= topLeftX + width && y <= topLeftY + height; }
	
	public String toString() { return names[id]; }
	
	public static int scoreFisherman(Gameboard board, Player p) { return 0; }
	public static int scoreMerchant(Gameboard board, Player p) { return 0; }
	public static int scoreDiscoverer(Gameboard board, Player p) { return 0; }
	public static int scoreHermit(Gameboard board, Player p) { return 0; }
	public static int scoreCitizen(Gameboard board, Player p) { return 0; }
	public static int scoreMiner(Gameboard board, Player p) { return 0; }
	public static int scoreWorker(Gameboard board, Player p) { return 0; }
	public static int scoreKnight(Gameboard board, Player p) { return 0; }
	public static int scoreLord(Gameboard board, Player p) { return 0; }
	public static int scoreFarmer(Gameboard board, Player p) { return 0; }
	
	public static int scoreFisherman(Gameboard board, int row, int col) { return 0; }
	public static int scoreMerchant(Gameboard board, int row, int col) { return 0; }
	public static int scoreDiscoverer(Gameboard board, int row, int col) { return 0; }
	public static int scoreHermit(Gameboard board, int row, int col) { return 0; }
	public static int scoreCitizen(Gameboard board, int row, int col) { return 0; }
	public static int scoreMiner(Gameboard board, int row, int col) { return 0; }
	public static int scoreWorker(Gameboard board, int row, int col) { return 0; }
	public static int scoreKnight(Gameboard board, int row, int col) { return 0; }
	public static int scoreLord(Gameboard board, int row, int col) { return 0; }
	public static int scoreFarmer(Gameboard board, int row, int col) { return 0; }
}
