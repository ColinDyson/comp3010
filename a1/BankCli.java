import java.net.*;
import java.io.*;
import java.util.regex.*;

public class BankCli {

    public static void main(String[] args) {

	Socket sock = null;              // client's socket
	InetAddress addr = null;         // addr of server (local host for now)
	DataOutputStream sockStrm = null;// stream used to write to socket
	InputStreamReader instrm = null; // terminal input stream
	BufferedReader stdin = null;     // buffered version of instrm

	System.out.println("Client starting.");

	// create socket
	try {
	    addr = InetAddress.getByName("cormorant.cs.umanitoba.ca");
	    sock = new Socket(addr, 13059); // create client socket
	} catch (Exception e) {
	    System.out.println("Creation of client's Socket failed.");
	    System.exit(1);
	}

	// set up terminal input and socket output streams
	
	try {
	    instrm = new InputStreamReader(System.in);
	    stdin = new BufferedReader(instrm);
	    // sockStrm = new DataOutputStream(sock.getOutputStream());
	} catch (Exception e) {
	    System.out.println("Socket output stream failed.");
	    System.exit(1);
	}

	// read and send integers over the socket until zero is entered
	boolean exitCmd = false;
	String input = null;
	do {
		try {
			input = stdin.readLine();
		} catch (Exception e) {
			System.out.println("Terminal read failed.");
			System.exit(1);
		}

		// Input verification
		if (input.matches("[CR]<\\d+>|E|[WD]<\\d+,\\d+>")) { //Input verification
			if (input.charAt(0) == 'E') {
				exitCmd = true;
			} else {
				try {
					sockStrm.writeChar(input.charAt(1));
					//sockStrm.writeInt(Integer.parseInt(input.substring(2, input.length()-1)));
				} catch (Exception e) {
					System.out.println("Socket output failed.");
					System.exit(1);
				}
			}
		} else {
			System.out.println(input + " is not a recognized command.");
		}
	} while(!exitCmd);

	// close the streams and  socket
	try {
		instrm.close();
	    stdin.close();
	    sockStrm.close();
	    sock.close();
	} catch (Exception e) {
	    System.out.println("Client couldn't close socket.");
	    System.exit(1);
	}

	System.out.println("Client finished.");
    }
}