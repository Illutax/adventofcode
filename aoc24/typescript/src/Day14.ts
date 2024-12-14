import { Assert } from "./Assert";
import { getInput, Util } from "./Util";
import { Vec2 } from "./Vec2";
import { Rect } from "./Rect";

const testInput = `p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
`;

class Robot {

    constructor(public readonly p: Vec2, public readonly v: Vec2) {
    }

    public static from(line: string) {
        if (!line) return null;
        const match = line.matchAll(/^p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)$/g).toArray()[0];
        const [_, x1, y1, x2, y2] = [...match]
        return new Robot(Vec2.of(+x1, +y1), Vec2.of(+x2, +y2));
    }

    public moveFor(seconds: number, space: Rect) {
        let p = this.p;
        for (let i = 0; i < seconds; i++) {
            p = p.plus(this.v)
        }

        return new Robot(p.mod(space.bottomRight), this.v);
    }
}

class Bathroom {
    private readonly quadrants: Rect[];

    constructor(public readonly space: Rect, public robots: Robot[]) {
        const halfWidth = Math.floor(space.bottomRight.x / 2);
        const halfHeight = Math.floor(space.bottomRight.y / 2);
        this.quadrants = [
            /*   I */ Rect.of(Vec2.ZERO, Vec2.of(halfWidth, halfHeight)),
            /*  II */ Rect.of(Vec2.of(halfWidth + 1, 0), Vec2.of(space.bottomRight.x, halfHeight)),
            /* III */ Rect.of(Vec2.of(0, halfHeight + 1), Vec2.of(halfWidth, space.bottomRight.y)),
            /*  VI */ Rect.of(Vec2.of(halfWidth + 1, halfHeight + 1), space.bottomRight),
        ];
    }

    public moveFor(seconds: number) {
        this.robots = this.robots.map(it => it.moveFor(seconds, this.space))
    }

    public calculateSafetyScore() {
        const robotPartition: Robot[][] = [];
        for (let robot of this.robots) {
            for (let i = 0; i < this.quadrants.length; i++) {
                if (this.quadrants[i].inBounds(robot.p)) {
                    if (!robotPartition[i]) {
                        robotPartition[i] = [];
                    }
                    robotPartition[i].push(robot);
                }
            }
        }
        return robotPartition.map(it => it.length).reduce((a, b) => a * b, 1)
    }

    public prettyPrint(char: string | undefined = undefined) {
        const message = this.prettyPrintInternal(char);
        console.log(message);
    }

    public prettyPrintInternal(char: string | undefined = undefined) {
        const amountOfRobotsPerCell: number[][] = []
        this.robots.forEach(robot => {
            const row = (amountOfRobotsPerCell[robot.p.y] || [])
            row[robot.p.x] = row[robot.p.x] + 1 || 1;
            amountOfRobotsPerCell[robot.p.y] = row;
        })

        const sb = [];
        for (let y = 0; y < this.space.bottomRight.y; y++) {
            const tmp = [];
            for (let x = 0; x < this.space.bottomRight.x; x++) {
                let c: string | number = ""
                if (char) {
                    c = (amountOfRobotsPerCell[y]?.[x] || 0) > 0 ? char : " ";
                } else {
                    c = (amountOfRobotsPerCell[y]?.[x] || " ");
                }
                tmp.push(c)
            }
            sb.push(tmp.join(""));
        }
        return sb.join("\n");
    }
}

function parse(input: string) {
    return input.split("\n")
        .map(line => Robot.from(line))
        .filter(it => it != null);
}

function part1(bathroomSize: Rect, robots: Robot[]): number {
    const bathroom = new Bathroom(bathroomSize, robots);
    bathroom.prettyPrint();
    bathroom.moveFor(100);
    bathroom.prettyPrint();
    return bathroom.calculateSafetyScore();
}

// Robot Test
const r = Robot.from("p=2,4 v=2,-3")!;
Assert.isEqual(Vec2.of(2, 4), r.p);
Assert.isEqual(Vec2.of(2, -3), r.v);
Assert.isEqual(Vec2.of(1, 3), r.moveFor(5, Rect.of(Vec2.of(11, 7))).p);

// Part1
const parsedTestInput = parse(testInput);
const part1Test = part1(Rect.of(Vec2.of(11, 7)), parsedTestInput);
Assert.isEqual(12, part1Test)

const parsedInput = parse(getInput(14));
console.log(parsedInput);
const part1Result = part1(Rect.of(Vec2.of(101, 103)), parsedInput);
Assert.isEqual(228410028, part1Result);

// Part2
function printEveryFrameToFile(bathroomSize: Rect, robots: Robot[])
{
    const bathroom = new Bathroom(bathroomSize, robots);
    const path = "./dontBlink.txt";
    Util.writeToFile(path, bathroom.prettyPrintInternal("█"));
    Util.appendToFile(path,"\n");
    for (let i = 0; i < 20_000; i++) {

        bathroom.moveFor(1);
        Util.appendToFile(path, i+"\n")
        Util.appendToFile(path, bathroom.prettyPrintInternal("█"));
        Util.appendToFile(path,"\n");
    }
}

function part2(bathroomSize: Rect, robots: Robot[]) {
    const bathroom = new Bathroom(bathroomSize, robots);
    bathroom.moveFor(8258);
    return bathroom.prettyPrintInternal("█")
}

const part2Result = part2(Rect.of(Vec2.of(101, 103)), parsedInput);
const expected = `                              █                                   █
                                                 █              █
                                                                        █
                                                                                      █  █

                   █                                                                           █
 █      █                  █
                                                                            █   █
                                                                         █


                                        █                                                         █
                                █                              █
                         █ █             █           █
                                  █
      █

                                                              █
                        █

                              █  █

                                                      █           █

                       █
            █  █                   █                                  █
          █                                                            █

                █
       █              █               ███████████████████████████████
               █                      █                             █            █
                  █                   █                             █ █
                                      █                             █
    █                                 █                             █
                                    █ █              █              █  █      █
                                      █             ███             █                    █
                                      █            █████            █
                                      █           ███████           █     █         █
                                      █          █████████          █                     █
                                      █            █████            █
                         █            █           ███████           █               █           █
                                      █          █████████          █
                                      █         ███████████         █                          █
                            █         █        █████████████        █
       █                              █          █████████          █
█                                 █   █         ███████████         █
                                      █        █████████████        █
                                      █       ███████████████       █ █
           █            █    █        █      █████████████████      █
               █                      █        █████████████        █
   █                                  █       ███████████████       █
                                  █   █      █████████████████      █
                                      █     ███████████████████     █
             █              █         █    █████████████████████    █
      █      █                        █             ███             █
                                      █             ███             █                        █
                                      █             ███             █
              █                       █                             █                         █
                                █     █                             █              █         █
                          █           █                             █
          █                  █   █    █                             █
                                      ███████████████████████████████
                                                                                                  █
                        █
                                            █
                                                      █
         █                                                    █
      █            █                                       █                  █


                                     █                                               █
                                                                            █
                                                             █
                                       █
                  █    █      █                                  █
              █
                      █                                                                           █
                                                                                                 █
                              █
                  █            █                                     █                        █  █
                █
                                                                      █    █



 █                                            █        █
                                                                                                █
   █                                 █                                   █      █
     █                                                          █       █   █
                      █
                                                            █
                                                                                    █
                                                        █            █  █

    █                     █                                    █                     █
                                                                             █
                                             █
             █
                                                       █               █
             █                                     █
                          ██                                                              █
                                              █
                                                                                                     
`;

Assert.isEqual(expected, part2Result);
