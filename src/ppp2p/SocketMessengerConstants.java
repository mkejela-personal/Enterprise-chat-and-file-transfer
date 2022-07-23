package ppp2p;

public interface SocketMessengerConstants
  {
     // address for multicast datagrams                         
     public static final String MULTICAST_ADDRESS = "239.255.63.132";

     // port for listening for multicast datagrams           
     public static final int MULTICAST_PORT = 60000;
 
     // port for Socket connections to MessengerServer
     public static final int SERVER_PORT = 56666;           

           
  } // end interface SocketMessengerConstants
