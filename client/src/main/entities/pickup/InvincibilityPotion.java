package main.entities.pickup;

import javafx.scene.Group;
import main.Level;
import main.entities.Avatar;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Invincibility Potion Enitity
 */
public class InvincibilityPotion extends Pickup{

    private int duration;

    {
        symbol = '>';
        duration = 10;
    }

    /**
     * Basic constructor
     * @param level : level of the invincibility potion belongs to
     */
    public InvincibilityPotion(Level level) {
        super(level);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/potion/0.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }


    /**
     * reduce the potions remaining duration
     */
    public void reduceDuration() {
        duration--;
    }

    /**
     * check if the potion has expired
     * @return true if potion has expired
     */
    public boolean hasExpired() {
        return duration <= 0;
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpInvincibilityPotion(this);
    }
}