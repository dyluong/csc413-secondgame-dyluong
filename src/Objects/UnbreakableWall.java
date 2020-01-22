package src.Objects;

import src.Components.Collidable;
import src.Game.GW;
import src.Components.SoundPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class UnbreakableWall extends GameObject implements Collidable {
    private boolean hasCollided = false;
    private Rectangle hitBox = new Rectangle();
    private SoundPlayer wallSound;
    private int hp=10000;
    private int score;

    public UnbreakableWall(){}
    public UnbreakableWall(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super(x, y, vx, vy, angle, img);
        hitBox.setSize(img.getWidth(), img.getHeight());
        hitBox.setLocation(x, y);
    }

    @Override
    public void init(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super.init(x, y, vx, vy, angle, img);
        hitBox.setSize(img.getWidth(), img.getHeight());
        hitBox.setLocation(x, y);
        initSound();
        score = 250 + (int) (Math.random()*(75-25)+25);
    }

    @Override
    public void handleCollision(Collidable co) {}

    public Rectangle getHitBox() {
        return hitBox;
    }

   @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setColor(Color.red);
        //g2d.draw(hitBox);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }

    public  int getWidth(){
        return (int) this.hitBox.getWidth();
    }
    public  int getHeight(){
        return (int) this.hitBox.getHeight();
    }

    public void initSound(){
        try {
            AudioInputStream temp = AudioSystem.getAudioInputStream(GW.soundURLs.get("Wall"));
            wallSound = new SoundPlayer(temp);
        }catch (Exception ex){
            System.out.println("Failed Sound");
        }
    }
    public void playSound(){
        wallSound.play();
    }

    public int getScore() {
        return score;
    }
}
