/*@Habiba Javaid
Roll No: 0030.
*/


import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite, 0); // King's value is 0 (priceless)
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {
        // First check if positions are within bounds
        if (!board.isInBounds(toX, toY)) {
            return false;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);

        // Standard king move (1 square in any direction)
        if (dx <= 1 && dy <= 1) {
            PieceInterface target = board.getPiece(toX, toY);
            // Can move to empty square or capture opponent
            return target == null || target.isWhite() != this.isWhite();
        }

        // Castling would be checked here in full implementation
        return false;
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y, BoardInterface board) {
        List<int[]> moves = new ArrayList<>();

        // All 8 possible directions
        int[][] directions = {
            {-1,-1}, {-1,0}, {-1,1},
            {0,-1},          {0,1},
            {1,-1},  {1,0},  {1,1}
        };

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (board.isInBounds(nx, ny)) {
                PieceInterface p = board.getPiece(nx, ny);
                if (p == null || p.isWhite() != this.isWhite()) {
                    moves.add(new int[]{nx, ny});
                }
            }
        }

        return moves;
    }

    public char getSymbol() {
        return isWhite() ? 'K' : 'k';
    }

    @Override
    public String getType() {
        return "King";
    }

    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
        // Delegate to the interface version
        return isValidMove(fromX, fromY, toX, toY, board);
    }

    // Helper method to check if square is under attack
    public boolean isSquareUnderAttack(int x, int y, BoardInterface board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PieceInterface p = board.getPiece(i, j);
                if (p != null && p.isWhite() != this.isWhite()) {
                    if (p.isValidMove(i, j, x, y, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}