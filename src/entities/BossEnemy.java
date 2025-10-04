package entities;

import java.util.List;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;
// import javafx.util.Duration;

/**
 * Skeleton for BossEnemy.
 */
public class BossEnemy extends Enemy {

    // Health points of the boss
    public int health;

    double time;
    // Hitbox dimensions
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    int phase;

    // Media dead = new Media(getClass().getResource("/explosion.mp3").toExternalForm());
    // Horizontal movement speed
    private double FireRate=50;
    static final Image BOSS_IMAGE = new Image(BossEnemy.class.getResourceAsStream("/boss.png"));
    /**
     * Constructs a BossEnemy at the given coordinates.
     * @param x initial X position
     * @param y initial Y position
     */
    public BossEnemy(double x, double y) {
        super(x, y);
        setHealth(50);
        setDead(false);
        phase = 0;
    }

    /**
     * Update boss's position and behavior each frame.
     */
    @Override
    public void update() {
        if(!exploding){
        time += (((double)(new Random().nextInt(8)+3))/100)*((51-(double)this.health)/40);
        x = 200 + 70 * Math.sin(1.5 * time) + 150 * Math.sin(1.1 * time)*Math.cos(time*1.5);
        y = 600 * Math.pow(Math.sin(1.1 * time), 2) + 175 * Math.abs(Math.sin(1.9 * time)*Math.cos(time*0.2));
        FireRate--;
        }   
        setDead(explosionStep>=30);
    }


    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        health--;
        if (health <= 0) {
            setExploding(true);

        }

    }
    @Override
    public void setExploding(boolean exploding) {
        // MediaPlayer dead1 = new MediaPlayer(dead);
        // dead1.seek(Duration.millis(0));
        // dead1.play();
        this.exploding = exploding;
        explosionStep = 0;
    }

    /**
     * Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<EnemyBullet> newObjects) {
        if (FireRate <= 0) {
            double angle = (new Random().nextInt(10)+10)/10;
            newObjects.add( new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI / 2), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI / 2)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI * 3 / 2), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI * 3 / 2)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle)));
            FireRate = new Random().nextInt(50)+25;
        }
    }

    public void phase(List<Satellite> moon,int count) {
        moon.clear();
    for (int i = 0; i < count; i++) {
        double angle = 2 * Math.PI * i / count - Math.PI / 2; 
        double x = centerX() + 180 * Math.cos(angle);
        double y = centerY() + 180 * Math.sin(angle);
        moon.add(new Satellite(x, y,angle));
        System.out.println( x + ", " + y);
}
    }

    /**
     * Render the boss on the canvas.
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        if (exploding&&explosionStep<=30) {
            gc.drawImage(EXPLOSION_IMAGE, (explosionStep/2) % 3 * 128, (explosionStep / 2)%3 * 128 + 1, 128, 128, x, y, WIDTH+100,
                    HEIGHT+100);
            explosionStep += 1;
        } 
        else
            gc.drawImage(BOSS_IMAGE, x, y,WIDTH,HEIGHT);
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    @Override
    public double getHeight() {
        return HEIGHT;
    }
    @Override
    public double getWidth() {
        return WIDTH;
    }

    public double centerX() {
        return x + WIDTH / 2;
    }
    public double centerY() {
        return y + HEIGHT / 2;
    }
    @Override
    public Bounds getBounds() {
        // TODO Auto-generated method stub
        return new javafx.scene.shape.Rectangle(
            x+11,
            y+20,
            getWidth()-38,
            getHeight()-50
        ).getBoundsInLocal();
    }
}
