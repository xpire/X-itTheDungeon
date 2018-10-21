package main.algorithms;

import main.math.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of an A-Star search which enemies use to find get the optimal path towards
 * the Avatar.
 * @param <T> : Data type of a state in AStar search
 */

public class AStar<T> {

    /**
     * Searches the map for the best sequence of states (path) from a beginning location
     * to the desired location
     * @param start Starting state
     * @param isGoal final state
     * @param expand function to generate the next set of states
     * @param heuristic heuristic used by AStar search
     * @return The sequence of states to reach the target specified
     */
    public List<T> search(
            T start,
            Predicate<T> isGoal,
            Function<T, List<Tuple<T, Integer>>> expand,
            Function<T, Integer> heuristic) {


        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(new Node(start, heuristic.apply(start)));

        HashMap<T, Node> preds = new HashMap<>();
        HashMap<T, Integer> dists = new HashMap<>();
        dists.put(start, 0);

        Set<T> expanded = new HashSet<>();

        while (!frontier.isEmpty()) {
            Node curr = frontier.poll();

            if (isGoal.test(curr.vertex))
                return constructPath(curr, preds);

            expanded.add(curr.vertex);

            for (Tuple<T, Integer> adj : expand.apply(curr.vertex)) {
                T vertex = adj.getX();
                int dist = adj.getY();

                if (expanded.contains(vertex))
                    continue;

                int oldDist = dists.getOrDefault(vertex, Integer.MAX_VALUE);
                int newDist = dists.get(curr.vertex) + dist;

                if (newDist < oldDist) {
                    dists.put(vertex, newDist);
                    preds.put(vertex, curr);
                    frontier.add(new Node(vertex, newDist + heuristic.apply(vertex)));
                }
            }
        }

        Node min = findMin(preds.values());
        if (min == null)
            return Collections.singletonList(start);
        return constructPath(min, preds);
    }

    /**
     * Finds the minimum priority value node (highest priority)
     * @param nodes : Collection of nodes to search through
     * @return the node with minimum priority value
     */
    private Node findMin(Collection<Node> nodes) {
        return nodes.stream().min(Node::compareTo).orElse(null);
    }


    /**
     * Builds the sequence of states to the target
     * @param target the target node
     * @param preds map of predecessor nodes
     * @return the sequence of states to the target
     */
    private List<T> constructPath(Node target, HashMap<T, Node> preds) {
        LinkedList<T> res = new LinkedList<>();

        res.add(target.vertex);

        Node pred = preds.getOrDefault(target.vertex, null);
        while (pred != null) {
            res.addFirst(pred.vertex);
            pred = preds.get(pred.vertex);
        }

        return res;
    }


    /**
     * A node within the search tree
     */
    private class Node implements Comparable<Node>{

        private T vertex;
        private int priority;

        /**
         * Basic constructor for a node
         * @param vertex state of the node
         * @param priority nodes priority
         */
        private Node(T vertex, int priority) {
            this.vertex = vertex;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node o) {
            return this.priority - o.priority; // TODO check order
        }
    }
}