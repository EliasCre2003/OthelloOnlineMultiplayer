package eliascregard.game;

public class OthelloGame {

    private final Brick[][] board;
    private int turn;
    private int player1Score;
    private int player2Score;
    private boolean gameOver = false;
    private boolean noLegalMoves = false;
    private boolean otherPlayerNoLegalMoves = false;
    private static final int[][] directions = {
            {1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1}
    };


    public OthelloGame() {
        this.board = new Brick[8][8];
        board[3][3] = new Brick(0);
        board[3][4] = new Brick(1);
        board[4][3] = new Brick(1);
        board[4][4] = new Brick(0);
        turn = 1;
        player1Score = 0;
        player2Score = 0;
    }

    public void update() {
        if (gameOver) return;
        boolean noLegalMoves = true;
        for (int i = 0; i < 8; i++) {
            boolean breakLoop = false;
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null && isLegal(i, j)) {
                    noLegalMoves = false;
                    breakLoop = true;
                    break;
                }
            }
            if (breakLoop) break;
        }
        if (noLegalMoves) {
            if (otherPlayerNoLegalMoves) {
                gameOver = true;
            } else {
                otherPlayerNoLegalMoves = true;
                changeTurn();
            }
        }
    }

    private void changeTurn() {
        if (turn == 0) {
            turn = 1;
        } else {
            turn = 0;
        }
    }

    private boolean isLegal(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return false;
        if (board[x][y] != null) return false;
        for (int[] direction : directions) {
            int step = 1;
            while (true) {
                int x2 = x + direction[0] * step;
                int y2 = y + direction[1] * step;

                if (x2 < 0 || x2 >= 8 || y2 < 0 || y2 >= 8) break;
                if (board[x2][y2] == null) break;
                if (board[x2][y2].getPlayer() == turn) {
                    if (step > 1) return true;
                    break;
                }
                step++;
            }
        }
        return false;
    }

    private void recalculateGrid(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return;
        for (int[] direction : directions) {
            int step = 1;
            Brick[] possibleFlips = new Brick[64];
            while (true) {
                int x2 = x + direction[0] * step;
                int y2 = y + direction[1] * step;
                if (x2 < 0 || x2 >= 8 || y2 < 0 || y2 >= 8) break;
                if (board[x2][y2] == null) break;
                if (board[x2][y2].getPlayer() == turn) {
                    if (step == 1) break;
                    for (Brick brick : possibleFlips) {
                        if (brick == null) continue;
                        brick.flip();
                    }
                    break;
                }
                possibleFlips[step - 1] = board[x2][y2];
                step++;
            }
        }
    }

    public void placeBrick(int x, int y) {
        if (isLegal(x, y)) {
            board[x][y] = new Brick(turn);
            recalculateGrid(x, y);
            changeTurn();
        }
    }

    public int getTurn() {
        return turn;
    }

    public Brick getBrick(int x, int y) {
        return board[x][y];
    }



}
