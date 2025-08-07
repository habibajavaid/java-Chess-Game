/*@Habiba Javaid
Roll No: 0030.
*/

import java.util.ArrayList;   // these classes used in java.util package.
import java.util.List;

public class Board implements BoardInterface {    //Board in which we implemnts the board interface.
    private PieceInterface[][] grid;  // our purpose to represent the 8x8 chess board with x,y gird. Gird is the fundamental data structure that represent the chess board ,gird is a two-dimensional array that stores all chess pieces on the board. It used direct Acess to any square using coordinates. 
    static final int SIZE = 8;   // 8x8 board representation.
    private int currentLevel;   // Player who start the game first their is the current level of him.
    
    public Board() { //costructor.
        grid = new PieceInterface[SIZE][SIZE]; //create 8x8 gird on the chess board.
        currentLevel = 1;   // Defaul starting level 1 ,or currently level 1.
    }
    
    @Override  // Board setup Methods.
    public void initializeBoard() {  //Setup board for current level.
        setupLevel(currentLevel);
    }
    
    @Override  // function logic for piece placement at different , because, different levels in different pieces sre play.
    public void setupLevel(int level) {  //it setup levels in the game.
        // Clear the board
        for (int x = 0; x < SIZE; x++) { //   outer loop x goes from 0 t0 7 since ( 8 size)
            for (int y = 0; y < SIZE; y++) {  //  inner loop y goes from 0 to 7  since ( 8 size)
                grid[x][y] = null; // if x,y null means that it removes all pieces and all levels are played and game is finished.
            }
        }
        
        if (level == 1) {
            // Level 1: King, Queen, 2 Knights, and 2 Pawns
            // White pieces
            grid[4][0] = new King(true);    //level 1 pieces placement. like [4] column and [0] rows.
            grid[3][0] = new Queen(true);
            grid[1][0] = new Knight(true);
            grid[6][0] = new Knight(true);
            grid[0][1] = new Pawn(true);
            grid[7][1] = new Pawn(true);
            
            // Black pieces
            grid[4][7] = new King(false);
            grid[3][7] = new Queen(false);
            grid[1][7] = new Knight(false);
            grid[6][7] = new Knight(false);
            grid[0][6] = new Pawn(false);
            grid[7][6] = new Pawn(false);
        } else if (level == 2) {
            // Level 2: More pieces
            // White pieces
           grid[4][0] = new King(true);
            grid[3][0] = new Queen(true);
            grid[1][0] = new Knight(true);
            grid[6][0] = new Knight(true);
            grid[0][0] = new Rook(true);
            grid[7][0] = new Rook(true);
            grid[2][0] = new Bishop(true);
            grid[5][0] = new Bishop(true);
            for (int i = 0; i < 3; i++) {
                grid[i][1] = new Pawn(true);
            }
            // Black pieces
            grid[4][7] = new King(false);
            grid[3][7] = new Queen(false);
            grid[1][7] = new Knight(false);
            grid[6][7] = new Knight(false);
            grid[0][7] = new Rook(false);
            grid[7][7] = new Rook(false);
            grid[2][7] = new Bishop(false);
            grid[5][7] = new Bishop(false);
            for (int i = 0; i < 3; i++) {
                grid[i][6] = new Pawn(false);
            }
        } else {
            // Level 3: Full setup
            // White pieces
            grid[4][0] = new King(true);
            grid[3][0] = new Queen(true);
            grid[1][0] = new Knight(true);
            grid[6][0] = new Knight(true);
            grid[0][0] = new Rook(true);
            grid[7][0] = new Rook(true);
            grid[2][0] = new Bishop(true);
            grid[5][0] = new Bishop(true);
            for (int i = 0; i < 5; i++) {   //add pawn from 
                grid[i][1] = new Pawn(true);
            }
            
            // Black pieces
            grid[4][7] = new King(false);
            grid[3][7] = new Queen(false);
            grid[1][7] = new Knight(false);
            grid[6][7] = new Knight(false);
            grid[0][7] = new Rook(false);
            grid[7][7] = new Rook(false);
            grid[2][7] = new Bishop(false);
            grid[5][7] = new Bishop(false);
            for (int i = 0; i < 5; i++) {  //add pawn on the black side.
                grid[i][6] = new Pawn(false);
            }
        }
    }
    
