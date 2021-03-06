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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
	private int maxStates = 2;
	private static Charset UTF8 = Charset.forName("UTF-8");
	private final static String PATH_BEG = "db/";
	
	private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock rl = rwl.readLock();
    private Lock wl = rwl.writeLock();
	private long[] classicComputations;
	private int[][] basicComputations;
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> buffer;
	
	private static final int maxDataInBuffer = 1024*1024*100;
	private static int numberDataInBuffer = 0;
	private static final boolean USE_BUFFER = true;
	
	private static long startingTime = 0;
	private static long specificTime = 0;
	private static long numberOfGettingSetting = 0;
	
	private static boolean checkFiles = true;
	
	private static Database instance = null;
	private static Object o = new Object();	

	private Database(){
		super();
		
		buffer = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer,Integer>>>>();
		startingTime = System.currentTimeMillis();
		this.initComputations();
		
		File db = new File(PATH_BEG);
		this.checkFiles = db.exists();
	}
	
	public void end()
	{
		long endingTime = System.currentTimeMillis();
		System.out.println("Total time: "+Math.round((endingTime-startingTime)/1000)+"s for "+numberOfGettingSetting+" operations");
		System.out.println("Specific time: "+Math.round(specificTime/1000000)+"ms for "+numberOfGettingSetting+" operations");
		
		numberOfGettingSetting = 0;
		startingTime = System.currentTimeMillis();
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
	@SuppressWarnings("unused")
	public void setValue(int state[][], int value)
	{
		String path;
		numberOfGettingSetting++;
		long start = System.nanoTime();
		
		//Small verification
		if(value < 0 || value > 127)
		{
			System.err.println("Invalid value given ("+value+"), it should be between 0 and 127!");
			if(value < 0)
				value = 0;
			else if(value > 127)
				value = 127;
		}
		
		//The general id
		long id = this.createId(state);	
					
		//We save the data now
		if(USE_BUFFER == true)
		{			
			//The path to the file, if we use the buffer, we don't need to ask to check if the file exists here!
			path = this.path(id, false);
			this.saveValueToBuffer(path, id, value);
			
			//if(numberDataInBuffer%10000 == 0)
			//	System.out.println("Data in buffer: "+numberDataInBuffer);
			
			//We verify if we don't need to save the buffer...
			if(numberDataInBuffer >= maxDataInBuffer)
				this.saveBufferToFiles();
		}
		else
		{
			//We directly save the value in the files
			path = this.path(id, true);
			this.saveValue(path, id, value);		
		}
		specificTime += (System.nanoTime() - start);	
	}
	
	/**
	 * Returns the value of a given state
	 * @param state the state which is a table 5x5
	 * @return int a value between 0 and 255
	 */
	@SuppressWarnings("unused")
	public int getValue(int state[][])
	{
		long id = this.createId(state);		
		String path = this.path(id, false);
		numberOfGettingSetting++;
		long start = System.nanoTime();
		
		if(USE_BUFFER == true)
		{
			int value = this.findValueInBuffer(path, id);
			
			//if value not found, maybe the buffer didn't get it, so we need to verify if there is not already a file with the data
			if(value == -1)
			{
				if(this.checkFiles == false)
					return 0;
				else
				{
					value = this.findValue(path, id);
					//if(value != 0)
					//	System.out.println("Found a value != 0 in files: "+value);

					//Even if we got 0 (which means 'no data' or simply '0') we save it into the buffer to avoid needing to open the associated file next time... The db increases faster, but the setValue() will be faster too
					this.saveValueToBuffer(path, id, value);
				}
			}			
			
			//As simply as that
			specificTime += (System.nanoTime() - start);	
			return value;
		}
		else
		{
			specificTime += (System.nanoTime() - start);	
			return this.findValue(path, id);		
		}
	}
	
	/**
	 * For debug only
	 */
	public void debug()
	{
		System.out.println("Max value of a long: "+Long.MAX_VALUE);
		
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
		System.out.println("Saving value for the id "+id1+": 25.");
		path2 = this.path(id2, true);
		this.saveValue(path2, id2, 2);
		System.out.println("Saving value for the id "+id2+": 2.");
				
		//For getting value
		System.out.println("Getting the value for the id "+id1+": "+this.findValue(path1, id1));
		System.out.println("Getting the value for the id "+id2+": "+this.findValue(path2, id2));
		
		//General setValue() and getValue()
		System.out.println("Setting value for the id "+id2+": 13.");
		this.setValue(state, 13);
		System.out.println("Getting value for the id "+id2+": "+this.getValue(state) + " (before saving the buffer, if USE_BUFFER = true)");	
		System.out.println("Getting value for the id "+id2+": "+this.findValue(path2, id2) + " (using the files directly, so we should have 2 as we previously saved the value 2 in the file)");	
		this.saveBufferToFiles();
		System.out.println("Getting value for the id "+id2+": "+this.getValue(state) + " (after saving the buffer manually)");
		
		//Getting the value
		id2 = 2023708051;
		path2 = this.path(id2, true);
		this.saveValue(path2, id2, 2);
		System.out.println("Saving value for the id "+id2+": 2.");
		
	}
	
	/**
	 * Pre-computes some values for the ids buffer
	 */
	private void initComputations()
	{
		int j=0;
		
		classicComputations = new long[5];
		for(j=0; j < 5; j++)
			classicComputations[j] = (long)(Math.pow(242+1, j));
		
		basicComputations = new int[5][3];
		for(j=0; j < 5; j++)
			for(int k=0; k < 3; k++)
				basicComputations[j][k] = (int) (k * (Math.pow(this.maxStates+1, j)));
	}
	
	/**
	 * Creates the id for a given state
	 * @param state
	 * @return long 
	 */
	private long createId(int state[][])
	{
		long id = 0;
		int i=0, temp=0, j=0;

		for( ; i < 5; i++)
			id += (basicComputations[0][state[i][0]] + basicComputations[1][state[i][1]] + basicComputations[2][state[i][2]] + basicComputations[3][state[i][3]] + basicComputations[4][state[i][4]]) * classicComputations[i];
		
		/*// Initial algo:		
		for( ; i < 5; i++, temp=0)
		{
			for(j=0; j < 5; j++)
				temp += state[i][j] * Math.pow(this.maxStates+1, j);
			id += temp * Math.pow(242+1, i);//2*3^0 + 2*3^1 + 2*3^2 + 2*3^3 + 2*3^4 = 242.
		}
		//*/
		
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
			return PATH_BEG+String.valueOf((id/(255*255*255))/255)+"/"+String.valueOf(id/(255*255*255))+"/"+id/(255*255)+".db";
		
		//We check the main directoy
		File f = new File(PATH_BEG.substring(0, PATH_BEG.length()-1));
		if(!f.exists())
			f.mkdir();
		
		//We check the main sub-directory (we cannot divide directly by 255*255*255*255 it's too big and we got a negative number)
		long sub = (id/(255*255*255))/255;
		f = new File(PATH_BEG+String.valueOf(sub));
		if(!f.exists())
			f.mkdir();
				
		//We check the  sub-sub-directory
		long subsub = id/(255*255*255);
		f = new File(PATH_BEG+String.valueOf(sub)+"/"+String.valueOf(subsub));
		if(!f.exists())
			f.mkdir();
						
		//Now we can use a file
		long filename = id/(255*255);
		f = new File(PATH_BEG+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db");
		if(!f.exists())
		{
			//We create the file if not existing
			PrintWriter writer;
			try {
				writer = new PrintWriter(PATH_BEG+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db", "UTF-8");
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return PATH_BEG+String.valueOf(sub)+"/"+String.valueOf(subsub)+"/"+filename+".db";
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
				e.printStackTrace();
			}
		    
		    try {
				fr.close();
			} catch (IOException e) {
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
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{			
			System.out.println("We tried to open the file: "+filename+". But file not found: "+e.getMessage());
		}
	}
	
	/**
	 * Saves multiples values in a file
	 * @param filename 
	 * @param fileData
	 * @return void
	 */
	private void saveMultipleValues(String filename, HashMap<Integer, Integer> fileData)
	{
		//rl.lock();
		Integer lineValue;
		Integer lineKey;

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
		    	int i = 0, j=0;
		    	int currentId = 0;

			    current = fr.read();
				while(current != -1)
				{
				    history[i] = current;
				    
				    //We check if we are at one line we need to change
				    if(i == 2)
				    {				    	
				    	currentId = history[0]*255 + history[1];
				    	 
				    	 //We check each (id, value) we need to save
				 		for(Entry<Integer, Integer> lineEntry : fileData.entrySet()) 
				 		{
				 			lineValue = lineEntry.getValue();
				 			lineKey = lineEntry.getKey();
				 			
				 			if(currentId == lineKey)//We are at the right place
					    	{//We are at the correct place, we can replace the character we just read. We cannot stop the loop anyway
					    		 current = lineValue;

					    		 //We need to delete the entry now
					    		 //fileData.remove(lineKey);
					    		//wl.unlock();
					    		 fileData.put(lineKey, -1);
					    		//wl.lock();
					    	}
				 		}
				 		
				    }
				    
					fw.write(current);
				    
				    i = (i+1)%3;	
				    current = fr.read();
				}
				
				//If some values were not found in the file, we add them now
				if(fileData.size() > 0)
				{
					for(Entry<Integer, Integer> lineEntry : fileData.entrySet()) 
			 		{
			 			lineValue = lineEntry.getValue();
			 			lineKey = lineEntry.getKey();
			 			
			 			if(lineValue != -1)
			 			{
			 				fw.write((int)Math.floor(lineKey/255.0));
			 				fw.write(lineKey%255);
			 				fw.write(lineValue);
			 			}
			 		}
				}
				
				//Now we replace the file
				fw.close();
				fr.close();
				if(!file.delete())
					System.out.println("Impossible to delete the old file...");
			    if(!tempFile.renameTo(file))
			    	System.out.println("Impossible to rename the temporary file...");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{			
			System.out.println("We tried to open the file: "+filename+". But file not found: "+e.getMessage());
		}	

		//wl.unlock();
       	//rl.unlock();
	}
	
	/**
	 * Saves the entire buffer
	 */
	public void saveBufferToFiles()
	{
		HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> sub;
		HashMap<Integer, HashMap<Integer, Integer>> subsub;
		HashMap<Integer, Integer> intermediaryFileData;
		HashMap<Integer, Integer> fileData;
		HashMap<Integer, Integer> data, dataCopy;
		Integer subKey, subsubKey, intermediaryFileDataKey, fileDataKey, lineKey, lineValue;
		long id=0L, idPart1=0L, idPart2=0L;
		int valueSaved=0, total=0;
		String path = "";
		
		if(USE_BUFFER == false)
		{
			System.out.println("No buffer used, all the data are already saved.");
			return;
		}
					
		System.out.println("Saving buffer...");
		
		//We list all the data in the general buffer attribut
		for(Entry<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>> subEntry : buffer.entrySet()) 
		{			
			sub = subEntry.getValue();
			subKey = subEntry.getKey();
			
			for(Entry<Integer, HashMap<Integer, HashMap<Integer, Integer>>> subsubEntry : sub.entrySet()) 
			{
				subsub = subsubEntry.getValue();
				subsubKey = subsubEntry.getKey();
				
				for(Entry<Integer, HashMap<Integer, Integer>> fileDataEntry : subsub.entrySet()) 
				{
					fileData = fileDataEntry.getValue();
					fileDataKey = fileDataEntry.getKey();
										
					if(fileDataKey == -1)
						System.err.println("Error in saveBufferToFiles().");
					
					//We verify first if we the filename already exists (and we create it if not)
					idPart1 = fileDataKey;
					idPart1 = idPart1*255;
					idPart2 = idPart1*255;
					id = idPart2 + 1;					
					path = this.path(id, true);//METTRE TRUE APRES DEBUG					
					
					if(!path.equals(PATH_BEG+subKey+"/"+subsubKey+"/"+fileDataKey+".db"))
						System.out.println(path+" != "+PATH_BEG+subKey+"/"+subsubKey+"/"+fileDataKey+".db");
					
					//We ask to save the different ids and their value in the file
					dataCopy = new HashMap<Integer, Integer>(fileData);
					this.saveMultipleValues(path, fileData);						
					
					//TO DELETE
					
					for(Entry<Integer, Integer> lineEntry : dataCopy.entrySet()) 
			 		{
			 			lineValue = lineEntry.getValue();
			 			lineKey = lineEntry.getKey();
			 			total++;
			 			idPart1 = fileDataKey;
						idPart1 = idPart1*255;
						idPart2 = idPart1*255;
						id = idPart2 + lineKey;
						path = this.path(id, false);
						valueSaved = this.findValue(path, id);
						if(valueSaved != lineValue)
							System.err.println("Value to save: "+lineValue+" and value really saved: "+valueSaved);
			 		}
				}
			}
		}
		
		buffer = null;
		System.gc();
		buffer = new HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>>();
		this.checkFiles = true;
		numberDataInBuffer = 0;
		System.out.println("Buffer saved ("+total+" entries)!");
	}
	
	/**
	 * Gets the value of a id from the buffer. Returns -1 (and NOT 0) if nothing found, this is important!
	 * @param filename
	 * @param id
	 * @return int or -1 if not found
	 */
	private int findValueInBuffer(String filename, long id)
	{
		Integer idIntoFile = (int) (id % (255*255));
		
		String temp[] = filename.split("/");
		
		Integer first = Integer.valueOf(temp[temp.length-3]);
		Integer second = Integer.valueOf(temp[temp.length-2]);
		String[] last = temp[temp.length-1].split("\\.");
		Integer third = Integer.valueOf(last[0]);
		
		//The "directories"
		if(!buffer.containsKey(first))
			return -1;
		
		HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> sub = buffer.get(first);
		
		if(!sub.containsKey(second))
			return -1;
		
		HashMap<Integer, HashMap<Integer, Integer>> subsub = sub.get(second);
		
		if(!subsub.containsKey(third))
			return -1;
		
		//The "file"		
		HashMap<Integer, Integer> fileData = subsub.get(third);
		
		if(fileData.containsKey(idIntoFile))
			return fileData.get(idIntoFile);
		
		//Nothing found.
		return -1;
	}
	
	/**
	 * Save the value of a id into a buffer
	 * @param filename
	 * @param id
	 * @param value
	 */
	private void saveValueToBuffer(String filename, long id, int value)
	{
		Integer idIntoFile = (int) (id % (255*255));
		
		String temp[] = filename.split("/");
		
		Integer first = Integer.valueOf(temp[temp.length-3]);
		Integer second = Integer.valueOf(temp[temp.length-2]);
		String[] last = temp[temp.length-1].split("\\.");
		Integer third = Integer.valueOf(last[0]);
								
		//The "directories"
		if(!buffer.containsKey(first))
			buffer.put(first, new HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>());
		
		HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> sub = buffer.get(first);
		
		if(!sub.containsKey(second))
			sub.put(second, new HashMap<Integer, HashMap<Integer, Integer>>());
		
		HashMap<Integer, HashMap<Integer, Integer>> subsub = sub.get(second);
		
		if(!subsub.containsKey(third))
			subsub.put(third, new HashMap<Integer, Integer>());
		
		//The "file"
		HashMap<Integer, Integer> fileData = subsub.get(third);		
		if(!fileData.containsKey(idIntoFile))
			numberDataInBuffer++;

		//We save the data
		fileData.put(idIntoFile, value);		
	}
}
