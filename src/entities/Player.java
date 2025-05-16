package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Skeleton for Player. Students must implement movement, rendering,
 * shooting, and health/state management.
 */
public class Player extends GameObject{

    // Hitbox dimensions
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    static final Image PLAYER_IMAGE = new Image(Player.class.getResourceAsStream("/player.png"));
    static final Image DAMAGED_IMAGE = new Image(Player.class.getResourceAsStream("/damged.PNG"));
    static final Image EXPLOSION_IMAGE = new Image(Bullet.class.getResourceAsStream("/explosion.png"));
    // Movement speed
    private static double SPEED = 10;
    public int FireRate;
    public int FIRE_RATE=10;

    // Movement flags w w
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveForward;
    private boolean moveBackward;

    // Player health
    private int health;

    // State flag for removal
    private boolean dead;
    boolean shooting=false;
    public boolean takingdame;
    public int takingdamestep;
    public boolean exploding;
    public int explosionStep = 0;

    
    /**
     * Updates player position based on movement flags.
     */
    @Override
    public void update() {
        if (moveLeft) {
            if (x < 10)
                x = 0; 
            else if(moveBackward||moveForward){
                x -= (double) SPEED / Math.sqrt(2);
            }
            else x -= SPEED;
        }
        if (moveRight) {
            if (x > SpaceShooter.WIDTH-43)
                x = SpaceShooter.WIDTH-40;
            else if(moveBackward||moveForward){
                x += (double) SPEED / Math.sqrt(2);
            }
            else x += SPEED;
        }
        if (moveBackward) {
            if (y > SpaceShooter.HEIGHT-43)
                y = SpaceShooter.HEIGHT-40;
            else if(moveLeft||moveRight){
                y += (double) SPEED / Math.sqrt(2);
            }
            else y += SPEED;
        }
        if (moveForward) {
            if (y <SpaceShooter.HEIGHT/4+3)
                y = SpaceShooter.HEIGHT/4;
            else if(moveLeft||moveRight){
                y -= (double) SPEED / Math.sqrt(2);
            }
            else y -= SPEED;
        }
        FireRate--;
    }
    
    /**
     * Renders the player on the canvas.
     */
    @Override
    public void render(GraphicsContext gc) {
         if (exploding&&explosionStep<=15) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x-15, y-20, 80, 80);
            explosionStep += 1;
        }
        else setExploding(false);
        if (takingdame&&takingdamestep<10){
            gc.drawImage(DAMAGED_IMAGE, x, y, WIDTH, HEIGHT);
            takingdamestep++;
        } 
        else
            gc.drawImage(PLAYER_IMAGE, x, y,WIDTH,HEIGHT);
    }
    public void Powerup() {
        if(FIRE_RATE>5)
        FIRE_RATE -=1;
        if(SPEED<30)
        this.SPEED +=1;
        setHealth(health+2);
    }
    public void setTakingdame(boolean takingdame) {
        this.takingdame = takingdame;
        takingdamestep = 0;
    }
    public void setExploding(boolean exploding) {
        this.exploding = exploding;
        explosionStep = 0;
    }
    /**
     * Sets movement flags.
     */
    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }
    
    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }
    
    public void setMoveForward(boolean moveForward) {
        
        this.moveForward = moveForward;
    }
    
    public void setMoveBackward(boolean moveBackward) {
        
        this.moveBackward = moveBackward;
    }
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    
    /**
     * Shoots a bullet from the player.
     */
    public void shoot(List<Bullet> bullets) {
        if(FireRate<=0){
            bullets.add(new Bullet(x+WIDTH/2-4, y - HEIGHT / 2));
            FireRate = FIRE_RATE;
        }
    }
    
    
    
    /**
     * Constructs a Player at the given position.
     * @param x initial X position
     * @param y initial Y position
     */
    public Player(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        setHealth(20);
        setDead(false);
        
    }
    /**
     * Marks the player as dead.
     */
    public void setDead(boolean dead) {
        
        this.dead = dead;
    }
    /**
     * Checks whether the player is dead.
     */
    @Override
    public boolean isDead() {
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
