import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private JTextArea chatArea;
    private JTextField messageField;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private JLabel statusLabel;
    private String username;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI client = new ChatClientGUI();
            client.createAndShowGUI();
        });
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Simple Chat Client by Group F");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(400, 30));
        messageField.addActionListener(e -> sendMessage());
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("Client Disconnected");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        frame.add(mainPanel);
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            updateStatus("Connected to Server");

            // Prompt for username
            getUsernameFromUser();

            // Start a new thread to handle incoming messages
            new Thread(() -> {
                try {
                    while (true) {
                        String message = reader.readLine();
                        if (message == null) {
                            break;
                        }
                        displayMessage(message);
                    }
                } catch (IOException e) {
                    updateStatus("Connection to Server Lost");
                }
            }).start();

        } catch (IOException e) {
            updateStatus("Failed to Connect to Server");
        }
    }

    private void getUsernameFromUser() {
        String input = JOptionPane.showInputDialog("Enter your username:");
        if (input != null && !input.isEmpty()) {
            username = input;
            writer.println(username); // Send username to server for authentication
            updateStatus("Username: " + username);
        } else {
            // If username is empty, close the connection
            closeConnection();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            displayMessage("You: " + message);
            messageField.setText("");
        }
    }

    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }

    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                updateStatus("Connection Closed");
            }
        } catch (IOException e) {
            updateStatus("Error closing connection: " + e.getMessage());
        }
    }
}
