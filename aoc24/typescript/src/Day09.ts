import { Assert } from "./Assert";
import { getInput, timed } from "./Util";

const testInput = `2333133121414131402
`;

class Disk {
    public readonly amountOfFreeBlocks: number;

    constructor(public blocks: DiskBlock[]) {
        this.amountOfFreeBlocks = blocks.filter(block => block instanceof FreeSpaceBlock).length;
    }

    public static form(input: string) {
        const parts = input.slice(0, input.length-1).split("").map(Number);
        const blocks: DiskBlock[] = []
        Assert.isEqual(1, parts.length % 2, `Expect ${parts.length} to be odd`);
        let count = 0;
        for (let i = 0; i < parts.length; i += 2) {
            const fileId = count++;
            const fileSize = parts[i];
            const freeSpaceSize = parts[i + 1] ?? 0; // The last entry is not followed by a space
            blocks.push(new FileBlock(fileId, fileSize))
            if (freeSpaceSize > 0)
                blocks.push(new FreeSpaceBlock(freeSpaceSize));
        }
        return new Disk(blocks);
    }

    public get size(): number {
        return this.blocks.length;
    }

    public get filesystemChecksum() {
        let sum = 0;
        let index = 0;
        for (let diskBlock of this.blocks) {
            const fileBlock = diskBlock as FileBlock;
            for (let i = 0; i < fileBlock.size; i++, index++) {
                if (diskBlock instanceof FreeSpaceBlock)
                    continue;
                // console.log(sum, fileBlock.id, index, fileBlock.id * index);
                sum += fileBlock.id * index;
            }
        }
        return sum;
    }

    public prettyPrint(big: boolean = false): void {
        const sb: string[] = []
        for (const block of this.blocks) {
            sb.push(block.idString.repeat(block.size));
        }
        console.log(sb.join(big ? "," : ""));
    }

    public fragment() {
        const freeBlockIndex = this.getFirstFreeSpaceBlockIndexAfter();
        const fileBlockIndex = this.getLastFileBlockIndex();
        const freeBlock = this.blocks[freeBlockIndex] as FreeSpaceBlock;
        const fileBlock = this.blocks[fileBlockIndex] as FileBlock;
        const indexOfLastElement = this.blocks.length - 1;
        // "00...1333"
        //    |   |
        //    |   ^- FileBlock
        //    ^- FreeBlock
        if (freeBlock.size == fileBlock.size) {
            // "00...1333" -> "003331..."
            this.blocks[freeBlockIndex] = fileBlock; // switcheroo
            this.remove(fileBlockIndex);
            this.addFreeBlockAtIndex(freeBlock, indexOfLastElement);
        } else if (fileBlock.size < freeBlock.size) {
            // "00....1333" -> "00333.1..."
            const remainingInfixFreeSpace = freeBlock.size - fileBlock.size;
            this.blocks[freeBlockIndex] = fileBlock; // insert the whole FileBlock
            this.remove(fileBlockIndex); // remove the FileBlock and only then                     v
            this.insert(freeBlockIndex + 1, new FreeSpaceBlock(remainingInfixFreeSpace)); // append the remaining space after that
            const remainingSpaceAtEnd = freeBlock.size - remainingInfixFreeSpace;
            this.addFreeBlockAtIndex(new FreeSpaceBlock(remainingSpaceAtEnd), indexOfLastElement);
        } else { // fileBlock > freeBlock
            // "00..1333" -> "003313..."
            const remainingFileSizeToRemainAtEnd = fileBlock.size - freeBlock.size;
            this.blocks[freeBlockIndex] = new FileBlock(fileBlock.id, freeBlock.size); // insert fitting part of FileBlock
            fileBlock.size = remainingFileSizeToRemainAtEnd;
            this.addFreeBlockAtIndex(freeBlock, indexOfLastElement);
        }
    }

    public defragment(printAfterEveryBlock = false) {
        for (let fileBlockIndex = this.blocks.length - 1; fileBlockIndex >= 0; fileBlockIndex--) {
            const diskBlock = this.blocks[fileBlockIndex];
            if (diskBlock instanceof FreeSpaceBlock) continue;
            const fileBlock = diskBlock as FileBlock;
            for (let freeBlockIndex = 0; freeBlockIndex < this.blocks.length; freeBlockIndex++) {
                const otherDiskBlock = this.blocks[freeBlockIndex];
                if (otherDiskBlock instanceof FileBlock) continue;
                const freeBlock = otherDiskBlock as FreeSpaceBlock;
                if (freeBlock.size >= fileBlock.size && fileBlockIndex > freeBlockIndex) {
                    this.swap(freeBlockIndex, fileBlockIndex)
                    break; // just once
                }
            }
            if (printAfterEveryBlock)
                this.prettyPrint(this.amountOfFreeBlocks > 10);
        }
    }

