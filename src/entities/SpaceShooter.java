package entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Skeleton for SpaceShooter. Students must implement game loop,
 * spawning, collision checks, UI, and input handling.
 */
public class SpaceShooter extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static int numLives = 3;
    private boolean bossExists=false;
    private int score;
    private int levelUpShown;
    private boolean reset;
    private boolean gameRunning;
    private boolean gameOver;
    private Player player;

    
    List<Enemy> enemies;
    List<Bullet> bullets;
    List<PowerUp> up;   
    BossEnemy boss;
    List<EnemyBullet> eBullets;
    List<satellite> moon;
    MediaPlayer themep = new MediaPlayer(new Media(getClass().getResource("/starwar.mp3").toExternalForm()));
    MediaPlayer victoryp = new MediaPlayer(new Media(getClass().getResource("/Victory.mp3").toExternalForm()));
    MediaPlayer deadp = new MediaPlayer(new Media(getClass().getResource("/dead.mp3").toExternalForm()));
    // TODO: Declare UI labels, lists of GameObjects, player, root Pane, Scene, Stage

    public static void main(String[] args) {
        launch(args);
    }

    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets
        window = primaryStage;
        window.setTitle("SPACE-INVADER");
        window.getIcons().add(new Image("/boss.png"));
        window.setScene(new Scene(createMenu()));
        window.setResizable (false);
        window.show();

        // TODO: initialize primaryStage, scene, canvas, UI labels, root pane
        // TODO: set up event handlers
        // TODO: initialize gameObjects list with player
        // TODO: create menu and switch to menu scene
        // TODO: set up AnimationTimer game loop and start it
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
                    if (player.getHealth() <= 0) {
                        gameOver = true;
                        try {
                            showLosingScreen();
                            stop();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    gameupdate(elapsedTime);
                    gamerender(gc);
                    lastUpdate = now;
                }
            }
        };
        timer.start();

    }

    protected void gamerender(GraphicsContext gc) {
        ImagePattern uni = new ImagePattern(new Image("/universe.jpg"));
        gc.setFill(uni);
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
        if (bossExists && boss != null) {
            boss.render(gc);
            for (int i = eBullets.size()-1; i >= 0; i--) {
                eBullets.get(i).render(gc);
            }
            for (int i = moon.size()-1; i >= 0; i--) {
                moon.get(i).render(gc);
            }
        }
        if (levelUpShown > 0) {
            showTempMessage("   LEVEL UP!", player.x+20, player.y-10);
        }
    }
    
    protected void gameupdate(double elapsedTime) {
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
            if (up == null)
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
            if (!up.isEmpty()) {
                up.get(0).update();
                if (up.get(0).isDead())
                    up.remove(0);
            }
            if (bossExists && boss != null) {
                boss.update();
                bossExists = !boss.isDead();
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
                    if (eBullets.get(i).isDead()) {
                        eBullets.remove(i);
                    }
                }
                for (int i = moon.size() - 1; i >= 0; i--) {
                    moon.get(i).up(boss);
                    if (moon.get(i).isDead()) {
                        moon.remove(i);
                    }
                }
            }
            if (boss != null && boss.exploding) {
                for (int i = eBullets.size() - 1; i >= 0; i--) {
                    eBullets.get(i).setExploding(true);
                }
                for (int i = moon.size() - 1; i >= 0; i--) {
                    moon.get(i).setExploding(true);
                    
                }
            }
            if (boss != null && boss.isDead()) {
                boss = null;
                bossExists = false;
            }
        }
    }

    private void spawnEnemy() {
        if (Math.random() < 0.0015 * score + 0.05) {
            enemies.add(new Enemy(new Random().nextInt(470) + 2, 0));
        }

    }

    private void spawnPowerUp() {
        if ((score+1)%3==0&&up.isEmpty()) {
            up.add(new PowerUp(new Random().nextInt(325) + 2, new Random().nextInt(50) + 600));
        }
    }

    private void spawnBossEnemy() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            enemies.get(i).setExploding(true);
        }
        boss = new BossEnemy(200, 0);
        eBullets = new ArrayList<>();
        moon = new ArrayList<>();
        bossExists = true;
    }

    private void checkCollisions() {
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                if (!enemy.exploding) {
                    player.setHealth(player.getHealth() - 1);
                    player.setTakingdame(true);
                }
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
        if (!up.isEmpty()) {
            for (int i = bullets.size() - 1; i >= 0; i--) {
                if (bullets.get(i).getBounds().intersects(up.get(0).getBounds())) {
                    up.get(0).setDead(true);
                    ;
                    bullets.get(i).setDead(true);
                    player.Powerup();
                    score++;
                    levelUpShown = 15;

                }
            }
            if (player.getBounds().intersects(up.get(0).getBounds())) {
                player.Powerup();
                score++;
                up.get(0).setDead(true);
                levelUpShown = 15;
            }
        }
        if (bossExists) {
            for (Bullet bullet : bullets) {
                if (bullet.getBounds().intersects(boss.getBounds())) {
                    if (!bullet.exploding&&!boss.exploding) {
                        score++;
                        bullet.setExploding(true);
                        boss.takeDamage();
                        bullet.setExploding(true);
                    }

                }
                for (int i = eBullets.size() - 1; i >= 0; i--) {
                    if (bullet.getBounds().intersects(eBullets.get(i).getBounds())) {
                        if (!bullet.exploding||eBullets.get(i).exploding) {
                            bullet.setExploding(true);
                            eBullets.remove(i);
                            score++;
                        }
                    }
                }

            }
            for (int i = eBullets.size() - 1; i >= 0; i--) {
                if (player.getBounds().intersects(eBullets.get(i).getBounds())) {
                    if (!eBullets.get(i).exploding) {
                        eBullets.get(i).setExploding(true);
                        player.setHealth(player.getHealth() - 1);
                        player.setTakingdame(true);
                    }
                }
            }
            if (player.getBounds().intersects(boss.getBounds())&&!player.exploding&&!boss.exploding) {
                player.setHealth(player.getHealth() - 3);
                player.setTakingdame(true);
                player.setExploding(true);
            }
            for (int i = moon.size() - 1; i >= 0; i--) {
                if (player.getBounds().intersects(moon.get(i).getBounds())) {
                    if (!player.exploding&&!moon.get(i).exploding) {
                        player.setHealth(player.getHealth() - 1);
                        player.setTakingdame(true);
                        player.setExploding(true);
                    }
                }
                for (Bullet bullet : bullets) {
                    if (!moon.get(i).exploding&&!bullet.exploding) {
                        if (moon.get(i).getBounds().intersects(bullet.getBounds())) {
                            moon.get(i).setExploding(true);
                            bullet.setExploding(true);
                        }
                    }
                }
            }

        }
    }

    private void checkEnemiesReachingBottom() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).getY() > 830){
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
        if (eBullets != null) {
            for (int i = eBullets.size() - 1; i >= 0; i--) {
                if (eBullets.get(i).getY() > 800) {
                    eBullets.remove(i);
                }

            }
        }
    }

    // UI and game state methods

