package entities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class Losing {

    @FXML
    private Label Score1;

    @FXML
    private ImageView myImageView;

    @FXML
    private Button quitButton;

    @FXML
    private Button tryagainButton;

    SpaceShooter game;
    public void setGame(SpaceShooter game) {
        this.game = game;
    }
    public void setScore1(int score) {
        Score1.setText(score + "");
    }
    @FXML
    void clickquit(ActionEvent event) {
        System.out.println("quit");
        this.game.window.close();
    }

    @FXML
    void clicktryagain(ActionEvent event) {

        System.out.println("tryagain");
        this.game.startGame();
    }

}
