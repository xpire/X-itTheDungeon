package main.maploading;

import main.entities.Avatar;
import main.math.Vec2i;

public class AvatarFactory {

    public Avatar getAvatar(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case 'P':
                return new Avatar(level, pos);
            default:
                throw new Exception();
        }
    }
}
