/*@Habiba Javaid
Roll No:0030.
*/



/*import javax.swing.*;
import java.awt.*;
import static java.awt.Color.black;
import static java.awt.Color.white;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.*;

public class ChessLevel11 extends JFrame {
    private JButton[][] squares = new JButton[6][8];
    private ImageIcon[] pieceImages = new ImageIcon[12];
    private int selectedRow = -1, selectedCol = -1;
    private boolean isWhiteTurn = true;
    private List<Point> possibleMoves = new ArrayList<>();
    private JLabel player1NameLabel, player2NameLabel;
    private JLabel player1ScoreLabel, player2ScoreLabel;
    private JTextArea player1CapturedArea, player2CapturedArea;
    private Image playerPanelBackground;
    private JLabel timerLabel;
    private JLabel levelLabel;
    private Timer gameTimer;
    private int secondsElapsed = 0;
    private boolean isPaused = false;
    private int player1Score = 0;
    private int player2Score = 0;
    private int currentLevel = 1;
    private String gameMode;
    private List<String> player1CapturedPieces = new ArrayList<>();
    private List<String> player2CapturedPieces = new ArrayList<>();
    private Clip moveSound;
    private Clip captureSound;
    
    private static final int WHITE_PAWN = 0;
    private static final int WHITE_KNIGHT = 1;
    private static final int WHITE_QUEEN = 2;
    private static final int WHITE_KING = 3;
    private static final int BLACK_PAWN = 6;
    private static final int BLACK_KNIGHT = 7;
    private static final int BLACK_QUEEN = 8;
    private static final int BLACK_KING = 9;

    public ChessLevel11(String gameMode ) {
        this.gameMode = gameMode;
        initializeFrame();
        loadPieceImages();
        loadPlayerPanelBackground();
        loadSounds();
        JPanel chessBoardPanel = createChessBoardPanel();
        JPanel playerInfoPanel = createPlayerInfoPanel();
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, playerInfoPanel);
        mainSplitPane.setResizeWeight(0.95);
        JPanel topPanel = createTopPanel();
        JPanel bottomPanel = createBottomPanel();
        add(topPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setPlayerNames();
        finalizeFrame();
    }
    
    private void loadSounds() {
        try {
            // Load move sound
            AudioInputStream moveAudio = AudioSystem.getAudioInputStream(
                getClass().getResource("/Sounds/chessmove.wav"));
            moveSound = AudioSystem.getClip();
            moveSound.open(moveAudio);
            
            // Load capture sound
            AudioInputStream captureAudio = AudioSystem.getAudioInputStream(
                getClass().getResource("/Sounds/chesscapture.wav"));
            captureSound = AudioSystem.getClip();
            captureSound.open(captureAudio);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading sound effects: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void playMoveSound() {
        if (moveSound != null) {
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    private void playCaptureSound() {
        if (captureSound != null) {
            captureSound.setFramePosition(0);
            captureSound.start();
        }
    }

    private void initializeFrame() {
        setTitle("Chess Game - Level 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }
    
    private void loadPlayerPanelBackground() {
        try {
            playerPanelBackground = ImageIO.read(getClass().getResource("/Images/image.png"));
        } catch (IOException e) {
            playerPanelBackground = null;
        }
    }

    private void setPlayerNames() {
        switch(gameMode) {
            case "free":
                player1NameLabel.setText("Player");
                player2NameLabel.setText("AI");
                break;
            case "1 vs 1":
                player1NameLabel.setText("Player 1");
                player2NameLabel.setText("Player 2");
                break;
            case "2 vs 2":
                player1NameLabel.setText("Team A");
                player2NameLabel.setText("Team B");
                break;
            default:
                player1NameLabel.setText("Player 1");
                player2NameLabel.setText("Player 2");
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("CHESS BOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 38));
        titleLabel.setForeground(Color.BLACK);
        levelLabel = new JLabel("Level: " + currentLevel, SwingConstants.RIGHT);
        levelLabel.setFont(new Font("Old English Text MT", Font.BOLD, 30));
        levelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(levelLabel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        newGameButton.setBackground(Color.decode("#49796B"));
        newGameButton.addActionListener(e -> startNewGame());
        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.PLAIN, 16));
        pauseButton.setBackground(Color.decode("#49796B"));
        pauseButton.addActionListener(e -> togglePause(pauseButton));
        JButton menuButton = new JButton("Back to Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 16));
        menuButton.setBackground(Color.decode("#49796B"));
        menuButton.addActionListener(e -> returnToMainMenu());
        buttonPanel.add(newGameButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(menuButton);
        panel.add(timerLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        Color lightPlayerColor = Color.decode("#E8F5E9");
        Color darkPlayerColor = Color.decode("#C8E6C9");
        panel.add(createPlayerPanel(lightPlayerColor));
        panel.add(createPlayerPanel(darkPlayerColor));
        return panel;
    }

    private JPanel createPlayerPanel(Color bg) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (playerPanelBackground != null) {
                    g.drawImage(playerPanelBackground, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setForeground(Color.WHITE);
        JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setForeground(Color.WHITE);
        JTextArea capturedArea = new JTextArea("Captured:\nNone", 5, 15);
        capturedArea.setFont(new Font("Arial", Font.BOLD, 16));
        capturedArea.setEditable(false);
        capturedArea.setOpaque(false);
        capturedArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(capturedArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        if (player1NameLabel == null) {
            player1NameLabel = nameLabel;
            player1ScoreLabel = scoreLabel;
            player1CapturedArea = capturedArea;
        } else {
            player2NameLabel = nameLabel;
            player2ScoreLabel = scoreLabel;
            player2CapturedArea = capturedArea;
        }
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createChessBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 8));
        panel.setBackground(Color.BLACK);
        Color lightSquare = Color.decode("#49796B");
        Color darkSquare = Color.decode("#FDECE0");
        String[][] layout = {
            {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
            {"bp", " ", " ", " ", " ", " ", " ", "bp"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"wp", " ", " ", " ", " ", " ", " ", "wp"},
            {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
        };
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = createChessSquare(layout[row][col], row, col, lightSquare, darkSquare);
                panel.add(squares[row][col]);
            }
        }
        return panel;
    }

    private JButton createChessSquare(String pieceCode, int row, int col, Color lightColor, Color darkColor) {
        JButton button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
        button.setPreferredSize(new Dimension(80, 80));
        switch (pieceCode) {
            case "wp" -> button.setIcon(pieceImages[WHITE_PAWN]);
            case "wn" -> button.setIcon(pieceImages[WHITE_KNIGHT]);
            case "wq" -> button.setIcon(pieceImages[WHITE_QUEEN]);
            case "wk" -> button.setIcon(pieceImages[WHITE_KING]);
            case "bp" -> button.setIcon(pieceImages[BLACK_PAWN]);
            case "bn" -> button.setIcon(pieceImages[BLACK_KNIGHT]);
            case "bq" -> button.setIcon(pieceImages[BLACK_QUEEN]);
            case "bk" -> button.setIcon(pieceImages[BLACK_KING]);
            default -> button.setIcon(null);
        }
        button.addActionListener(e -> handleSquareClick(row, col));
        return button;
    }

    private void handleSquareClick(int row, int col) {
        if (selectedRow == -1) {
            ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
            if (icon != null) {
                boolean isWhitePiece = isWhitePiece(icon);
                if ((isWhitePiece && isWhiteTurn) || (!isWhitePiece && !isWhiteTurn)) {
                    selectedRow = row;
                    selectedCol = col;
                    calculatePossibleMoves(row, col);
                    highlightPossibleMoves();
                }
            }
        } else {
            if (isValidMove(row, col)) {
                movePiece(selectedRow, selectedCol, row, col);
                isWhiteTurn = !isWhiteTurn;
            }
            selectedRow = -1;
            selectedCol = -1;
            clearHighlights();
        }
    }

    private boolean isWhitePiece(ImageIcon icon) {
        return icon == pieceImages[WHITE_PAWN] || 
               icon == pieceImages[WHITE_KNIGHT] || 
               icon == pieceImages[WHITE_QUEEN] || 
               icon == pieceImages[WHITE_KING];
    }
    
    private void calculatePossibleMoves(int row, int col) {
        possibleMoves.clear();
        ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
        
        if (icon == null) return;

        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) {
            // PAWN MOVEMENT (with backward movement)
            int forwardDir = (icon == pieceImages[WHITE_PAWN]) ? -1 : 1;
            int backwardDir = -forwardDir;
            
            // Forward movement
            if (isValidPosition(row + forwardDir, col) && squares[row + forwardDir][col].getIcon() == null) {
                possibleMoves.add(new Point(col, row + forwardDir));
                
                // Double move from starting position
                int startRow = (icon == pieceImages[WHITE_PAWN]) ? 4 : 1;
                if (row == startRow && squares[row + 2*forwardDir][col].getIcon() == null) {
                    possibleMoves.add(new Point(col, row + 2*forwardDir));
                }
            }
            
            // Backward movement (single square)
            if (isValidPosition(row + backwardDir, col) && squares[row + backwardDir][col].getIcon() == null) {
                possibleMoves.add(new Point(col, row + backwardDir));
            }
            
            // Forward captures
            for (int dc = -1; dc <= 1; dc += 2) {
                if (isValidPosition(row + forwardDir, col + dc)) {
                    ImageIcon targetIcon = (ImageIcon) squares[row + forwardDir][col + dc].getIcon();
                    if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(col + dc, row + forwardDir));
                    }
                }
            }
            
            // Backward captures
            for (int dc = -1; dc <= 1; dc += 2) {
                if (isValidPosition(row + backwardDir, col + dc)) {
                    ImageIcon targetIcon = (ImageIcon) squares[row + backwardDir][col + dc].getIcon();
                    if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(col + dc, row + backwardDir));
                    }
                }
            }
        }
        else if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) {
            // KNIGHT MOVEMENT (L-shape)
            int[][] knightMoves = {
                {-2, -1}, {-2, 1}, 
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
            };
            
            for (int[] move : knightMoves) {
                int newRow = row + move[0];
                int newCol = col + move[1];
                
                if (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    if (targetIcon == null || isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(newCol, newRow));
                    }
                }
            }
        }
        else if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) {
            // QUEEN MOVEMENT (vertical and diagonal only)
            // Vertical movement
            int[][] verticalDirections = {{-1, 0}, {1, 0}};
            
            for (int[] dir : verticalDirections) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                while (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    
                    if (targetIcon == null) {
                        possibleMoves.add(new Point(newCol, newRow));
                    } else {
                        if (isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(newCol, newRow));
                        }
                        break;
                    }
                    
                    newRow += dir[0];
                    newCol += dir[1];
                }
            }
            
            // Diagonal movement
            int[][] diagonalDirections = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            
            for (int[] dir : diagonalDirections) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                while (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    
                    if (targetIcon == null) {
                        possibleMoves.add(new Point(newCol, newRow));
                    } else {
                        if (isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(newCol, newRow));
                        }
                        break;
                    }
                    
                    newRow += dir[0];
                    newCol += dir[1];
                }
            }
        }
        else if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) {
            // KING MOVEMENT (one square in any direction)
            int[][] kingMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
            };
            
            for (int[] move : kingMoves) {
                int newRow = row + move[0];
                int newCol = col + move[1];
                
                if (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    if (targetIcon == null || isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(newCol, newRow));
                    }
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 6 && col >= 0 && col < 8;
    }

    private boolean isValidMove(int row, int col) {
        for (Point move : possibleMoves) {
            if (move.y == row && move.x == col) {
                return true;
            }
        }
        return false;
    }

    private void highlightPossibleMoves() {
        Color highlightColor = new Color(255, 255, 0, 100);
        for (Point move : possibleMoves) {
            squares[move.y][move.x].setBackground(highlightColor);
        }
    }

    private void clearHighlights() {
        Color lightSquare = Color.decode("#49796B");
        Color darkSquare = Color.decode("#FDECE0");
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? lightSquare : darkSquare);
            }
        }
    }

    private void updateCapturedArea(JTextArea area, List<String> pieces) {
        StringBuilder sb = new StringBuilder("Captured:\n");
        if (pieces.isEmpty()) {
            sb.append("None");
        } else {
            for (String piece : pieces) {
                sb.append(piece).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private String getPieceName(ImageIcon icon) {
        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) return "Pawn";
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return "Knight";
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return "Queen";
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return "King";
        return "";
    }

    private int getPieceValue(ImageIcon icon) {
        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) return 1;
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return 3;
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return 9;
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return 0;
        return 0;
    }

    private void loadPieceImages() {
        try {
            pieceImages[WHITE_PAWN] = createScaledIcon("/images/whitePawn.png", 60);
            pieceImages[WHITE_KNIGHT] = createScaledIcon("/images/whiteKnight.png", 60);
            pieceImages[WHITE_QUEEN] = createScaledIcon("/images/whiteQueen.png", 60);
            pieceImages[WHITE_KING] = createScaledIcon("/images/whiteKing.png", 60);
            pieceImages[BLACK_PAWN] = createScaledIcon("/images/blackPawn.png", 60);
            pieceImages[BLACK_KNIGHT] = createScaledIcon("/images/blackKnight.png", 60);
            pieceImages[BLACK_QUEEN] = createScaledIcon("/images/blackQueen.png", 60);
            pieceImages[BLACK_KING] = createScaledIcon("/images/blackKing.png", 60);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading piece images: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            for (int i = 0; i < pieceImages.length; i++) {
                if (pieceImages[i] == null) {
                    pieceImages[i] = new ImageIcon();
                }
            }
        }
    }

    private ImageIcon createScaledIcon(String path, int size) throws IOException {
        BufferedImage image = ImageIO.read(getClass().getResource(path));
        Image scaled = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void finalizeFrame() {
        validate();
        setLocationRelativeTo(null);
        startTimer();
        setVisible(true);
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });
        gameTimer.start();
    }

    private void togglePause(JButton pauseButton) {
        if (isPaused) {
            gameTimer.start();
            pauseButton.setText("Pause");
        } else {
            gameTimer.stop();
            pauseButton.setText("Resume");
        }
        isPaused = !isPaused;
    }

    private void startNewGame() {
        secondsElapsed = 0;
        timerLabel.setText("00:00");
        currentLevel = 1;
        levelLabel.setText("Level: " + currentLevel);
        player1Score = 0;
        player2Score = 0;
        player1CapturedPieces.clear();
        player2CapturedPieces.clear();
        player1ScoreLabel.setText("Score: 0");
        player2ScoreLabel.setText("Score: 0");
        player1CapturedArea.setText("Captured:\nNone");
        player2CapturedArea.setText("Captured:\nNone");
        setPlayerNames();
        if (gameTimer != null) {
            gameTimer.stop();
        }
       
        startTimer();
        
        String[][] layout = {
            {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
            {"bp", " ", " ", " ", " ", " ", " ", "bp"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"wp", " ", " ", " ", " ", " ", " ", "wp"},
            {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
        };
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                switch (layout[row][col]) {
                    case "wp" -> squares[row][col].setIcon(pieceImages[WHITE_PAWN]);
                    case "wn" -> squares[row][col].setIcon(pieceImages[WHITE_KNIGHT]);
                    case "wq" -> squares[row][col].setIcon(pieceImages[WHITE_QUEEN]);
                    case "wk" -> squares[row][col].setIcon(pieceImages[WHITE_KING]);
                    case "bp" -> squares[row][col].setIcon(pieceImages[BLACK_PAWN]);
                    case "bn" -> squares[row][col].setIcon(pieceImages[BLACK_KNIGHT]);
                    case "bq" -> squares[row][col].setIcon(pieceImages[BLACK_QUEEN]);
                    case "bk" -> squares[row][col].setIcon(pieceImages[BLACK_KING]);
                    default -> squares[row][col].setIcon(null);
                }
            }
        }
             
        isWhiteTurn = true;
        selectedRow = -1;
        selectedCol = -1;
        clearHighlights();
    }
    
    private boolean hasPlayerWon(boolean isWhite) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                if (icon != null) {
                    boolean pieceIsWhite = isWhitePiece(icon);
                    if ((isWhite && !pieceIsWhite) || (!isWhite && pieceIsWhite)) {
                        return false; // Found at least one opponent piece
                    }
                }
            }
        }
        return true; // No opponent pieces found
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        ImageIcon targetIcon = (ImageIcon) squares[toRow][toCol].getIcon();
        if (targetIcon != null) {
            // Play capture sound
            playCaptureSound();
            
            String pieceName = getPieceName(targetIcon);
            int pieceValue = getPieceValue(targetIcon);
            
            if (isWhiteTurn) {
                player1Score += pieceValue;
                player1CapturedPieces.add(pieceName);
                updateCapturedArea(player1CapturedArea, player1CapturedPieces);
            } else {
                player2Score += pieceValue;
                player2CapturedPieces.add(pieceName);
                updateCapturedArea(player2CapturedArea, player2CapturedPieces);
            }
            
            player1ScoreLabel.setText("Score: " + player1Score);
            player2ScoreLabel.setText("Score: " + player2Score);
        } else {
            // Play move sound only if not capturing
            playMoveSound();
        }
        
        squares[toRow][toCol].setIcon(squares[fromRow][fromCol].getIcon());
        squares[fromRow][fromCol].setIcon(null);
        
        // Check for win condition after move
        if (hasPlayerWon(isWhiteTurn)) {
            String winner = isWhiteTurn ? player1NameLabel.getText() : player2NameLabel.getText();
            JOptionPane.showMessageDialog(this, 
                winner + " has won by capturing all pieces! Moving to level 2...",
                "Level Complete",
                JOptionPane.INFORMATION_MESSAGE);
            advanceToNextLevel();
        }
    }

    protected void advanceToNextLevel() {
        this.dispose();
        new ChessGameLevel22(gameMode).setVisible(true);
    }

    private void returnToMainMenu() {
        this.dispose();
        new SuperChessGame2();
    }

    
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChessLevel11("1 vs 1");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
*/



