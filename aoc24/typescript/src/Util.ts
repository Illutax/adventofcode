import { readFileSync } from "fs";

export class Util {
    public static getInput(day: number) {
        const dayString = day < 10 ? `0${day}`: `${day}`;
        const content = readFileSync(`../input/day${dayString}.txt`, 'utf-8')
        console.log(`Read ${content.length} characters`);
        return content;
    }
}

export function getInput(day: number)
{
    return Util.getInput(day);
}