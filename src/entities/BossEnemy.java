package entities;

import java.util.List;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Skeleton for BossEnemy. Students must implement behavior
 * without viewing the original implementation.
 */
class satellite extends Enemy
{

    double time=0;
    public satellite(double x, double y,double angle) {
        super(x, y);
        this.time = angle;
    }
    public void up(BossEnemy boss) {
        if (!boss.exploding||!exploding) {
            
        
        time += 0.05*((51-(double)boss.health)/10);
        x = boss.centerX() + 180 * Math.cos(time);
        y = boss.centerY() + 180 * Math.sin(time);
        }
    }
    @Override
    public void render(GraphicsContext gc) {
         if (exploding&&explosionStep<=15) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 80, 80);
            explosionStep += 1;
        }
        else
            setExploding(false);
           
        if(!exploding) gc.drawImage(ENEMY_IMAGE, x, y,WIDTH,HEIGHT);
    }

    @Override
    public void update() {
        return;
    }
}
public class BossEnemy extends Enemy {

    // Health points of the boss
    public int health;

    double time;
    // Hitbox dimensions
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    int phase;

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
        setHealth(10);
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
        x = 200 + 70 * Math.sin(1.5 * time) + 150 * Math.sin(1.1 * time);
        y = 600 * Math.pow(Math.sin(0.4 * time), 2) + 175 * Math.abs(Math.sin(0.9 * time));
        FireRate--;
    }
        if(explosionStep>=30)
        setDead(true);
    }


    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        health--;
        setExploding(health<=0);
    }

    /**
     * Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<EnemyBullet> newObjects) {
        if (FireRate <= 0&&y<200) {
            double angle = (new Random().nextInt(10)+10)/10;
            newObjects.add( new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI / 2), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI / 2)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle + Math.PI * 3 / 2), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle + Math.PI * 3 / 2)));
            newObjects.add(new EnemyBullet(centerX() + new Random().nextInt(50)+50 * Math.cos(time*angle), centerY() + new Random().nextInt(50)+50 * Math.sin(time*angle)));
            FireRate = new Random().nextInt(50)+50;
        }
    }

    public void phase(List<satellite> moon,int count) {
        moon.clear();
    for (int i = 0; i < count; i++) {
        double angle = 2 * Math.PI * i / count - Math.PI / 2; // Rotate so first satellite is at top
        double x = centerX() + 180 * Math.cos(angle);
        double y = centerY() + 180 * Math.sin(angle);
        moon.add(new satellite(x, y,angle));
        System.out.println("Satellite at: " + x + ", " + y);
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
        return this.HEIGHT;
    }
    @Override
    public double getWidth() {
        return this.WIDTH;
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
