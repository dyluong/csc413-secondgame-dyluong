package src.Objects;

import src.Components.Collidable;
import src.Components.HealthSystem;
import src.Components.SoundPlayer;
import src.Game.GC;
import src.Game.GW;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 *
 */
public class Paddle extends GameObject implements Collidable {

    private int R = 2;

    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean respawnPressed;

    private float oldPosX;
    private float oldPosY;

    private Rectangle hitBox;
   // private HealthSystem healthSystem = new HealthSystem();
    private Boolean disableMove =false;
    private Boolean respawn =false;

    private final int playerID;
    private final int score = 100;
    private SoundPlayer paddleSound;

    public Paddle(int x, int y, int vx, int vy, int angle, BufferedImage img, int playerID) {
        super(x, y, vx, vy, angle, img);
        hitBox = new Rectangle(this.img.getWidth(), this.img.getHeight());
        this.playerID = playerID;
        //healthSystem.init((int)this.x, (int)this.y, 0, GW.imgList.get("Heart"),100, 3);
        //healthSystem.setPlayerID(this.playerID);
        oldPosX = x;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() { this.LeftPressed = true; }

    public void toggleRespawnPressed(){
        this.respawnPressed = true;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() { this.LeftPressed = false; }

    public void unToggleRespawnPressed(){
        this.respawnPressed = false;
    }

    public void update() {
        if (this.RightPressed) {
            this.moveRight();
        }
        if (this.LeftPressed) {
            this.moveLeft();
        }
        if(this.respawnPressed){
            this.respawn();
        };
       // healthSystem.update((int)this.x,(int) this.y);
        hitBox.setLocation((int)this.x, (int)this.y);
        checkWinner();

        if(disableMove) {disableMove =false; return;}

        oldPosX = this.x;
        oldPosY = this.y;
    }


    private void moveLeft() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
    }

    private void moveRight() {

        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }

     private void respawn(){
        respawn =true;
    }


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        Graphics2D g2d = (Graphics2D) g;

       // g2d.setColor(Color.red);
       // g2d.draw(hitBox);
    }

    @Override
    public void handleCollision(Collidable co) {
        if(co instanceof UnbreakableWall){
            if(this.hitBox.intersects(((UnbreakableWall) co).getHitBox())){
                if (x >= GC.WORLD_WIDTH-((UnbreakableWall) co).getX()) {
                    disableMove = true;
                    this.x = oldPosX;
                    this.y = oldPosY;
                }
                else if (x <= 0 + ((UnbreakableWall) co).getWidth()) {
                    disableMove = true;
                    this.x = oldPosX;
                    this.y = oldPosY;
                }
            }
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }


    private void checkWinner(){
/*        if(healthSystem.getWinnerID() != -1) {
            GW.getEndScreen().execute(healthSystem.getWinnerID());
            GW.setIsOn(false);
        }*/
    }

    public Boolean getRespawn() {
        return respawn;
    }

    public void setRespawn(Boolean respawn) {
        this.respawn = respawn;
    }

    public void initSound(){
        try {
            AudioInputStream temp = AudioSystem.getAudioInputStream(GW.soundURLs.get("Paddle"));
            paddleSound = new SoundPlayer(temp);
        }catch (Exception ex){
            System.out.println("Failed Sound");
        }
    }
    public void playSound(){
        paddleSound.play();
    }

    public int getScore() {
        return score;
    }
}
