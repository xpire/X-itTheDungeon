package main.entities.pickup;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Hover Potion entity
 */
public class HoverPotion extends Pickup{

    private Group pot;

    {
        symbol = '^';
    }

    /**
     * Basic constructor
     * @param level
     */
    public HoverPotion(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){

//        Circle potBottom  = new Circle(5, Color.LIMEGREEN);
//        Rectangle potPipe = new Rectangle(4, 10, Color.LIMEGREEN);
//        potPipe.setTranslateX(-2);
//        potPipe.setTranslateY(-10);
//
//        pot = new Group();
//        pot.getChildren().add(potPipe);
//        pot.getChildren().add(potBottom);
//        pot.setTranslateY(3);
//
//        view.addNode(pot);
        sprite = new SpriteView(getImage("sprite/pickup/potion/1.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpHoverPotion(this);
    }
}