private void showLosingScreen() throws IOException {
    // Load FXML
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/losing.fxml"));
    Pane losingPane = loader.load();
    deadp.play();

    // Get controller and inject game reference + score
    Losing controller = loader.getController();
    controller.setGame(this);
    controller.setScore1(score);
    MediaView mediaView = new MediaView(victoryp);
    losingPane.getChildren().add(mediaView);
    victoryp.setCycleCount(MediaPlayer.INDEFINITE);
    victoryp.play();
    // Set scene
    Scene scene = new Scene(losingPane);
    window.setScene(scene);
}


    private void restartGame() {
        // TODO: reset gameObjects, lives, score and switch back to game scene
        gameRunning = true;
        initEventHandlers(gameScene);
        window.setScene(gameScene);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        up = new ArrayList<>();
        score = 0;
        gameOver = false;
        bossExists = false;
        boss = null;
        player = new Player(WIDTH / 2, HEIGHT - 40);
    }

    private void resetGame() {
        // TODO: stop game loop and call showLosingScreen
    }

    private void initEventHandlers(Scene scene) {

        scene.setOnKeyPressed(event -> handleKeyPress(event));
        scene.setOnKeyReleased(event -> handleKeyRelease(event));
    }

    private Pane createMenu() {
        FXMLLoader root = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Pane a;
        try {
            a = root.load();
            Menu men = root.getController();
            men.setGame(this);
            themep.setCycleCount(MediaPlayer.INDEFINITE);
            MediaView theme = new MediaView(themep);
            a.getChildren().add(theme);
            themep.play();
            return a;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Pane();
        }
    }
    
    private void pause() {
        timer.stop();
        gameRunning = false;
        try {
            Pane a=FXMLLoader.load(getClass().getResource("/pause.fxml"));
            Scene scene = new Scene(a);
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    window.setScene(gameScene);
                    gameRunning = true;
                    timer.start();
                }
                
                if (event.getCode() == KeyCode.ESCAPE) {
                    window.setScene(new Scene(createMenu()));
                    gameRunning = true;
                }
            });
            window.setScene(scene);
            a.requestFocus();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void showTempMessage(String message, double x, double y) {
        if (levelUpShown < 0) {
            levelUpShown = 0;
        }
        else {
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setFont(Font.font(12));
            gc.setFill(Color.GREEN);
            gc.fillText(message, x, y);
            levelUpShown--;
        }
    }

    StackPane root = new StackPane(canvas);
    Scene gameScene = new Scene(root);
    public void startGame() {
        restartGame();
        gameloop(gc);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT)
            player.setMoveLeft(true);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(true);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(true);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(true);
        if (event.getCode() == KeyCode.SPACE) { player.setShooting(true); }
        if (event.getCode() == KeyCode.ESCAPE) { if(gameRunning) pause(); }
    }

    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) player.setMoveLeft(false);
        if (event.getCode() == KeyCode.RIGHT)player.setMoveRight(false);
        if (event.getCode() == KeyCode.UP)player.setMoveForward(false);
        if (event.getCode() == KeyCode.DOWN)player.setMoveBackward(false);
        if (event.getCode() == KeyCode.SPACE)player.setShooting(false);
    }
}
