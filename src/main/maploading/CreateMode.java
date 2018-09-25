package main.maploading;

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

        DraftBuilder draftBuilder = new DraftBuilder(8, 8, command[0]);
        draftBuilder.saveMap(draftBuilder.getName(), "drafts");

        System.out.println("You may begin editing " + draftBuilder.getName());
        draftBuilder.displayLevel();

        while (sc.hasNextLine()) {
            if ((command = sc.nextLine().split("\\s+")).length > 0) {
                if (command[0].toLowerCase().equals("exit")) {
                    sc.close();
                    return;
                }
                if (command[0].toLowerCase().equals("delete")) {
                    if (draftBuilder.deleteDraft(draftBuilder.getName())) {
                        System.out.println("Draft deleted");
                        return;
                    }
                }
                createMode.commandInterpreter(draftBuilder, command, sc);
            }
        }

        sc.close();
    }

    /**
     * Converts arguments from scanner into instructions
     * @param draftBuilder instance of the DraftBuilder being used
     * @param command user's input
     * @param sc copy of an open scanner (used when adding keys/doors)
     */
    public void commandInterpreter(DraftBuilder draftBuilder, String[] command, Scanner sc) {
        switch (command[0].toLowerCase()) {
            case "save":
                draftBuilder.saveMap(draftBuilder.getName(), "drafts");

                System.out.println("Map Saved");
                return;
            case "edit":

                Vec2i tile = new Vec2i(Integer.parseInt(command[1]), Integer.parseInt(command[2]));

                if (!draftBuilder.isValidGridPos(tile)) {
                    System.out.println("Error: tile out of bounds");
                    return;
                }

                draftBuilder.editTile(tile, command[3], sc);

                draftBuilder.displayLevel();
                return;
            case "addr":
                draftBuilder.resize(draftBuilder.getNRows() + 1, draftBuilder.getNCols());
                draftBuilder.displayLevel();

                return;
            case "addc":
                draftBuilder.resize(draftBuilder.getNRows(), draftBuilder.getNCols() + 1);
                draftBuilder.displayLevel();

                return;
            case "remr":
                draftBuilder.resize(draftBuilder.getNRows() - 1, draftBuilder.getNCols());
                draftBuilder.displayLevel();

                return;
            case "remc":
                draftBuilder.resize(draftBuilder.getNRows(), draftBuilder.getNCols() - 1);
                draftBuilder.displayLevel();

                return;
            case "resize":
                if (command.length != 3) {
                    System.out.println("Usage: Resize <newNRow> <newNCol>");
                    return;
                }

                draftBuilder.resize(Integer.parseInt(command[1]), Integer.parseInt(command[2]));

                draftBuilder.displayLevel();
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
                    draftBuilder.setObjective(levelObj);
                    draftBuilder.displayLevel();
                } else System.out.println("Error: invalid set of objectives");

                return;
            case "play":
                System.out.println("Please upgrade to premium for just $99.99 a month to access this feature");

                draftBuilder.saveMap(draftBuilder.getName(), "drafts");
                MapLoader mapLoader = new MapLoader();
                Level playTest = mapLoader.loadLevel(draftBuilder.getName(), "drafts");

                //playGame(playTest);

                return;
            case "publish":
                // still needs to check for conquerable
                System.out.println("Please upgrade to premium for just $99.99 a month for complete feature");

                draftBuilder.saveMap(draftBuilder.getName(), "levels");
                System.out.println("Map published");

                return;
            default:
                System.out.println("Command not recognised");
        }
    }

    /**
     * Checks if a set of objectives the user wishes to set is valid
     *
     * @param objectives ArrayList of objectives
     * @return true if value set of objectives, false otherwise
     */
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

        // TODO: enforce that the objective exists for it to be added?
    }
}
