import java.awt.*;

public class Hexagon {
	static double SIDE_LENGTH; /* Actual side length of the hexagon */
	static int HIGHLIGHT_MARGIN = 5; /* the margin (pixels) that is highlighted within the hexagon */
	private int row, col; /* The row and col of the hexagon in the game board */
	private int centerX, centerY; /* the coordinates of the center of the hexagon */
	public boolean isHighlighted; /* whether or not the hexagon is highlighted */
	private Polygon hexagon; /* A Polygon-Object representing the hexagon */
	private int type; /* what type of hexagon it is */
	private Settlement settlement;
	
	/* Key:
	 * 0 - Desert
	 * 1 - Water
	 * 2 - Forest
	 * 3 - Plains
	 * 4 - Canyon
	 * 5 - Flowers
	 * 6 - Mountains
	 * 7 - Action Tile
	 * 8 - Castle
	 */
	
	/* Initializes a hexagon with the given center coordinates */
	public Hexagon(int row, int col, int type) {
		setLocation(row, col);
		Point p = Gameboard.getCoords(row, col);
		setCoords(p.x, p.y);
		this.type = type;
		initPolygon();
	}
	
	/* Sets the row and col of the hexagon */
	public void setLocation(int r, int c) {
		row = r; col = c;
	}
	/* Sets the center coordinates of the hexagon */
	public void setCoords(int x, int y) {
		centerX = x; centerY = y;
	}
	/* Sets the specific side length of this hexagon */
	public void setSideLength(int sideLength) {
		SIDE_LENGTH = sideLength;
	}
	/* Sets whether or not the hexagon is highlighted */
	public void setHighlighted(boolean bool) {
		isHighlighted = bool;
	}
	/* Gets the row of the hexagon */
	public int getRow() {
		return row;
	}
	/* Gets the col of the hexagon */
	public int getCol() {
		return col;
	}
	/* Gets the center coords of the hexagon */
	public Point getCenterCoords() {
		return new Point(centerX, centerY);
	}
	/* Gets the coordinates of the hexagon */
	public Point [] getCoords() {
		Point [] coords = new Point[6];
		double theta = Math.PI / 6;
		for (int i = 0; i < 6; i++) {
			coords[i] = new Point((int) (SIDE_LENGTH * Math.cos(theta) + centerX), (int) (SIDE_LENGTH * Math.sin(theta) + centerY));
			theta += Math.PI / 3;
		} return coords;
	}
	public int getType() {
		return type;
	}
	/* Initializes the polygon */
	public void initPolygon() {
		hexagon = new Polygon();
		for (Point p: getCoords()) hexagon.addPoint(p.x, p.y);
	}
	/* Returns a polygon representing the hexagon */
	public Polygon getPolygon() {
		return hexagon;
	}
	/* Returns whether this hexagon contains the point (x, y) */
	public boolean contains(int x, int y) {
		return getPolygon().contains(x, y);
	}
	/* Returns whether this hexagon is on the edge of the game board */
	public boolean isOnBorder() {
		return row == 0 || row == Gameboard.LARGE_SIZE - 1 || col == 0 || col == Gameboard.LARGE_SIZE - 1;
	}
	public Settlement getSettlement() {
		return settlement;
	}
	
	/* Displays the hexagon (highlighted portion) */
	public void display(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawPolygon(getPolygon());
	}
}
