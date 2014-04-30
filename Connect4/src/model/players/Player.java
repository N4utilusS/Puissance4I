package model.players;

import observer.Observer;
import observer.Subject;
import model.Board;

public class Player extends AbstractPlayer implements Subject{
	
	private Observer observer;

	public Player(int type, Observer obs) {
		super(type);
		
		addObserver(obs);
	}

	@Override
	public Board play(Board board) {
		
		// Send state to the view
		notifyObserver();
		
		return null;
	}

	@Override
	public void addObserver(Observer obs) {
		this.observer = obs;
	}

	@Override
	public void notifyObserver() {
		this.observer.update(this);
	}

}
