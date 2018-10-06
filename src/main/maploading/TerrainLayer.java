package main.maploading;

import main.component.ViewComponent;
import main.entities.terrain.Ground;
import main.entities.terrain.Terrain;
import main.math.Vec2i;
import main.util.Array2DIterator;

import java.util.Iterator;
import java.util.function.Supplier;

public class TerrainLayer extends EntityLayer<Terrain> {

    private final Supplier<Terrain> DEFAULT_TERRAIN = () -> new Ground(level);

    private int nRows;
    private int nCols;

    private Terrain[][] terrains;

    public TerrainLayer(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;


//        GridPane gridView = new GridPane();
//        gridView.setMinSize(nCols * size, nRows * size);
//        gridView.gridLinesVisibleProperty().set(true);

        terrains = new Terrain[nRows][nCols];
        view = new ViewComponent();
    }


    @Override
    public Terrain getEntity(Vec2i pos) {
        return terrains[pos.getY()][pos.getX()];
    }

    @Override
    protected void setEntityRaw(Vec2i pos, Terrain entity) {
        terrains[pos.getY()][pos.getX()] = entity;
    }

    @Override
    protected Terrain removeEntityRaw(Vec2i pos) {
        Terrain terrain = terrains[pos.getY()][pos.getX()];
        terrains[pos.getY()][pos.getX()] = null;
        return terrain;
    }

    public Terrain removeEntity(Vec2i pos, boolean replaceWithDefault) {
        Terrain terrain = removeEntity(pos);

        if (replaceWithDefault)
            addEntity(pos, DEFAULT_TERRAIN.get());

        return terrain;
    }

    @Override
    public Iterator<Terrain> iterator() {
        return new Array2DIterator<>(terrains);
    }


    @Override
    public void resize(int newNRows, int newNCols) {
        super.resize(newNRows, newNCols);

        Terrain[][] newTerrains = new Terrain[newNRows][newNCols];

        int copyNRow = Math.min(nRows, newNRows);
        int copyNCol = Math.min(nCols, newNCols);

        for (int i = 0; i < copyNRow; i++) {
            for (int j = 0; j < copyNCol; j++) {
                newTerrains[i][j] = terrains[i][j];
            }
        }

        nRows = newNRows;
        nCols = newNCols;
        terrains = newTerrains;

        for (int i = 0; i < newNRows; i++) {
            for (int j = 0; j < newNCols; j++) {
                Vec2i pos = new Vec2i(j, i);
                if (!hasEntity(pos))
                    addEntity(pos, DEFAULT_TERRAIN.get());
            }
        }
    }
}