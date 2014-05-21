package model.players;

import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.learning.Learner;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

/**
 * Computer player using the epsilon-Greedy algorithm to play.
 * This player is also used to learn during large quantities of games.
 *
 */
public class Decider extends AbstractPlayer implements Subject{

	public static float EPSILON = 0.1f;
	private Random random;
	private Observer observer;
	private int[] values;

	public Decider(int type, Board board, Observer obs, float epsilon){
		super(type, board);
		
		this.EPSILON = epsilon;
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
		
		// To get the learner's experience, we need to reverse the board coins type if the player's type is not the learner's type:
		Board board, boardOther;
		if (getType() == Learner.LEARN_TYPE) {
			board = getBoard();
			boardOther = getBoard().reverse();
		} else {
			board = getBoard().reverse();
			boardOther = getBoard();
		}

		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (heights[i] < Board.HEIGHT) {
				
				// Normal move choice:
				board.addCoinInColumn(i, Learner.LEARN_TYPE);
				states[i] = PseudoState.getPseudoStateForColumn(i, board);
				board.removeCoinInColumn(i);
				
				values[i] = states[i].getValue();

				// Prevention move detection, put yourself into ennemie's mind:
				boardOther.addCoinInColumn(i, Learner.LEARN_TYPE);
				int preventionValue = PseudoState.getPseudoStateForColumn(i, boardOther).getValue()-1;	// -1 because attack has higher priority on defense.
				values[i] = Math.max(values[i], preventionValue);	// Remember the best value.
				boardOther.removeCoinInColumn(i);
				
				// Best actions management:
				if (values[i] > maxValue) {
					best = new ArrayList<Integer>();
					best.add(i);
					maxValue = values[i];
				} else if (values[i] == maxValue) {
					best.add(i);
				}
				
				available.add(i);
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
		this.notifyObserver();
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
