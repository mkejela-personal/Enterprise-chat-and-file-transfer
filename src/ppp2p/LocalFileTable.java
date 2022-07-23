package ppp2p;

/**
 *  P2P File System
 * 
 */

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;


public class LocalFileTable extends P2PFileTable {

	private static final long serialVersionUID = 1L;
	private File directory;
	private String localhost;
	
	public LocalFileTable() {
		super();
		// TODO Auto-generated constructor stub
		//header
		try {
			setLocalhost(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			setLocalhost("127.0.0.1");
			e.printStackTrace();
		}
		
		setHeaderVect(new String[] { "Name", "Size", "Type", "Time", "Owner", "LocalPath","FileId" });
		SortableTableModel dm = new SortableTableModel() {
			public Class getColumnClass(int col) {
				switch (col) {
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
		setDirectory(new File("."));
	}
	
	public void refresh() {
		setFileList(getFileList());
	}
	
	/**
	 * get current file list
	 * 
	 * */
	private Vector getFileList() {
		File[] files = getDirectory().listFiles();
		Vector fileVect = new Vector();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			FileInformation file = new FileInformation(this.getDir(), files[i].getName(), new Long(files[i]
					                                                       .length()), files[i].isDirectory() ? "<DIR>" : "file",
					                                                    		   new Date(files[i].lastModified()), getLocalhost(),"");
			
			fileVect.add(file);
		}
		return fileVect;
	}
	
	public String getDir() {
		try {
			return getDirectory().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void goToDir(String path) {
		goToDir(new File(path));
	}
	
	public void goToDir(File file) {
		setDirectory(file);
		refresh();
	}
	
	
	public void goToUpDir() {
		if (getDirectory().getParentFile() != null)
			setDirectory(getDirectory().getParentFile());
		refresh();
	}

	public void selectDir(int row) {
		FileInformation fileinfo = this.getFile(row);
		goToDir(fileinfo.getFullPath());
	}

	public boolean isDirectory(int row) {
		FileInformation fileinfo = (FileInformation)this.getFile(row);
		return fileinfo.getType().equals("<DIR>");
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}

	public String getLocalhost() {
		return localhost;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public File getDirectory() {
		try {
			return directory.getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return directory.getAbsoluteFile();
		}
	}
	
}
