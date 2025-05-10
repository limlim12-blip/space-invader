package entities;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Skeleton for Enemy. Students must implement movement, rendering,
 * and death state without viewing the original implementation.
 */
public class Enemy extends GameObject {

    // Hitbox dimensions
    protected static final int WIDTH = 30;
    protected static final int HEIGHT = 30;

    ArrayList<Enemy> enemies=new ArrayList<>();
    static final Image ENEMY_IMAGE = new Image(Enemy.class.getResourceAsStream("/res/enemy.png"));
    // Movement speed
    public static double SPEED = 1;

    // Flag to indicate if enemy should be removed
    private boolean dead;

    /**
     * Constructs an Enemy at the given coordinates.
     * @param x initial X position
     * @param y initial Y position
     */
    public Enemy(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        dead = false;
        // TODO: load sprite if needed and initialize dead flag
    }

    /**
     * Updates enemy position each frame.
     */
    @Override
    public void update() {
        // TODO: implement vertical movement by SPEED
         y += SPEED*10;
        
    }
    
    /**
     * Renders the enemy on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(ENEMY_IMAGE, x, y,WIDTH,HEIGHT);
        // TODO: draw sprite or fallback shape (e.g., colored rectangle)
    }
    
    /**
     * Returns the current width of the enemy.
     * @return WIDTH
     */
    @Override
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Returns the current height of the enemy.
     * @return HEIGHT
     */
    @Override
    public double getHeight() {
        return HEIGHT;
    }

    /**
     * Marks this enemy as dead (to be removed).
     * @param dead true if enemy should be removed
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Checks if this enemy is dead.
     * @return true if dead, false otherwise
     */
    @Override
    public boolean isDead() {
        return dead;
    }
}
