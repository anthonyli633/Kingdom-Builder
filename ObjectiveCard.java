import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class ObjectiveCard {
	/* 0 - Citizens
	 * 1 - Discoverers
	 * 2 - Farmers
	 * 3 - Fishermen
	 * 4 - Hermits
	 * 5 - Knights
	 * 6 - Lords
	 * 7 - Merchants
	 * 8 - Miners
	 * 9 - Workers
	 */
	
	static String [] names = "Citizens Discoverers Farmers Fishermen Hermits Knights Lords Merchants Miners Workers".split(" ");
	static int WIDTH = 108, HEIGHT = 154;
	 
	private int width, height;
	private int id;
	private int topLeftX, topLeftY;
	
	public ObjectiveCard(int id) {
		width = WIDTH * 7 / 4; height = HEIGHT * 7 / 4;
		this.id = id;
	}
	public ObjectiveCard(int id, int x, int y) {
		width = WIDTH * 7 / 4; height = HEIGHT * 7 / 4;
		this.id = id;
		setCoords(x, y);
	}
	
	public void display(Graphics g) {
		BufferedImage img = null;
		try { img = ImageIO.read(this.getClass().getResource("/Images/" + names[id] + "Objective.png")); }
		catch (IOException e) { e.printStackTrace(); }
		g.drawImage(img, topLeftX, topLeftY, width, height, null);
	}
	
	public int score(Gameboard board, Player p) {
		switch (toString()) {
			case "Citizens": return scoreCitizen(board, p);
			case "Discoverers": return scoreDiscoverer(board, p);
			case "Farmers": return scoreFarmer(board, p);
			case "Fishermen": return scoreFisherman(board, p);
			case "Hermits": return scoreHermit(board, p);
			case "Knights": return scoreKnight(board, p);
			case "Lords": return scoreLord(board, p);
			case "Merchants": return scoreMerchant(board, p);
			case "Miners": return scoreMiner(board, p);
			case "Workers": return scoreWorker(board, p);
		} return 0;
	}
	public int score(Gameboard board, int row, int col) {
		switch (toString()) {
			case "Citizens": return scoreCitizen(board, row, col);
			case "Discoverers": return scoreDiscoverer(board, row, col);
			case "Farmers": return scoreFarmer(board, row, col);
			case "Fishermen": return scoreFisherman(board, row, col);
			case "Hermits": return scoreHermit(board, row, col);
			case "Knights": return scoreKnight(board, row, col);
			case "Lords": return scoreLord(board, row, col);
			case "Merchants": return scoreMerchant(board, row, col);
			case "Miners": return scoreMiner(board, row, col);
			case "Workers": return scoreWorker(board, row, col);
		} return 0;
	}
	 
	public int getID() { return id; }
	
	public void setCoords(int x, int y) { topLeftX = x; topLeftY = y; }
//	public void enlarge() { width *= 7 / 4; height *= 7 / 4; }
	public void reset() { width = WIDTH; height = HEIGHT; }
	
	public boolean isEnlarged() { return width > WIDTH; }
	public boolean contains(int x, int y) { return x >= topLeftX && y >= topLeftY && x <= topLeftX + width && y <= topLeftY + height; }
	
	public String toString() { return names[id]; } 
	
	public static int scoreFisherman(Gameboard board, Player p) { 
		int score = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					boolean isAdjacentToWater = false;
					int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
					int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
					for (int k = 0; k < 6; k++) {
						int r = i + dx[k], c = j + dy[k];
						if (Gameboard.isValid(r, c) && board.board[r][c].getType() == 6)
							isAdjacentToWater = true;
					} if (isAdjacentToWater && board.board[i][j].getType() != 6) score++;
				}
			}
		} return score;
	}
	public static int scoreMerchant(Gameboard board, Player p) { 
		int count = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getType() >= 7) {
					if (dfsMerchant(board, p, new boolean[Gameboard.LARGE_SIZE][Gameboard.LARGE_SIZE], i, j)) count++;
				}
			}
		} return count * 4;
	}
	public static boolean dfsMerchant(Gameboard board, Player p, boolean [][] visited, int row, int col) {
		// System.out.println("Merchant: " + row + " " + col);
		visited[row][col] = true;
		int [] dx = row % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
		int [] dy = row % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
		for (int i = 0; i < 6; i++) {
			int r = row + dx[i], c = col + dy[i];
			if (Gameboard.isValid(r, c) && !visited[r][c]) {
				if (board.board[r][c].getType() >= 7 || board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == p.getID())
					if (board.board[r][c].getType() >= 7 || dfsMerchant(board, p, visited, r, c)) { System.out.println("L"); return true; }
			}
		} return false;
	}
	
	public static int scoreDiscoverer(Gameboard board, Player p) { 
		int score = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			boolean hasOneSettlement = false;
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					hasOneSettlement = true;
				}
			} score += hasOneSettlement ? 1 : 0;
		} return score;
	}
	public static int scoreHermit(Gameboard board, Player p) { 
		boolean [][] visited = new boolean[Gameboard.LARGE_SIZE][Gameboard.LARGE_SIZE];
		int count = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (!visited[i][j] && board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					size = 0;
					dfs(board, p, visited, i, j);
					count++;
				}
			}
		} return count;
	}
	public static int scoreCitizen(Gameboard board, Player p) { 
		boolean [][] visited = new boolean[Gameboard.LARGE_SIZE][Gameboard.LARGE_SIZE];
		int maxSize = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (!visited[i][j] && board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					size = 0;
					dfs(board, p, visited, i, j);
					maxSize = Math.max(maxSize, size);
				}
			}
		} return maxSize / 2;
	}
	
	static int size;
	public static void dfs(Gameboard board, Player p, boolean [][] visited, int row, int col) {
		size++;
		visited[row][col] = true;
		int [] dx = row % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
		int [] dy = row % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
		for (int i = 0; i < 6; i++) {
			int r = row + dx[i], c = col + dy[i];
			if (Gameboard.isValid(r, c) && !visited[r][c]) {
				if (board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == p.getID())
					dfs(board, p, visited, r, c);
			}
		}
	}
	
	
	public static int scoreMiner(Gameboard board, Player p) { 
		int score = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					boolean isAdjacentToMountain = false;
					int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
					int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
					for (int k = 0; k < 6; k++) {
						int r = i + dx[k], c = j + dy[k];
						if (Gameboard.isValid(r, c) && board.board[r][c].getType() == 4)
							isAdjacentToMountain = true;
					} if (isAdjacentToMountain) score++;
				}
			}
		} return score;
	}
	public static int scoreWorker(Gameboard board, Player p) { 
		int score = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					boolean isAdjacentToLocOrCastle = false;
					int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
					int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
					for (int k = 0; k < 6; k++) {
						int r = i + dx[k], c = j + dy[k];
						if (Gameboard.isValid(r, c) && board.board[r][c].getType() >= 7)
							isAdjacentToLocOrCastle = true;
					} if (isAdjacentToLocOrCastle) score++;
				}
			}
		} return score;
	}
	public static int scoreKnight(Gameboard board, Player p) { 
		int mostSettlements = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			int settlements = 0;
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				if (board.board[i][j].getSettlement() != null && board.board[i][j].getSettlement().getOwnerID() == p.getID()) {
					settlements++;
				}
			} mostSettlements = Math.max(mostSettlements, settlements);
		} return mostSettlements * 2;
	}
	public static int scoreLord(Gameboard board, Player p) { 
		int score = 0;
		for (int k = 0; k < 4; k++) {
			Hexagon [][] smallBoard = board.getSmallGameboard(k);
			int [][] vals = new int[4][2];
			for (int id = 0; id < 4; id++) {
				int settlements = 0;
				for (int i = 0; i < Gameboard.SMALL_SIZE; i++) {
					for (int j = 0; j < Gameboard.SMALL_SIZE; j++) {
						if (smallBoard[i][j].getSettlement() != null && smallBoard[i][j].getSettlement().getOwnerID() == id) {
							settlements++;
						}
					}
				} vals[id] = new int[] {id, settlements};
			} Arrays.sort(vals, (x, y) -> x[1] - y[1]);
			int max1 = vals[3][1];
			int max2 = 0;
			for (int [] i: vals) if (i[1] < max1) max2 = i[1];
			for (int [] i: vals) {
				if (i[0] == p.getID()) {
					if (i[1] == max1 && i[1] > 0) score += 12;
					if (i[1] == max2 && max1 != max2 && i[1] > 0) score += 6;
				}
			}
		} return score;
	}
	public static int scoreFarmer(Gameboard board, Player p) { 
		int minSettlements = 100;
		for (int k = 0; k < 4; k++) {
			Hexagon [][] smallBoard = board.getSmallGameboard(k);
			int settlements = 0;
			for (int i = 0; i < Gameboard.SMALL_SIZE; i++) {
				for (int j = 0; j < Gameboard.SMALL_SIZE; j++) {
					if (smallBoard[i][j].getSettlement() != null && smallBoard[i][j].getSettlement().getOwnerID() == p.getID()) {
						settlements++;
					}
				}
			} minSettlements = Math.min(settlements, minSettlements);
		} return minSettlements * 3;
	}
	public static int scoreCastle(Gameboard board, Player p) { 
		int score = 0;
		for (int i = 0; i < Gameboard.LARGE_SIZE; i++) {
			for (int j = 0; j < Gameboard.LARGE_SIZE; j++) {
				boolean isAdj = false;
				if (board.board[i][j].getType() == 8) {
					int [] dx = i % 2 == 0 ? Gameboard.dxEvens : Gameboard.dxOdds;
					int [] dy = i % 2 == 0 ? Gameboard.dyEvens : Gameboard.dyOdds;
					for (int k = 0; k < 6; k++) {
						int r = i + dx[k], c = j + dy[k];
						if (Gameboard.isValid(r, c) && board.board[r][c].getSettlement() != null && board.board[r][c].getSettlement().getOwnerID() == p.getID()) {
							isAdj = true;
						}
					}
				} if (isAdj) score++;
			}
		} return score * 3;
	}
	
	public static int scoreFisherman(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreFisherman(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreFisherman(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreMerchant(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreMerchant(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreMerchant(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreDiscoverer(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreDiscoverer(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreDiscoverer(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreHermit(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreHermit(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreHermit(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreCitizen(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreCitizen(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreCitizen(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreMiner(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreMiner(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreMiner(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreWorker(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreWorker(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreWorker(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreKnight(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreKnight(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreKnight(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreLord(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreLord(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreLord(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreFarmer(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreFarmer(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreFarmer(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
	public static int scoreCastle(Gameboard board, int row, int col) { 
		Player p = KingdomBuilderPanel.players[KingdomBuilderPanel.currentPlayerID];
		int before = scoreCastle(board, p);
		board.board[row][col].setSettlement(board, new Settlement(KingdomBuilderPanel.currentPlayerID, board.board[row][col]));
		int after = scoreCastle(board, p);
		board.board[row][col].removeSettlement(board);
		return after - before;
	}
}
