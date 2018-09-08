package main.maploading;


import main.Key;
import main.core.Entity;
import main.math.Vec2i;

import java.util.ArrayList;

public class Tile{

    private ArrayList<Entity> entities;

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void setDoor(Vec2i mDoor) {
        for (Entity e : entities)
            if (e instanceof Key) ((Key) e).setMatchingDoor(mDoor);
    }
}
