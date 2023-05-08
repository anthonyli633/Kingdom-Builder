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
    public int getType() { return id; }
    public String getName() { return names[id]; }
    public void display(Graphics g) {
        Point center = getCenterCoords();
        g.drawImage(getImage(), (int) (Math.round(center.x - Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2)), (int) Math.round(center.y - Hexagon.SIDE_LENGTH), (int) Math.round(SIDE_LENGTH * Math.sqrt(3)), (int) Math.round(2 * SIDE_LENGTH), null);
        g.setColor(Color.YELLOW);
        if (isDarkened) {
            g.setColor(KingdomBuilderPanel.SHADE);
            g.fillPolygon(getPolygon());
        }
        if (isHighlighted) g.drawPolygon(super.getPolygon());
        g.setColor(Color.BLACK);
    }
    public boolean equals(Object o) {
    	if (o.getClass() != this.getClass()) return false;
    	LocationTile t = (LocationTile) o;
    	return getRow() == t.getRow() && getCol() == t.getCol() && id == t.getType();
    }
    public String toString() {
    	return getRow() + " " + getCol() + " " + id + " " + isHighlighted;
    }
}
