package main.entities.terrain;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.events.DoorEvent;
import main.events.KeyEvent;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Door extends Terrain {

    private boolean isOpen;
    private Rectangle doorFrame;

    {
        symbol = '|';
    }

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

        isOpen = false;

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


    public void onOpen() {
        isOpen = true;
        doorFrame.setFill(Color.WHITE);
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
    public boolean onPush(Avatar avatar) {

        if (avatar.hasKeyFor(this)) {
            onOpen();
            avatar.useKey();
            return true;
        }

        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackForProp(Prop prop) {
        return canStackFor(prop);
    }

    @Override
    public boolean canStackForPickup(Pickup pickup) {
        return canStackFor(pickup);
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return canStackFor(enemy);
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return canStackFor(avatar);
    }
}
