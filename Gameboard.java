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

    public BufferedImage [] imgs = new BufferedImage[4];
    private int width, height; /* width and height in pixels */
    private int SIZE; /* # of hexagons in a dimension */
    private int topX, topY; /* the coordinates of the top-left corner */
    public Hexagon [][] board; /* a 2-D array representing the board */

    private Polygon outline; 

    public static int [] dxEvens = new int[] {0, -1, -1, 0, 1, 1};
    public static int [] dyEvens = new int[] {1, 0, -1, -1, -1, 0};
    public static int [] dxOdds = new int[] {0, -1, -1, 0, 1, 1};
    public static int [] dyOdds = new int[] {1, 1, 0, -1, 0, 1};
    
    public static boolean isValid(int r, int c) {
        return r >= 0 && c >= 0 && r < LARGE_SIZE && c < LARGE_SIZE;
    }

    public ArrayList<int []> locationTileCoords = new ArrayList<> ();

    /* Initializes a small gameboard */
    public Gameboard(InputStream file) throws IOException {
        width = SMALL_WIDTH; height = SMALL_HEIGHT;
        SIZE = SMALL_SIZE;

        Hexagon.SIDE_LENGTH = (double) height * 2 / 31;
        board = new Hexagon[SIZE][SIZE];
        Scanner sc = new Scanner(file);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Point p = getCoords(i, j);
                board[i][j] = new Hexagon(i, j, Hexagon.conversion[sc.nextInt()]);
                if (board[i][j].getType() == 7) {
                    locationTileCoords.add(new int[] {i, j});
                }
            }
        } sc.close();
    }

    /* Initializes a large gameboard */
    public Gameboard (Gameboard g1, Gameboard g2, Gameboard g3, Gameboard g4) {
        width = LARGE_WIDTH; height = LARGE_HEIGHT;
        SIZE = LARGE_SIZE;

        for (int i = 0; i < 4; i++) {
            try { imgs[i] = ImageIO.read(this.getClass().getResource("/Images/" + names[i] + ".png")); }
            catch (IOException e) { e.printStackTrace(); }
        }

        board = new Hexagon[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i < SMALL_SIZE && j < SMALL_SIZE) board[i][j] = g1.board[i][j];
                if (i < SMALL_SIZE && j >= SMALL_SIZE) board[i][j] = g2.board[i][j - SMALL_SIZE];
                if (i >= SMALL_SIZE && j < SMALL_SIZE) board[i][j] = g3.board[i - SMALL_SIZE][j];
                if (i >= SMALL_SIZE && j >= SMALL_SIZE) board[i][j] = g4.board[i - SMALL_SIZE][j - SMALL_SIZE];
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new Hexagon(i, j, board[i][j].getType());
            }
        }

        int MARGIN = 10;
        int [] xs = new int[160], ys = new int[160];
        for (int i = 0; i < 20; i++) {
            xs[2 * i] = board[i][0].getCoords()[3].x; ys[2 * i] = board[i][0].getCoords()[3].y;
            xs[2 * i + 1] = board[i][0].getCoords()[2].x; ys[2 * i + 1] = board[i][0].getCoords()[2].y;
        }
        for (int i = 20; i < 40; i++) {
            xs[2 * i] = board[19][i % 20].getCoords()[1].x; ys[2 * i] = board[19][i % 20].getCoords()[1].y;
            xs[2 * i + 1] = board[19][i % 20].getCoords()[0].x; ys[2 * i + 1] = board[19][i % 20].getCoords()[0].y;
        }
        for (int i = 40; i < 60; i++) {
            xs[2 * i] = board[19 - i % 20][19].getCoords()[0].x; ys[2 * i] = board[19 - i % 20][19].getCoords()[0].y;
            xs[2 * i + 1] = board[19 - i % 20][19].getCoords()[5].x; ys[2 * i + 1] = board[19 - i % 20][19].getCoords()[5].y;
        }
        for (int i = 60; i < 80; i++) {
            xs[2 * i] = board[0][19 - i % 20].getCoords()[4].x; ys[2 * i] = board[0][19 - i % 20].getCoords()[4].y;
            xs[2 * i + 1] = board[0][19 - i % 20].getCoords()[3].x; ys[2 * i + 1] = board[0][19 - i % 20].getCoords()[3].y;
        }

        outline = new Polygon();
        for (int i = 0; i < 160; i++) outline.addPoint(xs[i], ys[i]);

        locationTileCoords.addAll(g1.locationTileCoords);
        locationTileCoords.addAll(g2.locationTileCoords);
        locationTileCoords.addAll(g3.locationTileCoords);
        locationTileCoords.addAll(g4.locationTileCoords);
    }
    
    public Hexagon [][] getSmallGameboard(int index) {
    	Hexagon [][] smallBoard = new Hexagon[SMALL_SIZE][SMALL_SIZE];
    	for (int i = 0; i < 10; i++) {
    		for (int j = 0; j < 10; j++) {
    			smallBoard[i][j] = board[SMALL_SIZE * (index / 2) + i][SMALL_SIZE * (index % 2) + j];
    		}
    	} return smallBoard;
    }

    public void display(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(15));
        g.drawPolygon(outline);
        g2.setStroke(new BasicStroke(5));

        for (int i = 0; i < 4; i++) {
            int r = i / 2, c = i % 2;
            g.drawImage(imgs[i], x + c * (SMALL_WIDTH - (int) Math.round(Hexagon.SIDE_LENGTH * Math.sqrt(3) / 2)), y + (int) Math.round(r * (SMALL_HEIGHT - Hexagon.SIDE_LENGTH / 2)), SMALL_WIDTH, SMALL_HEIGHT, null);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j].display(g);
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].isHighlighted) board[i][j].display(g);
            }
        } 
    }

    /* Returns the coordiantes of a hexagon at a specific row and col */
    public static Point getCoords(int row, int col) {
        double x = KingdomBuilderPanel.GAMEBOARD_MARGIN_X + 30, y = KingdomBuilderPanel.GAMEBOARD_MARGIN_Y;
        if (row % 2 == 0) {
            x += Hexagon.SIDE_LENGTH * (2 * col + 1) * Math.sqrt(3) / 2;
            y += Hexagon.SIDE_LENGTH * (3 * row / 2 + 1);
        } else {
            x += Hexagon.SIDE_LENGTH * (col + 1) * Math.sqrt(3);
            y += Hexagon.SIDE_LENGTH * (6 * (row / 2 + 1) - 1) / 2;
        } return new Point((int) Math.round(x), (int) Math.round(y));
    }
}
