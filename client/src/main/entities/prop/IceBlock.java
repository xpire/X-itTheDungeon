package main.entities.prop;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Boulder entity
 */
public class IceBlock extends Prop {

    // Melts when on a heat plate

    {
        symbol = 'I';
        isHeavy = true;
    }

    /**
     * Basic constructor
     * @param level
     */
    public IceBlock(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/prop/boulder/0.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
    }


    @Override
    public void onExploded() {
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
        Vec2i dir = pos.sub(avatar.getGridPos());
        Vec2i target = pos.add(dir);
        Vec2i originalPos = new Vec2i(pos);
        int counter = 0;

        //check if a single push is possible
        if (!level.isPassableForProp(target, this))
            return false;

        //Set current sprite as invisible
        sprite.setVisible(false);

        //Sprite for the transition
        SpriteView spriteTransition = new SpriteView(
                getImage("sprite/prop/boulder/0.png"),
                new Vec2d(-8,-8),
                1.875,
                1.875
        );
        Vec2d worldPos = level.gridPosToWorldPosCentre(originalPos).add(new Vec2d(-8, -8));
        spriteTransition.setX(worldPos.getX());
        spriteTransition.setY(worldPos.getY());
        level.getView().getChildren().add(spriteTransition);

        //Move the actual sprite
        while (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            target = target.add(dir);
            counter++;
        }
        //-1 for over counting
        target = target.sub(dir);

        //Translation sprite
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(1+counter*100));
        transition.setNode(spriteTransition);

        //Calculate translate path
        if (dir.getY() == 0)
            transition.setByX((target.getX()-originalPos.getX())*30);
        if (dir.getX() == 0)
            transition.setByY((target.getY()-originalPos.getY())*30);

        //Reset visibilities, remove sprite for transition
        transition.setOnFinished(e -> {
            sprite.setVisible(true);
            spriteTransition.setVisible(false);
        });

        transition.play();

        return true;
    }
}
