package litz;

import java.io.Serializable;

class Point implements Serializable {
    final byte x;
    final byte y;
    Point(byte x, byte y) {
        this.x = x;
        this.y = y;
    }
    Point move(Vector displacement) {
        return new Point((byte)(x + displacement.dx), (byte)(y + displacement.dy));
    }
    Point moveX(byte displacementX) {
        return new Point((byte)(x + displacementX), y);
    }
    Point moveY(byte displacementY) {
        return new Point(x, (byte)(y + displacementY));
    }
    Point copy() {
        return new Point(x, y);
    }
}
