package main.entities;

public class InvincibilityPot extends Entity {

    private int duration;

    public InvincibilityPot() {
        super("Invincibility Pot");
        this.symbol = '>';
        this.duration = 5;
    }

    public void reduceDuration() {
        this.duration--;
    }

    public int getDuration() {
        return this.duration;
    }
}