package main.maploading;

import main.Level;
import main.entities.enemies.*;
import main.math.Vec2i;

/**
 * Implementation of the factory pattern to get Enemy entities
 */
public class EnemyFactory {

    /**
     * Switch statement to determine and initialise the correct Enemy
     * entity
     * @param entity : symbol of entity
     * @param level : Level to add entity to
     * @param pos : position to add entity at
     * @return an instance of the Entity to be added
     * @throws Exception if unrecognised symbol
     */
    public Enemy getEnemy(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case '1':
                return new Hunter(level, pos);
            case '2':
                return new Strategist(level, pos);
            case '3':
                return new Hound(level, pos);
            case '4':
                return new Coward(level, pos);
            default:
                throw new Exception();
        }
    }

}
