package entities;

import javafx.scene.canvas.GraphicsContext;
import java.util.List;

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

    /**
     * Constructs a BossEnemy at the given coordinates.
     * @param x initial X position
     * @param y initial Y position
     */
    public BossEnemy(double x, double y) {
        super(x, y);
        // TODO: initialize health, speeds, and load resources
    }

    /**
     * Update boss's position and behavior each frame.
     */
    @Override
    public void update() {
        // TODO: implement vertical and horizontal movement
    }

    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        // TODO: decrement health, mark dead when <= 0
    }

    /**
     * Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<GameObject> newObjects) {
        // TODO: implement shooting logic (spawn EnemyBullet)
    }

    /**
     * Render the boss on the canvas.
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        // TODO: draw boss sprite or placeholder
    }
}
