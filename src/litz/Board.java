package litz;

class Board {
    final byte size;
    private final byte[][] board;
    /*private*/ static final byte BLANK = -2;
    Board(byte boardSize) {
        this.size = boardSize;
        this.board = new byte[boardSize][boardSize];
        clear();
    }
    byte get(Point position) {
        return board[position.x][position.y];
    }
    final void clear() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = BLANK;
            }
        }
    }
    void clearPosition(Point position) {
        board[position.x][position.y] = BLANK;
    }
    void place(Piece piece) {
        board[piece.position.x][piece.position.y] = piece.styleIndex;
    }
    void remove(Piece piece) {
        if (hasPiece(piece)) {
            board[piece.position.x][piece.position.y] = BLANK;
        }
    }
    boolean isValid(Point position) {
        return position.x >= 0 && position.x < size && position.y >= 0 && position.y < size;
    }
    boolean isBlank(Point position) {
        return /*isValid(position) &&*/ get(position) == BLANK;
    }
    boolean hasPiece(Piece piece) {
        return /*isValid(piece.position) &&*/ get(piece.position) == piece.styleIndex;
    }
}
