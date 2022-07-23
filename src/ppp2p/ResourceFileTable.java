package ppp2p;

import java.util.Date;
import java.util.Vector;


public class ResourceFileTable extends P2PFileTable{
	
	private static final long serialVersionUID = 1L;
	private Vector<FileInformation> fileVect;

	
	public ResourceFileTable() {
		super();
		// TODO Auto-generated constructor stub
	
		setHeaderVect(new String[] {  "Name", "Size", "Type", "Time", "Owner", "RemotePath", "FileId" });
	    SortableTableModel dm = new SortableTableModel()
	    {
	        
			private static final long serialVersionUID = 1L;
			public Class getColumnClass(int col) {
	    		switch (col){
	    		case 1:
	    			return Long.class;
	    		case 3:
	    			return Date.class;
	    		default:
	    			return String.class;
	    		}

	    	}
	    	
	    };
	    this.setModel(dm);
	    fileVect = new Vector<FileInformation>();
	}

    
	public void refresh() {
		setFileList(fileVect);
	}
	
	public void setFile(Vector data)
	{
		fileVect = data;
		refresh();
	}
	
	public Vector getFileInfo(int row) {
		return (Vector) fileVect.elementAt(row);
	}

	public String getFileName(int row) {
		String name = (String) getFileInfo(row).elementAt(0);
		return (name);
	}
	
	public String getFileId(int row) {
		String Id = (String) getFileInfo(row).elementAt(6);
		return Id;
	}

	public int getFileSize(int row)
	{
		return ((Integer)getFileInfo(row).elementAt(1)).intValue();
	}
	
	public int fileExist(String name)
	{
		int length = fileVect.size();
		for (int i = 0; i<length; i++)
		{
			if (((Vector)(fileVect.get(i))).get(0).equals(name))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int fileExist(FileInformation file){
		for (int i = 0; i < fileVect.size(); i++){
			if (((FileInformation)fileVect.get(i)).equals(file))
				return i;
		}
		return -1;
	} 
	
	public void removeFile(int row)
	{
		if (row == -1)
			return;
		this.fileVect.remove(row);
		refresh();
	}
	

	
	public void removeFileByInf(FileInformation f){
		for (int i = 0; i < fileVect.size(); i++){
			System.out.println(fileVect.get(i));
			if (f.getFileLocation().equals(fileVect.get(i).getFileLocation()) && f.getFileId().equals(fileVect.get(i).getFileId())){
				removeFile(i);
				System.out.println(i);
				
			}
		}
		
	}
	
	public void addFile(FileInformation file)
	{
		this.fileVect.add(file);
		refresh();
	}


	public boolean isDirectory(int row) {
		// TODO Auto-generated method stub
		return false;
	}
}
