package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.entities.terrain.Door;
import main.events.DoorEvent;
import main.events.KeyEvent;
import main.maploading.Level;
import main.math.Vec2i;

public class Key extends Pickup {

    {
        score = 1;
    }
    private Door door;
    private Circle circ;

    {
        symbol = 'K';
    }

    public Key(Level level) {
        super(level);
    }

    public Key(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        circ = new Circle(5, Color.HOTPINK);
        view.addNode(circ);

        // TODO: extract out isCreateMode
        level.addEventHandler(DoorEvent.DOOR_REMOVED, e -> {
            if (level.isCreateMode()) {
                Door d = e.getDoor();
                if (isMatchingDoor(d)) {
                    onDestroyed();
                }
            }
        });
    }

    @Override
    public void onRemovedFromLevel() {
        level.postEvent(new KeyEvent(KeyEvent.KEY_REMOVED, this));
    }

    @Override
    public String getMetaData() {
        StringBuilder sb = new StringBuilder();

        sb.append(pos.getX()).append("\t").append(pos.getY()).append("\t");
        sb.append(door.getGridPos().getX()).append("\t").append(door.getGridPos().getY());

        return sb.toString();
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpKey(this);
    }


    public Door getMatchingDoor() {
        return door;
    }

    public void setMatchingDoor(Door door) {
        this.door = door;
    }

    public boolean isMatchingDoor(Door door) {
        return this.door.equals(door);
    }
}

