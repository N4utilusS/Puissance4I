package model.players;

import model.Board;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

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

		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			values[i] = PseudoState.getPseudoStateForColumn(i, getBoard()).getValue();
		}

		notifyObserver();
	}
	
	@Override
	public void addObserver(Observer obs) {
		this.observer = obs;
	}

	@Override
	public void notifyObserver() {
		this.observer.update(this);
	}
	
	public int[] getValues() {
		return this.values;
	}
}
