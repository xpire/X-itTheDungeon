package main.maploading;

import main.Level;
import main.entities.prop.Boulder;
import main.entities.prop.Prop;
import main.math.Vec2i;

/**
 * Implementation of the factory pattern to get Prop entities
 */
public class PropFactory {

    /**
     * Switch statement to determine and initialise the correct Prop
     * entity
     * @param entity : symbol of entity
     * @param level : Level to add entity to
     * @param pos : position to add entity at
     * @return an instance of the Entity to be added
     * @throws Exception if unrecognised symbol
     */
    public Prop getProp(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case 'O':
                return new Boulder(level, pos);
            default:
                throw new Exception();
        }
    }
}
