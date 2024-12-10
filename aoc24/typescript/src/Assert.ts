export class Assert {
    public static isTrue(expression: boolean, message: string) {
        if (!expression) throw new Error(`Assertion failed! Expected: Expression to be true. ${message}`);
    }

    public static notNull(value: object | undefined | null, name: string | undefined) {
        if (value == null) throw new Error(`Expected Name to be set ${name}`);
    }

    public static isEqual(expected: number | object, actual: number | object, message = "") {
        if (expected != actual) throw new Error(`Assertion failed! Expected: ${expected} to be equal to ${actual} ${message}`);
    }

    public static isIdentical(expected: number | object, actual: number | object) {
        if (expected !== actual) throw new Error(`Assertion failed! Expected: ${expected} to be equal to ${actual}`);
    }

    public static isNotEqual(expected: number | object, actual: number | object) {
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

    static isFalse(expression: boolean, message: string) {
        if (expression) throw new Error(`Assertion failed! Expected: Expression to be true. ${message}`);
    }
}