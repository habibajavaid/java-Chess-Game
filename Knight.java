/*@Habiba Javaid
Roll No: 0030.
*/


import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite, 3);
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {  // check if a piece is legally move from x and y , based on chess rule pieces and interfaces ses their right position.
        //prevent illegal move like , Rook move four square horizontally and vertically not diagonally.
        int dx = Math.abs(toX - fromX);   // dx for vertical move , Math.abs used for sign change negative to positive and positive remain same.
        int dy = Math.abs(toY - fromY);   //dy for horizontal move, from Y to y.
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);  // its mean that two square move vertical and one move horizantal like a L- Shape.
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y, BoardInterface board) {
        List<int[]> moves = new ArrayList<>();
        int[][] jumps = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {1,-2}, {-1,2}, {-1,-2}};
        
        for (int[] jump : jumps) {
            int nx = x + jump[0];
            int ny = y + jump[1];
            if (board.isInBounds(nx, ny)) {
                PieceInterface p = board.getPiece(nx, ny);
                if (p == null || p.isWhite() != this.isWhite()) {
                    moves.add(new int[]{nx, ny});
                }
            }
        }
        return moves;
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'N' : 'n';
    }

    @Override
   public String getType() {
        return "Knight";
                }

    @Override
    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
        return isValidMove(fromX, fromY, toX, toY, board);
    }
}