package litz;

import java.io.Serializable;

class Vector implements Serializable {
    final byte dx;
    final byte dy;
    Vector(byte dx, byte dy) {
        this.dx = dx;
        this.dy = dy;
    }
    Vector reverse() {
        return new Vector((byte)-dx, (byte)-dy);
    }
    Vector reverseX() {
        return new Vector((byte)-dx, dy);
    }
    Vector reverseY() {
        return new Vector(dx, (byte)-dy);
    }
    Vector copy() {
        return new Vector(dx, dy);
    }
}
