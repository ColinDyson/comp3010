//Created by:	Colin Dyson
//Student #:	7683407

import java.util.regex.*;
import java.rmi.*;

public class BankServ_RMI {
    public static void main(String[] args) {
		System.out.println("Server starting.");

		try {
			Naming.rebind("//cormorant.cs.umanitoba.ca:13059/Bank", new Bank());
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}