/*@Shahreen Qasim
Roll No: 0023.
*/

import javax.swing.*;
import java.awt.*;
import static java.awt.Color.black;
import static java.awt.Color.white;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.*;

public class ChessLevel11 extends JFrame {
    private JButton[][] squares = new JButton[6][8];
    private ImageIcon[] pieceImages = new ImageIcon[12];
    private int selectedRow = -1, selectedCol = -1;
    private boolean isWhiteTurn = true;
    private List<Point> possibleMoves = new ArrayList<>();
    private JLabel player1NameLabel, player2NameLabel;
    private JLabel player1ScoreLabel, player2ScoreLabel;
    private JTextArea player1CapturedArea, player2CapturedArea;
    private Image playerPanelBackground;
    private JLabel timerLabel;
    private JLabel levelLabel;
    private Timer gameTimer;
    private int secondsElapsed = 0;
    private boolean isPaused = false;
    private int player1Score = 0;
    private int player2Score = 0;
    private int currentLevel = 1;
    private String gameMode;
    private List<String> player1CapturedPieces = new ArrayList<>();
    private List<String> player2CapturedPieces = new ArrayList<>();
    private Clip moveSound;
    private Clip captureSound;
    private Timer aiTimer;
    private boolean isAITurn = false;
    
    private static final int WHITE_PAWN = 0;
    private static final int WHITE_KNIGHT = 1;
    private static final int WHITE_QUEEN = 2;
    private static final int WHITE_KING = 3;
    private static final int BLACK_PAWN = 6;
    private static final int BLACK_KNIGHT = 7;
    private static final int BLACK_QUEEN = 8;
    private static final int BLACK_KING = 9;

