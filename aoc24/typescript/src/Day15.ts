import { Assert } from "./Assert";
import { getInput } from "./Util";
import { CardinalDirection } from "./CardinalDirection";
import { Vec2 } from "./Vec2";
import { Rect } from "./Rect";

const testInput = `##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
`;

const enum CellType {
    EMPTY = ".",
    ROBOT = "@",
    BOX = "O",
    WALL = "#"
}

class Cell {
    private constructor(public type: CellType, public readonly pos: Vec2) {
    }

    public static from(cell: string, pos: Vec2) {
        let type: CellType;
        switch (cell) {
            case CellType.ROBOT:
                type = CellType.ROBOT;
                break;
            case CellType.WALL:
                type = CellType.WALL;
                break;
            case CellType.BOX:
                type = CellType.BOX;
                break;
            case CellType.EMPTY:
                type = CellType.EMPTY;
                break;
            default:
                throw new Error(`Unexpected CellType: ${cell}`);
        }
        return new Cell(type, pos);
    }

    toString() {
        return `{${this.type}, ${this.pos.toString()}}`
    }
}

class Warehouse {
    private readonly boundries: Rect;

    private readonly robotStartingCell: Cell;

    constructor(private readonly cells: Cell[][], private readonly directions: CardinalDirection[]) {
        this.robotStartingCell = cells.flatMap(row => row.filter(cell => cell.type == CellType.ROBOT)!)[0]!;
        this.boundries = Rect.of(Vec2.of(cells[0].length, cells.length));
    }

    public part1() {
        let robot = this.robotStartingCell;
        for (const direction of this.directions) {
            robot = this.tryMove(robot, direction);
            // this.prettyPrint();
        }
        this.prettyPrint();
        return this.cells.flatMap(row => row
            .filter(cell => cell.type == CellType.BOX)
            .map(cell => this.gpsCoordinate(cell)))
            .reduce((a, b) => a + b);
    }

    private tryMove(cell: Cell, direction: CardinalDirection): Cell {
        const targetCellPos = cell.pos.plus(direction.value);
        let targetCell = this.cells[targetCellPos.y][targetCellPos.x];
        if (targetCell.type == CellType.BOX) {
            targetCell = this.tryMove(targetCell, direction);
        }
        if (targetCell.type == CellType.EMPTY) {
            targetCell.type = cell.type;  // LET'S GO!
            cell.type = CellType.EMPTY;
            return targetCell.type == CellType.ROBOT ? targetCell : cell;
        }
        return cell; // FBI: STAY RIGHT WHERE YOU ARE
    }

    private gpsCoordinate(cell: Cell) {
        return cell.pos.y * 100 + cell.pos.x;
    }

    public prettyPrint() {
        const sb = []
        for (let y = 0; y < this.cells.length; y++) {
            const tmp: string[] = []
            for (let x = 0; x < this.cells[y].length; x++) {
                const cell = this.cells[y][x]
                tmp.push(cell.type);
            }
            sb.push(tmp.join(""));
        }
        console.log(sb.join("\n") + "\n");
    }
}

function parse(input: string): Warehouse {
    const [map, moves] = input.split("\n\n");
    console.log(map, moves);
    const directions = moves.split("\n")
        .flatMap(line => line.split("")
            .map(m => CardinalDirection.from(m)))
    const cells = map.split("\n")
        .map((line, y) => line.split("")
            .map((cell, x) =>
                Cell.from(cell, Vec2.of(x, y))));
    return new Warehouse(cells, directions);
}

function part1(input: Warehouse): number {
    input.prettyPrint();
    const result = input.part1();
    input.prettyPrint();
    return result;
}

// Part1
const parsedTestInput = parse(testInput);
const part1Test = part1(parsedTestInput);
Assert.isEqual(10092, part1Test)

const parsedInput = parse(getInput(15));
const part1Result = part1(parsedInput);
Assert.isEqual(-1, part1Result);

// // Part2
// function part2(input: Warehouse): number {
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
