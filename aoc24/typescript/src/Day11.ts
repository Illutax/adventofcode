import { Assert } from "./Assert";
import { computeIfAbsent, getInput, timed } from "./Util";

const testInput = `125 17
`;

class Pebble {
    private static readonly CACHE = new Map<number, Pebble>();

    public static readonly ZERO = new Pebble(0);
    public static readonly ONE = new Pebble(1);


    constructor(public readonly engraving: number) {
        Pebble.CACHE.set(0, Pebble.ZERO);
        Pebble.CACHE.set(1, Pebble.ONE);
    }

    public static of(value: number) {
        return computeIfAbsent(Pebble.CACHE, value, () => new Pebble(value))
    }

    public blink(): Pebble[] {
        const v = this.engraving;
        if (v == 0) return [Pebble.ONE];
        const s = `${v}`;
        const length = s.length;
        if (length % 2 == 1) {
            return [Pebble.of(v * 2024)]
        }
        const left = s.substring(0, length / 2)
        const right = s.substring(length / 2)
        return [Pebble.of(Number.parseInt(left)), Pebble.of(Number.parseInt(right))]
    }

    public toString() {
        return `${this.engraving}`;
    }
}

class CacheKey {
    private static readonly CACHE = new Map<string, CacheKey>()

    private constructor(public readonly line: string, public readonly blinkAmount: number) {
    }

    public static of(line: string, blinkAmount: number) {
        const key = `${line};${blinkAmount}`;
        return computeIfAbsent(this.CACHE, key, () => new CacheKey(line, blinkAmount));
    }
}

class PebbleLine {
    private static readonly CACHE = new Map<CacheKey, PebbleLine>()

    constructor(public readonly line: Pebble[]) {
    }

    public static from(input: string) {
        return new PebbleLine(input.split(" ")
            .map(Number)
            .map(it => new Pebble(it)));
    }

    public chop(): PebbleLine[] {
        return this.line.map(pebble => new PebbleLine([pebble]))
    }

    public blink(times = 1): PebbleLine {
        return computeIfAbsent(PebbleLine.CACHE, CacheKey.of(this.toString(), times), () => this.blinkInternal(times));
    }

    private blinkInternal(times: number) {
        let result: PebbleLine = this;
        for (let i = 0; i < times; i++) {
            // console.log(i, this.amountOfUniquePebbles(), this.line.length)
            result = new PebbleLine(result.line.flatMap(pebble => pebble.blink()));
        }
        return result;
    }

    public amountOfUniquePebbles() {
        return this.line.map(pebble => pebble.engraving)
            .reduce((set, pebble) => set.add(pebble), new Set<number>())
            .size;
    }

    public prettyPrint() {
        console.log(this.toString());
    }

    public toString() {
        return this.line.join(" ");
    }
}

function parse(input: string): PebbleLine {
    return PebbleLine.from(input);
}

function fiveBlinksLater(input: PebbleLine) {
    return input.blink(5);
}

function twentyFiveBlinksLater(input: PebbleLine) {
    return input.blink(25);
}

function part1(input: PebbleLine): number {
    input.prettyPrint();
    input = timed(() => twentyFiveBlinksLater(input), `Part1 with ${input.line.length} pebbles`);
    return input.line.length;
}

// Part1
const parsedTestInput = parse(testInput);
const part1Test = part1(parsedTestInput); // 11.26ms
Assert.isEqual(55312, part1Test)

const parsedInput = parse(getInput(11));
console.log(parsedInput);
const part1Result = part1(parsedInput); // 44.65ms
Assert.isEqual(228668, part1Result);

// Part2
function part2(input: PebbleLine): number {
    input.prettyPrint();
    console.log("First pass");
    let lines = twentyFiveBlinksLater(input).chop();
    console.log("Second pass");
    let lastLog = performance.now();
    const intermediateResult = lines.flatMap((line, index) => {
        if (lastLog < performance.now() - 100) {
            // console.log(`Second pass: ${index / lines.length * 100}%`)
            lastLog = performance.now();
        }
        return twentyFiveBlinksLater(line);
    });
    console.log("Third pass");
    let sum = 0;
    intermediateResult.flatMap((line, index) => {
        const lines = line.chop();
        if (lastLog < performance.now() - 500) {
            // console.log(`Third pass: ${index / intermediateResult.length * 100}%`)
            lastLog = performance.now();
        }
        return lines.forEach(line => sum += twentyFiveBlinksLater(line).line.length);
    });
    return sum;
}

const part2TestResult = timed(() => part2(parsedTestInput),`Part 2 with ${parsedTestInput.line.length} pebbles`); // 9.05min
Assert.isEqual(65601038650482, part2TestResult);
const part2Result = timed(() => part2(parsedInput), `Part 2 with ${parsedInput.line.length} pebbles`); // 26.76min
Assert.isEqual(270673834779359, part2Result);
