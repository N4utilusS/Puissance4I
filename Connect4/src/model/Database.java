package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

public class Database {
	private int maxStates = 2;
	private BufferedReader in;
	
	/**
	 * Update the value of a given state
	 * @param state the state which is a table 5x5
	 * @param value the new value to save (value between 0 and 255)
	 */
	public void setValue(int state[][], int value)
	{
		long id = this.createId(state);
		
		try {
			String path = this.path(id, true);
			this.saveValue(path, id, value);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the value of a given state
	 * @param state the state which is a table 5x5
	 * @return int a value between 0 and 255
	 */
	public int getValue(int state[][])
	{
		long id = this.createId(state);
		
		try {
			String path = this.path(id, false);
			return this.findValue(path, id);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * Creates the id for a given state
	 * @param state
	 * @return long 
	 */
	private long createId(int state[][])
	{
		long id = 0;
		int temp = 0;
		
		for(int i=0; i < state.length; i++)
		{
			temp = 0;
			for(int j=0; j < state[i].length; j++)
				temp += state[i][j] * Math.pow(this.maxStates, j);
			id += temp * Math.pow(242, i);//2*3^0 + 2*3^1 + 2*3^2 + 2*3^3 + 2*3^4 = 242. Maximum value for the id : 242*242^0+...= 3 443 973 390
		}
		
		return id;
	}
	
	/**
	 * Finds the path to the file where will be stored the data
	 * @param id
	 * @param canCreate if true, we can create the file if not existing
	 * @return string
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private String path(long id, boolean canCreate) throws FileNotFoundException, UnsupportedEncodingException
	{
		File f = new File("db");
		
		//We check the main directoy
		if(!f.exists())
			f.mkdir();
		
		//We check the main subdirectory
		long sub = id/(255*255*255);
		f = new File("db/"+String.valueOf(sub));
		if(!f.exists())
			f.mkdir();
						
		//Now we can use a file
		long filename = id/(255*255);
		f = new File("db/"+String.valueOf(sub)+"/"+filename+".db");
		if(!f.exists() && canCreate == true)
		{
			//We create the file if not existing if asked
			PrintWriter writer = new PrintWriter("db/"+String.valueOf(sub)+"/"+filename+".db", "UTF-8");
			writer.close();
		}
		
		return "db/"+String.valueOf(sub)+"/"+filename+".db";
	}
	
	/**
	 * Opens the given file to find the value associated to a given id
	 * @param filename
	 * @param id
	 * @return int or 0 if not found
	 */
	private int findValue(String filename, long id)
	{
		int idIntoFile = (int) (id % (255*255));
		int value = 0;
		
		//We check if the file exists at least
		File f = new File(filename);
		if(!f.exists())
			return 0;
		
		//We open the file
		FileInputStream file;
		try 
		{			
		    file = new FileInputStream(filename);

			//We read the file
			char current;
			char history[] = new char[4];	
		    try {
		    	int i = 0;
		    	int currentId = 0;
				while(file.available() > 0) {
				    current = (char) file.read();
				    history[i] = current;
				    
				    //We check if we are at the corresponding line
				    if(i == 3)
				    {
				    	 currentId = Integer.parseInt(Character.toString(history[0]))*255 + Integer.parseInt(Character.toString(history[1]));
				    	 if(currentId == idIntoFile)
				    	 {//We are at the correct place
				    		 value = history[2];
				    		 break;
				    	 }
				    }
				    
				    i = (i+1)%4;				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{			
			System.out.println("We tried to open the file: "+filename+". But file not found: "+e.getMessage());
		}
		
		//We return the value directly, if we didn't find it, we will just send back 0
		return value;
	}
	
	
	/**
	 * Save the value of a id into a file
	 * @param filename
	 * @param id
	 * @param value
	 */
	private void saveValue(String filename, long id, int value)
	{
		int idIntoFile = (int) (id % (255*255));
		String line = "";
				
		//We open the file
		FileInputStream file;
		try 
		{			
		    file = new FileInputStream(filename);

		    File tempFile = File.createTempFile("db/buffer."+System.currentTimeMillis(), ".tmp");
		    FileWriter fw = new FileWriter(tempFile);

			//We read the file
			char current;
			char history[] = new char[4];	
		    try {
		    	int i = 0;
		    	int currentId = 0;
				while(file.available() > 0) {
				    current = (char) file.read();
				    history[i] = current;
				    
				    //We check if we are at the corresponding line
				    if(i == 3)
				    {
				    	 currentId = Integer.parseInt(Character.toString(history[0]))*255 + Integer.parseInt(Character.toString(history[1]));
				    	 if(currentId == idIntoFile)
				    	 {//We are at the correct place, we can replace the character we just read
				    		 current = (char) value;
				    		 break;
				    	 }
				    }
				    
					fw.write(current);
				    
				    i = (i+1)%4;				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{			
			System.out.println("We tried to open the file: "+filename+". But file not found: "+e.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
