//Created by:	Colin Dyson
//Student #:	7683407

import java.io.*;
import java.util.regex.*;
import java.net.*;

public class BankCli_DG {

    public static void main(String[] args) {

    	DatagramSocket inSock = null;
		DatagramSocket outSock = null;             // client's socket
		DatagramPacket outPacket = null;
		DatagramPacket inPacket = null;
		byte[] inBuff = null;						// datagram packet buffer
		byte[] outBuff = null;
		InetAddress addr = null;         		// addr of server (local host for now)
		BufferedReader stdin = null;

		System.out.println("Client starting.");

		// set up terminal input
		try {
		    stdin = new BufferedReader(new InputStreamReader(System.in));  
		} catch (Exception e) {
		    System.out.println("Failed to create terminal reader.");
		    System.exit(1);
		}

		try {
			addr = InetAddress.getByName("cormorant.cs.umanitoba.ca");
			outSock = new DatagramSocket();
			inSock = new DatagramSocket(13059);
		} catch (Exception e) {
			System.out.println("Failed to create datagram socket.");
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
				//Send datagram to server
				try {
					inBuff = input.getBytes();
					outPacket = new DatagramPacket(inBuff, inBuff.length, addr, 13059);
					outSock.send(outPacket);
				} catch (Exception e) {
				    System.out.println("Failed to send datagram.");
				    System.exit(1);
				}	
				//Recieve datagram from server
				try {
					inBuff = new byte[128];
					inPacket = new DatagramPacket(inBuff, inBuff.length);
					inSock.receive(inPacket);
					response = new String(inPacket.getData(), 0, inPacket.getLength());
					System.out.println("[Server]: " + response);
				} catch (Exception e) {
					System.out.println("Failed to receive server response.");
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