import java.util.Hashtable;
import java.util.regex.*;
import java.net.*;
import java.io.*;


public class BankServ {

    public static void main(String[] args) {

	ServerSocket sock = null;    // server's master socket
	InetAddress addr = null;     // address of server
	Socket cliSock = null;       // socket to the client
	DataInputStream strm = null; // stream used to read from socket
	Hashtable<Integer, Integer> accounts = new Hashtable<Integer, Integer>();


	System.out.println("Server starting.");

	// Create Socket
	try {
	    addr = InetAddress.getLocalHost();
	    sock = new ServerSocket(13059,3,addr);
	} catch (Exception e) {
	    System.out.println("Creation of ServerSocket failed.");
	    System.exit(1);
	}

	// Accept a connection
	try {
	    cliSock = sock.accept(); // accept a connection from client
	} catch (Exception e) {
	    System.out.println("Accept failed.");
	    System.exit(1);
	}

	try {
	    strm = new DataInputStream(cliSock.getInputStream());
	} catch (Exception e) {
	    System.out.println("Couldn't create socket input stream.");
	    System.exit(1);
	}

	// Read messages from client until the message 'E' is recieved.
	do {
	   try {
		char cmd = strm.readChar();
		System.out.println("Received: "+ cmd);
		int num = strm.readInt();
		System.out.println("Received: "+ num);
		// if (input.matches("[CR]<\\d+>|[WD]<\\d+,\\d+>")) {
		// 	switch (input.charAt(0)) {
		// 		case 'C':
		// 					break;
		// 		case 'R':	
		// 					break;
		// 		case 'W':	
		// 					break;
		// 		case 'D':	
		// 					break;
		// 		default:	break;
		// 	}
		// } else {
		// 	System.out.println("Command not recognized: " + input);
		// }
		} catch (Exception e) {
			System.out.println("Socket input failed.");
			System.exit(1);
	   }
	} while (true);

	// close the stream and sockets
	// try {
	//     reader.close();
	//     cliSock.close();
	//     sock.close();
	// } catch (Exception e) {
	//     System.out.println("Server couldn't close a socket.");
	//     System.exit(1);
	// }

	// System.out.println("Server finished.");

    }
}
