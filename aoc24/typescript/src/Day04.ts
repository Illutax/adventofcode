import { Assert } from "./Assert";
import { getInput, timed } from "./Util";

const smallTestInput = `..X...
.SAMX.
.A..A.
XMAS.S
.X....`;

const testInput = `MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX`;

class Grid {
    public readonly w: number;
    public readonly h: number;

    constructor(public cells: string[][]) {
        this.h = cells.length;
        this.w = cells[0].length;
    }

    static of(input: string) {
        return new Grid(input.split("\n").map(line => line.split("")));
    }

    public countXMAS(): number {
        const phrase = "XMAS";
        let sum = 0;
        for (let y = 0; y < this.h; y++) {
            for (let x = 0; x < this.w; x++) {
                const currentCell = this.cells[y][x];
                if (currentCell != "X" && currentCell != "S") continue;

                const horizontalSlice = [currentCell,
                    this.cells.at(y)?.at(x + 1),
                    this.cells.at(y)?.at(x + 2),
                    this.cells.at(y)?.at(x + 3)];
                const verticalSlice = [currentCell,
                    this.cells.at(y + 1)?.at(x),
                    this.cells.at(y + 2)?.at(x),
                    this.cells.at(y + 3)?.at(x)];
                const firstDiagonalSlice = [currentCell,
                    this.cells.at(y + 1)?.at(x + 1),
                    this.cells.at(y + 2)?.at(x + 2),
                    this.cells.at(y + 3)?.at(x + 3)];
                const secondDiagonalSlice = [currentCell,
                    this.cells.at(y - 1)?.at(x + 1),
                    this.cells.at(y - 2)?.at(x + 2),
                    this.cells.at(y - 3)?.at(x + 3)];

                if (horizontalSlice.join("") === phrase || horizontalSlice.toReversed().join("") === phrase) sum++;
                if (verticalSlice.join("") === phrase || verticalSlice.toReversed().join("") === phrase) sum++;
                if (firstDiagonalSlice.join("") === phrase || firstDiagonalSlice.toReversed().join("") === phrase) sum++;
                if (secondDiagonalSlice.join("") === phrase || secondDiagonalSlice.toReversed().join("") === phrase) sum++;
            }
        }
        return sum;
    }


    public countMAS(): number {
        const phrase = /*X-*/"MAS";
        let sum = 0;
        for (let y = 0; y < this.h; y++) {
            for (let x = 0; x < this.w; x++) {
                const currentCell = this.cells[y][x];
                if (currentCell != "A") continue;

                const firstDiagonalSlice = [this.cells.at(y -1)?.at(x -1),
                    currentCell,
                    this.cells.at(y + 1)?.at(x + 1)];
                const secondDiagonalSlice = [this.cells.at(y + 1)?.at(x - 1),
                    currentCell,
                    this.cells.at(y - 1)?.at(x + 1)];

                if ((firstDiagonalSlice.join("") === phrase || firstDiagonalSlice.toReversed().join("") === phrase) &&
                    (secondDiagonalSlice.join("") === phrase || secondDiagonalSlice.toReversed().join("") === phrase)) sum++;
            }
        }
        return sum;
    }
}

// Part1
function part1(input: Grid) {
    return timed(() => input.countXMAS(), `Part 1: ${input.w}x${input.h}`);
}

const parsedSmallTestResult = Grid.of(smallTestInput);
console.log(parsedSmallTestResult);
const part1SmallTest = part1(parsedSmallTestResult);
Assert.isEqual(4, part1SmallTest)

const parsedTestResult = Grid.of(testInput);
console.log(parsedTestResult);
const part1Test = part1(parsedTestResult);
Assert.isEqual(18, part1Test)

const parsedResult = Grid.of(getInput(4));
console.log(parsedResult);
const part1Result = part1(parsedResult);
Assert.isEqual(2370, part1Result);

// Part2
function part2(input: Grid) {
    return input.countMAS();
}

const part2Test = part2(parsedTestResult);
Assert.isEqual(9, part2Test);

const part2Result = part2(parsedResult);
Assert.isEqual(1908, part2Result);
