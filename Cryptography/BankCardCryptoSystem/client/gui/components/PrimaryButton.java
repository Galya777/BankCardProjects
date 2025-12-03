package client.gui.components;

import javafx.scene.control.Button;
import client.gui.theme.ThemeManager;

public class PrimaryButton extends Button {
    public PrimaryButton(String text) {
        super(text);
        setStyle("-fx-background-color: " + ThemeManager.getPrimaryColor() + 
                "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; -fx-background-radius: 5px;");
        
        setOnMouseEntered(e -> setStyle("-fx-background-color: " + ThemeManager.getSecondaryColor() + 
                                      "; -fx-text-fill: white; -fx-font-weight: bold; " +
                                      "-fx-padding: 10px 20px; -fx-background-radius: 5px;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: " + ThemeManager.getPrimaryColor() + 
                                     "; -fx-text-fill: white; -fx-font-weight: bold; " +
                                     "-fx-padding: 10px 20px; -fx-background-radius: 5px;"));
    }
}
