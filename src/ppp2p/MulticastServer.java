package ppp2p;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




public class MulticastServer extends Thread implements SocketMessengerConstants{

	
	private static MulticastSocket socket;

	private static boolean flag = true;
	
	public static List<Node> nodeList = new ArrayList<Node>();
	public static Lock lock = new ReentrantLock();
	
	public void run(){
		
		try{
			socket = new MulticastSocket(MULTICAST_PORT);
			socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));

			String Ip = InetAddress.getLocalHost().getHostAddress();
			String info = new String(Ip + " "+ P2PServer.DEFAULT_SERVER_PORT);
			
                     
            byte Buffer [] = info.getBytes();		
			byte Buf [] = new byte[1024];
			
			DatagramPacket sdp = 
				new DatagramPacket(Buffer,Buffer.length,InetAddress.getByName(MULTICAST_ADDRESS),MULTICAST_PORT);
		    	
		       DatagramPacket rdp = 
		    	new DatagramPacket (Buf,Buf.length);
		    
		    while(flag){
						try {
							
							socket.send(sdp);
							socket.receive(rdp);
							String rec = new String (rdp.getData(),0,rdp.getLength());
					    	String [] data = rec.trim().split(" ");
					    	Node node = new Node(data[0],Integer.parseInt(data[1]));
					    	synchronized(nodeList){
					    		if(nodeList!=null&&nodeList.size()>0){
							    	if(getNode(data[0])==null){
							    		nodeList.add(node);
							    		System.out.println(nodeList.size());
							    	}
						    	}else{
						    		nodeList = new LinkedList<Node>();
						    		//lock.lock();
						    		nodeList.add(node);
						    		//lock.unlock();
						    	}
					    	}
					    	
					    	try{
					    		Thread.sleep(500);
					    	}
					    	catch(InterruptedException e){
					    		System.out.println(e.getMessage());
					    	}
							
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
					}
			  
		}
		catch (IOException e){
			System.out.println( e.getMessage());
		}
		
		
	}
	
	public static void CloseSocket(){
		try{
			socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS));
			socket.close();
		}
		catch(UnknownHostException e){	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public  Node getNode(String host){
		Node temp = null;
		for(int i =0;i<nodeList.size();i++){
			if(nodeList.get(i).getHost().equals(host))
				temp=nodeList.get(i);
		}
		return temp;
	}
	

		
	
}
