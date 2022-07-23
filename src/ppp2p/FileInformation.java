package ppp2p;
import java.io.File;
import java.util.*;

/**
*     This class saves the information of the shared files,we use this class to transfer
*     file information between client and server
**/


public class FileInformation extends Vector<Object>
{
	
	private static final long serialVersionUID = 1L;

	public FileInformation(String filePath, String fileName, Long fileSize, String type,Date lastUpdateTime, String fileLocation,String fileId)
	{	
		this.add(fileName);
		this.add(fileSize);
		this.add(type);
		this.add(lastUpdateTime);
		this.add(fileLocation);
		this.add(filePath);
		this.add(fileId);
	}

	
	public boolean equal(String filePath, String fileName, Long fileSize, String type,Date lastUpdateTime, String fileLocation, String FileId)
	{
		return this.getFileName().equals(fileName) 
		&&(this.getFilePath().equals(filePath))
		&&(this.getFileSize().equals(fileSize))
		&&(this.getType().equals(type))
		&& this.getLastUpdateTime().equals(lastUpdateTime)
		&& this.getFileLocation().equals(fileLocation)
		&& this.getFileId().equals(FileId);
		
	}
	
	// check files to see if they are the same
	public boolean equal(FileInformation file)
	{
		return equal( file.getFilePath(), file.getFileName(), file.getFileSize(), file.getType(), file.getLastUpdateTime(), file.getFileLocation(), file.getFileId());
	}
	public void setFileId(String fileId) {
		this.setElementAt(fileId, 6);
	}

	public String getFileId() {
		return (String)this.elementAt(6);
	}
	
	public void setFileName(String fileName) {
		this.setElementAt(fileName, 0);
	}

	public String getFileName() {
		return (String)this.elementAt(0);
	}

	public void setFileSize(Long fileSize) {
		this.setElementAt(fileSize, 1);
	}

	public Long getFileSize() {
		return (Long)this.elementAt(1);
	}

	public void setType(String type) {
		this.setElementAt(type, 2);
	}

	public String getType() {
		return (String)this.elementAt(2);
	}
	
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.setElementAt(lastUpdateTime, 3);
	}

	public Date getLastUpdateTime() {
		return (Date)this.elementAt(3);
	}
	
	public void setFileLocation(String fileLocation) {
		this.setElementAt(fileLocation, 4);
	}

	public String getFileLocation() {
		return (String)this.elementAt(4);
	}

	public void setFilePath(String filePath) {
		this.setElementAt(filePath, 5);
	}

	public String getFilePath() {
		return (String)this.elementAt(5);
	}
	
	public String getFullPath(){
		return getFilePath() + File.separator + getFileName();
	}
}
