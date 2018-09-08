package main;

import main.Entities.Entity;
import main.math.Vec2i;

public class Key extends Entity {
    //refactor this to contain the door object, rather than coord
    private Vec2i matchingDoor;

    public Key(String name) {
        super(name);
    }

    //this needs to be enforced
    //depending on the full implementation
    public void setMatchingDoor(Vec2i doorCoord) {
        matchingDoor = doorCoord;
    }

    public Vec2i getMatchingDoor() {
        return matchingDoor;
    }
}
