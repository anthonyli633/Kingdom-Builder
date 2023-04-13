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
	
	private int id;
	
	public TerrainCard(int id) {
		this.id = id;
	}
	
	public int getID() { return id; }
	
	public void display(Graphics g, int x, int y) {
		if (id > 0) g.drawImage(cardImages[id], x, y, WIDTH, HEIGHT, null);
		else g.drawImage(CARD_BACK, x, y, WIDTH, HEIGHT, null);
	}
}
