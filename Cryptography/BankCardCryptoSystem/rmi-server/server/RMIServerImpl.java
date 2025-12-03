package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the RMI server for bank card encryption/decryption service.
 */
public class RMIServerImpl implements BankService {
    private static final int RMI_PORT = 1099;
    private static final String SERVICE_NAME = "BankCardService";
    private Registry registry;
    
    // In-memory storage for demonstration (replace with database in production)
    private final Map<String, String> userCredentials;
    private final Map<String, String> cardData;

    public RMIServerImpl() {
        this.userCredentials = new HashMap<>();
        this.cardData = new HashMap<>();
        
        // Add some test users (in real app, use a database)
        userCredentials.put("admin", "admin123");
        userCredentials.put("user1", "password1");
    }

    /**
     * Starts the RMI server and registers the service.
     */
    public void start() {
        try {
            // Create and export the remote object
            BankService bankService = (BankService) UnicastRemoteObject.exportObject(this, 0);
            
            // Create or get the RMI registry
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("RMI registry created on port " + RMI_PORT);
            } catch (RemoteException e) {
                // Registry already exists, get it
                registry = LocateRegistry.getRegistry(RMI_PORT);
                System.out.println("Using existing RMI registry on port " + RMI_PORT);
            }
            
            // Bind the remote object
            registry.rebind(SERVICE_NAME, bankService);
            System.out.println("RMI Server started and bound to '" + SERVICE_NAME + "'");
            
        } catch (RemoteException e) {
            System.err.println("Error starting RMI server: " + e.getMessage());
            stop();
        }
    }

    /**
     * Stops the RMI server and cleans up resources.
     */
    public void stop() {
        try {
            if (registry != null) {
                // Unbind the service
                registry.unbind(SERVICE_NAME);
                System.out.println("RMI Server stopped");
            }
        } catch (Exception e) {
            System.err.println("Error stopping RMI server: " + e.getMessage());
        }
    }

    @Override
    public String encryptCard(String cardNumber, int shift) throws RemoteException {
        if (!isValidCardNumber(cardNumber)) {
            throw new RemoteException("Invalid card number");
        }
        
        StringBuilder result = new StringBuilder();
        for (char c : cardNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                int original = Character.getNumericValue(c);
                int encrypted = (original + shift) % 10;
                result.append(encrypted);
            } else {
                result.append(c);
            }
        }
        
        // Store the encrypted data (in real app, use a database)
        String encryptedData = result.toString();
        cardData.put(encryptedData, cardNumber);
        
        return encryptedData;
    }

    @Override
    public String decryptCard(String encryptedData, int shift) throws RemoteException {
        if (encryptedData == null || encryptedData.trim().isEmpty()) {
            throw new RemoteException("Encrypted data cannot be empty");
        }
        
        StringBuilder result = new StringBuilder();
        for (char c : encryptedData.toCharArray()) {
            if (Character.isDigit(c)) {
                int encrypted = Character.getNumericValue(c);
                int decrypted = (encrypted - shift + 10) % 10;
                result.append(decrypted);
            } else {
                result.append(c);
            }
        }
        
        String decryptedCard = result.toString();
        if (!isValidCardNumber(decryptedCard)) {
            throw new RemoteException("Decryption resulted in an invalid card number");
        }
        
        return decryptedCard;
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        if (username == null || password == null) {
            return false;
        }
        String storedPassword = userCredentials.get(username);
        return password.equals(storedPassword);
    }
    
    /**
     * Validates a card number using the Luhn algorithm.
     */
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove all non-digit characters
        String digits = cardNumber.replaceAll("\\D", "");
        
        // Check if the card number starts with 3,4,5, or 6 and has between 12-19 digits
        if (!digits.matches("^[3456]\\d{11,18}$")) {
            return false;
        }
        
        // Luhn algorithm
        int sum = 0;
        boolean alternate = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(digits.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    
    public static void main(String[] args) {
        try {
            // Create and start the server
            RMIServerImpl server = new RMIServerImpl();
            server.start();
            
            System.out.println("Bank Card RMI Server is running...");
            System.out.println("Press Ctrl+C to stop the server");
            
            // Keep the server running
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static class SessionInfo {
        private final long creationTime;
        
        public SessionInfo() {
            this.creationTime = System.currentTimeMillis();
        }
        
        public boolean isExpired() {
            // Sessions expire after 30 minutes
            return System.currentTimeMillis() - creationTime > 30 * 60 * 1000;
        }
    }
}
