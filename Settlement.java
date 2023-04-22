import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;

public class Settlement {
    static String [] colors = "Blue Green Orange Purple Red Yellow".split(" ");
    static BufferedImage[] images;
    private int ownerID;
    private Hexagon hexagon;

    public Settlement(int ownerID, Hexagon hexagon){
        this.ownerID = ownerID;
        this.hexagon = hexagon;
    }
    public int getOwnerID() { return ownerID; }

    public void display(Graphics g) {
        int x = hexagon.getCenterCoords().x - (int) Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2);
        int y = hexagon.getCenterCoords().y - (int) Math.round(Hexagon.SIDE_LENGTH);
        int size = 30;
        g.drawImage(images[ownerID], x + 7, y + 9, size, size, null);
    }
}
