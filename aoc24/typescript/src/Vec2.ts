export class Vec2 {
    private static readonly cache = new Map<string, Vec2>();
    public static ZERO = Vec2.of(0, 0);

    readonly x: number;
    readonly y: number;

    private constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    static of(x: number, y: number) {
        const key = `${x}|${y}`;
        if (!this.cache.has(key)) {
            this.cache.set(key, new Vec2(x, y))
        }
        return this.cache.get(key)!;
    }

    plus(dir: Vec2) {
        return Vec2.of(this.x + dir.x, this.y + dir.y);
    }

    minus(dir: Vec2) {
        return Vec2.of(this.x - dir.x, this.y - dir.y);
    }

    mult(factor: number) {
        return Vec2.of(this.x * factor, this.y * factor)
    }

    div(dividend: number) {
        if (dividend === 0) throw new Error("Can't divide by 0!");
        return Vec2.of(this.x / dividend, this.y / dividend);
    }

    rotateCW() {
        // noinspection JSSuspiciousNameCombination NO, STUPID INTELLIJ!
        return Vec2.of(-this.y, this.x);
    }

    toString() {
        return `(${this.x}; ${this.y})`;
    }

    equals(other: Vec2) {
        return this === other ||
            this.x === other.x && this.y === other.y;
    }

    mod(rect: Vec2) {
        let x = this.x % rect.x;
        let y = this.y % rect.y;
        if (x < 0) {
            x += rect.x;
        }
        if (y < 0) {
            y += rect.y;
        }
        return Vec2.of(x, y);
    }
}

export function of(x: number, y: number) {
    return Vec2.of(x, y);
}