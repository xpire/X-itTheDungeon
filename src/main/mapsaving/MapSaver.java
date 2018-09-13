package main.mapsaving;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import main.entities.Entity;
import main.entities.Key;
import main.maploading.MapLoader;
import main.maploading.Tile;
import main.maploading.TileMap;
import main.math.Vec2i;

public class MapSaver{

    public void saveMap(TileMap tileMap, String mapName) {
        StringBuilder mapPath = new StringBuilder("./src/main/drafts/");
        mapPath.append(mapName).append(".txt");

        BufferedWriter w = null;
        try {
            w = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(
                    new File(mapPath.toString()))));
            
            int nRow = tileMap.getNRows();
            int nCol = tileMap.getNCols();

            //set the map size
            w.write(nRow + "\t" + nCol);
            w.append("\n");

            //set the main body of the map
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    Vec2i pos = new Vec2i(i, j);
                    Tile t = tileMap.getTile(pos);
                    StringBuilder sb = new StringBuilder();

                    List<Entity> tileEntities = t.getEntities();
                    for (Entity e : tileEntities) {
                        char ch = e.getSymbol();
                        sb.append(ch);
                    }
                    w.write(sb.toString() + "\t");
                }
                w.newLine();
            }

            //set the objectives
            ArrayList<String> obj = tileMap.getObjectives();
            for (String s : obj) {
                w.write(s + "\t");
            }
            w.newLine();

            //set the key-door mapping
            ArrayList<Key> mapKeys = tileMap.findKeys();
            for (Key k : mapKeys) {
                Vec2i kPos = k.getGridPos();
                w.write(kPos.getX() + "\t" + kPos.getY() + "\t");
                Vec2i dPos = k.getMatchingDoor().getGridPos();
                w.write(dPos.getX() + "\t" + dPos.getY());
                w.newLine();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (w != null) w.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader();
        TileMap tileMap = mapLoader.getTileMap("map1");

        MapSaver mapSaver = new MapSaver();
        mapSaver.saveMap(tileMap, "map1.1");

    }

}