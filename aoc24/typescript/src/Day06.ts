import { Assert } from "./Assert";
import { getInput } from "./Util";
import { of, Vec2 } from "./Vec2";
import { PosAndDir } from "./PosDir";
import { CardinalDirection } from "./CardinalDirection";

const testInput = `....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...`;

enum Cell {
    EMPTY = '.',
    OBSTACLE = '#',
    START = '^'
}

class Grid {
    private _visitedCells: boolean[][] = [];
    // @ts-ignore
    private readonly startPosition: Vec2;
    private _guardPosition: Vec2 = Vec2.ZERO;
    private _guardOrientation = CardinalDirection.NORTH.value
    private bounds: Vec2;

    private _newObstacle: Vec2 | undefined;

    constructor(private cells: Cell[][]) {
        for (let y = 0; y < cells.length; y++) {
            const row = cells[y]
            for (let x = 0; x < row.length; x++) {
                if (row[x] == Cell.START) {
                    this._guardPosition = of(x, y);
                    this.startPosition = of(x, y);
                    break;
                }
            }
        }
        // @ts-ignore
        Assert.notNull(this.startPosition, " startPosition");
        this.bounds = of(cells[0].length, cells.length);
        console.log(this.bounds);
    }

    get visitedCells(): boolean[][] {
        return this._visitedCells;
    }

    get guardPosition(): Vec2 {
        return this._guardPosition;
    }

    get amountOfTraversedUniqueCells(): number {
        return this.visitedCells.flatMap(row => row.filter(Boolean)).length;
    }

    get newObstacle(): Vec2 | undefined {
        return this._newObstacle;
    }

    set newObstacle(value: Vec2 | undefined) {
        this._newObstacle = value;
    }

    public printTraversed() {
        const result = []
        for (let y = 0; y < this.cells.length; y++) {
            const row = this.cells[y];
            const temp = [];
            for (let x = 0; x < row.length; x++) {
                if (this.newObstacle && this.newObstacle.equals(of(x, y))) {
                    temp[x] = "O";
                } else if (row[x] !== Cell.EMPTY) {
                    temp[x] = row[x];
                } else if (this.visitedCells[y]?.at(x)) {
                    temp[x] = "#";
                } else {
                    temp[x] = ".";
                }
            }
            result[y] = temp;
        }
        console.log(result.map(r => r.join("")).join("\n"))
    }

    public traverse() {
        this._visitedCells = [];
        while (true) {
            while (this.getCellAtGuardsPosition() && this.getCellAtGuardsPosition() !== Cell.OBSTACLE) { // path is clear
                this.visit(this.guardPosition);
                this._guardPosition = this.guardPosition.plus(this._guardOrientation);
                // console.log(this.amountOfTraversedUniqueCells)
            }
            if (this.getCellAtGuardsPosition() === undefined) return; // inside grid
            this._guardPosition = this.guardPosition.plus(this._guardOrientation.mult(-1));
            this._guardOrientation = this._guardOrientation.rotateCW();
        }
    }

    public loops(): Configuration {
        Assert.notNull(this.newObstacle, "newObstacle");
        this._guardPosition = this.startPosition;
        this._guardOrientation = CardinalDirection.from("^").value;
        const visited: PosAndDir[] = [];
        while (true) {
            while (
                this.getCellAtGuardsPosition() && // inside grid
                this.getCellAtGuardsPosition() !== Cell.OBSTACLE &&
                !this.guardPosition.equals(this.newObstacle!)) {
                if (visited.find(it => PosAndDir.of(this.guardPosition, this._guardOrientation).equals(it))) {
                    // console.log("Returned to a known position and orientation")
                    return new Configuration(visited);
                }
                visited.push(PosAndDir.of(this.guardPosition, this._guardOrientation));
                this._guardPosition = this.guardPosition.plus(this._guardOrientation);
            }
            if (this.getCellAtGuardsPosition() === undefined) return new Configuration(); // inside grid
            this._guardPosition = this.guardPosition.plus(this._guardOrientation.mult(-1));
            this._guardOrientation = this._guardOrientation.rotateCW();
        }
    }

    public possibleLocationsForNewObstacle() {
        return this.visitedCells
            .flatMap((row, y) => row
                .map(Boolean)
                .map((_, x) => of(x, y)))
    }

    private visit(pos: Vec2) {
        let row = this.visitedCells[pos.y];
        if (!row) {
            row = this.visitedCells[pos.y] = [];
        }
        row[pos.x] = true;
    }

    private getCellAtGuardsPosition() {
        return this.cells.at(this.guardPosition.y)?.at(this.guardPosition.x);
    }
}

function cellFromString(cell: string) {
    switch (cell) {
        case '.':
            return Cell.EMPTY;
        case '#':
            return Cell.OBSTACLE;
        case '^':
            return Cell.START;
        default:
            throw new Error(`Unknown cell ${cell}`);
    }
}

function parse(input: string): Grid {
    const cells = input.split("\n")
        .map(line => line.split("")
            .map(cell => cellFromString(cell)))
    return new Grid(cells);
}

function part1(input: Grid): number {
    input.traverse();
    // input.printTraversed();
    return input.amountOfTraversedUniqueCells;
}

// Part1
const parsedTestResult = parse(testInput);
const part1Test = part1(parsedTestResult);
Assert.isEqual(41, part1Test)

const parsedResult = parse(getInput(6));
const part1Result = part1(parsedResult);
Assert.isEqual(4665, part1Result);

// Part2
function part2(input: Grid): number {
    const possibleLocationsForNewObstacle = input.possibleLocationsForNewObstacle();
    console.log(`Checking ${possibleLocationsForNewObstacle.length} possibleLocationsForNewObstacle`);
    const allExistingConfigurations: Configuration[] = [];
    possibleLocationsForNewObstacle.forEach(newObstacle => {
        input.newObstacle = newObstacle;
        const newConfiguration = input.loops();
        if (newConfiguration?.length > 0) {
            const alreadyExists = allExistingConfigurations.some(existingConfiguration =>
                existingConfiguration.equals(newConfiguration));

            if (!alreadyExists) {
                allExistingConfigurations.push(newConfiguration);
                // console.log(`Accepting:`, newConfiguration.length);
            } else {
                // console.log(`Rejecting:`, newConfiguration.length);
            }
        }
    });

    return allExistingConfigurations.length;
}

class Configuration {

    constructor(public _posDirs: PosAndDir[] = []) {
    }

    public equals(other: Configuration) {
        if (this._posDirs.length != other._posDirs.length) return false;
        const containsAll = (arr1: PosAndDir[], arr2: PosAndDir[]) => arr2.every(arr2Item => arr1.includes(arr2Item));

        return containsAll(this._posDirs, other._posDirs) && containsAll(other._posDirs, this._posDirs);
    }

    get length(): number {
        return this._posDirs.length;
    }
}

console.log(parsedTestResult);
const part2Test = part2(parsedTestResult);
Assert.isEqual(6, part2Test);

const part2Result = part2(parsedResult);
Assert.isSmallerThan(part2Result, 1863)
Assert.isEqual(1688, part2Result);
