import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) {
        final String path = "java/input8.txt";
        final var day = new Day8();
        final List<Instruction> instructions = day.getInstructions(path);

        System.out.println("Part 1:");
        var machine = new Machine();
        day.applyUntilLoopOrEnd(machine, instructions);
        assert machine.ac == 1331 : "correct answer";
        System.out.println("Current ac: "+ machine.ac + " current pc: " + machine.pc);

        System.out.println("\nPart 2:");
        var lastSwappedInstruction = -1;
        var iter = -1;
        while(machine.pc < instructions.size()) {
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
        System.out.println("Correct instruction: " + lastSwappedInstruction + " current ac: "+ machine.ac + " current pc: " + machine.pc);
    }

    private static void swapInstruction(List<Instruction> instructions, int i) {
        instructions.get(i).opCode = instructions.get(i).opCode.equals("nop") ? "jmp" : "nop";
    }

    public void applyUntilLoopOrEnd(Machine machine, List<Instruction> instructions) {
        machine.apply(instructions.get(0));
        while(machine.pc < instructions.size()) {
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

class Machine {
    int pc, ac;
    Set<Integer> history = new TreeSet<>();

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
        history.add(pc);
        pc += i;
    }

    public void apply(Instruction in) {
        try {
            final Method op = Machine.class.getMethod(in.opCode, int.class);
            var pcBefore = pc;
            op.invoke(this, in.arg);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Machine{" +
                "pc=" + pc +
                ", ac=" + ac +
                ", history=" + history +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Machine)) return false;
        Machine machine = (Machine) o;
        return pc == machine.pc &&
                ac == machine.ac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pc, ac);
    }
}

class Instruction {
    String opCode;
    int arg;

    public static Instruction of(String opCode, int arg) {
        var instr = new Instruction();
        instr.opCode = opCode;
        instr.arg = arg;
        return instr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return arg == that.arg &&
                opCode.equals(that.opCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opCode, arg);
    }
}
