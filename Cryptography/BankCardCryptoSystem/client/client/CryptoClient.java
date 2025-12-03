package client.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Client for the multi-threaded bank card encryption server.
 */
public class CryptoClient {
    private static final int BUFFER_SIZE = 1024;
    private final String host;
    private final int port;
    private SocketChannel clientChannel;
    private final ByteBuffer buffer;
    private boolean isConnected;
    private String sessionToken;

    public CryptoClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.isConnected = false;
        this.sessionToken = null;
    }

    /**
     * Connects to the server.
     * @return true if connection is successful, false otherwise
     */
    public boolean connect() {
        try {
            clientChannel = SocketChannel.open();
            clientChannel.connect(new InetSocketAddress(host, port));
            clientChannel.configureBlocking(true);
            isConnected = true;
            System.out.println("Connected to server at " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates the user with the server.
     * @param username The username
     * @param password The password
     * @return true if authentication is successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (!isConnected) {
            System.err.println("Not connected to server");
            return false;
        }

        String loginCommand = String.format("LOGIN %s %s", username, password);
        String response = sendCommand(loginCommand);
        
        if (response != null && response.startsWith("AUTH_SUCCESS")) {
            this.sessionToken = response.split("\\s+")[1];
            System.out.println("Login successful");
            return true;
        } else {
            System.err.println("Login failed: " + response);
            return false;
        }
    }

    /**
     * Encrypts a card number.
     * @param cardNumber The card number to encrypt
     * @param shift The shift value for encryption
     * @return The encrypted card data or null if failed
     */
    public String encryptCard(String cardNumber, int shift) {
        if (!isConnected || sessionToken == null) {
            System.err.println("Not authenticated");
            return null;
        }

        String command = String.format("ENCRYPT %s %d %s", cardNumber, shift, sessionToken);
        return sendCommand(command);
    }

    /**
     * Decrypts card data.
     * @param encryptedData The encrypted card data
     * @param shift The shift value used for encryption
     * @return The decrypted card number or null if failed
     */
    public String decryptCard(String encryptedData, int shift) {
        if (!isConnected || sessionToken == null) {
            System.err.println("Not authenticated");
            return null;
        }

        String command = String.format("DECRYPT %s %d %s", encryptedData, shift, sessionToken);
        return sendCommand(command);
    }

    /**
     * Sends a command to the server and waits for a response.
     * @param command The command to send
     * @return The server's response or null if an error occurs
     */
    private String sendCommand(String command) {
        if (!isConnected) {
            System.err.println("Not connected to server");
            return null;
        }

        try {
            // Send command
            buffer.clear();
            buffer.put(command.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            clientChannel.write(buffer);

            // Read response
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                System.err.println("Server closed the connection");
                disconnect();
                return null;
            }

            buffer.flip();
            byte[] responseBytes = new byte[buffer.remaining()];
            buffer.get(responseBytes);
            return new String(responseBytes, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Error communicating with server: " + e.getMessage());
            disconnect();
            return null;
        }
    }

    /**
     * Disconnects from the server.
     */
    public void disconnect() {
        if (isConnected) {
            try {
                if (sessionToken != null) {
                    sendCommand("LOGOUT " + sessionToken);
                }
                clientChannel.close();
                System.out.println("Disconnected from server");
            } catch (IOException e) {
                System.err.println("Error disconnecting: " + e.getMessage());
            } finally {
                isConnected = false;
                sessionToken = null;
            }
        }
    }

    /**
     * Example usage of the client.
     */
    public static void main(String[] args) {
        CryptoClient client = new CryptoClient("localhost", 8080);
        Scanner scanner = new Scanner(System.in);

        if (client.connect()) {
            try {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (client.login(username, password)) {
                    System.out.println("1. Encrypt card");
                    System.out.println("2. Decrypt card");
                    System.out.print("Choose an option: ");
                    
                    String option = scanner.nextLine();
                    if ("1".equals(option)) {
                        System.out.print("Enter card number: ");
                        String cardNumber = scanner.nextLine();
                        System.out.print("Enter shift value: ");
                        int shift = Integer.parseInt(scanner.nextLine());
                        
                        String encrypted = client.encryptCard(cardNumber, shift);
                        System.out.println("Encrypted: " + encrypted);
                        
                    } else if ("2".equals(option)) {
                        System.out.print("Enter encrypted data: ");
                        String encryptedData = scanner.nextLine();
                        System.out.print("Enter shift value: ");
                        int shift = Integer.parseInt(scanner.nextLine());
                        
                        String decrypted = client.decryptCard(encryptedData, shift);
                        System.out.println("Decrypted: " + decrypted);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                client.disconnect();
                scanner.close();
            }
        }
    }
}
