package main.entities.pickup;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.Level;
import main.entities.Avatar;
import main.math.Vec2i;

/**
 * Class describing the Hover Potion entity
 */
public class BombPotion extends Pickup{

    private Group pot;

    {
        symbol = '@';
    }

    /**
     * Basic constructor
     * @param level
     */
    public BombPotion(Level level) {
        super(level);
    }

    public BombPotion(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated(){

        Circle potBottom  = new Circle(5, Color.FIREBRICK);
        Rectangle potPipe = new Rectangle(4, 10, Color.FIREBRICK);
        potPipe.setTranslateX(-2);
        potPipe.setTranslateY(-10);

        pot = new Group();
        pot.getChildren().add(potPipe);
        pot.getChildren().add(potBottom);
        pot.setTranslateY(3);

        view.addNode(pot);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {

        if (avatar.pickUpBombPotion(this)) {
            return true;
        }
        return false;
    }
}