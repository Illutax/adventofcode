import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum Direction {
    EAST(1, 0), SOUTH_EAST(0, 1), SOUTH_WEST(-1, 1), WEST(-1, 0), NORTH_WEST(0,
                    -1), NORTH_EAST(1, -1);

    private final Point _dir;

    Direction(int x, int y) {
        _dir = new Point(x, y);
    }

    public Point getDirection() {
        return new Point(_dir);
    }
}


class Point extends java.awt.Point {

    public static final Point ORIGIN = new Point(0, 0);

    public Point(@NotNull java.awt.Point p) {
        super(p);
    }

    public Point(int x, int y) {
        super(x, y);
    }

    public void translate(Point dir) {
        super.translate(dir.x, dir.y);
    }
}


@Slf4j public class Day24 {
    public static void main(String[] args) {
        var lines = AoCUtil.parseInputToListStrings(24);
        var result1 = part1(lines);
        assert result1 == 266 : "correct answer";
        log.info("Part 1 = {}", result1);
    }

    public static long part1(List<String> lines) {
        Map<Point, Boolean> flippedTiles = new HashMap<>();
        for (var line : lines) {
            final List<Direction> directions = getDirections(line);
            final Point destination = walkDirections(directions);
            flippedTiles.put(destination,
                            !flippedTiles.getOrDefault(destination, false));
        }
        return flippedTiles.values().stream()
                        .filter(b -> b)
                        .count();
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
            }
        }
        return directions;
    }

    public static Point walkDirections(List<Direction> directions) {
        var position = new Point(0, 0);
        for (var dir : directions) {
            position.translate(dir.getDirection());
        }
        return position;
    }
}