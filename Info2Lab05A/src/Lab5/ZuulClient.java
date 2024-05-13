package Lab5;
import java.io.BufferedReader; //reads characters from the input stream
import java.io.IOException; 
import java.io.InputStreamReader;//reads bytes and decodes them into characters
import java.io.PrintWriter;//writes formatted representations of objects to a text-output stream
import java.net.Socket;

public class ZuulClient {
    private static final String SERVER_HOST = "localhost"; 
    private static final int PORT = 12345; 

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //data from client to server
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receive data from server
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to Zuul Server. Type 'quit' to exit.");

            // Thread for receiving messages from the server
            Thread receivingThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receivingThread.start();

            // Send messages from client to server
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if (userInput.equalsIgnoreCase("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
