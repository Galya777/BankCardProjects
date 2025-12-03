package multithreaded.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Handles communication with a single client connection in a separate thread.
 */
public class ClientHandler implements Runnable {
    private final SocketChannel clientChannel;
    private final CommandProcessor commandProcessor;
    private String sessionId;
    private boolean isRunning;

    /**
     * Creates a new ClientHandler for the given client channel.
     * @param clientChannel The client's socket channel
     * @param commandProcessor The command processor to handle client requests
     */
    public ClientHandler(SocketChannel clientChannel, CommandProcessor commandProcessor) {
        this.clientChannel = clientChannel;
        this.commandProcessor = commandProcessor;
        this.isRunning = true;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        try {
            while (isRunning && clientChannel.isConnected()) {
                // Read request
                buffer.clear();
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    break; // Client disconnected
                }

                String request = new String(buffer.array(), 0, bytesRead, StandardCharsets.UTF_8).trim();
                System.out.println("Received: " + request);

                // Process command
                String response = commandProcessor.processCommand(request, sessionId);
                
                // Handle session management
                if (response.startsWith("AUTH_SUCCESS")) {
                    sessionId = response.split("\\s+")[1];
                } else if (response.equals("LOGOUT_SUCCESS")) {
                    sessionId = null;
                }

                // Send response
                sendResponse(response);
            }
        } catch (Exception e) {
            ErrorHandler.handleException("Client handler error", e);
        } finally {
            closeConnection();
        }
    }
    
    /**
     * Sends a response to the client.
     * @param response The response to send
     * @throws IOException If an I/O error occurs
     */
    private void sendResponse(String response) throws IOException {
        if (response == null) {
            response = "ERROR No response from server";
        }
        ByteBuffer responseBuffer = ByteBuffer.wrap((response + "\n").getBytes(StandardCharsets.UTF_8));
        clientChannel.write(responseBuffer);
    }
    
    /**
     * Closes the client connection and cleans up resources.
     */
    public void closeConnection() {
        isRunning = false;
        if (sessionId != null) {
            commandProcessor.processCommand("LOGOUT", sessionId);
            sessionId = null;
        }
        if (clientChannel != null && clientChannel.isOpen()) {
            try {
                clientChannel.close();
                System.out.println("Client disconnected: " + clientChannel.getRemoteAddress());
            } catch (IOException e) {
                ErrorHandler.handleException("Error closing client channel", e);
            }
        }
    }
    
    /**
     * Stops this client handler.
     */
    public void stop() {
        isRunning = false;
        closeConnection();
    }
}
