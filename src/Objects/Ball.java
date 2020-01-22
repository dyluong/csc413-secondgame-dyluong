package src.Objects;

import src.Components.Collidable;
import src.Game.GC;
import src.Game.GW;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public class Ball extends GameObject implements Collidable {

    private int R = 2;
    private double S = 0;
    private Rectangle hitBox;
    private int dmg =100;
    private int oldDmg;
    private static HashMap<String,Boolean> hasCollided;

    static{
        hasCollided = new HashMap<>();
        hasCollided.put("EnemyX",false);
        hasCollided.put("EnemyY",false);
        hasCollided.put("UnbreakableX",false);
        hasCollided.put("UnbreakableY",false);
        hasCollided.put("Paddle",false);
        hasCollided.put("Breakable",false);
        hasCollided.put("Enemy",false);
        hasCollided.put("PowerUp",false);
    }

    private boolean decrementHeart = false;
    private int directionX = 1;
    private int directionY = 1;

    private float initX;
    private float initY;

    public Ball(int x, int y, int vx, int vy, int angle, BufferedImage img){
        super( x, y, vx, vy, angle, img);
        hitBox = new Rectangle(this.img.getWidth(), this.img.getHeight());
        hitBox.setLocation((int) this.x, (int) this.y);
        initX = x;
        initY = y;
        oldDmg = dmg;
    }

    public void update(){
        moveForwards();
        hitBox.setLocation((int) x, (int) y);
        if(checkOnscreen()){
            decrementHeart = true;
        }
    }

    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        vx += S;
        vy += S;
        vx *= directionX;
        vy *= directionY;
        x += vx;
        y += vy;

    }

    @Override
    public void handleCollision(Collidable co) {
        if(co instanceof Paddle){
            if(hitBox.intersects(((Paddle) co).getHitBox()) && !hasCollided.get("Paddle")){
                directionY = -directionY;
                S+=0.05;
                ((Paddle) co).playSound();
                GW.addScore(((Paddle) co).getScore());
                setHasCollided(false,false,true,false,false,false,false,false);
            }
        }
        if(co instanceof UnbreakableWall){
            if(hitBox.intersects(((UnbreakableWall) co).getHitBox())){
                if(y <= ((UnbreakableWall) co).getHeight()) {
                    if(!hasCollided.get("UnbreakableY")) {
                        directionY = -directionY;
                        ((UnbreakableWall) co).playSound();
                        GW.addScore(((UnbreakableWall) co).getScore());
                    }
                    setHasCollided(false,true,false,false,false,false,false,false);
                }
                else {
                    if(!hasCollided.get("UnbreakableX")) {
                        directionX = -directionX;
                        ((UnbreakableWall) co).playSound();
                        GW.addScore(((UnbreakableWall) co).getScore());
                        setHasCollided(true,false,false,false,false,false,false,false);
                    }
                }
            }
        }

        if(co instanceof PowerUp){
            if (hitBox.intersects(((PowerUp) co).getHitBox()) && !hasCollided.get("PowerUp")) {
                directionY = -directionY;
                if(((PowerUp) co).getType() == 0)
                    GW.getHealthSystem().setHearts(GW.getHealthSystem().getHearts()+1);
                else{
                    if(GW.containBreakable())
                        GW.removeRandomBricks((int)(Math.random()*3)+1);
                    else
                        GW.addScore(10000);
                }
                ((PowerUp) co).setRemoveThis(true);
                ((PowerUp) co).playSound();
                GW.addScore(1000);
                setHasCollided(false,false,false,true,false,false,false,true);
            }
        }

        if(co instanceof BreakableWall){

            if (hitBox.intersects(((BreakableWall) co).getHitBox()) && !hasCollided.get("Breakable")) {
                directionY = -directionY;
                if(((BreakableWall) co).getLayers() == 1)
                    ((BreakableWall) co).setRemoveThis(true);
                else
                    ((BreakableWall) co).setLayers(((BreakableWall) co).getLayers()-1);
                ((BreakableWall) co).playSound();
                ((BreakableWall) co).setScore(((BreakableWall) co).getScore()-1000);
                GW.addScore(1000);
                setHasCollided(false,false,false,true,false,false,false,false);
            }
        }
        if(co instanceof Enemy){
            if (hitBox.intersects(((Enemy) co).getHitBox()) && !hasCollided.get("Enemy")) {

                int lowerY = (int)((Enemy) co).getY();
                int upperY = (int)((Enemy) co).getY()+((Enemy) co).getHeight();
                int lowerX = (int)((Enemy) co).getX();
                int upperX = (int)((Enemy) co).getX()+((Enemy) co).getWidth();

                if(x<lowerX || x<upperX) {
                    if(!hasCollided.get("EnemyX")) {
                        directionX = -directionX;
                        //((UnbreakableWall) co).playSound();
                        GW.addScore(((Enemy) co).getScore());
                        setHasCollided(false, false, false, false, true, false, true,false);
                        ((Enemy) co).getHealthSystem().setHp( ((Enemy) co).getHealthSystem().getHp() - dmg );
                        dmg = 0;
                    }
                }
               else if(y<lowerY || y<upperY) {
                    if(!hasCollided.get("EnemyY")) {
                        directionY = -directionY;
                        //((UnbreakableWall) co).playSound();
                        GW.addScore(((Enemy) co).getScore());
                    }
                    setHasCollided(false,false,false,false,false,true,true,false);
                    ((Enemy) co).getHealthSystem().setHp( ((Enemy) co).getHealthSystem().getHp() - dmg );
                    dmg = 0;
                }
               else
                    setHasCollided(false,false,false,false,false,false,true,false);
               if(((Enemy) co).getHealthSystem().getHp()<=0) {
                   ((Enemy) co).setRemoveThis(true);
               }
            }
        }
    }


    public void respawn(){
        x = initX;
        y = initY;
        S = 0;
        directionX = 1;
        directionY = 1;
        dmg = oldDmg;
        setHasCollided(false,false,false,false,false,false,false,false);

    }

    @Override
    public String toString() {
        return "x=" + (int) x + ", y=" + (int) y + ", angle=" + (int) angle + ", speed=" + S;
    }

    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        Graphics2D g2d = (Graphics2D) g;

        //g2d.setColor(Color.RED);
        //g2d.draw(hitBox);
    }

    public void setHasCollided(String key, boolean hasCollided) {
        this.hasCollided.replace(key,hasCollided);
    }

    public void setHasCollided(boolean UnbreakableX, boolean UnbreakableY, boolean Paddle, boolean Breakable, boolean EnemyX, boolean EnemyY,boolean Enemy, boolean PowerUp){
        hasCollided.replace("UnbreakableX",UnbreakableX);
        hasCollided.replace("UnbreakableY", UnbreakableY);
        hasCollided.replace("Paddle", Paddle);
        hasCollided.replace("Breakable",Breakable);
        hasCollided.replace("EnemyX", EnemyX);
        hasCollided.replace("EnemyY", EnemyY);
        hasCollided.replace("Enemy", Enemy);
        hasCollided.replace("PowerUp",PowerUp);
    }
    private boolean checkOnscreen(){
        if(this.x> GC.WORLD_WIDTH || this.y > GC.WORLD_HEIGHT) {return true;}
        return false;
    }

    public boolean isDecrementHeart() {
        return decrementHeart;
    }

    public void setDecrementHeart(boolean decrementHeart) {
        this.decrementHeart = decrementHeart;
    }

    public void resetDmg() {
        dmg = oldDmg;
    }
}
