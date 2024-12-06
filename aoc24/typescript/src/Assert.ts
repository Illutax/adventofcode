export class Assert {
    public static isTrue(expression: boolean, message: string) {
        if (!expression) throw new Error(`Assertion failed! Expected: Expression to be true. ${message}`);
    }

    public static isEqual(expected: number, actual: number) {
        if (expected != actual) throw new Error(`Assertion failed! Expected: ${expected} to be equal to ${actual}`);
    }

    public static isNotEqual(expected: number, actual: number) {
        if (expected == actual) throw new Error(`Assertion failed! Expected: ${expected} to be not equal to ${actual}`);
    }

    public static isGreaterThan(actual: number, threshold: number) {
        if (actual === threshold) throw new Error(`Assertion failed! Expected: ${actual} to be greater than ${threshold}, but it was equal!`);
        if (actual < threshold) throw new Error(`Assertion failed! Expected: ${actual} to be greater than ${threshold}`);
    }

    public static isSmallerThan(actual: number, threshold: number) {
        if (actual === threshold) throw new Error(`Assertion failed! Expected: ${actual} to be less than ${threshold}, but it was equal!`);
        if (actual > threshold) throw new Error(`Assertion failed! Expected: ${actual} to be less than ${threshold}`);
    }
}