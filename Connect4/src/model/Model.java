package model;

import java.util.ArrayList;

import model.players.Player;

import observer.Observer;
import observer.Subject;

public class Model implements Subject, Runnable { // TODO Need to implement Subject ?
	private Database database;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private Thread learningThread;
	private boolean continueLearning = true;
	private int amountOfGamesToLearn = 0;
	private int gamesPlayed = 0;
	private Game game;
	private int mode;
	
	public Model()
	{
		database = Database.getInstance();
		//database.debug();
	}

	@Override
	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	@Override
	public void notifyObserver() {
		for (Observer obs : this.listObserver)
			obs.update(this);
	}
	
	public void startGame(int mode) {
		game = new Game(mode, this.listObserver.get(0));
		this.mode = mode;
		game.letsPlay();
	}
	
	public void startLearning(int amountOfGames) {
		
		if (this.learningThread != null) {
			this.continueLearning = false;
			try {
				this.learningThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.learningThread = new Thread(this);
		this.continueLearning = true;
		if (amountOfGames <= 0)
			this.amountOfGamesToLearn = -1;
		else
			this.amountOfGamesToLearn = amountOfGames;
		this.learningThread.start();
	}
	
	public void stopLearning() {
		this.continueLearning = false;
	}

	@Override
	public void run() {
		while (true) {
			
			startGame(Game.COMPUTER_VS_COMPUTER);
			
			if (amountOfGamesToLearn > 0)
				amountOfGamesToLearn--;
			
			gamesPlayed++;
			notifyObserver();
			
			synchronized(this) {
				if (!continueLearning || amountOfGamesToLearn == 0)
					break;
			}
		}
	}
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}
	
	/**
	 * Called by the view after the human player has played,
	 * so his action is taken into account, and the game can continue.
	 * @param column The column played.
	 * @return
	 */
	public boolean humanPlayerPlayed(int column) {
		if (this.game != null && this.mode != Game.COMPUTER_VS_COMPUTER)
			return game.humanPlayerPlayed(column);
		else
			return true;
	}
}
