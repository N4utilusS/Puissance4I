package model.players;

import model.Board;
import model.learning.Learner;

public abstract class AbstractPlayer {
	
	private Learner learner;
	private int type;

	public AbstractPlayer(int type){
		super();
		
		this.type = type;
		this.learner = new Learner();
	}
	
	abstract public Board play(Board board);

	public int getType() {
		return type;
	}
	
	public Learner getLearner() {
		return this.learner;
	}
}
