//Created by:	Colin Dyson
//Student #:	7683407

import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.Hashtable;

public class BankServ_DG implements Runnable{

	DatagramPacket cliPacket = null;
	static Hashtable<Integer, Integer> accounts;

    BankServ_DG(DatagramPacket cliPacket) {	// constructor called by server for each client
		this.cliPacket = cliPacket;
    }

    public static void main(String[] args) {

		DatagramSocket sock = null;    		// server's master socket
		DatagramPacket packet = null;
		byte[] inputBuffer = null;
		InetAddress addr = null;
		accounts = new Hashtable<Integer, Integer>();

		System.out.println("Server starting.");

		// Create master Socket
		try {
		    sock = new DatagramSocket(13059);
		} catch (Exception e) {
		    System.out.println("Creation of server datagram socket failed.");
		    System.exit(1);
		}

		while(true) {
		// Accept a connection (can only be force quit for now)
			inputBuffer = new byte[32];
			try {
			    packet = new DatagramPacket(inputBuffer, inputBuffer.length);
			    sock.receive(packet);
			} catch (Exception e) {
			    System.out.println("Socket Recieve failed.");
			    System.exit(1);
			}

			new Thread(new BankServ_DG(packet)).start();	//Start a new thread for each client we accept
		}
	}

	public void run() {
		String status = null; // Status string to return to client
		String message = null;
		InetAddress clientAddr = null;
		DatagramSocket cliSock;

		try {
			message = new String(cliPacket.getData(), 0, cliPacket.getLength());
			clientAddr = cliPacket.getAddress();
			cliSock = new DatagramSocket();
		} catch (Exception e) {
			System.out.println("Failed to read packet." + e);
			System.exit(1);
		}

		switch (message.charAt(0)) {
			case 'C':	status = create(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))));
						break;
			case 'R':	status = retrieve(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))));
						break;
			case 'W':	status = withdraw(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf(','))),
											Integer.parseInt(message.substring(message.indexOf(',') + 1, message.indexOf('>'))));
						break;
			case 'D':	status = deposit(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf(','))),
											Integer.parseInt(message.substring(message.indexOf(',') + 1, message.indexOf('>'))));
						break;
			default:	break;
		}

		try {
			byte[] buff = status.getBytes();
			cliSock = new DatagramSocket();
			DatagramPacket outPacket = new DatagramPacket(buff, buff.length, clientAddr, 13059);
			cliSock.send(outPacket);
		} catch (Exception e) {
			System.out.println("Failed to send return message.");
			System.exit(1);
		}
	}

    static synchronized String create(int acctNumber) {
    	String status = null;
    	System.out.println("Creating accout " + acctNumber);
    	if (!accounts.containsKey(acctNumber)) {
    		accounts.put(acctNumber, 0);
    		status = new String("Account #" + acctNumber + " successfully created.");
    	} else {
    		status = new String("Error: An account with that number already exists.");
    	}

    	return status;
    }

    static synchronized String retrieve(int acctNumber) {
    	String status = null;
    	System.out.println("Retrieving account " + acctNumber);
    	Integer balance = accounts.get(acctNumber);

    	if (balance != null) {
    		status = new String("Balance of Account #" + acctNumber + ": $" + balance);
    	} else {
    		status = new String("Error: Account #" + acctNumber + " does not exist");
    	}

    	return status;
    }

    static synchronized String deposit(int acctNumber, int ammount) {
    	String status = null;
    	System.out.println("Depositing $" + ammount + " into Account #" + acctNumber);

    	if (accounts.containsKey(acctNumber)) {
    		Integer oldBalance = accounts.get(acctNumber);
    		accounts.replace(acctNumber, oldBalance, oldBalance + ammount);
    		status = new String("Deposited $" + ammount + " into Account #" + acctNumber + ". New Balance: $" + (oldBalance + ammount));
    	} else {
    		status = new String("Error: Account #" + acctNumber + " does not exist");
    	}

    	return status;
    }

    static synchronized String withdraw(int acctNumber, int ammount) {
    	String status = null;
    	System.out.println("Withdrawing $" + ammount + " from Account #" + acctNumber);

    	if (accounts.containsKey(acctNumber)) {
    		Integer oldBalance = accounts.get(acctNumber);
    		if (ammount <= oldBalance) {
    			accounts.replace(acctNumber, oldBalance, oldBalance - ammount);
    			status = new String("$" + ammount + " withdrawn from Account #" + acctNumber + ". New Balance: $" + (oldBalance - ammount));
    		} else {
    			status = new String("Error: Insufficient Funds. Account #" + acctNumber +" balance: " + oldBalance);
    		}
    	} else {
    		status = new String("Error: Account #" + acctNumber + " does not exist");
    	}

    	return status;
    }
}