package main.entities.prop;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
<<<<<<< HEAD
import main.entities.Entity;
=======
>>>>>>> Entity package javadoc done
import main.Level;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Flying Arrow entity
 */
public class FlyingArrow extends Prop{

    public Vec2i direction;

    {
        isProjectile = true;
    }

    /**
     * Basic constructor
     * @param level level entity belongs to
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
        sprite.setState(sprite.directionParser(direction));
        Vec2i finishPos = new Vec2i(pos);
        while(level.isValidGridPos(finishPos)) {

            if (level.hasEnemy(finishPos)) {
                level.getEnemy(finishPos).destroy();
                break;
            } else if (!level.isPassableForProp(finishPos, this)) {
                break;
            }

            finishPos = finishPos.add(direction);
        }

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
