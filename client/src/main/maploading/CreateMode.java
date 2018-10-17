package main.maploading;

import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Terminal based implementation of the games Create Mode
 * Allows users to create complete original maps
 */
public class CreateMode {

    /**
     * Main function to run the Map Creator
     * @param args Never used
     */
    public static void main(String[] args) {
        CreateMode createMode = new CreateMode();

        Scanner sc = new Scanner(System.in);
        String[] command = null;

        System.out.println("Welcome to the Creation Lab");
        while (command == null) {
            System.out.println("Please name this draft");
            if ((command = sc.nextLine().split("\\s+")).length != 1) {
                System.out.println("Error: no white space allowed");
                command = null;
            } else if (!command[0].matches("[a-zA-Z0-9]+")) {
                System.out.println("Error: alphanumeric characters only");
                command = null;
            }
        }

        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel(command[0], "drafts", true);

        DraftBuilder draftBuilder = (level == null) ? new DraftBuilder(8, 8, command[0]) : new DraftBuilder(level);
        draftBuilder.saveMap(draftBuilder.getName(), "drafts");

        System.out.println("You may begin editing " + draftBuilder.getName());
        draftBuilder.displayLevel();

        while (sc.hasNextLine()) {
            if ((command = sc.nextLine().split("\\s+")).length > 0) {

                if (!createMode.commandInterpreter(draftBuilder, command, sc)) break;
            }
        }

        sc.close();
    }

    /**
     * Converts arguments from scanner into instructions
     * @param draftBuilder instance of the DraftBuilder being used
     * @param command user's input
     * @param sc copy of an open scanner (used when adding keys/doors)
     * @return false when the command should exit the creation environment, true otherwise
     */
    public boolean commandInterpreter(DraftBuilder draftBuilder, String[] command, Scanner sc) {
        switch (command[0].toLowerCase()) {
            case "save":
                draftBuilder.saveMap(draftBuilder.getName(), "drafts");

                System.out.println("Map Saved");
                return true;
            case "edit":
                if (command.length != 4) {
                    System.out.println("Usage : edit <x-coord> <y-coord> <entities>");
                    return true;
                }

                try {
                    Vec2i tile = new Vec2i(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                    if (!draftBuilder.isValidGridPos(tile)) {
                        System.out.println("Error: tile out of bounds");
                        return true;
                    }

                    draftBuilder.editTile(tile, command[3], sc);

                    draftBuilder.displayLevel();
                } catch (NumberFormatException e) {
                    System.out.println("Error: bad arguments");
                }

                return true;
            case "addr":
                draftBuilder.resize(draftBuilder.getNRows() + 1, draftBuilder.getNCols());
                draftBuilder.displayLevel();

                return true;
            case "addc":
                draftBuilder.resize(draftBuilder.getNRows(), draftBuilder.getNCols() + 1);
                draftBuilder.displayLevel();

                return true;
            case "remr":
                draftBuilder.resize(draftBuilder.getNRows() - 1, draftBuilder.getNCols());
                draftBuilder.displayLevel();

                return true;
            case "remc":
                draftBuilder.resize(draftBuilder.getNRows(), draftBuilder.getNCols() - 1);
                draftBuilder.displayLevel();

                return true;
            case "resize":
                if (command.length != 3) {
                    System.out.println("Usage: Resize <newNRow> <newNCol>");
                    return true;
                }
                try {
                    draftBuilder.resize(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                    draftBuilder.displayLevel();
                } catch (NumberFormatException e) {
                    System.out.println("Error: bad arguments");
                }

                return true;
            case "obj":
            case "objective":
                if (command.length > 4) {
                    System.out.println("Error: too many objective");
                    return true;
                }

                ArrayList<String> levelObj = new ArrayList<>();

                for (int i = 1; i < command.length && !levelObj.contains(command[i]); i++)
                    levelObj.add(command[i]);

                if (isValidObj(levelObj)) {
                    draftBuilder.setObjective(levelObj);
                    draftBuilder.displayLevel();
                } else System.out.println("Error: invalid set of objective");

                return true;
            case "play":
                System.out.println("Please upgrade to premium for just $99.99 a month to access this feature");

                draftBuilder.saveMap(draftBuilder.getName(), "drafts");
                MapLoader mapLoader = new MapLoader();
                Level playTest = mapLoader.loadLevel(draftBuilder.getName(), "drafts", false);

                //playGame(playTest);

                return true;
            case "publish":
                // still needs to check for conquerable
                System.out.println("Please upgrade to premium for just $99.99 a month for complete feature");

                draftBuilder.saveMap(draftBuilder.getName(), "levels");
                System.out.println("Map published");

                return true;
            case "delete":
                return !draftBuilder.deleteDraft(draftBuilder.getName());
            case "exit":
                if (command.length > 1) draftBuilder.saveMap(draftBuilder.getName(), "drafts");

                return false;
            case "help":
                System.out.println("Valid commands are:");
                System.out.println("save : saves the draft");
                System.out.println("edit <x-coord> <y-coord> <entities> : places 'entities' onto tile (x,y)");
                System.out.println("addr; addc; remr; remc : appends row/col; removes row/col");
                System.out.println("resize <newNumRows> <newNumCols> : resizes the map to newNumRows x newNumCols");
                System.out.println("obj/objective <obj_1> (<obj_2>) (<obj_3>) : sets the levels objective as per input");
                System.out.println("play : test-plays the map");
                System.out.println("publish : publishes the map");
                System.out.println("delete : deletes the draft");
                System.out.println("exit (<s>) : exits Create Mode; flag <s> will save before exiting");
                System.out.println("help : displays valid commands");

                return true;
            default:
                System.out.println("Command not recognised, type 'help' for more info");
                return true;
        }
    }

    /**
     * Checks if a set of objective the user wishes to set is valid
     *
     * @param objectives ArrayList of objective
     * @return true if value set of objective, false otherwise
     */
    private boolean isValidObj(ArrayList<String> objectives) {
        //check all objective are a single letter between A and D
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
    }
}
