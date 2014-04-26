package model;

public class Model {
	private Database database;
	
	public Model()
	{
		database = new Database();		
		//database.debug();
	}
	
	/**
	 * Update the value of a given state
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
}
