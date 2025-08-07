


/*@Habiba Javaid
Roll No:0030
*/
public abstract class Piece implements PieceInterface {
    private final boolean isWhite;
    private final int value;
    private final Color color;  // Using enum for color instead of boolean for better readability
    
    // Enum for piece colors
    public enum Color {
        WHITE,
        BLACK;
        
        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }
    
  
    public enum Type {
        KING("King", 0),
        QUEEN("Queen", 9),
        ROOK("Rook", 5),
        BISHOP("Bishop", 3),
        KNIGHT("Knight", 3),
        PAWN("Pawn", 1);
        
        private final String name;
        private final int value;
        
        Type(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() { return name; }
        public int getValue() { return value; }
    }

    public Piece(boolean isWhite, int value) {
        this.isWhite = isWhite;
        this.value = value;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    // Abstract method that all concrete pieces must implement
    public abstract String getType();
    
    // Common method for all pieces
    @Override
    public char getSymbol() {
        String type = getType().toUpperCase();
        char symbol = type.charAt(0);
        return isWhite ? symbol : Character.toLowerCase(symbol);
    }
    
    // Method to check if move is valid (to be implemented by subclasses)
    public abstract boolean isValidMove(Board board, int fromX, int fromY, int toX, int toY);
}