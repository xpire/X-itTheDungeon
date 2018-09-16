package main.maploading;

import main.entities.terrain.*;
import main.math.Vec2i;

public class TerrainFactory {

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
