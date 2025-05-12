package entities;

import java.lang.classfile.Signature.TypeArg.Bounded.WildcardIndicator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    List<PowerUp> up;   
    BossEnemy boss;
    // TODO: Declare UI labels, lists of GameObjects, player, root Pane, Scene, Stage

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setTitle("SPACE-INVADER");
        startGame();
        initEventHandlers(scene);
        scene.setOnKeyPressed(event -> handleKeyPress(event));
        scene.setOnKeyReleased(event -> handleKeyRelease(event));
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getRoot().requestFocus();
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
        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_667) {
                    double elapsedTime = (now - lastUpdate) / 1_000_000_000.0;
                    gameupdate(elapsedTime);
                    gamerender(gc);
                    lastUpdate = now;
                }
            }
        };
        timer.start();

    }

    protected void gamerender(GraphicsContext gc) {
    gc.setFill(Color.grayRgb(20));
    gc.fillRect(0, 0, WIDTH, HEIGHT);
    gc.setTextAlign(TextAlignment.LEFT);
    gc.setFont(Font.font(20));
    gc.setFill(Color.WHITE);
    if (!bossExists) {
        gc.fillText("Score: " + score, 10, 30);
        gc.fillText("Health: " + player.getHealth(), 10, 55);
    } 
    else {
        gc.fillText("Score: " + score, 10, 30);
        gc.fillText("Player health: " + player.getHealth(), 10, 55);
        gc.fillText("Boss health: " + boss.getHealth(), 10, 80);
    }
    player.render(gc);
    for (int i = enemies.size() - 1; i >= 0; i--) {
        enemies.get(i).render(gc);
    }
    for (int i = bullets.size() - 1; i >= 0; i--) {
        bullets.get(i).render(gc);
    }
    if(!up.isEmpty())
    up.get(0).render(gc);
    if(bossExists) boss.render(gc);
        
    }
    
    protected void gameupdate(double elapsedTime) {
        if (score == 3) {
            spawnBossEnemy();
        }
        player.update();
        if (player.shooting) player.shoot(bullets);
        checkCollisions();
        checkEnemiesReachingBottom();
        if(!bossExists) spawnEnemy();
        spawnPowerUp();
        if(up==null) spawnPowerUp();
        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).update();
            if (enemies.get(i).isDead())
                enemies.remove(i);
        }
        for (int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).update();
            if (bullets.get(i).isDead())
                bullets.remove(i);
        }
        if(!up.isEmpty()){
            up.get(0).update();
            if (up.get(0).isDead())
                up.remove(0);
        }
        if(bossExists) boss.update();
    }

    private void spawnEnemy() {
        if (Math.random() < 0.005 * score + 0.05) {
            enemies.add(new Enemy(new Random().nextInt(325) + 2, 0));
        }

    }

    private void spawnPowerUp() {
        if ((score+1)%5==0&&up.isEmpty()) {
            up.add(new PowerUp(new Random().nextInt(325) + 2, new Random().nextInt(50) + 600));
        }
    }

    private void spawnBossEnemy() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).setExploding(true);
        }
        boss = new BossEnemy(WIDTH, HEIGHT);
        bossExists = true;
    }

    private void checkCollisions() {
        // TODO: detect and handle collisions between bullets, enemies, power-ups, player
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                if (!enemy.exploding)
                    player.setHealth(player.getHealth() - 1);
                enemy.setExploding(true);
            }

            for (Bullet bullet : bullets) {
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    if (!enemy.exploding) {
                        score++;
                        bullet.setDead(true);
                    }
                    enemy.setExploding(true);

                }
            }
        }
        if(!up.isEmpty()) {
            for (int i = bullets.size() - 1; i >= 0;i--) {
                if (bullets.get(i).getBounds().intersects(up.get(0).getBounds())) {
                    up.get(0).setDead(true);;
                    bullets.get(i).setDead(true);
                    player.Powerup();
                    
                }
            }
            if (player.getBounds().intersects(up.get(0).getBounds())) {
                player.Powerup();
                    up.get(0).setDead(true);;
            }
        }
    }

    private void checkEnemiesReachingBottom() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).getY() > 800){
                player.setHealth(player.getHealth() - 1);
                enemies.remove(i);
            }

        }
        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (bullets.get(i).getY() <= 0) {
                bullets.remove(i);
            }
        }
        if (!up.isEmpty()&&up.get(0).getY() > 800) {
            up.remove(0);
        }
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

        scene.setOnKeyPressed(event -> handleKeyPress(event));
        scene.setOnKeyReleased(event -> handleKeyRelease(event));
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
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        up = new ArrayList<>();
        player = new Player(WIDTH / 2, HEIGHT - 40);
        // TODO: set gameRunning to true and switch to game scene
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT)
            player.setMoveLeft(true);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(true);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(true);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(true);
        if (event.getCode() == KeyCode.SPACE)player.setShooting(true);
    }

    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) player.setMoveLeft(false);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(false);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(false);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(false);
        if (event.getCode() == KeyCode.SPACE)player.setShooting(false);
    }
}
