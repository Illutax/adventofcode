import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.Is.is;

public class Day8Test_Machine {
    @Test
    public void nop_alway_incPcAccStays0() {
        // Arrange
        Machine machine = new Machine();

        // Act
        machine.nop();

        // Assert
        assertThat(machine.ac, is(0));
        assertThat(machine.pc, is(1));
    }

    @Test
    public void acc_given5_adds5ToAcAnd1ToPc() {
        // Arrange
        Machine machine = new Machine();

        // Act
        machine.acc(5);

        // Assert
        assertThat(machine.ac, is(5));
        assertThat(machine.pc, is(1));
    }

    @Test
    public void jmp_given7_adds7ToPcAndAcStays0() {
        // Arrange
        Machine machine = new Machine();

        // Act
        machine.jmp(7);

        // Assert
        assertThat(machine.ac, is(0));
        assertThat(machine.pc, is(7));
    }

    @Test
    public void apply_nop_doesTheSameAsNop() {
        // Arrange
        var machineUnderTest = new Machine();
        var controllMachine = new Machine();
        controllMachine.nop();
        Instruction nop = Instruction.of("nop", 1337);

        // Act
        machineUnderTest.apply(nop);

        // Assert
        assertThat(machineUnderTest, is(controllMachine));
    }
}
