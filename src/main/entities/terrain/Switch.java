package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.events.SwitchEvent;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Switch extends Terrain{

    private boolean wasOn;
    private boolean isOn;
    private Rectangle rect;

    {
        symbol  = '/';
        wasOn   = false;
        isOn    = false;
    }

    public Switch(Level level) {
        super(level);
    }

    public Switch(Level level, Vec2i pos) {
        super(level, pos);
    }

    public void onActivated() {
        isOn = true;
        rect.setFill(Color.GREEN);

    }

    public void onDeactivated() {
        isOn = false;
        rect.setFill(Color.RED);
    }

    @Override
    public void onCreated(){
        rect = new Rectangle();
        rect.setWidth(30);
        rect.setHeight(30);

        onDeactivated();

        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));

        level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_CREATED));
    }

    @Override
    public void onDestroyed() {
        level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_DESTROYED));
    }

    @Override
    public void onEnterByProp(Prop prop) {
        if (prop.isHeavy()) {
            onActivated();
        }
    }

    @Override
    public void onLeaveByProp(Prop prop) {
        if (prop.isHeavy()) {
            onDeactivated();
        }
    }

    @Override
    public void onTurnUpdate() {
        if (!wasOn && isOn) {
            level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_ACTIVATED));
        } else if (wasOn && !isOn) {
            level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_DEACTIVATED));
        }
        wasOn = isOn;
    }

    public boolean canStackFor(Entity entity) {
        return true;
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

    public boolean isPassableFor(Entity entity) {
        return true;
    }
}
