//Created by:	Colin Dyson
//Student #:	7683407

import java.rmi.*;

public interface BankIface extends Remote {
	
public String request(String input) throws RemoteException;
}