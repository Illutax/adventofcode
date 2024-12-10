import { Vec2 } from "./Vec2";

export class CardinalDirection {
    public static readonly NORTH = new CardinalDirection(Vec2.of(0, -1));
    public static readonly EAST = new CardinalDirection(Vec2.of(+1, 0));
    public static readonly SOUTH = new CardinalDirection(Vec2.of(0, +1));
    public static readonly WEST = new CardinalDirection(Vec2.of(-1, 0));

    public static readonly ALL = [this.NORTH, this.EAST, this.SOUTH, this.WEST];

    constructor(public readonly value: Vec2) {
    }

    public static from(value: string) {
        switch (value) {
            case '^':
                return this.NORTH;
            case '>':
                return this.EAST;
            case 'v':
                return this.SOUTH;
            case '<':
                return this.WEST;
            default:
                throw new Error(`Unknown value ${value}`);
        }
    }
}