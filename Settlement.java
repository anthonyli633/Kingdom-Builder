import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;

public class Settlement {
    static String [] colors = "Blue Green Orange Purple Red Yellow".split(" ");
    static BufferedImage[] images;
    private int ownerID;
    private Hexagon hexagon;
    private int topLeftX, topLeftY;

    public Settlement(int ownerID, Hexagon hexagon){
        this.ownerID = ownerID; 
        this.hexagon = hexagon;
        topLeftX = hexagon.getCenterCoords().x - (int) Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2);
        topLeftY = hexagon.getCenterCoords().y - (int) Math.round(Hexagon.SIDE_LENGTH);
    }
    public int getOwnerID() { return ownerID; }
    public int getTopLeftX() { return topLeftX; } 
    public int getTopLeftY() { return topLeftY; }
    public void setCoords(int x, int y) {
    	topLeftX = x; topLeftY = y;
    }
    public Hexagon getHexagon() { return hexagon; }
    
    public void display(Graphics g) {
        int size = 30;
        g.drawImage(images[ownerID], topLeftX + 7, topLeftY + 9, size, size, null);
    }
} 
