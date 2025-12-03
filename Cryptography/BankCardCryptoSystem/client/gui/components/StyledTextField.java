package client.gui.components;

import javafx.scene.control.TextField;

public class StyledTextField extends TextField {
    public StyledTextField() {
        super();
        setStyle("-fx-padding: 10px; -fx-background-radius: 5px; " +
                "-fx-border-color: #ddd; -fx-border-radius: 5px; " +
                "-fx-border-width: 1px; -fx-font-size: 14px;");
    }
}
