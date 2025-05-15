package entities;

import javafx.scene.canvas.GraphicsContext;

public class Explosion {
    private double x, y;
    private int duration = 30; // Hiệu ứng tồn tại trong 30 khung hình

    public Explosion(double x, double y, int duration) {
        this.x = x;
        this.y = y;
        this.duration = duration;
    }

    public void update() {
        duration--; // Giảm thời gian hiệu ứng
    }

    public boolean isFinished() {
        return duration <= 0;
    }

    public void render(GraphicsContext gc) {
        double size = (30 - duration) * 2; // Kích thước tăng dần khi hiệu ứng tiến triển
        gc.setFill(javafx.scene.paint.Color.RED);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }
}