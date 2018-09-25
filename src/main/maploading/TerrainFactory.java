package main.maploading;

import main.Level;
import main.entities.terrain.*;
import main.math.Vec2i;

/**
 * Implementation of the factory pattern to get Terrain entities
 */
public class TerrainFactory {

    /**
     * Switch statement to determine and initialise the correct Terrain
     * entity
     * @param entity : symbol of entity
     * @param level : Level to add entity to
     * @param pos : position to add entity at
     * @return an instance of the Entity to be added
     * @throws Exception if unrecognised symbol
     */
    public Terrain getTerrain(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case '|':
                return new Door(level, pos);
            case 'X':
                return new Exit(level, pos);
            case '.':
                return new Ground(level, pos);
            case '#':
                return new Pit(level, pos);
            case '/':
                return new Switch(level, pos);
            case '*':
                return new Wall(level, pos);
            default:
                throw new Exception();
        }
    }
}
