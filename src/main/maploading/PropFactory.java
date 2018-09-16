package main.maploading;

import main.entities.prop.Boulder;
import main.entities.prop.Prop;
import main.math.Vec2i;

public class PropFactory {

    public Prop getProp(char entity, Level level, Vec2i pos) throws Exception {
        switch (entity) {
            case 'O':
                return new Boulder(level, pos);
            default:
                throw new Exception();
        }
    }
}
