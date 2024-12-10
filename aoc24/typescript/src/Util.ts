import { readFileSync } from "fs";

export class Util {
    public static getInput(day: number) {
        const dayString = day < 10 ? `0${day}` : `${day}`;
        const content = readFileSync(`../input/day${dayString}.txt`, 'utf-8')
        console.log(`Read ${content.length} characters`);
        return content;
    }

    public static timed<T>(callback: () => T, label: string | undefined = undefined) {
        const start = performance.now();
        const result = callback();
        const end = performance.now();
        const prefix = label ? label + ": " : "";
        const deltaMS = (end - start);
        let deltaString = ""
        if (deltaMS > 60_000) {
            deltaString = `${(deltaMS / 60_000)}m`;
        } else if (deltaMS > 100) {
            deltaString = `${(deltaMS / 1000)}s`;
        } else if (deltaMS < 1) {
            deltaString = `${deltaMS*1000}µs`;
        } else {
            deltaString = `${deltaMS}ms`;
        }
        console.log(`${prefix}took ` + deltaString);
        return result;
    }
}

export function getInput(day: number) {
    return Util.getInput(day);
}


/**
 *
 * @param map
 * @param key
 * @param value
 */
export function addIfAbsent(map: Map<string, object[]>, key: string, value: object) {
    map.set(key, [...(map.get(key) || []), value]);
}

export function timed<T>(callback: () => T, label: string | undefined = undefined) {
    return Util.timed(callback, label);
}