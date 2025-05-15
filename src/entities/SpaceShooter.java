package entities;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Skeleton for SpaceShooter. Students must implement game loop,
 * spawning, collision checks, UI, and input handling.
 */
public class SpaceShooter extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static int numLives = 3;
    public boolean bossExists=false;
    public int score;
    public boolean reset;
    public boolean gameRunning;
    public boolean gameOver;
    public Player player;

    List<Enemy> enemies;
    List<Bullet> bullets;
    List<PowerUp> powerUps;   
    BossEnemy boss;
    List<EnemyBullet> eBullets;
    List<satellite> moon;
    // TODO: Declare UI labels, lists of GameObjects, player, root Pane, Scene, Stage

    public static void main(String[] args) {
        launch(args);
    }
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    public Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets

        this.window = primaryStage;
        window.setTitle("       SPACE-INVADER");
        window.getIcons().add(new Image("boss.png"));
        startGame();
        window.show();
        
        
        // TODO: initialize primaryStage, scene, canvas, UI labels, root pane
        // TODO: set powerUps event handlers
        // TODO: initialize gameObjects list with player
        // TODO: create menu and switch to menu scene
        // TODO: set powerUps AnimationTimer game loop and start it
        // TODO: show primaryStage
        
    }
    // Game mechanics stubs
    
    AnimationTimer timer;
    private void gameloop(GraphicsContext gc) {
            timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 20_666_667) {
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
    Image uni = new Image("/universe.jpg");
    ImagePattern pattern = new ImagePattern(uni,0,0,1,1,true);
    gc.setFill(pattern);
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
    if (bossExists && boss != null) {
        boss.render(gc);
            for (int i = eBullets.size()-1; i >= 0; i--) {
                eBullets.get(i).render(gc);
            }
            for (int i = moon.size()-1; i >= 0; i--) {
                moon.get(i).render(gc);
            }
    }
    }
    
    protected void gameupdate(double elapsedTime) {
        if (player.getHealth() <= 0)
            gameOver = true;
        if (!gameOver) {
            if (score == 1 && !bossExists) {
                spawnBossEnemy();
            }
            player.update();
            if (player.shooting)
                player.shoot(bullets);
            checkCollisions();
            checkEnemiesReachingBottom();
            if (!bossExists)
                spawnEnemy();
            spawnPowerUp();
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
            if (bossExists && boss != null) {
                boss.update();
                if (boss.health == 40 && moon.size() < 2)
                    boss.phase(moon, 2);
                if (boss.health == 30 && moon.size() < 4)
                    boss.phase(moon, 4);
                if (boss.health == 20 && moon.size() < 6)
                    boss.phase(moon, 6);
                if (boss.health == 10 && moon.size() < 10)
                    boss.phase(moon, 10);
                boss.shoot(eBullets);
                for (int i = eBullets.size() - 1; i >= 0; i--) {
                    eBullets.get(i).update();
                }
                for (int i = moon.size() - 1; i >= 0; i--) {
                    moon.get(i).up(boss);
                }
            }
        }
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

    public Scene gameScene;
    public void startGame() {
        StackPane root = new StackPane(canvas);
        gameScene = new Scene(root);
        initEventHandlers(gameScene);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();
        score = 0;
        gameOver = false;
        gameRunning = true;
        boss = null;
        player = new Player(WIDTH / 2, HEIGHT - 40);
        window.setScene(gameScene);
        gameloop(gc);
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT)
            player.setMoveLeft(true);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(true);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(true);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(true);
        if (event.getCode() == KeyCode.SPACE) {
            if(gameRunning)
            player.setShooting(true);
             else {
            timer.start();
            gameRunning = true;
            window.setScene(gameScene);
            }
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            if (gameRunning)
                gameRunning = false;
            else
                window.setScene(new Scene(createMenu()));
        }
    }

    public void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) player.setMoveLeft(false);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(false);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(false);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(false);
        if (event.getCode() == KeyCode.SPACE)player.setShooting(false);
    }
}
