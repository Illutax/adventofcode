package tech.dobler.aoc24;

import java.util.HashMap;
import java.util.Map;

public record Vec2(int x, int y) {
    private static final Map<String, Vec2> cache = new HashMap<>();
    public static final Vec2 ZERO = Vec2.of(0,0);

    public static Vec2 of(int x, int y) {
        final var key = "%d|%d".formatted(x,y);
        return cache.computeIfAbsent(key, _ -> new Vec2(x, y));
    }

    public Vec2 plus(Vec2 other) {
        return Vec2.of(x + other.x, y + other.y);
    }

    public Vec2 mult(int factor) {
        return Vec2.of(x * factor, y * factor);
    }

    public Vec2 rotateCW() {
        //noinspection SuspiciousNameCombination NO, STUPID INTELLIJ!
        return Vec2.of(-this.y, this.x);
    }
}
