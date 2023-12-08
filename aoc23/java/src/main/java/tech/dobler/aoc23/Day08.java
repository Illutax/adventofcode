package tech.dobler.aoc23;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08 {
    public long part1(String input) {
        final var desertMap = DesertMap.of(input);
        return desertMap.walk();
    }

    record DesertMap(List<Direction> directions, Graph graph) {
        private static final Pattern PATTERN = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

        public static DesertMap of(String input) {
            final var lines = Util.splitToList(input);
            final var dirs = Arrays.stream(lines.getFirst().split("")).map(Direction::valueOf).toList();
            final var rawNodes = readRawNodes(lines);
            final var graph = Graph.of(rawNodes);
            return new DesertMap(dirs, graph);
        }

        public long walk() {
            final var size = directions.size();
            var current = graph.start;
            int dist = 0;
            int i = 0;
            while (!current.label().equals("ZZZ")) {
                final var direction = directions.get((i % size));
                current = switch (direction) {
                    case L -> current.left();
                    case R -> current.right();
                };
                dist++;
                i++;
            }
            return dist;
        }

        private static Map<String, RawNode> readRawNodes(List<String> lines) {
            final var strings = lines.subList(2, lines.size()).stream().sorted(Comparator.reverseOrder()).toList();
            final var rawNodes = new ArrayList<RawNode>();
            for (var line : strings) {
                final var matcher = PATTERN.matcher(line);
                Util.requirePatternMatches(line, matcher);
                final var label = matcher.group(1);
                final var left = matcher.group(2);
                final var right = matcher.group(3);
                rawNodes.add(RawNode.of(label, left, right));
            }
            return rawNodes.stream()
                    .collect(Collectors.toMap(RawNode::label, Function.identity()));
        }

        private record RawNode(String label, String left, String right, boolean isDeadEnd) {
            public static RawNode of(String label, String left, String right) {
                final var isDeadEnd = label.equals(left) && label.equals(right);
                return new RawNode(label, left, right, isDeadEnd);
            }
        }
    }

    record Graph(Map<String, Node> map, Node start, Node end) {
        private static Graph of(Map<String, DesertMap.RawNode> rawNodes) {
            final Map<String, Node> nodes = HashMap.newHashMap(rawNodes.size());
            for (var node : rawNodes.values()) {
                final var leftChild = nodes.get(node.left());
                final var rightChild = nodes.get(node.right());
                if (node.isDeadEnd) {
                    nodes.put(node.label, Node.deadEnd(node.label));
                }
                nodes.put(node.label, Node.of(node.label, leftChild, rightChild));
            }
            patch(rawNodes, nodes);
            return new Graph(nodes, nodes.get("AAA"), nodes.get("ZZZ"));
        }

        private static void patch(Map<String, DesertMap.RawNode> rawNodes, Map<String, Node> nodes) {
            for (Node node : nodes.values()) {
                final var label = node.label;
                if (node.left() == null) {
                    final var childLabel = rawNodes.get(label).left();
                    node._left.set(nodes.get(childLabel));
                }
                if (node.right() == null) {
                    final var childLabel = rawNodes.get(label).right();
                    node._right.set(nodes.get(childLabel));
                }
            }
        }

        public Path bfs() {
            final var queue = new ArrayDeque<>(map.values());
            start.dist.set(0);
            while (!queue.isEmpty()) {
                final var current = queue.poll();
                if (current == this.end()) {
                    break;
                }
                current.left().update(current);
                current.right().update(current);
            }
            final var path = new ArrayList<Node>();
            var currentNode = this.end();
            path.add(currentNode);
            while (currentNode.prev().get() != null) {
                final var prev = currentNode.prev().get();
                path.add(prev);
                currentNode = prev;
            }
            return new Path(path);
        }
    }

    record Path(List<Node> nodes) {
    }

    enum Direction {
        L, R
    }

    record Node(String label, AtomicReference<Node> _left, AtomicReference<Node> _right, AtomicInteger dist, AtomicReference<Node> prev) {

        public static Node of(String label, Node left, Node right) {
            return new Node(label, new AtomicReference<>(left), new AtomicReference<>(right), new AtomicInteger(Integer.MAX_VALUE), new AtomicReference<>());
        }

        public static Node deadEnd(String label) {
            return Node.of(label, null, null);
        }

        public void update(Node maybeNewPreview) {
            final var prev = this.prev().get();
            if (prev == null || dist().get() > maybeNewPreview.dist().get() + 1) {
                this.prev.set(maybeNewPreview);
                this.dist.set(maybeNewPreview.dist().get() + 1);
            }
        }

        public Node left()
        {
            return this._left.get();
        }

        public Node right()
        {
            return this._right.get();
        }

        @Override
        public String toString() {
            if (isDeadEnd()) {
                return "Leaf{" + label + "}";
            }
            return "Node{" +
                    "label='" + label + '\'' +
                    ", left=" + left().label +
                    ", right=" + right().label +
                    ", dist=" + dist +
                    '}';
        }

        private boolean isDeadEnd() {
            return _left == null && null == _right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return label.equals(node.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    public long part2(String input) {
        return -1;
    }
}