import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServerGUI {
    private static final int PORT = 12345;
    private Map<String, String> userMap = new HashMap<>();
    private Map<String, List<String>> messageHistory = new HashMap<>();
    private JTextArea logArea;
    private JList<String> userList;
    private JComboBox<String> chatRoomDropdown;
    private JTextArea messageArea;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServerGUI server = new ChatServerGUI();
            server.createAndShowGUI();
            server.startServer();
        });
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Advanced Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        logArea = new JTextArea(10, 30); // Adjust size
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        userList = new JList<>();
        JScrollPane userScrollPane = new JScrollPane(userList);

        chatRoomDropdown = new JComboBox<>();
        chatRoomDropdown.addItem("Select Chat Room");

        messageArea = new JTextArea(10, 30); // Adjust size
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        JPanel controlPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        JButton startServerButton = new JButton("Start Server");
        JButton stopServerButton = new JButton("Stop Server");

        refreshButton.addActionListener(e -> updateUsersList());
        startServerButton.addActionListener(e -> startServer());
        stopServerButton.addActionListener(e -> stopServer());

        controlPanel.add(refreshButton);
        controlPanel.add(startServerButton);
        controlPanel.add(stopServerButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("User List"), BorderLayout.NORTH);
        leftPanel.add(userScrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Chat Room"), BorderLayout.NORTH);
        centerPanel.add(chatRoomDropdown, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Message History"), BorderLayout.NORTH);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(leftPanel);
        topPanel.add(centerPanel);
        topPanel.add(rightPanel);

        frame.setLayout(new BorderLayout());
        frame.add(logScrollPane, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            log("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            log("Error starting server: " + e.getMessage());
        }
    }

    private void updateUsersList() {
        SwingUtilities.invokeLater(() -> {
            userList.setListData(userMap.keySet().toArray(new String[0]));
            chatRoomDropdown.removeAllItems();
            chatRoomDropdown.addItem("Select Chat Room");
            for (String room : messageHistory.keySet()) {
                chatRoomDropdown.addItem(room);
            }
        });
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log("Server stopped.");
            }
        } catch (IOException e) {
            log("Error stopping server: " + e.getMessage());
        }
    }

    class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Handle client messages
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }

                    // Handle messages from clients
                    log("Received message: " + message);
                    // Process messages, send to other clients, etc.
                }
            } catch (IOException e) {
                log("Error handling client: " + e.getMessage());
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
