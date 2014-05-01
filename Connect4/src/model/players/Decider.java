package model.players;

import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

/**
 * Computer player using the epsilon-Greedy algorithm to play.
 * This player is also used to learn during large quantities of games.
 *
 */
public class Decider extends AbstractPlayer implements Subject{

	public final static float EPSILON = 0.1f;
	private Random random;
	private Observer observer;
	private int[] values;

	public Decider(int type, Board board, Observer obs){
		super(type, board);

		this.addObserver(obs);
		this.random = new Random();
	}

	@Override
	public void play() {
		PseudoState[] states = new PseudoState[Board.WIDTH];
		int maxValue = -1;
		int[] heights = getBoard().getHeights();
		ArrayList<Integer> available = new ArrayList<Integer>();
		ArrayList<Integer> best = null;
		this.values = new int[Board.WIDTH];

		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (heights[i] < Board.HEIGHT) {
				getBoard().addCoinInColumn(i, getType());
				states[i] = PseudoState.getPseudoStateForColumn(i, getBoard());
				getBoard().removeCoinInColumn(i);
				
				if (states[i].getValue() > maxValue) {
					best = new ArrayList<Integer>();
					best.add(i);
					maxValue = states[i].getValue();
				} else if (states[i].getValue() == maxValue) {
					best.add(i);
				}
				
				available.add(i);
				values[i] = states[i].getValue();
			}
		}

		// Choose one.
		if (random.nextFloat() >= EPSILON) {
			int column = best.get(random.nextInt(best.size()));
			getLearner().newState(states[column]);
			getBoard().addCoinInColumn(column, getType());
		} else {
			int column = available.get(random.nextInt(available.size()));
			getLearner().newState(states[column]);
			getBoard().addCoinInColumn(column, getType());
		}
		
		// Notify:
		//this.notifyObserver();
	}
	
	@Override
	public void addObserver(Observer obs) {
		this.observer = obs;
	}

	@Override
	public void notifyObserver() {
		if (this.observer != null)
			this.observer.update(this);
	}
	
	public int[][] getState() {
		return this.getBoard().getGrid();
	}
	
	/**
	 * Returns the values of the pseudo states.
	 * @return
	 */
	public int[] getValues() {
		return this.values;
	}
}
