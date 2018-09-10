package main.maploading;


import main.Entities.Door;
import main.Entities.Key;
import main.Entities.Entity;
import main.math.Vec2i;

import java.util.ArrayList;

public class Tile{

    private ArrayList<Entity> entities = new ArrayList<>();

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public boolean isPassable() {

        return entities.stream().noneMatch(e -> !e.isPassable());
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void listEntities() {
        for (Entity e : entities) {
            System.out.print(e.getName());
//            testing only
//            if (e instanceof Key) {
//                System.out.print(((Key) e).getMatchingDoor().getName());
//            }
        }
    }

    public Door getDoor() {
        for (Entity e : entities) {
            if (e instanceof Door) return (Door)e;
        }
        return null;
    }

    public Key getKey() {
        for (Entity e : entities) {
            if (e instanceof Key) return (Key)e;
        }
        return null;
    }

}
