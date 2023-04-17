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
	
	private boolean show;
	private int width, height;
	
	private int id;
	
	public TerrainCard(int id) {
		this.id = id;
		width = WIDTH; height = HEIGHT;
	}
	
	public int getID() { return id; }
	public void setDimensions(int width, int height) { this.width = width; this.height = height; }
	
	public void display(Graphics g, int x, int y) {
		if (id > 0 && show) g.drawImage(cardImages[id], x, y, width, height, null);
		else g.drawImage(CARD_BACK, x, y, width, height, null);
	}
}
