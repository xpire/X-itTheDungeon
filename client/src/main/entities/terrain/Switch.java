package main.entities.terrain;

import main.Level;
import main.entities.Entity;
import main.entities.prop.Prop;
import main.events.SwitchEvent;
import main.math.Vec2d;
import main.sound.SoundManager;
import main.sprite.SpriteView;

/**
 * Class describing the Switch entity
 */
public class Switch extends Terrain{

    private boolean wasOn;
    private boolean isOn;

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
        sprite.setState("Pressed");
        soundManager.playSoundEffect("Click");

    }

    /**
     * Flag if the switch is deactivated
     */
    public void onDeactivated() {
        isOn = false;
        sprite.setState("Not Pressed");
        soundManager.playSoundEffect("Click");
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/terrain/switch/notpressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        sprite.addState("Not Pressed", getImage("sprite/terrain/switch/notpressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        sprite.addState("Pressed", getImage("sprite/terrain/switch/pressed2.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
        onDeactivated();
        soundManager = SoundManager.getInstance(5);
        level.postEvent(new SwitchEvent(SwitchEvent.SWITCH_CREATED));
    }

    @Override
    public void destroy() {
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
