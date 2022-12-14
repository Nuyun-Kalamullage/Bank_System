package com.ccs313;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;


public class Server extends Thread {
    private static HashMap<String,Client> authorisedClients = new HashMap<String,Client>();
    private static HashMap<String,Client> authorisedClientAccounts = new HashMap<String,Client>();
    private Client connectedClient;
    private Socket s;
    int hashCode;

    public Server(Socket s) {//pass the values to class variables.
        this.s = s;
    }

    public static HashMap<String, Client> getAuthorisedClientAccounts() {
        return authorisedClientAccounts;
    }

    public void run() {
        try {
            if (s.getLocalPort() == 2021) { // if  port is 2021.
                handle(); // execute the client side.
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();// display the error.

        }
    }


    public int verifyPassword(String name, Socket s,BufferedReader in ,PrintWriter out ) throws IOException, InterruptedException {
        out.println("");
        out.print(" Enter the Password : ");
        out.flush();
        int loopTime = 1;

        EraserThread et = new EraserThread("Thread Stared",out);
        Thread mask = new Thread(et);
        mask.start();

        for (String pass = in.readLine(); !pass.toLowerCase(Locale.ROOT).equals("quit") && loopTime < 4 ; ){
            mask.stop();
//            out.print(String.format("\033[%dA",1));
//            out.flush();
//            out.println("\rEntered Password : *******                           ");
//            out.flush();
            if (authorisedClients.get(name).getPassword() == pass.hashCode()){
                connectedClient = authorisedClients.get(name);
                loopTime = pass.hashCode();
                return 1;
            }else{
                loopTime++;
                out.print(" Enter Valid password : ");
                out.flush();
                mask = new Thread(et);
                mask.start();
                pass = in.readLine();
                mask.stop();
            }
            mask = new Thread(et);
            mask.start();
        }
        return 0;

    }


    private void handle() throws IOException {// Implement methods for Client-Sever

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); // get inputs from user.
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream())); // print outputs to user.
            out.println("==================================================================");
            out.println("===============  Welcome to MID BANK - WEB Server ================");
            out.println("=================== AA 1711 - NuYuN Pabasara =====================");
            out.println("==================================================================\n");
            out.println("Enter 'quit' and press enter, any time to quit from the console.");
            out.println("If you are a new user please Type create and press enter:)");
            out.print("Please Enter Your User ID : ");
            out.flush();
            String name;
            String Fname;
            String Lname;
            String nic;
            int password;
            String userAccount;
            String amountStr;
            float deposit;

            String menuKey;
            Client c1 = new Client("Nuyun","Pabasara","991781757V","nuyun99","123".hashCode(),0);
            authorisedClients.put("nuyun99",c1);
            authorisedClientAccounts.put(c1.getAccountNumber(),c1);

            for (name = in.readLine(); !name.toLowerCase(Locale.ROOT).equals("quit"); name = in.readLine()) { //Get the unique Name ID from user
                System.out.println(name);
                int returnValue = -1;
                if(authorisedClients.containsKey(name)){
                    returnValue = verifyPassword(name,s,in,out);
                }
                if (hashCode == "quit".hashCode()) {
                    out.println("LogOut from Server");
                    out.flush();
                    s.close();
                }else if(returnValue == 0){
                    out.println("Password limit reached. Please Login again");
                    out.print("Please Enter Your User ID : ");//print Name ID is not unique.}
                    out.flush();
                    returnValue = -1;
                }else if(returnValue == 1){
                    out.println("");
                    out.println("Login Successfully");
                    out.flush();
                    returnValue = -1;
                    break;
                } else if (name.toLowerCase(Locale.ROOT).equals("create")) {
                    out.print("First Name : ");
                    out.flush();
                    Fname = in.readLine();
                    out.print("Last Name : ");
                    out.flush();
                    Lname = in.readLine();
                    out.print("User Name : ");
                    out.flush();
                    name = in.readLine();
                    while (Client.usernameList.contains(name) || name.isEmpty()) {
                        out.print("Enter Valid User Name : ");
                        out.flush();
                        name = in.readLine();
                    }
                    out.print("NIC : ");
                    out.flush();
                    nic = in.readLine();
                    while (Client.NICList.contains(nic) || nic.isEmpty()) {
                        out.print("Enter Valid NIC : ");
                        out.flush();
                        nic = in.readLine();
                    }
                    out.print("Password : ");
                    out.flush();
                    password = in.readLine().hashCode();
                    out.print("Enter amount for deposit : ");
                    out.flush();
                    amountStr = in.readLine();
                    while (!amountStr.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                        out.print("Please Enter valid amount for deposit : ");
                        out.flush();
                        amountStr = in.readLine();
                    }
                    deposit = Float.parseFloat(amountStr);
                    out.print("Do you want to proceed the request (y/n) : ");
                    out.flush();
                    String key;
                    for (key = in.readLine();  !key.toLowerCase(Locale.ROOT).equals("quit") ; key = in.readLine()) {
                        System.out.println(key);
                        if (key.toLowerCase(Locale.ROOT).equals("y")) {
                            Client temporyClient = new Client(Fname, Lname, nic, name, password, deposit);
                            authorisedClients.put(name, temporyClient);
                            authorisedClientAccounts.put(temporyClient.getAccountNumber(),temporyClient);
                            out.println("Account Creation Successful");
                            out.print("Please Enter Your User ID to login: ");
                            temporyClient.showClient();
                            out.flush();
                            break;

                        }else if(key.toLowerCase(Locale.ROOT).equals("n")){
                            out.println("Account Creation Unsuccessful");
                            out.print("Please Enter Your User ID to login: ");
                            out.flush();
                            break;
                        }
                        out.println("Please Enter valid key");
                        out.flush();
                    }
                    if (key.equals("quit")) {
                        out.println("LogOut from Server");
                        out.flush();
                        s.close();
                    }

                    out.flush();
                }else{
                    out.println("Enter valid User Name. Please Try again");//print Name ID is not unique.
                    out.print("Please Enter Your User ID : ");
                    out.flush();
                }

            }
            if (name.equals("quit")) {
                out.println("LogOut from Server");
                out.flush();
                s.close();
            }
            //out.println("Running User Menu Down here");
            out.flush();
            while (s.isConnected()) {
                out.println("1.check Balance");
                out.println("2.Money Deposit");
                out.println("3.Money Withdraw");
                out.println("4.Money Transfer");
                out.println("Enter the the number do you want to proceed :");
                out.flush();
                synchronized (connectedClient) {
                menuKey = in.readLine();

                    if (menuKey.toLowerCase(Locale.ROOT).equals("quit")) {
                        out.println("LogOut from Server");
                        out.flush();
                        s.close();
                    } else if (menuKey.toLowerCase(Locale.ROOT).equals("1")) {
                        out.println("Account No : " + connectedClient.getAccountNumber());
                        out.println("Balance : " + connectedClient.getBalance());
                    } else if (menuKey.toLowerCase(Locale.ROOT).equals("2")) {
                        amountStr = null;
                        out.print("Enter amount for deposit : ");
                        out.flush();
                        amountStr = in.readLine();
                        while (!amountStr.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                            out.print("Please Enter valid amount for deposit : ");
                            out.flush();
                            amountStr = in.readLine();
                        }
                        Float amount = Float.parseFloat(amountStr);
                        if (verifyPassword(connectedClient.getUsername(), s, in, out) == 1) {
                            connectedClient.deposit(amount);
                            out.println("Deposit Successful:)");
                            out.println("Updated balance : Rs:" + connectedClient.getBalance() + "/=");
                        } else {
                            out.println("Password limit reached. Please Try again:(");
                        }
                        out.flush();


                    } else if (menuKey.toLowerCase(Locale.ROOT).equals("3")) {

                        amountStr = null;
                        int response = 0;
                        out.print("Enter amount for Withdraw : ");
                        out.flush();
                        amountStr = in.readLine();
                        while (!amountStr.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                            out.println("Current Balance : Rs:" + connectedClient.getBalance() + "/=");
                            out.print("Please Enter valid amount for Withdraw : ");
                            out.flush();
                            amountStr = in.readLine();
                        }
                        Float amount = Float.parseFloat(amountStr);
                        if (verifyPassword(connectedClient.getUsername(), s, in, out) == 1) {
                            response = connectedClient.withdraw(amount);
                            if (response == 1) {
                                out.println("Withdraw Successful:)");
                                out.println("Updated balance : Rs:" + connectedClient.getBalance()+"/=");
                            } else {
                                out.println("Insufficient money:(");
                            }
                        } else {
                            out.println("Password limit reached. Please Try again:(");
                            //out.print("Please Enter Your User ID : ");//print Name ID is not unique.}
                        }
                        out.flush();

                    } else if (menuKey.toLowerCase(Locale.ROOT).equals("4")) {

                        amountStr = null;
                        int response = 0;
                        out.print("Enter amount for Withdraw : ");
                        out.flush();
                        amountStr = in.readLine();
                        while (!amountStr.matches("[-+]?[0-9]*\\.?[0-9]+")) {
                            out.println("Current Balance : Rs:" + connectedClient.getBalance() + "/=");
                            out.print("Please Enter valid amount for Transfer : ");
                            out.flush();
                            amountStr = in.readLine();
                        }
                        Float amount = Float.parseFloat(amountStr);

                        out.print("Enter the user Account : ");
                        out.flush();
                        userAccount = in.readLine();
                        while (!Client.getAccountNumberList().contains(userAccount) || userAccount.isEmpty()) {
                            if (userAccount.toLowerCase(Locale.ROOT).equals("quit")) {
                                out.println("LogOut from Server");
                                out.flush();
                                s.close();
                            }
                            out.print("Enter Valid User Account : ");
                            out.flush();
                            userAccount = in.readLine();
                        }

                        if (verifyPassword(connectedClient.getUsername(), s, in, out) == 1) {
                            response = connectedClient.transfer(amount, userAccount);
                            if (response == 1) {
                                out.println("Transfer Successful:)");
                                out.println("Updated balance : Rs:" + connectedClient.getBalance()+"/=");
                            } else {
                                out.println("Insufficient money:(");
                            }
                        } else {
                            out.println("Password limit reached. Please Try again:(");
                            //out.print("Please Enter Your User ID : ");//print Name ID is not unique.}
                        }
                        out.flush();
                    }
                }
            }

        } catch (IOException iOException) {
            this.s.close(); //if try block fails close the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
