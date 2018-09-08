import Behavior.AIBehavior;
import java.util.ArrayList;

public abstract class Enemy {
    private int [] map;
    private int[] playerLocation;
    private int[] currLocation;
    private ArrayList<Integer> pastMoves;

    private AIBehavior currBehavior;

    public void setMap(int [] map) { this.map = map; }
    public void setPlayerLocation(int[] playerLocation) { this.playerLocation = playerLocation; }
    public void setCurrLocation(int[] CurrLocation) { this.currLocation = CurrLocation; }
    public void givePastMove(int[] pastMoves) { this.currLocation = pastMoves; }
    public void setCurrBehavior(AIBehavior currBehavior) { this.currBehavior = currBehavior; }
    public int[] update() {
        return currBehavior.decideMove(this.map,this.currLocation,this.playerLocation, this.pastMoves);
    }

    /**
     * Finding the shortest path to a specific square
     * @param targets target square that the AI wants to go to
     * @return an Arraylist of the path to the square
     */
    public ArrayList<Integer> shortestPath(ArrayList<int[]> targets, int [] map) {
        ArrayList<Integer> ret = new ArrayList<>();

        return ret;
    }

    public static void main(String [] args) {
        // this is a new function

    }
}
