package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.math.Vec2d;
import main.sprite.SpriteView;


/**
 * Class describing the Pit entity
 */
public class HeatPlate extends Terrain{

    private static final int COLD = 0;
    private static final int GLOW = 1;
    private static final int WARM = 2;
    private static final int BURN = 3;
    private static final int TURNS = 4;

    private Integer state;

    {
        symbol = 'H';
    }

    /**
     * Basic constructor
     * @param level
     */
    public HeatPlate(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        state = COLD;
        sprite = new SpriteView(getImage("sprite/terrain/heatplate/4.png"), new Vec2d(-16,-16), 30.0/32, 30.0/32);
        sprite.addState("0", getImage("sprite/terrain/heatplate/0.png"), new Vec2d(-16,-16), 30.0/32, 30.0/32);
        sprite.addState("1", getImage("sprite/terrain/heatplate/1.png"), new Vec2d(-16,-16), 30.0/32, 30.0/32);
        sprite.addState("2", getImage("sprite/terrain/heatplate/2.png"), new Vec2d(-16,-16), 30.0/32, 30.0/32);
        sprite.addState("3", getImage("sprite/terrain/heatplate/3.png"), new Vec2d(-16,-16), 30.0/32, 30.0/32);
        view.addNode(sprite);
    }

    @Override
    public void onTurnUpdate() {
        state = (state + 1) % TURNS;
        sprite.setState(state.toString());

        if (state == BURN) {
            if (level.hasEnemy(pos)) {
                level.getEnemy(pos).destroy(); //TodO differentiate between destroy and destroy
            }

            if (level.hasAvatar(pos)) {
                level.getAvatar().onThreatenedByBomb(null);
            }

        }
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }

    @Override
    public void onEnterByEnemy(Enemy enemy) {
        if (state == BURN)
            enemy.destroy();
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (state == BURN)
            avatar.onThreatenedByBomb(null);
    }
}