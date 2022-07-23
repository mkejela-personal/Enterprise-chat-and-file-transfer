package ppp2p;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;


import rmi_chat.*;
import Server.*;
/**
 * P2P File System
 */
public class P2PClient extends JFrame implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
        private Client_UI chatbox = new Client_UI();

	private long mouseTime;

	private JSplitPane jspMain, jspLocal;

	private JTextField  jtfLocalPath;

	private JButton jbtnSearch, jbtnRefresh, jbtnDown, jbtnUp, jbtnAdd,jbtnChat,
			jbtnDel, jbtnSync;

	private LocalFileTable localFileTable;

	public static ResourceFileTable serverResourceTable, clientResourceTable;
	
	private boolean flag = true;

	private ObjectOutputStream output;

	private ObjectInputStream input;

	private JLabel jlblState;

	private List <FileInformation> results = new ArrayList <FileInformation> ();


	public static void main(String[] args) {
		new P2PClient();
		Thread thread = new ServerThread();
		thread.start();
		MulticastServer ms = new MulticastServer();
	    ms.start();
	    
	}

	public P2PClient() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		/*
		 * Initialize server resource
		 */

		JToolBar jtbServer = new JToolBar();

		jbtnDown = new JButton("Fetch");
		jbtnDown.addActionListener(this);
		jbtnDown.setEnabled(true);
		jtbServer.add(jbtnDown);
                
		jbtnChat = new JButton("Chat");
		jbtnChat.addActionListener(this);
		jbtnChat.setEnabled(true);
		jtbServer.add(jbtnChat);
                
		jbtnSync = new JButton("Sync");
		jbtnSync.addActionListener(this);
		jbtnSync.setEnabled(true);
		jtbServer.add(jbtnSync);

		jbtnSearch = new JButton("Search");
		jbtnSearch.addActionListener(this);
		jtbServer.add(jbtnSearch);
		 
		JPanel jpnlServer = new JPanel();
		jpnlServer.setLayout(new BorderLayout());
		jlblState = new JLabel("Connected!");
		serverResourceTable = new ResourceFileTable();

		jpnlServer.add(jtbServer, BorderLayout.NORTH);
		jpnlServer.add(new JScrollPane(serverResourceTable),
				BorderLayout.CENTER);
		jpnlServer.add(jlblState, BorderLayout.SOUTH);

		/*
		 * Initialize client resource
		 */

		JToolBar jtbLocal = new JToolBar();

		jtfLocalPath = new JTextField();
		jtbLocal.add(jtfLocalPath);
		jtfLocalPath.addActionListener(this);

		localFileTable = new LocalFileTable();
		localFileTable.goToDir(".");
		localFileTable.addMouseListener(this);
		this.setLocalPath();

		jbtnUp = new JButton("up");
		jbtnUp.addActionListener(this);
		jtbLocal.add(jbtnUp);

		jbtnAdd = new JButton("Insert");
		jbtnAdd.addActionListener(this);
		jtbLocal.add(jbtnAdd);

		JPanel jpnlLocalFile = new JPanel();
		jpnlLocalFile.setLayout(new BorderLayout());
		jpnlLocalFile.add(jtbLocal, BorderLayout.NORTH);
		jpnlLocalFile.add(new JScrollPane(localFileTable), BorderLayout.CENTER);
		
		clientResourceTable = new ResourceFileTable();
		
		JToolBar jtbLocalResource = new JToolBar();
		jbtnDel = new JButton("Delete");
		jbtnDel.addActionListener(this);
		jtbLocalResource.add(jbtnDel);
		
		JPanel jpnlLocalRescouce = new JPanel();
		jpnlLocalRescouce.setLayout(new BorderLayout());
		jpnlLocalRescouce.add(new JScrollPane(clientResourceTable), BorderLayout.CENTER);
		jpnlLocalRescouce.add(jtbLocalResource, BorderLayout.SOUTH);
		
		jspLocal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpnlLocalRescouce, jpnlLocalFile);
		jspLocal.setDividerLocation(300);

		jspMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpnlServer,
				jspLocal);
		jspMain.setDividerLocation(400);

		container.add(jspMain, BorderLayout.CENTER);
		
		this.setSize(800, 600);
		this.setTitle("P2P client");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (source == jbtnDown) {
			int row = serverResourceTable.getSelectedRow();
			if (row != -1) {
				if (serverResourceTable.isDirectory(row)) {
				} else if (true) {
					FileInformation fileinfo = (FileInformation) serverResourceTable
							.getFile(row);
					Downloader downloader = new Downloader(fileinfo,
					jtfLocalPath.getText()+File.separator+fileinfo.getFileName());
					downloader.run();
					localFileTable.refresh();
				}

			}
		} 

		else if (source == jbtnAdd) {
			this.addToServer();
		} else if (source == jbtnDel) {
			this.delFromServer();
		}
	    else if (source == jbtnSync) {
		this.sync();
	    }
		//path up
		else if (source == jbtnUp) {
			localFileTable.goToUpDir();
			setLocalPath();
		}
		//set local path
		else if (source == jtfLocalPath) {
			localFileTable.goToDir(jtfLocalPath.getText());
			setLocalPath();
		}
               /* else if (source=jbtnChat){
                    chatbox.getUsersPanel();
                }
*/
		else if (source == jbtnSearch) {
			String wts = "nothing";
			wts = new String(JOptionPane.showInputDialog("Enter the File Name or FileId you are looking for:"));
			System.out.println("Searching for: "+wts);
			
		    for(int i=0;i<serverResourceTable.getRowCount();i++){	
			   if(serverResourceTable.getFileName(i).equalsIgnoreCase(wts)
					   ||serverResourceTable.getFileId(i).equalsIgnoreCase(wts)){

				   System.out.println("File found :"+serverResourceTable.getFileInfo(i));
			
				   serverResourceTable.addRowSelectionInterval(i, i);
				   flag = false;
			    }
			   }
				    while(flag){
				    	JOptionPane.showMessageDialog(jbtnSearch, "Search returned no results");
				    	break;
				    }
		     }   
	}

