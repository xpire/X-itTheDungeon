package main.entities;

import javafx.scene.Node;
import main.component.ViewComponent;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Terrain;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;


/*
TODO: refactor using the visitor pattern

EntityVisitor {

}
 */


public abstract class Entity {

    protected char symbol = '?';

    protected Vec2i pos;
    protected Level level;
    protected ViewComponent view;

    public Entity(Level level) {
        this(level, new Vec2i());
    }

    public Entity(Level level, Vec2i pos) {
        this.view  = new ViewComponent();
        this.pos   = new Vec2i(pos);
        this.level = level;

        onCreated();
    }

    public void onCreated() {}

    public String getMetaData() {

        return null;
    }

    // Removes from map
    public abstract void onDestroyed();

    public void onRemovedFromLevel() {}

    public void onExploded() { }

    public void onTurnUpdate() {}



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
//        Vec2i from = new Vec2i(pos);
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

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public Vec2d getWorldPos() {
        return level.gridPosToWorldPosCentre(pos);
    }



    /*
    Miscellaneous Properties
     */

    public char getSymbol() {
        return symbol;
    }



    public boolean isPassableFor(Entity entity) {
        return true;
    }
    public boolean isPassableForProp(Prop prop) {
        return isPassableFor(prop);
    }
    public boolean isPassableForEnemy(Enemy enemy) {
        return isPassableFor(enemy);
    }
    public boolean isPassableForAvatar(Avatar avatar) {
        return isPassableFor(avatar);
    }

    public abstract boolean canStackFor(Entity entity);
    public abstract boolean canStackForTerrain(Terrain terrain);
    public abstract boolean canStackForProp(Prop prop);
    public abstract boolean canStackForPickup(Pickup pickup);
    public abstract boolean canStackForEnemy(Enemy enemy);
    public abstract boolean canStackForAvatar(Avatar avatar);

    public void onEnterBy(Entity entity) {}
    public void onEnterByProp(Prop prop) {}
    public void onEnterByEnemy(Enemy enemy) {}
    public void onEnterByAvatar(Avatar avatar) {}

    public void onLeaveBy(Entity entity) {}
    public void onLeaveByProp(Prop prop) {}
    public void onLeaveByEnemy(Enemy enemy) {}
    public void onLeaveByAvatar(Avatar avatar) {}
}
