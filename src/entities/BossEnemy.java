package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

/**
 * Skeleton for BossEnemy. Students must implement behavior
 * without viewing the original implementation.
 */
public class BossEnemy extends Enemy {

    // Health points of the boss
    private int health;

    // Hitbox dimensions
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    // Horizontal movement speed
    private double horizontalSpeed;
    static final Image BOSS_IMAGE = new Image(Enemy.class.getResourceAsStream("/res/boss.png"));
    /**
     * Constructs a BossEnemy at the given coordinates.
     * @param x initial X position
     * @param y initial Y position
     */
    public BossEnemy(double x, double y) {
        super(x, y);
        setHealth(50);
    }

    /**
     * Update boss's position and behavior each frame.
     */
    @Override
    public void update() {
        y += 2;
    }


    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        // TODO: decrement health, mark dead when <= 0
        health--;
        if(health<0) setDead(true);
    }

    /**
     * Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<EnemyBullet> newObjects) {
        // TODO: implement shooting logic (spawn EnemyBullet)
            newObjects.add(new EnemyBullet(x+WIDTH/2-4, y - HEIGHT / 2));
    }

    /**
     * Render the boss on the canvas.
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        // TODO: draw boss sprite or placeholder
            gc.drawImage(BOSS_IMAGE, x, y,WIDTH,HEIGHT);
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
