package model;


/**
 * Represents the playing board and its state, with a matrix of integers and
 * the heights of the different columns.
 *
 */
public class Board {
	public final static int WIDTH = 7;
	public final static int HEIGHT = 6;
	public final static int AMOUNT_TO_WIN = 4;	
	
	private int[][] grid;
	private int[] heights;
	
	public Board(){
		super();
		this.grid = new int[WIDTH][HEIGHT];
		this.heights = new int[WIDTH];
	}
	
	public Board(int[][] grid, int[] heights){
		super();
		this.grid = grid;
		this.heights = heights;
	}
	
	/**
	 * Used to insert a coin in the specified column.
	 * @param column The column number to insert to coin into.
	 * @param coinType The coin type of the player.
	 */
	public void addCoinInColumn(int column, int coinType){
		
		grid[column][heights[column]] = coinType;
		heights[column]++;
	}
	
	/**
	 * Used to remove a coin in the specified column.
	 * @param column The column number where to remove the coin.
	 */
	public void removeCoinInColumn(int column) {
		grid[column][heights[column]-1] = 0;
		heights[column]--;
	}

	/**
	 * Returns the matrix containing all the coin types, representing the game board.
	 * @return
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Returns the height of each column in a vector.
	 * @return
	 */
	public int[] getHeights() {
		return heights;
	}
	
	/**
	 * Return a boolean telling if the game is over, by the rules of the game (not if the board is filled).
	 * @return
	 */
	public boolean gameOver() {
		int count = 0;
		int type = 0;
		
		// Look at rows
		for (int j = 0; j < Board.HEIGHT; ++j) {
			for (int i = 0; i < Board.WIDTH; ++i) {
				if (grid[i][j] != type) {
					count = 0;
					type = grid[i][j];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		// Look at columns
		for (int i = 0; i < Board.WIDTH; ++i) {
			for (int j = 0; j < Board.HEIGHT; ++j) {
				if (grid[i][j] != type) {
					count = 0;
					type = grid[i][j];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		// Diagonals SouthEast
		for (int j = AMOUNT_TO_WIN-1; j < Board.HEIGHT; ++j) {
			for (int i = 0; j - i >= 0 && i < Board.WIDTH; ++i) {
				if (grid[i][j-i] != type) {
					count = 0;
					type = grid[i][j-i];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		for (int i = 1; i <= Board.WIDTH-4; ++i) {
			for (int j = Board.HEIGHT-1; j >= 0 && i + Board.HEIGHT-1 - j < Board.WIDTH; --j) {
				if (grid[i + Board.HEIGHT-1 - j][j] != type) {
					count = 0;
					type = grid[i + Board.HEIGHT-1 - j][j];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		// Diagonals NorthEast
		for (int j = 0; j <= Board.HEIGHT-4; ++j) {
			for (int i = 0; i < Board.WIDTH && j + i < Board.HEIGHT; ++i) {
				if (grid[i][j+i] != type) {
					count = 0;
					type = grid[i][j+i];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		for (int i = 1; i <= Board.WIDTH-4; ++i) {
			for (int j = 0; j < Board.HEIGHT && i + j < Board.WIDTH; ++j) {
				if (grid[i + j][j] != type) {
					count = 0;
					type = grid[i + j][j];
				}
				if (type != 0)
					count++;
				if (count == AMOUNT_TO_WIN)
					return true;
			}
			
			count = 0;
		}
		
		return false;
	}
	
	/**
	 * Returns a boolean telling if the board is filled entirely.
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (this.heights[i] < Board.HEIGHT)
				return false;
		}
		
		return true;
	}

	public Board reverse() {
		
		int[][] grid = new int[WIDTH][HEIGHT];
		int[] heights = this.heights.clone();
		
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				if (this.grid[i][j] == 1) {
					grid[i][j] = 2;
				} else if (this.grid[i][j] == 2) {
					grid[i][j] = 1;
				}
			}
		}
				
		return new Board(grid, heights);
	}
}
