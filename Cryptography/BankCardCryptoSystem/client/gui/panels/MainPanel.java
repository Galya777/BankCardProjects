package client.gui.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import client.gui.BankCardApp;
import client.gui.components.IconButton;
import client.gui.components.PrimaryButton;
import client.gui.components.StyledTextField;
import client.gui.theme.ThemeManager;

public class MainPanel extends BorderPane {
    private TextField cardNumberField;
    private TextField shiftValueField;
    private TextField resultField;
    
    public MainPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        // Set background
        setStyle("-fx-background-color: " + ThemeManager.getBackgroundColor() + ";");
        
        // Create top toolbar
        ToolBar toolbar = createToolbar();
        setTop(toolbar);
        
        // Create center content
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20));
        
        // Card encryption section
        VBox cardSection = createCardSection();
        centerContent.getChildren().add(cardSection);
        
        // Result section
        VBox resultSection = createResultSection();
        centerContent.getChildren().add(resultSection);
        
        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button encryptButton = new PrimaryButton("Encrypt");
        try {
            encryptButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/lock.png"))));
        } catch (Exception e) {
            // Icon not found, use text only
        }
        encryptButton.setOnAction(e -> handleEncrypt());
        
        Button decryptButton = new PrimaryButton("Decrypt");
        try {
            decryptButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/unlock.png"))));
        } catch (Exception e) {
            // Icon not found, use text only
        }
        decryptButton.setOnAction(e -> handleDecrypt());
        
        buttonBox.getChildren().addAll(encryptButton, decryptButton);
        centerContent.getChildren().add(buttonBox);
        
        setCenter(centerContent);
    }
    
    private ToolBar createToolbar() {
        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color: " + ThemeManager.getPrimaryColor() + ";");
        
        // Logo
        ImageView logoView = new ImageView();
        try {
            logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo-small.png")));
            logoView.setFitHeight(32);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            // Logo not found, use text instead
            logoView = null;
        }
        
        // Title
        Label titleLabel = new Label("Bank Card Encryption");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User menu
        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label usernameLabel = new Label("Admin");
        usernameLabel.setStyle("-fx-text-fill: white;");
        
        Button logoutButton = new IconButton("", "logout.png");
        logoutButton.setOnAction(e -> handleLogout());
        
        userBox.getChildren().addAll(usernameLabel, logoutButton);
        
        if (logoView != null) {
            toolbar.getItems().addAll(logoView, titleLabel, spacer, userBox);
        } else {
            toolbar.getItems().addAll(titleLabel, spacer, userBox);
        }
        
        return toolbar;
    }
    
    private VBox createCardSection() {
        VBox cardSection = new VBox(10);
        cardSection.setAlignment(Pos.CENTER_LEFT);
        cardSection.setStyle("-fx-background-color: " + ThemeManager.getCardBackground() + 
                           "; -fx-padding: 20px; -fx-background-radius: 10px;");
        cardSection.setMaxWidth(600);
        
        Label sectionTitle = new Label("Card Information");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        cardNumberField = new StyledTextField();
        cardNumberField.setPromptText("Enter card number");
        
        shiftValueField = new StyledTextField();
        shiftValueField.setPromptText("Shift value (1-9)");
        
        cardSection.getChildren().addAll(sectionTitle, cardNumberField, shiftValueField);
        return cardSection;
    }
    
    private VBox createResultSection() {
        VBox resultSection = new VBox(10);
        resultSection.setAlignment(Pos.CENTER_LEFT);
        resultSection.setStyle("-fx-background-color: " + ThemeManager.getCardBackground() + 
                             "; -fx-padding: 20px; -fx-background-radius: 10px;");
        resultSection.setMaxWidth(600);
        
        Label resultLabel = new Label("Result");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        resultField = new StyledTextField();
        resultField.setEditable(false);
        
        resultSection.getChildren().addAll(resultLabel, resultField);
        return resultSection;
    }
    
    private void handleEncrypt() {
        // TODO: Implement encryption
        String cardNumber = cardNumberField.getText();
        String shiftText = shiftValueField.getText();
        
        if (cardNumber.isEmpty() || shiftText.isEmpty()) {
            BankCardApp.showError("Error", "Please enter both card number and shift value");
            return;
        }
        
        try {
            int shift = Integer.parseInt(shiftText);
            // TODO: Call encryption service
            resultField.setText("Encrypted: " + cardNumber + " (shift: " + shift + ")");
        } catch (NumberFormatException e) {
            BankCardApp.showError("Invalid Input", "Shift value must be a number");
        }
    }
    
    private void handleDecrypt() {
        // TODO: Implement decryption
        String encryptedData = cardNumberField.getText();
        String shiftText = shiftValueField.getText();
        
        if (encryptedData.isEmpty() || shiftText.isEmpty()) {
            BankCardApp.showError("Error", "Please enter both encrypted data and shift value");
            return;
        }
        
        try {
            int shift = Integer.parseInt(shiftText);
            // TODO: Call decryption service
            resultField.setText("Decrypted: 4111111111111111");
        } catch (NumberFormatException e) {
            BankCardApp.showError("Invalid Input", "Shift value must be a number");
        }
    }
    
    private void handleLogout() {
        // TODO: Implement logout
        BankCardApp.getInstance().showLoginScreen();
    }
}