// add to server function
	private void addToServer() {
		int row = localFileTable.getSelectedRow();
		if (row != -1) {
			if (localFileTable.isDirectory(row)) {
				localFileTable.selectDir(row);
				setLocalPath();
			} else if (true) {
				FileInformation fileinfo = (FileInformation) localFileTable
						.getFile(row);
				File file = new File(fileinfo.getFilePath()+File.separator+fileinfo.getFileName());
				if(file.exists()){
					fileinfo.setFileId(MD5Builder.getMD5(file));
					fileinfo.setLastUpdateTime(new Date());
				}
				if (clientResourceTable.fileExist(fileinfo) == -1) {
					clientResourceTable.addFile(fileinfo);

					try {
						synchronized (MulticastServer.nodeList){
							for(java.util.Iterator<Node> it = MulticastServer.nodeList.iterator();it.hasNext();){
								Node node = it.next();
								String host = node.getHost();
								int port = node.getPort();
								String localhost =InetAddress.getLocalHost().getHostAddress();
							
								if(host.equals(localhost)){
									serverResourceTable.addFile(fileinfo);
									
								}else{
									System.out.println(host+port);
									Socket nskt = new Socket(host,port);
									ObjectOutputStream opt = new ObjectOutputStream(nskt.getOutputStream());
									ObjectInputStream ipt = new ObjectInputStream(nskt.getInputStream());
									opt.writeObject("publish");
									opt.flush();
									opt.writeObject(fileinfo);
									opt.flush();
									String msg = (String) ipt.readObject();

								}
							}	
							
							}
						
					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (ClassNotFoundException ex) {
						ex.printStackTrace();
					}
				}
			}

		}
	}
//delete from server function
	private void delFromServer() {
		int row = clientResourceTable.getSelectedRow();
		if (row != -1) {
			if (clientResourceTable.isDirectory(row)) {
			} else if (true) {
				FileInformation fileinfo = (FileInformation) clientResourceTable
						.getFile(row);
				clientResourceTable.removeFile(row);


				try {
					serverResourceTable.removeFile(serverResourceTable
							.fileExist(fileinfo));
					synchronized (MulticastServer.nodeList){
						for(java.util.Iterator<Node> it = MulticastServer.nodeList.iterator();it.hasNext();){
							Node node = it.next();
							String host = node.getHost();
							int port = node.getPort();
							String localhost =InetAddress.getLocalHost().getHostAddress();
						
							if(host.equals(localhost)){
								serverResourceTable.removeFileByInf(fileinfo);
								
							}else{
								System.out.println(host+port);
								Socket nskt = new Socket(host,port);
								ObjectOutputStream opt = new ObjectOutputStream(nskt.getOutputStream());
								ObjectInputStream ipt = new ObjectInputStream(nskt.getInputStream());
								opt.writeObject("delete");
								opt.flush();
								opt.writeObject(fileinfo);
								opt.flush();
								String msg = (String) ipt.readObject();

							}
						}	
//							
						}
					
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}
		}

	}
	//sync function
private void sync(){
	
				try {
					synchronized (MulticastServer.nodeList){
						for(java.util.Iterator<Node> it = MulticastServer.nodeList.iterator();it.hasNext();){
							Node node = it.next();
							String host = node.getHost();
							int port = node.getPort();
							String localhost =InetAddress.getLocalHost().getHostAddress();
						
							if(!host.equals(localhost)){
								
								System.out.println(host+port);
								Socket nskt = new Socket(host,port);
								ObjectOutputStream opt = new ObjectOutputStream(nskt.getOutputStream());
								ObjectInputStream ipt = new ObjectInputStream(nskt.getInputStream());
								opt.writeObject("sync");
								opt.flush();	
								int count = ipt.readInt();
								for (int i =0;i<count;i++){
								FileInformation file = (FileInformation) ipt.readObject();
								serverResourceTable.addFile(file);
								System.out.println(file);
								}
								

							}
						}	
						
						}
					
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			
		}

	


	public void setLocalPath() {
		this.jtfLocalPath.setText(localFileTable.getDir());
	}

	public void mouseClicked(MouseEvent e) {
		long time = new Date().getTime();
		//double click
		if (time - mouseTime < 600) {
			addToServer();
		}
		mouseTime = time;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
