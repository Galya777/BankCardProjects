package multithreaded.server;

import common.model.CardData;
import common.model.User;
import common.model.AccessLevel;
import common.util.ErrorHandler;
import common.util.SecurityUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Processes commands received from clients.
 */
public class CommandProcessor {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> sessions = new ConcurrentHashMap<>();
    private final Map<String, CardData> cards = new ConcurrentHashMap<>();
    
    // In-memory storage for demo (replace with database in production)
    public CommandProcessor() {
        // Add some test users
        byte[] salt = SecurityUtils.generateSalt();
        users.put("admin", new User("admin", 
            SecurityUtils.hashPassword("admin123", salt), salt, AccessLevel.ADMIN));
    }
    
    /**
     * Processes a command from a client.
     * @param command The command string
     * @param sessionId The client's session ID, or null if not authenticated
     * @return The response to send back to the client
     */
    public String processCommand(String command, String sessionId) {
        if (command == null || command.trim().isEmpty()) {
            return "ERROR Empty command";
        }
        
        String[] parts = command.trim().split("\\s+", 2);
        String cmd = parts[0].toUpperCase();
        String args = parts.length > 1 ? parts[1] : "";
        
        try {
            switch (cmd) {
                case "LOGIN":
                    return handleLogin(args);
                case "LOGOUT":
                    return handleLogout(sessionId);
                case "ENCRYPT":
                    return handleEncrypt(args, sessionId);
                case "DECRYPT":
                    return handleDecrypt(args, sessionId);
                case "LIST":
                    return handleList(sessionId);
                default:
                    return "ERROR Unknown command: " + cmd;
            }
        } catch (Exception e) {
            ErrorHandler.handleException("Command processing error", e);
            return "ERROR " + e.getMessage();
        }
    }
    
    private String handleLogin(String args) {
        String[] credentials = args.split("\\s+", 2);
        if (credentials.length != 2) {
            return "ERROR Invalid login format. Use: LOGIN username password";
        }
        
        String username = credentials[0];
        String password = credentials[1];
        
        User user = users.get(username);
        if (user == null || !user.validatePassword(password)) {
            return "ERROR Invalid username or password";
        }
        
        String sessionId = generateSessionId();
        sessions.put(sessionId, username);
        return "AUTH_SUCCESS " + sessionId;
    }
    
    private String handleLogout(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
        return "LOGOUT_SUCCESS";
    }
    
    private String handleEncrypt(String args, String sessionId) {
        if (!isAuthenticated(sessionId)) {
            return "ERROR Not authenticated";
        }
        
        String[] parts = args.split("\\s+", 2);
        if (parts.length != 2) {
            return "ERROR Invalid format. Use: ENCRYPT cardNumber shift";
        }
        
        try {
            String cardNumber = parts[0];
            int shift = Integer.parseInt(parts[1]);
            
            if (!CardData.validate(cardNumber)) {
                return "ERROR Invalid card number";
            }
            
            CardData card = new CardData(cardNumber, shift);
            String encrypted = card.encrypt();
            cards.put(encrypted, card);
            
            return "ENCRYPTED " + encrypted;
        } catch (NumberFormatException e) {
            return "ERROR Invalid shift value";
        }
    }
    
    private String handleDecrypt(String args, String sessionId) {
        if (!isAuthenticated(sessionId)) {
            return "ERROR Not authenticated";
        }
        
        String[] parts = args.split("\\s+", 2);
        if (parts.length != 2) {
            return "ERROR Invalid format. Use: DECRYPT encryptedData shift";
        }
        
        try {
            String encryptedData = parts[0];
            int shift = Integer.parseInt(parts[1]);
            
            CardData card = cards.get(encryptedData);
            if (card == null) {
                return "ERROR Card data not found";
            }
            
            String decrypted = card.decrypt();
            return "DECRYPTED " + decrypted;
        } catch (NumberFormatException e) {
            return "ERROR Invalid shift value";
        }
    }
    
    private String handleList(String sessionId) {
        if (!isAuthenticated(sessionId)) {
            return "ERROR Not authenticated";
        }
        
        if (cards.isEmpty()) {
            return "No cards stored";
        }
        
        StringBuilder result = new StringBuilder("STORED CARDS:\\n");
        cards.forEach((encrypted, card) -> {
            result.append(String.format("- %s (shift: %d)\\n", 
                encrypted, card.getEncryptionShift()));
        });
        return result.toString();
    }
    
    private boolean isAuthenticated(String sessionId) {
        return sessionId != null && sessions.containsKey(sessionId);
    }
    
    private String generateSessionId() {
        return "SESS_" + System.currentTimeMillis() + "_" + 
               (int)(Math.random() * 1000);
    }
}
