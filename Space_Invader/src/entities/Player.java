package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Skeleton for Player. Students must implement movement, rendering,
 * shooting, and health/state management.
 */
public class Player extends entities.GameObject {

    // Hitbox dimensions
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    static final Image PLAYER_IMAGE = new Image(Player.class.getResourceAsStream("/res/player.png"));
    static final Image DAMAGED_IMAGE = new Image(Player.class.getResourceAsStream("/res/player.png"));
    // Movement speed
    private static final double SPEED = 5;


    // Movement flags
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveForward;
    private boolean moveBackward;

    // Player health
    private int health;

    // State flag for removal
    private boolean dead=false;
    boolean collision=false;

    
    /**
     * Updates player position based on movement flags.
     */
    @Override
    public void update() {
        if (moveLeft && x > 0) x -= SPEED;  // Chỉ cho phép di chuyển khi chưa chạm biên trái
        if (moveRight && x < SpaceShooter.WIDTH - WIDTH) x += SPEED;  // Biên phải hợp lý
        if (moveForward && y > SpaceShooter.HEIGHT / 2) y -= SPEED;  // Không cho đi quá xa lên trên
        if (moveBackward && y < SpaceShooter.HEIGHT - HEIGHT) y += SPEED;  // Không vượt quá biên dưới
    }
    
    public boolean collision(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }
    /**
     * Renders the player on the canvas.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (health < 5) {
            gc.drawImage(DAMAGED_IMAGE, x, y, WIDTH, HEIGHT); // Hiển thị khi bị thương
        } else {
            gc.drawImage(PLAYER_IMAGE, x, y, WIDTH, HEIGHT);
        }
    }
    
    /**
     * Sets movement flags.
     */
    public void setMoveLeft(boolean moveLeft) {
        // TODO: update moveLeft flag
        this.moveLeft = moveLeft;
    }
    
    public void setMoveRight(boolean moveRight) {
        // TODO: update moveRight flag
        this.moveRight = moveRight;
    }
    
    public void setMoveForward(boolean moveForward) {
        // TODO: update moveForward flag
        this.moveForward = moveForward;
    }
    
    public void setMoveBackward(boolean moveBackward) {
        // TODO: update moveBackward flag
        this.moveBackward = moveBackward;
    }
    
    /**
     * Shoots a bullet from the player.
     */
    public void shoot(List<Bullet> bullets) {
        bullets.add(new Bullet(x + WIDTH / 2 - Bullet.WIDTH / 2, y - Bullet.HEIGHT));
    }
    
    
    
    /**
     * Constructs a Player at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    public Player(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        setHealth(10);
        setDead(false);
        // TODO: initialize health, dead flag, load sprite if needed
    }
    /**
     * Marks the player as dead.
     */
    public void setDead(boolean dead) {
        // TODO: update dead flag
        
        this.dead = dead;
    }
    /**
     * Checks whether the player is dead.
     */
    @Override
    public boolean isDead() {
        // TODO: return dead flag
        return dead;
        
    }
    
    /**
     * Returns the width of the player.
     */
    @Override
    public double getWidth() {
        return WIDTH;
    }
    
    /**
     * Returns the height of the player.
     */
    @Override
    public double getHeight() {
        return HEIGHT;
    }
    
    /**
     * Returns current health of the player.
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Sets player's health.
     */
    public void setHealth(int health) {
        this.health = health;
    }
}
