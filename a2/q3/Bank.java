import java.rmi.*;
import java.rmi.server.*;
import java.util.Hashtable;
import java.util.regex.*;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class Bank extends UnicastRemoteObject implements BankIface {
	static Hashtable<Integer, Integer> accounts = new Hashtable<Integer, Integer>();

	public Bank() throws RemoteException {
	}

	public String request(String input) throws RemoteException {
		String status = null;
		if (input.matches("S|[CR]<\\d+>|[WD]<\\d+,\\d+>")) {
			switch (input.charAt(0)) {
				case 'C':	status = create(	Integer.parseInt(input.substring(input.indexOf('<') + 1, input.indexOf('>'))));
							break;
				case 'R':	status = retrieve(	Integer.parseInt(input.substring(input.indexOf('<') + 1, input.indexOf('>'))));
							break;
				case 'W':	status = withdraw(	Integer.parseInt(input.substring(input.indexOf('<') + 1, input.indexOf(','))),
												Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf('>'))));
							break;
				case 'D':	status = deposit(	Integer.parseInt(input.substring(input.indexOf('<') + 1, input.indexOf(','))),
												Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf('>'))));
							break;
                case 'S':   status = summary();
				default:	break;
			}
		}
		else {
			status = new String("\'" + input + "\' is not a recognized command.");
		}

		return status;
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

    static synchronized String summary() {
        String status = "";
        System.out.println("Retrieving Accounts Summary.");

        Set set = accounts.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            status += (entry.getKey() + " : " + entry.getValue() + "\n");
        }

        return status;
    }
}