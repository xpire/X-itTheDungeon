package main.entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import main.component.ViewComponent;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Terrain;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sound.SoundManager;
import main.sprite.SpriteView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/*
TODO: refactor using the visitor pattern

EntityVisitor {

}
 */

/**
 * Class which represents all Entities available in the Game
 * provides behaviours and interactions for entities
 */

public abstract class Entity {

    public final static double DEFAULT_SIZE = 30.0;
    protected char symbol = '?';

    protected Vec2i pos;
    protected Level level;
    protected ViewComponent view;

    public SpriteView sprite;
    public SoundManager soundManager;// = SoundManager.getInstance(5);


    /**
     * Basic constructor
     * @param level current level
     */
    public Entity(Level level) {
        this.view = new ViewComponent();
        this.pos = new Vec2i(0,0);
        this.level = level;
        soundManager = SoundManager.getInstance(5);
        onCreated();
    }

    public void onLevelRescaled() {
        view.getView().setScaleX(size()/DEFAULT_SIZE);
        view.getView().setScaleY(size()/DEFAULT_SIZE);
        view.moveTo(getWorldPos().sub(view.getCentre()));
    }

    /**
     * Logic when an entity is made
     */
    public void onCreated() {}

    // Removes from map
    /**
     * logic when an entity is removed from the level
     */
    public void onRemovedFromLevel() {}

    /**
     * logic when an entity explodes
     */
    public void onExploded() { }

    /**
     * logic when the turn changes
     */
    public void onTurnUpdate() {}

    /**
     * logic when an entity is destroyed
     */
    public abstract void destroy();


    /*
    View
     */

    /**
     * Getter for the entities view
     * @return entity view
     */
    public Node getView() {
        return view.getView();
    }



    /*
    Movement
     */

    /**
     * Moves an entity by a constant amount
     * @param x - change in x
     * @param y - change in y
     */
    public void setPos(int x, int y) {
        pos = new Vec2i(x, y);
        view.moveTo(getWorldPos().sub(view.getCentre()));
    }


    /**
     * Move to a new vector position
     * @param newPos : position to move to
     */
    public void setPos(Vec2i newPos) {
        setPos(newPos.getX(), newPos.getY());
    }





    /*
    Position
     */

    /**
     * Getter for grid position
     * @return the grid position
     */
    public Vec2i getGridPos() {
        return new Vec2i(pos);
    }

    /**
     * Getter for the x - coord
     * @return x coord
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Getter for the y - coord
     * @return y coord
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Get the entities position on the world
     * @return the world position
     */
    public Vec2d getWorldPos() {
        return level.gridPosToWorldPosCentre(pos);
    }



    /*
    Miscellaneous Properties
     */

    /**
     * Getter for the entities unique symbol
     * @return the unique symbol associated with the entity
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Getter for an entities sprite
     * @return the sprite associated with the entity
     */
    public SpriteView getSprite() {
        return sprite;
    }


    /**
     * Check if entities are allowed to pass through a certain entity
     * @param entity entity requesting to pass through
     * @return true if can pass, false otherwise
     */
    public abstract boolean isPassableFor(Entity entity);
    public boolean isPassableForProp(Prop prop) {
        return isPassableFor(prop);
    }
    public boolean isPassableForEnemy(Enemy enemy) {
        return isPassableFor(enemy);
    }
    public boolean isPassableForAvatar(Avatar avatar) {
        return isPassableFor(avatar);
    }

    /**
     * check if an entity can be stacked on top of a certain entity
     * @param entity entity asking to be stacked
     * @return true if can stack, false otherwise
     */
    public abstract boolean canStackFor(Entity entity);
    public boolean canStackForTerrain(Terrain terrain)  {
        return canStackFor(terrain);
    }
    public boolean canStackForProp(Prop prop)  {
        return canStackFor(prop);
    }
    public boolean canStackForPickup(Pickup pickup)  {
        return canStackFor(pickup);
    }
    public boolean canStackForEnemy(Enemy enemy)  {
        return canStackFor(enemy);
    }
    public boolean canStackForAvatar(Avatar avatar)  {
        return canStackFor(avatar);
    }

    /**
     * logic when an entity enters the current entity
     * @param entity entity which just entered the current tile
     */
    public void onEnterBy(Entity entity) {}
    public void onEnterByProp(Prop prop) {}
    public void onEnterByEnemy(Enemy enemy) {}
    public void onEnterByAvatar(Avatar avatar) {}

    /**
     * logic when an entity leaves the current entity
     * @param entity entity which just left the current tile
     */
    public void onLeaveBy(Entity entity) {}
    public void onLeaveByProp(Prop prop) {}
    public void onLeaveByEnemy(Enemy enemy) {}
    public void onLeaveByAvatar(Avatar avatar) {}

    /**
     * A to-string like implementation which gives
     * information about the entity
     * used in keys and doors to provide their mapping
     * @return
     */
    public String getMetaData() {
        return "";
    }

    public Image getImage(String path) {
        FileInputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        sb.append("./src/asset/").append(path);
        try {
            inputStream = new FileInputStream(sb.toString());
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double size() {
        return level.getSize();
    }
}
