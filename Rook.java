

/*@Habiba Javaid
Roll No: 0030.
*/
    import java.util.List;  
   import java.util.ArrayList;




public class Rook extends Piece {  
    public Rook(boolean isWhite) {  
        super(isWhite, 5);            
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {  
        int dx = Math.abs(toX - fromX);   
        int dy = Math.abs(toY - fromY);  
        
        
        if (!((dx == 4 && dy == 0) || (dx == 0 && dy == 4))) {    
        
            return false;
        }
        
        int stepX = Integer.signum(toX - fromX);  
        int stepY = Integer.signum(toY - fromY);   
        
        
        
        for (int i = 1; i < 4; i++) { 
            int checkX = fromX + i * stepX;
            int checkY = fromY + i * stepY;
            if (board.getPiece(checkX, checkY) != null) {  
                return false;
            }
        }
        
        PieceInterface endPiece = board.getPiece(toX, toY);
        return endPiece == null || endPiece.isWhite() != this.isWhite();  
    }

    @Override
    public List<int[]> getPossibleMoves(int x, int y, BoardInterface board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{0,4}, {0,-4}, {4,0}, {-4,0}};
        
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
        return isWhite() ? 'R' : 'r';
    }

    @Override
    public String getType() {
        return "Rook";
    }

    @Override
    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
        return isValidMove(fromX, fromY, toX, toY, board);
    }
}