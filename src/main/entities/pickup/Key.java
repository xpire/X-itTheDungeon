package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.entities.terrain.Door;
import main.events.DoorEvent;
import main.events.KeyEvent;
import main.maploading.Level;
import main.math.Vec2i;

/**
 * Class describing the key entity
 */
public class Key extends Pickup {

    private Door door;

    {
        symbol = 'K';
        score = 1;
    }

    /**
     * Basic constructor
     * @param level : current level
     */
    public Key(Level level) {
        super(level);
    }
    public Key(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        view.addNode(new Circle(5, Color.HOTPINK));

        // TODO: extract out isCreateMode
        level.addEventHandler(DoorEvent.DOOR_REMOVED, e -> {
            if (level.isCreateMode() && isMatchingDoor(e.getDoor()))
                onDestroyed();
        });
    }

    @Override
    public void onRemovedFromLevel() {
        level.postEvent(new KeyEvent(KeyEvent.KEY_REMOVED, this));
    }

    @Override
    public String getMetaData() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\t%d\t", pos.getX(), pos.getY()));
        sb.append(String.format("%d\t%d", door.getGridPos().getX(), door.getGridPos().getY()));
        return sb.toString();
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpKey(this);
    }

    /**
     * Check if a door is the matching one for this key
     * @param door : door being checked
     * @return true if the door is the matching one
     */
    public boolean isMatchingDoor(Door door) {
        return this.door.equals(door);
    }

    /**
     * Sets the keys matching door
     * @param door : the door to be set as matching
     */
    public void setMatchingDoor(Door door) {
        this.door = door;
    }
}

