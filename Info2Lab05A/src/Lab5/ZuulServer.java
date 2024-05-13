package Lab5;
import java.io.BufferedReader;//reads characters from the input stream
import java.io.IOException;
import java.io.InputStreamReader;//reads bytes and decodes them into characters
import java.io.PrintWriter; //writes formatted representations of objects to a text output stream
import java.net.ServerSocket; //listens for incoming connections
import java.net.Socket; // client side endpoint for communication with the server

public class ZuulServer {
    private static final int PORT = 12345; // Choose a port number

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { //create a ServerSocket on specified port and open it
            System.out.println("Zuul Server is running on port " + PORT);

            while (true) {
            	// infinite loop to accept client connection
                Socket clientSocket = serverSocket.accept(); //accepts connection from client and returns a Socket object representing the client
                System.out.println("New client connected: " + clientSocket);

                // Create a new thread to handle the client, passing the clientSocket as an argument
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start(); //start the thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable { //Defines an inner class named ClientHandler that implements the Runnable interface
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override //the run method of the runnable interface, which contains the code that will be executed when the clientHandler is run as a separate thread
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true); //sending messages
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //receiving messages

                // Create a new instance of the Zuul game
                ZuulGame game = new ZuulGame(in, out);
                game.play();
            } catch (IOException e) { // catch and handle potential errors, properly closes the clientsocket after communication with the client is finished
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
