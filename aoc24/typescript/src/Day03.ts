import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = `xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))`;
const testInput2 = `xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))`;

class Instruction {
    constructor(public operation: Operation, public operand1: number, public operand2: number) {
    }

    public apply() {
        return this.operand1 * this.operand2;
    }
}

enum Operation {
    mul = "MUL"
}

function parse1(input: string): Instruction[] {
    const match = input.match(/(mul\(\d+,\d+\))/g);
    const len = match?.length
    console.log([{input: input, groups: match, len: len}]);

    return match?.map(inst => {
        const [op1, op2] = inst.match(/(\d+)/g)!.map(Number);
        return new Instruction(Operation.mul, op1, op2)
    })!
}

function applyAndSum(input: Instruction[]): number {
    return input.map(instruction => instruction.apply()).reduce((a: number, b: number) => a + b, 0);
}

// Part1
const parsedTestResult = parse1(testInput);
console.log(parsedTestResult);
const part1Test = applyAndSum(parsedTestResult);
Assert.isEqual(161, part1Test)

const parsedResult = parse1(getInput(3));
console.log(parsedResult);
const part1Result = applyAndSum(parsedResult);
Assert.isEqual(165_225_049, part1Result);

// Part2
function parse2(input: string): Instruction[] {
    const match = input.match(/(mul\(\d+,\d+\)|do\(\)|don't\(\))/g);
    const len = match?.length
    console.log([{input: input, groups: match, len: len}]);
    let enabled = true;
    return match
        ?.filter(inst => {
            if (inst == "do()") {
                enabled = true;
                return false;
            }
            if (inst == "don't()") {
                enabled = false;
                return false;
            }
            return enabled;
        })
        .map(inst => {
            const [op1, op2] = inst.match(/(\d+)/g)!.map(Number);
            return new Instruction(Operation.mul, op1, op2)
        })!
}

const parsedTestResult2 = parse2(testInput2);
console.log(parsedTestResult2);
const part2Test = applyAndSum(parsedTestResult2);
Assert.isEqual(48, part2Test);

const parsedResult2 = parse2(getInput(3));
const part2Result = applyAndSum(parsedResult2);
Assert.isSmallerThan(part2Result, 165_225_049);
Assert.isEqual(108830766, part2Result);
