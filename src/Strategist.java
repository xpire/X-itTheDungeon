public class Strategist extends Enemy {
    public Strategist(int [] map, int[] userLocation, int[] currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }
}
