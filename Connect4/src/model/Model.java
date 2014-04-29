package model;

import java.util.ArrayList;

import observer.Observer;
import observer.Subject;

public class Model implements Subject {
	private Database database;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	
	public Model()
	{
		database = new Database();		
		//database.debug();
	}
	
	/**
	 * Updates the value of a given state
	 * @param state the state which is a table 5x5
	 * @param value the new value to save (value between 0 and 255)
	 */
	public void setValue(int state[][], int value)
	{
		database.setValue(state, value);
	}
	
	/**
	 * Returns the value of a given state
	 * @param state the state which is a table 5x5
	 * @return int a value between 0 and 255
	 */
	public int getValue(int state[][])
	{
		return database.getValue(state);	
	}

	@Override
	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	@Override
	public void notifyObserver() {
		for (Observer obs : this.listObserver)
			obs.update();
	}
}
