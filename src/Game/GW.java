/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.Game;


import src.Components.*;
import src.GUI.EndScreen;
import src.GUI.StartScreen;
import src.Objects.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 *
 *
 */
public class GW extends JPanel  {


    private BufferedImage world;
    private Graphics2D buffer;
    private SoundPlayer bgMusic;
    private MapLoader map;
    private static ScoreSystem scoreSystem;
    private static HealthSystem healthSystem;
    private static StartScreen startScreen = new StartScreen();
    private static EndScreen endScreen = new EndScreen();

    private static int level =1;

    private static JFrame jf;
    private Paddle p1;
    private Ball b1;

    private static boolean isOn =false;
    private boolean nextLevelPressed = false;

    public static HashMap<String,BufferedImage> imgList = new HashMap<>();
    public static HashMap<String,URL> soundURLs = new HashMap<>();
    public static HashMap<Integer,InputStream> mapLevels = new HashMap<>();

    private static ArrayList<GameObject> gameObjects = new ArrayList<>();
    private static ArrayList<Collidable> collidables = new ArrayList<>();

    //private static ArrayList<GameObject> initialObjects = new ArrayList<>();
    //private static ArrayList<Collidable> initialColliables = new ArrayList<>();

    public static void main(String[] args) {
        Thread x;
        GW gw = new GW();
        gw.init();
        startScreen.init();
        endScreen.init();
        try {

            while (true) {
                if(isOn){
                    gw.checkLevelClear();
                    gw.p1.update();
                    gw.b1.update();
                    gw.healthSystem.update();
                    gw.repaint();
                    gw.handleRemove();
                    gw.checkCollisions();
                    gw.checkRespawn();
                    gw.resetCollisions(); //reset the booleans in collision
                    if(level > GC.LEVELS)
                        gw.checkWinner();
                  // System.out.println(gw.b1);
                   // System.out.println(gw.p1);
                }
                Thread.sleep(1000 / 144);

            }
        } catch (InterruptedException ignored) {

        }

    }


