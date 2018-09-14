package main.behaviour;

import main.maploading.Tile;
import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.ArrayList;

public class StrategistBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(TileMap map,
                                       Vec2i currLocation,
                                       Vec2i userLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<main.entities.Entity> entities,
                                       ArrayList<Vec2i> entitiesCoord) {
        // Possible coordinates
        ArrayList<Vec2i> pCoord = getPossibleCoord(map,currLocation);
        // Handles case where nothing is accessible -> strategist go for the player
        return null;
    }

    /**
     * Get what coordinate is possible, note that it only checks coordinates no entities
     * @param map Current state of map
     * @param currLocation Current location of the strategist
     * @return list of possible coordinates
     */
    private ArrayList<Vec2i> getPossibleCoord(TileMap map, Vec2i currLocation) {
        ArrayList<Vec2i> ret = new ArrayList<>();
        // Get current coordinate
        int coordX = currLocation.getX();
        int coordY = currLocation.getY();

        // Get dimension of the map
        int dimx = map.getNCols();
        int dimy = map.getNRows();

        // TODO Likely error due to dimension
        if (coordX - 1 >= 0 && (map.isPassable(new Vec2i(coordX - 1, coordY)))) {
            Vec2i buf1 = new Vec2i(coordX - 1, coordY);
            ret.add(buf1);
        }
        if (coordX + 1 < dimx  && (map.isPassable(new Vec2i(coordX + 1, coordY)))) {
            Vec2i buf1 = new Vec2i(coordX + 1, coordY);
            ret.add(buf1);
        }
        if (coordY - 1 >= 0 && (map.isPassable(new Vec2i(coordX, coordY - 1)))) {
            Vec2i buf1 = new Vec2i(coordX, coordY - 1);
            ret.add(buf1);
        }
        if (coordY + 1 < dimy && (map.isPassable(new Vec2i(coordX, coordY + 1)))) {
            Vec2i buf1 = new Vec2i(coordX, coordY + 1);
            ret.add(buf1);
        }
        return ret;
    }

}
