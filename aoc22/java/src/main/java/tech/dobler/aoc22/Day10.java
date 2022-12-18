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

    static class SimpleMachine extends Machine {
        private final List<Long> signals = new ArrayList<>();

        public SimpleMachine() {
            super(20, 40);
        }

        private long currentSignal() {
            return cycles * aggregate(xs);
        }

        @Override
        protected void flush() {
            final var currentX = aggregate(xs);
            signals.add(currentSignal());
            xs.clear();
            xs.add(currentX);
        }

        @Override
        public SimpleMachine accept(Stream<Instruction> instructions) {
            return (SimpleMachine) super.accept(instructions);
        }

        public long getSignalStrength() {
            return aggregate(signals);
        }
    }

    static class CRTMachine extends Machine {
        private StringBuilder crtBuffer;
        private final StringBuilder crt = new StringBuilder(6);

        public CRTMachine() {
            super(0, 40);
            crtBuffer = new StringBuilder(cycleLength);
        }

        @Override
        protected void flush() {
            crt.append(flushBuffer()).append("\n");
        }

        public String flushBuffer() {
            final var content = crtBuffer.toString();
            crtBuffer = new StringBuilder(cycleLength);
            return content;
        }

        public String writeCRT() {
            return crt.toString();
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

        @Override
        public CRTMachine accept(Stream<Instruction> instructions) {
            return (CRTMachine) super.accept(instructions);
        }

        @Override
        protected void preCycle() {
            drawToCRTBuffer();
        }
    }

    abstract static class Machine {
        private final int firstCycleOffSet;

        protected int cycles = 0;
        protected final List<Long> xs = new ArrayList<>();
        protected final int cycleLength;

        protected Machine(int firstCycleOffSet, int cycleLength) {
            xs.add(1L); // initial value
            this.firstCycleOffSet = firstCycleOffSet;
            this.cycleLength = cycleLength;
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
            preCycle();

            cycles++;

            if (cycles != 0 && cycles == firstCycleOffSet
                    || (cycles - firstCycleOffSet) % cycleLength == 0) {
                flush();
            }
        }

        protected void preCycle() {
        }

        protected static long aggregate(List<Long> list) {
            return list.stream().mapToLong(i -> i).sum();
        }

        protected Machine accept(Stream<Instruction> instructions) {
            instructions.forEach(this::accept);
            return this;
        }

        protected abstract void flush();
    }

    public long part1(Stream<Instruction> input) {
        return new SimpleMachine()
                .accept(input)
                .getSignalStrength();
    }

    public String part2(Stream<Instruction> input) {
        return new CRTMachine()
                .accept(input)
                .writeCRT();
    }
}