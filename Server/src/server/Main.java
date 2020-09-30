package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        ServerAssistant.initializeRooms();
        ServerAssistant.initializeAccounts();

        try (ServerSocket serversocket = new ServerSocket(8080)) {
            System.out.println("Server is running..");
            while (true) {
                new ServerAssistant(serversocket.accept()).start();
                System.out.println("Client Connected");
            }

        } catch (IOException e) {
            System.out.println("Server Error" + e.getMessage());
        }

    }

}
