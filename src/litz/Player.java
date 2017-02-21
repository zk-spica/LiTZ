package litz;

import java.io.Serializable;

class Player implements Serializable {
    static final int MAX_STYLE_NUMBER = 5;
    static final int MAX_PLAYER_NUMBER = Math.min(MAX_STYLE_NUMBER, Byte.MAX_VALUE + 1);
    byte clientIndex;
    volatile boolean isHuman = true;
    byte pieceStyleIndex;
    String name;
    Player(byte clientIdx, byte pieceStyleIdx, String playerName) {
        clientIndex = clientIdx;
        pieceStyleIndex = pieceStyleIdx;
        name = playerName;
    }
}
