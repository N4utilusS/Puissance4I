package model;

public class Board {
	public final static int WIDTH = 7;
	public final static int HEIGHT = 6;
	
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
	
	public Board addCoinInColumn(int column, int coinType){
		int[][] grid = deepGridClone();
		int[] heights = this.heights.clone();
		
		grid[column][heights[column]] = coinType;
		heights[column]++;
	    
		return new Board(grid, heights);
	}
	
	private int[][] deepGridClone(){
		if (this.grid == null)
	        return null;
	    int[][] grid = new int[this.grid.length][];
	    for (int r = 0; r < this.grid.length; r++) {
	        grid[r] = this.grid[r].clone();
	    }
	    
	    return grid;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int[] getHeights() {
		return heights;
	}
	
}
