import { PosAndDir } from "./PosDir";
import { Vec2 } from "./Vec2";
import { Assert } from "./Assert";

const v1 = PosAndDir.of(Vec2.of(0,1), Vec2.of(1,2))
const v2 = PosAndDir.of(Vec2.of(0,1), Vec2.of(1,2))

const v3 = PosAndDir.of(Vec2.of(1,0), Vec2.of(1,2))
const v4 = PosAndDir.of(Vec2.of(0,1), Vec2.of(2,1))
const v5 = PosAndDir.of(Vec2.of(2,1), Vec2.of(0,1))

Assert.isEqual(v1,v2);
Assert.isIdentical(v1,v2);
Assert.isTrue(v1.equals(v2), "Are equal")
Assert.isFalse(v1.equals(v3), "Are equal")

Assert.isNotEqual(v1,v3);
Assert.isNotEqual(v1,v4);
Assert.isNotEqual(v1,v5);
Assert.isNotEqual(v3,v4);
Assert.isNotEqual(v4,v5);
