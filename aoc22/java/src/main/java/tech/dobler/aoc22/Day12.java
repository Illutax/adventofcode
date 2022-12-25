package tech.dobler.aoc22;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

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
            this(null);
        }

        Node(Node parent) {
            this(parent, new LinkedList<>());
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

        public String getCoord() {
            return coord;
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

    static Node transform(String input) { // NOSONAR (it's alright)
        Node startNode = null;
        final var allNodes = HashMap.<String, Node>newHashMap(input.length());
        final var lines = input.split("\n");
        for (int i = 0; i < lines.length; i++) { // y
            String line = lines[i];
            final var chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) { // X
                char c = chars[j];
                Node n = new Node();
                if (c == 'E') {
                    startNode = n;
                }
                if (i > 0) {
                    final var aboveC = lines[i - 1].charAt(j);
                    final var aboveNeighbour = allNodes.get(getKey(j, i - 1));
                    if (canConnectReverse(aboveC, c)) aboveNeighbour.neighbours.add(n);
                    if (canConnectReverse(c, aboveC)) n.neighbours.add(aboveNeighbour);
                }
                if (j > 0) {
                    final var leftC = line.charAt(j - 1);
                    final var leftNeighbour = allNodes.get(getKey(j - 1, i));
                    if (canConnectReverse(leftC, c)) leftNeighbour.neighbours.add(n);
                    if (canConnectReverse(c, leftC)) n.neighbours.add(leftNeighbour);
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
        return (char) (re(from) + 1) >= re(to);
    }

    public static boolean canConnectReverse(char from, char to) {
        return canConnect(to, from); // NOSONAR that is intended... duh!
    }

    static class Dijkstra {
        private final Queue<Node> discovered = new ArrayDeque<>();
        private final Set<Node> processed = new HashSet<>();
        private Node start;

        public Dijkstra(Node end) {
            end.setCost(0);
            processed.add(end);
            discoverAll(end);
        }

        public Dijkstra part1() {
            while (discovered.peek() != null) {
                final var current = discovered.poll();
                discoverAll(current);
                if (current.getValue() == 'S') {
                    this.start = current;
                    break;
                }
            }
            return this;
        }

        public Dijkstra part2() {
            List<Node> starts = new LinkedList<>();
            while (discovered.peek() != null) {
                final var current = discovered.poll();
                discoverAll(current);
                if (current.getValue() == 'S' || current.getValue() == 'a') {
                    starts.add(current);
                }
            }
            start = starts.stream()
                    .min(Comparator.comparing(Node::getCost))
                    .orElseThrow();
            return this;
        }

        private void discoverAll(Node current) {
            current.neighbours().forEach(n -> {
                final var newCost = current.getCost() + 1;
                if (newCost < n.getCost()) {
                    n.setParent(current);
                    n.setCost(newCost);
                }
            });
            for (Node neighbour : current.neighbours()) {
                if (!processed.contains(neighbour) && !discovered.contains(neighbour))
                    discovered.add(neighbour);
            }
            processed.add(current);
        }

        public Dijkstra draw(Consumer<String> printer) {
            Map<String, Character> grid = new HashMap<>();
            for (var node = start; node.parent() != null; node = node.parent()) {
                grid.put(node.getCoord(), getDisplayDirection(node));
                for (Node neighbour : node.neighbours()) {
                    grid.put(neighbour.getCoord(), getDisplayDirection(neighbour));
                }
            }

            final var maxDims = new ArrayList<>(List.of(0, 0));
            grid.keySet().stream()
                    .map(s -> Arrays.stream(s.split(":")).map(Integer::parseInt).toList())
                    .forEach(e -> {
                        if (e.get(0) > maxDims.get(0)) maxDims.set(0, e.get(0));
                        if (e.get(1) > maxDims.get(1)) maxDims.set(1, e.get(1));
                    });

            var sb = new StringBuilder();
            for (int y = 0; y <= maxDims.get(1); y++) {
                for (int x = 0; x <= maxDims.get(0); x++) {
                    final var key = x + ":" + y;
                    if (grid.containsKey(key))
                        sb.append(grid.get(key));
                    else
                        sb.append('.');
                }
                sb.append("\n");
            }
            printer.accept(sb.toString());
            return this;
        }

        private Character getDisplayDirection(Node node) {
            final var coord = node.getCoord();
            final var maybeParent = Optional.ofNullable(node.parent)
                    .map(Node::getCoord);
            if (maybeParent.isEmpty()) return 'E';
            final var parent = maybeParent.get();
            final var coords = coord.split(":");
            final var x = Integer.parseInt(coords[0]);
            final var y = Integer.parseInt(coords[1]);
            final var parentCoords = parent.split(":");
            final var dx = Integer.parseInt(parentCoords[0]);
            final var dy = Integer.parseInt(parentCoords[1]);
            if (y < dy) return 'v';
            if (y > dy) return '^';
            if (x < dx) return '>';
            return '<';
        }

        public int getLength() {
            return start.getCost();
        }
    }

    public int part1(Node startNode) {
        return new Dijkstra(startNode)
                .part1()
                .draw(Util::print)
                .getLength();
    }

    public int part2(Node startNode) {
        return new Dijkstra(startNode)
                .part2()
                .draw(Util::print)
                .getLength();
    }
}