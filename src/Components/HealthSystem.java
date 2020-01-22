package src.Components;

import src.Game.GW;
import src.Objects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HealthSystem extends GameObject {

    private int textX = 0;
    private int textY = 0;
    private int x;
    private int y;
    private int angle;
    private BufferedImage img;
    private int playerID;
    private int winnerID=-1;

    private int hp=100;
    private int hearts=3;
    private Rectangle healthBar = new Rectangle();

    public void init(int x, int y, int angle, BufferedImage img, int hp, int hearts){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.hp = hp;
        this.hearts = hearts;

        healthBar.setSize(hp/2, 5);
        healthBar.setLocation(this.x,this.y);
    }

    public void update(){
        if(hearts <= 0){
            System.out.println("Player "+playerID+": lose");
            GW.isLose();
        }
    }

    public void update(int x, int y){
        this.x = x;
        this.y = y;
        healthBar.setLocation(this.x,this.y);

        if(hearts <= 0){
            System.out.println("Player "+playerID+": lose");
        }

        if(winnerID != -1){
            System.out.println("Player "+winnerID+": win");
        }
    }


    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        healthBar.setSize(hp/2, 5);

        if(playerID == 2) {
            g2d.setColor(Color.green);
            g2d.draw(healthBar);
            g2d.fill(healthBar);
            return;
        }

        Font font = new Font("Courier", Font.ITALIC+Font.BOLD, 28);

        g2d.setColor(Color.black);
        g2d.setFont(font);
        g2d.drawString("Lives: " + hearts, textX, textY+30);
        try {
            for (int i = 0; i < hearts; i++) {
                 g2d.drawImage(img, x - i * (img.getWidth() / 1 + 2), y , null);
            }
        }catch (Exception ex){}

    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public void setTextX(int textX) {
        this.textX = textX;
    }

    public void setTextY(int textY) {
        this.textY = textY;
    }

    public void decrementHeart(){
        hearts--;
    }
}
