package ppp2p;
/**
 *An Index server.  This contains an index of all the files that are being
 *shared and their locations.  The key problems for this component are:
 *How to construct a robust index that can be searched as quickly as
 *possible.
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class P2PServer extends Thread {
	// default port number
	public static final int DEFAULT_SERVER_PORT = 56666;

	// server socket
	private ServerSocket p2pServer;

	// hash table to save the hashed data
	public HashTable hashTableByFileName, hashTableByFileLocation;

	// default constructor
	public P2PServer() {

		try {
			// initiate the server socket
			p2pServer = new ServerSocket(DEFAULT_SERVER_PORT);

			// set up the hash table
			hashTableByFileName = new HashTable(HashTable.HASH_BY_FILE_NAME);
			hashTableByFileLocation = new HashTable(
					HashTable.HASH_BY_FILE_LOCATION);

			start();
			System.out.println("server Started!");
		} catch (Exception e) {

		}
	}

	public void run() {
		while (true) {

			try {
				// set up an socket to deal with the client's request
				Socket connectionSocket = p2pServer.accept();

				// create a new thread		
				connectionSocket.close();
			} catch (Exception e) {
			}
		}
	}
}

// server Thread to dealt with the client's request, this is an inner class
class ServerThread extends Thread {
	Socket socket;

	ServerSocket server_socket;

	ObjectOutputStream output;

	ObjectInputStream input;

	HashTable hashTableByFileName, hashTableByFileLocation;


	public void run() {

		String message;
		try {

			server_socket = new ServerSocket(56666);

			while (true) {
				socket = server_socket.accept();
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());

				// read message from the client
				message = input.readObject().toString();
				System.out.println("message:" + message);

				// the client wants to publish a shared file
				if (message.equalsIgnoreCase("publish")) {
					FileInformation fileInformation = (FileInformation) input
							.readObject();
					System.out.println("publish");

					// insert the item to the hash table

					if (hashTableByFileName == null) {
						hashTableByFileName = new HashTable();
					}
					if (hashTableByFileLocation == null) {
						hashTableByFileLocation = new HashTable();
					}
					hashTableByFileName.insertItem(fileInformation);
					hashTableByFileLocation.insertItem(fileInformation);
					P2PClient.serverResourceTable.addFile(fileInformation);
					// answer to the client
					output.writeObject("success");
					output.close();
					input.close();
					socket.close();

				}
				// the client want to get the shared file list
				else if (message.equalsIgnoreCase("sync")) {

				    
					System.out.println("Sync requested!");
					int noF = P2PClient.clientResourceTable.getRowCount();
					System.out.println(noF);
					
					output.writeInt(noF);
					output.flush();
					for( int row = 0;row<noF;row++){
					FileInformation file = P2PClient.clientResourceTable.getFile(row);
						output.writeObject(file);
						output.flush();
					}
					output.writeObject("success");
					output.close();
					input.close();
					socket.close();


				}
				// the client wants to delete sth from the server
				else if (message.equalsIgnoreCase("delete")) {

					FileInformation fileInformation = (FileInformation) input
							.readObject();
					System.out.println("remove");

					hashTableByFileName.removeItem(fileInformation);
					hashTableByFileLocation.removeItem(fileInformation);
					P2PClient.serverResourceTable
							.removeFileByInf(fileInformation);
					// answer to the client
					output.writeObject("success");
					output.close();
					input.close();
					socket.close();

				}				
				// find method to find the needed file
				else if (message.startsWith("find")) {
					FileInformation fileInformation = (FileInformation) input
							.readObject();
					String path = fileInformation.getFullPath();
					File file = new File(path);
					if (file.exists()) {
						DataInputStream infile = new DataInputStream(
								new BufferedInputStream(new FileInputStream(
										path)));
						DataOutputStream output = new DataOutputStream(socket
								.getOutputStream());
						byte[] buf = new byte[2048];
						int len;
						while ((len = infile.read(buf, 0, buf.length)) > 0) {
							System.out.println(len);
							output.write(buf, 0, len);
						}

						output.close();
						infile.close();
						socket.close();
					}

				} 
				
				
				else {
					output.writeObject("error");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
};
