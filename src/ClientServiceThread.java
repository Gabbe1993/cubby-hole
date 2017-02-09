import java.io.*;
import java.net.Socket;

class ClientServiceThread extends Thread {
    private Socket clientSocket;
    private boolean isRunning = true;

    final static String QUIT = "QUIT";
    final static String HELP = "HELP";
    final static String DROP = "DROP";
    final static String LOOK = "LOOK";
    final static String GET = "GET";
    final static String PUT = "PUT";

    public static void main(String [] args) {
        new ClientServiceThread();
    }

    private ClientServiceThread() {

    }

    ClientServiceThread(Socket s) {
        System.out.println("created client");
        clientSocket = s;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        DataOutputStream out = null;
        System.out.println(
                "Accepted Client Address - " + clientSocket.getInetAddress().getHostName());
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());

            out.writeBytes("!HELLO -  For help insert HELP" + "\n");
            while (isRunning) {
                String[] lines = in.readLine().split("\\s", 2);
                String command = lines[0].toUpperCase();

                //System.out.println("Got: " + command);

                if (!Server.isRunning) {
                    System.out.print("Server has already stopped" + "\n");
                    out.writeBytes("Server has already stopped" + "\n");
                    isRunning = false;
                }
                switch (command) {
                    case QUIT:
                        isRunning = false;
                        Server.isRunning = false;
                        out.writeBytes("Bye!\n");
                        break;
                    case HELP:
                        out.writeBytes("HELP:\n" +
                                "PUT < message > Places a new message in the cubbyhole.\n" +
                                "GET Takes the message out of the cubbyhole and displays it.\n" +
                                "LOOK Displays message without taking it out of the cubbyhole.\n" +
                                "DROP Takes the message out of the cubbyhole without displaying it.\n" +
                                "HELP Displays some help message.\n" +
                                "QUIT Terminates the connection.\n");
                        break;
                    case DROP:
                        out.writeBytes("!" + command + ": ok ");
                        Server.storedMessage = "";
                        break;
                    case LOOK:
                        out.writeBytes("!" + command + ": " + Server.storedMessage + "\n");
                        break;
                    case GET:
                        out.writeBytes("!" + command + ": " + Server.storedMessage + "\n");
                        Server.storedMessage = "";
                        break;
                    case PUT:
                        out.writeBytes("!" + command + ": ok " + "\n");
                        try {
                            Server.storedMessage = lines[1];
                            System.out.println("Stored message: " + Server.storedMessage + "\n");
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                            // no message
                        }
                        break;
                    default:
                        if (!command.isEmpty())
                            out.writeBytes("Invalid command: " + command + "\n");
                        out.flush();
                        break;
                }
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("...Stopped" + "\n");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}