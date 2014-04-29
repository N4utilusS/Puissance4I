package model;

public class Model {
	private Database database;
	
	public Model()
	{
		database = Database.getInstance();
		//database.debug();
	}
	

}
