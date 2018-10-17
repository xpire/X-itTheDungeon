package main.entities.terrain;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.events.DoorEvent;
import main.events.KeyEvent;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;

/**
 * Class describing the Door entity
 */
public class Door extends Terrain {

    // TODO unused methods, eclipse testing, user stories -- deep, assumptions

    private boolean isOpen;
    private Rectangle doorFrame;

    {
        symbol = '|';
        isOpen = false;
    }

    /**
     * Basic constructor
     * @param level : current Level
     */
    public Door(Level level) {
        super(level);
    }

    public Door(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        doorFrame = new Rectangle(6, 30, Color.BLACK);
        view.addNode(doorFrame);
        view.setCentre(new Vec2d(3, 15));

        // Enforce Key-Door coupling in creative mode
        level.addEventHandler(KeyEvent.KEY_REMOVED, e -> {
            if (level.isCreateMode() && e.isMatchingDoor(this)) {
                onDestroyed();
            }
        });
    }


    @Override
    public void onRemovedFromLevel() {
        level.postEvent(new DoorEvent(DoorEvent.DOOR_REMOVED, this));
    }


    /**
     * flag to open a door
     */
    public void onOpen() {
        isOpen = true;
        doorFrame.setFill(Color.SILVER);
    }



    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean isPassableForProp(Prop prop) {
        return prop.isProjectile();
    }

    @Override
    public boolean isPassableForEnemy(Enemy enemy) {
        return isOpen;
    }

    @Override
    public boolean isPassableForAvatar(Avatar avatar) {
        return isOpen;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }


    @Override
    public boolean onPush(Avatar avatar) {

        if (avatar.hasKeyFor(this)) {
            onOpen();
            avatar.useKey();
            return true;
        }

        return false;
    }
}
