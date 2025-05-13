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
        y += SPEED; // Boss di chuyển xuống từ trên màn hình

        // Cho Boss di chuyển ngang
        x += horizontalSpeed;
        if (x <= 0 || x >= SpaceShooter.WIDTH - WIDTH) {
            horizontalSpeed *= -1; // Đảo hướng khi chạm biên
        }

        if (y >= SpaceShooter.HEIGHT) {
            setDead(true); // Nếu Boss ra khỏi màn hình thì xóa
        }
    }

    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        health -= 10; // Mỗi lần trúng đạn, trừ 10 máu
        if (health <= 0) {
            setDead(true); // Nếu máu <= 0, Boss bị tiêu diệt
        }
    }

    /**
     * The Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<GameObject> newObjects) {
        EnemyBullet bullet = new EnemyBullet(x + WIDTH / 2, y + HEIGHT);
        newObjects.add(bullet); // Thêm đạn vào danh sách để hiển thị
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
