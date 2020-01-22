package src.Objects;

import src.Components.Collidable;
import src.Components.HealthSystem;
import src.Game.GW;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends GameObject implements Collidable {

    private HealthSystem healthSystem;
    private Rectangle hitBox;
    private int score=1000;

    public void update(){

    }

    @Override
    public void init(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super.init(x, y, vx, vy, angle, img);
        hitBox = new Rectangle(this.img.getWidth(), this.img.getHeight());
        hitBox.setLocation(x,y);

        healthSystem = new HealthSystem();
        healthSystem.init(x, y-5,0, null, 150,0);
        healthSystem.setPlayerID(2);
        System.out.println(y);
    }

    @Override
    public void handleCollision(Collidable co) {}

    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        healthSystem.drawImage(g);

        Graphics2D g2d = (Graphics2D) g;
/*        g2d.setColor(Color.RED);
        g2d.draw(hitBox);*/
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public HealthSystem getHealthSystem() {
        return healthSystem;
    }

    public  int getWidth(){
        return (int) this.hitBox.getWidth();
    }
    public  int getHeight(){
        return (int) this.hitBox.getHeight();
    }
    public int getScore() {
        return score;
    }

    public float getY() {
        return y;
    }
    public float getX() {
        return x;
    }
}
