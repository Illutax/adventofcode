package tech.dobler.aoc22;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class Day09 {

    enum Direction {
        U, D, L, R;

        static Stream<Direction> from(int dx, int dy) {
            final var directions = new HashSet<Direction>();
            if (dx > 1) directions.add(R);
            if (dx < -1) directions.add(L);
            if (dy > 1) directions.add(D);
            if (dy < -1) directions.add(U);
            if (!directions.isEmpty()) {
                if (dx > 0) directions.add(R);
                if (dx < 0) directions.add(L);
                if (dy > 0) directions.add(D);
                if (dy < 0) directions.add(U);
            }
            return directions.stream();
        }
    }

    record Instruction(Direction direction, int distance) {
        static Instruction from(String line) {
            if (!Pattern.compile("[LURD] [1-9]\\d*").matcher(line).matches())
                throw new IllegalStateException("Line does not match %s".formatted(line));
            final var parts = line.split(" ");
            return new Instruction(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    record Position(int x, int y) {
        public static final Position START = new Position(0, 0);

        Position move(Direction direction) {
            return switch (direction) {
                case L -> new Position(x - 1, y);
                case R -> new Position(x + 1, y);
                case U -> new Position(x, y - 1);
                case D -> new Position(x, y + 1);
            };
        }

        int squareDistance(Position other) {
            final var dx = other.x - x;
            final var dy = other.y - y;
            return dx * dx + dy * dy;
        }
    }

    static class TailPathDrawer {
        private static final boolean DRAW_LABELS = true;
        private static final boolean DRAW_ALL_KNOTS = true;

        private static final boolean DRAW_TAIL = false;
        private static final boolean DRAW_TAIL_DEPTH = true; // req DRAW_TAIL = true
        private static final boolean TAIL_DEPTH_CLAMPED_TO_ONE_DIGIT = true; // req DRAW_TAIL = true

        private final Map<Position, Integer> visitedPositions;
        private final Position head;
        private final Position tail;
        private final Bounds bounds;
        private final List<Position> intermediateKnots;

        static final class Bounds {
            int xMin = MAX_VALUE;
            int xMax = MIN_VALUE;
            int yMin = MAX_VALUE;
            int yMax = MIN_VALUE;

            Bounds(Set<Position> positions) {
                init(positions);
            }

            private void init(Set<Position> positions) {
                for (Position p : positions) {
                    xMin = Math.min(xMin, p.x);
                    xMax = Math.max(xMax, p.x);

                    yMin = Math.min(yMin, p.y);
                    yMax = Math.max(yMax, p.y);
                }
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this) return true;
                if (obj == null || obj.getClass() != this.getClass()) return false;
                var that = (Bounds) obj;
                return this.xMin == that.xMin &&
                        this.xMax == that.xMax &&
                        this.yMin == that.yMin &&
                        this.yMax == that.yMax;
            }

            @Override
            public int hashCode() {
                return Objects.hash(xMin, xMax, yMin, yMax);
            }

            @Override
            public String toString() {
                return "Bounds[" +
                        "xMin=" + xMin + ", " +
                        "xMax=" + xMax + ", " +
                        "yMin=" + yMin + ", " +
                        "yMax=" + yMax + ']';
            }
        }

        public TailPathDrawer(Map<Position, Integer> visitedPositions, List<Position> knots) {
            this.visitedPositions = Collections.unmodifiableMap(visitedPositions);
            this.bounds = new Bounds(visitedPositions.keySet());

            var tailIndex = knots.size() - 1;
            this.head = knots.get(0);
            this.tail = knots.get(tailIndex);
            intermediateKnots = tailIndex == 1
                    ? List.of()
                    : Collections.unmodifiableList(knots.subList(1, tailIndex + 1));
        }

        public String draw() {
            final var sb = new StringBuilder();
            for (int y = bounds.yMin; y <= bounds.yMax; y++) {
                for (int x = bounds.xMin; x <= bounds.xMax; x++) {
                    final var currentPosition = new Position(x, y);
                    if (DRAW_LABELS && currentPosition.equals(head)) sb.append("H");
                    else if (DRAW_LABELS && DRAW_ALL_KNOTS && intermediateKnots.contains(currentPosition))
                        sb.append(intermediateKnots.indexOf(currentPosition) + 1);
                    else if (DRAW_LABELS && currentPosition.equals(Position.START)) sb.append("s");
                    else if (DRAW_LABELS && currentPosition.equals(tail)) sb.append("T");
                    else {
                        final var visits = visitedPositions.getOrDefault(new Position(x, y), 0);
                        sb.append(getCell(visits));
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }

        private static String getCell(Integer visits) {
            if (!DRAW_TAIL) return ".";
            final var pathDepth = TAIL_DEPTH_CLAMPED_TO_ONE_DIGIT ? Math.min(visits, 9) : visits;
            final var path = DRAW_TAIL_DEPTH ? String.valueOf(pathDepth) : "#";
            return visits > 0 ? path : ".";
        }
    }

    public static class Rope {
        final Map<Position, Integer> visitedPositions = new HashMap<>();
        final List<Position> knots;
        final int tailIndex;

        Rope(int ropeLength) {
            knots = IntStream.range(0, ropeLength).mapToObj(ignored -> Position.START).collect(Collectors.toList());
            visitedPositions.put(getTail(), 1);
            tailIndex = ropeLength - 1;
        }

        public Position getHead() {
            return knots.get(0);
        }

        private void updateHead(Direction direction) {
            updateKnot(0, direction);
        }

        public Position getTail() {
            return knots.get(tailIndex);
        }

        private void updateKnot(int index, Direction direction) {
            knots.set(index, knots.get(index).move(direction));
        }

        void move(Instruction instruction) {
            for (int i = 0; i < instruction.distance; i++) {
                updateHead(instruction.direction);
                ghostVisit(getHead());
                adjustKnots();
                visit(getTail());
            }
        }

        private void adjustKnots() {
            for (int prevIndex = 0, knotIndex = 1; knotIndex < knots.size(); prevIndex = knotIndex++) {
                if (knots.get(prevIndex).squareDistance(knots.get(knotIndex)) > 2) {
                    adjustKnot(prevIndex, knotIndex);
                }
            }
        }

        private void adjustKnot(int headIndex, int tailIndex) {
            final var head = knots.get(headIndex);
            final var tail = knots.get(tailIndex);
            final var dx = head.x - tail.x;
            final var dy = head.y - tail.y;
            Direction.from(dx, dy).forEach(direction -> updateKnot(tailIndex, direction));
        }

        private void visit(Position position) {
            visitedPositions.merge(position, 1, Integer::sum);
        }

        private void ghostVisit(Position position) {
            visitedPositions.putIfAbsent(position, 0);
        }

        public int amountOfVisitedPositions() {
            return (int) visitedPositions.values().stream().filter(it -> it > 0).count();
        }

        public Rope move(Stream<Instruction> instructions) {
            instructions.forEach(this::move);
            return this; // For method chaining
        }
    }

    public int part1(Stream<Instruction> instructions) {
        final var rope = new Rope(2).move(instructions);
        Util.print(new TailPathDrawer(rope.visitedPositions, rope.knots).draw());
        return rope.amountOfVisitedPositions();
    }

    public int part2(Stream<Instruction> instructions) {
        final var rope = new Rope(10).move(instructions);
        Util.print(new TailPathDrawer(rope.visitedPositions, rope.knots).draw());
        return rope.amountOfVisitedPositions();
    }
}