package src.Objects;

import src.Components.Collidable;
import src.Components.SoundPlayer;
import src.Game.GW;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PowerUp extends GameObject implements Collidable {

    private Rectangle hitBox;
    private Random RNG = new Random();
    private int type;
    private SoundPlayer wallSound;

    public PowerUp(){}

    public PowerUp(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super(x, y, vx, vy, angle, img);
        hitBox = new Rectangle (this.img.getWidth(), this.img.getHeight());
        hitBox.setLocation((int)this.x, (int)this.y);
    }

    @Override
    public void init(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super.init(x, y, vx, vy, angle, img);
        hitBox = new Rectangle (this.img.getWidth(), this.img.getHeight());
        hitBox.setLocation((int)this.x, (int)this.y);
        type= RNG.nextInt(2);
        if(type == 0) this.img = GW.imgList.get("BlockHeart");
        initSound();
    }

    @Override
    public void handleCollision(Collidable co) {}

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

    public int getType() {
        return type;
    }
}
