package main.entities.pickup;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2i;

public class InvincibilityPotion extends Pickup{

    private Group pot;
    private int duration;

    {
        symbol = '>';
        duration = 10;
    }

    public InvincibilityPotion(Level level) {
        super(level);
    }

    public InvincibilityPotion(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){

        Circle potBottom  = new Circle(5, Color.TOMATO);
        Rectangle potPipe = new Rectangle(4, 10, Color.TOMATO);
        potPipe.setTranslateX(-2);
        potPipe.setTranslateY(-10);

        pot = new Group();
        pot.getChildren().add(potPipe);
        pot.getChildren().add(potBottom);
        pot.setTranslateY(3);

        view.addNode(pot);
    }


    public void reduceDuration() {
        duration--;
    }

    public boolean hasExpired() {
        return duration <= 0;
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpInvincibilityPotion(this);
    }
}