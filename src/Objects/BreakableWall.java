package src.Objects;

import src.Components.Collidable;
import src.Game.GW;
import src.Components.SoundPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject implements Collidable {

    private Rectangle hitBox = new Rectangle();
    private int layers=0;
    private int score=0;
    private SoundPlayer wallSound;

    public BreakableWall(){}

   public BreakableWall(int x, int y, int vx, int vy, int angle, BufferedImage img) {
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
    }

    @Override
    public void handleCollision(Collidable co) {}

   @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setColor(Color.red);
        //g2d.draw(hitBox);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void initSound(){
        try {
            AudioInputStream temp = AudioSystem.getAudioInputStream(GW.soundURLs.get("Block"));
            wallSound = new SoundPlayer(temp);
        }catch (Exception ex){
            System.out.println("Failed Sound");
        }
    }
    public void playSound(){
        wallSound.play();
    }

    public int getLayers() {
        return layers;
    }

    public void setLayers(int layers) {
        this.layers = layers;
        this.img = GW.imgList.get("Block"+layers);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

}
