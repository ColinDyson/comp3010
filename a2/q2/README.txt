Created by 		Colin Dyson
Student Number	7683407

Setup
 - ensure the file mysql-connector-java-8.0.15.jar is located in the same directory as BankServ_Threaded.java
 - run setenv CLASSPATH /home/student/dysonc/comp3010/a2/q2/mysql-connector-java-8.0.15.jar:
 	(Changing the path to the proper path on your system) to allow for JDBC usage
 - 

Compilation:
 - javac BankServ_Threaded.java

Operation:
 - log onto www3.cs.umanitoba.ca (Server location is hardcoded. Client may be run from any machine.)
 - mysql -vvv --user=USERNAME --password=PASSWORD DBNAME < dbsetup.sql
 - this creates a DB with a table named "dysonc_BANKACCTS", which is the hard-coded name the java server uses to make updates.
 - java BankServ_Threaded
 - log onto aviary.cs.umanitoba.ca
 - python BankCli.py