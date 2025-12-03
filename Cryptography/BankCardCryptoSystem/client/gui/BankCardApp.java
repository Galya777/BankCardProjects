package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import client.gui.panels.LoginPanel;
import client.gui.theme.ThemeManager;

public class BankCardApp extends Application {
    private static final String APP_TITLE = "Bank Card Encryption System";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;
    
    private Stage primaryStage;
    private static BankCardApp instance;
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        
        // Initialize theme manager
        ThemeManager.initialize();
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app-icon.png")));
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
        
        // Show login screen
        showLoginScreen();
        
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(WINDOW_WIDTH);
        primaryStage.setMinHeight(WINDOW_HEIGHT);
        primaryStage.show();
    }
    
    public void showLoginScreen() {
        LoginPanel loginPanel = new LoginPanel();
        Scene scene = new Scene(loginPanel, WINDOW_WIDTH, WINDOW_HEIGHT);
        try {
            scene.getStylesheets().add(ThemeManager.getMainStylesheet());
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }
        primaryStage.setScene(scene);
    }
    
    public void showMainApplication() {
        MainPanel mainPanel = new MainPanel();
        Scene scene = new Scene(mainPanel, WINDOW_WIDTH, WINDOW_HEIGHT);
        try {
            scene.getStylesheets().add(ThemeManager.getMainStylesheet());
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + e.getMessage());
        }
        primaryStage.setScene(scene);
    }
    
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static BankCardApp getInstance() {
        return instance;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