    private swap(freeBlockIndex: number, fileBlockIndex: number) {
        const freeBlock = this.blocks[freeBlockIndex] as FreeSpaceBlock;
        const fileBlock = this.blocks[fileBlockIndex] as FileBlock;
        // "00...1333"
        //    |   |
        //    |   ^- FileBlock
        //    ^- FreeBlock
        if (freeBlock.size == fileBlock.size) {
            // "00...1333" -> "003331..."
            this.blocks[freeBlockIndex] = fileBlock; // switcheroo
            this.blocks[fileBlockIndex] = freeBlock; // switcheroo
            // this.remove(fileBlockIndex);
            // this.addFreeBlockAtIndex(freeBlock, fileBlockIndex);
        } else if (fileBlock.size < freeBlock.size) {
            // "00....1333" -> "00333.1..."
            const remainingInfixFreeSpace = freeBlock.size - fileBlock.size;
            this.blocks[freeBlockIndex] = fileBlock; // replace the FreeBlock with the whole FileBlock
            this.remove(fileBlockIndex); // remove the FileBlock at the end and only               v
            this.insert(freeBlockIndex + 1, new FreeSpaceBlock(remainingInfixFreeSpace)); // pad the newly inserted fileBlock with the proper FreeSpace after that
            const remainingSpaceAtEnd = freeBlock.size - remainingInfixFreeSpace;
            this.addFreeBlockAtIndex(new FreeSpaceBlock(remainingSpaceAtEnd), fileBlockIndex);
        } else { // fileBlock > freeBlock
            console.log(fileBlock, freeBlock);
            throw new Error("Tried to swap bigger file with smaller gap");
        }
    }

    private insert(index: number, block: DiskBlock) {
        this.blocks = this.blocks.slice(0, index).concat(block, this.blocks.slice(index));
    }

    private remove(index: number) {
        this.blocks = this.blocks.slice(0, index).concat(this.blocks.slice(index + 1));
    }

    private getLastFileBlockIndex(): number {
        return this.blocks.findLastIndex(it => it instanceof FileBlock)!;
    }

    private getFirstFreeSpaceBlockIndexAfter(): number {
        return this.blocks.findIndex(it => it instanceof FreeSpaceBlock)!;
    }

    private addFreeBlockAtIndex(freeBlock: FreeSpaceBlock, index = this.size - 1) {
        const lastBlock = this.blocks[index]!;
        if (lastBlock instanceof FreeSpaceBlock) {
            lastBlock.merge(freeBlock);
        } else {
            this.blocks.push(freeBlock);
        }
    }
}

interface DiskBlock {
    get idString(): string;

    get size(): number;

    set size(newSize);
}

class FileBlock implements DiskBlock {
    constructor(public readonly id: number,
                public size: number) {
        Assert.isGreaterThan(size, 0);
    }

    public get idString() {
        return `${this.id}`;
    }
}

class FreeSpaceBlock implements DiskBlock {
    constructor(public size: number) {
        Assert.isGreaterThan(size, -1);
    }

    public merge(other: FreeSpaceBlock) {
        this.size += other.size;
    }

    public get idString(): string {
        return ".";
    }
}

function part1(input: Disk) {
    console.log(`Defragmenting for ${input.amountOfFreeBlocks} steps`);
    return timed(() => {
        for (let i = 1; i < input.amountOfFreeBlocks; i++) {
            // input.prettyPrint();
            input.fragment();
        }
        // input.prettyPrint(input.amountOfFreeBlocks>10);
        return input.filesystemChecksum
    }, `Part1 with ${input.size} blocks total`);
}

// Part1
const part1Test = part1(timed(() => Disk.form(testInput), "parsing testinput"));
Assert.isEqual(1928, part1Test)

const part1Result = part1(Disk.form(getInput(9)));
Assert.isEqual(6_320_029_754_031, part1Result);

// Part2
function part2(input: Disk): number {
    input.prettyPrint(input.amountOfFreeBlocks > 10);
    timed(() => input.defragment(false), `Part2 with ${input.size} blocks total`);
    input.prettyPrint(input.amountOfFreeBlocks > 10);
    return input.filesystemChecksum;
}

const part2Test = part2(timed(() => Disk.form(testInput), "parsing testinput"));
Assert.isEqual(2858, part2Test);

const part2Result = part2(Disk.form(getInput(9)));
Assert.isGreaterThan(part2Result, 6342995049780);
Assert.isSmallerThan(part2Result, 6404714266860);
Assert.isEqual(-1, part2Result);
