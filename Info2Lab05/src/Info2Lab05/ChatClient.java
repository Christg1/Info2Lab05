package Info2Lab05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame {
    private String senderName;
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter writer;

    public ChatClient(String senderName) {
        this.senderName = senderName;
        setTitle(senderName + "'s Chat");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        try {
            Socket socket = new Socket("localhost", 12345);
            writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            if (message.startsWith("/")) {
                // If the message starts with "/", send it without the prefix
                writer.println(message.substring(1)); // Exclude the first character ("/")
            } else {
                // Otherwise, prepend the sender's name and send as a regular chat message
                writer.println(senderName + ": " + message);
                chatArea.append("You: " + message + "\n");
            }
            messageField.setText("");
        }
    }



    public static void main(String[] args) {
        String senderName = JOptionPane.showInputDialog("Enter your name:");
        if (senderName == null || senderName.isEmpty()) {
            System.out.println("Invalid name");
            return;
        }

        SwingUtilities.invokeLater(() -> new ChatClient(senderName));
    }
}