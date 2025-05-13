package entities;

import javafx.scene.canvas.GraphicsContext;

public class Explosion {
    private double x, y;
    private int duration = 30; // Hiệu ứng tồn tại trong 30 khung hình

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (duration > 0) {
            duration--;
        }
    }

    public boolean isFinished() {
        return duration <= 0;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.RED);
        gc.fillOval(x - 10, y - 10, 20, 20);
    }
}