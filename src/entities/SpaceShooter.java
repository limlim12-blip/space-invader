package entities;

import java.lang.classfile.Signature.TypeArg.Bounded.WildcardIndicator;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Skeleton for SpaceShooter. Students must implement game loop,
 * spawning, collision checks, UI, and input handling.
 */
public class SpaceShooter extends Application {

    public static final int WIDTH = 350;
    public static final int HEIGHT = 800;
    public static int numLives = 3;
    private boolean bossExists;
    private int score;
    private boolean reset;
    private boolean levelUpShown;
    private boolean gameRunning;
    private boolean gameOver;
    private Player player;   
    
    List<Enemy> enemies;
    List<Bullet> bullets;
    // TODO: Declare UI labels, lists of GameObjects, player, root Pane, Scene, Stage

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets
        
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        player = new Player(WIDTH / 2, HEIGHT - 40);
        Canvas canvas = new Canvas(WIDTH,HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();        
        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        // Scene scene = new Scene(root);
        // initEventHandlers(scene);
        primaryStage.setTitle("SPACE-INVADER");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        gameloop(gc);
        
        
        // TODO: initialize primaryStage, scene, canvas, UI labels, root pane
        // TODO: set up event handlers
        // TODO: initialize gameObjects list with player
        // TODO: create menu and switch to menu scene
        // TODO: set up AnimationTimer game loop and start it
        // TODO: show primaryStage
        
    }
    // Game mechanics stubs
    
    private void gameloop(GraphicsContext gc) {
        AnimationTimer timer= new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    double elapsedTime = (now - lastUpdate) / 1000000000;
                    gameupdate(elapsedTime);
                    gamerender(gc);
                }
                lastUpdate = now;
            }
        };
        timer.start();
        
    }

    protected void gamerender(GraphicsContext gc) {
        player.render(gc);
        spawnEnemy();
        for (Enemy obj : enemies) {
            obj.render(gc);
        }
    }

    protected void gameupdate(double elapsedTime) {
        player.update();
        for (Enemy obj : enemies) {
            obj.update();
        }
        checkCollisions();
    }

    private void spawnEnemy() {
        if (Math.random() < 0.05) {
            enemies.add(new Enemy(Math.random() * 800, 0));
        }

    }

    private void spawnPowerUp() {
        // TODO: implement power-up spawn logic
    }

    private void spawnBossEnemy() {
        // TODO: implement boss-only spawn logic
    }

    private void checkCollisions() {
        // TODO: detect and handle collisions between bullets, enemies, power-ups, player
    }

    private void checkEnemiesReachingBottom() {
        List<Enemy> remove=new ArrayList<>();
        for (Enemy obj : enemies) {
            if(obj.getY()>800)
                remove.add(obj);
        }
        // TODO: handle enemies reaching bottom of screen (reduce lives, respawn, reset game)
    }

    // UI and game state methods

    private void showLosingScreen() {
        // TODO: display Game Over screen with score and buttons
    }

    private void restartGame() {
        // TODO: reset gameObjects, lives, score and switch back to game scene
    }

    private void resetGame() {
        // TODO: stop game loop and call showLosingScreen
    }

    private void initEventHandlers(Scene scene) {
        
        scene.setOnKeyPressed(event->handleKeyPress(event));
        scene.setOnKeyReleased(event->handleKeyRelease(event));
        // TODO: set OnKeyPressed and OnKeyReleased for movement and shooting
    }

    private Pane createMenu() {
        // TODO: build and return main menu pane with styled buttons
        return new Pane();
    }

    private void showInstructions() {
        // TODO: display instructions dialog
    }

    private void showTempMessage(String message, double x, double y, double duration) {
        // TODO: show temporary on-screen message for duration seconds
    }

    private void startGame() {
        // TODO: set gameRunning to true and switch to game scene
    }

    private void handleKeyPress(KeyEvent event) {
        if(event.getCode()==KeyCode.LEFT) player.setMoveLeft(true);
        else if(event.getCode()==KeyCode.RIGHT) player.setMoveRight(true);
        else if(event.getCode()==KeyCode.UP) player.setMoveForward(true);
        else if(event.getCode()==KeyCode.DOWN) player.setMoveBackward(false);
        else if(event.getCode()==KeyCode.SPACE) player.shoot(bullets);
    }
    private void handleKeyRelease(KeyEvent event) {
        if(event.getCode()==KeyCode.LEFT) player.setMoveLeft(false);
        else if(event.getCode()==KeyCode.RIGHT) player.setMoveRight(false);
        else if(event.getCode()==KeyCode.UP) player.setMoveForward(false);
        else if(event.getCode()==KeyCode.DOWN) player.setMoveBackward(false);
    }
}
