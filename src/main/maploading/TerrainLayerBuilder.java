package main.maploading;

import main.Level;
import main.entities.terrain.*;
import main.math.Vec2i;

public class TerrainLayerBuilder extends LayerBuilder<Terrain> {

    public TerrainLayerBuilder(Level level, EntityFactory<Terrain> factory) {
        super(level, factory);
//        EntityFactory<Terrain> factory = new EntityFactory<>();
//        factory.addSupplier('|', () -> new Door(level));
//        factory.addSupplier('X', () -> new Exit(level));
//        factory.addSupplier('.', () -> new Ground(level));
//        factory.addSupplier('#', () -> new Pit(level));
//        factory.addSupplier('/', () -> new Switch(level));
//        factory.addSupplier('*', () -> new Wall(level));
    }

    @Override
    protected boolean canPlaceEntity(Vec2i pos, Terrain entity) {
        return level.canPlaceTerrain(pos, entity);
    }

    @Override
    protected boolean canReplaceEntity(Vec2i pos, Terrain entity) {
        return level.canReplaceTerrain(pos, entity);
    }

    @Override
    protected EntityLayer<Terrain> getLayer() {
        return level.getTerrainLayer();
    }
}
