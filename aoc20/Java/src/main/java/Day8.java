import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) {
        final String path = "input8.txt";
        final var day = new Day8();
        final List<Instruction> instructions = day.getInstructions(path);

        System.out.println("Part 1:");
        var machine = new Machine();
        day.applyUntilLoopOrEnd(machine, instructions);
        assert machine.ac == 1331 : "correct answer";
        System.out.println("Current ac: " + machine.ac + " current pc: " + machine.pc);

        System.out.println("\nPart 2:");
        var lastSwappedInstruction = -1;
        var iter = -1;
        while (machine.pc < instructions.size()) {
            iter++;
            machine = new Machine();
            // Look for next possible instruction
            for (int i = 0; i < instructions.size(); i++) {
                var instruction = instructions.get(i);
                if (i > lastSwappedInstruction && !instruction.opCode.equals("acc")) {
                    //System.out.println("Swapping: "+ i + " " + instruction);
                    lastSwappedInstruction = i;
                    swapInstruction(instructions, i);
                    break;
                }
            }
            day.applyUntilLoopOrEnd(machine, instructions);
            // Swap it back
            swapInstruction(instructions, lastSwappedInstruction);
            if (iter > instructions.size()) break;
        }
        assert machine.pc == instructions.size() : "Ran till the end";
        assert machine.ac == 1121 : "correct answer";
        System.out.println("Correct instruction: " + lastSwappedInstruction + " current ac: " + machine.ac + " current pc: " + machine.pc);
    }

    private static void swapInstruction(List<Instruction> instructions, int i) {
        final var newOpCode = instructions.get(i).opCode.equals("nop")
                ? "jmp"
                : "nop";
        final var instructionReplacement = Instruction.of(newOpCode, instructions.get(i).arg);
        instructions.set(i, instructionReplacement);
    }

    public void applyUntilLoopOrEnd(Machine machine, List<Instruction> instructions) {
        machine.apply(instructions.get(0));
        while (machine.pc < instructions.size()) {
            if (machine.history.contains(machine.pc)) {
                //System.out.println(":( Halted at pc=" + machine.pc);
                break;
            }
            machine.apply(instructions.get(machine.pc));
        }
        //System.out.println("accumulator=" + machine.ac);
    }

    public List<Instruction> getInstructions(String path) {
        try {
            return Files.lines(Paths.get(path))
                    .map(this::parseInstruction)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Instruction parseInstruction(String s) {
        assert s.matches("\\w{3} [+-]\\d+");
        final String[] split = s.split(" ");
        return Instruction.of(split[0], Integer.parseInt(split[1]));
    }

    public List<Instruction> parseInstructions(String input) {
        return Arrays.stream(input.split("\n"))
                .map(this::parseInstruction)
                .collect(Collectors.toList());
    }
}

@ToString
@EqualsAndHashCode
class Machine {
    final Set<Integer> history = new TreeSet<>();
    int pc, ac;

    @SuppressWarnings("unused") /*accessed via reflection*/
    public void nop(int i) {
        nop();
    }

    public void nop() {
        jmp(1);
    }

    public void acc(int i) {
        nop();
        ac += i;
    }

    public void jmp(int i) {
        if (pc + i < 0) throw new IllegalArgumentException("Underflow at pc:" + pc);
        history.add(pc);
        pc += i;
    }

    public void apply(Instruction in) {
        try {
            final Method op = Machine.class.getMethod(in.opCode, int.class);
            op.invoke(this, in.arg);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
class Instruction {
    final String opCode;
    final int arg;
}
