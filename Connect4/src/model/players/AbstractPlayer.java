package model.players;

import model.Board;
import model.learning.Learner;

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
	
	abstract public void play();

	public int getType() {
		return type;
	}
	
	public Learner getLearner() {
		return this.learner;
	}

	public Board getBoard() {
		return board;
	}
	
	public void wins() {
		getLearner().ledToWin();
	}
	
	public void looses() {
		getLearner().ledToLoss();
	}
	
	public void filled() {
		getLearner().ledToFill();
	}
}
