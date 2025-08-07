
/*@Habiba Javaid
Roll No: 0030
*/



import java.util.List;          
import java.util.ArrayList;     


public class Queen extends Piece {      
    public Queen(boolean isWhite) {     
        super(isWhite, 9);              
    }

    @Override
    public boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board) {   


        int dx = toX - fromX;    
        int dy = toY - fromY;    
        
        
        if (!(dx == 0 || Math.abs(dx) == Math.abs(dy))) {   
            return false;                                  
            
        }
        
        int stepX = Integer.signum(dx);   
        int stepY = Integer.signum(dy);   
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
       
        for (int i = 1; i < steps; i++) {
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
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {1,-1}, {1,0}, {1,1}}; // No horizontal
        
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            while (board.isInBounds(nx, ny)) {
                PieceInterface p = board.getPiece(nx, ny);
                if (p == null) {
                    moves.add(new int[]{nx, ny});
                } else {
                    if (p.isWhite() != this.isWhite()) {
                        moves.add(new int[]{nx, ny});
                    }
                    break;
                }
                nx += dir[0];
                ny += dir[1];
            }
        }
        return moves;
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'Q' : 'q';
    }

    @Override
    public String getType() {
        return "Queen";
    }

    @Override
    public boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY) {
        return isValidMove(fromX, fromY, toX, toY, board);
    }
}



//capital X and Y represent up down direction.
//lowercase x and y represent columns, row like right and left .