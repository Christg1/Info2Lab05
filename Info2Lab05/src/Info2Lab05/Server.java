package Info2Lab05;



import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static List<Socket> clientSockets = new ArrayList<>();
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            // Start a thread to handle console input
            new Thread(Server::handleConsoleInput).start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                clientSockets.add(clientSocket);
                clientWriters.add(clientWriter);

                Thread clientThread = new Thread(() -> handleClient(clientSocket, clientWriter));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, PrintWriter writer) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println(clientMessage); // Print messages received from clients
                broadcastMessage(clientMessage, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastMessage(String message, PrintWriter sender) {
        for (PrintWriter clientWriter : clientWriters) {
            if (clientWriter != sender) {
                clientWriter.println(message);
            }
        }
    }

    private static void handleConsoleInput() {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Server: "); // Prompt for server message
                String serverMessage = consoleReader.readLine();
                if (serverMessage != null) {
                    broadcastMessage("Server: " + serverMessage, null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
