import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = `7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9`;

class Report {
    constructor(public lines: number[]) {
    }

    isSafe() {
        let increasing: boolean | undefined = undefined;
        const safetyThreshold = 3;
        for (let i = 1; i < this.lines.length; i++) {
            let prev = this.lines[i - 1];
            let next = this.lines[i];
            const steppingSize = next - prev;
            if (prev == next || increasing == undefined && Math.abs(steppingSize) > safetyThreshold ||
                increasing === true && steppingSize > safetyThreshold ||
                increasing === true && steppingSize < 0 ||
                increasing === false && steppingSize < -safetyThreshold ||
                increasing === false && steppingSize > 0
            ) {
                return false;
            }
            increasing = Math.sign(steppingSize) == 1;
        }
        return true;
    }
}

function parse(input: string): Report[] {
    return input.split("\n").filter(Boolean)
        .map(line => {
            Assert.isTrue(line.match(/\d+(^ \d+)*/)!.length > 0 , `"${line}" should match "/\\d+(^ \\d+)*/"`);
            let values = line.split(/\s+/).filter(Boolean).map(Number);
            return new Report(values);
        });
}

function part1(input: Report[]): number {
    return input.filter(it => it.isSafe()).length
}

// Part1
const parsedTestResult = parse(testInput);
console.log(parsedTestResult);
const part1Test = part1(parsedTestResult);
Assert.isEqual(2, part1Test)

const parsedResult = parse(getInput(2));
console.log(parsedResult);
const part1Result = part1(parsedResult);
Assert.isEqual(321, part1Result);

// Part2
function part2(input: Report[]): number {
    return input.filter(report => {
        if (report.isSafe()) return true;

        const lines = [...report.lines];
        for (let i = 0; i < lines.length; i++) {
            const withoutOne = lines.slice(0, i).concat(lines.slice(i + 1));
            if (new Report(withoutOne).isSafe()) return true;
        }
        return false;
    }).length
}

console.log(parsedTestResult);
const part2Test = part2(parsedTestResult);
Assert.isEqual(4, part2Test);

const part2Result = part2(parsedResult);
Assert.isEqual(386, part2Result);
