//Created by:	Colin Dyson
//Student #:	7683407

import java.util.regex.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class BankServ_Threaded implements Runnable {

	Socket cliSock = null;  // socket for each client
							// *** This is instantiated per client whenever
							// a new Thread is created

	static String dbName = null;
	static String userName = null;
	static String pass = null;
	static String tableName = "dysonc_BANKACCTS";

    BankServ_Threaded(Socket csocket) {	// constructor called by server for each client
		this.cliSock = csocket;
    }

    public static void main(String[] args) {

		ServerSocket sock = null;    		// server's master socket
		InetAddress addr = null;     		// address of server
		Socket cli = null;					// socket returned from accept
		BufferedReader stdin = null;

		//Ensure user has entered dbname and password
		if (args.length < 2) {
			System.out.println("Error: Insufficient Arguments.");
			System.exit(1);
		}

		dbName = args[0];
		userName = args[1];

		System.out.println("Server starting.");

		//Prompt for password if not provided
		if (args.length < 3) {
			// set up terminal input
			try {
			    stdin = new BufferedReader(new InputStreamReader(System.in));  
			} catch (Exception e) {
			    System.out.println("Failed to create terminal reader.");
			    System.exit(1);
			}

			System.out.print("Enter Password: ");
			try {
				pass = stdin.readLine();
			} catch (Exception e) {
				System.out.println("Failed to read from terminal.");
				System.exit(1);
			}
			System.out.println();

			try {
				stdin.close();
			} catch (Exception e) {
				System.out.println("Failed to close terminal reader.");
				System.exit(1);
			}
		} else {
			pass = args[2];
		}

		// Create master Socket
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
			    cli = sock.accept();
			} catch (Exception e) {
			    System.out.println("Accept failed.");
			    System.exit(1);
			}

			new Thread(new BankServ_Threaded(cli)).start();	//Start a new thread for each client we accept
		}
	}

	public void run() {
		BufferedReader inStream = null; 	// stream used to read from socket
		PrintWriter outStream = null;	// stream used to write to socket
		String status = null;				// Status string to return to client
		Connection con = null;
		//Setup socket i/o
		try {
		    inStream = new BufferedReader(new InputStreamReader(cliSock.getInputStream()));
		    outStream = new PrintWriter(cliSock.getOutputStream(),true);
		} catch (Exception e) {
		    System.out.println("Couldn't create socket i/o streams.");
		    System.exit(1);
		}

		//Get JDBC driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Couldn't get the JDBC driver");
			e.printStackTrace();
			System.exit(44);
		}

		//Get connection to db
		try {
			String srvr = "127.0.0.1";
			String port = "3306";
			String url = "jdbc:mysql://" + srvr + ":" + port + "/" + dbName;

			con = DriverManager.getConnection(url, userName, pass);
		} catch (Exception e) {
			System.out.println("Connection to database failed.");
			e.printStackTrace();
			System.exit(55);
		}

		//Process message
		try {
			String message = inStream.readLine();
			// Input verification
			if (message.matches("[CR]<\\d+>|[WD]<\\d+,\\d+>")) {
				switch (message.charAt(0)) {
					case 'C':	status = create(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))), con);
								break;
					case 'R':	status = retrieve(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf('>'))), con);
								break;
					case 'W':	status = withdraw(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf(','))),
													Integer.parseInt(message.substring(message.indexOf(',') + 1, message.indexOf('>'))), con);
								break;
					case 'D':	status = deposit(	Integer.parseInt(message.substring(message.indexOf('<') + 1, message.indexOf(','))),
													Integer.parseInt(message.substring(message.indexOf(',') + 1, message.indexOf('>'))), con);
								break;
					default:	break;
				}
			} else {
				status = new String("\'" + message + "\' is not a recognized command.");
			}
			outStream.println(status);
		} catch (Exception e) {
			System.out.println("Socket i/o failed.");
			System.exit(1);
		}

		//close the stream and sockets
		try {
	    	inStream.close();
	    	outStream.close();
	    	cliSock.close();
	    	con.close();
		} catch (Exception e) {
	    	System.out.println("Server couldn't close a socket.");
	    	System.exit(1);
		}
	}

    static synchronized String create(int acctNumber, Connection con) {
    	String status = null;
    	int[] columns = {1, 2};
    	System.out.println("Trying to create accout " + acctNumber);

    	try {
    	Statement st = con.createStatement();
    	ResultSet rs = st.executeQuery(
    		"select 1 from " + tableName + " where acctnum = " + acctNumber +";");
    	if (rs.next()) {
    		status = new String("Error: An account with that number already exists.");
    	} else {
    		int result = st.executeUpdate(
    			"insert into " + tableName + "(acctnum,acctbalance) values (" + acctNumber + ", 0);", columns);
    		status = new String("Account #" + acctNumber + " successfully created.");
    	}
    	System.out.println(status);
    	st.close();
    	} catch (Exception e) {
	    	System.out.println("Error accessing database.");
	    	e.printStackTrace();
	    	System.exit(1);
    	}

    	return status;
    }

    static synchronized String retrieve(int acctNumber, Connection con) {
    	String status = null;
    	System.out.println("Retrieving account " + acctNumber);

    	try {
	    	Statement st = con.createStatement();
	    	ResultSet rs = st.executeQuery(
	    		"select * from " + tableName + " where acctnum = " + acctNumber + ";");
	    	if (!rs.next()) {
	    		status = new String("Error: Account #" + acctNumber + " does not exist");
	    	} else {
	    		status = new String("Balance of Account #" + rs.getInt(1) + ": $" + rs.getInt(2));
	    	}
	    	System.out.println(status);
	    	st.close();
	    } catch (Exception e) {
	    	System.out.println("Error accessing database.");
	    	e.printStackTrace();
	    	System.exit(1);
    	}

    	return status;
    }

    static synchronized String deposit(int acctNumber, int ammount, Connection con) {
    	String status = null;
    	System.out.println("Depositing $" + ammount + " into Account #" + acctNumber);

    	try {
	    	Statement st = con.createStatement();
	    	ResultSet rs = st.executeQuery(
	    		"select * from " + tableName + " where acctnum = " + acctNumber + ";");
	    	if (!rs.next()) {
	    		status = new String("Error: Account #" + acctNumber + " does not exist");
	    	} else {
	    		int oldBalance = rs.getInt(2);
	    		st.executeUpdate("update " + tableName + " set acctbalance = " + (oldBalance + ammount) + " where acctnum = " + acctNumber +";");
	    		status = new String("Deposited $" + ammount + " into Account #" + acctNumber + ". New Balance: $" + (oldBalance + ammount));
	    	}
	    	System.out.println(status);
	    	st.close();
    	} catch (Exception e) {
	    	System.out.println("Error accessing database.");
	    	e.printStackTrace();
	    	System.exit(1);
    	}

    	return status;
    }

    static synchronized String withdraw(int acctNumber, int ammount, Connection con) {
    	String status = null;
    	System.out.println("Withdrawing $" + ammount + " from Account #" + acctNumber);

    	try {
	    	Statement st = con.createStatement();
	    	ResultSet rs = st.executeQuery(
	    		"select * from " + tableName + " where acctnum = " + acctNumber + ";");
	    	if (!rs.next()) {
	    		status = new String("Error: Account #" + acctNumber + " does not exist");
	    	} else {
	    		int oldBalance = rs.getInt(2);
	    		if (oldBalance >= ammount) {
	    			st.executeUpdate("update " + tableName + " set acctbalance = " + (oldBalance - ammount) + " where acctnum = " + acctNumber +";");
	    			status = new String("$" + ammount + " Withdrawn from Account #" + acctNumber + ". New Balance: $" + (oldBalance - ammount));
	    		} else {
	    			status = new String("Error: Insufficient Funds. Account #" + acctNumber +" Balance: $" + oldBalance);
	    		}
	    	}
	    	System.out.println(status);
	    	st.close();
	    } catch (Exception e) {
	    	System.out.println("Error accessing database.");
	    	e.printStackTrace();
	    	System.exit(1);
    	}

    	return status;
    }
}
