package entities;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Menu {

    @FXML
    private Button instructionButton;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button startButton;
    SpaceShooter game;
    public void setGame(SpaceShooter game) {
        this.game = game;
    }   
    @FXML
    void clickInstructions(ActionEvent event) throws IOException {
        
        System.out.println("instructions");
        Parent a = FXMLLoader.load(getClass().getResource("/instruction.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(a));
        stage.show();
    }

    @FXML
    void clickStart(ActionEvent event){
        System.out.println("start");
        this.game.startGame();
    }

    @FXML
    void clickquit(ActionEvent event) {

        System.out.println("quit");
        this.game.window.close();
    }

}
