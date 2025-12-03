package client.client;

import common.model.CardData;
import rmi.server.BankService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Handles RMI communication with the RMI server.
 */
public class RMIClient implements AutoCloseable {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;
    private static final String SERVICE_NAME = "BankService";
    
    private final String host;
    private final int port;
    private Registry registry;
    private BankService bankService;
    private String sessionId;
    
    /**
     * Creates a new RMI client with default connection settings.
     */
    public RMIClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }
    
    /**
     * Creates a new RMI client with custom connection settings.
     *
     * @param host the server hostname
     * @param port the server port
     */
    public RMIClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    /**
     * Connects to the RMI server.
     *
     * @throws Exception if connection fails
     */
    public void connect() throws Exception {
        try {
            // Get the registry
            registry = LocateRegistry.getRegistry(host, port);
            
            // Look up the remote object
            bankService = (BankService) registry.lookup(SERVICE_NAME);
            
            System.out.println("Connected to RMI server at " + host + ":" + port);
        } catch (RemoteException e) {
            throw new Exception("Failed to connect to RMI server: " + e.getMessage(), e);
        } catch (NotBoundException e) {
            throw new Exception("Service '" + SERVICE_NAME + "' not found on the server", e);
        }
    }
    
    /**
     * Authenticates a user with the server.
     *
     * @param username the username
     * @param password the password
     * @return true if authentication was successful, false otherwise
     * @throws Exception if an error occurs during authentication
     */
    public boolean login(String username, String password) throws Exception {
        try {
            sessionId = bankService.login(username, password);
            return sessionId != null && !sessionId.isEmpty();
        } catch (RemoteException e) {
            throw new Exception("Authentication failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Logs out the current user.
     *
     * @return true if logout was successful, false otherwise
     * @throws Exception if an error occurs during logout
     */
    public boolean logout() throws Exception {
        if (sessionId == null) {
            return true; // Already logged out
        }
        
        try {
            boolean result = bankService.logout(sessionId);
            if (result) {
                sessionId = null;
            }
            return result;
        } catch (RemoteException e) {
            throw new Exception("Logout failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Encrypts a card number.
     *
     * @param cardNumber the card number to encrypt
     * @param shift the shift value for encryption
     * @return the encrypted card data
     * @throws Exception if encryption fails or user is not authenticated
     */
    public String encryptCard(String cardNumber, int shift) throws Exception {
        checkAuthenticated();
        try {
            return bankService.encryptCard(sessionId, cardNumber, shift);
        } catch (RemoteException e) {
            throw new Exception("Encryption failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Decrypts a card number.
     *
     * @param encryptedData the encrypted card data
     * @param shift the shift value used for encryption
     * @return the decrypted card number
     * @throws Exception if decryption fails or user is not authenticated
     */
    public String decryptCard(String encryptedData, int shift) throws Exception {
        checkAuthenticated();
        try {
            return bankService.decryptCard(sessionId, encryptedData, shift);
        } catch (RemoteException e) {
            throw new Exception("Decryption failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets the list of all encrypted cards for the current user.
     *
     * @return list of card data
     * @throws Exception if the operation fails or user is not authenticated
     */
    public List<CardData> listCards() throws Exception {
        checkAuthenticated();
        try {
            return bankService.listCards(sessionId);
        } catch (RemoteException e) {
            throw new Exception("Failed to list cards: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if the client is currently authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return sessionId != null && !sessionId.isEmpty();
    }
    
    /**
     * Gets the current session ID.
     *
     * @return the session ID, or null if not authenticated
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * Closes the RMI client and releases resources.
     */
    @Override
    public void close() {
        try {
            if (isAuthenticated()) {
                logout();
            }
        } catch (Exception e) {
            System.err.println("Error during client cleanup: " + e.getMessage());
        }
    }
    
    private void checkAuthenticated() throws Exception {
        if (!isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
    }
}
