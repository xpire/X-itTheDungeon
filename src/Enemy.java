import Behavior.AIBehavior;
import java.util.ArrayList;
import java.lang.Math;
import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class Enemy {
    private int length;
    private int width;

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
    public void setDimensions(int length, int width) { this.length = length; this.width = width; }

    // Inner class for A* search
    private class Node implements Comparable {
        private int[] coordinate;
        private int heuristic;
        private int[] prev;

        public Node(int[] coordinate, int heuristic) {
            this.coordinate = coordinate;
            this.heuristic = heuristic;
        }

        /** @return previous nodes */
        public int[] getPrev() { return prev; }
        /** set previous nodes */
        public void setPrev(int[] prev) { this.prev = prev; }

        /** @return coordinate */
        public int[] getCoordinate() { return coordinate; }
        /** @return the nodes current heuristic  */
        public int getHeuristic() { return heuristic; }

        /**
         * @param other the other object
         * @return if the one is bigger
         */
        @Override
        public int compareTo(Object other) {
            int thisValue = this.heuristic;
            int otherValue = ((Node)other).getHeuristic();

            int v = thisValue - otherValue;
            //return ((v > 0)? 1: (v < 0)? -1: 0);
            return Integer.compare(v,0);
        }

        /**
         * @return All adjacent nodes of the current nodes
         */
        public ArrayList<Node> getAjdacent() {
            for (int i = 0; )
        }
    }

    /**
     * Construct paths from traversed node
     * @param traversed All nodes
     * @return The arrayList of coordinates
     */
    private ArrayList<int[]> constructPath(ArrayList<Node> traversed) {
        return null;
    }

    /**
     * Finding the shortest path to a specific square using A*
     * @param targets target square that the AI wants to go to
     * @return an ArrayList of the path to the square
     */
    public ArrayList<int[]> shortestPath(ArrayList<int[]> targets) {
        // List of return
        ArrayList<Integer> ret = new ArrayList<>();
        // List of visited tiles
        ArrayList<Node> visited = new ArrayList<>();
        // List of traversed nodes
        PriorityQueue<Node> traversedHeurisic = new PriorityQueue<Node>();

        // Set initial cost of enemy and the player and add to traversed visited list
        Node start_node = new Node(this.currLocation,manhattenDist(currLocation,playerLocation));
        start_node.setPrev(null);
        visited.add(start_node);

        // Until it runs out of or the algorithm ends
        while (!traversedHeurisic.isEmpty()) {
            // Pop the first element in the queque
            Node buffer = (Node)traversedHeurisic.poll();
            for(int[] x: targets) {
                // TODO: note that any location is find first will be the goal position...
                if(x[0] == buffer.getCoordinate()[0] && x[1] == buffer.getCoordinate()[1])
                    return constructPath(visited);
            }

            // Get all the possible reachable nodes from the current popped node
            ArrayList<Node> adjs = buffer.getAjdacent();
        }
        return null;
    }

    /**
     * @param curr Current node
     * @param result Wanted result
     * @return The heuristic
     */
    private int manhattenDist(int[] curr, int[] result) {
        return Math.abs(curr[0] - result[1]) + Math.abs(curr[1] - result[1]);
    }

    public static void main(String [] args) {
        // This is a new function

    }
}
