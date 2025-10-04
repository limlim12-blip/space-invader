package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;
// import javafx.util.Duration;

/**
 * Skeleton for Bullet. Students must implement movement,
 * rendering, and state management.
 */
public class Bullet extends GameObject {

    // Width and height of the bullet
    public static final int WIDTH = 12;
    public static final int HEIGHT = 17;

    // Movement speed of the bullet
    private static final double SPEED = 30;
    public boolean exploding;
    public int explosionStep = 0;
    // Flag to indicate if bullet should be removed
    private boolean dead;

    // Media boom = new Media(getClass().getResource("/explosion1.mp3").toExternalForm());
    static final Image EXPLOSION_IMAGE = new Image(Bullet.class.getResourceAsStream("/explosion.png"));
    /**
     * Constructs a Bullet at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    public Bullet(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        setDead(false);
    }

    /**
     * Updates bullet position each frame.
     */
    @Override
    public void update() {
        if(!exploding) y -= SPEED;
        if (explosionStep >= 15) {
            setDead(true);
        }
    }

    /**
     * Renders the bullet on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
           if (exploding) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 50,
                    50);
            explosionStep += 1;
        } 
        else {
            gc.setFill(Color.rgb(250, 0, 0, 0.3));
            gc.fillOval(x, y, WIDTH, HEIGHT);
            }
    }

    public void setExploding(boolean exploding) {
        // MediaPlayer boom1 = new MediaPlayer(boom);
        // boom1.setRate(4.0);
        // boom1.seek(Duration.millis(0));
        // boom1.play();
        this.exploding = exploding;
        explosionStep = 0;
    }
    /**
     * Returns current width of the bullet.
     * @return WIDTH
     */
    @Override
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Returns current height of the bullet.
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
