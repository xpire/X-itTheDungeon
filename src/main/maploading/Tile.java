package main.maploading;


import main.core.Entity;

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
}
