package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

public class Database {
	private int maxStates = 2;
	private static Charset UTF8 = Charset.forName("UTF-8");
	
	private static Database instance = null;
	private static Object o = new Object();
	

	private Database(){
		super();
	}
	
	/**
	 * Return the only instance of the connection with the database.
	 * @return The only instance of the connection with the database.
	 */
	public static Database getInstance(){
		synchronized(o){
			if (instance == null) {
				instance = new Database();
			}
		}
		
		return instance;
	}
	
	/**
	 * Update the value of a given state
	 * @param state the state which is a table 5x5
	 * @param value the new value to save (value between 0 and 255)
	 */
	public void setValue(int state[][], int value)
	{
		//Small verification
		if(value < 0 || value > 127)
		{
			System.err.println("Invalid value given ("+value+"), it should be between 0 and 127!");
			if(value < 0)
				value = 0;
			else if(value > 127)
				value = 127;
		}

		long id = this.createId(state);		
		String path = this.path(id, true);
		this.saveValue(path, id, value);
	}
	
	/**
	 * Returns the value of a given state
	 * @param state the state which is a table 5x5
	 * @return int a value between 0 and 255
	 */
	public int getValue(int state[][])
	{
		long id = this.createId(state);		
		String path = this.path(id, false);
		return this.findValue(path, id);		
	}
	
	/**
	 * For debug only
	 */
	public void debug()
	{
		//For id creation
		int state[][] = new int[5][5];
		for(int i=0; i < state.length; i++)
			for(int j=0; j < state[i].length; j++)
				state[i][j] = 1;
		
		state[0][0] = 2;
		state[0][1] = 2;
		state[0][2] = 2;
		state[0][3] = 2;
		state[0][4] = 2;
		long id1 = this.createId(state);
		System.out.println("Model, creation id: "+id1);
		state[0][0] = 1;
		state[0][1] = 0;
		state[0][2] = 0;
		state[0][3] = 0;
		state[0][4] = 0;
		state[1][0] = 1;
		long id2 = this.createId(state);
		System.out.println("Model, creation id: "+id2);

		//For path creation
		String path1 = this.path(id1, false);
		System.out.println("Model, path for "+id1+": "+path1);
		String path2 = this.path(id2, false);
		System.out.println("Model, path for "+id2+": "+path2);

		//For saving value
		path1 = this.path(id1, true);
		this.saveValue(path1, id1, 25);
		System.out.println("Saving value for the id "+id1+".");
		path2 = this.path(id2, true);
		this.saveValue(path2, id2, 2);
		System.out.println("Saving value for the id "+id2+".");
				
		//For getting value
		System.out.println("Getting the value for the id "+id1+": "+this.findValue(path1, id1));
		System.out.println("Getting the value for the id "+id2+": "+this.findValue(path2, id2));
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
				temp += state[i][j] * Math.pow(this.maxStates+1, j);
			id += temp * Math.pow(242+1, i);//2*3^0 + 2*3^1 + 2*3^2 + 2*3^3 + 2*3^4 = 242.
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
	private String path(long id, boolean canCreate)
	{		
		if(canCreate == false)
			return "db/"+String.valueOf((id/(255*255*255))/255)+"/"+String.valueOf(id/(255*255*255))+"/"+id/(255*255)+".db";
		
		//We check the main directoy
		File f = new File("db");
		if(!f.exists())
			f.mkdir();
		
		//We check the main sub-directory (we cannot divide directly by 255*255*255*255 it's too big and we got a negative number)
		long sub = (id/(255*255*255))/255;
		f = new File("db/"+String.valueOf(sub));
		if(!f.exists())
			f.mkdir();
		
		//We check the  sub-sub-directory
		long subsub = id/(255*255*255);
		f = new File("db/"+String.valueOf(sub)+"/"+String.valueOf(subsub));
		if(!f.exists())
			f.mkdir();
						
		//Now we can use a file
		long filename = id/(255*255);
		f = new File("db/"+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db");
		if(!f.exists())
		{
			//We create the file if not existing
			PrintWriter writer;
			try {
				writer = new PrintWriter("db/"+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db", "UTF-8");
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "db/"+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db";
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
		try 
		{			
		    Reader fr = new InputStreamReader(new FileInputStream(filename), UTF8);

			//We read the file
			int current;
			int history[] = new int[3];	
		    try {
		    	int i = 0;
		    	int currentId = 0;
		    	current = fr.read();
				while(current != -1)
				{    
				    history[i] = current;
				    
				    //We check if we are at the corresponding line
				    if(i == 2)
				    {
				    	 currentId = history[0]*255 + history[1];
				    	 if(currentId == idIntoFile)
				    	 {//We are at the correct place
				    		 value = history[2];
				    		 break;
				    	 }
				    }
				    
				    i = (i+1)%3;
				    current = fr.read();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    try {
				fr.close();
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
		boolean found = false;
		
		//We open the file
		try 
		{			
			File file = new File(filename);
		    Reader fr = new InputStreamReader(new FileInputStream(filename), UTF8);
	        
		    File tempFile = new File("buffer");
		    Writer fw = new OutputStreamWriter(new FileOutputStream("buffer"), UTF8);	

			//We read the file
			int current;
			int history[] = new int[3];	
		    try {
		    	int i = 0;
		    	int currentId = 0;

			    current = fr.read();
				while(current != -1)
				{
				    history[i] = current;
				    
				    //We check if we are at the corresponding line
				    if(i == 2)
				    {				    	
				    	 currentId = history[0]*255 + history[1];
				    	 if(currentId == idIntoFile)
				    	 {//We are at the correct place, we can replace the character we just read. We cannot stop the loop anyway
				    		 current = value;
				    		 found = true;
				    	 }
				    }
				    
					fw.write(current);
				    
				    i = (i+1)%3;	
				    current = fr.read();
				}
				
				//If value not found, we add it
				if(found == false)
				{
					//There is a problem if we want to write 57150 (it becomes 63), but not when we write 47150. So, we need two chars to write the id
					fw.write((int)Math.floor(idIntoFile/255.0));
					fw.write(idIntoFile%255);
					fw.write(value);
				}
				
				//Now we replace the file
				fw.close();
				fr.close();
				if(!file.delete())
					System.out.println("Impossible to delete the old file...");
			    if(!tempFile.renameTo(file))
			    	System.out.println("Impossible to rename the temporary file...");
				
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
