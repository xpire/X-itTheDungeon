package main.maploading;

import main.entities.enemies.*;
import main.math.Vec2i;

public class EnemyFactory {

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
