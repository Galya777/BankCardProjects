package client.gui.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import client.gui.BankCardApp;
import client.gui.components.PrimaryButton;
import client.gui.components.StyledTextField;
import client.gui.theme.ThemeManager;

public class LoginPanel extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    
    public LoginPanel() {
        initializeUI();
        setupEventHandlers();
    }
    
    private void initializeUI() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        
        // Logo
        ImageView logoView = new ImageView();
        try {
            logoView.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            // If logo not found, use text instead
            logoView = null;
            Label logoText = new Label("Bank Card System");
            logoText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + 
                            ThemeManager.getPrimaryColor() + ";");
            getChildren().add(logoText);
        }
        
        if (logoView != null) {
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            getChildren().add(logoView);
        }
        
        // Title
        Label titleLabel = new Label("Bank Card Encryption System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + 
                          ThemeManager.getPrimaryColor() + ";");
        
        // Form
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(350);
        formBox.setStyle("-fx-background-color: " + ThemeManager.getCardBackground() + 
                        "; -fx-padding: 30px; -fx-background-radius: 10px;");
        
        Label loginLabel = new Label("Login to Your Account");
        loginLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: " + 
                          ThemeManager.getTextColor() + ";");
        
        usernameField = new StyledTextField();
        usernameField.setPromptText("Username");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-radius: 5; -fx-padding: 10px;");
        
        loginButton = new PrimaryButton("Sign In");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        formBox.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton);
        
        // Version info
        Label versionLabel = new Label("v1.0.0");
        versionLabel.setTextFill(Color.GRAY);
        versionLabel.setStyle("-fx-font-size: 12px;");
        
        getChildren().addAll(titleLabel, formBox, versionLabel);
    }
    
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());
        
        // Handle Enter key press
        passwordField.setOnAction(e -> handleLogin());
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            BankCardApp.showError("Login Failed", "Please enter both username and password");
            return;
        }
        
        // TODO: Implement actual authentication
        if (username.equals("admin") && password.equals("admin123")) {
            BankCardApp.getInstance().showMainApplication();
        } else {
            BankCardApp.showError("Login Failed", "Invalid username or password");
        }
    }
}
