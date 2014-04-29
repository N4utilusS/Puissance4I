package model;

import java.util.ArrayList;

import observer.Observer;
import observer.Subject;

public class Model implements Subject {
	private Database database;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	
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
			obs.update();
	}
}
