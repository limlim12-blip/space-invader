package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Bounds;

/**
 * Skeleton for GameObject. Base class for all game objects.
 * Subclasses must implement the abstract methods below.
 */
public abstract class GameObject {
    // Position and size
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    /**
     * Constructs a GameObject at the specified position with dimensions.
     * @param x initial X position
     * @param y initial Y position
     * @param width object width
     * @param height object height
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Updates the game object's state each frame.
     */
    public abstract void update();

    /**
     * Renders the game object on the canvas.
     * @param gc graphics context
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Checks whether this object should be removed from the game.
     * @return true if dead/removed
     */
    public abstract boolean isDead();

    /**
     * Returns the current X coordinate.
     * @return x position
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the current Y coordinate.
     * @return y position
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the bounding box for collision detection.
     * @return bounds of this object
     */
    public Bounds getBounds() {
        // Provided by engine; do not modify
        return new javafx.scene.shape.Rectangle(
            x,
            y,
            getWidth(),
            getHeight()
        ).getBoundsInLocal();
    }

    /**
     * Returns the width of the object.
     * @return width
     */
    public abstract double getWidth();

    /**
     * Returns the height of the object.
     * @return height
     */
    public abstract double getHeight();
}
