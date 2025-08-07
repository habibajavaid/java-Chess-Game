/*@Habiba Javaid
Roll No: 0030
*/





import java.util.List;


public interface PlayerInterface {
    String getName();    //return the player name.
    boolean isWhite();   //player which belongs to white or black color of chess.
    int getScore();      //Return the playper current score.
    void addScore(int points); //Add the points to the player's current score.
    boolean hasExtraMove();    //whether the player earned as an extra move.add to their currently points.
    void setExtraMove(boolean extraMove);  //set extra move for the player.which player eaarned extra points.
    void addCapturedPiece(PieceInterface piece);  //the piece he captured add to their list pieces of player. is a function add captured pieces.
    List<PieceInterface> getCapturedPieces();    //return the list of pieces the player has captured. is a list of all capturd pieces.
}  
