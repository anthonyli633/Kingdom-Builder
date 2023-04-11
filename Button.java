import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Button {
	private BufferedImage img1, img2, img3;
	private boolean enabled, hovering;
	private int x, y, width, height;
	
	public Button(BufferedImage img1, BufferedImage img2, BufferedImage img3) {
		this.img1 = img1;
		this.img2 = img2;
		this.img3 = img3;
		enabled = true; hovering = false;
	}

	public void setEnabled(boolean b) { enabled = b; }
	public boolean isEnabled() { return enabled; }
	public void setHovering(boolean b) { hovering = b; }
	public boolean isHovering() { return hovering; }

	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public void setCoords(int x, int y) { this.x = x; this.y = y; }
	public void setCenterCoords(int x, int y) {
		int topY = y - height / 2;
		int leftX = x - width / 2;
		this.x = leftX; this.y = topY;
	}
	public int getX() { return x; }
	public int getY() { return y; }

	public boolean isClicked(int x1, int y1) {
		return enabled && inBounds(x1, y1);
	}
	public boolean inBounds(int x1, int y1) {
		return (x1 >= x && y1 >= y && x1 <= x + width && y1 <= y + height);
	}

	public void display(Graphics g) {
		if (!enabled) g.drawImage(img2, x, y, width, height, null);
		else if (hovering) display2(g); 
		else g.drawImage(img1, x, y, width, height, null);
	}
	public void display2(Graphics g) {
		g.drawImage(img3, x, y, width, height, null);
	}
}