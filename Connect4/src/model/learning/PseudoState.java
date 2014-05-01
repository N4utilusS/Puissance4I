package model.learning;

import model.Board;
import model.Database;

/**
 * Represents a pseudo state of the board, containing only a smaller neighbourhood of the considered column to improve space complexity.
 * The algorithm only has a partial knowledge of the current global state of the game.
 *
 */
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
	 * @param column The number of the column we want the pseudo state from.
	 * @param board The game board.
	 * @return The created pseudo state.
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

	/**
	 * Returns the matrix of the pseudo state, with all the coin types.
	 * @return
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Returns the value associated to this pseudo state.
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
}
