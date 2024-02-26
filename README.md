# Advanced Chat Application

This is a Java client-server chat application using Java Swing for the client interface and Java networking for communication between clients and the server.

## Features
- Client-Server architecture
- Private messaging
- User authentication
- GUI with Java Swing
- Error handling

## How to Run

### Prerequisites
- Java Development Kit (JDK) installed
- VS Code with Java Extension Pack

### Running the Server
1. Open the project folder in VS Code.
2. Navigate to `src/ChatClientGUI.java` file.
3. Right-click on ChatClientGUI.java` and select "Run".

### Running the Client
1. Open another instance of VS Code.
2. Navigate to `src/ChatClientGUI.java` file.
3. Right-click on `ChatClientGUI.java` and select "Run".

### Connecting Clients to the Server
- When the server is running, clients can connect by entering `localhost` as the server address and `12345` as the port.

## Usage
- Upon running the client, a GUI window will appear.
- Enter your username and start sending messages in the chat area.
- You can send messages by typing in the message field and pressing "Enter" or clicking the "Send" button.

## Additional Notes
- Private messaging: To send a private message, enter `@username message`.
- User authentication: Username is required to join the chat. The server authenticates users based on the provided username.
- Error handling: The application handles connection errors and displays appropriate messages.
- GUI Interface: The client GUI is built using Java Swing for a simple and user-friendly chat interface.

## Credits
This project was created by Derrick Muthoni.

## License
MIT License
