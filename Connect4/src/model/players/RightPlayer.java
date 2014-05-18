package model.players;

import model.Board;
import model.learning.Learner;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

/**
 * Computer player always playing the the right most column.
 * This player is also used to learn during large quantities of games.
 *
 */
public class RightPlayer extends AbstractPlayer implements Subject{

	private Observer observer;
	private int[] values;

	public RightPlayer(int type, Board board, Observer obs){
		super(type, board);

		this.addObserver(obs);
	}

	@Override
	public void play() {
		this.values = new int[Board.WIDTH];
		int[] heights = getBoard().getHeights();
		int column = 0;
		
		for (int i = Board.WIDTH-1; i >= 0; --i) {
			if (heights[i] < Board.HEIGHT) {
				column = i;
				break;
			}
		}

		// To get the learner's experience, we need to reverse the board coins type if the player's type is not the learner's type:
		Board board;
		if (getType() == Learner.LEARN_TYPE) {
			board = getBoard();
		} else {
			board = getBoard().reverse();
		}

		PseudoState state = PseudoState.getPseudoStateForColumn(column, board);
		this.values[column] = state.getValue();
		getLearner().newState(state);
		getBoard().addCoinInColumn(column, getType());
		
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
