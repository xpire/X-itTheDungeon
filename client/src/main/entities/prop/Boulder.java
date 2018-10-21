package main.entities.prop;

import main.entities.Avatar;
import main.entities.Entity;
import main.Level;
import main.events.BoulderEvent;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Boulder entity
 */
public class Boulder extends Prop {

    {
        symbol = 'O';
        isHeavy = true;
    }

    /**
     * Basic constructor
     * @param level
     */
    public Boulder(Level level) {
        super(level);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/prop/boulder/2.png"), new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }


    @Override
    public void onExploded() {
        level.postEvent(new BoulderEvent(BoulderEvent.BOULDER_BOMBED));
        destroy();
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean onPush(Avatar avatar) {
        Vec2i target = pos.sub(avatar.getGridPos()).add(pos);

        if (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            soundManager.playSoundEffect("Boulder");
            return true;
        }
        soundManager.playSoundEffect("Hit");
        return false;
    }
}
