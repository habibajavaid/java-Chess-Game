/*@Habiba Javaid
Roll No:0030
*/


import java.util.List;



// is used in java to tell the compiler we want to use the list interface from the java.util package.
//we used list when you want to store multiple objects likes moves,pieces.





public interface PieceInterface {
    boolean isValidMove(int fromX, int fromY, int toX, int toY, BoardInterface board);
    List<int[]> getPossibleMoves(int x, int y, BoardInterface board);
    boolean isWhite();
    char getSymbol();
    int getValue();
}


//isValidMove.
//checks if a piece can legally move from square (fromX, fromY) to (toY, toX) based on chess piece rules and board interface to see that are pieces are in on their right poeition.
//Prevents illegal moves like king moves one square any direction, pawn move forward.

//List
// see all the piece are in legal moves the piece can make from position (x ,y) given the current.
//if player is in illegal moves left.


//boolean 
//to distinguish which player a piece belongs to.


//getSymbol
//represent the piece visually.uppercase letter for white pieces and lowercase letter for black pieces.
//


//getValue : we get the score of the player so we used this, deciding moves comparing  etc.