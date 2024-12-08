import { Assert } from "./Assert";
import { Vec2 } from "./Vec2";

const v0 = Vec2.of(0,0);
const v1 = Vec2.of(0,1);
const v2 = Vec2.of(0,1);
const v3 = Vec2.of(1,1);

Assert.isEqual(v0, v0.mult(-1))
Assert.isEqual(v1,v2);
Assert.isIdentical(v1,v2);
Assert.isTrue(v1.equals(v2), "Are equal")
Assert.isFalse(v1.equals(v3), "Are equal")

Assert.isNotEqual(v1,v3);