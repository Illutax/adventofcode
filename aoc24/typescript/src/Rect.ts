import { Vec2 } from "./Vec2";

export class Rect {
    public readonly topLeft: Vec2;
    public readonly bottomRight: Vec2;

    private constructor(public readonly x1: number,
                        public readonly x2: number,
                        public readonly y1: number,
                        public readonly y2: number) {
        this.topLeft = Vec2.of(x1, y1);
        this.bottomRight = Vec2.of(x2, y2);
    }

    public static of(topLeft: Vec2, bottomRight: Vec2) {
        return new Rect(topLeft.x, bottomRight.x, topLeft.y, bottomRight.y);
    }

    public static from(x1: number, x2: number, y1: number, y2: number) {
        return new Rect(x1, x2, y1, y2);
    }

    public inBounds(pos: Vec2) {
        return pos.x >= this.x1 && pos.x < this.x2 &&
            pos.y >= this.y1 && pos.y < this.y2;
    }
}