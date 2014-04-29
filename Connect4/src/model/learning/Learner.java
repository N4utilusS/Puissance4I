package model.learning;

import model.Database;


public class Learner {

	public final static float ALPHA = 0.1f;
	public final static float GAMMA = 0.9f;


	private PseudoState previousState;

	public void newState(PseudoState state){
		if (this.previousState != null) {
			int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * state.getValue() - this.previousState.getValue()));
			Database.getInstance().setValue(this.previousState.getGrid(), value);
		}

		this.previousState = state;
	}
}
