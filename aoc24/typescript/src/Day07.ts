import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = `190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
`;

class Operator {
    public static readonly PLUS = new Operator("+");
    public static readonly MULT = new Operator("*");
    public static readonly CONC = new Operator("||");

    private constructor(private type: string) {
        if (type != "+" && type != "*" && type != "||") throw new Error(`Unknown type ${type}`);
    }

    public apply(operand1: number, operand2: number) {
        if (this.type == "+") {
            return operand1 + operand2;
        } else if (this.type == "*") {
            return operand1 * operand2;
        } else if (this.type == "||") {
            return Number.parseInt(`${operand1}${operand2}`);
        }
        throw new Error("Should not get here.");
    }
}

class CalibrationEquation {

    constructor(public readonly result: number, public readonly operands: number[]) {
    }

    isSolvable(): boolean {
        return this.result === this.solveInternal();
    }

    private solveInternal(operators: Operator[] = [Operator.PLUS]): number {
        if (operators.length == this.operands.length) {
            return this.operands.reduce((a, b, i) => operators[i].apply(a, b), 0);
        }
        const product = this.solveInternal(operators.concat(Operator.MULT));
        const sum = this.solveInternal(operators.concat(Operator.PLUS));
        return product == this.result ? product : sum;
    }

    isSolvable2(): boolean {
        return this.result === this.solveInternal2();
    }

    private solveInternal2(operators: Operator[] = [Operator.PLUS]): number {
        if (operators.length == this.operands.length) {
            return this.operands.reduce((a, b, i) => operators[i].apply(a, b), 0);
        }
        const product = this.solveInternal2(operators.concat(Operator.MULT));
        const sum = this.solveInternal2(operators.concat(Operator.PLUS));
        const concat = this.solveInternal2(operators.concat(Operator.CONC));
        if (product == this.result) {
            return product;
        } else if (sum == this.result) {
            return sum;
        } else {
            return concat;
        }
    }
}

function parse(input: string): CalibrationEquation[] {
    const iterator = input.matchAll(/(\d+): (\d+(?: \d+)+)/g)!
    const match = [...iterator]
    return match.map(calibration => {
        const [_, result, operators] = calibration;
        return new CalibrationEquation(+result, operators.split(" ").map(Number));
    });
}

function part1(input: CalibrationEquation[]): number {
    return input.filter(equation => equation.isSolvable())
        .map(it => it.result)
        .reduce((a, b) => a + b, 0);
}

// Part1
const parsedTestResult = parse(testInput);
const part1Test = part1(parsedTestResult);
Assert.isEqual(3749, part1Test)

const parsedResult = parse(getInput(7));
const part1Result = part1(parsedResult);
Assert.isEqual(42_283_209_483_350, part1Result);

// Part2
function part2(input: CalibrationEquation[]): number {
    return input.filter(equation => equation.isSolvable2())
        .map(it => it.result)
        .reduce((a, b) => a + b, 0);
}

const part2Test = part2(parsedTestResult);
Assert.isEqual(11387, part2Test);

const part2Result = part2(parsedResult);
Assert.isEqual(1_026_766_857_276_279, part2Result);
