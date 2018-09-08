package main.maploading;

import main.core.Entity;

import java.util.ArrayList;

public class MapInterpreter {

    public ArrayList<Entity> getTileEntities(String tileInput) {
        ArrayList<Entity> tileEntities = new ArrayList<>();

        char[] eachEntity = tileInput.toCharArray();
        for (char ch : eachEntity) {
            tileEntities.add(getSingleEntity(ch));
        }

        return tileEntities;
    }

    // This will need refactoring when entity classes have been
    // properly defined
    public Entity getSingleEntity(char ch) {
        Entity currEntity = null;
        switch (ch) {
            case 'P':
                currEntity = new Entity("Player");
                break;
            case '*':
                currEntity = new Entity("Wall");
                break;
            case 'X':
                currEntity = new Entity("Exit");
                break;
            case '/':
                currEntity = new Entity("Switch");
                break;
            case 'O':
                currEntity = new Entity("Boulder");
                break;
            case '|':
                currEntity = new Entity("Door");
                break;
            case 'K':
                currEntity = new Key("Key");
                break;
            case '$':
                currEntity = new Entity("Treasure");
                break;
            case '+':
                currEntity = new Entity("Sword");
                break;
            case '-':
                currEntity = new Entity("Arrow");
                break;
            case '!':
                currEntity = new Entity("Bomb");
                break;
            case '#':
                currEntity = new Entity("Pit");
                break;
            case '1':
                currEntity = new Entity("Hunter");
                break;
            case '2':
                currEntity = new Entity("Strategist");
                break;
            case '3':
                currEntity = new Entity("Hound");
                break;
            case '4':
                currEntity = new Entity("Coward");
                break;
            case '>':
                currEntity = new Entity("Invincibility Potion");
                break;
            case '^':
                currEntity = new Entity("Hover Potion");
                break;
        }

        if (currEntity == null) System.out.println("Unrecognised Entity");
        return currEntity;
    }
}

/*

    Entity Symbols:

    P : Player
    * : Wall
    X : Exit
    / : Switch
    O : Boulder
    | : Door            must link with a key
    K : Key
    $ : Treasure
    + : Sword
    - : Arrow
    ! : Bomb
    # : Pit
    1 : Hunter
    2 : Strategist
    3 : Hound
    4 : Coward
    > : Invincibility Potion
    ^ : Hover Potion

    Objectives:
    A : Exit the Dungeon
    B : Collect all Treasure
    C : Kill all Enemies
    D : Activate all Switches

*/
