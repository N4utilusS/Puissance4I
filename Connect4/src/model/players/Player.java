package model.players;

import observer.Observer;
import observer.Subject;
import model.Board;
import model.learning.PseudoState;

public class Player extends AbstractPlayer implements Subject {
	
	private Observer observer;

	public Player(int type, Board board, Observer obs) {
		super(type, board);
		
		addObserver(obs);
	}
	
	public int[][] getState() {
		return null;
	}

	@Override
	public void play() {
		
		// Send state to the view to allow the human to choose.
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
	
	public void setAction(int column) {
		PseudoState state = PseudoState.getPseudoStateForColumn(column, getBoard());
		getLearner().newState(state);
		getBoard().addCoinInColumn(column, getType());
	}

}
