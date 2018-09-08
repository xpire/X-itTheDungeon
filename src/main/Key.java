package main;

import main.core.Entity;

public class Key extends Entity {
    private int[] matchingDoor;

    public Key(String name) {
        super(name);
        this.matchingDoor = new int[2];
    }

    public void setMatchingDoor(int[] doorCoord) {
        matchingDoor = doorCoord;
    }

    public int[] getMatchingDoor() {
        return matchingDoor;
    }
}
