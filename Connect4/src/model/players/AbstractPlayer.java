package model.players;

import model.Board;
import model.learning.Learner;

/**
 * The abstract class inherited by all players, containing common attributes and methods.
 *
 */
public abstract class AbstractPlayer {
	
	private Learner learner;
	private int type;
	private Board board;

	public AbstractPlayer(int type, Board board){
		super();
		
		this.type = type;
		this.board = board;
		this.learner = new Learner();
	}
	
	/**
	 * Gives the hand to the player to play his turn.
	 * Must implement all the logic behind the player's choice.
	 */
	abstract public void play();

	/**
	 * Returns the coin type of the player to fill the game board with.
	 * @return
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Returns the learner object connected to this player.
	 * @return
	 */
	public Learner getLearner() {
		return this.learner;
	}

	/**
	 * Returns the game board.
	 * @return
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Must be called when the player wins.
	 */
	public void wins() {
		if (getType() == 2)
			getLearner().ledToWin();
	}
	
	/**
	 * Must be called when the player looses.
	 */
	public void looses() {
		if (getType() == 2)
			getLearner().ledToLoss();
	}
	
	/**
	 * Must be called when the player is equal to the other at the end of the game.
	 */
	public void filled() {
		if (getType() == 2)
			getLearner().ledToFill();
	}
}
