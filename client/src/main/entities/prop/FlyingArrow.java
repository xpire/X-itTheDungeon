package main.entities.prop;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import main.PlayMode;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

/**
 * Class describing the Flying Arrow entity
 */
public class FlyingArrow extends Prop{

    public Vec2i direction;

    {
        isProjectile = true;
//        Vec2i direction;
    }

    /**
     * Basic constructor
     * @param level
     */
    public FlyingArrow(Level level, Vec2i pos, Vec2i dir) {
        super(level);
        this.pos = pos;
        this.direction = dir;
        shoot();
    }

    /**
     * Logic for shooting the arrow, and animation
     */
    private void shoot() {
        // kill first enemy in avatar's direction, if the enemy exists and is reachable
        sprite.setState(sprite.directionParser(direction));
        Vec2i finishPos = new Vec2i(pos);
        while(level.isValidGridPos(finishPos)) {

            // enemy hit
            if (level.hasEnemy(finishPos)) {
                level.getEnemy(finishPos).destroy();
                break;
            }
            // non-passable entity hit
            else if (!level.isPassableForProp(finishPos, this)) {
                break;
            }

            finishPos = finishPos.add(direction);
        }

        //animate the path from pos to finishPos which should be in direction
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(40*pos.manhattan(finishPos)+1));
        transition.setNode(sprite);

        if (direction.getY() == 0)
            transition.setToX(pos.getX() + (finishPos.getX()-pos.getX())*30);
        if (direction.getX() == 0)
            transition.setByY(pos.getY() + (finishPos.getY()-pos.getY())*30);

        transition.setOnFinished(e -> this.destroy());
        transition.play();
    }


    @Override
    public void onCreated() {
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), 1, 1);
        sprite.addState("Left",getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), 1, 1);
        sprite.addState("Right",getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), -1, 1);
        sprite.addState("Up",getImage("sprite/prop/flyingarrow/3.png"), new Vec2d(-3,-8), 1, 1);
        sprite.addState("Down",getImage("sprite/prop/flyingarrow/8.png"), new Vec2d(-3,-8), 1, 1);
        view.addNode(sprite);
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }
}
