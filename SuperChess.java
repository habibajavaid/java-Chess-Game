

/*@Habiba Javaid
Roll No: 0030
*/

import java.util.List;  
 import java.util.Scanner; 
import java.util.Random;  

public class SuperChess implements GameInterface {  
    private final Board board;   
    private final Player whitePlayer;  
    private final Player blackPlayer; 
    private boolean whiteTurn;  
    private final Random random;  
    public SuperChess(String whiteName, String blackName) {  
        board = new Board();
        whitePlayer = new Player(whiteName, true);
        blackPlayer = new Player(blackName, false);
        whiteTurn = true;
        random = new Random();
    }
    
    @Override   
    public void startGame() {   
        board.initializeBoard();   
        Scanner scanner = new Scanner(System.in);  
        
        while (true) {  
            board.displayBoard();   
            Player currentPlayer = whiteTurn ? whitePlayer : blackPlayer;
            System.out.println(currentPlayer.getName() + " (" + (currentPlayer.isWhite() ? "White" : "Black") + ")'s turn.");  
            System.out.println("Score: " + currentPlayer.getScore());  
            
            if (currentPlayer.hasExtraMove()) {  
                System.out.println("You have an extra move from capturing!");
                currentPlayer.setExtraMove(false);
            }
            
            System.out.print("Enter your move (e.g., e2 e4 or 'quit'): ");  
            String input = scanner.nextLine().trim();  
            
            if (input.equalsIgnoreCase("quit")) {  
                endGame();
                break;
            }
            
            String[] parts = input.split("\\s+");  
            if (parts.length != 2) {  
                System.out.println("Invalid input. Use format: e2 e4");
                continue;
            }
            
            try {   
                int fromX = parts[0].charAt(0) - 'a';   
                int fromY = Character.getNumericValue(parts[0].charAt(1)) - 1;  
                int toX = parts[1].charAt(0) - 'a';  
                int toY = Character.getNumericValue(parts[1].charAt(1)) - 1; 
                
                if (!board.isInBounds(fromX, fromY) || !board.isInBounds(toX, toY)) {   
                    System.out.println("Coordinates out of bounds!");
                    continue;  
                }
                
                PieceInterface piece = board.getPiece(fromX, fromY);
                if (piece == null) {
                    System.out.println("No piece at " + parts[0]);  
                    continue;
                }
                if (piece.isWhite() != currentPlayer.isWhite()) {
                    System.out.println("You can only move your own pieces.");  
                    continue;
                }
                if (!piece.isValidMove(fromX, fromY, toX, toY, board)) {
                    System.out.println("Invalid move for " + piece.getSymbol()); 
                    continue;
                }
                
                // Handle capture
                PieceInterface capturedPiece = board.getPiece(toX, toY);  //if piece captured a oponent piece add score ,and extra move and print meassge you get an extra move.
                if (capturedPiece != null) {
                    currentPlayer.addScore(capturedPiece.getValue());
                    currentPlayer.addCapturedPiece(capturedPiece);
                    currentPlayer.setExtraMove(true); // Super Chess capture bonus
                    System.out.println("Captured " + capturedPiece.getSymbol() + "! +" + 
                        capturedPiece.getValue() + " points. You get an extra move!");
                }
                
                // Make move
                board.movePiece(fromX, fromY, toX, toY);  //make possible move of the pieces.
                
                // Check for checkmate
                if (board.isCheckmate(!currentPlayer.isWhite())) {   //if current player is black and checkmate white player . curent player wins.
                    System.out.println("Checkmate! " + currentPlayer.getName() + " wins!");
                    handleLevelCompletion(currentPlayer);   
                    break;
                }
                
                // Check for check
                if (board.isCheck(!currentPlayer.isWhite())) {
                    System.out.println("Check!");
                }
                
                // Switch turns if no extra move
                if (!currentPlayer.hasExtraMove()) {
                    whiteTurn = !whiteTurn;
                }
                
                // Special level 2/3 bonuses
                if (board.getCurrentLevel() > 1 && random.nextInt(3) == 0) {
                    System.out.println("Lucky roll! You get a bonus:");
                    if (random.nextBoolean()) {
                        System.out.println("2-3 consecutive turns!");
                        currentPlayer.setExtraMove(true);
                    } else {
                        System.out.println("Revive a captured piece!");
                        revivePiece(currentPlayer);
                    }
                }
                
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Invalid coordinate format. Use format like 'e2 e4'");
            }
        }
        scanner.close();
    }
    
    private void handleLevelCompletion(Player winner) {
        int currentLevel = board.getCurrentLevel();
        if (currentLevel < 3) {
            System.out.println("Advancing to level " + (currentLevel + 1));
            board.nextLevel();
            startGame(); // Restart game with new level
        } else {
            System.out.println("Congratulations! You've completed all levels!");
            endGame();
        }
    }
    
    private void revivePiece(Player player) {
        List<PieceInterface> captured = player.getCapturedPieces();
        if (captured.isEmpty()) {
            System.out.println("No pieces to revive.");
            return;
        }
        
        // Simple revival - just pick the first captured piece
        PieceInterface piece = captured.get(0);
        System.out.println("Revived " + piece.getSymbol() + "!");
        
        // Find an empty square near the back row
        int y = player.isWhite() ? 0 : 7;
        for (int x = 0; Board.SIZE >= x; x++) {
            if (board.getPiece(x, y) == null) {
                board.setPiece(x, y, piece);
                captured.remove(0);
                return;
            }
        }
        
        System.out.println("No space to place the revived piece.");
    }
    
    @Override
    public void nextLevel() {
        board.nextLevel();
    }
    
    @Override
    public void endGame() {
        System.out.println("Game over!");
        System.out.println("Final scores:");
        System.out.println(whitePlayer.getName() + " (White): " + whitePlayer.getScore());
        System.out.println(blackPlayer.getName() + " (Black): " + blackPlayer.getScore());
        System.out.println("Captured pieces:");
        System.out.println(whitePlayer.getName() + ": " + 
            whitePlayer.getCapturedPieces().stream().map(PieceInterface::getSymbol).toList());
        System.out.println(blackPlayer.getName() + ": " + 
            blackPlayer.getCapturedPieces().stream().map(PieceInterface::getSymbol).toList());
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Super Chess!");
        System.out.print("Enter White player name: ");
        String whiteName = scanner.nextLine();
        System.out.print("Enter Black player name: ");
        String blackName = scanner.nextLine();
        
        SuperChess game = new SuperChess(whiteName, blackName);
        game.startGame();
        scanner.close();
    }
}


