//package main.algorithms;
//
//import main.Level;
//import main.math.Tuple;
//import main.math.Vec2i;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.PriorityQueue;
//
//public class AStar2 {
//
//    private Level level;
//    private ArrayList<Vec2i> targets;
//    private Vec2i pos;
//
//    public AStar2(Level level, ArrayList<Vec2i> targets, Vec2i pos) {
//        this.level = level;
//        this.targets = targets;
//        this.pos = pos;
//    }
//
//    // Inner class for A* search
//    private class Node implements Comparable {
//        private Vec2i coordinate;
//        private int pathLength;
//        private int mDist;
//        private Node prev;
//
//        public Node(Vec2i coordinate, int mDist, int pathLength) {
//            this.coordinate = coordinate;
//            this.mDist = mDist;
//            this.pathLength = pathLength;
//        }
//
//        /** @return Previous nodes */
//        public Node getPrev() { return prev; }
//        /** Set previous nodes */
//        public void setPrev(Node prev) { this.prev = prev; }
//
//        /** set the path length */
//        public void setPathLength(int pathLength) { this.pathLength = pathLength; }
//        /** @return Path length */
//        public int getPathLength() { return pathLength; }
//
//        public int getAll() {
//            return pathLength + mDist;
//        }
//
//        /** @return coordinate */
//        public Vec2i getCoordinate() { return coordinate; }
//        /** @return the nodes current heuristic  */
//        public int getHeuristic() { return mDist; }
//
//        /**
//         * @param other the other object
//         * @return if the one is bigger
//         */
//        @Override
//        public int compareTo(Object other) {
//            int thisValue = mDist + pathLength;
//            int otherValue = ((Node)other).getHeuristic(); // TODO
//
//            int v = thisValue - otherValue;
//            return Integer.compare(v,0); // Integer.compare(thisValue, otherValue)
//        }
//
//        /**
//         * @return All adjacent nodes of the current nodes
//         */
//        private ArrayList<Vec2i> getAdjacent(Node request) {
//            ArrayList<Vec2i> ret = new ArrayList<>();
//
//            int coordX = request.getCoordinate().getX();
//            int coordY = request.getCoordinate().getY();
//
//            if (coordX - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX - 1, coordY), null))) {
//                Vec2i buf1 = new Vec2i(coordX - 1, coordY);
//                ret.add(buf1);
//            }
//            if (coordX + 1 < level.getNCols() && (level.isPassableForEnemy(new Vec2i(coordX + 1, coordY), null))) {
//                Vec2i buf1 = new Vec2i(coordX + 1, coordY);
//                ret.add(buf1);
//            }
//            if (coordY - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX, coordY - 1), null))) {
//                Vec2i buf1 = new Vec2i(coordX, coordY - 1);
//                ret.add(buf1);
//            }
//            if (coordY + 1 < level.getNRows() && (level.isPassableForEnemy(new Vec2i(coordX, coordY + 1), null))) {
//                Vec2i buf1 = new Vec2i(coordX, coordY + 1);
//                ret.add(buf1);
//            }
//            return ret;
//        }
//    }
//
//    private ArrayList<Vec2i> getAdjacent(Node request) {
//        ArrayList<Vec2i> ret = new ArrayList<>();
//
//        int coordX = request.getCoordinate().getX();
//        int coordY = request.getCoordinate().getY();
//
//        if (coordX - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX - 1, coordY), null))) {
//            Vec2i buf1 = new Vec2i(coordX - 1, coordY);
//            ret.add(buf1);
//        }
//        if (coordX + 1 < level.getNCols() && (level.isPassableForEnemy(new Vec2i(coordX + 1, coordY), null))) {
//            Vec2i buf1 = new Vec2i(coordX + 1, coordY);
//            ret.add(buf1);
//        }
//        if (coordY - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX, coordY - 1), null))) {
//            Vec2i buf1 = new Vec2i(coordX, coordY - 1);
//            ret.add(buf1);
//        }
//        if (coordY + 1 < level.getNRows() && (level.isPassableForEnemy(new Vec2i(coordX, coordY + 1), null))) {
//            Vec2i buf1 = new Vec2i(coordX, coordY + 1);
//            ret.add(buf1);
//        }
//        return ret;
//    }
//
//
//
//
//
//
//    /**
//     * @param check requested coordinate
//     * @param checkArray Array of recorded nodes
//     * @return If the recorded list has a coordinate/node
//     */
//    private boolean hasCoord(Vec2i check, Collection<Node> checkArray) {
//        // does this coordinate make sense ?
//        for (Node x: checkArray) {
//            if (x.getCoordinate().getX() == check.getX() && x.getCoordinate().getY() == check.getY()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param check Request coordinate to check
//     * @param checkArray Array List to be checked
//     * @return a Node with the requested coordinate
//     */
//    private Node getNode(Vec2i check, Collection<Node> checkArray) {
//        // does this coordinate make sense ?
//        for (Node x: checkArray) {
//            if (x.getCoordinate().getX() == check.getX() && x.getCoordinate().getY() == check.getY()) {
//                return x;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Construct paths from traversed node
//     * @param traversed All nodes
//     * @return The arrayList of coordinates
//     */
//    private ArrayList<Vec2i> constructPath(Node Target, ArrayList<Node> traversed) {
//        ArrayList<Vec2i> ret = new ArrayList<>();
//        ret.add(Target.getCoordinate());
//        Node save = Target.getPrev();
//
//        // Loop until null --> starting node
//        while (save != null) {
//            ret.add(0, save.getCoordinate());
//            save = save.getPrev();
//        }
//
//        return ret;
//    }
//
//    /**
//     * @param curr Current node
//     * @param targets targets
//     * @return The heuristic takes the Mahanttan distance from the closest node
//     */
//    private int manHattanDist(Vec2i curr, ArrayList<Vec2i> targets) {
//
//        // TODO change this, runs everytime, maybe dont
//        // Closest node
//        Vec2i min = null;
//        int minVal = 0;
//        for (Vec2i x: targets) {
//            if (min == null) {
//                min = x;
//                minVal = x.manhattan(curr);
//            } else {
//                if (minVal > x.manhattan(curr)) {
//                    min = x;
//                    minVal = x.manhattan(curr);
//                }
//            }
//        }
//        return minVal;
//    }
//
//
//
//
//
//
//
//    /**
//     * @return The shortest paths for the requested coordinates
//     */
//    public ArrayList<Vec2i> search() {
//        // List of currStack tiles
//        PriorityQueue<Node> frontier = new PriorityQueue<>();
//
//        // List of distance that is stored
//        ArrayList<Node> traversedHeuristic = new ArrayList<>();
//
//        // Set initial cost of enemy and the player and add to traversed currStack list
//        Node start = new Node(pos, manhattanDist(pos, targets), 0);
//        start.setPrev(null);
//
//        frontier.add(start);
//        traversedHeuristic.add(start);
//
//        // Until it runs out of or the algorithm ends
//        while (!frontier.isEmpty()) {
//
//            // Pop the first element in the queue
//            Node curr = frontier.poll();
//
//            if (isGoal(curr.coordinate))
//                return constructPath(curr, traversedHeuristic);
//
//
//            for (Tuple<Vec2i, Integer> adjs : getAdjs(curr.coordinate)) {
//
//                Vec2i x = adjs.getX();
//
//                Node next = new Node(x, manhattanDist(x, targets), adjs.getY() + 1);
//
//
//
//            }
//
//            // For all adjacent nodes, check the following which if unseen node or need to update length
//            for (Vec2i x: adjs) {
//                // If this is not in the currStack nodes, add to it.
//                if (!hasCoord(x, frontier) && !hasCoord(x, traversedHeuristic)) {
//                    frontier.add(curr);
//                    curr.setPrev(buffer);
//                }
//
//                // Update the value
//                if (hasCoord(x,traversedHeuristic)) {
//                    Node buf = getNode(x, traversedHeuristic);
//                    if (buf.getPathLength() > aggregateInt) {
//                        buf.setPathLength(aggregateInt);
//                        curr.setPrev(buffer);
//                    }
//                }
//            }
//            traversedHeuristic.add(buffer);
//        }
//
//        Node max = findMax(traversedHeuristic);
//
//        return constructPath(max, traversedHeuristic);
//    }
//
//    /**
//     * Find the best heuristic
//     * @param heuristicList Resultant list from A* search
//     * @return The best node
//     */
//    private Node findMax(ArrayList<Node> heuristicList) {
//        // If it reaches here then return a path to the nearest node (Max heuristic)
//        Node max = null;
//        for (Node x: heuristicList) {
//            if (max == null) {
//                max = x;
//            } else {
//                if (max.getHeuristic() > x.getHeuristic()) {
//                    max = x;
//                }
//            }
//        }
//        return max;
//    }
//}
