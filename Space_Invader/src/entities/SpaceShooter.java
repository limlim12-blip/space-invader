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
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).render(gc);
        }

        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).render(gc);
        }

    }

    protected void gameupdate(double elapsedTime) {
        player.update(); // Cập nhật người chơi

        // Gọi phương thức bắn tự động
        player.shoot(bullets);

        // Cập nhật đạn
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
            if (bullets.get(i).isDead()) {
                bullets.remove(i);
                i--; // Giảm index để tránh lỗi khi xóa phần tử
            }
        }

        // Cập nhật kẻ địch
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update();
            if (enemy.isDead()) {
                explosions.add(new Explosion(enemy.getX(), enemy.getY(), 30)); // Thêm giá trị duration
                enemies.remove(i);
                i--; // Giảm index sau khi xóa phần tử
            }
        }

        checkCollisions();
        updateGameObjects(); // Xóa các đối tượng đã chết
    }

    private void spawnEnemy() {
        if (Math.random() < 0.01) { // Chỉ spawn với xác suất 1% mỗi frame
            double spawnX = Math.random() * (WIDTH - Enemy.WIDTH);
            Enemy enemy = new Enemy(spawnX, 0);
            enemies.add(enemy);
        }
    }

    private void spawnPowerUp() {
        if (Math.random() < 0.005) { // Xác suất xuất hiện mỗi frame
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
        // Kiểm tra va chạm với PowerUp
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            if (player.getBounds().intersects(powerUp.getBounds())) {
                player.setHealth(player.getHealth() + 1); // Tăng mạng sống
                powerUp.setDead(true); // Đánh dấu là đã chết
            }
        }

        // Kiểm tra va chạm với kẻ địch
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.setHealth(player.getHealth() - 1);
                enemy.setDead(true);
                enemies.remove(i);
                i--; // Giảm index để tránh lỗi khi xóa phần tử
            }
        }
    }

    private void checkEnemiesReachingBottom() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getY() > SpaceShooter.HEIGHT) { // Nếu kẻ địch chạm đáy
                enemies.remove(i); // Xóa kẻ địch khỏi danh sách
                i--; // Giảm `i` để tránh lỗi khi xóa phần tử
                SpaceShooter.numLives--; // Trừ mạng khi kẻ địch chạm đáy
            }
        }
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
