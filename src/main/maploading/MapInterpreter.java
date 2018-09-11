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
//                currEntity = new Entity("Wall");
                currEntity = new Wall();
                break;
            case 'X':
                currEntity = new Entity("Exit");
                //currEntity = new Exit();
                break;
            case '/':
                currEntity = new Entity("Switch");
                //currEntity = new Switch();
                break;
            case 'O':
//                currEntity = new Entity("Boulder");
                currEntity = new Boulder();
                break;
            case '|':
                currEntity = new Door("Door");
                break;
            case 'K':
                currEntity = new Key("Key");
                break;
            case '$':
                currEntity = new Treasure("Treas");
                break;
            case '+':
                currEntity = new Sword("Sword");
                break;
            case '-':
                currEntity = new Arrow("Arrow");
                break;
            case '!':
                currEntity = new Bomb("Bomb");
                break;
            case '#':
                currEntity = new Entity("Pit");
                //currEntity = new Pit();
                break;
            case '1':
                currEntity = new Entity("main.enemies.Hunter");
                //currEntity = new main.enemies.Hunter();
                break;
            case '2':
                currEntity = new Entity("main.enemies.Strategist");
                //currEntity = new main.enemies.Strategist();
                break;
            case '3':
                currEntity = new Entity("main.enemies.Hound");
                //currEntity = new main.enemies.Hound();
                break;
            case '4':
                currEntity = new Entity("main.enemies.Coward");
                //currEntity = new main.enemies.Coward();
                break;
            case '>':
                currEntity = new Entity("Invincibility Potion");
                //currEntity = new InvincibilityPot();
                break;
            case '^':
                currEntity = new Entity("Hover Potion");
                //currEntity = new HoverPot();
                break;
            case '.':
                currEntity = new Ground("Ground");
                break;
            default:
                System.out.println("Unrecognised Entity");
        }

        return currEntity;
    }
}
