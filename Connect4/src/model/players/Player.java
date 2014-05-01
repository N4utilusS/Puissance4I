package model.players;

import observer.Observer;
import observer.Subject;
import model.Board;
import model.learning.PseudoState;

public class Player extends AbstractPlayer implements Subject {
	
	public final static int PLAYING = 0;
	public final static int WON = 1;
	public final static int LOST = 2;
	public final static int FILLED = 3;
	
	
	private Observer observer;
	private int status = PLAYING;

	public Player(int type, Board board, Observer obs) {
		super(type, board);
		
		addObserver(obs);
	}
	
	public int[][] getState() {
		return this.getBoard().getGrid();
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
		getBoard().addCoinInColumn(column, getType());
		PseudoState state = PseudoState.getPseudoStateForColumn(column, getBoard());
		getLearner().newState(state);
	}
	
	@Override
	public void wins() {
		super.wins();
		
		this.status = WON;
		this.notifyObserver();
	}
	
	@Override
	public void looses() {
		super.looses();
		
		this.status = LOST;
		this.notifyObserver();
	}
	
	@Override
	public void filled() {
		super.filled();
		
		this.status = FILLED;
		this.notifyObserver();
	}

	public int getStatus() {
		return this.status;
	}
}
