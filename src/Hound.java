public class Hound extends Enemy {
    public Hound(int [] map, int[] userLocation, int[] currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }
}
