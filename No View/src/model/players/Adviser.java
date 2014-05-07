package model.players;

import model.Board;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

/**
 * Player not actually playing itself, but advising a human player by sending him the values of the pseudo states.
 *
 */
public class Adviser extends AbstractPlayer implements Subject {

	private Observer observer;
	private int[] values;

	public Adviser(int type, Board board, Observer obs){
		super(type, board);
		
		addObserver(obs);
	}

	@Override
	public void play() {
		values = new int[Board.WIDTH];
		int[] heights = getBoard().getHeights();

		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (heights[i] < Board.HEIGHT) {
				getBoard().addCoinInColumn(i, getType());
				values[i] = PseudoState.getPseudoStateForColumn(i, getBoard()).getValue();
				getBoard().removeCoinInColumn(i);
			}
		}

		notifyObserver();
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
	
	/**
	 * Called to get the values.
	 * @return
	 */
	public int[] getValues() {
		return this.values;
	}
}
