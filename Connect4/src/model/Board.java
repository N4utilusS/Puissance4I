package model;

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
	
	public void addCoinInColumn(int column, int coinType){
		
		grid[column][heights[column]] = coinType;
		heights[column]++;
	}
	
	public void removeCoinInColumn(int column) {
		grid[column][heights[column]-1] = 0;
		heights[column]--;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int[] getHeights() {
		return heights;
	}
	
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
			for (int j = Board.HEIGHT-1; j >= 0 && i + Board.HEIGHT - j < Board.WIDTH; --j) {
				if (grid[i + Board.HEIGHT - j][j] != type) {
					count = 0;
					type = grid[i + Board.HEIGHT - j][j];
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
	
	public boolean isFull() {
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (this.heights[i] < Board.HEIGHT)
				return false;
		}
		
		return true;
	}
}
