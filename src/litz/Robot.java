package litz;

import static litz.Board.BLANK;

import java.util.ArrayList;

class Robot {

    private static int boardSize;
    private static int[][] board;
    private static int N;
    private static ArrayList<Vector> slant;
    private static final int inf = 1000000;

    private static int gcd(int x, int y) {
        if (y == 0) {
            return x;
        }
        return gcd(y, x % y);
    }

    private static void init(Game game) {
        boardSize = game.board.size;
        board = new int[boardSize][boardSize];
        for (byte i = 0; i < boardSize; ++i) {
            for (byte j = 0; j < boardSize; ++j) {
                board[i][j] = game.board.get(new Point(i, j));
            }
        }
        N = game.rule.connectNumber;
        slant = new ArrayList<>();
        slant.add(new Vector((byte) 0, (byte) 1));
        slant.add(new Vector((byte) 1, (byte) 0));
        if (game.rule.allowSlant) {
            for (byte i = 1; i < boardSize; ++i) {
                for (byte j = 1; j < boardSize; ++j) {
                    if (gcd(i, j) != 1) {
                        continue;
                    }
                    if (i * (N - 1) >= boardSize || j * (N - 1) >= boardSize) {
                        continue;
                    }
                    slant.add(new Vector(i, j));
                    slant.add(new Vector(i, (byte) -j));
                }
            }
        } else {
            slant.add(new Vector((byte) 1, (byte) 1));
            slant.add(new Vector((byte) 1, (byte) -1));
        }
    }

    private static void setColor(Point p, int c) {
        board[p.x][p.y] = c;
    }

    private static int getColor(Point p) {
        return board[p.x][p.y];
    }

    private static int calcScore(Point origin, int myId) {
        int score = 0;
        setColor(origin, myId);
        ArrayList<Point> left = new ArrayList<>();
        ArrayList<Point> right = new ArrayList<>();
        for (Vector del : slant) {
        	right.clear();
            for (Point p = origin.copy(); ; p = p.move(del)) {
                if (p.x < 0 || p.x >= boardSize || p.y < 0 || p.y >= boardSize) {
                    break;
                }
                right.add(p);
            }
            Vector minus = del.reverse();
            left.clear();
            for (Point p = origin.copy(); ; p = p.move(minus)) {
                if (p.x < 0 || p.x >= boardSize || p.y < 0 || p.y >= boardSize) {
                    break;
                }
                left.add(p);
            }

            int l, r, dl, dr;
            for (l = 0; l < left.size() && getColor(left.get(l)) == myId; ++l);
            for (r = 0; r < right.size() && getColor(right.get(r)) == myId; ++r);
            for (dl = 0; l + dl < left.size() && getColor(left.get(l + dl)) == BLANK; ++dl);
            for (dr = 0; r + dr < right.size() && getColor(right.get(r + dr)) == BLANK; ++dr);
            int subjectA = (l + r - 1) * 2;
            int subjectC = dl + dr;
            int subjectB = 0;
            if (left.size() > 1 && getColor(left.get(1)) != BLANK) {
                for (l = 1; l < left.size() && getColor(left.get(l)) == getColor(left.get(1)); ++l);
            } else {
                l = 0;
            }
            if (right.size() > 1 && getColor(right.get(1)) != BLANK) {
                for (r = 1; r < right.size() && getColor(right.get(r)) == getColor(right.get(1)); ++r);
            } else {
                r = 0;
            }
            if (l != 0 && r != 0 && getColor(left.get(1)) == getColor(right.get(1))) {
                subjectB = l + r - 1;
            } else {
                subjectB = Math.max(l, r);
            }
            score += subjectA + subjectB - subjectC;
        }
        setColor(origin, BLANK);
        return score;
    }

    static Point calc(Game game, byte myId) {
        init(game);
        int bestScore = -inf;
        ArrayList<Point> best = new ArrayList<>();
        for (byte i = 0; i < boardSize; ++i) {
            for (byte j = 0; j < boardSize; ++j) {
                if (board[i][j] != BLANK) {
                    continue;
                }
                int score = calcScore(new Point(i, j), myId);
                if (score > bestScore) {
                	best.clear();
                	best.add(new Point(i, j));
                	bestScore = score;
                }
                else if (score == bestScore) {
                    best.add(new Point(i, j));
                }
            }
        }
        return best.get((int)Math.floor(Math.random() * best.size()));
    }
}
