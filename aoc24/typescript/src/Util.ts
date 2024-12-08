import { readFileSync } from "fs";

export class Util {
    public static getInput(day: number) {
        const dayString = day < 10 ? `0${day}`: `${day}`;
        const content = readFileSync(`../input/day${dayString}.txt`, 'utf-8')
        console.log(`Read ${content.length} characters`);
        return content;
    }

    public static timed(callback: () => void, label: string | undefined = undefined) {
        const start = new Date().getTime();
        callback();
        const end = new Date().getTime();
        const deltaSeconds = (end-start)/1000;

        const prefix = label? label+": " : "";
        console.log(`${prefix}took ${deltaSeconds}s`);
    }
}

export function getInput(day: number)
{
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

export function timed(callback: () => void, label: string | undefined) {
    return Util.timed(callback, label);
}