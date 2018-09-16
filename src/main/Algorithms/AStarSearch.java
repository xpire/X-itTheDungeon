package main.Algorithms;

import main.entities.enemies.Enemy;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

public class AStarSearch {
    private Level level;
    private ArrayList<Vec2i> targets;
    private Vec2i pos;

    public AStarSearch(Level level, ArrayList<Vec2i> targets, Vec2i pos) {
        this.level = level;
        this.targets = targets;
        this.pos = pos;
    }

    // Inner class for A* search
    private class Node implements Comparable {
        private Vec2i coordinate;
        private int pathLength;
        private int mDist;
        private Node prev;

        public Node(Vec2i coordinate, int mDist, int pathLength) {
            this.coordinate = coordinate;
            this.mDist = mDist;
            this.pathLength = pathLength;
        }

        /** @return Previous nodes */
        public Node getPrev() { return prev; }
        /** Set previous nodes */
        public void setPrev(Node prev) { this.prev = prev; }

        /** set the path length */
        public void setPathLength(int pathLength) { this.pathLength = pathLength; }
        /** @return Path length */
        public int getPathLength() { return pathLength; }

        public int getAll() {
            return pathLength + mDist;
        }

        /** @return coordinate */
        public Vec2i getCoordinate() { return coordinate; }
        /** @return the nodes current heuristic  */
        public int getHeuristic() { return mDist; }

        /**
         * @param other the other object
         * @return if the one is bigger
         */
        @Override
        public int compareTo(Object other) {
            int thisValue = this.mDist + this.pathLength;
            int otherValue = ((Node)other).getHeuristic();

            int v = thisValue - otherValue;
            //return ((v > 0)? 1: (v < 0)? -1: 0);
            return Integer.compare(v,0);
        }

        /**
         * @return All adjacent nodes of the current nodes
         */
        private ArrayList<Vec2i> getAdjacent(Node request, Level map ) {
            ArrayList<Vec2i> ret = new ArrayList<>();
            int coordX = request.getCoordinate().getX();
            int coordY = request.getCoordinate().getY();

            if (coordX - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX - 1, coordY), null))) {
                Vec2i buf1 = new Vec2i(coordX - 1, coordY);
                ret.add(buf1);
            }
            if (coordX + 1 < level.getNCols() && (level.isPassableForEnemy(new Vec2i(coordX + 1, coordY), null))) {
                Vec2i buf1 = new Vec2i(coordX + 1, coordY);
                ret.add(buf1);
            }
            if (coordY - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX, coordY - 1), null))) {
                Vec2i buf1 = new Vec2i(coordX, coordY - 1);
                ret.add(buf1);
            }
            if (coordY + 1 < level.getNRows() && (level.isPassableForEnemy(new Vec2i(coordX, coordY + 1), null))) {
                Vec2i buf1 = new Vec2i(coordX, coordY + 1);
                ret.add(buf1);
            }
            return ret;
        }
    }

    /**
     * @param check requested coordinate
     * @param checkArray Array of recorded nodes
     * @return If the recorded list has a coordinate/node
     */
    private boolean hasCoord(Vec2i check, Collection<Node> checkArray) {
        // does this coordinate make sense ?
        for (Node x: checkArray) {
            if (x.getCoordinate().getX() == check.getX() && x.getCoordinate().getY() == check.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param check Request coordinate to check
     * @param checkArray Array List to be checked
     * @return a Node with the requested coordinate
     */
    private Node getNode(Vec2i check, Collection<Node> checkArray) {
        // does this coordinate make sense ?
        for (Node x: checkArray) {
            if (x.getCoordinate().getX() == check.getX() && x.getCoordinate().getY() == check.getY()) {
                return x;
            }
        }
        return null;
    }

    /**
     * Construct paths from traversed node
     * @param traversed All nodes
     * @return The arrayList of coordinates
     */
    private ArrayList<Vec2i> constructPath(Node Target, ArrayList<Node> traversed) {
        ArrayList<Vec2i> ret = new ArrayList<>();
        ret.add(Target.getCoordinate());
        Node save = Target.getPrev();

        // Loop until null --> starting node
        while (save != null) {
            ret.add(0, save.getCoordinate());
            save = save.getPrev();
        }

        return ret;
    }

    /**
     * @param curr Current node
     * @param targets targets
     * @return The heuristic takes the Mahanttan distance from the closest node
     */
    private int manHattanDist(Vec2i curr, ArrayList<Vec2i> targets) {

        // TODO change this, runs everytime, maybe dont
        // Closest node
        Vec2i min = null;
        int minVal = 0;
        for (Vec2i x: targets) {
            if (min == null) {
                min = x;
                minVal = x.mDist(curr);
            } else {
                if (minVal > x.mDist(curr)) {
                    min = x;
                    minVal = x.mDist(curr);
                }
            }
        }
        return minVal;
    }

    /**
     * @return The shortest paths for the requested coordinates
     */
    public ArrayList<Vec2i> Search() {
        // List of currStack tiles
        PriorityQueue<Node> currStack = new PriorityQueue<>();
        // List of distance that is stored
        ArrayList<Node> traversedHeuristic = new ArrayList<>();

        // Set initial cost of enemy and the player and add to traversed currStack list
        Node start_node = new Node(pos, manHattanDist(pos,targets), 0);
        start_node.setPrev(null);
        currStack.add(start_node);
        traversedHeuristic.add(start_node);

        // Until it runs out of or the algorithm ends
        while (currStack.size() > 0) {
            // Pop the first element in the queue
            Node buffer = currStack.poll();
            for(Vec2i x: targets) {
                if (x.getX() == buffer.getCoordinate().getX() && x.getY() == buffer.getCoordinate().getY())
                    return constructPath(buffer, traversedHeuristic);
            }

            // Get all the possible reachable coordinate from here
            ArrayList<Vec2i> adjs = buffer.getAdjacent(buffer, level);
            int aggregateInt = buffer.getPathLength() + 1;

            // For all adjacent nodes, check the following which if unseen node or need to update length
            for (Vec2i x: adjs) {
                // Make new node
                Node curr = new Node(x, manHattanDist(x, targets), aggregateInt);
                // if (!hasCoord(x,currStack) && !hasCoord(x,traversedHeuristic)) { curr.setPrev(buffer); }

                // If this is not in the currStack nodes, add to it.
                if (!hasCoord(x, currStack) && !hasCoord(x, traversedHeuristic)) {
                    currStack.add(curr);
                    curr.setPrev(buffer);
                }

                // Update the value
                if (hasCoord(x,traversedHeuristic)) {
                    Node buf = getNode(x, traversedHeuristic);
                    if (buf.getPathLength() > aggregateInt) {
                        buf.setPathLength(aggregateInt);
                        curr.setPrev(buffer);
                    }
                }
            }
            traversedHeuristic.add(buffer);
        }

        Node max = findMax(traversedHeuristic);

        return constructPath(max, traversedHeuristic);
    }

    /**
     * Find the best heuristic
     * @param heuristicList Resultant list from A* search
     * @return The best node
     */
    private Node findMax(ArrayList<Node> heuristicList) {
        // If it reaches here then return a path to the nearest node (Max heuristic)
        Node max = null;
        for (Node x: heuristicList) {
            if (max == null) {
                max = x;
            } else {
                if (max.getHeuristic() > x.getHeuristic()) {
                    max = x;
                }
            }
        }
        return max;
    }
}
