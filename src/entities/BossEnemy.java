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
        time += 0.05*((51-(double)boss.health)/10);
        x = boss.centerX() + 180 * Math.cos(time);
        y = boss.centerY() + 180 * Math.sin(time);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (exploding && explosionStep <= 60) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 80, 80);
            explosionStep++;
            if (explosionStep > 60) {
                setDead(true);
            }
        } else {
            setExploding(false);
        }
        if (!exploding) gc.drawImage(ENEMY_IMAGE, x, y, WIDTH, HEIGHT);
    }

    @Override
    public void update() {
        if (!exploding && SpaceShooter.instance.boss != null) {
            up(SpaceShooter.instance.boss);
        }
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
    private boolean tookDamageThisFrame = false;
    public boolean exploding = false;
    public int explosionStep = 0;

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
        phase = 0;
    }

    /**
     * Update boss's position and behavior each frame.
     */

    @Override
    public void update() {
        time += (((double) (new Random().nextInt(8) + 3)) / 100) * ((51 - (double) this.health) / 15);
        x = 200 + 70 * Math.sin(1.5 * time) + 150 * Math.sin(1.1 * time);
        y = 600 * Math.pow(Math.sin(0.4 * time), 2) + 175 * Math.abs(Math.sin(0.9 * time));
        FireRate--;
        if (exploding) {
            explosionStep++;
            if (explosionStep >= 60) {
                setDead(true);
            }
        }
        tookDamageThisFrame = false;
    }


    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        if (!tookDamageThisFrame) {
            health--;
            tookDamageThisFrame = true;
            if (health == 40 || health == 30 || health == 20 || health == 10) {
                phase(SpaceShooter.instance.moon, 4 - (health / 10));
            }
            if (health <= 0) {
                setExploding(true);
            }
        }
    }

    /**
     * Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<EnemyBullet> newObjects) {
        if (FireRate <= 0) {
            double angle = (new Random().nextInt(10) + 10) / 10.0;
            newObjects.add(new EnemyBullet(centerX() + 50 * Math.cos(time * angle + Math.PI), centerY() + 50 * Math.sin(time * angle + Math.PI)));
            newObjects.add(new EnemyBullet(centerX() + 50 * Math.cos(time * angle + Math.PI / 2), centerY() + 50 * Math.sin(time * angle + Math.PI / 2)));
            newObjects.add(new EnemyBullet(centerX() + 50 * Math.cos(time * angle + Math.PI * 3 / 2), centerY() + 50 * Math.sin(time * angle + Math.PI * 3 / 2)));
            newObjects.add(new EnemyBullet(centerX() + 50 * Math.cos(time * angle), centerY() + 50 * Math.sin(time * angle)));
            FireRate = 50;
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
        if (exploding && explosionStep <= 60) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 100, 100);
            explosionStep++;
        } else if (!exploding) {
            gc.drawImage(BOSS_IMAGE, x, y, WIDTH, HEIGHT);
        }
    }
    public void setExploding(boolean exploding) {
        this.exploding = exploding;
        if (exploding) {
            explosionStep = 0;
        }
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
        return new javafx.scene.shape.Rectangle(x, y, WIDTH, HEIGHT).getBoundsInLocal();
    }
}
