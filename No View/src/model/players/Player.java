package model.players;

import observer.Observer;
import observer.Subject;
import model.Board;
import model.learning.PseudoState;

/**
 * Class for the human player. Contacts the view to send the state of the game.
 * The view then sends back the action chosen, so the game can go on.
 *
 */
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
	
	/**
	 * Returns the matrix containing all the coin types, the actual state of the game.
	 * @return
	 */
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
		if (this.observer != null)
			this.observer.update(this);
	}
	
	/**
	 * Must be called to set the choice of the human player in the view.
	 * Puts the coin in the column and gives the new pseudo state to the learner.
	 * @param column The chosen column.
	 */
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

	/**
	 * Returns the status of the player, that is, if the player is currently playing, or if he has lost, won, or filled the board with the other player.
	 * @return
	 */
	public int getStatus() {
		return this.status;
	}
}
