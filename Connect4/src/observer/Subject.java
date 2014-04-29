package observer;

public interface Subject {

	public void addObserver(Observer obs);
	public void notifyObserver();
}
