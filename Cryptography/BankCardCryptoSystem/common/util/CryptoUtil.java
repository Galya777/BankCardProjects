package common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for cryptographic operations including password hashing and card encryption.
 */
public class CryptoUtil {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;
    private static final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Hashes a password with a random salt using PBKDF2.
     * 
     * @param password the password to hash
     * @return a hashed string in the format: algorithm:iterations:salt:hash
     */
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);
            
            // Hash the password with the salt
            byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS);
            
            // Format: algorithm:iterations:salt:hash
            return String.format("%s:%d:%s:%s",
                HASH_ALGORITHM,
                ITERATIONS,
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verifies a password against a stored hash.
     * 
     * @param password the password to verify
     * @param storedHash the stored hash to verify against
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Parse the stored hash
            String[] parts = storedHash.split(":");
            if (parts.length != 4) {
                return false;
            }
            
            String algorithm = parts[0];
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] hash = Base64.getDecoder().decode(parts[3]);
            
            // Generate hash with the same parameters
            byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations);
            
            // Compare hashes
            return MessageDigest.isEqual(hash, testHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Encrypts a card number using a simple shift cipher.
     * 
     * @param cardNumber the card number to encrypt
     * @param shift the shift value (1-9)
     * @return the encrypted card number
     * @throws IllegalArgumentException if the card number is invalid
     */
    public static String encryptCard(String cardNumber, int shift) {
        if (!CardValidator.isValid(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number");
        }
        
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            if (Character.isDigit(c)) {
                int num = Character.getNumericValue(c);
                num = (num + shift) % 10;
                result.append(num);
            } else if (c != ' ' && c != '-') {
                throw new IllegalArgumentException("Invalid character in card number: " + c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts a card number that was encrypted with the shift cipher.
     * 
     * @param encryptedData the encrypted card data
     * @param shift the shift value used for encryption (1-9)
     * @return the decrypted card number
     * @throws IllegalArgumentException if the encrypted data is invalid
     */
    public static String decryptCard(String encryptedData, int shift) {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < encryptedData.length(); i++) {
            char c = encryptedData.charAt(i);
            if (Character.isDigit(c)) {
                int num = Character.getNumericValue(c);
                num = (num - shift + 10) % 10; // +10 to handle negative numbers
                result.append(num);
            } else {
                throw new IllegalArgumentException("Invalid character in encrypted data: " + c);
            }
        }
        
        String decrypted = result.toString();
        if (!CardValidator.isValid(decrypted)) {
            throw new IllegalArgumentException("Decryption did not produce a valid card number");
        }
        
        return decrypted;
    }
    
    /**
     * PBKDF2 key derivation function.
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) 
            throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] input = concatenate(
                password,
                salt,
                String.valueOf(iterations).getBytes(StandardCharsets.UTF_8)
            );
            
            // Simple implementation (use PBKDF2WithHmacSHA256 in production)
            for (int i = 0; i < iterations; i++) {
                input = digest.digest(input);
            }
            
            return input;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }
    
    /**
     * Helper method to concatenate byte arrays.
     */
    private static byte[] concatenate(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        
        byte[] result = new byte[length];
        int pos = 0;
        
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        
        return result;
    }
    
    /**
     * Helper method to concatenate a char array with byte arrays.
     */
    private static byte[] concatenate(char[] chars, byte[]... arrays) {
        byte[] charBytes = new String(chars).getBytes(StandardCharsets.UTF_8);
        return concatenate(charBytes, concatenate(arrays));
    }
}
