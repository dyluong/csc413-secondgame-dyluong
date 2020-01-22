package src.Components;

import src.Game.GC;
import src.Game.GW;
import src.Objects.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MapLoader {

    private File mapFile;
    private BufferedReader mapReader;
    private int imageSizeX;
    private int imageSizeY;
    private static HashMap<Integer, String> objectTable;
    private static HashMap<String, String> charTable;

    static{
        objectTable = new HashMap<>();
        objectTable.put(9,"UnbreakableWall");
        objectTable.put(8,"Enemy");
        objectTable.put(7,"PowerUp");
        if(GC.BLOCK_LAYERS>5) GC.BLOCK_LAYERS = 5;  //to set max layers

        for(int i = 1 ; i<=GC.BLOCK_LAYERS; i++){
            objectTable.put(i, "BreakableWall");
        }
        objectTable.put(0,"");
        objectTable.put(-1,"Enemy");

    }

    static {
        charTable = new HashMap<>();
        charTable.put("a","BigLeg");
        charTable.put("b", "BigLeg_S");
    }
   public MapLoader(InputStream in){
        mapReader = new BufferedReader(new InputStreamReader(in));
        imageSizeX = GW.imgList.get("UnbreakableWall").getWidth();
        imageSizeY= GW.imgList.get("UnbreakableWall").getHeight();
    }


    public void init(){
        try {
            String line = mapReader.readLine();
            System.out.println(line);
            Class<?> c;
            String className =null;
            GameObject gameObject;

            int currY = 0;
            int counter =0;
            int key = -1;

            while(line!=null){
                for(int i = 0; i < GC.WORLD_WIDTH ; i+=imageSizeX) {
                    try {
                        key = Integer.parseInt(String.valueOf(line.charAt(counter)));
                        className = objectTable.get(key);
                    }catch (Exception ex){
                        className = null;
                    }
                    if (className == null || className.equals("")) {
                        counter++;
                        continue;
                    }
                    c = Class.forName("src.Objects."+className);
                    gameObject = (GameObject) c.getConstructor().newInstance();
                    gameObject.init(i,currY,0,0,0,GW.imgList.get(className));
                    setLayers(key, gameObject);
                    setScore(key, gameObject);
                    GW.addGameObject(gameObject);
                    GW.addColliable((Collidable) gameObject);
                    counter++;
                }
                currY+=imageSizeY;
                counter = 0;
                line = mapReader.readLine();
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void newMap(InputStream in){
        mapReader = new BufferedReader(new InputStreamReader(in));

        try {
            String line = mapReader.readLine();
            System.out.println(line);
            Class<?> c;
            String className;
            GameObject gameObject;

            int currY = 0;
            int counter =0;
            int key=-1;

            while(line!=null){
                for(int i = 0; i < GC.WORLD_WIDTH ; i+=imageSizeX) {
                    try {
                        key = Integer.parseInt(String.valueOf(line.charAt(counter)));
                        className = objectTable.get(key);
                    }catch (Exception ex){
                        className = null;

                    }
                    if (className == null || className.equals("") ||className == "UnbreakableWall") {
                        counter++;
                        continue;
                    }
                    c = Class.forName("src.Objects."+className);
                    gameObject = (GameObject) c.getConstructor().newInstance();
                    gameObject.init(i,currY,0,0,0,GW.imgList.get(className));
                    setLayers(key,gameObject);
                    setScore(key,gameObject);
                    GW.addGameObject(gameObject);
                    GW.addColliable((Collidable) gameObject);
                    counter++;
                }
                currY+=imageSizeY;
                counter = 0;
                line = mapReader.readLine();
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void setLayers(int numLayers, GameObject gameObject){
        if(gameObject instanceof BreakableWall){
            ((BreakableWall) gameObject).setLayers(numLayers);
        }
    }

    private void setScore(int numLayers, GameObject gameObject){
        if (gameObject instanceof BreakableWall)
            ((BreakableWall) gameObject).setScore(numLayers*1000);
    }

}
