package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Entity;
import main.entities.prop.Prop;
import main.events.SwitchEvent;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Switch extends Terrain{

    private boolean wasOn;
    private boolean isOn;
    private Rectangle floorSwitch;

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
        floorSwitch.setFill(Color.GREEN);

    }

    public void onDeactivated() {
        isOn = false;
        floorSwitch.setFill(Color.RED);
    }

    @Override
    public void onCreated(){
        floorSwitch = new Rectangle(30, 30);
        view.addNode(floorSwitch);
        view.setCentre(new Vec2d(15, 15));

        onDeactivated();
        level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_CREATED));
    }

    @Override
    public void onDestroyed() {
        level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_DESTROYED));
    }

    @Override
    public void onEnterByProp(Prop prop) {
        if (prop.isHeavy())
            onActivated();
    }

    @Override
    public void onLeaveByProp(Prop prop) {
        if (prop.isHeavy())
            onDeactivated();
    }

    @Override
    public void onTurnUpdate() {

        if (!wasOn && isOn)
            level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_ACTIVATED));

        else if (wasOn && !isOn)
            level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_DEACTIVATED));

        wasOn = isOn;
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }
}
