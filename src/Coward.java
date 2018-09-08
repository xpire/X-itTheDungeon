public class Coward extends Enemy {
    public Coward(int [] map, int[] userLocation, int[] currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }
}
