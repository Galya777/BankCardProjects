package common.model;

/**
 * Defines the access levels for system users with corresponding permissions.
 */
public enum AccessLevel {
    /**
     * Administrator with full access to all features
     */
    ADMIN {
        @Override
        public boolean hasPermission(Permission permission) {
            return true; // Admins have all permissions
        }
        
        @Override
        public boolean canManageUsers() {
            return true;
        }
    },
    
    /**
     * Regular user with standard access
     */
    USER {
        @Override
        public boolean hasPermission(Permission permission) {
            return permission != Permission.MANAGE_USERS;
        }
    },
    
    /**
     * Guest user with limited access
     */
    GUEST {
        @Override
        public boolean hasPermission(Permission permission) {
            return permission == Permission.VIEW_CARDS;
        }
    };
    
    /**
     * Available permissions in the system
     */
    public enum Permission {
        ENCRYPT_CARD,    // Can encrypt card numbers
        DECRYPT_CARD,    // Can decrypt card numbers
        VIEW_CARDS,      // Can view card information
        MANAGE_USERS,    // Can manage user accounts
        EXPORT_DATA      // Can export card data
    }
    
    /**
     * Checks if this access level has the specified permission.
     * @param permission The permission to check
     * @return true if the permission is granted, false otherwise
     */
    public abstract boolean hasPermission(Permission permission);
    
    /**
     * Checks if this access level can manage users.
     * @return true if user management is allowed, false otherwise
     */
    public boolean canManageUsers() {
        return hasPermission(Permission.MANAGE_USERS);
    }
    
    /**
     * Gets the default access level for new users.
     * @return The default access level
     */
    public static AccessLevel getDefault() {
        return USER;
    }
    
    /**
     * Converts a string to an AccessLevel (case-insensitive).
     * @param value The string to convert
     * @return The corresponding AccessLevel, or null if not found
     */
    public static AccessLevel fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return AccessLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
