package entities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class instruction {

    @FXML
    private Button OK;

    @FXML
    private AnchorPane pane;
    Stage stage;
    @FXML
    void clickOK(ActionEvent event) {
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

}
