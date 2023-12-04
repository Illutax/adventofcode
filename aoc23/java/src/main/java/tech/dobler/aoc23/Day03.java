package tech.dobler.aoc23;

import java.util.*;
import java.util.regex.Pattern;

public class Day03 {
    public int part1(String input) {
        return Grid.of(input)
                .calculateAdjacent()
                .schematicSum();
    }

    record Grid(List<Part> parts, List<Symbol> symbols) {
        public static Grid of(String input) {
            final var pattern = Pattern.compile("(\\d+).*");
            List<Part> parts = new ArrayList<>();
            List<Symbol> symbols = new ArrayList<>();
            String[] rows = input.split("\n");
            for (int y = 0; y < rows.length; y++) {
                String row = rows[y];
                String[] cells = row.split("");
                for (int x = 0; x < cells.length; x++) {
                    String cell = cells[x];
                    if (".".equals(cell)) {
                        continue;
                    }
                    if (Character.isDigit(cell.charAt(0))) {
                        var matcher = pattern.matcher(row.substring(x));
                        if (!matcher.matches())
                            throw new IllegalStateException("Coudln't find part in %s starting at %d".formatted(row, x));
                        var partNumber = matcher.group(1);
                        Part newPart = new Part(new Pos(x, y), Integer.parseInt(partNumber), new ArrayList<>());
                        parts.add(newPart);
                        x += partNumber.length() - 1;
                    } else { // is a symbol
                        symbols.add(new Symbol(new Pos(x, y), cell));
                    }
                }

            }
            return new Grid(parts, symbols);
        }

        public int schematicSum() {
            return parts.stream()
                    .filter(part -> !part.symbols().isEmpty())
                    .mapToInt(Part::number)
                    .sum();
        }

        public Grid calculateAdjacent() {
            parts.forEach(part -> part.process(symbols));
            return this;
        }

        public Long gearRatios() {
            var partsWithGears = parts.stream()
                    .filter(part -> part.symbols().stream().anyMatch(Symbol::isGear))
                    .toList();
            final var map = new HashMap<Symbol, List<Part>>();
            partsWithGears.forEach(part -> part.symbols().stream()
                    .filter(Symbol::isGear)
                    .forEach(gear -> map.computeIfAbsent(gear, __ -> new ArrayList<>()).add(part)));
            return map.values().stream()
                    .filter(partList -> partList.size() == 2)
                    .mapToLong(partList -> partList.stream().mapToLong(Part::number).reduce(1L, (a, b) -> a * b))
                    .sum();

        }
    }

    record Part(Pos pos, int number, List<Symbol> symbols) {
        public void process(List<Symbol> symbolsToCheck) {
            var length = (int)Math.log10(number) + 1;
            for (Symbol symbol : symbolsToCheck) {
                if (symbol.pos.x > pos.x - 2
                        && symbol.pos.x < pos.x + length + 1
                        && symbol.pos.y > pos.y - 2
                        && symbol.pos.y < pos.y + 2) {
                        this.symbols.add(symbol);
                }
            }
        }
    }

    record Symbol(Pos pos, String value) {
        boolean isGear() {
            return value.equals("*");
        }
    }

    record Pos(int x, int y) {
    }

    public long part2(String input) {
        return Grid.of(input)
                .calculateAdjacent()
                .gearRatios();
    }
}