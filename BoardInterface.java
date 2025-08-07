/*@Habiba Javaid
Roll No: 0030.
*/


public interface BoardInterface {
    void initializeBoard();   //setup the board to a starting place,or their initial positions.
    void displayBoard();      //shows the current state of the board visually.
    PieceInterface getPiece(int x, int y);  //Returns the piece located at the board coordinates(x , y).
    void setPiece(int x, int y, PieceInterface piece);  //Places a piece at coordinates(x , y) on the board. for the update of board when setting up the board or after a move .
    void removePiece(int x, int y);  //when a piece is captured remove from the board.
    void movePiece(int fromX, int fromY, int toX, int toY); //show the movements of the pieces fromX , fromY  'to'  toX, toY  used for updating the bosrd state when players make moves.
    boolean isInBounds(int x, int y);  //check if a coordinate(x,y) lies inside the board.
    boolean isCheck(boolean isWhite);  // check if the player of the given color is white == true otherwise false.
    boolean isCheckmate(boolean isWhite); // check if the player of the given color checkmate the oponent player. mean check it and they have no move to left.
    void setupLevel(int level);  // check the levels and move next levels according to the scenario.
}


