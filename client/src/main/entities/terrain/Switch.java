package main.entities.terrain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Entity;
import main.entities.prop.Prop;
import main.events.SwitchEvent;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Switch entity
 */
public class Switch extends Terrain{

    private boolean wasOn;
    private boolean isOn;
    private Rectangle floorSwitch;

    {
        symbol  = '/';
        wasOn   = false;
        isOn    = false;
    }

    /**
     * basic constructor
     * @param level current level
     */
    public Switch(Level level) {
        super(level);
    }

    /**
     * Flag if the switch is activated
     */
    public void onActivated() {
        isOn = true;
//        floorSwitch.setFill(Color.GREEN);
        sprite.setState("Pressed");

    }

    /**
     * Flag if the switch is deactivated
     */
    public void onDeactivated() {
        isOn = false;
//        floorSwitch.setFill(Color.RED);
        sprite.setState("Not Pressed");
    }

    @Override
    public void onCreated(){
//        floorSwitch = new Rectangle(30, 30);
//        view.addNode(floorSwitch);
//        view.setCentre(new Vec2d(15, 15));
        sprite = new SpriteView(getImage("sprite/terrain/switch/notpressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        sprite.addState("Not Pressed", getImage("sprite/terrain/switch/notpressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        sprite.addState("Pressed", getImage("sprite/terrain/switch/pressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
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
