package ppp2p;
/**
* HashTable	class definition for vector implementation of a hash table
* 
* This class is used to hash the FileInformation class
*/

import java.util.*;			//Needed for Vector



/**
 class HashTable
*/

public class HashTable     {

    private static final int DefaultTableSize = 11;
	public final static int HASH_BY_FILE_NAME=1;               
	public static final int HASH_BY_FILE_LOCATION=2;

    private Vector [] table;	//The vectors
    private int size;			//Number of vectors in hash table
    private int items;			//Number of items stored in hash table
	private int hashBy;
	
    
   //Create a hash table of default size
    public HashTable()
    { 
		this(DefaultTableSize,1);
	}
    	
    public HashTable(int hashBy)
    { 
		this(DefaultTableSize,hashBy);
	}
   //Create a hash table of a user-defined size
   // n is the specified size of the hash table
    public HashTable(int n,int hashBy)
    {
		// Initialize the buckets
		table = new Vector[n];
		this.hashBy = hashBy;
		
		// Initialize each bucket a new vector
		for (int i=0; i<n; i++)
		{
			table[i] = new Vector<Object>();
		}
		// Initialize the size
		size = n;
		// Initialize the number of terms
		items = 0;
	}
    	
   //hash -- obtain hash code for object using division method
	//map the specified Object x to the specified value m
    private final static int hash(Object x,  int m)
    {
       String str = x.toString(); // Change it into a string
	   int key = 0;               // Initialize a local integer
	   // Give each string a standard hash method
	   // figure out the key of each string
	   for (int i=0 ; i<str.length(); i++)
	   { 
		  key = 37 * key + str.charAt(i); 
	   }
	   // If key is positive, return the former one
	   // if negative, return the latter one
	   return ( (key>0)?(key%m):(0-key%m) );
    }//end hash
	
	
    //isEmpty -- determine whether hash table is empty

    public boolean isEmpty()
    { 
		return size()==0;   // If no items, means empty
	}

   //hashCode -- get hash code for an object  
    public int hashCode(Object x)
    {
		if(hashBy == this.HASH_BY_FILE_NAME)
			return hash(((FileInformation)x).getFileName(), size); // Call method hash
		else
			return hash(((FileInformation)x).getFileLocation(), size);
	}
    	
    //size -- get number of entries in hash table
    public int size()
    { 
		return items;   // Return the number of items in the table
	}

  //find -- determine whether item is in hash table (updates bucket)
    public boolean findItem(Object x)
    {
		// If the bucket contains the object x, return true
		// else , return false , meaning no such object
		Vector temp = table[hashCode(x)];

		for (int i=0;i<temp.size() ;i++ )
		{	
			if(((FileInformation)(temp.elementAt(i))).equal((FileInformation)x));
				return true;
		}

		return false;
    }//end find
	
	public Vector findFileByFileName(String file)
	{
		if(hashBy != HASH_BY_FILE_NAME)
		{
			System.out.println("This hash table is hash by file location");
			return null;
		}

		Vector result = new Vector();
		Vector temp = table[hash(file,size)];
		
		FileInformation fileInformation;
		
		for (int i=0;i<temp.size() ;i++ )
		{
			fileInformation =(FileInformation) temp.elementAt(i);

			if(fileInformation.getFileName().equals(file))
			{
				result.addElement(fileInformation);
			}
		}

		return result;
	}

	public Vector findFileByFileLocation(String location)
	{
		if(hashBy != HASH_BY_FILE_LOCATION)
		{
			System.out.println("This hash table is hash by file name");
			return null;
		}

		Vector result = new Vector();
		Vector temp = table[hash(location,size)];
		
		FileInformation fileInformation;
		//UIManager.getColor("control")
		//UIManager.getColor("controlShadow")
		for (int i=0;i<temp.size() ;i++ )
		{
			fileInformation =(FileInformation) temp.elementAt(i);

			if(fileInformation.getFileName().equals(location))
			{
				result.addElement(fileInformation);
			}
		}

		return result;
	}
	
	public Vector getFileList()
	{
		Vector result = new Vector();
		
		
		for (int i=0;i<size ;i++ )
		{
			for (int j=0;j<table[i].size() ;j++ )
			{
				FileInformation fileInformation =(FileInformation) table[i].elementAt(j);

				result.addElement(fileInformation);
							
			}
		}

		return result;
	}
    //flush -- empty the hash table
    public void flush()
    { 
		// Use the in-built method to remove all the elements in the buckets
		for (int i=0; i<size; i++)
		{
			table[i].removeAllElements();
		}
    }//end flush

    
    //insertItem -- place an object into the hash table & increment item count
    public void insertItem(Object x)
    {
		// If not yet an item, add the object, and add one to the number of items
		// if already an item, do nothing
		if ( !findItem(x) )
		{
			table[hashCode(x)].addElement(x);// Use the in-built Vector method addElement
			items++;

		}
    }//end insertItem


    //removeItem -- remove an object from the hash table & decrement item count
    public void removeItem(Object x)
    {
		// If already an item, then remove the object, and minus one to the number of items
		// if not yet an item, do nothing
    	if ( findItem(x) )
    	{
			table[hashCode(x)].remove(x);// Use the in-built Vector method remove
			items--;
		}
    }//end removeItem

} //end class HashTable
