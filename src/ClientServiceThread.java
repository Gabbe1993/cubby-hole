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
        PrintWriter out = null;
        System.out.println(
                "Accepted Client Address - " + clientSocket.getInetAddress().getHostName());
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));

            out.print("!HELLO");
            while (isRunning) {
                String[] lines = in.readLine().split("\\s", 2);
                String command = lines[0].toUpperCase();

                //System.out.println("Got: " + command);

                if (!Server.isRunning) {
                    System.out.print("Server has already stopped");
                    out.println("Server has already stopped");
                    out.flush();
                    isRunning = false;
                }
                switch (command) {
                    case QUIT:
                        isRunning = false;
                        Server.isRunning = false;
                        out.println("Bye!");
                        break;
                    case HELP:
                        out.print("HELP:\n" +
                                "PUT < message > Places a new message in the cubbyhole.\n" +
                                "GET Takes the message out of the cubbyhole and displays it.\n" +
                                "LOOK Displays message without taking it out of the cubbyhole.\n" +
                                "DROP Takes the message out of the cubbyhole without displaying it.\n" +
                                "HELP Displays some help message.\n" +
                                "QUIT Terminates the connection.");
                        break;
                    case DROP:
                        out.println("!" + command + ": ok ");
                        Server.storedMessage = "";
                        break;
                    case LOOK:
                        out.println("!" + command + ": " + Server.storedMessage);
                        break;
                    case GET:
                        out.println("!" + command + ": " + Server.storedMessage);
                        Server.storedMessage = "";
                        break;
                    case PUT:
                        out.println("!" + command + ": ok ");
                        try {
                            Server.storedMessage = lines[1];
                            System.out.println("Stored message: " + Server.storedMessage);
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                            // no message
                        }
                        break;
                    default:
                        if (!command.isEmpty())
                            out.println("Invalid command: " + command);
                        out.flush();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
                System.out.println("...Stopped");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}