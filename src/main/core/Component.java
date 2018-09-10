package main.core;


import main.Entities.Entity;

import java.util.ArrayList;

public abstract class Component {

    protected Entity entity = null;

    protected ArrayList<Component> components = new ArrayList<>();


    public void update(float delta) {

        for (Component c : components) {
            c.update(delta);
        }
    }

    public Entity getEntity() {
        return entity;
    }


    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
