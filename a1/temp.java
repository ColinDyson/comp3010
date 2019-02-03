//
// Skeleton code for implementing a multi-threaded server using Java stream
// sockets. Your class will need a 'main' method in which the core server loop
// is coded and a 'run' method which will be executed for each thread
// created. Since threads are created for each client connection, the run
// method will contain the actual service code. Thus, after accepting a
// connection and getting a client socket, the main code will need to create
// and start a Thread for that client and pass it the client socket so it
// know who to communicate with while providing the service. To support
// Threads, your class must implement the 'Runnable' interface. 
//

import java.net.*;
import java.io.*;


public class SERVERCLASS implements Runnable {

    Socket cliSock = null;  // socket for each client
							// *** This is instantiated per client whenever
							// a new Thread is created

    SERVERCLASS(Socket csocket) {// constructor called by server for each client
	this.cliSock=csocket;
    } // end constructor


    public static void main(String[] args) {// The method run when the
	// server is started from the command line

	ServerSocket sock = null;    // server's master socket
	InetAddress addr = null;     // address of server
	Socket cli = null;           // client socket returned from accept

	System.out.println("Server starting.");

	// Create main ServerSocket
	try {
	    addr = InetAddress.getLocalHost();
	    sock = new ServerSocket(YOURPORTHERE,3,addr); // create server socket:
	} catch (Exception e) {
	    System.err.println("Creation of ServerSocket failed.");
	    System.exit(1);
	} // end try-catch

	// Loop forever accepting client connections
	while (1==1) {
	    // Accept a connection
	    try {
		cli = sock.accept(); // accept a connection from client,
		// returns socket to client
	    } catch (Exception e) {
		System.err.println("Accept failed.");
		System.exit(1);
	    } // end try-catch
	    
	    // Create a thread for this client (which starts our run() method
	    new Thread(new SERVERCLASS(cli)).start();
	} // end while - accept

	// Will never get here - its a server!

    } // end main


    public void run() {// The method run by each thread when it is started

	// any Thread local variables are declared here

	// provide the service over the connection using cliSock (declared above).
	// don't forget all the necessary try-catch blocks.

	// close the socket
	try {
	    cliSock.close();
	} catch (Exception e) {
	    System.err.println("couldn't close client socket.");
	    System.exit(1);
	} // end try-catch
    } // end run

} // end class:SERVERCLASS