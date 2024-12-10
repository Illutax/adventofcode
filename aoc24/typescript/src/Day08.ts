import { Assert } from "./Assert";
import { addIfAbsent, getInput, timed, Util } from "./Util";
import { Vec2 } from "./Vec2";
import { Rect } from "./Rect";

const testInput = `............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
`;

const testInputPart2 = `T.........
...T......
.T........
..........
..........
..........
..........
..........
..........
..........
`;

class Antenna {
    constructor(public readonly frequency: string, public readonly pos: Vec2) {
    }
}

class CityRoofs {
    private readonly antennasLookup = new Map<Vec2, Antenna>();

    constructor(public readonly antennas: Antenna[], public readonly bounds: Rect) {
        for (let antenna of antennas) {
            this.antennasLookup.set(antenna.pos, antenna);
        }
    }

    public static of(input: string) {
        const rows = input.split("\n");
        const antennas = rows.flatMap((row, y) =>
            row.split("").map((cell, x) =>
                cell !== "."
                    ? new Antenna(cell, Vec2.of(x, y))
                    : null))
            .filter(it => it !== null);
        return new CityRoofs(antennas, Rect.of(Vec2.ZERO, Vec2.of(rows[0].length, rows.length - 1)));
    }

    public antinodes(considerResonantHarmonics = false): Set<Vec2> {
        const antennasByFrequency = new Map<string, Antenna[]>();
        for (let antenna of this.antennas) {
            addIfAbsent(antennasByFrequency, antenna.frequency, antenna);
        }

        const addWhenInBounds = (antinodes: Set<Vec2>, pos: Vec2, dir: Vec2) => {
            if (considerResonantHarmonics) {
                while (this.bounds.inBounds(pos)) {
                    antinodes.add(pos);
                    pos = pos.plus(dir); // REASSIGN 😇
                }
            } else {
                const antinodePos = pos.plus(dir);
                if (this.bounds.inBounds(antinodePos)) {
                    antinodes.add(antinodePos);
                }
            }
        }
        const antinodes = new Set<Vec2>;
        for (const [_, antennas] of antennasByFrequency) {
            for (let i = 0; i < antennas.length - 1; i++) {
                const antenna = antennas[i];
                for (let j = i + 1; j < antennas.length; j++) {
                    const otherAntenna = antennas[j]
                    const dir = otherAntenna.pos.minus(antenna.pos);
                    addWhenInBounds(antinodes, antenna.pos, dir.mult(-1));
                    addWhenInBounds(antinodes, otherAntenna.pos, dir);
                }
            }
        }
        return antinodes;
    }

    public prettyPrint(considerResonantHarmonics = false) {
        const antinodes = this.antinodes(considerResonantHarmonics);
        const sb = []
        for (let y = 0; y < this.bounds.bottomRight.y; y++) {
            const temp = [];
            for (let x = 0; x < this.bounds.bottomRight.x; x++) {
                const pos = Vec2.of(x, y);
                const maybeAntenna = this.antennasLookup.get(pos)
                if (maybeAntenna) {
                    temp.push(maybeAntenna!.frequency);
                } else if (antinodes.has(pos)) {
                    temp.push("#");
                } else {
                    temp.push(".");
                }
            }
            sb.push(temp);
        }
        console.log(sb.map(row => row.join("")).join("\n") + "\n");
    }

    public part1() {
        return timed(() => this.antinodes().size, `part1 ${this.antennas.length}`);
    }

    public part2() {
        return timed(() => this.antinodes(true).size, `part2 ${this.antennas.length}`);
    }
}

// Part1
const parsedTestResult = CityRoofs.of(testInput);
const part1Test = parsedTestResult.part1();
Assert.isEqual(14, part1Test)

const parsedResult = CityRoofs.of(getInput(8));
const part1Result = parsedResult.part1();
Assert.isEqual(367, part1Result);

// Part2
const parsedTest2Result = CityRoofs.of(testInputPart2);
const part2Test2 = parsedTest2Result.part2();
Assert.isEqual(9, part2Test2);

const part2Test = parsedTestResult.part2();
Assert.isEqual(34, part2Test);

const part2Result = parsedResult.part2();
Assert.isEqual(1285, part2Result);
