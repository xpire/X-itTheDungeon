package main.maploading;

import main.entities.*;

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


    /*
    TODO:
    make it cleaner, e.g. store the character to entity mapping in a hash table
    and return a clone of the entity corresponding to the given character
    Look up: Prototype Pattern
     */
    public Entity getSingleEntity(char ch) {
        Entity currEntity = null;


        switch (ch) {
            case 'P':
                currEntity = new Entity("Player");
                //currEntity = new MockAvatar();
                break;
            case '*':
                currEntity = new Wall();
                break;
            case 'X':
                currEntity = new Exit();
                break;
            case '/':
                currEntity = new Switch();
                break;
            case 'O':
                currEntity = new Boulder();
                break;
            case '|':
                currEntity = new Door();
                break;
            case 'K':
                currEntity = new Key();
                break;
            case '$':
                currEntity = new Treasure();
                break;
            case '+':
                currEntity = new Sword();
                break;
            case '-':
                currEntity = new Arrow();
                break;
            case '!':
                currEntity = new Bomb();
                break;
            case '#':
                currEntity = new Pit();
                break;
            case '1':
                currEntity = new Entity("Hunter", '1');
                //currEntity = new main.enemies.Hunter();
                break;
            case '2':
                currEntity = new Entity("Strategist", '2');
                //currEntity = new main.enemies.Strategist();
                break;
            case '3':
                currEntity = new Entity("Hound", '3');
                //currEntity = new main.enemies.Hound();
                break;
            case '4':
                currEntity = new Entity("Coward", '4');
                //currEntity = new main.enemies.Coward();
                break;
            case '>':
                currEntity = new InvincibilityPot();
                break;
            case '^':
                currEntity = new HoverPot();
                break;
            case '.':
                currEntity = new Ground();
                break;
            default:
                System.out.println("Unrecognised Entity");
        }

        return currEntity;
    }
}
