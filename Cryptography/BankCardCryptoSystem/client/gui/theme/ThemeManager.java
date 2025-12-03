package client.gui.theme;

import javafx.scene.paint.Color;

public class ThemeManager {
    private static final String MAIN_STYLESHEET = "/styles/main.css";
    private static final Color PRIMARY_COLOR = Color.web("#2c3e50");
    private static final Color SECONDARY_COLOR = Color.web("#3498db");
    private static final Color ACCENT_COLOR = Color.web("#e74c3c");
    private static final Color BACKGROUND_COLOR = Color.web("#f5f6fa");
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = Color.web("#2c3e50");
    private static final Color SUCCESS_COLOR = Color.web("#2ecc71");
    private static final Color WARNING_COLOR = Color.web("#f39c12");
    private static final Color DANGER_COLOR = Color.web("#e74c3c");
    
    private ThemeManager() {}
    
    public static void initialize() {
        // Load custom fonts if needed
        // Font.loadFont(ThemeManager.class.getResourceAsStream("/fonts/..."), 12);
    }
    
    public static String getMainStylesheet() {
        return ThemeManager.class.getResource(MAIN_STYLESHEET).toExternalForm();
    }
    
    // Getters for colors
    public static String getPrimaryColor() {
        return toHexString(PRIMARY_COLOR);
    }
    
    public static String getSecondaryColor() {
        return toHexString(SECONDARY_COLOR);
    }
    
    public static String getAccentColor() {
        return toHexString(ACCENT_COLOR);
    }
    
    public static String getBackgroundColor() {
        return toHexString(BACKGROUND_COLOR);
    }
    
    public static String getCardBackground() {
        return toHexString(CARD_BACKGROUND);
    }
    
    public static String getTextColor() {
        return toHexString(TEXT_COLOR);
    }
    
    public static String getSuccessColor() {
        return toHexString(SUCCESS_COLOR);
    }
    
    public static String getWarningColor() {
        return toHexString(WARNING_COLOR);
    }
    
    public static String getDangerColor() {
        return toHexString(DANGER_COLOR);
    }
    
    private static String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
}
