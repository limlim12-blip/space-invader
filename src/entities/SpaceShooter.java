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
        if (player.getHealth() <= 0) gameOver = true;
        if (!gameOver) {
            if (score >= 100 && !bossExists) { // Sửa từ score == 1 thành score >= 100
                spawnBossEnemy();
            }
            player.update();
            if (player.shooting) player.shoot(bullets);
            checkCollisions();
            checkEnemiesReachingBottom();
            if (!bossExists) spawnEnemy();
            spawnPowerUp();
            for (int i = enemies.size() - 1; i >= 0; i--) {
                enemies.get(i).update();
                if (enemies.get(i).isDead()) enemies.remove(i);
            }
            for (int i = bullets.size() - 1; i >= 0; i--) {
                bullets.get(i).update();
                if (bullets.get(i).isDead()) bullets.remove(i);
            }
            if (bossExists && boss != null) {
                boss.update();
                // Sửa logic phase để chỉ gọi một lần khi health thay đổi
                if (boss.health <= 40 && boss.health > 30 && moon.size() == 0) boss.phase(moon, 2);
                if (boss.health <= 30 && boss.health > 20 && moon.size() == 2) boss.phase(moon, 4);
                if (boss.health <= 20 && boss.health > 10 && moon.size() == 4) boss.phase(moon, 6);
                if (boss.health <= 10 && moon.size() == 6) boss.phase(moon, 10);
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
        if (Math.random() < 0.01 && enemies.size() < 10) {
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
            boss = new BossEnemy(WIDTH / 2, 50); // Gán giá trị cho biến boss
            enemies.add(boss);
            eBullets = new ArrayList<>(); // Khởi tạo danh sách đạn của boss
            moon = new ArrayList<>(); // Khởi tạo danh sách vệ tinh
            bossExists = true;
        }
    }

    private void checkCollisions() {
        // Kiểm tra va chạm với PowerUp
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            if (player.getBounds().intersects(powerUp.getBounds())) {
                player.setHealth(player.getHealth() + 1);
                player.Powerup(); // Gọi Powerup() để tăng tốc độ bắn
                powerUp.setDead(true);
            }
        }

        // Kiểm tra va chạm giữa đạn của người chơi và kẻ thù
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bullet.setExploding(true); // Kích hoạt hiệu ứng nổ cho đạn
                    enemy.setExploding(true);  // Kích hoạt hiệu ứng nổ cho kẻ thù
                    score += 10; // Tăng điểm khi tiêu diệt kẻ thù
                    break; // Thoát vòng lặp kẻ thù để tránh kiểm tra tiếp
                }
            }
        }

        // Kiểm tra va chạm giữa đạn của người chơi và boss (nếu boss tồn tại)
        if (bossExists && boss != null) {
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                if (bullet.getBounds().intersects(boss.getBounds())) {
                    bullet.setExploding(true);
                    boss.takeDamage(); // Giảm máu của boss
                    score += 20; // Tăng điểm khi bắn trúng boss
                }
            }

            // Kiểm tra va chạm giữa đạn của người chơi và vệ tinh
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                for (int j = moon.size() - 1; j >= 0; j--) {
                    satellite sat = moon.get(j);
                    if (bullet.getBounds().intersects(sat.getBounds())) {
                        bullet.setExploding(true);
                        sat.setExploding(true);
                        score += 15; // Tăng điểm khi tiêu diệt vệ tinh
                        break;
                    }
                }
            }

            // Kiểm tra va chạm giữa đạn của kẻ thù và người chơi
            for (int i = eBullets.size() - 1; i >= 0; i--) {
                EnemyBullet eBullet = eBullets.get(i);
                if (eBullet.getBounds().intersects(player.getBounds())) {
                    player.setHealth(player.getHealth() - 1);
                    player.setTakingdame(true);
                    eBullet.setExploding(true); // Thêm hiệu ứng nổ cho đạn của kẻ thù
                }
            }
        }

        // Kiểm tra va chạm với kẻ thù
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.setHealth(player.getHealth() - 1);
                player.setTakingdame(true);
                enemy.setDead(true);
                enemies.remove(i);
                i--;
            }
        }

        // Kiểm tra va chạm giữa người chơi và vệ tinh
        for (int i = moon.size() - 1; i >= 0; i--) {
            satellite sat = moon.get(i);
            if (player.getBounds().intersects(sat.getBounds())) {
                player.setHealth(player.getHealth() - 1);
                player.setTakingdame(true);
                sat.setExploding(true);
            }
        }

        // Kiểm tra va chạm giữa người chơi và boss
        if (bossExists && boss != null && player.getBounds().intersects(boss.getBounds())) {
            player.setHealth(player.getHealth() - 2); // Boss gây sát thương lớn hơn
            player.setTakingdame(true);
        }

        // Cập nhật trạng thái các đối tượng
        updateGameObjects();
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
        if (bossExists && boss != null) {
            eBullets.removeIf(eBullet -> eBullet.isDead());
            moon.removeIf(sat -> sat.isDead());
            if (boss.isDead()) {
                bossExists = false;
                boss = null;
                score += 100;
                showTempMessage("Victory! Boss Defeated!", WIDTH / 2, HEIGHT / 2, 3.0); // Thêm thông báo chiến thắng
                gameOver = true; // Kết thúc game
            }
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
        AnimationTimer messageTimer = new AnimationTimer() {
            long startTime = System.nanoTime();
            @Override
            public void handle(long now) {
                double elapsed = (now - startTime) / 1_000_000_000.0;
                if (elapsed < duration) {
                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font(20));
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(message, x, y);
                } else {
                    stop();
                }
            }
        };
        messageTimer.start();
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
        gameloop(gc); // Thêm dòng này để khởi động game loop
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
