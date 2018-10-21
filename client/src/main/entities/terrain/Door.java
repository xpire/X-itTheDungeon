package main.entities.terrain;


import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.events.DoorEvent;
import main.events.KeyEvent;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Door entity
 */
public class Door extends Terrain {

    private boolean isOpen;

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

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/terrain/door/0.png"),new Vec2d(-8,-8), 1.875,1.875);
        sprite.addState("Open", getImage("sprite/terrain/door/1.png"), new Vec2d(-8,-15),1.875,1.875);
        view.addNode(sprite);

        level.addEventHandler(KeyEvent.KEY_REMOVED, e -> {
            if (level.isCreateMode() && e.isMatchingDoor(this)) {
                destroy();
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
    private void onOpen() {
        isOpen = true;
        level.postEvent(new DoorEvent(DoorEvent.DOOR_OPENED, this));
        sprite.setState("Open");
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
