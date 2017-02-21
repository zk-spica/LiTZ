package litz;

import java.io.Serializable;

class Piece implements Serializable {
    final Point position;
    final byte styleIndex;
    Piece(Point position, byte pieceStyleIndex) {
        this.position = position;
        this.styleIndex = pieceStyleIndex;
    }
}
