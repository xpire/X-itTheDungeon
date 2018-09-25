package main.maploading;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2i;

/**
 * Implementation of the factory pattern to get Enemy entities
 */
public class AvatarFactory {

    /**
     * Switch statement to determine and initialise the correct Pickup
     * entity
     * @param entity : symbol of entity
     * @param level : Level to add entity to
     * @param pos : position to add entity at
     * @return an instance of the Entity to be added
     * @throws Exception if unrecognised symbol
     */
    public Avatar getAvatar(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case 'P':
                return new Avatar(level, pos);
            default:
                throw new Exception();
        }
    }
}
