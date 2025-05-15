package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Skeleton for EnemyBullet. Students must implement movement,
 * rendering, and state management.
 */
public class EnemyBullet extends GameObject {

    // Dimensions of the enemy bullet
    public static final int WIDTH = 4;
    public static final int HEIGHT = 20;

    // Movement speed of the bullet
    private static final double SPEED = 3;

    // Flag indicating whether the bullet should be removed
    private boolean dead;
    private boolean exploding;
    private int explosionStep = 0;

    // Hình ảnh hiệu ứng nổ
    private static final Image EXPLOSION_IMAGE = new Image(EnemyBullet.class.getResourceAsStream("/explosion.png"));

    /**
     * Constructs an EnemyBullet at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    public EnemyBullet(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        this.dead = false; // Đảm bảo viên đạn chưa bị đánh dấu chết khi được tạo
    }

    /**
     * Updates bullet position each frame.
     */
    @Override
    public void update() {
        if (exploding) {
            explosionStep++;
            if (explosionStep >= 15) {
                setDead(true);
            }
        } else {
            y += SPEED;
            if (y > SpaceShooter.HEIGHT) {
                setDead(true);
            }
        }
    }

    /**
     * Renders the bullet on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (exploding && explosionStep < 15) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 50, 50);
            explosionStep++;
        } else if (!exploding) {
            gc.setFill(javafx.scene.paint.Color.PURPLE.deriveColor(0, 1.0, 1.0, 0.7));
            gc.fillRect(x, y, WIDTH, HEIGHT);
        }
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

    /**
     * Sets the exploding state and resets explosion step if starting.
     * @param exploding true to start explosion effect
     */
    public void setExploding(boolean exploding) {
        this.exploding = exploding;
        if (exploding) {
            explosionStep = 0;
        }
    }
}