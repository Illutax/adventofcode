package tech.dobler.aoc22;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day12 {
    static final class Node {
        private char value;
        private String coord;
        private int cost = Integer.MAX_VALUE;
        private Node parent;
        private final List<Node> neighbours;

        Node(Node parent, List<Node> neighbours) {
            this.parent = parent;
            this.neighbours = neighbours;
        }

        Node() {
            this((Node) null);
        }

        Node(Node parent) {
            this(parent, new LinkedList<>());
        }

        Node(List<Node> neighbours) {
            this(null, neighbours);
        }

        public Node parent() {
            return parent;
        }

        public List<Node> neighbours() {
            return neighbours;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Node) obj;
            return Objects.equals(this.coord, that.coord);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord);
        }

        @Override
        public String toString() {
            return "Node[" +
                    "parent=" + parent + ", " +
                    "value=" + value + ", " +
                    "coord=" + coord + ", " +
                    "cost=" + cost + ", " +
                    "neighbours=" + neighbours.size() + ']';
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public void setCoord(String key) {
            coord = key;
        }

        public char getValue() {
            return value;
        }

        public void setValue(char value) {
            this.value = value;
        }

        public void setParent(Node prev) {
            parent = prev;
        }
    }

    static Node transform(String input) {
        Node startNode = null;
        final var allNodes = HashMap.<String, Node>newHashMap(input.length());
        final var lines = input.split("\n");
        for (int i = 0; i < lines.length; i++) { // y
            String line = lines[i];
            final var chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) { // X
                char c = chars[j];
                Node n = new Node();
                if (c == 'S') {
                    startNode = n;
                }
                if (i > 0) {
                    final var aboveC = lines[i - 1].charAt(j);
                    final var aboveNeighbour = allNodes.get(getKey(j, i - 1));
                    if (canConnect(aboveC, c)) aboveNeighbour.neighbours.add(n);
                    if (canConnect(c, aboveC)) n.neighbours.add(aboveNeighbour);
                }
                if (j > 0) {
                    final var leftC = line.charAt(j - 1);
                    final var leftNeighbour = allNodes.get(getKey(j - 1, i));
                    if (canConnect(leftC, c)) leftNeighbour.neighbours.add(n);
                    if (canConnect(c, leftC)) n.neighbours.add(leftNeighbour);
                }
                final var key = getKey(j, i);
                n.setCoord(key);
                n.setValue(c);
                allNodes.put(key, n);
            }
        }

        return startNode;
    }

    private static String getKey(int x, int y) {
        return x + ":" + y;
    }

    public static char re(char a) {
        final var c = a == 'E'
                ? 'z'
                : a;
        return a == 'S'
                ? 'a'
                : c;
    }

    public static boolean canConnect(char from, char to) {
        final char i = (char) (re(from) + 1);
        final char j = re(to);
        return i >= j;
    }

    class Dijkstra {
        private Queue<Node> discovered = new ArrayDeque<>();
        private Set<Node> processed = new HashSet<>();
        private Node end;

        public Dijkstra(Node start) {
            start.setCost(0);
            processed.add(start);
            discoverAll(start);
            while(discovered.peek() != null) {
                final var current = discovered.poll();
                discoverAll(current);
                if (current.getValue() == 'E') {
                    end = current;
                    break;
                }
            }
        }

        private void discoverAll(Node current) {
            current.neighbours.forEach(n -> {
                final var newCost = current.getCost() + 1;
                if (newCost < n.getCost()) {
                    n.setParent(current);
                    n.setCost(newCost);
                }
            });
            for (Node neighbour : current.neighbours) {
                if (!processed.contains(neighbour) && !discovered.contains(neighbour))
                    discovered.add(neighbour);
            }
            processed.add(current);
        }

        public Node getEnd() {
            return end;
        }
    }

    public int part1(Node startNode) {
        final var dijkstra = new Dijkstra(startNode);
        return dijkstra.getEnd().getCost();
    }

    public int part2(Node input) {
        return -1;
    }
}