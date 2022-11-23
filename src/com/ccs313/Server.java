package com.ccs313;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;


public class Server extends Thread {
    private static HashMap<String,Client> authorisedClients = new HashMap<String,Client>();
    private Client connectedClient;
    private Socket s;
    int hashCode;

    public Server(Socket s) {//pass the values to class variables.
        this.s = s;
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
        byte loopTime = 1;
        int passLength = 0;

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
                loopTime = 13;
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
//            Console con = System.console();
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
            String amountStr;
            float deposit;

            String menuKey;
            authorisedClients.put("nuyun99",new Client("Nuyun","Pabasara","991781757V","nuyun99","123".hashCode(),0));

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
                            out.println("Account Creation Successful");
                            //connectedClient = new Client("Nuyun","Pabasara","991781757V","nuyun99","123".hashCode(),0);
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
                menuKey =in.readLine();
                if (menuKey.toLowerCase(Locale.ROOT).equals("quit")){
                    out.println("LogOut from Server");
                    out.flush();
                    s.close();
                }else if(menuKey.toLowerCase(Locale.ROOT).equals("1")){
                    out.println("Account No : "+connectedClient.getAccountNumber());
                    out.println("Balance : "+connectedClient.getBalance());
                }else if(menuKey.toLowerCase(Locale.ROOT).equals("2")){

                    //connectedClient.deposit(amount,pass)

                }else if(menuKey.toLowerCase(Locale.ROOT).equals("3")){

                }else if(menuKey.toLowerCase(Locale.ROOT).equals("4")){

                }
            }

//
//            Item.nameSet.add(name);
//            while (s.isConnected()) {
//                if (Main.a.matches("Remaining BID-Time for not extended items 0:0:0")) {
//                    out.println("\nRemaining BID-Time for not extended items is Over"); // display the remaining time for not extended items.
//                } else {
//                    out.println("\n" + Main.a + "\r");
//                }
//                out.print("\nOK " + name + ", Please Enter the symbol of the item that you want to bid : ");
//                out.flush();
//
//                String symbol;
//                for (symbol = in.readLine().toUpperCase();  ; symbol = in.readLine().toUpperCase()) { // get symbol from user.
//                    if (symbol.equalsIgnoreCase("quit"))
//                        s.close();
//                    else if(!item_map.containsKey(symbol)) {
//                        out.println("-1 , Symbol is invalid.Try again");// Display when symbol is invalid.
//                        out.flush();
//                    }else {
//                        out.println("\nYes " + name + ", The CURRENT PRICE of the " + symbol + " item is : " + item_map.get(symbol).get_price()+"\n");
//                        out.println(name + ", Do you want to Bid on this item ? (Type with \"yes\" or \"no\")");
//                        out.flush();
//                        String exist;
//                        for (exist = in.readLine(); !exist.equalsIgnoreCase("no") && !exist.equalsIgnoreCase("yes") ; exist = in.readLine()) {
//                            if(exist.equalsIgnoreCase("quit")) {
//                                s.close();
//                            }
//                            out.println("Enter valid Input!!");// if enter another value display invalid input.
//                            out.flush();
//                        }
//                        if(exist.equalsIgnoreCase("yes")){ // break the loop and go for bidding.
//                            break;
//                        }
//                        out.print("\nOK " + name + ", Please Enter the symbol of the item that you want to bid : ");
//                        out.flush();
//                    }
//
//
//                }
//
//                Item item = item_map.get(symbol);
//                out.println("\nPlease Wait.......");
//                out.flush();
//                synchronized (item) { //wait others when accessing same item.
//
//                    out.print("\nPlease enter your price to bid : ");
//                    out.flush();
//                    String price = "0";
//                    int excessBytesDuringWait = s.getInputStream().available();
//                    if (excessBytesDuringWait > 0) {
//                        s.getInputStream().skip(excessBytesDuringWait);
//                        System.out.println(excessBytesDuringWait);
//                    }
//                    try {
//                        for (price = in.readLine(); !price.equals("quit") && Float.parseFloat(price) <= item.get_price(); price = in.readLine()) {//Check the entered price is higher than current price.
//
//                            out.print("\nError: Hi " + name + ", The price you entered must be more than the current price of the item. Note that the current price of " + symbol + " is " + item.get_price());
//                            out.print("\nPlease re-enter your price to bid : ");
//                            out.flush();
//                        }
//                        if (price.equals("quit"))
//                            s.close();
//                    } catch (NumberFormatException e) {//if there is a number format exception.
//                        out.println("\nError You entered an invalid value for price. Exiting the Auction server ....Try again");
//                        out.flush();
//                        s.close();
//                    }
//
//                    out.println("\nOk " + name + ",Your price accepted. Please enter 'confirm' and press enter to confirm bidding.");
//                    out.println("Or enter 'quit' and press enter to quit bidding.");
//                    out.flush();
//
//                    for (String confirm = in.readLine(); !confirm.equals("confirm"); confirm = in.readLine()) { //get confirmation about bidding from user.
//                        if (confirm.equals("quit"))
//                            s.close();
//                        out.println("Error input: Hi " + name + ", Enter 'confirm' and press enter to confirm bidding.");
//                        out.println("Or enter 'quit' and press enter to quit bidding.");
//                        out.flush();
//                    }
//
//                    if (!item_map.get(symbol).timeOut) {
//                        item_map.get(symbol).set_Name(name);
//                        if (item_map.get(symbol).make_bid(Float.parseFloat(price)) == 0) {//Display corresponding output according to error code.
//                            out.println("\nCongratulations " + name + ", Your bid saved successfully.");
//                            out.println("Current Price in " + symbol + " is " + price + ".");
//                            item_map.get(symbol).make_bid(Float.parseFloat(price));// do the bidding function.
//                            sleep(500);// sleep the thread 0.5 seconds. Then no more users can't made bid within 500ms.
//                        } else {
//                            out.println(name + ",Your bid is expired. Due to TimeOut ");// Display bid is expired.
//                        }
//                    } else {
//                        out.println(name + ",Your bid is expired. Due to TimeOut");// Display bid is expired.
//                    }
//                    out.println("\nThank You for using Stock Exchange Server."); // exit the server.
//                    out.println("==================================================================");
//                    out.flush();
//                }
//
        } catch (IOException iOException) {
            this.s.close(); //if try block fails close the connection.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
