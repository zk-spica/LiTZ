package litz;

import static litz.Player.MAX_PLAYER_NUMBER;

import java.io.Serializable;

class Setting implements Serializable {
    Rule rule;
    Player[] players;
    byte boardSize;
    public Setting() {
        this.rule = new Rule();
        this.players = new Player[MAX_PLAYER_NUMBER];
        this.boardSize = (byte)(rule.connectNumber * 4);
    }
}
