package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Skeleto for EnemyBullet. Students must implement movement,
 * rendering, and state management.
 */
public class EnemyBullet extends GameObject {

    // Dimensions of the enemy bullet
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;

    // Movement speed of the bullet
    private static final double SPEED = 3;
    public boolean exploding;
    public int explosionStep = 0;
    // Flag to indicate if bullet should be removed
    private boolean dead;

    static final Image EXPLOSION_IMAGE = new Image(Bullet.class.getResourceAsStream("/res/explosion.png"));
    static final Image BOMB_IMAGE = new Image(Bullet.class.getResourceAsStream("/res/bomb.png"));
    /**
     * Constructs an EnemyBullet at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    @Override
    public void update() {
        y += SPEED;
        setDead(explosionStep>=15);
    }
    public EnemyBullet(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        setDead(false);
    }
    
    /**
     * Updates bullet position each frame.
     */

    /**
     * Renders the bullet on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        // TODO: draw bullet (e.g., filled rectangle or sprite)
           if (exploding) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 30,
                    30);
            explosionStep += 2;
        } 
        else {
            gc.drawImage(BOMB_IMAGE, x, y,WIDTH,HEIGHT);
            }
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }
    /**
     * Returns the width of the bullet.
     * @return WIDTH
     */
    @Override
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Returns the height of the bullet.
     * @return HEIGHT
     */
    @Override
    public double getHeight() {
        return HEIGHT;
    }

    /**
     * Marks this bullet as dead (to be removed).
     * @param dead true if bullet should be removed
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Checks if this bullet is dead.
     * @return true if dead, false otherwise
     */
    @Override
    public boolean isDead() {
        return dead;
    }

}
