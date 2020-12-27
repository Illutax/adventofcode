import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum Direction {
    EAST(1, 0),
    SOUTH_EAST(0, 1), SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(0, -1), NORTH_EAST(1, -1);

    private final Point _dir;

    Direction(int x, int y) {
        _dir = new Point(x, y);
    }

    public Point getDirection() {
        return new Point(_dir);
    }
}

@Slf4j public class Day24 {
    private static final boolean BLACK_SIDE_UP = true;
    private static final boolean WHITE_SIDE_UP = false;

    private static long countBlackTiles(Map<Point, Boolean> tiles) {
        return tiles.values().stream()
                        .filter(b -> b)
                        .count();
    }

    public static long countAdjestBlackTiles(Map<Point, Boolean> flippedTiles, Point position) {
        return Arrays.stream(Direction.values())
                        .filter(dir -> getNeighbourAt(flippedTiles, position, dir.getDirection()))
                        .count();
    }

    public static void main(String[] args) {
        var lines = AoCUtil.parseInputToStringList(24);
        var result1 = part1(lines);
        log.info("Part 1 = {}", result1);

        var result2 = part2(lines);
        log.info("Part 2 = {}", result2);
    }

    public static long part1(List<String> lines) {
        var flippedTiles = initialFloorTiling(lines);
        return countBlackTiles(flippedTiles);
    }

    @NotNull private static Map<Point, Boolean> initialFloorTiling(
                    List<String> lines) {
        Map<Point, Boolean> flippedTiles = new HashMap<>();
        for (var line : lines) {
            final var directions = getDirections(line);
            final var destination = walkDirections(directions);
            final var isBlackSideUp = flippedTiles
                            .getOrDefault(destination, false);
            flippedTiles.put(destination, !isBlackSideUp);
            addNewWhiteNeighbours(flippedTiles, destination);
        }
        return flippedTiles;
    }

    private static void addNewWhiteNeighbours(Map<Point, Boolean> flippedTiles, Point target) {
        for (Direction dir : Direction.values()) {
            final var neighbour = new Point(
                            target.x + dir.getDirection().x,
                            target.y + dir.getDirection().y);
            if (!flippedTiles.containsKey(neighbour))
                flippedTiles.put(neighbour, false);
        }
    }

    public static List<Direction> getDirections(String line) {
        List<Direction> directions = new ArrayList<>();

        int i = 0;
        while (i < line.length()) {
            if (line.startsWith("se", i)) {
                directions.add(Direction.SOUTH_EAST);
                i += 2;
            } else if (line.startsWith("sw", i)) {
                directions.add(Direction.SOUTH_WEST);
                i += 2;
            } else if (line.startsWith("ne", i)) {
                directions.add(Direction.NORTH_EAST);
                i += 2;
            } else if (line.startsWith("nw", i)) {
                directions.add(Direction.NORTH_WEST);
                i += 2;
            } else if (line.charAt(i) == 'e') {
                directions.add(Direction.EAST);
                i++;
            } else if (line.charAt(i) == 'w') {
                directions.add(Direction.WEST);
                i++;
            } else {
                throw new RuntimeException("Invalid input! " + line);
            }
        }
        return directions;
    }

    public static Point walkDirections(List<Direction> directions) {
        var destination = new Point(0, 0);
        for (var dir : directions)
            destination.translate(dir.getDirection().x, dir.getDirection().y);
        return destination;
    }

    public static long part2(List<String> lines) {
        var flippedTiles = initialFloorTiling(lines);
        for (int daysPassed = 0; daysPassed < 100; ++daysPassed)
            permuteExhibition(flippedTiles);
        return countBlackTiles(flippedTiles);
    }

    public static void permuteExhibition(Map<Point, Boolean> tiles) {
        var oldTiling = new HashMap<>(tiles);
        for (var point : oldTiling.keySet()) {
            var amountOfBlackTiledNeighbours = countAdjestBlackTiles(oldTiling,
                            point);
            if (oldTiling.get(point) == BLACK_SIDE_UP &&
                            (amountOfBlackTiledNeighbours == 0 || amountOfBlackTiledNeighbours > 2)) {
                tiles.put(point, WHITE_SIDE_UP);
                addNewWhiteNeighbours(tiles, point);
            } else if (oldTiling.get(point) == WHITE_SIDE_UP && amountOfBlackTiledNeighbours == 2) {
                tiles.put(point, BLACK_SIDE_UP);
                addNewWhiteNeighbours(tiles, point);
            }
        }
    }

    private static boolean getNeighbourAt(Map<Point, Boolean> flippedTiles,
                                          Point target, Point dir) {
        return flippedTiles.getOrDefault(
                        new Point(target.x + dir.x, target.y + dir.y),
                        false);
    }
}