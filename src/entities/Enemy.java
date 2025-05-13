package entities;

import java.util.ArrayList;

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
    static final Image EXPLOSION_IMAGE = new Image(Enemy.class.getResourceAsStream("/res/explosion.png"));
    // Movement speed
    public static double SPEED = 5;

    
    // Flag to indicate if enemy should be removed
    private boolean dead;
    public boolean exploding;
    public int explosionStep = 0;
    double time = 0;

    /**
     * Constructs an Enemy at the given coordinates.
     * @param x initial X position
     * @param y initial Y position
     */
    public Enemy(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        setDead(false);
    }

    /**
     * Updates enemy position each frame.
     */
    @Override
    public void update() {
        y += SPEED;
        setDead(explosionStep>=15);
    }
    
    /**
     * Renders the enemy on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (exploding) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 50,
                    50);
            explosionStep += 1;
        } 
        else
            gc.drawImage(ENEMY_IMAGE, x, y,WIDTH,HEIGHT);

    }
    public void setExploding(boolean exploding) {
        this.exploding = exploding;
        explosionStep = 0;
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
