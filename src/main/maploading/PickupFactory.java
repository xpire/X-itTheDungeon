package main.maploading;

import main.Level;
import main.entities.pickup.*;
import main.math.Vec2i;

/**
 * Implementation of the factory pattern to get Pickup entities
 */
public class PickupFactory {

    /**
     * Switch statement to determine and initialise the correct Pickup
     * entity
     * @param entity : symbol of entity
     * @param level : Level to add entity to
     * @param pos : position to add entity at
     * @return an instance of the Entity to be added
     * @throws Exception if unrecognised symbol
     */
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
