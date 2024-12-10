import { Assert } from "./Assert";
import { getInput, timed, Util } from "./Util";
import { Vec2 } from "./Vec2";
import { CardinalDirection } from "./CardinalDirection";

const testInput = `89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
`;

class Cell {
    public static readonly UNREACHABLE = Number.MIN_VALUE;

    private constructor(public readonly height: number, public readonly pos: Vec2) {
    }

    public static of(height: string, pos: Vec2) {
        return new Cell(height == "." ? this.UNREACHABLE : +height, pos);
    }

    public toString() {
        return `Cell: height=${this.height}, pos=${this.pos.toString()}`
    }
}

class Trail {
    public static readonly EMPTY = new Trail([]);

    private constructor(public readonly cells: Cell[]) {
    }

    public static of(cell: Cell) {
        Assert.notNull(cell, "cell");
        return new Trail([cell]);
    }

    public append(cell: Cell) {
        Assert.notNull(cell, "cell");
        return new Trail(this.cells.concat(cell));
    }

    public equals(other: Trail) {
        return this.cells.length === other.cells.length &&
            this.cells.every((cell, index) => cell.pos.equals(other.cells[index].pos))
    }

    isEmpty() {
        return this === Trail.EMPTY;
    }
}

class TopographicMap {

    private readonly _trailheads: Cell[];

    private constructor(public readonly cells: Cell[][]) {
        this._trailheads = cells.flatMap(row => row.filter(it => it.height == 0));
    }

    public static from(input: string): TopographicMap {
        const cells = input.split("\n")
            .map(((line, y) => line.split("")
                    .map((cell, x) => Cell.of(cell, Vec2.of(x, y)))
            ));
        return new TopographicMap(cells);
    }

    get trailheads(): Cell[] {
        return this._trailheads;
    }

    // Part 1
    public getScore(trailhead: Cell): number {
        return this.walkToTop(trailhead.pos, -1).size;
    }

    private walkToTop(pos: Vec2, height: number): Set<Vec2> {
        const currentCell = this.cells[pos.y]?.[pos.x];
        if (!this.isValidWalk(currentCell, height)) {
            return Util.emptySet();
        }
        if (currentCell.height === 9)
            return Util.setOf(pos);
        return CardinalDirection.ALL.map(dir =>
            this.walkToTop(pos.plus(dir.value), currentCell.height)
        ).reduce((a, b) => a.union(b), new Set<Vec2>())
    }

    private isValidWalk(currentCell: Cell | undefined, height: number) {
        return currentCell && currentCell.height > height && currentCell.height <= height + 1;
    }

    // Part 2
    public getRating(trailhead: Cell): number {
        return this.walkTrail(trailhead.pos, -1).length;
    }

    private walkTrail(pos: Vec2, height: number): Trail[] {
        const currentCell = this.cells[pos.y]?.[pos.x];
        if (!this.isValidWalk(currentCell, height)) {
            return [];
        }
        if (currentCell.height === 9)
            return [Trail.of(currentCell)];
        return CardinalDirection.ALL.map(dir =>
            this.walkTrail(pos.plus(dir.value), currentCell.height)
        ).flatMap(trails => trails.filter(it => !it.isEmpty())
            .map(it => it.append(currentCell)));
    }
}

function part1(input: TopographicMap): number {
    return  timed(() => input.trailheads.map(trailhead => input.getScore(trailhead))
        .reduce((a, b) => a + b), `Part 1 with ${input.trailheads.length} trailheads`);
}

// Part1
const parsedTestInput = TopographicMap.from(testInput);
const part1Test = part1(parsedTestInput);
Assert.isEqual(36, part1Test)

const parsedInput = TopographicMap.from(getInput(10));
const part1Result = part1(parsedInput);  // 4ms
Assert.isEqual(557, part1Result);

// Part2
function part2(input: TopographicMap): number {
    return timed(() => input.trailheads.map(trailhead => input.getRating(trailhead))
        .reduce((a, b) => a + b), `Part 2 with ${input.trailheads.length} trailheads`);
}

const part2Test = part2(parsedTestInput);
Assert.isEqual(81, part2Test);

const part2Result = part2(parsedInput); // 8ms
Assert.isEqual(1062, part2Result);
