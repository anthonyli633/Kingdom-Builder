import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Gameboard {
	static String [] names = new String [] {"Board 1", "Board 2", "Board 3", "Board 4"};
	static int SMALL_WIDTH = 465, SMALL_HEIGHT = 396;
	static int SMALL_SIZE = 10;
	static int LARGE_WIDTH = SMALL_WIDTH * 2, LARGE_HEIGHT = SMALL_HEIGHT * 2;
	static int LARGE_SIZE = SMALL_SIZE * 2;
	
	private int width, height; /* width and height in pixels */
	private int SIZE; /* # of hexagons in a dimension */
	private int topX, topY; /* the coordinates of the top-left corner */
	public Hexagon [][] board; /* a 2-D array representing the board */
	
	private static int [] dx = new int[6];
	private static int [] dy = new int[6];
	
	/* Initializes a small gameboard */
	public Gameboard(File file) throws IOException {
		width = SMALL_WIDTH; height = SMALL_HEIGHT;
		SIZE = SMALL_SIZE;
		
		Hexagon.SIDE_LENGTH = height * 2 / 31;
		board = new Hexagon[SIZE][SIZE];
		Scanner sc = new Scanner(file);
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Point p = getCoords(i, j);
				board[i][j] = new Hexagon(i, j, sc.nextInt());
			}
		} sc.close();
	}
	
	/* Initializes a large gameboard */
	public Gameboard (Gameboard g1, Gameboard g2, Gameboard g3, Gameboard g4) {
		width = LARGE_WIDTH; height = LARGE_HEIGHT;
		SIZE = LARGE_SIZE;
		
		board = new Hexagon[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (i < SMALL_SIZE && j < SMALL_SIZE) board[i][j] = g1.board[i][j];
				if (i < SMALL_SIZE && j >= SMALL_SIZE) board[i][j] = g2.board[i][j - SMALL_SIZE];
				if (i >= SMALL_SIZE && j < SMALL_SIZE) board[i][j] = g3.board[i - SMALL_SIZE][j];
				if (i >= SMALL_SIZE && j >= SMALL_SIZE) board[i][j] = g4.board[i - SMALL_SIZE][j - SMALL_SIZE];
			}
		}
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				board[i][j].setCoords(getCoords(i, j).x, getCoords(i, j).y);
	}
	
	public void display(Graphics g, int x, int y) {
		for (int i = 0; i < 4; i++) {
			int r = i / 2, c = i % 2;
			BufferedImage img = null;
			try { img = ImageIO.read(this.getClass().getResource("/Images/" + names[i] + ".png")); } 
			catch (IOException e) { e.printStackTrace(); }
			g.drawImage(img, x + c * (SMALL_WIDTH - (int) Math.ceil(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2)), y + r * (SMALL_HEIGHT - Hexagon.SIDE_LENGTH / 2), SMALL_WIDTH, SMALL_HEIGHT, null);
		}
	}
	
	/* Returns the coordiantes of a hexagon at a specific row and col */
	public static Point getCoords(int row, int col) {
		return new Point(0, 0);
	}
}
