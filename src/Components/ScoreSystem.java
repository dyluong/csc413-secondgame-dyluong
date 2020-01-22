package src.Components;

import src.Objects.GameObject;

import java.awt.*;


public class ScoreSystem extends GameObject{
    private int playerScore;
    private int x;
    private int y;

    public ScoreSystem(){}

    public void init(){
        playerScore = 0;
        x = 0;
        y = 0;
    }

    public void init(int playerScore, int x, int y){
        this.playerScore = playerScore;
        this.x = x;
        this.y = y;
    }

    public int add(int points){
        return playerScore += points;
    }

    @Override
    public void drawImage(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Courier", Font.ITALIC+Font.BOLD, 30);

        g2d.setColor(Color.black);
        g2d.setFont(font);
        g2d.drawString(this.toString(), this.x, this.y);

    }

    @Override
    public String toString() {
        return "Player Score: " + playerScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
}
