//Created by:	Colin Dyson
//Student #:	7683407

import java.util.Hashtable;
import java.util.regex.*;
import java.net.*;
import java.io.*;


public class BankServ {

	static Hashtable<Integer, Integer> accounts;
    public static void main(String[] args) {

		ServerSocket sock = null;    		// server's master socket
		InetAddress addr = null;     		// address of server
		Socket cliSock = null;       		// socket to the client
		DataInputStream inStream = null; 	// stream used to read from socket
		DataOutputStream outStream = null;	// stream used to write to socket
		accounts = new Hashtable<Integer, Integer>();
		String status = null;				// Status string to return to client

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
			    cliSock = sock.accept();
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
				outStream.writeUTF(status);
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
		}
    }

    static String create(int acctNumber) {
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

    static String retrieve(int acctNumber) {
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

    static String deposit(int acctNumber, int ammount) {
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

    static String withdraw(int acctNumber, int ammount) {
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
