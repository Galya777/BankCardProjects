package common.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bank card and its encrypted data.
 */
public class CardData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String cardNumber;
    private String encryptedData;
    private int encryptionShift;
    private LocalDateTime creationDate;

    public CardData(String cardNumber, int encryptionShift) {
        if (!validate(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number");
        }
        this.cardNumber = cardNumber;
        this.encryptionShift = encryptionShift;
        this.creationDate = LocalDateTime.now();
        this.encryptedData = encrypt();
    }

    /**
     * Encrypts the card number using the current shift value.
     * @return The encrypted card data
     */
    public String encrypt() {
        if (this.encryptedData != null) {
            return this.encryptedData;
        }
        
        StringBuilder result = new StringBuilder();
        for (char c : cardNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                int original = Character.getNumericValue(c);
                int encrypted = (original + encryptionShift) % 10;
                result.append(encrypted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Decrypts the card data using the stored shift value.
     * @return The decrypted card number
     */
    public String decrypt() {
        if (this.encryptedData == null) {
            return this.cardNumber;
        }
        
        StringBuilder result = new StringBuilder();
        for (char c : this.encryptedData.toCharArray()) {
            if (Character.isDigit(c)) {
                int encrypted = Character.getNumericValue(c);
                int decrypted = (encrypted - encryptionShift + 10) % 10;
                result.append(decrypted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Validates the card number using Luhn algorithm.
     * @param cardNumber The card number to validate
     * @return true if the card number is valid, false otherwise
     */
    public static boolean validate(String cardNumber) {
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

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public int getEncryptionShift() {
        return encryptionShift;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardData cardData = (CardData) o;
        return encryptionShift == cardData.encryptionShift &&
               Objects.equals(cardNumber, cardData.cardNumber) &&
               Objects.equals(encryptedData, cardData.encryptedData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, encryptedData, encryptionShift);
    }

    @Override
    public String toString() {
        return "CardData{" +
                "cardNumber='" + "*".repeat(Math.max(0, cardNumber.length() - 4)) + 
                cardNumber.substring(cardNumber.length() - 4) + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                ", encryptionShift=" + encryptionShift +
                ", creationDate=" + creationDate +
                '}';
    }
}
