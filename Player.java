
/*@Habiba Javaid
Roll No:0030
*/

import java.util.ArrayList;   //these classes used in java.util package .
import java.util.List;




public class Player implements PlayerInterface {  //Player implements interface of player.
    private final String name;     //private  keywaord used because we want to hide it and final used for fixed the name throughout the game which user enter it.
    private final boolean isWhite;   //    final used Player color is white not change throughout the game 
    private int score;                // add score of the player.
    private boolean hasExtraMove;        // Extra move earn if they captured oponent king. boolean used if captured true extra move or not.
    private final List<PieceInterface> capturedPieces;   // List used to show which pieces player captured and the end of the game,dispaly it.
    
    public Player(String name, boolean isWhite) {  // constructor object creation player name and his piecec color.
        this.name = name;                    
        this.isWhite = isWhite;
        this.score = 0;
        this.hasExtraMove = false;
        this.capturedPieces = new ArrayList<>();
    }
    
    @Override
    public String getName() {         //function used to get hte name of the currently player whose play the game.
        return name;                   // and their return typr is name.
    }
    
    @Override
    public boolean isWhite() {           //function used currntly player who pieces color is white oterwise return false. move to black pieces player.
        return isWhite;
    }
    
    @Override
    public int getScore() {              // get the score of the player who captured the pieces.
        return score;
    }
    
    @Override
    public void addScore(int points) {      // and add score to their points which they win.
        score += points;             // score + points .
    }
    
    @Override
    public boolean hasExtraMove() {          //if they captured oponent piece they get extra move , datatype usd boolean if they captuered has Extramove otherwise not.
        return hasExtraMove;
    }
    
    @Override
    public void setExtraMove(boolean extraMove) {     //  set he rule if they captured oponent piece they get extra move.
        hasExtraMove = extraMove;
    }
    
    @Override
    public void addCapturedPiece(PieceInterface piece) {   // it add the captured piece to the player  captured pieces 
        capturedPieces.add(piece);
    }
    
    @Override
    public List<PieceInterface> getCapturedPieces() {             //it display the list of the captured pieces which they win throughout the game.
        return capturedPieces;
    }
}


