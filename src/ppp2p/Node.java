package ppp2p;

public class Node {

	
	public String host;
	public int port;
	
	public Node(String host, int port){
		this.host = host;
		this.port= port;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
