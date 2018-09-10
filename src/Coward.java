import main.math.Vec2i;

public class Coward extends Enemy {
    public Coward(int [] [] map, Vec2i userLocation, Vec2i currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }
}
