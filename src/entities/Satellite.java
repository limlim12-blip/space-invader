
package entities;

import javafx.scene.canvas.GraphicsContext;

public class Satellite extends Enemy
{

    double time=0;
    public Satellite(double x, double y,double angle) {
        super(x, y);
        this.time = angle;
    }
    public void up(BossEnemy boss) {
        if (!boss.exploding||!exploding) {
            
        
        time += 0.02*((51-(double)boss.health)/10);
        x = boss.centerX() + 220 * Math.cos(time);
        y = boss.centerY() + 220 * Math.sin(time);
        }
    }
    @Override
    public void render(GraphicsContext gc) {
         if (exploding&&explosionStep<=15) {
            gc.drawImage(EXPLOSION_IMAGE, explosionStep % 3 * 128, (explosionStep / 3) * 128 + 1, 128, 128, x, y, 80, 80);
            explosionStep += 1;
        }
        else
            setExploding(false);
           
        if(!exploding) gc.drawImage(ENEMY_IMAGE, x, y,WIDTH,HEIGHT);
    }

    @Override
    public void update() {
        return;
    }
    @Override
    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }
}