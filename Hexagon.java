import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class TerrainCard {
	static int WIDTH = 120, HEIGHT = 180;
	static BufferedImage CARD_BACK;
	static BufferedImage [] cardImages;
	static String [] names = "Canyons Deserts Flowers Forests Mountains Plains Water".split(" ");
	
	public boolean isShown, isHighlighted;
	private int x, y;
	private int width, height;
	boolean isDarkened;
	private int id; 
	
	public TerrainCard(int id) {
		this.id = id;
		width = WIDTH; height = HEIGHT;
	}
	
	public void setHighlighted(boolean val) { isHighlighted = val; }
	public int getID() { return id; }
	public void setDimensions(int width, int height) { this.width = width; this.height = height; }
	public void setCoords(int x, int y) { this.x = x; this.y = y; }
	public boolean contains(int x, int y) {
		return new Rectangle(this.x, this.y, width, height).contains(x, y);
	}
	
	public void displayFront(Graphics g) {
		g.setColor(Color.YELLOW);
		if (isHighlighted) g.fillRoundRect(x - 7, y - 7, width + 14, height + 14, 30, 30);
		g.drawImage(cardImages[id], x, y, width, height, null);
		if (isDarkened) {
			g.setColor(KingdomBuilderPanel.SHADE);
			g.fillRoundRect(x, y, width, height, 30, 30);
		}
	}
	public void displayBack(Graphics g) {
		g.drawImage(CARD_BACK, x, y, width, height, null);
	}
}
