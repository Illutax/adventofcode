import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = ``;

function parse(input: string): any[] {
    input.split("\n").forEach(line => {

    })
    return [];
}

function part1(input: any[]): number {

    return -1;
}

// Part1
const parsedTestResult = parse(testInput);
console.log(parsedTestResult);
const part1Test = part1(parsedTestResult);
Assert.isEqual(-1, part1Test)

const parsedResult = parse(getInput(0));
console.log(parsedResult);
const part1Result = part1(parsedResult);
Assert.isEqual(-1, part1Result);

// // Part2
// function part2(input: any[]): number {
//
//     return -1;
// }
//
// console.log(parsedTestResult);
// const part2Test = part2(parsedTestResult);
// Assert.isEqual(-1, part2Test);
//
// const part2Result = part2(parsedResult);
// Assert.isEqual(-1, part2Result);
