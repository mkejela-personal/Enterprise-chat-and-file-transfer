package ppp2p;

/**
 * P2P File System
 *
 */
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;



public class Downloader extends Thread {
	public final int DEFAULT_CLIENT_PORT = 56666;
	private FileInformation serverFile;
	private String path;
	private Socket socket = null;
	private DataInputStream in = null;
	private File File = null;
	private File localFile = null;
	
	byte[] fileData = new byte[4096];

	int byteNumber = 0;

	// Get the related information of the indicated file the client wants to download
	public Downloader(FileInformation serverFile, String path) {
		this.serverFile = serverFile;
		this.path = path;
		System.out.println(path);
	}
	
	public void run() {
		try {
			
			if(serverFile.getFileLocation().equals(InetAddress.getLocalHost().getHostAddress())){
				File file = new File(serverFile.getFullPath());
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(path);
				int len;
				byte[] buf = new byte[4096];
				while((len = fis.read(buf,0,buf.length))>0){
					fos.write(buf,0,len);
				}
			}else{
				socket = new Socket(serverFile.getFileLocation(), DEFAULT_CLIENT_PORT);
				ObjectOutputStream obj = new ObjectOutputStream(socket.getOutputStream());
				obj.writeObject("find");
				obj.flush();
				System.out.println(serverFile.getFileId()+" "+serverFile.getFileLocation()+" "+serverFile.getFileName()+" "+serverFile.getFilePath()+" "+serverFile.getFullPath()+" "+serverFile.getType()+" "+serverFile.getFileSize());
				obj.writeObject(serverFile);
				obj.flush();
				in = new DataInputStream(socket.getInputStream());
				DataOutputStream outwrite = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
				localFile = new File(path);
				System.out.println(path);
				
					System.out.println(localFile.getPath());
					int len;
					byte[] buf = new byte[4096];
					while((len=in.read(buf, 0, buf.length))>0){
						System.out.println(len);
						outwrite.write(buf, 0, len);
					}
				outwrite.close();				
				in.close();
				obj.close();
				socket.close();
			}
			
		} 
			catch (IOException e) {
			if (File.exists())
				File.delete();
		}
	}

}
