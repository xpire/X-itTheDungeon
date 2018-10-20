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
    EventHandler<ActionEvent> afterFinish =  e -> {
    };

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

    private void shoot() {
        // kill first enemy in avatar's direction, if the enemy exists and is reachable
        sprite.setState(sprite.directionParser(direction));
        Vec2i finishPos = new Vec2i(pos);
        while(level.isValidGridPos(finishPos)) {

            // enemy hit
            if (level.hasEnemy(finishPos)) {
                level.getEnemy(finishPos).onDestroyed();
                break;
            }
            // non-passable entity hit
            else if (!level.isPassableForProp(finishPos, this)) {
                break;
            }

            finishPos = finishPos.add(direction);
        }

        final Vec2i fPos = new Vec2i(finishPos);
        //animate the path from pos to finishPos which should be in directionn
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(40*pos.manhattan(finishPos)+1));
        transition.setNode(sprite);
        if (direction.getY() == 0)
            transition.setToX(pos.getX() + (finishPos.getX()-pos.getX())*30);
        if (direction.getX() == 0)
            transition.setByY(pos.getY() + (finishPos.getY()-pos.getY())*30);
        transition.setOnFinished(e -> {
//            if (level.hasEnemy(fPos))
//                level.getEnemy(fPos).onDestroyed();
            this.onDestroyed();
        });
        transition.play();
    }


    @Override
    public void onCreated() {
        sprite = new SpriteView(getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), 1, 1);
        sprite.addState("Left",getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), 1, 1);
        sprite.addState("Right",getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), -1, 1);
        sprite.addState("Up",getImage("sprite/prop/flyingarrow/3.png"), new Vec2d(-3,-8), 1, 1);
        sprite.addState("Down",getImage("sprite/prop/flyingarrow/8.png"), new Vec2d(-3,-8), 1, 1);
        SpriteAnimation left = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-8,-3),1,1);
        left.addState(getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3));
        sprite.addAnime("Left", left);
        SpriteAnimation right = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-8,-3),-1,1);
        right.addState(getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3));
        sprite.addAnime("Right", right);
        SpriteAnimation up = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-3,-8),1,1);
        up.addState(getImage("sprite/prop/flyingarrow/3.png"), new Vec2d(-3,-8));
        sprite.addAnime("Up", up);
        SpriteAnimation down = new SpriteAnimation(sprite, new Duration(500), new Vec2i(-3,-8),1,1);
        down.addState(getImage("sprite/prop/flyingarrow/8.png"), new Vec2d(-3,-8));
        sprite.addAnime("Down", down);

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
