package com.ccs313;

import java.net.Socket;
import java.util.HashSet;
import java.util.Random;

public class Client {
    private String FirstName;
    private String LastName;
    private String NIC;
    private String username;
    private int Password;//store hashcode password
    private String accountNumber;
    private float balance = 1000;
    private static HashSet<String> accountNumberList = new HashSet<>();
    protected static HashSet<String> usernameList = new HashSet<>();
    protected static HashSet<String> NICList = new HashSet<>();

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPassword() {
        return Password;
    }

    public void setPassword(int password) {
        Password = password;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public static HashSet<String> getAccountNumberList() {
        return accountNumberList;
    }

    public static void setAccountNumberList(HashSet<String> accountNumberList) {
        Client.accountNumberList = accountNumberList;
    }

    public Client(String firstName, String lastName, String nic, String username, int password, float balance) {
        FirstName = firstName;
        LastName = lastName;
        NIC = nic;
        this.username = username;
        Password = password;
        this.balance += balance;
        int acc = new Random().nextInt((9999 - 100) + 1) + 10;
        while(accountNumberList.contains(Integer.toString(acc))){
            acc = new Random().nextInt((9999 - 100) + 1) + 10;
        }
        this.accountNumber = Integer.toString(acc);
        accountNumberList.add(accountNumber);
        usernameList.add(username);
        NICList.add(NIC);
    }

    public void showClient(){
        String profile= "First Name : "+FirstName+"\nLast Name : "+LastName+"\nNIC no : "+NIC+"\nUser Name : "+username+"\nAccount Number : "+accountNumber+"\nBalance : "+balance+"\n";
        System.out.println(profile);

    }

    public void deposit(float amount){
            balance+=amount;
    }

    public int transfer(float amount, String accountNumber){
        if(amount <= balance){ //success
            balance-=amount;
            float transferUserBalance = Server.getAuthorisedClientAccounts().get(accountNumber).getBalance();
            Server.getAuthorisedClientAccounts().get(accountNumber).setBalance(transferUserBalance + amount);
            return 1;
        }else{ //insufficient balance
            return 0;
        }
    }
    public int withdraw(float amount){
        if(amount <= balance){ //success
            balance-=amount;
            return 1;
        }else{ //insufficient balance
            return 0;
        }

    }

}
