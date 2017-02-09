import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    static boolean isRunning = true;
    final static int PORT = 1337;
    static volatile String storedMessage;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Socket listening on " + serverSocket.getLocalSocketAddress().toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start();
            } catch (IOException ioe) {
                System.out.println("Exception found on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }
        try {
            serverSocket.close();
            System.out.println("Server Stopped");
        } catch (Exception ioe) {
            System.out.println("Error Found stopping server socket");
            System.exit(-1);
        }
    }
    
    public static void main(String[] args) {
        new Server();

    }
}