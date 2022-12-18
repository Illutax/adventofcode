package tech.dobler.aoc22;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day10 {

    sealed interface Instruction permits AddX, Noop {
        Pattern pattern = Pattern.compile("^(?:(noop)|(addx) (-?[1-9]\\d*))$");

        static Instruction from(String line) {
            final var matcher = pattern.matcher(line);
            if (!matcher.matches())
                throw new IllegalArgumentException("Unexpected line: \"%s\"".formatted(line));
            if ("noop".equals(matcher.group(1))) return new Noop();
            return new AddX(Long.parseLong(matcher.group(3)));
        }
    }

    record AddX(long amount) implements Instruction {
    }

    record Noop() implements Instruction {
    }

    // TODO: Pull out two concrete machines
    static class Machine {
        private final int firstCycleOffSet;
        private int cycles = 0;
        private final List<Long> xs = new ArrayList<>();
        private final List<Long> signals = new ArrayList<>();
        private final int cycleLength;
        private StringBuilder crtBuffer;
        private final StringBuilder crt = new StringBuilder(6);

        public Machine(int firstCycleOffSet, int cycleLength) {
            xs.add(1L); // initial value
            this.firstCycleOffSet = firstCycleOffSet;
            this.cycleLength = cycleLength;
            crtBuffer = new StringBuilder(cycleLength);
        }

        public void accept(Instruction instruction) {
            cycle();
            if (instruction instanceof AddX addX) {
                cycle();
                append(addX.amount);
            }
        }

        private void append(long amount) {
            xs.add(amount);
        }

        private void cycle() {
            drawToCRTBuffer();

            cycles++;

            if ((cycles - firstCycleOffSet) % cycleLength == 0)
                crt.append(flushBuffer()).append("\n");
            if (cycles == firstCycleOffSet || (cycles - firstCycleOffSet) % cycleLength == 0) {
                flush();
            }
        }

        private void drawToCRTBuffer() {
            if (shouldDrawSprite())
                putInBuffer("#");
            else
                putInBuffer(".");
        }

        private void putInBuffer(String str) {
            crtBuffer.append(str);
        }

        private boolean shouldDrawSprite() {
            final var signal = aggregate(xs);
            final var currentCycle = cycles % 40;
            return currentCycle - 1 <= signal && signal <= currentCycle + 1;
        }

        private void flush() {
            final var currentX = aggregate(xs);
            final var signal = currentSignal();
            signals.add(signal);
            xs.clear();
            xs.add(currentX);
        }

        private long currentSignal() {
            return cycles * aggregate(xs);
        }

        private long aggregate(List<Long> list) {
            return list.stream().mapToLong(i -> i).sum();
        }

        public long getSignalStrength() {
            return aggregate(signals);
        }

        public String flushBuffer() {
            final var content = crtBuffer.toString();
            crtBuffer = new StringBuilder(cycleLength);
            return content;
        }

        public String writeCRT() {
            return crt.toString();
        }

        public Machine accept(Stream<Instruction> instructions) {
            instructions.forEach(this::accept);
            return this;
        }
    }

    public long part1(Stream<Instruction> input) {
        return new Machine(20, 40)
                .accept(input)
                .getSignalStrength();
    }

    public String part2(Stream<Instruction> input) {
        return new Machine(0, 40)
                .accept(input)
                .writeCRT();
    }
}