package main.maploading;

import main.entities.Entity;
import main.math.Vec2i;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class DraftBuilder {

    private Level level;

    public DraftBuilder(int nRows, int nCols, String draftName) {
        level = new Level(nRows, nCols, 30.0, draftName, true);
    }

    public String getName() {
        return level.getName();
    }

    public void displayLevel() {
        level.displayLevel();
    }

    public boolean isValidGridPos(Vec2i tile) {
        return level.isValidGridPos(tile);
    }

    public void setObjective(ArrayList<String> objectives) {
        level.setObjectives(objectives);
    }

    public void resize(int newNRow, int newNCol) {
        level.resize(newNRow, newNCol);
    }

    public int getNRows() {
        return level.getNRows();
    }

    public int getNCols() {
        return level.getNCols();
    }

    public void saveMap(String mapName, String path) {
        StringBuilder mapPath = new StringBuilder("./src/main/");
        mapPath.append(path).append("/").append(mapName).append(".txt");

        BufferedWriter w = null;
        try {

            //beautiful use of Decorator Pattern
            w = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(mapPath.toString()))));

            int nRow = getNRows();
            int nCol = getNCols();

            //set the map size
            w.write(nRow + "\t" + nCol);
            w.append("\n");

            //set the main body of the map
            StringBuilder metaData = new StringBuilder();

            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    Vec2i pos = new Vec2i(j, i);
                    StringBuilder sb = new StringBuilder();

                    Iterator<Entity> it = level.getEntitiesAt(pos);

                    while (it.hasNext()) {
                        Entity e = it.next();

                        sb.append(e.getSymbol());
                        if (e.getMetaData() != null)
                            metaData.append(e.getMetaData());
                    }

                    w.write(sb.toString() + "\t");
                }
                w.newLine();
            }

            //set the objectives
            ArrayList<String> obj = level.getObjectives();
            for (String s : obj) {
                w.write(s + "\t");
            }
            w.newLine();

            //set the key-door mapping
            w.write(metaData.toString());

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

    public void editTile(Vec2i tile, String entities, Scanner sc) {
        LevelBuilder levelBuilder = new LevelBuilder();

        char[] eachEnt = entities.toCharArray();
        for (char ent : eachEnt) {
            try {
                if (ent != 'K' && ent != '|') {
                    levelBuilder.buildEntity(ent, tile, level);

                } else {//// if (ent == 'K') {

                    String[] matching = null;

                    while (matching == null) {
                        System.out.println("Please set the matching Key/Door");
                        if (sc.hasNextLine()) {
                            matching = sc.nextLine().split("\\s+");
                            if (matching.length != 2) {
                                System.out.println("Error: must be a tile location");
                                matching = null;
                            }
                        }
                    }

                    Vec2i matchingPos = new Vec2i(Integer.parseInt(matching[0]),
                            Integer.parseInt(matching[1]));

                    if (ent == 'K') {
                        levelBuilder.buildKeyDoor(level, tile, matchingPos);
                    } else {
                        levelBuilder.buildKeyDoor(level, matchingPos, tile);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean deleteDraft(String draftName) {
        StringBuilder draftPath = new StringBuilder("./src/main/drafts/");
        draftPath.append(draftName).append(".txt");

        File draft = new File(draftPath.toString());

        return draft.delete();
    }
}