package litz;

import static litz.Player.MAX_STYLE_NUMBER;

import java.util.ArrayList;

class Game {
    Rule rule;
    Player[] players;
    Board board;
    private ArrayList<Vector> intervals;
    private ArrayList<Piece> history;
    Game (Setting setting) {
        rule = setting.rule;
        players = setting.players;/*new ArrayList<>();
        for (Player player : setting.players) {
            if (player != null) players.add(player);
        }*/
        board = new Board(setting.boardSize);
        buildIntervals();
        clearHistory();
        clearTmp();
    }
    private void buildIntervals() {
        intervals = new ArrayList<>();
        intervals.add(new Vector((byte)0, (byte)1));
        intervals.add(new Vector((byte)1, (byte)0));
        intervals.add(new Vector((byte)1, (byte)1));
        intervals.add(new Vector((byte)1, (byte)-1));
        if (rule.allowSlant) {
            byte maxInterval = (byte)((board.size - 1) / rule.connectNumber);
            for (byte i = 2; i <= maxInterval; ++i) {
                for (byte j = 1; j < i; ++j) {
                    if (GCD(i, j) == 1) {
                        intervals.add(new Vector(i, j));
                        intervals.add(new Vector(j, i));
                        intervals.add(new Vector(i, (byte)-j));
                        intervals.add(new Vector(j, (byte)-i));
                    }
                }
            }
        }
    }
    private int GCD(int a, int b) {
        // greatest common divisor
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }
    private void clearHistory() {
        history = new ArrayList<>();
    }
    private Connection[][][][] tmpConnections;
    private boolean tmpValid[][];
    private void clearTmp() {
    	tmpConnections = new Connection[board.size][board.size][][];
        tmpValid = new boolean[board.size][board.size];
        for (int i = 0; i < board.size; ++i) {
            for (int j = 0; j < board.size; ++j) {
                tmpValid[i][j] = false;
            }
        }
    }
    /*private */Connection[][] getConnections(Point position) {
        if (tmpValid[position.x][position.y]) return tmpConnections[position.x][position.y];
        Connection[][] result = new Connection[MAX_STYLE_NUMBER][];
        for (byte pieceStyleIndex = 0; pieceStyleIndex < MAX_STYLE_NUMBER; ++pieceStyleIndex) {
            result[pieceStyleIndex] = getPieceConnections(position, pieceStyleIndex);
        }
        tmpConnections[position.x][position.y] = result;
        tmpValid[position.x][position.y] = true;
        return result;
    }
    private Connection[] getPieceConnections(Point position, byte pieceStyleIndex) {
        if (tmpValid[position.x][position.y]) return tmpConnections[position.x][position.y][pieceStyleIndex];
        ArrayList<Connection> pieceResult = new ArrayList<>();
        for (Vector direction : intervals) {
            Connection directionResult = new Connection();
            int step = 1;
            step += fillConnection(directionResult, position, direction, pieceStyleIndex);
            step += fillConnection(directionResult, position, direction.reverse(), pieceStyleIndex);
            if ((directionResult.connected.size() > 0 || directionResult.separated.size() > 0) && step >= rule.connectNumber) {
                pieceResult.add(directionResult);
            }
        }
        if (pieceResult.isEmpty()) return null;
        Connection[] r = new Connection[pieceResult.size()];
        for (int i = 0; i < r.length; ++i) {
        	r[i] = pieceResult.get(i);
        }
        return r;
    }
    private byte fillConnection(Connection connection, Point position, Vector direction, byte pieceStyleIndex) {
        boolean connected = true;
        byte blankNumber = 0;
        byte step = 0;
        ArrayList<Point> blankTmp = new ArrayList<>();
        for (Point pos = position.move(direction); board.isValid(pos); pos = pos.move(direction)) {
            if (board.isBlank(pos)) {
                connected = false;
                if (++blankNumber > rule.connectNumber - 2) {
                    break;
                }
                blankTmp.add(pos);
            } else if (board.get(pos) == pieceStyleIndex) {
                if (connected) {
                    connection.connected.add(pos);
                } else {
                    connection.separated.add(pos);
                    if (!blankTmp.isEmpty()) {
                    	connection.blank.addAll(blankTmp);
                    	blankTmp.clear();
                    }
                }
                blankNumber = 0;
            } else {
                break;
            }
            ++step;
        }
        if (!blankTmp.isEmpty()) {
        	connection.blankOut.addAll(blankTmp);
        	blankTmp.clear();
        	blankTmp = null;
        }
        return step;
    }
    private byte maxConnectedNumber(Connection[] connections) {
        byte max = 0;
        if (connections != null) {
	        for (Connection connection : connections) {
	            byte tmp = (byte)connection.connected.size();
	            if (tmp > max) {
	                max = tmp;
	            }
	        }
    	}
        return (byte)(max + 1);
    }
    byte getPlayerNumber() {
        byte cnt = 0;
        for (Player player : players) {
            if (player != null) ++cnt;
        }
        return cnt;
    }
    boolean canPlace(Piece piece) {
        return board.isBlank(piece.position) && (rule.allowOverline || maxConnectedNumber(getPieceConnections(piece.position, piece.styleIndex)) <= rule.connectNumber);
    }
    void place(Piece piece) {
        board.place(piece);
        history.add(piece);
        clearTmp();
    }
    void undo() {
        if (!history.isEmpty()) {
            board.remove(history.remove(history.size() - 1));
        }
        clearTmp();
    }
    private boolean isWinnon(Point position, byte pieceStyleIndex) {
        byte num = maxConnectedNumber(getPieceConnections(position, pieceStyleIndex));
        return num == rule.connectNumber || (rule.allowOverline && num > rule.connectNumber);
    }
    private int[] countWinnon() {
        int[] cnt = new int[MAX_STYLE_NUMBER + 1];
        int sum = 0;
        for (byte x = 0; x < board.size; ++x) {
            for (byte y = 0; y < board.size; ++y) {
            	if (!board.isBlank(new Point(x, y))) continue;
                boolean isWinnonXY = false;
                for (byte pieceStyleIndex = 0; pieceStyleIndex < MAX_STYLE_NUMBER; ++pieceStyleIndex) {
                    if (isWinnon(new Point(x, y), pieceStyleIndex)) {
                        ++cnt[pieceStyleIndex];
                        isWinnonXY = true;
                    }
                }
                if (isWinnonXY) {
                    ++sum;
                }
            }
        }
        cnt[MAX_STYLE_NUMBER] = sum;
        return cnt;
    }
    boolean win(Point position) {
    	if (isWinnon(position, board.get(position))) {
    		return true;
    	} else if (!rule.winByConnect) {
            //getConnections(position);
            int[] countResult = countWinnon();
            return countResult[MAX_STYLE_NUMBER] >= getPlayerNumber() /*&& countResult[board.get(position)] >= 2*/;
        } else {
        	return false;
        }
    }
}
