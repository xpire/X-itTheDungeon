package main.core;

import main.math.Vec2i;

public class Entity {

    private String name;

    public Entity(String name) {
        this.name = name;
//        this.coord = new Vec2i(0,0);
    }

    public String getName() {
        return name;
    }

//    public Vec2i getCoord() { return coord; }

//    public void setCoord(int x, int y) {this.coord = new Vec2i(x,y); }

}
