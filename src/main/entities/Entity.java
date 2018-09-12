package main.entities;

import javafx.scene.Node;
import main.component.ViewComponent;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

public class Entity {

    protected String name;
    protected char symbol;

    protected ViewComponent view;
    protected Vec2i pos;
    protected TileMap map;

    public Entity(String name) {
        this(name, null, new Vec2i());
    }


//    public Entity(String name, Function<Vec2i, Vec2d> gridToWorld) {
//        this(name, gridToWorld, new Vec2i(0, 0));
//    }

    public Entity(String name, TileMap map, Vec2i pos) {
        this.name  = name;
        this.view  = new ViewComponent();
        this.pos   = new Vec2i(pos);
        this.map   = map;

        onCreated();
    }


    // Temporary measure
    public void setMap(TileMap map) {
        this.map = map;
    }


    public void onCreated() {

    }


    public void onDestroyed() {

    }



    /*
    View
     */
    public Node getView() {
        return view.getView();
    }



    /*
    Movement
     */

    public void moveTo(int x, int y) {
        map.moveTo(this, x, y);
        pos.set(x, y);
        view.moveTo(getWorldPos().sub(view.getCentre()));
    }

    public void moveTo(Vec2i newPos) {
        moveTo(newPos.getX(), newPos.getY());
    }

    public void moveBy(int dx, int dy) {
        moveTo(pos.getX() + dx, pos.getY() + dy);
    }

    public void moveBy(Vec2i dv) {
        moveBy(dv.getX(), dv.getY());
    }


    /*
    Position
     */

    public Vec2i getGridPos() {
        return new Vec2i(pos);
    }

    public Vec2d getWorldPos() {
        return map.gridPosToWorldPosCentre(pos);
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }


    public int getRow() {
        return pos.getY();
    }

    public int getCol() {
        return pos.getX();
    }


    /*
    Miscellaneous Properties
     */

    public char getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public boolean isPassable() {
        return true;
    }



    /*
    Entity Interactions - to be refactored using an Event System
     */

    /**
     * Handles...
     *
     * @param other the entity that is planning to enter this entity's tile
     * @return true if allowing the other entity to enter this entity's tile
     */
    public boolean onInvadeBy(Entity other) {
        return isPassable();
    }
}
