package main.entities.enemies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Level;
import main.behaviour.AIBehaviour;
import main.behaviour.CowardBehaviour;
import main.behaviour.HoundBehaviour;
import main.behaviour.HunterBehaviour;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * The Hound enemy entity
 * Tries to trap the Avatar between it and the Hunter nearest to
 * the Avatar. Becomes a Hunter if no Hunters exist.
 */
public class Hound extends Enemy { //TODO remove test codes

    {
        symbol = '3';
        isHunter = false;
    }

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Hound(Level level) {
        super(level);
    }

    public Hound(Level map, Vec2i pos) {
        super(map, pos);
    }

    @Override
    public void onCreated(){
        super.onCreated();
//        view.addNode(new Circle(10, Color.YELLOW));
        Pane pane = new Pane();
        sprite = new SpriteView(getImage("sprite/enemies/hound/0.png"),new Vec2d(-6,-8), 1,1);
        pane.getChildren().add(sprite);
        view.addNode(pane);
    }

    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());

        if (manager.checkHunterExists())
            return new HoundBehaviour(level, pos, manager.getAvatarPos());
        else
            return new HunterBehaviour(level, pos, manager.getAvatarPos());
    }
}