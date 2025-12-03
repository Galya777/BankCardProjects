package common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a system user with authentication and authorization details.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    
    private String username;
    private String hashedPassword;
    private AccessLevel accessLevel;
    private int loginAttempts;
    private boolean locked;

    public User(String username, String hashedPassword, AccessLevel accessLevel) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.accessLevel = accessLevel;
        this.loginAttempts = 0;
        this.locked = false;
    }

    /**
     * Validates the provided password against the stored hash.
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    public boolean validatePassword(String password) {
        if (isLocked()) {
            return false;
        }
        // In a real implementation, use a proper password hashing library like BCrypt
        boolean isValid = this.hashedPassword.equals(hashPassword(password));
        if (!isValid) {
            incrementLoginAttempts();
        } else {
            resetLoginAttempts();
        }
        return isValid;
    }

    /**
     * Increments the login attempt counter and locks the account if necessary.
     */
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            this.locked = true;
        }
    }

    /**
     * Resets the login attempt counter and unlocks the account.
     */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.locked = false;
    }

    /**
     * Simple password hashing function.
     * In a production environment, use BCrypt or similar.
     */
    private String hashPassword(String password) {
        // This is a simple hash for demonstration
        // Replace with proper password hashing in production
        return String.valueOf(password.hashCode());
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", accessLevel=" + accessLevel +
                ", loginAttempts=" + loginAttempts +
                ", locked=" + locked +
                '}';
    }
}
