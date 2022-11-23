package com.ccs313;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println();

        System.out.println("==================================================================");//Display that sever was began.
        System.out.println("====================== Bank Server Starts =======================");
        System.out.println("==================================================================");

        // accept multiple connections (multi-threading)
        int x = 0;// initiate this variable for count how many Clients join the server.
        try {
            ServerSocket ss = new ServerSocket(2021);// make 2022 port visible for users.
            while (true) { // do infinite loop for gathering users to server.
                System.out.println("Waiting for clients....\n");
                Socket socket = ss.accept(); //if user connect to sever using this port accept the user. if error must close the socket.
                System.out.flush();
                //Create a server and accept 1 connection
                Server server = new Server(socket);   // Create a Server class and pass the values to constructor.
                server.start();    // Server starts running here.
                if (!ss.isBound() && ss.isClosed()) {
                    break;
                }
                System.out.println("Client " + (x + 1) + " Connected\n"); // print in the server that user is connect to the server now.
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();// if there was a error in try block print the error.

        }


    }
}
