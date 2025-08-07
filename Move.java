




/*public abstract class Move {
    private final int fromX, fromY;
    private final int toX, toY;
    private Piece capturedPiece;
    private boolean givesExtraTurn = false;
    
    public Move(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }
    
    // Getters
    public int getFromX() { return fromX; }
    public int getFromY() { return fromY; }
    public int getToX() { return toX; }
    public int getToY() { return toY; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public boolean givesExtraTurn() { return givesExtraTurn; }
    
    // Setters
    public void setCapturedPiece(Piece piece) { 
        this.capturedPiece = piece;
        this.givesExtraTurn = piece != null; // Capture gives extra turn
    }
    
    @Override
    public String toString() {
        return String.format("%c%d to %c%d", 
            'a'+fromX, 8-fromY, 
            'a'+toX, 8-toY);
    }
}

*/