package main.algorithms;

import main.math.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;



public class AStar<T> {

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
        return constructPath(min, preds);
    }


    private Node findMin(Collection<Node> nodes) {
        return nodes.stream().min(Node::compareTo).orElse(null);
    }


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

    // lower priority value corresponds to higher priority in the queue
    private class Node implements Comparable<Node>{

        private T vertex;
        private int priority;

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


/*

package main.algorithms;

        import main.math.Tuple;

        import java.util.*;
        import java.util.function.Function;
        import java.util.function.Predicate;



public class AStar<T> {

    private T start;
    private Predicate<T> isGoal;
    private Function<T, List<Tuple<T,Integer>>> expand;
    private Function<T, Integer> heuristic;

    public AStar(T start,
                 Predicate<T> isGoal,
                 Function<T, List<Tuple<T, Integer>>> expand,
                 Function<T, Integer> heuristic) {

        this.start = start;
        this.isGoal = isGoal;
        this.expand = expand;
        this.heuristic = heuristic;
    }

    public List<T> search() {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(new Node(start, heuristic.apply(start)));

        HashMap<T, Node> preds = new HashMap<>();
        HashMap<T, Integer> dists = new HashMap<>();

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
        return constructPath(min, preds);
    }


    private Node findMin(Collection<Node> nodes) {
        return nodes.stream().min(Node::compareTo).orElse(null);
    }


    private List<T> constructPath(Node target, HashMap<T, Node> preds) {
        LinkedList<T> res = new LinkedList<>();

        res.add(target.vertex);

        Node pred = preds.get(target.vertex);
        while (pred != null) {
            res.addFirst(pred.vertex);
            pred = preds.get(pred.vertex);
        }

        return res;
    }

    // lower priority value corresponds to higher priority in the queue
    private class Node implements Comparable<Node>{

        private T vertex;
        private int priority;

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
 */