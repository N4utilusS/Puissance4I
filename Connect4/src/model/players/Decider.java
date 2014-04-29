package model.players;

import java.util.Random;

import model.Board;
import model.learning.PseudoState;

public class Decider extends AbstractPlayer {
	
	public final static float EPSILON = 0.1f;
	private Random random;
	
	public Decider(int number){
		super(number);
		
		this.random = new Random();
	}

	@Override
	public Board play(Board board) {
		PseudoState[] states = new PseudoState[Board.WIDTH];
		int maxValue = 0;
		int column = 0;
		
		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			states[i] = PseudoState.getPseudoStateForColumn(i, board);
			
			if (states[i].getValue() >= maxValue) {
				maxValue = states[i].getValue();
				column = i;
			}
		}
			
		// Choose one.
		if (random.nextFloat() < EPSILON) {
			int rand = random.nextInt(Board.WIDTH);
			getLearner().newState(states[rand]);
			return board.addCoinInColumn(rand, getType());
		} else {
			getLearner().newState(states[column]);
			return board.addCoinInColumn(column, getType());
		}
	}
}
