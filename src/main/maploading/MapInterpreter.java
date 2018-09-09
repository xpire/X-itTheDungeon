package main.maploading;

import main.Entities.*;

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

    public Entity getSingleEntity(char ch) {
        Entity currEntity = null;
        switch (ch) {
            case 'P':
                currEntity = new Entity("Player");
                //currEntity = new Player();
                break;
            case '*':
                currEntity = new Entity("Wall");
                //currEntity = new Wall();
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
                currEntity = new Entity("Boulder");
                //currEntity = new Boulder();
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
                currEntity = new Entity("Hunter");
                //currEntity = new Hunter();
                break;
            case '2':
                currEntity = new Entity("Strategist");
                //currEntity = new Strategist();
                break;
            case '3':
                currEntity = new Entity("Hound");
                //currEntity = new Hound();
                break;
            case '4':
                currEntity = new Entity("Coward");
                //currEntity = new Coward();
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
