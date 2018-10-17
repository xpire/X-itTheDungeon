package main.entities.enemies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Level;
import main.behaviour.AIBehaviour;
import main.behaviour.CowardBehaviour;
import main.behaviour.HunterBehaviour;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * The Hunter enemy entity
 * Always follows the shortest path towards the Avatar
 */
public class Hunter extends Enemy {

    {
        symbol = '1';
        isHunter = true;
    }

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Hunter(Level level) {
        super(level);
    }

    public Hunter(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        super.onCreated();
//        view.addNode(new Circle(10, Color.RED));
        Pane pane = new Pane();
        sprite = new SpriteView(getImage("sprite/enemies/hunter/0.png"),new Vec2d(-6,-8), 1,1);
        pane.getChildren().add(sprite);
        view.addNode(pane);

    }


    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());
        else
            return new HunterBehaviour(level, pos, manager.getAvatarPos());
    }
}