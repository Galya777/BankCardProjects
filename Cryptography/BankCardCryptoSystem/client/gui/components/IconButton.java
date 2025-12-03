package client.gui.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconButton extends Button {
    public IconButton(String text, String iconName) {
        super(text);
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/" + iconName)));
            icon.setFitWidth(16);
            icon.setFitHeight(16);
            setGraphic(icon);
        } catch (Exception e) {
            // Icon not found, use text only
        }
        
        setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        
        setOnMouseEntered(e -> setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
    }
}
