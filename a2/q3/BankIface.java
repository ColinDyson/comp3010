import java.rmi.*;

public interface BankIface extends Remote {
	
public String request(String input) throws RemoteException;
}