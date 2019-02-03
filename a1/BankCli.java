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
	    addr = InetAddress.getByName("owl.cs.umanitoba.ca");
	    sock = new Socket(addr, 13059); // create client socket
	} catch (Exception e) {
	    System.out.println("Creation of client's Socket failed.");
	    System.exit(1);
	}

	// set up terminal input and socket output streams
	
	try {
	    instrm = new InputStreamReader(System.in);
	    stdin = new BufferedReader(instrm);
	    sockStrm = new DataOutputStream(sock.getOutputStream());
	} catch (Exception e) {
	    System.out.println("Socket output stream failed.");
	    System.exit(1);
	}

	// read and send integers over the socket until zero is entered
	String input;

	try {
		input = stdin.readLine();
	} catch (Exception e) {
		System.out.println("Terminal read failed.");
		System.exit(1);
	}

	while(!input.equals("E")) {
		// Input verification
		if (input.matches("[CR]<\\d+>|[WD]<\\d+,\\d+>")) { 
			switch(input.charAt(0)) {
				case 'C': 	create();
							break;
				case 'R': 	retrieve();
							break;
				case 'D': 	deposit();
							break;
				case 'W': 	withdraw();
							break;
				default:	break;
			}
		} else {
			System.out.println(input.charAt(0) + " is not a recognized command.");
		}
	}

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

    private void create(int acctNum) {

    }

    private void retrieve(int acctNum) {

    }

    private void deposit(int acctNum, int ammount) {

    }

    private void withdraw(int acctNum, int ammount) {
    	
    }
}