package litz;

import java.io.Serializable;

class Rule implements Serializable {
    byte connectNumber = 5;
    boolean winByConnect = true;
    boolean allowOverline = true;
    boolean allowSlant = true;
    Rule() {
    }
    Rule(byte connectNumber, boolean winByConnect, boolean allowOverline, boolean allowSlant) {
        this.connectNumber = connectNumber;
        this.winByConnect = winByConnect;
        this.allowOverline = allowOverline;
        this.allowSlant = allowSlant;
    }
}
