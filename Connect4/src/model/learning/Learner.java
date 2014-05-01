package model.learning;

import model.Database;


public class Learner {

	public final static float ALPHA = 0.1f;
	public final static float GAMMA = 0.9f;
	public final static int WINNING_REWARD = 127;
	public final static int LOOSING_REWARD = 0;
	public final static int FILLING_REWARD = 63;


	private PseudoState previousState;

	public void newState(PseudoState state){
		if (this.previousState != null) {
			int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * state.getValue() - this.previousState.getValue()));
			Database.getInstance().setValue(this.previousState.getGrid(), value);
		}

		this.previousState = state;
	}
	
	public void ledToWin() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * WINNING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
	
	public void ledToLoss() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * LOOSING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
	
	public void ledToFill() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * FILLING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
}
