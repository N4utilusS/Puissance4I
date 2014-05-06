package model.learning;

import model.Database;

/**
 * Used to learn the value associated to each pseudo state, while players are playing.
 *
 */
public class Learner {

	public final static float ALPHA = 0.1f;
	public final static float GAMMA = 0.9f;
	public final static int WINNING_REWARD = 127;
	public final static int LOOSING_REWARD = 0;
	public final static int FILLING_REWARD = 63;
	public final static int LEARN_TYPE = 2;
	public final static int OTHER_TYPE = 1;	


	private PseudoState previousState;

	/**
	 * Receives a the new chosen pseudo state, 
	 * and updates the value of the previous one based on the value of the new one.
	 * @param state The new pseudo state.
	 */
	public void newState(PseudoState state){
		if (this.previousState != null) {
			int value = Math.round(this.previousState.getValue() + ALPHA * (GAMMA * state.getValue() - this.previousState.getValue()));
			Database.getInstance().setValue(this.previousState.getGrid(), value);
		}

		this.previousState = state;
	}
	
	/**
	 * Used at the end of the game to indicate that the previous state led to victory.
	 * Simulates the addition of a new state with victory reward.
	 */
	public void ledToWin() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (WINNING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
	
	/**
	 * Used at the end of the game to indicate that the previous state led to loss.
	 * Simulates the addition of a new state with loss reward.
	 */
	public void ledToLoss() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (LOOSING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
	
	/**
	 * Used at the end of the game to indicate that the previous state led to equality.
	 * Simulates the addition of a new state with equality reward.
	 */
	public void ledToFill() {
		int value = Math.round(this.previousState.getValue() + ALPHA * (FILLING_REWARD - this.previousState.getValue()));
		Database.getInstance().setValue(this.previousState.getGrid(), value);
	}
}
