//Created by:	Colin Dyson
//Student #:	7683407

import java.net.*;
import java.io.*;
import java.util.regex.*;

public class BankCli {

    public static void main(String[] args) {

		Socket sock = null;              		// client's socket
		InetAddress addr = null;         		// addr of server (local host for now)
		DataOutputStream outStream = null;		// stream used to write to socket
		DataInputStream inStream = null;		//stream used to read server response
		BufferedReader stdin = null;

		System.out.println("Client starting.");

		// set up terminal input
		try {
		    stdin = new BufferedReader(new InputStreamReader(System.in));  
		} catch (Exception e) {
		    System.out.println("Failed to create terminal reader.");
		    System.exit(1);
		}

		// read and send integers over the socket until zero is entered
		boolean exitCmd = false;
		String input = null;
		String response = null;
		do {
			try {
				input = stdin.readLine();
			} catch (Exception e) {
				System.out.println("Failed to read from terminal.");
				System.exit(1);
			}

			// Input verification
			if (input.matches("[CR]<\\d+>|[WD]<\\d+,\\d+>")) {
				//Connect to server
				try {
				    addr = InetAddress.getByName("cormorant.cs.umanitoba.ca");
				    sock = new Socket(addr, 13059);
				    outStream = new DataOutputStream(sock.getOutputStream());
			    	inStream = new DataInputStream(sock.getInputStream());
				} catch (Exception e) {
				    System.out.println("Failed to create socket.");
				    System.exit(1);
				}	

				try {
					outStream.writeUTF(input);
					response = inStream.readUTF();
					System.out.println("[Server]: " + response);
				} catch (Exception e) {
					System.out.println("Communication with the server has failed.");
					System.exit(1);
				}

				//Disconnect from server
				try {
					outStream.close();
					inStream.close();
					sock.close();
				} catch (Exception e) {
					System.out.println("Failed to close socket.");
					System.exit(1);
				}
			} else if (input.matches("E")) {
				exitCmd = true;
			} else {
				System.out.println(input + " is not a recognized command.");
			}
		} while(!exitCmd);

		// close the streams and  socket
		try {
		    stdin.close();
		} catch (Exception e) {
		    System.out.println("Client couldn't close terminal reader.");
		    System.exit(1);
		}

		System.out.println("Client finished.");
    }
}