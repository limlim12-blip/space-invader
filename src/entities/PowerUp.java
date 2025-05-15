package entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * Skeleton for PowerUp. Students must implement movement,
 * rendering, and state management.
 */
public class PowerUp extends GameObject {

    // Dimensions of the power-up
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    // Fall speed of the power-up
    private static final double SPEED = 2;

    // Flag indicating whether the power-up should be removed
    private boolean dead;

    /**
     * Constructs a PowerUp at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    public PowerUp(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        this.dead = false; // Đảm bảo PowerUp khởi tạo ở trạng thái hoạt động
    }

    /**
     * Updates power-up position each frame.
     */
    @Override
    public void update() {
        y += SPEED; // Di chuyển PowerUp xuống dưới
        if (y >= SpaceShooter.HEIGHT) {
            setDead(true); // Xóa nếu ra khỏi màn hình
        }
    }

    /**
     * Renders the power-up on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.YELLOW); // Màu sắc của PowerUp
        gc.fillOval(x, y, WIDTH, HEIGHT); // Vẽ PowerUp dưới dạng hình tròn
    }

    /**
     * Returns the width of the power-up.
     * @return WIDTH
     */
    @Override
    public double getWidth() {
        // TODO: return width
        return WIDTH;
    }

    /**
     * Returns the height of the power-up.
     * @return HEIGHT
     */
    @Override
    public double getHeight() {
        // TODO: return height
        return HEIGHT;
    }

    /**
     * Checks whether the power-up should be removed.
     * @return true if dead
     */
    @Override
    public boolean isDead() {
        // TODO: return dead flag
        return dead;
    }

    /**
     * Marks this power-up as dead (to be removed).
     * @param dead true if should be removed
     */
    public void setDead(boolean dead) {
        // TODO: update dead flag
        this.dead = dead;
    }
}