    private void init() {
        this.jf = new JFrame("Rainbow Reef");
        this.world = new BufferedImage(GC.WORLD_WIDTH, GC.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage p1img = null, block= null, b1img = null, unbkWallimg = null, bkWallimg= null, background = null ,powerUpimg = null, blockHeart = null, bigLeg = null, bigLegS = null, gameTitle =null, heart = null;
        AudioInputStream bgMusicStream = null;
        InputStream mapFile = null;
        try {
            System.out.println(System.getProperty("user.dir"));
            p1img = ImageIO.read(GW.class.getClassLoader().getResource("resources/Characters/HermitPaddle.png"));
            b1img = ImageIO.read(GW.class.getClassLoader().getResource("resources/Characters/Momomimi.png"));
            background = ImageIO.read(GW.class.getClassLoader().getResource("resources/Backgrounds/Background1.png"));
            bkWallimg= ImageIO.read(GW.class.getClassLoader().getResource("resources/Blocks/Block1.png"));
            unbkWallimg = ImageIO.read(GW.class.getClassLoader().getResource("resources/Blocks/Block_solid.png"));
            bigLeg = ImageIO.read(GW.class.getClassLoader().getResource("Characters/Bigleg.png"));
            powerUpimg = ImageIO.read(GW.class.getClassLoader().getResource("Blocks/Block_powerup.png"));
            blockHeart = ImageIO.read(GW.class.getClassLoader().getResource("resources/Blocks/Block_heart.png"));
            heart = ImageIO.read(GW.class.getClassLoader().getResource("resources/Blocks/0Heart.png"));
            gameTitle = ImageIO.read(GW.class.getClassLoader().getResource("resources/Other/Title.bmp"));
            URL bgMusicURL = GW.class.getClassLoader().getResource("resources/Sounds/Theme_song.wav");
            URL paddleSoundURL = GW.class.getClassLoader().getResource("resources/Sounds/Sound_katch.wav");
            URL blockSoundURL = GW.class.getClassLoader().getResource("resources/Sounds/Sound_block.wav");
            URL wallSoundURL = GW.class.getClassLoader().getResource("resources/Sounds/Sound_wall.wav");

            for(int x = 1; x <= GC.BLOCK_LAYERS; x++){
                block =  ImageIO.read(GW.class.getClassLoader().getResource("resources/Blocks/Block"+x+".png"));
                imgList.put("Block"+x,block);
                System.out.println();
            }

            for(int i = level; i<= GC.LEVELS; i++) {
                mapFile = GW.class.getClassLoader().getResourceAsStream("resources/Maps/Map_" + i + ".txt");
                mapLevels.put(i, mapFile);
            }

            imgList.put("Paddle",p1img);
            imgList.put("Ball",b1img);
            imgList.put("Background", background);
            imgList.put("UnbreakableWall", unbkWallimg);
            imgList.put("BreakableWall", bkWallimg);
            imgList.put("Enemy", bigLeg);
            imgList.put("BigLeg_S",bigLegS);
            imgList.put("PowerUp",powerUpimg);
            imgList.put("BlockHeart", blockHeart);
            imgList.put("Heart", heart);
            imgList.put("Title",gameTitle);

            soundURLs.put("Background", bgMusicURL);
            soundURLs.put("Paddle", paddleSoundURL);
            soundURLs.put("Block", blockSoundURL);
            soundURLs.put("Wall",wallSoundURL);

            bgMusicStream = AudioSystem.getAudioInputStream(bgMusicURL);

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        initBG(background);
        map = new MapLoader(mapLevels.get(level));
        map.init();

        bgMusic = new SoundPlayer(bgMusicStream);
        bgMusic.playLoop();

        p1 = new Paddle(p1img.getWidth()+10, GC.SCREEN_HEIGHT-100, 0, 0, 0, p1img,1);
        b1 = new Ball( p1img.getWidth() ,GC.SCREEN_HEIGHT-300,0,0,45,b1img);
        p1.initSound();

        healthSystem = new HealthSystem();
        healthSystem.init(GC.SCREEN_WIDTH-imgList.get("UnbreakableWall").getWidth()-70,imgList.get("UnbreakableWall").getHeight()+5,0,imgList.get("Heart"),100,3);
        healthSystem.setPlayerID(1);
        gameObjects.add(healthSystem);
        //p1 = new Paddle(SCREEN_WIDTH/2, SCREEN_HEIGHT-100, 0, 0, 0, p1img,1);
        //b1 = new Ball( SCREEN_WIDTH/2 ,SCREEN_HEIGHT-250,1,1,90,b1img);
        gameObjects.add(p1);
        gameObjects.add(b1);

        collidables.add(p1);
        collidables.add(b1);

        scoreSystem = new ScoreSystem();
        scoreSystem.init(0,50,50);
        healthSystem.setTextX(50);
        healthSystem.setTextY(50);

        gameObjects.add(scoreSystem);

        Control tc1 = new Control(p1, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.addKeyListener(tc1);
        KeyListener nextLvl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    nextLevelPressed = true;
                    try{
                        if(nextLevelPressed) {
                            nextLevel();
                        }
                    }catch (Exception ex){}
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { nextLevelPressed = false;}
        };
        this.jf.addKeyListener(nextLvl);
        this.jf.setSize(GC.SCREEN_WIDTH, GC.SCREEN_HEIGHT + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(false);

       // save(gameObjects,collidables);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);
        for(int i = 0 ; i<gameObjects.size();i++){
            gameObjects.get(i).drawImage(buffer);
        }
        g2.drawImage(world,0,0,null);
    }

    private void initBG(BufferedImage bg){
        try {
            int tileWidth = bg.getWidth();
            int tileHeight = bg.getHeight();
            int x = world.getWidth() / tileWidth;
            int y = world.getHeight() / tileHeight;
            int currX = 0;
            int currY = 0;
            Background background;
            for (int i = 0; i <= y; i++) {
                for (int j = 0; j <= x; j++) {
                    background = new Background(currX,currY,0,0,0,bg);
                    gameObjects.add(background);
                    currX += tileWidth;
                }
                currX = 0;
                currY += tileHeight;
            }
        }catch(Exception ex){System.out.println("Failed to init background.");}
    }

    public static void addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
    }

    public static void addColliable(Collidable collidable){
        collidables.add(collidable);
    }

    public void checkCollisions(){
        try{
        collidables.forEach(collidable ->{
            b1.handleCollision(collidable);              //check for collisions between balls and other objects
            if(collidable instanceof UnbreakableWall)
                p1.handleCollision(collidable);          //to prevent paddle moving beyond the unbreakable walls
        });
        }catch (Exception ex){}
    }

    public void handleRemove(){
        GameObject object;
        for (int i=0; i < gameObjects.size() ; i++){
            if(gameObjects.get(i).getRemoveThis()) {
                object = gameObjects.get(i);
                collidables.remove(gameObjects.get(i));
                gameObjects.remove(gameObjects.get(i));
                object.nullEverything();
            }
        }
    }

    private void checkRespawn(){
        if(p1.getRespawn()){
            b1.respawn();
            p1.setRespawn(false);
        }

        if(b1.isDecrementHeart()){
            b1.respawn();
            healthSystem.setHearts(healthSystem.getHearts()-1);
            scoreSystem.setPlayerScore(scoreSystem.getPlayerScore()-1000);
            b1.setDecrementHeart(false);
        }
    }

    private void resetPos(){
        b1.respawn();
    }

    public static JFrame getJf() {
        return jf;
    }

/*    public static EndScreen getEndScreen() {
        return endScreen;
    }*/

    public static void setIsOn(Boolean isOn) {
        GW.isOn = isOn;
    }

    public void nextLevel(){
        level++;
        if(level>GC.LEVELS) return;
        clear();
        resetPos();
        map.newMap(mapLevels.get(level));
        System.out.println(level);
    }
    private void clear(){
        GameObject object;

        for(int i=gameObjects.size()-1;i>0;i--){
            if(gameObjects.get(i) instanceof BreakableWall ||gameObjects.get(i) instanceof Enemy ||gameObjects.get(i) instanceof PowerUp ) {
                object = gameObjects.get(i);
                gameObjects.remove(i);
                object.nullEverything();
            }
        }
        for (int i = collidables.size()-1; i>0;i--){
            if(collidables.get(i) instanceof BreakableWall || collidables.get(i) instanceof  Enemy||collidables.get(i) instanceof PowerUp){
                collidables.remove(i);
            }
        }

    }

    public static void addScore(int score){
        scoreSystem.add(score);
    }

    public void resetCollisions(){
        b1.setHasCollided(false,false,false,false,false,false,false,false);
        b1.resetDmg();
    }

    public void checkLevelClear(){
        boolean result = false;
        if(nextLevelPressed) return;
        for(int i=0;i<gameObjects.size();i++){
            if(gameObjects.get(i) instanceof BreakableWall) {
                return;
            }
            if(gameObjects.get(i) instanceof PowerUp) {
                return;
            }
        }
        if(level<=GC.LEVELS)
            nextLevel();
        else
            level++;
        System.out.println(level);
    }

    public static HealthSystem getHealthSystem() {
        return healthSystem;
    }

    public static void removeRandomBricks(int num){
        try {
            Random random = new Random();
            int brick;
            GameObject object;
            for (int i = 0; i < num; i++) {
                do {
                    brick = random.nextInt(gameObjects.size());
                    if(!containBreakable()) return;
                }
                while (!(gameObjects.get(brick) instanceof BreakableWall));

                object = gameObjects.get(brick);
                collidables.remove(gameObjects.get(brick));
                gameObjects.remove(gameObjects.get(brick));
                object.nullEverything();
            }
        }catch (Exception ex){
            scoreSystem.add(10000);
        }
    }

    public static boolean containBreakable() {
        for (int i = 0; i < gameObjects.size(); i++){
            if (gameObjects.get(i) instanceof BreakableWall) {
                return true;
            }
         }
        return false;
    }

    public static void isLose(){
        endScreen.execute(healthSystem.getPlayerID(),false);
    }

    public void checkWinner(){
        if(healthSystem.getHearts()>=1){
            if(level >= GC.LEVELS+1){
                for(int i = 0; i<gameObjects.size(); i++){
                    if(gameObjects.get(i) instanceof BreakableWall ||gameObjects.get(i) instanceof PowerUp ||gameObjects.get(i) instanceof Enemy)
                        return;
                }
                for(int i = 0; i<collidables.size(); i++){
                    if(collidables.get(i) instanceof BreakableWall ||collidables.get(i) instanceof PowerUp ||collidables.get(i) instanceof Enemy)
                        return;
                }
                    endScreen.execute(healthSystem.getPlayerID(),true);
            }
        }
    }

    /*    public static void save(ArrayList<GameObject> gameObjects, ArrayList<Collidable> collidables){
        gameObjects.forEach(gameObject -> initialObjects.add(gameObject));
        collidables.forEach(collidable -> initialColliables.add(collidable));
    }

    public static void reset(){
        gameObjects.clear();
        collidables.clear();
        initialObjects.forEach(gameObject -> gameObjects.add(gameObject));
        initialColliables.forEach(collidable -> collidables.add(collidable));
    }*/
    public static int getScore(){
        return scoreSystem.getPlayerScore();
    }
}
