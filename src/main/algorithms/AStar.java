package main.algorithms;

import main.math.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;



public class AStar {

    public <T> List<T> search(
            T start,
            Predicate<T> isGoal,
            Function<T, List<Tuple<T, Integer>>> expand,
            Function<T, Integer> heuristic) {


        PriorityQueue<Node<T>> frontier = new PriorityQueue<>();
        frontier.add(new Node<>(start, heuristic.apply(start)));

        HashMap<T, Node<T>> preds = new HashMap<>();
        HashMap<T, Integer> dists = new HashMap<>();

        Set<T> expanded = new HashSet<>();


        while (!frontier.isEmpty()) {
            Node<T> curr = frontier.poll();

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
                    frontier.add(new Node<>(vertex, newDist + heuristic.apply(vertex)));
                }
            }
        }


        Node<T> max = findMin(preds.values());
        return constructPath(max, preds);
    }


    public <T> Node<T> findMin(Collection<Node<T>> nodes) {
        return nodes.stream().min(Node::compareTo).orElse(null);
    }


    private <T> List<T> constructPath(Node<T> target, HashMap<T, Node<T>> preds) {
        LinkedList<T> res = new LinkedList<>();

        res.add(target.vertex);

        Node<T> pred = preds.get(target.vertex);
        while (pred != null) {
            res.addFirst(pred.vertex);
            pred = preds.get(pred.vertex);
        }

        return res;
    }


    private class Node<T> implements Comparable<Node<T>>{

        private T vertex;
        private int priority;

        public Node(T vertex, int priority) {
            this.vertex = vertex;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node o) {
            return o.priority - this.priority; // TODO check order
        }
    }
}
