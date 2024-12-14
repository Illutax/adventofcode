import { readFileSync } from "fs";
import { appendFileSync, writeFileSync } from "node:fs";

export class Util {
    public static getInput(day: number) {
        const dayString = day < 10 ? `0${day}` : `${day}`;
        const content = readFileSync(`../input/day${dayString}.txt`, 'utf-8')
        console.log(`Read ${content.length} characters`);
        return content;
    }

    public static appendToFile(filePath: string, data: string) {
        appendFileSync(filePath, data);
    }

    public static writeToFile(filePath: string, data: string) {
        writeFileSync(filePath, data);
    }

    public static timed<T>(callback: () => T, label: string | undefined = undefined) {
        const start = performance.now();
        const result = callback();
        const end = performance.now();
        const prefix = label ? label + ": " : "";
        const deltaMS = (end - start);
        let deltaString: string
        if (deltaMS > 60_000) {
            deltaString = `${(deltaMS / 60_000)}m`;
        } else if (deltaMS > 100) {
            deltaString = `${(deltaMS / 1000)}s`;
        } else if (deltaMS < 1) {
            deltaString = `${deltaMS * 1000}µs`;
        } else {
            deltaString = `${deltaMS}ms`;
        }
        console.log(`${prefix}took ` + deltaString);
        return result;
    }

    public static setOf<T>(...values: T[]) {
        const set = new Set<T>();
        values.forEach(value =>
            set.add(value));
        return set;
    }

    public static emptySet<T>() {
        return this.setOf<T>();
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
export function addIfAbsent<K, V>(map: Map<K, V[]>, key: K, value: V) {
    map.set(key, [...(map.get(key) || []), value]);
}

export function computeIfAbsent<K, V>(map: Map<K, V>, key: K, producer: () => V) {
    const existingValue = map.get(key);
    if (existingValue)
        return existingValue;

    const value = producer();
    map.set(key, value);
    return value;
}

export function compute<K,V>(map: Map<K,V>, key: K, remappingFunction: (_: K, __: V) => V, fallBackValue: V|undefined = undefined) {
    const oldValue = (map.get(key) || fallBackValue)!;
    const newValue = remappingFunction(key, oldValue);
    map.set(key,newValue);
    return newValue;
}

export function timed<T>(callback: () => T, label: string | undefined = undefined) {
    return Util.timed(callback, label);
}

export function partition<T>(array: T[], size: number): T[][] {
    const output: T[][] = [];
    for (var i = 0; i < array.length; i += size)
    {
        output[output.length] = array.slice(i, i + size);
    }
    return output;
}

export function partition2<T>(array: T[], size: number): T[][] {
    return array.length ? [array.splice(0, size)].concat(partition(array, size)) : [];
}