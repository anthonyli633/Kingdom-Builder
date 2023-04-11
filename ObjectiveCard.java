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
	static int WIDTH = 162, HEIGHT = 231;
	private int id;
	
	public ObjectiveCard(int id) {
		this.id = id;
	}
	
	public void display(Graphics g, int x, int y) {
		BufferedImage img = null;
		try { img = ImageIO.read(this.getClass().getResource("/Images/" + names[id] + "Objective.png")); }
		catch (IOException e) { e.printStackTrace(); }
		g.drawImage(img, x, y, WIDTH, HEIGHT, null);
	}
	
	public void score() {
		
	}
	
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
