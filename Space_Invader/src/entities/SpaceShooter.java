package entities;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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

    List<PowerUp> powerUps;
    List<Enemy> enemies;
    List<Bullet> bullets;
    static List<Explosion> explosions = new ArrayList<>();
    // TODO: Declare UI labels, lists of GameObjects, player, root Pane, Scene, Stage

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets
        
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();
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
        for (Explosion explosion : explosions) {
            explosion.render(gc); // Hiển thị hiệu ứng nổ
        }

    }

    protected void gameupdate(double elapsedTime) {
        player.update();

        for (Enemy obj : enemies) {
            obj.update();
        }

        checkCollisions();
        updateGameObjects(); // Xóa các đối tượng đã chết
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                explosions.add(new Explosion(enemy.getX(), enemy.getY())); // Hiển thị hiệu ứng nổ
            }
        }

    }

    private void spawnEnemy() {
        double spawnX = Math.random() * (WIDTH - Enemy.WIDTH);
        Enemy enemy = new Enemy(spawnX, 0);
        enemies.add(enemy);


    }

    private void spawnPowerUp() {
        if (Math.random() < 0.01) { // Xác suất xuất hiện mỗi frame
            double spawnX = Math.random() * (WIDTH - PowerUp.WIDTH);
            PowerUp powerUp = new PowerUp(spawnX, 0);
            powerUps.add(powerUp);
        }

    }

    private void spawnBossEnemy() {
        if (score >= 100 && !bossExists) {
            BossEnemy boss = new BossEnemy(WIDTH / 2, 50);
            enemies.add(boss);
            bossExists = true;
        }

    }

    private void checkCollisions() {
        for (PowerUp powerUp : powerUps) {
            if (player.getBounds().intersects(powerUp.getBounds())) {
                player.setHealth(player.getHealth() + 1); // Tăng mạng sống
                powerUp.setDead(true); // Xóa khỏi danh sách
            }
        }
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.setHealth(player.getHealth() - 1); // Giảm mạng sống
                enemy.setDead(true);
            }
        }


    }

    private void checkEnemiesReachingBottom() {
        List<Enemy> remove=new ArrayList<>();
        for (Enemy obj : enemies) {
            if(obj.getY()>800)
                remove.add(obj);
        }
        // TODO: handle enemies reaching bottom of screen (reduce lives, respawn, reset game)
    }

    private void updateGameObjects() {
        enemies.removeIf(enemy -> enemy.isDead());
        bullets.removeIf(bullet -> bullet.isDead());
        powerUps.removeIf(powerUp -> powerUp.isDead());
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
