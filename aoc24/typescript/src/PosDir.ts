import { Vec2 } from "./Vec2";

export class PosAndDir {
    private static readonly cache = new Map<string, PosAndDir>();

    private constructor(public readonly pos: Vec2, public readonly dir: Vec2) {
    }

    public static of(pos: Vec2, dir: Vec2) {
        const cacheKey = `${pos.x},${pos.y}|${dir.x},${dir.y}`;
        if (!this.cache.has(cacheKey)) {
            this.cache.set(cacheKey, new PosAndDir(pos, dir));
        }
        return this.cache.get(cacheKey)!;
    }

    equals(other: PosAndDir): boolean {
        return this === other || this.pos.equals(other.pos) && this.dir.equals(other.dir);
    }
}