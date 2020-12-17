import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class Day8Test {
    @Test
    public void parseInstruction_givenNopInstruction_returnsNopFunc() {
        // Arrange
        var day8 = new Day8();
        String instruction = "nop +0";
        var machine = new Machine();

        // Act
        var result = day8.parseInstruction(instruction);

        // Assert
        assertThat(result, is(Instruction.of("nop", 0)));
    }

    @Test
    public void parseInstruction_givenAccInstruction_returnsAccFunc() {
        // Arrange
        var day8 = new Day8();
        String instruction = "acc +13";
        var machine = new Machine();

        // Act
        var result = day8.parseInstruction(instruction);

        // Assert
        assertThat(result, is(Instruction.of("acc", 13)));
    }

    @Test
    public void parseInstruction_givenJmpInstruction_returnsJmpFunc() {
        // Arrange
        var day8 = new Day8();
        String instruction = "jmp -27";
        var machine = new Machine();

        // Act
        var result = day8.parseInstruction(instruction);

        // Assert
        assertThat(result, is(Instruction.of("jmp", -27)));
    }

    @Test
    public void parseInstructions_given3Instructions_parsesAll3Correctly() {
        // Arrange
        var day8 = new Day8();
        String instructions = "jmp -27\nacc +13\nnop +0";

        // Act
        var result = day8.parseInstructions(instructions);

        // Assert
        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(Instruction.of("jmp", -27)));
        assertThat(result.get(1), is(Instruction.of("acc", 13)));
        assertThat(result.get(2), is(Instruction.of("nop", 0)));
    }
}