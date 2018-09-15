package main.maploading;

import main.entities.Door;
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

    public void saveMap(String mapName, String path) {
        StringBuilder mapPath = new StringBuilder("./src/main/");
        mapPath.append(path).append("/").append(mapName).append(".txt");

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
                    Vec2i pos = new Vec2i(j, i);
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

        ArrayList<Entity> entities1 =mapInterpreter.getTileEntities(entities);
        entities1.forEach(e -> {
            if (e instanceof Door) {
                ((Door) e).setPos(tile);
            }

        });
        t.setEntities(entities1);
    }

    public void addToTile(Vec2i tile, char entity) {
        Tile t = tileMap.getTile(tile);
        MapInterpreter mapInterpreter = new MapInterpreter();

        Entity e = mapInterpreter.getSingleEntity(entity);

        if (e instanceof Key) {
            ((Key) e).setPos(tile);
        }

        t.addEntity(e);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] command = null;

        while (command == null) {
            System.out.println("Please name this draft");
            if ((command = sc.nextLine().split("\\s+")).length != 1) {
                System.out.println("Error: no white space allowed");
                command = null;
            } else if (command[0].equals("exit")) {
                sc.close();
                return;
            }
        }

        Draft draft = new Draft(command[0]);

        draft.saveMap(draft.getDraftName(), "drafts");

        System.out.println("You may begin editing " + draft.getDraftName());
        draft.displayTileMap();

        while (sc.hasNextLine()) {
            if ((command = sc.nextLine().split("\\s+")).length > 0) {
                if (command[0].equals("exit")) {
                    sc.close();
                    return;
                }
                draft.commandInterpreter(command, sc);
            }
        }

        sc.close();
    }

    public void commandInterpreter(String[] command, Scanner sc) {
        switch (command[0].toLowerCase()) {
            case "save":
                saveMap(getDraftName(), "drafts");

                System.out.println("Map Saved");
                return;
            case "edit":
                if (!isValidEdit(command)) {
                    System.out.println("Error: invalid command");
                    return;
                }

                Vec2i tile = new Vec2i(Integer.parseInt(command[1]), Integer.parseInt(command[2]));

                if (!tileMap.isValidGridPos(tile)) {
                    System.out.println("Error: tile out of bounds");
                    return;
                }

                editTile(tile, command[3]);

                if (isDoor(command[3])) {
                    String[] matchingKey = null;

                    while (matchingKey == null) {
                        System.out.println("Please set the matching key");
                        if (sc.hasNextLine()) {
                            matchingKey = sc.nextLine().split("\\s+");
                            if (matchingKey.length != 2) {
                                System.out.println("Error: must be a tile location");
                                matchingKey = null;
                            }
                        }
                    }

                    //contract that this is accurate
                    Vec2i keyCoord = new Vec2i(Integer.parseInt(matchingKey[0]),
                            Integer.parseInt(matchingKey[1]));

                    addToTile(keyCoord, 'K');
                    Key k = tileMap.getTile(keyCoord).getKey();
                    Door d = tileMap.getTile(tile).getDoor();

                    k.setMatchingDoor(d);

                }

                displayTileMap();
                return;
            case "addr":
                resize((getNRows() + 1), getNCols());
                displayTileMap();

                return;
            case "addc":
                resize(getNRows(), (getNCols() + 1));
                displayTileMap();

                return;
            case "remr":
                resize((getNRows() - 1), getNCols());
                displayTileMap();

                return;
            case "remc":
                resize(getNRows(), (getNCols() - 1));
                displayTileMap();

                return;
            case "resize":
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
                if (command.length > 4) {
                    System.out.println("Error: too many objectives");
                    return;
                }

                ArrayList<String> levelObj = new ArrayList<>();

                for (int i = 1; i < command.length; i++) levelObj.add(command[i]);

                if (isValidObj(levelObj)) {
                    setObj(levelObj);
                    displayTileMap();
                } else System.out.println("Error: invalid set of objectives");

                return;
            case "play":
                System.out.println("Please upgrade to premium for just $99.99 a month to access this feature");

                saveMap(getDraftName(), "draft");
                MapLoader mapLoader = new MapLoader();
                Level playTest = mapLoader.getLevel(getDraftName(), "drafts");

                //playGame(playTest);

                return;
            case "publish":
                // still needs to check for conquerable
                System.out.println("Please upgrade to premium for just $99.99 a month for complete feature");

                saveMap(getDraftName(), "levels");
                System.out.println("Map published");

                return;
            default:
                System.out.println("Command not recognised");
        }
    }

    private boolean isValidEdit(String[] command) {
        if (command.length != 4) {
            System.out.println("Usage: Edit <X coord> < Y coord> <Entities>");
            return false;
        }

        String validEntities = "P*X/O|$+-!#1234>^.";
        char[] ch = command[3].toCharArray();

        for (char c : ch) {
            if (!validEntities.contains(Character.toString(c))) {
                System.out.println("Error: unrecognised entities");
                System.out.println("Note: keys can only be placed by placing a door");
                return false;
            }
        }

        if (ch.length == 1) return true;

        if (ch.length > 3 || ch.length == 0) {
            System.out.println("Error: invalid number of entities");
            return false;
        }

        //check for duplicates

        //layer 1 entities
        String neverStack = "*X|#";
        for (char c : ch) {
            if (neverStack.contains(Character.toString(c))) return false;
        }

        //layer 2 entities
        //cannot stack each other

        //layer 3 entities

        return true;
    }

    private boolean isDoor(String entities) {
        return entities.contains("|");
    }

    private boolean isValidObj(ArrayList<String> objectives) {
        //check all objectives are a single letter between A and D
        for (String s : objectives) {
            char[] ch = s.toCharArray();
            if (ch.length != 1) {
                System.out.println(s + " is not a valid objective");
                return false;
            }
            if (!(ch[0] >= 'A') || !(ch[0] <= 'D')) return false;
        }

        if (objectives.size() == 1) return true;

        //check "Exit" objective isn't coupled with others
        return (!objectives.contains("A"));

        //enforce that the objective exists for it to be added?
    }
}
