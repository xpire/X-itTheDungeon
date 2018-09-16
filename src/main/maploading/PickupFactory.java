package main.maploading;

import main.entities.pickup.*;
import main.math.Vec2i;

public class PickupFactory {

    public Pickup getPickup(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case '-':
                return new Arrow(level, pos);
            case '!':
                return new Bomb(level, pos);
            case '^':
                return new HoverPotion(level, pos);
            case '>':
                return new InvincibilityPotion(level, pos);
            case 'K':
                return new Key(level, pos);
            case '+':
                return new Sword(level, pos);
            case '$':
                return new Treasure(level, pos);
            default:
                throw new Exception();
        }
    }
}
