package model.players;

import java.util.ArrayList;
import java.util.Random;

import model.Board;
import model.learning.PseudoState;
import observer.Observer;
import observer.Subject;

public class Decider extends AbstractPlayer implements Subject{

	public final static float EPSILON = 0.1f;
	private Random random;
	private Observer observer;

	public Decider(int type, Board board, Observer obs){
		super(type, board);

		this.addObserver(obs);
		this.random = new Random();
	}

	@Override
	public void play() {
		PseudoState[] states = new PseudoState[Board.WIDTH];
		int maxValue = 0;
		int column = 0;
		int[] heights = getBoard().getHeights();
		ArrayList<Integer> available = new ArrayList<Integer>();

		// Get all the pseudo states for all the columns.
		for (int i = 0; i < Board.WIDTH; ++i) {
			if (heights[i] < Board.HEIGHT) {
				getBoard().addCoinInColumn(i, getType());
				states[i] = PseudoState.getPseudoStateForColumn(i, getBoard());
				getBoard().removeCoinInColumn(i);

				if (states[i].getValue() >= maxValue) {
					maxValue = states[i].getValue();
					column = i;
				}
				
				available.add(i);
			}
		}

		// Choose one.
		if (random.nextFloat() >= EPSILON) {
			getLearner().newState(states[column]);
			getBoard().addCoinInColumn(column, getType());
		} else {
			int rand = available.get(random.nextInt(available.size()));
			getLearner().newState(states[rand]);
			getBoard().addCoinInColumn(rand, getType());
		}
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
	
	public int[][] getState() {
		return this.getBoard().getGrid();
	}
}
