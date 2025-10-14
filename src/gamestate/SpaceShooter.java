package gamestate;
import py4j.GatewayServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.BossEnemy;
import entities.Bullet;
// import entities.DataGame;
import entities.Enemy;
import entities.EnemyBullet;
import entities.Player;
import entities.PowerUp;
import entities.Satellite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer                                                                       ;
// import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyCode;
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
    private boolean gameRunning;
    private boolean gameOver;
    private Player player;

    List<Enemy> enemies;
    List<Bullet> bullets;
    List<PowerUp> up;   
    BossEnemy boss;
    List<EnemyBullet> eBullets;
    List<Satellite> moon;
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
                                                //! MediaPlayer conflict with linux 
    // MediaPlayer themep = new MediaPlayer(new Media(getClass().getResource("/starwar.mp3").toExternalForm()));
    // MediaPlayer victoryp = new MediaPlayer(new Media(getClass().getResource("/Victory.mp3").toExternalForm()));
    // MediaPlayer deadp = new MediaPlayer(new Media(getClass().getResource("/dead.mp3").toExternalForm()));
    
    public static void main(String[] args) {
        launch(args);
    }
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Stage window;
    StackPane root = new StackPane(canvas);
    Scene gameScene = new Scene(root);

    @Override
    public void start(Stage primaryStage) throws Exception {
        // This is where you add scenes, layouts, and widgets
        window = primaryStage;
        window.setTitle("SPACE-INVADER");
        window.getIcons().add(new Image("/boss.png"));
        window.setScene(new Scene(createMenu()));
        window.setResizable (false);
        window.show();
        GatewayServer gatewayServer = new GatewayServer(this, 25333);
        System.out.println("Gateway Server Started");
        gatewayServer.start();
    }
    // Game mechanics stubs
    AnimationTimer timer;
    private void gameloop(GraphicsContext gc) {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
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
                    gamerender(gc);
            }
        };
        timer.start();

    }
    
    long HUMAN_MODE = 60; 
    long AI_MODE = 3_000; 
    long FPNS = 1_000_000_000 / AI_MODE;  // SET FRAME PER NANOSECOND 
    public Thread updateThread=null;
    
    int steps = 0;
    public void updateThread() {
        updateThread = new Thread(() -> {
            long lastUpdateTime = System.nanoTime();
            long lag = 0;
            while (gameRunning) {
                long now = System.nanoTime();
                long delta = now - lastUpdateTime;
                lastUpdateTime = now;
                lag += delta;

                while (lag >= FPNS) {
                    if (!gameOver){ gameupdate();
                        System.out.println(steps);
                        steps++;
                    }
                    lag -= FPNS;
                }
                if (lag > 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            }
        }, "spaceinvader");

        updateThread.start();
    }

    public void stopUpdateThread() {     //! khong bao gio dung thread java nua
    if (updateThread != null) {
        gameRunning = false; 
        
        updateThread.interrupt(); 
        
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
        updateThread = null;
    }
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
            showTempMessage("   LEVEL UP!", player.getX()+20, player.getY()-10);
        }
    }
    
    protected void gameupdate() {
        if (!bossExists) spawnEnemy();
        if (score!= 0&& score %40 == 0 && !bossExists) spawnBossEnemy();
        if (up == null) spawnPowerUp();


        enemies.forEach(e->e.update());
        bullets.forEach(b->b.update());
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
            }
            for (int i = moon.size() - 1; i >= 0; i--) {
                moon.get(i).up(boss);
            }
            eBullets.removeIf(b -> b == null || b.isDead());
            moon.removeIf(b -> b == null || b.isDead());
        }
        if (boss != null && boss.exploding) {
            for (int i = eBullets.size() - 1; i >= 0; i--) {
                eBullets.get(i).setDead(true);;
            }
            for (int i = moon.size() - 1; i >= 0; i--) {
                moon.get(i).setDead(true);;
                
            }
        }
        checkEnemiesReachingBottom();
        checkCollisions();
        spawnPowerUp();
        player.update();
        if (player.shooting) player.shoot(bullets);
        enemies.removeIf(b -> b == null || b.isDead());
        bullets.removeIf(b -> b == null || b.isDead());
    }

    private void spawnEnemy() {
        if (Math.random() < 0.01 + (0.25 * Math.tanh(score / 1000.0))) {
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
        for (int i = enemies.size() - 1; i >= 0;i--) {
            if (player.getBounds().intersects(enemies.get(i).getBounds())) {
                if (!enemies.get(i).exploding) {
                    player.setHealth(player.getHealth() - 1);
                    player.setTakingdame(true);
                    enemies.get(i).setExploding(true);
                }
            }

            if (bullets != null) {
                for (int j = bullets.size() - 1; j >= 0; j--) {
                    if (bullets.get(j).getBounds().intersects(enemies.get(i).getBounds())) {
                        if (!enemies.get(i).exploding) {
                            score++;
                            bullets.get(j).setDead(true);
                            enemies.get(i).setExploding(true);
                        }

                    }
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
                        if(Math.random()<0.1) score++;
                        bullet.setExploding(true);
                        boss.takeDamage();
                    }

                }
                for (int i = eBullets.size() - 1; i >= 0; i--) {
                    if (bullet.getBounds().intersects(eBullets.get(i).getBounds())) {
                        if (!bullet.exploding||eBullets.get(i).exploding) {
                            bullet.setExploding(true);
                            eBullets.remove(i);
                            if(Math.random()<0.05) score++;
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
        for (Enemy e : enemies) {
            if (e.getY() > 830){
                player.setHealth(player.getHealth() - 1);
            }

        }
        enemies.removeIf(b -> b.getY() >830 );
        bullets.removeIf(b -> b.getY() < 0);
        if (!up.isEmpty()&&up.get(0).getY() > 800) {
            up.remove(0);
        }
        if (eBullets != null) {
            eBullets.removeIf(b -> b.getY() >830);
        }
        
    }

    // UI and game state methods

    // Load FXML

    public void resetGame() {
        stopUpdateThread();
        gameRunning = true;
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        up = new ArrayList<>();
        score = 0;
        gameOver = false;
        bossExists = false;
        boss = null;
        player = new Player(WIDTH / 2, HEIGHT - 40);
        Platform.runLater(() -> {
            window.setScene(gameScene);
        }
    );}

    private void initEventHandlers(Scene scene) {
        EventHandler ev = new EventHandler();
        scene.setOnKeyPressed(event ->  ev.handleKeyPress(event, player, gameRunning, this ));
        scene.setOnKeyReleased(event -> ev.handleKeyRelease(event, player, gameRunning));
    }

    
    protected void pause() {
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

    private Pane createMenu() {
        FXMLLoader root = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Pane a;
        try {
            a = root.load();
            Menu men = root.getController();
            men.setGame(this);
            // themep.setCycleCount(MediaPlayer.INDEFINITE);
            // MediaView theme = new MediaView(themep);
            // a.getChildren().add(theme);
            // themep.play();
            return a;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Pane();
        }
    }
    private void showLosingScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/losing.fxml"));
        Pane losingPane = loader.load();
        
        // Get controller and inject game reference + score
        Losing controller = loader.getController();
        controller.setGame(this);
        controller.setScore1(score);
        // deadp.play(); 
        // MediaView mediaView = new MediaView(victoryp);                //! conflict with linux
        // losingPane.getChildren().add(mediaView);
        // victoryp.setCycleCount(MediaPlayer.INDEFINITE);
        // victoryp.play();
        // Set scene
        Scene scene = new Scene(losingPane);
        window.setScene(scene);
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

    public void startGame() {
        resetGame();
        initEventHandlers(gameScene);
        gameloop(gc);
        updateThread();
    }

    public void Action(int action){
        // gameupdate();        //!sync wiht Agent loop
        switch (action) {
            case 0: {
                player.setMoveRight(false);
                player.setMoveLeft(true);
                player.setShooting(false);
                break;
            }
            case 1:{
                player.setMoveRight(false);
                player.setMoveLeft(false);
                player.setShooting(false);
                break;
            }
            case 2:{
                player.setMoveRight(true);
                player.setMoveLeft(false);
                player.setShooting(false);
                break;
            }
            case 3: {
                player.setShooting(true);
                player.setMoveRight(false);
                player.setMoveLeft(false);
                break;
            }
        
        }
    }

    int reward = 0;
    
    public int getReward() {
        int bosshealth = 50;
        if (boss!=null)
            bosshealth = boss.getHealth();
        else
            bosshealth = 50;
        reward = (player.getHealth()-20)*3+score*2+(50-bosshealth);
        return reward;
        
    }

    public SpaceShooter getGame(){
        return this;
    }
    public int getScore() {
        return this.score;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public boolean getGameRunning() {
        return gameRunning;
    }
    public double[][] getEnemies() {
        double[][] arr = new double[10][2];
        for (int i = 0; i < Math.min(enemies.size(),10); i++) {
            Enemy e = enemies.get(i);
            arr[i][0] = e.getX();
            arr[i][1] = e.getY();
        }
        return arr;
    }

    public double[] getBoss() {
        if(boss==null){
            double[] a ={-1,-1};
            return a;
        }
        double[] arr = new double[2];
        arr[0] = boss.getX();
        arr[1] = boss.getY();
        return arr;
    }


    public Player getPlayer() {
        return player;
    }

    public double[][] getMoon() {
        if(boss==null)
            return new double[10][2];
        double[][] arr = new double[10][2];
        for (int i = 0; i < moon.size(); i++) {
            Satellite e = moon.get(i);
            arr[i][0] = e.getX();
            arr[i][1] = e.getY();
        }
        return arr;
    }
    public double[][] geteBullets() {
        if(boss==null)
            return new double[20][2];
        double[][] arr = new double[20][2];
        for (int i = 0; i < Math.min(bullets.size(),20); i++) {
            Bullet e = bullets.get(i);
            arr[i][0] = e.getX();
            arr[i][1] = e.getY();
        }
        return arr;
    }
    public double[][] getUp() {
        double[][] arr = new double[1][2];
        for (int i = 0; i < up.size(); i++) {
            PowerUp e = up.get(i);
            arr[i][0] = e.getX();
            arr[i][1] = e.getY();
        }
        return arr;
    }
}
