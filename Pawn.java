/*@Habiba Javaid
Roll No: 0030.
*/


import java.util.List;
import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite, 1); // Pawns have a value of 1
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {
        // Verify positions are within bounds
        if (!board.isInBounds(toX, toY)) {
            return false;
        }

        int dx = toX - fromX;
        int dy = toY - fromY;
        int direction = isWhite() ? 1 : -1;
        PieceInterface target = board.getPiece(toX, toY);

        // Normal move forward
        if (dx == 0 && dy == direction) {
            return target == null;
        }
        // First move can be two squares forward
        else if (dx == 0 && dy == 2 * direction && 
                ((isWhite() && fromY == 1) || (!isWhite() && fromY == 6))) {
            return target == null && 
                   board.getPiece(fromX, fromY + direction) == null;
        }
        // Capture diagonally
        else if (Math.abs(dx) == 1 && dy == direction) {
            return target != null && target.isWhite() != this.isWhite();
        }
        // Backward move (Super Chess special rule)
        else if (dx == 0 && dy == -direction) {
            return target == null;
        }
        return false;
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y, BoardInterface board) {
        List<int[]> moves = new ArrayList<>();
        int direction = isWhite() ? 1 : -1;

        // Forward move
        if (board.isInBounds(x, y + direction) && board.getPiece(x, y + direction) == null) {
            moves.add(new int[]{x, y + direction});
            
            // Double move from starting position
            if ((isWhite() && y == 1) || (!isWhite() && y == 6)) {
                if (board.getPiece(x, y + 2 * direction) == null) {
                    moves.add(new int[]{x, y + 2 * direction});
                }
            }
        }

        // Capture moves (diagonal)
        int[] captureX = {x - 1, x + 1};
        for (int cx : captureX) {
            if (board.isInBounds(cx, y + direction)) {
                PieceInterface p = board.getPiece(cx, y + direction);
                if (p != null && p.isWhite() != this.isWhite()) {
                    moves.add(new int[]{cx, y + direction});
                }
            }
        }

        // Backward move (Super Chess special rule)
        if (board.isInBounds(x, y - direction) && board.getPiece(x, y - direction) == null) {
            moves.add(new int[]{x, y - direction});
        }

        return moves;
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'P' : 'p';
    }

    @Override
    public String getType() {
        return "Pawn";
    }

    @Override
    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
    
      
        return isValidMove(fromX, fromY, toX, toY, board);
    }
    
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