import { Assert } from "./Assert";
import { getInput } from "./Util";

const testInput = `47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47`;

class PageOrderRule {
    constructor(public x: number, public y: number) {
    }

    equals(other: PageOrderRule) {
        return this.x == other.x && this.y == other.y;
    }
}

class PageUpdate {
    constructor(public values: number[]) {
    }

    public get middlePageNumber() {
        return this.values[Math.floor(this.values.length / 2)];
    }
}

class PrintQueue {
    constructor(public rules: PageOrderRule[], public updates: PageUpdate[]) {
    }

    public part1() {
        return this.updates.filter(update => this.isValid(update))
            .map(update => update.middlePageNumber)
            .reduce((a, b) => a + b, 0)
    }

    public part2() {
        return this.updates.filter(update => !this.isValid(update))
            .map(update => this.reorder(update))
            .map(update => update.middlePageNumber)
            .reduce((a, b) => a + b, 0)
    }

    private reorder(unorderedPage: PageUpdate): PageUpdate {
        const incorrectOrder = [...unorderedPage.values];
        const correctOrder: number[] = []
        while (incorrectOrder.length > 0) {
            const currentPage = incorrectOrder.shift()!
            const matches = incorrectOrder.every(followingPage => this.containsRule(new PageOrderRule(currentPage, followingPage)))
            if (matches) {
                correctOrder.push(currentPage);
            } else {
                incorrectOrder.push(currentPage);
            }
        }
        const reorderedPageUpdate = new PageUpdate(correctOrder);
        Assert.isTrue(this.isValid(reorderedPageUpdate), `expect ${JSON.stringify(reorderedPageUpdate)} to be valid`);
        return reorderedPageUpdate;
    }

    private isValid(pageUpdate: PageUpdate): boolean {
        const values = pageUpdate.values;
        const len = values.length;
        for (let i = 0; i < len - 1; i++) {
            const currentPage = values[i];
            for (let j = i + 1; j < len; j++) {
                const pageToCheck = values[j];
                const breakingThePageOrder = new PageOrderRule(pageToCheck, currentPage);
                if (this.containsRule(breakingThePageOrder))
                    return false;
            }
        }
        return true;
    }

    private containsRule(ruleToFind: PageOrderRule): boolean {
        return this.rules.find(rule => rule.equals(ruleToFind)) != undefined
    }
}

function parse(input: string): PrintQueue {
    const [rules, updates] = input.split("\n\n")

    const pageRuleOrders = rules.split("\n").map(line => {
        const match = line.match(/^(\d+)\|(\d+)$/);
        const [_, x, y] = match!.map(Number);
        return new PageOrderRule(x, y);
    })

    const pageUpdates = updates.split("\n").map(line => {
        const values = line.split(",").map(Number);
        return new PageUpdate(values);
    })

    console.log(`Parsed ${pageRuleOrders.length} rules and ${pageRuleOrders.length} updates`);
    return new PrintQueue(pageRuleOrders, pageUpdates);
}

// Part1
const parsedTestResult = parse(testInput);
const part1Test = parsedTestResult.part1();
Assert.isEqual(143, part1Test)

const parsedResult = parse(getInput(5));
const part1Result = parsedResult.part1();
Assert.isEqual(6041, part1Result);

// // Part2
const part2Test = parsedTestResult.part2();
Assert.isEqual(123, part2Test);

const part2Result = parsedResult.part2();
Assert.isEqual(4884, part2Result);
