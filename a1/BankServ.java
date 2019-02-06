import java.util.Hashtable;
import java.util.regex.*;
import java.net.*;
import java.io.*;


public class BankServ {

    public static void main(String[] args) {

		ServerSocket sock = null;    		// server's master socket
		InetAddress addr = null;     		// address of server
		Socket cliSock = null;       		// socket to the client
		DataInputStream inStream = null; 	// stream used to read from socket
		DataOutputStream outStream = null;	// stream used to write to socket
		BufferedReader stdin = null;
		Hashtable<Integer, Integer> accounts = new Hashtable<Integer, Integer>();

		System.out.println("Server starting.");

		// Create Socket
		try {
		    addr = InetAddress.getLocalHost();
		    sock = new ServerSocket(13059,3,addr);
		} catch (Exception e) {
		    System.out.println("Creation of server socket failed.");
		    System.exit(1);
		}

		while(true) {
		// Accept a connection (can only be force quit for now)
			try {
				System.out.println("Waiting for client to connect.");
			    cliSock = sock.accept();
			    System.out.println("Connected to client.");
			} catch (Exception e) {
			    System.out.println("Accept failed.");
			    System.exit(1);
			}

			try {
			    inStream = new DataInputStream(cliSock.getInputStream());
			    outStream = new DataOutputStream(cliSock.getOutputStream());
			} catch (Exception e) {
			    System.out.println("Couldn't create socket i/o streams.");
			    System.exit(1);
			}
			try {
				String message = inStream.readUTF();
				System.out.println("Received: "+ message);
				switch (message.charAt(0)) {
					case 'C':	create(Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))));
								break;
					case 'R':	retrieve(Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))));
								break;
					case 'W':	
								break;
					case 'D':	
								break;
					default:	break;
				}
				outStream.writeUTF("Message Received.");
			} catch (Exception e) {
				System.out.println("Socket i/o failed." + e);
				System.exit(1);
			}

			//close the stream and sockets
			try {
		    	inStream.close();
		    	outStream.close();
		    	cliSock.close();
			} catch (Exception e) {
		    	System.out.println("Server couldn't close a socket.");
		    	System.exit(1);
			}

			System.out.println("Transaction complete. Waiting for new client.");
		}
    }

    static void create(int acctNumber) {
    	System.out.println("Creating accout " + acctNumber);
    }

    static void retrieve(int acctNumber) {
    	System.out.println("Retrieving account " + acctNumber);
    }
}
