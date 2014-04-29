package model.learning;

import model.Board;
import model.Database;

public class PseudoState {
	public final static int WIDTH = 5;
	public final static int HEIGHT = 5;
	
	private int[][] grid;
	private int value;
	
	public PseudoState(int[][] grid, int value){
		super();
		
		this.grid = grid;
		this.value = value;
	}
	
	/**
	 * We suppose, for performance reasons, that all uninitialized int variables are set to 0.
	 * @param column
	 * @param board
	 * @return
	 */
	public static PseudoState getPseudoStateForColumn(int column, Board board){
		
		int begin = column-WIDTH/2;
		int[] heights = board.getHeights();
		int minRow = 0;
		int[][] boardGrid = board.getGrid();
		int[][] grid = new int[WIDTH][HEIGHT];

		for (int c = begin; c < begin + WIDTH; ++c) {
			if (c >= 0 && c < Board.WIDTH && heights[c] >= HEIGHT) {
				minRow = 1;
				break;
			}
		}
		
		
		for (int c = begin, i = 0; c < begin + WIDTH; ++c, ++i) {
			if (c >= 0 && c < Board.WIDTH) {
				for (int r = minRow, j = 0; r < minRow + HEIGHT; ++r, ++j) {
					grid[i][j] = boardGrid[c][r];
				}
			}
		}
		
		int value = Database.getInstance().getValue(grid);
		
		return new PseudoState(grid, value);
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getValue() {
		return value;
	}
	
}
