public class Hunter extends Enemy {
    public Hunter(int [] map, int[] userLocation, int[] currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }

}