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
//        Circle circle = new Circle(12, Color.LIGHTBLUE);
//        view.addNode(circle);
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

        if (!level.isPassableForProp(target, this))
            return false;
        sprite.setVisible(false);
        SpriteView spriteTransition = new SpriteView(getImage("sprite/prop/boulder/0.png"),new Vec2d(-8,-8), 1.875,1.875);
        Vec2d worldPos = level.gridPosToWorldPosCentre(originalPos).add(new Vec2d(-8, -8));
        spriteTransition.setX(worldPos.getX());
        spriteTransition.setY(worldPos.getY());
        level.getView().getChildren().add(spriteTransition);


        while (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            target = target.add(dir);
            counter++;
        }
        target = target.sub(dir);

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(spriteTransition);

        if (dir.getY() == 0)
            transition.setByX((target.getX()-originalPos.getX())*30);
        if (dir.getX() == 0)
            transition.setByY((target.getY()-originalPos.getY())*30);
        transition.setOnFinished(e -> {
            sprite.setVisible(true);
            spriteTransition.setVisible(false);
        });
        transition.play();

        return true;
    }
}
