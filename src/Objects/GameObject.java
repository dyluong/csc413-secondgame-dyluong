package src.Objects;

import src.Game.GC;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected float x;
    protected float y;
    protected float vx;
    protected float vy;
    protected float angle;

    protected BufferedImage img;


    private Boolean removeThis = false;

    public GameObject(){}

    public GameObject(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        System.out.println(this.img);
    }

    public void init(int x, int y, int vx, int vy, int angle, BufferedImage img){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        System.out.println(this.img);
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    public Boolean getRemoveThis() {
        return removeThis;
    }

    public void setRemoveThis(Boolean removeThis) {
        this.removeThis = removeThis;
    }

    public void nullEverything(){
        this.x= GC.WORLD_WIDTH+9000;
        this.y=GC.WORLD_HEIGHT+9000;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
