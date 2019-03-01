//Created by:	Colin Dyson
//Student #:	7683407

import java.net.*;
import java.io.*;
import java.rmi.*;

public class BankCli_RMI {

    public static void main(String[] args) {

		BufferedReader stdin = null;
		BankIface bank = null;

		System.out.println("Client starting.");

		// set up terminal input
		try {
		    stdin = new BufferedReader(new InputStreamReader(System.in));  
		} catch (Exception e) {
		    System.out.println("Failed to create terminal reader.");
		    System.exit(1);
		}

		try {
			bank = (BankIface)Naming.lookup("//cormorant.cs.umanitoba.ca:13059/Bank");
		} catch (Exception e) {
			System.out.println(e);
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
				try {
					response = bank.request(input);
					System.out.println("[Server]: " + response);
				} catch (Exception e) {
					System.out.println("Communication with the server has failed.");
					e.printStackTrace();
					System.exit(1);
				}
			} else if (input.matches("E")) {
				exitCmd = true;
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