    public ChessLevel11(String gameMode) {
        this.gameMode = gameMode;
        initializeFrame();
        loadPieceImages();
        loadPlayerPanelBackground();
        loadSounds();
        JPanel chessBoardPanel = createChessBoardPanel();
        JPanel playerInfoPanel = createPlayerInfoPanel();
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, playerInfoPanel);
        mainSplitPane.setResizeWeight(0.95);
        JPanel topPanel = createTopPanel();
        JPanel bottomPanel = createBottomPanel();
        add(topPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setPlayerNames();
        finalizeFrame();
        
        // If game mode is "free" (human vs AI), set AI to play as black
        if (gameMode.equals("free")) {
            isAITurn = false; // AI plays black (second player)
            setupAITimer();
        }
    }
    
    private void setupAITimer() {
        aiTimer = new Timer(1000, e -> {
            if (!isWhiteTurn && !isPaused && !isAITurn) {
                isAITurn = true;
                makeAIMove();
                isAITurn = false;
            }
        });
        aiTimer.start();
    }
    
    private void makeAIMove() {
        // Find all possible moves for AI pieces
        List<Move> allPossibleMoves = new ArrayList<>();
        
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                if (icon != null && !isWhitePiece(icon)) {
                    // This is a black piece (AI's piece)
                    selectedRow = row;
                    selectedCol = col;
                    calculatePossibleMoves(row, col);
                    
                    for (Point move : possibleMoves) {
                        allPossibleMoves.add(new Move(row, col, move.y, move.x));
                    }
                    
                    possibleMoves.clear();
                }
            }
        }
        
        if (!allPossibleMoves.isEmpty()) {
            // Simple AI: prioritize captures, then random moves
            List<Move> captureMoves = new ArrayList<>();
            List<Move> otherMoves = new ArrayList<>();
            
            for (Move move : allPossibleMoves) {
                ImageIcon targetIcon = (ImageIcon) squares[move.toRow][move.toCol].getIcon();
                if (targetIcon != null && isWhitePiece(targetIcon)) {
                    captureMoves.add(move);
                } else {
                    otherMoves.add(move);
                }
            }
            
            Move chosenMove;
            Random random = new Random();
            
            if (!captureMoves.isEmpty()) {
                // Choose a random capture move
                chosenMove = captureMoves.get(random.nextInt(captureMoves.size()));
            } else {
                // Choose a random move
                chosenMove = otherMoves.get(random.nextInt(otherMoves.size()));
            }
            
            // Execute the move
            selectedRow = chosenMove.fromRow;
            selectedCol = chosenMove.fromCol;
            calculatePossibleMoves(selectedRow, selectedCol);
            
            if (isValidMove(chosenMove.toRow, chosenMove.toCol)) {
                movePiece(selectedRow, selectedCol, chosenMove.toRow, chosenMove.toCol);
                isWhiteTurn = true; // Switch to human's turn
            }
            
            selectedRow = -1;
            selectedCol = -1;
            clearHighlights();
        }
    }
    
    private class Move {
        int fromRow, fromCol, toRow, toCol;
        
        public Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    private void loadSounds() {
        try {
            // Load move sound
            AudioInputStream moveAudio = AudioSystem.getAudioInputStream(
                getClass().getResource("/Sounds/chessmove.wav"));
            moveSound = AudioSystem.getClip();
            moveSound.open(moveAudio);
            
            // Load capture sound
            AudioInputStream captureAudio = AudioSystem.getAudioInputStream(
                getClass().getResource("/Sounds/chesscapture.wav"));
            captureSound = AudioSystem.getClip();
            captureSound.open(captureAudio);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading sound effects: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void playMoveSound() {
        if (moveSound != null) {
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    private void playCaptureSound() {
        if (captureSound != null) {
            captureSound.setFramePosition(0);
            captureSound.start();
        }
    }

    private void initializeFrame() {
        setTitle("Chess Game - Level 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }
    
    private void loadPlayerPanelBackground() {
        try {
            playerPanelBackground = ImageIO.read(getClass().getResource("/Images/image.png"));
        } catch (IOException e) {
            playerPanelBackground = null;
        }
    }

    private void setPlayerNames() {
        switch(gameMode) {
            case "free":
                player1NameLabel.setText("Player");
                player2NameLabel.setText("AI");
                break;
            case "1 vs 1":
                player1NameLabel.setText("Player 1");
                player2NameLabel.setText("Player 2");
                break;
            case "2 vs 2":
                player1NameLabel.setText("Team A");
                player2NameLabel.setText("Team B");
                break;
            default:
                player1NameLabel.setText("Player 1");
                player2NameLabel.setText("Player 2");
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("CHESS BOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 38));
        titleLabel.setForeground(Color.BLACK);
        levelLabel = new JLabel("Level: " + currentLevel, SwingConstants.RIGHT);
        levelLabel.setFont(new Font("Old English Text MT", Font.BOLD, 30));
        levelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(levelLabel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        newGameButton.setBackground(Color.decode("#49796B"));
        newGameButton.addActionListener(e -> startNewGame());
        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.PLAIN, 16));
        pauseButton.setBackground(Color.decode("#49796B"));
        pauseButton.addActionListener(e -> togglePause(pauseButton));
        JButton menuButton = new JButton("Back to Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 16));
        menuButton.setBackground(Color.decode("#49796B"));
        menuButton.addActionListener(e -> returnToMainMenu());
        buttonPanel.add(newGameButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(menuButton);
        panel.add(timerLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        Color lightPlayerColor = Color.decode("#E8F5E9");
        Color darkPlayerColor = Color.decode("#C8E6C9");
        panel.add(createPlayerPanel(lightPlayerColor));
        panel.add(createPlayerPanel(darkPlayerColor));
        return panel;
    }

    private JPanel createPlayerPanel(Color bg) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (playerPanelBackground != null) {
                    g.drawImage(playerPanelBackground, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setForeground(Color.WHITE);
        JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setForeground(Color.WHITE);
        JTextArea capturedArea = new JTextArea("Captured:\nNone", 5, 15);
        capturedArea.setFont(new Font("Arial", Font.BOLD, 16));
        capturedArea.setEditable(false);
        capturedArea.setOpaque(false);
        capturedArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(capturedArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        if (player1NameLabel == null) {
            player1NameLabel = nameLabel;
            player1ScoreLabel = scoreLabel;
            player1CapturedArea = capturedArea;
        } else {
            player2NameLabel = nameLabel;
            player2ScoreLabel = scoreLabel;
            player2CapturedArea = capturedArea;
        }
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createChessBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 8));
        panel.setBackground(Color.BLACK);
        Color lightSquare = Color.decode("#49796B");
        Color darkSquare = Color.decode("#FDECE0");
        String[][] layout = {
            {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
            {"bp", " ", " ", " ", " ", " ", " ", "bp"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"wp", " ", " ", " ", " ", " ", " ", "wp"},
            {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
        };
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = createChessSquare(layout[row][col], row, col, lightSquare, darkSquare);
                panel.add(squares[row][col]);
            }
        }
        return panel;
    }

    private JButton createChessSquare(String pieceCode, int row, int col, Color lightColor, Color darkColor) {
        JButton button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
        button.setPreferredSize(new Dimension(80, 80));
        switch (pieceCode) {
            case "wp" -> button.setIcon(pieceImages[WHITE_PAWN]);
            case "wn" -> button.setIcon(pieceImages[WHITE_KNIGHT]);
            case "wq" -> button.setIcon(pieceImages[WHITE_QUEEN]);
            case "wk" -> button.setIcon(pieceImages[WHITE_KING]);
            case "bp" -> button.setIcon(pieceImages[BLACK_PAWN]);
            case "bn" -> button.setIcon(pieceImages[BLACK_KNIGHT]);
            case "bq" -> button.setIcon(pieceImages[BLACK_QUEEN]);
            case "bk" -> button.setIcon(pieceImages[BLACK_KING]);
            default -> button.setIcon(null);
        }
        button.addActionListener(e -> handleSquareClick(row, col));
        return button;
    }

    private void handleSquareClick(int row, int col) {
        if (gameMode.equals("free") && !isWhiteTurn) {
            return; // Ignore clicks when it's AI's turn
        }
        
        if (selectedRow == -1) {
            ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
            if (icon != null) {
                boolean isWhitePiece = isWhitePiece(icon);
                if ((isWhitePiece && isWhiteTurn) || (!isWhitePiece && !isWhiteTurn)) {
                    selectedRow = row;
                    selectedCol = col;
                    calculatePossibleMoves(row, col);
                    highlightPossibleMoves();
                }
            }
        } else {
            if (isValidMove(row, col)) {
                movePiece(selectedRow, selectedCol, row, col);
                isWhiteTurn = !isWhiteTurn;
                
                // If in AI mode and it's now AI's turn, trigger AI move
                if (gameMode.equals("free") && !isWhiteTurn) {
                    SwingUtilities.invokeLater(() -> {
                        makeAIMove();
                        isWhiteTurn = true; // Switch back to human's turn
                    });
                }
            }
            selectedRow = -1;
            selectedCol = -1;
            clearHighlights();
        }
    }

    private boolean isWhitePiece(ImageIcon icon) {
        return icon == pieceImages[WHITE_PAWN] || 
               icon == pieceImages[WHITE_KNIGHT] || 
               icon == pieceImages[WHITE_QUEEN] || 
               icon == pieceImages[WHITE_KING];
    }
    
    private void calculatePossibleMoves(int row, int col) {
        possibleMoves.clear();
        ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
        
        if (icon == null) return;

        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) {
            // PAWN MOVEMENT (with backward movement)
            int forwardDir = (icon == pieceImages[WHITE_PAWN]) ? -1 : 1;
            int backwardDir = -forwardDir;
            
            // Forward movement
            if (isValidPosition(row + forwardDir, col) && squares[row + forwardDir][col].getIcon() == null) {
                possibleMoves.add(new Point(col, row + forwardDir));
                
                // Double move from starting position
                int startRow = (icon == pieceImages[WHITE_PAWN]) ? 4 : 1;
                if (row == startRow && squares[row + 2*forwardDir][col].getIcon() == null) {
                    possibleMoves.add(new Point(col, row + 2*forwardDir));
                }
            }
            
            // Backward movement (single square)
            if (isValidPosition(row + backwardDir, col) && squares[row + backwardDir][col].getIcon() == null) {
                possibleMoves.add(new Point(col, row + backwardDir));
            }
            
            // Forward captures
            for (int dc = -1; dc <= 1; dc += 2) {
                if (isValidPosition(row + forwardDir, col + dc)) {
                    ImageIcon targetIcon = (ImageIcon) squares[row + forwardDir][col + dc].getIcon();
                    if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(col + dc, row + forwardDir));
                    }
                }
            }
            
            // Backward captures
            for (int dc = -1; dc <= 1; dc += 2) {
                if (isValidPosition(row + backwardDir, col + dc)) {
                    ImageIcon targetIcon = (ImageIcon) squares[row + backwardDir][col + dc].getIcon();
                    if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(col + dc, row + backwardDir));
                    }
                }
            }
        }
        else if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) {
            // KNIGHT MOVEMENT (L-shape)
            int[][] knightMoves = {
                {-2, -1}, {-2, 1}, 
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
            };
            
            for (int[] move : knightMoves) {
                int newRow = row + move[0];
                int newCol = col + move[1];
                
                if (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    if (targetIcon == null || isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(newCol, newRow));
                    }
                }
            }
        }
        else if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) {
            // QUEEN MOVEMENT (vertical and diagonal only)
            // Vertical movement
            int[][] verticalDirections = {{-1, 0}, {1, 0}};
            
            for (int[] dir : verticalDirections) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                while (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    
                    if (targetIcon == null) {
                        possibleMoves.add(new Point(newCol, newRow));
                    } else {
                        if (isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(newCol, newRow));
                        }
                        break;
                    }
                    
                    newRow += dir[0];
                    newCol += dir[1];
                }
            }
            
            // Diagonal movement
            int[][] diagonalDirections = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            
            for (int[] dir : diagonalDirections) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                while (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    
                    if (targetIcon == null) {
                        possibleMoves.add(new Point(newCol, newRow));
                    } else {
                        if (isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(newCol, newRow));
                        }
                        break;
                    }
                    
                    newRow += dir[0];
                    newCol += dir[1];
                }
            }
        }
        else if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) {
            // KING MOVEMENT (one square in any direction)
            int[][] kingMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
            };
            
            for (int[] move : kingMoves) {
                int newRow = row + move[0];
                int newCol = col + move[1];
                
                if (isValidPosition(newRow, newCol)) {
                    ImageIcon targetIcon = (ImageIcon) squares[newRow][newCol].getIcon();
                    if (targetIcon == null || isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(newCol, newRow));
                    }
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 6 && col >= 0 && col < 8;
    }

    private boolean isValidMove(int row, int col) {
        for (Point move : possibleMoves) {
            if (move.y == row && move.x == col) {
                return true;
            }
        }
        return false;
    }

    private void highlightPossibleMoves() {
        Color highlightColor = new Color(255, 255, 0, 100);
        for (Point move : possibleMoves) {
            squares[move.y][move.x].setBackground(highlightColor);
        }
    }

    private void clearHighlights() {
        Color lightSquare = Color.decode("#49796B");
        Color darkSquare = Color.decode("#FDECE0");
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? lightSquare : darkSquare);
            }
        }
    }

    private void updateCapturedArea(JTextArea area, List<String> pieces) {
        StringBuilder sb = new StringBuilder("Captured:\n");
        if (pieces.isEmpty()) {
            sb.append("None");
        } else {
            for (String piece : pieces) {
                sb.append(piece).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private String getPieceName(ImageIcon icon) {
        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) return "Pawn";
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return "Knight";
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return "Queen";
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return "King";
        return "";
    }

    private int getPieceValue(ImageIcon icon) {
        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) return 1;
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return 3;
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return 9;
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return 0;
        return 0;
    }

    private void loadPieceImages() {
        try {
            pieceImages[WHITE_PAWN] = createScaledIcon("/images/whitePawn.png", 60);
            pieceImages[WHITE_KNIGHT] = createScaledIcon("/images/whiteKnight.png", 60);
            pieceImages[WHITE_QUEEN] = createScaledIcon("/images/whiteQueen.png", 60);
            pieceImages[WHITE_KING] = createScaledIcon("/images/whiteKing.png", 60);
            pieceImages[BLACK_PAWN] = createScaledIcon("/images/blackPawn.png", 60);
            pieceImages[BLACK_KNIGHT] = createScaledIcon("/images/blackKnight.png", 60);
            pieceImages[BLACK_QUEEN] = createScaledIcon("/images/blackQueen.png", 60);
            pieceImages[BLACK_KING] = createScaledIcon("/images/blackKing.png", 60);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading piece images: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            for (int i = 0; i < pieceImages.length; i++) {
                if (pieceImages[i] == null) {
                    pieceImages[i] = new ImageIcon();
                }
            }
        }
    }

    private ImageIcon createScaledIcon(String path, int size) throws IOException {
        BufferedImage image = ImageIO.read(getClass().getResource(path));
        Image scaled = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void finalizeFrame() {
        validate();
        setLocationRelativeTo(null);
        startTimer();
        setVisible(true);
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });
        gameTimer.start();
    }

    private void togglePause(JButton pauseButton) {
        if (isPaused) {
            gameTimer.start();
            if (gameMode.equals("free")) {
                aiTimer.start();
            }
            pauseButton.setText("Pause");
        } else {
            gameTimer.stop();
            if (gameMode.equals("free")) {
                aiTimer.stop();
            }
            pauseButton.setText("Resume");
        }
        isPaused = !isPaused;
    }

    private void startNewGame() {
        secondsElapsed = 0;
        timerLabel.setText("00:00");
        currentLevel = 1;
        levelLabel.setText("Level: " + currentLevel);
        player1Score = 0;
        player2Score = 0;
        player1CapturedPieces.clear();
        player2CapturedPieces.clear();
        player1ScoreLabel.setText("Score: 0");
        player2ScoreLabel.setText("Score: 0");
        player1CapturedArea.setText("Captured:\nNone");
        player2CapturedArea.setText("Captured:\nNone");
        setPlayerNames();
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (aiTimer != null) {
            aiTimer.stop();
        }
       
        startTimer();
        if (gameMode.equals("free")) {
            setupAITimer();
        }
        
        String[][] layout = {
            {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
            {"bp", " ", " ", " ", " ", " ", " ", "bp"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"wp", " ", " ", " ", " ", " ", " ", "wp"},
            {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
        };
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                switch (layout[row][col]) {
                    case "wp" -> squares[row][col].setIcon(pieceImages[WHITE_PAWN]);
                    case "wn" -> squares[row][col].setIcon(pieceImages[WHITE_KNIGHT]);
                    case "wq" -> squares[row][col].setIcon(pieceImages[WHITE_QUEEN]);
                    case "wk" -> squares[row][col].setIcon(pieceImages[WHITE_KING]);
                    case "bp" -> squares[row][col].setIcon(pieceImages[BLACK_PAWN]);
                    case "bn" -> squares[row][col].setIcon(pieceImages[BLACK_KNIGHT]);
                    case "bq" -> squares[row][col].setIcon(pieceImages[BLACK_QUEEN]);
                    case "bk" -> squares[row][col].setIcon(pieceImages[BLACK_KING]);
                    default -> squares[row][col].setIcon(null);
                }
            }
        }
             
        isWhiteTurn = true;
        selectedRow = -1;
        selectedCol = -1;
        clearHighlights();
    }
    
    private boolean hasPlayerWon(boolean isWhite) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                if (icon != null) {
                    boolean pieceIsWhite = isWhitePiece(icon);
                    if ((isWhite && !pieceIsWhite) || (!isWhite && pieceIsWhite)) {
                        return false; // Found at least one opponent piece
                    }
                }
            }
        }
        return true; // No opponent pieces found
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        ImageIcon targetIcon = (ImageIcon) squares[toRow][toCol].getIcon();
        if (targetIcon != null) {
            // Play capture sound
            playCaptureSound();
            
            String pieceName = getPieceName(targetIcon);
            int pieceValue = getPieceValue(targetIcon);
            
            if (isWhiteTurn) {
                player1Score += pieceValue;
                player1CapturedPieces.add(pieceName);
                updateCapturedArea(player1CapturedArea, player1CapturedPieces);
            } else {
                player2Score += pieceValue;
                player2CapturedPieces.add(pieceName);
                updateCapturedArea(player2CapturedArea, player2CapturedPieces);
            }
            
            player1ScoreLabel.setText("Score: " + player1Score);
            player2ScoreLabel.setText("Score: " + player2Score);
        } else {
            // Play move sound only if not capturing
            playMoveSound();
        }
        
        squares[toRow][toCol].setIcon(squares[fromRow][fromCol].getIcon());
        squares[fromRow][fromCol].setIcon(null);
        
        // Check for win condition after move
        if (hasPlayerWon(isWhiteTurn)) {
            String winner = isWhiteTurn ? player1NameLabel.getText() : player2NameLabel.getText();
            JOptionPane.showMessageDialog(this, 
                winner + " has won by capturing all pieces! Moving to level 2...",
                "Level Complete",
                JOptionPane.INFORMATION_MESSAGE);
            advanceToNextLevel();
        }
    }

    protected void advanceToNextLevel() {
        this.dispose();
        new ChessGameLevel22(gameMode).setVisible(true);
    }

    private void returnToMainMenu() {
        this.dispose();
        new SuperChessGame2();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChessLevel11("1 vs 1");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}