    @Override
    public void displayBoard() {                         
        System.out.println("  a b c d e f g h");  //chess board label.
        for (int y = SIZE-1; y >= 0; y--) {       //Ranks 8 to 1.start 1 to 8 , into y-axis. y-- pieces moves backward .
            System.out.print((y+1) + " ");
            for (int x = 0; x < SIZE; x++) {      //Rank number from a to h pieces moves forward.
                PieceInterface p = grid[x][y];
                System.out.print(p == null ? "." : p.getSymbol()); //pieces are placen otherwis eemtpy dot are placed.
                System.out.print(" ");   //empty if there is no pieces.
            }
            System.out.println(y+1);  //Rank nmber again.
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Current Level: " + currentLevel);  ///for current level.
    }
    
    @Override
    public PieceInterface getPiece(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return grid[x][y];   //check if pieces is in board. if piece is not in board return null.
    }
    
    @Override
    public void setPiece(int x, int y, PieceInterface piece) {
        if (isInBounds(x, y)) {
            grid[x][y] = piece;   // set pieces is in the board x,y grid.
        }
    }
    
    @Override
    public void removePiece(int x, int y) { //if pieces are captured remove from the board grid in x,y.
        if (isInBounds(x, y)) {
            grid[x][y] = null;  //square null or empty after removal.
        }
    }
    
    @Override
    public void movePiece(int fromX, int fromY, int toX, int toY) {
        PieceInterface piece = getPiece(fromX, fromY);
        if (piece != null) {
            removePiece(fromX, fromY); //if square is not empty remove pieces captured case.
            setPiece(toX, toY, piece);  //place in new position.
        }
    }
    
      @Override
    public boolean isInBounds(int x, int y) {             //check if it is ijnboard x,y coordinates. 
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;   //check x-coordinates is greater than 0 and less than size , and asit is y coordinates
    }
    
    @Override
    public boolean isCheck(boolean isWhite) {
        // Find the king's position.
        int kingX = -1, kingY = -1; //these will bw used to store x,y coordinates of the color king specified by the isWhite. the initial cvalue of -1 indicates that the king's position has not yet been found.-1 indicate the king has no longer yet placed in the -1 position.
        for (int x = 0; x < SIZE; x++) {    // there are nested for loop used ,the outer loop indicates the rows and the inner loop inducates the columns.
            for (int y = 0; y < SIZE; y++) {
                PieceInterface p = grid[x][y];   //grid[x][y] 2D array represent the board disoaly.
                if (p != null && p instanceof King && p.isWhite() == isWhite) {  // if piece is not empty from the king position and king instance of piece and is white color ,this is the position of the kin piece.
                    kingX = x;   //store the x-coordinate of the king int he kingX variable. and Yvariable for y-coordinate.
                    kingY = y;
                    break;
                }
            }
        }
        
        // Check if any opponent piece can attack the king
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                PieceInterface p = grid[x][y];
                if (p != null && p.isWhite() != isWhite) {  //if piece is not null or empty any other piece is place in currnt position ,
                    if (p.isValidMove(x, y, kingX, kingY, this)) { //it help for the king to the valid moves.
                        return true;
                    }
                }
            }//otherwise return false.
        }
        return false;
    }
    
    @Override
    public boolean isCheckmate(boolean isWhite) {   //Checmate method aims that to determine thathe current player is in white orb balck on the based of checkmate.
        if (!isCheck(isWhite)) return false;  // this condition we check that the current player is actually check .
        
        // Check if any move can get out of check
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                PieceInterface p = grid[x][y];
                if (p != null && p.isWhite() == isWhite) {  //it indicate that the current polayer is in white or pice belongs to the current player.
                    List<int[]> moves = p.getPossibleMoves(x, y, this);  //if we find a piece belinging to the current player,....
                    for (int[] move : moves) {              //...this line calls a method getpossibleMoves on that pieces.
                        // Try the move                //this loop runs for the move add moves.
                        PieceInterface temp = grid[move[0]][move[1]];   //this block of code making the current move:
                        grid[move[0]][move[1]] = p;         //temp used to store the currently moves of the piece to the destination square of the move.help to move piece from[0] to[1]
                        grid[x][y] = null;   //set the original square is empty.
                        
                        boolean stillInCheck = isCheck(isWhite); //afetr the moving of the previous move  isCheck method again to see if the current player's King is still in check in this board.
                        
                        // Undo the move
                        grid[x][y] = p;   //after  checkmate the board is restoring on its original state before the move was tired.set the board and piece to the original position.
                        grid[move[0]][move[1]] = temp;
                        
                        if (!stillInCheck) {  //if the stillcheck is return false it means that the current king is not longer check one legal move it takes, so the current situation is not checkmate . the method immediately return false.
                            return false;
                        }
                    }
                }   //if outer loops complete without if(!stillInCheck) mening that the checkmate remain at king at the possible legal move king remains check return true.
            }
        }
        return true;
    }
    
    public void nextLevel() {   // if first level complete , it moves second level and then third.
        currentLevel++;
        if (currentLevel > 3) currentLevel = 3;
        setupLevel(currentLevel);  //first level set as current levvel.
    }
    
    public int getCurrentLevel() {  // for current level ,when player start the game.
        return currentLevel;
    }
}  
