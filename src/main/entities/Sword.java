package main.entities;

public class Sword extends Entity{

    private int durability;

    public Sword() {
        super("Sword");
        this.symbol = '+';
        this.durability = 5;
    }

    public int getDurability() {
        return durability;
    }

    public void reduceDurability() {
        durability--;
        if (durability == 0) {
            System.out.println("Sword has broken");
        }
    }

}
