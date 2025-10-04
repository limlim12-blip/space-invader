package gamestate;
import entities.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EventHandler {

    protected void handleKeyPress(KeyEvent event, Player player, boolean gameRunning, SpaceShooter spaceinvader) {
        if (event.getCode() == KeyCode.LEFT) player.setMoveLeft(true);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(true);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(true);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(true);
        if (event.getCode() == KeyCode.A) player.setMoveLeft(true);
        if (event.getCode() == KeyCode.D)player.setMoveRight(true);
        if (event.getCode() == KeyCode.W)player.setMoveForward(true);
        if (event.getCode() == KeyCode.S)player.setMoveBackward(true);
        if (event.getCode() == KeyCode.SPACE) { player.setShooting(true); }
        if (event.getCode() == KeyCode.ESCAPE) { if(gameRunning) spaceinvader.pause(); }
    }

    protected void handleKeyRelease(KeyEvent event, Player player, boolean gameRunning) {
        if (event.getCode() == KeyCode.LEFT) player.setMoveLeft(false);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(false);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(false);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(false);
        if (event.getCode() == KeyCode.A) player.setMoveLeft(false);
        if (event.getCode() == KeyCode.D)player.setMoveRight(false);
        if (event.getCode() == KeyCode.W)player.setMoveForward(false);
        if (event.getCode() == KeyCode.S)player.setMoveBackward(false);
        if (event.getCode() == KeyCode.SPACE)player.setShooting(false);
    }
}
