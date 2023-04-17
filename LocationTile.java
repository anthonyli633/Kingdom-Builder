import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;


public class LocationTile extends Hexagon {
	static String [] names = "Barn Farm Harbor Oasis Oracle Paddock Tavern Tower".split(" ");
	static BufferedImage [] images = new BufferedImage[names.length];
	private int id; 
	
	public LocationTile(int row, int col, int id) {
		super(row, col, 7);
		this.id = id;
	}
	
	public BufferedImage getImage() { return images[id]; }
	public void display(Graphics g, int x, int y) {
		g.drawImage(getImage(), x, y, (int) Math.round(SIDE_LENGTH * Math.sqrt(3)), (int) (2 * SIDE_LENGTH), null);
	}
}
