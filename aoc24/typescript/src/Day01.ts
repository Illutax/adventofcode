import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = `3   4
4   3
2   5
1   3
3   9
3   3`;

class List {
    constructor(public values: number[] = [],
                public length: number = 0,
                public occurrences: Map<number, number> = new Map<number, number>()) {
    }

    public add(value: number) {
        const prevAmountOfOccurrences = this.occurrences.get(value) ?? 0;
        this.occurrences.set(value, prevAmountOfOccurrences + 1)
        this.values.push(value)
        this.length++;
    }
}

function parse(input: string): List[] {
    const list1 = new List()
    const list2 = new List()
    input.split("\n").forEach(line => {
        if (line == "") return;
        let values = line.split(/\s+/).filter(Boolean).map(Number);
        list1.add(values[0]);
        list2.add(values[1]);
    })
    if (list1.length != list2.length) throw new Error(`Length differs! ${list1.length}, ${list2.length}`)
    return [list1, list2];
}

function part1(lists: List[]): number {
    const [list1, list2] = lists;
    list1.values.sort((a,b) => a-b)
    list2.values.sort((a,b) => a-b)
    let result = 0;
    for (let i = 0; i < list1.length; i++) {
        result += Math.abs(list2.values[i] - list1.values[i])
    }
    return result;
}

function part2(lists: List[]): number {
    const [list1, list2] = lists;

    let result = 0;
    for (let i = 0; i < list1.length; i++) {
        const referenceNumber = list1.values[i];
        const amount = list2.occurrences.get(referenceNumber) ?? 0
        result += referenceNumber * amount;
    }
    return result;
}

// Part1
const parsedTestResult = parse(testInput);
console.log(parsedTestResult);
const part1Test = part1(parsedTestResult);
Assert.isEqual(11, part1Test)

const parsedResult = parse(getInput(1));
console.log(parsedResult);
const part1Result = part1(parsedResult);
Assert.isGreaterThan(part1Result, 2173431);
Assert.isEqual(2285373, part1Result);

// Part2
console.log(parsedTestResult);
const part2Test = part2(parsedTestResult);
Assert.isEqual(31, part2Test);

const part2Result = part2(parsedResult);
Assert.isGreaterThan(part2Result, 2285373);
Assert.isEqual(21142653, part2Result);
