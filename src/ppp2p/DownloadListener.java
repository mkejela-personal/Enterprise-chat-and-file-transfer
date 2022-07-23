package ppp2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * P2P listener
 * 
 * 
 */
// A class to listen to requests from other host to establish socket and download
// file
public class DownloadListener extends Thread {
	// Default port set to the listener
	public final int DEFAULT_CLIENT_PORT = 56666;

	// A client socket wanting connect to listener side
	private Socket client;

	private ServerSocket listener;

	private DataInputStream in;

	private DataOutputStream out;

	private FileInputStream fileInStream;


	// Get the related information of the indicated file the client wants to
	// download
	public DownloadListener() {
		super();
		try {
			listener = new ServerSocket(DEFAULT_CLIENT_PORT);
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			// A new socket connection to the listener where file will be
			// downloaded
			do {
				client = listener.accept();
				
				ObjectInputStream obj = new ObjectInputStream(client.getInputStream());
				FileInformation fileinfo = (FileInformation)obj.readObject();
							
				

				// Enable/disable SO_TIMEOUT with the specified timeout, in
				// milliseconds.
				client.setSoTimeout(1000);

				in = new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());
				// Create a new file in client side
				File localfile = new File(fileinfo.getFullPath());


				// Create a new file input stream
				fileInStream = new FileInputStream(localfile);
				// Create a new file data array
				byte[] fileData = new byte[4096];
				// Read in the data
				int byteNumber;
				while ((byteNumber = fileInStream.read(fileData)) != -1) {
					// Write out the data
					out.write(fileData, 0, byteNumber);
					
				}
				// Close the file out stream
				fileInStream.close();
				// If file exists already
				// Close the in stream
				in.close();
				// Close the out stream
				out.close();
						
			} while (!listener.isClosed());
			
		}
		// Connecting listener error
		catch (SocketException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
