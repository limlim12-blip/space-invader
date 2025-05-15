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
        this.health = 100; // Boss có 100 máu ban đầu
        this.horizontalSpeed = 2; // Boss di chuyển ngang với tốc độ cố định
    }

    /**
     * Update boss's position and behavior each frame.
     */
    @Override
    public void update() {
        y += SPEED; // Boss di chuyển xuống từ trên màn hình

        // Cho Boss di chuyển ngang
        x += horizontalSpeed;
        if (x <= 5 || x >= SpaceShooter.WIDTH - WIDTH - 5) {
            horizontalSpeed *= -1; // Đảo hướng khi gần biên
        }

        if (y >= SpaceShooter.HEIGHT) {
            setDead(true); // Nếu Boss ra khỏi màn hình thì xóa
        }
    }

    /**
     * Inflicts damage to the boss.
     */
    public void takeDamage() {
        health -= 10;
        if (health <= 0) {
            SpaceShooter.explosions.add(new Explosion(x, y, 30)); // Thêm hiệu ứng nổ khi chết
            setDead(true);
        }
    }

    /**
     * The Boss fires bullets towards the player.
     * @param newObjects list to which new bullets are added
     */
    public void shoot(List<GameObject> newObjects) {
        for (int i = -1; i <= 1; i++) { // Bắn 3 viên đạn tỏa ra
            EnemyBullet bullet = new EnemyBullet(x + WIDTH / 2 + i * 10, y + HEIGHT);
            newObjects.add(bullet);
        }
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
