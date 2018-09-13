package main.entities;

public class Sword extends Entity{

<<<<<<< e485b8277eef48c86962f8de887197607baddf32
    private int durability;

=======
>>>>>>> made key-door mapping consisten in txt, fixed entity constructers which didnt merge correctly
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
