package main.entities;

public class Exit extends Entity {

    public Exit() {
        super("Exit");
        this.symbol = 'X';
    }


    public boolean isPassableFor(Entity other) {
        if (other.getName().equals("Avatar")) return true;
        return false;
    }
}