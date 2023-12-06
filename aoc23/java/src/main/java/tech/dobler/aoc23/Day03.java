package tech.dobler.aoc23;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class Day03 {
    public int part1(String input) {
        return Grid.of(input)
                .calculateAdjacent()
                .schematicSum();
    }

    record Grid(List<Part> parts, List<Symbol> symbols) {
        private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+).*");

        @SuppressWarnings({"ReassignedVariable", "java:S127"})
        public static Grid of(String input) {
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
                        var matcher = NUMBER_PATTERN.matcher(row.substring(x));
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
            return parts.stream()
                    .flatMap(part -> part.symbols.stream()
                            .filter(Symbol::isGear)
                            .map(symbol -> new AbstractMap.SimpleEntry<>(symbol, part)))
                    .collect(Collectors.groupingBy(
                            AbstractMap.SimpleEntry::getKey,
                            mapping(AbstractMap.SimpleEntry::getValue, toList())))
                    .values().stream()
                    .filter(partList -> partList.size() == 2)
                    .mapToLong(Grid::getProductOfPartNumbers)
                    .sum();
        }

        private static long getProductOfPartNumbers(List<Part> partList) {
            return partList.stream()
                    .mapToLong(Part::number)
                    .reduce(1L, Util.Math::multiply);
        }
    }

    record Part(Pos pos, int number, List<Symbol> symbols) {
        public void process(List<Symbol> symbolsToCheck) {
            var length = (int) Math.log10(number) + 2;
            for (Symbol symbol : symbolsToCheck) {
                if (symbol.pos.x > pos.x - 2
                        && symbol.pos.x < pos.x + length
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