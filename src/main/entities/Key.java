package main.entities;

public class Key extends Entity {
    private Door matchingDoor;

    public Key(String name) {
        super(name);
        this.symbol = 'K';
    }

    public void setMatchingDoor(Door d) {
        matchingDoor = d;
    }

    public Door getMatchingDoor() {
        return matchingDoor;
    }
}
