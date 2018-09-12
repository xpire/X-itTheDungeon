package main.maploading;


import main.entities.Boulder;
import main.entities.Door;
import main.entities.Entity;
import main.entities.Key;

import java.util.ArrayList;
import java.util.List;

public class Tile{

    private ArrayList<Entity> entities = new ArrayList<>();

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public boolean isPassable() {

        return entities.stream().noneMatch(e -> !e.isPassable());
    }

    // Create a Pushable component?
    // how do I check if an entity has a pushable component..
    // so broken!
    public boolean isPassableForPushable() {
        return entities.size() == 0;
    }

//    private ArrayList<Entity> save = new ArrayList<>();

    // Essentially, observer

    public void addEntity(Entity entity) {
        entities.add(entity);
        getEntities().forEach(e -> e.onEntityEnter(entity));
    }


    public boolean removeEntity(Entity entity) {

        if (entities.remove(entity)) {
            getEntities().forEach(e -> e.onEntityLeave(entity));
            return true;
        }
        return false;
    }


    public boolean hasEntity(String name) {
        for (Entity e : entities) {
            if (e.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }


    public Boulder getBoulder() {

        for (Entity e : entities) {
            System.out.println(e.getName());

            if (e instanceof Boulder) {
                System.out.println("Boulder Pos: " + ((Boulder) e).getGridPos());
                return (Boulder) e;
            }
        }

        return null;
    }


    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
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
