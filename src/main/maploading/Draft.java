package main.maploading;

import main.entities.Entity;
import main.entities.Key;
import main.math.Vec2i;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Draft {

    private TileMap tileMap;
    private String draftName;

    public Draft(String draftName) {
        this(8, 8, draftName);
    }

    public Draft(int nRows, int nCols, String draftName) {
        tileMap = new TileMap(nRows, nCols);
        this.draftName = draftName;
    }

    public int getNRows() {
        return tileMap.getNRows();
    }

    public int getNCols() {
        return tileMap.getNCols();
    }

    public String getDraftName() {
        return draftName;
    }

    public ArrayList<String> getObjectives(){
        return tileMap.getObjectives();
    }

    public void setObj(ArrayList<String> obj) {
        tileMap.setObj(obj);
    }

    public Tile getTile(Vec2i pos) {
        return tileMap.getTile(pos);
    }

    public void displayTileMap() {
        tileMap.displayTileMap();
    }

    public ArrayList<Key> findKeys() {
        return tileMap.findKeys();
    }

    public void resize(int newNRow, int newNCol) {
        tileMap.resize(newNRow, newNCol);
    }

    public void saveMap(String mapName) {
        StringBuilder mapPath = new StringBuilder("./src/main/drafts/");
        mapPath.append(mapName).append(".txt");

        BufferedWriter w = null;
        try {
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
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    Vec2i pos = new Vec2i(i, j);
                    Tile t = getTile(pos);
                    StringBuilder sb = new StringBuilder();

                    if (t.isEmpty()) {
                        sb.append('.');
                    } else {
                        List<Entity> tileEntities = t.getEntities();
                        for (Entity e : tileEntities) {
                            char ch = e.getSymbol();
                            sb.append(ch);
                        }
                    }
                    w.write(sb.toString() + "\t");
                }
                w.newLine();
            }

            //set the objectives
            ArrayList<String> obj = getObjectives();
            for (String s : obj) {
                w.write(s + "\t");
            }
            w.newLine();

            //set the key-door mapping
            ArrayList<Key> mapKeys = findKeys();
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

    public void editTile(Vec2i tile, String entities) {
        Tile t = tileMap.getTile(tile);
        MapInterpreter mapInterpreter = new MapInterpreter();

        t.setEntities(mapInterpreter.getTileEntities(entities));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] command = null;

        while (command == null) {
            System.out.println("Please name this draft");
            if ((command = sc.nextLine().split("\\s+")).length != 1) {
                System.out.println("Error: no white space allowed");
                command = null;
            } else if (command[0].equals("Exit")) return;
        }

        Draft draft = new Draft(command[0]);

        //temp save method here
        draft.saveMap(draft.getDraftName());

        System.out.println("You may begin editing " + draft.getDraftName());
        draft.displayTileMap();

        while ((command = sc.nextLine().split("\\s+")).length > 0) {
            if (command[0].equals("Exit")) return;
            draft.commandInterpreter(command);
        }

        sc.close();
    }

    public void commandInterpreter(String[] command) {
        switch (command[0]) {
            case "save":
            case "Save":
                saveMap(getDraftName());

                System.out.println("Map Saved");
                return;
            case "edit":
            case "Edit":
                if (command.length != 4) {
                    System.out.println("Usage: <X coord> <Y coord> <Entities String>");
                    return;
                }

                Vec2i tile = new Vec2i(Integer.parseInt(command[1]),
                        Integer.parseInt(command[2]));

                editTile(tile, command[3]);

                displayTileMap();
                return;
            case "resize":
            case "Resize":
                if (command.length != 3) {
                    System.out.println("Usage: Resize <newNRow> <newNCol>");
                    return;
                }

                resize(Integer.parseInt(command[1]),
                        Integer.parseInt(command[2]));

                displayTileMap();
                return;
            case "obj":
            case "objective":
            case "Objective":
                if (command.length > 4) {
                    System.out.println("Error: invalid set of objectives");
                    return;
                }

                ArrayList<String> levelObj = new ArrayList<>();

                for (int i = 1; i < command.length; i++) {
                    levelObj.add(command[i]);
                }

                setObj(levelObj);
                displayTileMap();
                return;
            case "play":
            case "Play":
                System.out.println("Not yet available");
                return;
            case "publish":
            case "Publish":
                System.out.println("Not yet available");
                return;
            default:
                System.out.println("Command not recognised");
        }
    }
}
