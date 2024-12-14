import { Assert } from "./Assert";
import { Vec2 } from "./Vec2";

const v0 = Vec2.of(0, 0);
const v1 = Vec2.of(0, 1);
const v2 = Vec2.of(0, 1);
const v3 = Vec2.of(1, 1);

Assert.isEqual(v0, v0.mult(-1))
Assert.isEqual(v1, v2);
Assert.isIdentical(v1, v2);
Assert.isTrue(v1.equals(v2), `Expected ${v1} and ${v2} to be equal`)
Assert.isFalse(v1.equals(v3), `Expected ${v1} and ${v3} to be NOT equal`)

Assert.isNotEqual(v1, v3);

Assert.isEqual(Vec2.of(-2, -7).mod(Vec2.of(3, 5)), Vec2.of(1,3));
Assert.isEqual(Vec2.of(-5, -12).mod(Vec2.of(3, 5)), Vec2.of(1,3));
Assert.isEqual(Vec2.of(3, 12).mod(Vec2.of(3, 5)), Vec2.of(0,2));