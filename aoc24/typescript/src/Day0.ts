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
const parsedTestInput = parse(testInput);
console.log(parsedTestInput);
const part1Test = part1(parsedTestInput);
Assert.isEqual(-1, part1Test)

const parsedInput = parse(getInput(0));
console.log(parsedInput);
const part1Result = part1(parsedInput);
Assert.isEqual(-1, part1Result);

// // Part2
// function part2(input: any[]): number {
//
//     return -1;
// }
//
// console.log(parsedTestInput);
// const part2Test = part2(parsedTestInput);
// Assert.isEqual(-1, part2Test);
//
// const part2Result = part2(parsedInput);
// Assert.isEqual(-1, part2Result);
