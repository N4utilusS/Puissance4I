package model;

import java.io.File;
import java.util.ArrayList;

import observer.Observer;
import observer.Subject;

public class Model implements Subject, Runnable { // TODO Need to implement Subject ?
	private Database database;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private Thread learningThread;
	private boolean continueLearning = true;
	private int amountOfGamesToLearn = 0;
	private int gamesPlayed = 0;
	public final static float INTERVAL = 1000f;
	private int temp;
	private long savedTime;
	private float averageTime = 0f;
	
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
		Game game = new Game(mode, null);
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
		this.temp = this.amountOfGamesToLearn;
		this.savedTime = System.currentTimeMillis();
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
			
			if (this.temp - this.amountOfGamesToLearn == INTERVAL) {
				long time = System.currentTimeMillis();
				float rem =  (float) (time - this.savedTime) / INTERVAL * this.amountOfGamesToLearn / (3600000f);
				this.averageTime = this.averageTime * 0.9f + rem * 0.1f;
				this.averageTime = Math.round(10 * this.averageTime) / 10.0f;
				System.out.println("To learn:" + amountOfGamesToLearn + " - Learnt:" + gamesPlayed + " - Remaining time:" + this.averageTime + " hours");
				this.temp = this.amountOfGamesToLearn;
				this.savedTime = time;
				File stop = new File("stop");
				if (stop.exists())
					this.continueLearning = false;
			}
			
			notifyObserver();
			
			synchronized(this) {
				if (!continueLearning || amountOfGamesToLearn == 0)
					break;
			}
		}
		
		this.saveDatabase();
	}
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}
	
	public void saveDatabase()
	{
		database.saveBufferToFiles();
		//database.end();
	}
}
