
/*@Habiba Javaid
Roll No: 0030.
*/
import java.util.List;              // these classes used in java.util package.
import java.util.ArrayList;
    

public class Bishop extends Piece {    //subclass of piece.
    public Bishop(boolean isWhite) {   //constructor used for object creation and  bolean used if piece is white true otherwise false.
        
        super(isWhite, 3);  //parent constructor call.
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {  // it represent the starting position of the bishop and target position which he wants.
        int dx = Math.abs(toX - fromX);  //it ensures dx moving vertically,  ( Math.abs) ensures the different is always positive.
        int dy = Math.abs(toY - fromY);  // it ensures dy moving horizontally ,
        
        // Bishop can move exactly 3 squares diagonally
        if (dx != dy || dx != 3) { //it ensures bishop moves diagonally. dx!=3 bishop moves 3 squares no more or no less square.
            return false;
        }
        
        int stepX = Integer.signum(toX - fromX);  //it possible the direction of the bishop. it don't change th sign tof the bishop movements.
        int stepY = Integer.signum(toY - fromY);   //which ensures that the which direction bishop moves like top-left,top-right,bottom-left,bottom-right.
        
        for (int i = 1; i < 3; i++) {   //run the loop for the possible movements of the bishop.
            int checkX = fromX + i * stepX;
            int checkY = fromY + i * stepY;
            if (board.getPiece(checkX, checkY) != null) { //it ensures that bishop cannot jump over other pieces.check if any other pieces is place in their path ,the way is blocked.
                return false;
            }
        }
        
        PieceInterface endPiece = board.getPiece(toX, toY);    //get the pieces of the board if square is empty possible move and any piece of oponent captured it.
        return endPiece == null || endPiece.isWhite() != this.isWhite();
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y, BoardInterface board) {///store the posiible moves of the Bishop.
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{3,3}, {3,-3}, {-3,3}, {-3,-3}}; 
        //{3,3}  3 square upward ,{-3,-3} downward,{3, -3} upward right ,{-3,3} downward left.
        
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (board.isInBounds(nx, ny) && isValidMove(x, y, nx, ny, board)) {
                moves.add(new int[]{nx, ny});
            }
        }
        return moves;
    }

    @Override
    public char getSymbol() {

        return isWhite() ? 'B' : 'b';
    }

    @Override
    public String getType() {
        return "Bishop";
    }
    @Override
    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
        return isValidMove(fromX, fromY, toX, toY, board);
    }
}


    