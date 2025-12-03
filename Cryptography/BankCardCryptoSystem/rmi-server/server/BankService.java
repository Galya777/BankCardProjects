package rmi.server;

import common.model.CardData;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI interface for bank card encryption/decryption service.
 */
public interface BankService extends Remote {
    /**
     * Authenticates a user with the provided credentials and returns a session ID.
     *
     * @param username The username
     * @param password The password
     * @return Session ID if authentication is successful, null otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    String login(String username, String password) throws RemoteException;
    
    /**
     * Logs out a user and invalidates the session.
     *
     * @param sessionId The session ID to invalidate
     * @return true if logout was successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean logout(String sessionId) throws RemoteException;
    
    /**
     * Encrypts a card number using the specified shift value.
     *
     * @param sessionId The session ID for authentication
     * @param cardNumber The card number to encrypt
     * @param shift The shift value for encryption (1-9)
     * @return The encrypted card data
     * @throws RemoteException if a remote communication error occurs
     * @throws SecurityException if the session is invalid or user is not authorized
     */
    String encryptCard(String sessionId, String cardNumber, int shift) 
            throws RemoteException, SecurityException;
    
    /**
     * Decrypts card data using the specified shift value.
     *
     * @param sessionId The session ID for authentication
     * @param encryptedData The encrypted card data
     * @param shift The shift value used for encryption (1-9)
     * @return The decrypted card number
     * @throws RemoteException if a remote communication error occurs
     * @throws SecurityException if the session is invalid or user is not authorized
     */
    String decryptCard(String sessionId, String encryptedData, int shift) 
            throws RemoteException, SecurityException;
    
    /**
     * Gets a list of all encrypted cards for the current user.
     *
     * @param sessionId The session ID for authentication
     * @return List of card data objects
     * @throws RemoteException if a remote communication error occurs
     * @throws SecurityException if the session is invalid or user is not authorized
     */
    List<CardData> listCards(String sessionId) throws RemoteException, SecurityException;
    
    /**
     * Validates if a session is still active.
     *
     * @param sessionId The session ID to validate
     * @return true if the session is valid, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean validateSession(String sessionId) throws RemoteException;
}
