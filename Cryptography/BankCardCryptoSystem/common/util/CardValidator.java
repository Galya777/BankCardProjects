package common.util;

/**
 * Provides validation for credit card numbers using Luhn algorithm.
 */
public class CardValidator {
    
    /**
     * Validates a credit card number using the Luhn algorithm.
     *
     * @param cardNumber the card number to validate
     * @return true if the card number is valid, false otherwise
     */
    public static boolean isValid(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove all non-digit characters
        String cleanNumber = cardNumber.replaceAll("[^0-9]", "");
        
        // Check if the card number contains only digits and has a valid length
        if (!cleanNumber.matches("^\\d{13,19}$")) {
            return false;
        }
        
        int sum = 0;
        boolean alternate = false;
        
        // Process each digit from right to left
        for (int i = cleanNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cleanNumber.substring(i, i + 1));
            
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
    
    /**
     * Gets the card type based on the card number.
     *
     * @param cardNumber the card number
     * @return the card type as a String (Visa, MasterCard, etc.) or "Unknown"
     */
    public static String getCardType(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return "Unknown";
        }
        
        // Remove all non-digit characters
        String cleanNumber = cardNumber.replaceAll("[^0-9]", "");
        
        if (cleanNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")) {
            return "Visa";
        } else if (cleanNumber.matches("^5[1-5][0-9]{14}$")) {
            return "MasterCard";
        } else if (cleanNumber.matches("^3[47][0-9]{13}$")) {
            return "American Express";
        } else if (cleanNumber.matches("^3(?:0[0-5]|[68][0-9])[0-9]{11}$")) {
            return "Diners Club";
        } else if (cleanNumber.matches("^6(?:011|5[0-9]{2})[0-9]{12}$")) {
            return "Discover";
        } else if (cleanNumber.matches("^(?:2131|1800|35\\d{3})\\d{11}$")) {
            return "JCB";
        } else {
            return "Unknown";
        }
    }
}
