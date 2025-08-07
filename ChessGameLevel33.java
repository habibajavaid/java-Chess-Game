/*@Habiba Javaid
Roll No:0030
*/



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.sound.sampled.*;

public class ChessGameLevel33 extends JFrame {
    private JButton[][] squares = new JButton[8][8]; // 8×8 board
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
    private final int currentLevel = 3; // Made final since this is the last level
    private List<String> player1CapturedPieces = new ArrayList<>();
    private List<String> player2CapturedPieces = new ArrayList<>();
    private int consecutiveWins = 0;
    private int bonusPoints = 0;
    private Clip moveSound;
    private Clip captureSound;
    private boolean gameOver = false;
    
    // Piece indices
    private static final int WHITE_PAWN = 0;
    private static final int WHITE_ROOK = 1;
    private static final int WHITE_KNIGHT = 2;
    private static final int WHITE_BISHOP = 3;
    private static final int WHITE_QUEEN = 4;
    private static final int WHITE_KING = 5;
    private static final int BLACK_PAWN = 6;
    private static final int BLACK_ROOK = 7;
    private static final int BLACK_KNIGHT = 8;
    private static final int BLACK_BISHOP = 9;
    private static final int BLACK_QUEEN = 10;
    private static final int BLACK_KING = 11;

    public ChessGameLevel33(String gameMode) {
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
            System.err.println("Error loading sound effects: " + e.getMessage());
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
        setTitle("Chess Game - Final Level (Full Chess)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private void loadPlayerPanelBackground() {
        try {
            playerPanelBackground = ImageIO.read(getClass().getResource("/images/image.png"));
        } catch (IOException e) {
            playerPanelBackground = null;
        }
    }

    private void setPlayerNames() {
        player1NameLabel.setText("Player 1");
        player2NameLabel.setText("Player 2");
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("CHESS BOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 38));
        titleLabel.setForeground(Color.BLACK);
        levelLabel = new JLabel("Level: " + currentLevel + " (Final)", SwingConstants.RIGHT);
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
        JPanel panel = new JPanel(new GridLayout(8, 8));
        panel.setBackground(Color.BLACK);
        Color lightSquare = Color.decode("#49796B");
        Color darkSquare = Color.decode("#FDECE0");
        String[][] layout = {
            {"br","bn","bb","bq","bk","bb","bn","br"},
            {"bp","bp","bp","bp","bp","bp","bp","bp"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"wp","wp","wp","wp","wp","wp","wp","wp"},
            {"wr","wn","wb","wq","wk","wb","wn","wr"}
        };
        for (int row = 0; row < 8; row++) {
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
            case "wr" -> button.setIcon(pieceImages[WHITE_ROOK]);
            case "wn" -> button.setIcon(pieceImages[WHITE_KNIGHT]);
            case "wb" -> button.setIcon(pieceImages[WHITE_BISHOP]);
            case "wq" -> button.setIcon(pieceImages[WHITE_QUEEN]);
            case "wk" -> button.setIcon(pieceImages[WHITE_KING]);
            case "bp" -> button.setIcon(pieceImages[BLACK_PAWN]);
            case "br" -> button.setIcon(pieceImages[BLACK_ROOK]);
            case "bn" -> button.setIcon(pieceImages[BLACK_KNIGHT]);
            case "bb" -> button.setIcon(pieceImages[BLACK_BISHOP]);
            case "bq" -> button.setIcon(pieceImages[BLACK_QUEEN]);
            case "bk" -> button.setIcon(pieceImages[BLACK_KING]);
            default -> button.setIcon(null);
        }
        button.addActionListener(e -> handleSquareClick(row, col));
        return button;
    }

    private void handleSquareClick(int row, int col) {
        if (gameOver) return;
        
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
                
                // Check for checkmate after each move
                boolean currentPlayerIsWhite = !isWhiteTurn; // Because we just switched turns
                if (isCheckmate(currentPlayerIsWhite)) {
                    gameOver = true;
                    String winner = currentPlayerIsWhite ? player2NameLabel.getText() : player1NameLabel.getText();
                    consecutiveWins++;
                    bonusPoints = consecutiveWins * 50;
                    
                    if (currentPlayerIsWhite) {
                        player2Score += bonusPoints;
                        player2ScoreLabel.setText("Score: " + player2Score + " (+" + bonusPoints + " bonus)");
                    } else {
                        player1Score += bonusPoints;
                        player1ScoreLabel.setText("Score: " + player1Score + " (+" + bonusPoints + " bonus)");
                    }
                    
                    JOptionPane.showMessageDialog(this, 
                        winner + " has won by checkmate!\n" +
                        "Game over - this was the final level.",
                        "Checkmate!",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            selectedRow = -1;
            selectedCol = -1;
            clearHighlights();
        }
    }

    private boolean isWhitePiece(ImageIcon icon) {
        return icon == pieceImages[WHITE_PAWN] || 
               icon == pieceImages[WHITE_ROOK] || 
               icon == pieceImages[WHITE_KNIGHT] || 
               icon == pieceImages[WHITE_BISHOP] || 
               icon == pieceImages[WHITE_QUEEN] || 
               icon == pieceImages[WHITE_KING];
    }
    
    private void calculatePossibleMoves(int row, int col) {
        possibleMoves.clear();
        ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
        
        if (icon == null) return;

        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) {
            // PAWN MOVEMENT
            int forwardDir = (icon == pieceImages[WHITE_PAWN]) ? -1 : 1;
            
            // Forward movement
            if (isValidPosition(row + forwardDir, col) && squares[row + forwardDir][col].getIcon() == null) {
                possibleMoves.add(new Point(col, row + forwardDir));
                
                // Double move from starting position
                int startRow = (icon == pieceImages[WHITE_PAWN]) ? 6 : 1;
                if (row == startRow && squares[row + 2*forwardDir][col].getIcon() == null) {
                    possibleMoves.add(new Point(col, row + 2*forwardDir));
                }
            }
            
            // Captures
            for (int dc = -1; dc <= 1; dc += 2) {
                if (isValidPosition(row + forwardDir, col + dc)) {
                    ImageIcon targetIcon = (ImageIcon) squares[row + forwardDir][col + dc].getIcon();
                    if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                        possibleMoves.add(new Point(col + dc, row + forwardDir));
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
        else if (icon == pieceImages[WHITE_ROOK] || icon == pieceImages[BLACK_ROOK]) {
            // ROOK MOVEMENT (horizontal and vertical)
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            
            for (int[] dir : directions) {
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
        else if (icon == pieceImages[WHITE_BISHOP] || icon == pieceImages[BLACK_BISHOP]) {
            // BISHOP MOVEMENT (diagonal)
            int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            
            for (int[] dir : directions) {
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
        else if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) {
            // QUEEN MOVEMENT (horizontal, vertical and diagonal)
            int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Rook moves
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Bishop moves
            };
            
            for (int[] dir : directions) {
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
        return row >= 0 && row < 8 && col >= 0 && col < 8;
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
        for (int row = 0; row < 8; row++) {
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
        if (icon == pieceImages[WHITE_ROOK] || icon == pieceImages[BLACK_ROOK]) return "Rook";
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return "Knight";
        if (icon == pieceImages[WHITE_BISHOP] || icon == pieceImages[BLACK_BISHOP]) return "Bishop";
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return "Queen";
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return "King";
        return "";
    }

    private int getPieceValue(ImageIcon icon) {
        if (icon == pieceImages[WHITE_PAWN] || icon == pieceImages[BLACK_PAWN]) return 1;
        if (icon == pieceImages[WHITE_ROOK] || icon == pieceImages[BLACK_ROOK]) return 5;
        if (icon == pieceImages[WHITE_KNIGHT] || icon == pieceImages[BLACK_KNIGHT]) return 3;
        if (icon == pieceImages[WHITE_BISHOP] || icon == pieceImages[BLACK_BISHOP]) return 3;
        if (icon == pieceImages[WHITE_QUEEN] || icon == pieceImages[BLACK_QUEEN]) return 9;
        if (icon == pieceImages[WHITE_KING] || icon == pieceImages[BLACK_KING]) return 0;
        return 0;
    }

    private void loadPieceImages() {
        try {
            pieceImages[WHITE_PAWN] = createScaledIcon("/images/whitePawn.png", 60);
            pieceImages[WHITE_ROOK] = createScaledIcon("/images/whiteRook.png", 60);
            pieceImages[WHITE_KNIGHT] = createScaledIcon("/images/whiteKnight.png", 60);
            pieceImages[WHITE_BISHOP] = createScaledIcon("/images/whiteBishop.png", 60);
            pieceImages[WHITE_QUEEN] = createScaledIcon("/images/whiteQueen.png", 60);
            pieceImages[WHITE_KING] = createScaledIcon("/images/whiteKing.png", 60);
            pieceImages[BLACK_PAWN] = createScaledIcon("/images/blackPawn.png", 60);
            pieceImages[BLACK_ROOK] = createScaledIcon("/images/blackRook.png", 60);
            pieceImages[BLACK_KNIGHT] = createScaledIcon("/images/blackKnight.png", 60);
            pieceImages[BLACK_BISHOP] = createScaledIcon("/images/blackBishop.png", 60);
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
            if (!isPaused && !gameOver) {
                secondsElapsed++;
                int minutes = secondsElapsed / 60;
                int seconds = secondsElapsed % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            }
        });
        gameTimer.start();
    }

    private void togglePause(JButton pauseButton) {
        if (isPaused) {
            pauseButton.setText("Pause");
        } else {
            pauseButton.setText("Resume");
        }
        isPaused = !isPaused;
    }

    private void startNewGame() {
        secondsElapsed = 0;
        timerLabel.setText("00:00");
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
        gameOver = false;
        
        String[][] layout = {
            {"br","bn","bb","bq","bk","bb","bn","br"},
            {"bp","bp","bp","bp","bp","bp","bp","bp"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"wp","wp","wp","wp","wp","wp","wp","wp"},
            {"wr","wn","wb","wq","wk","wb","wn","wr"}
        };
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (layout[row][col]) {
                    case "wp" -> squares[row][col].setIcon(pieceImages[WHITE_PAWN]);
                    case "wr" -> squares[row][col].setIcon(pieceImages[WHITE_ROOK]);
                    case "wn" -> squares[row][col].setIcon(pieceImages[WHITE_KNIGHT]);
                    case "wb" -> squares[row][col].setIcon(pieceImages[WHITE_BISHOP]);
                    case "wq" -> squares[row][col].setIcon(pieceImages[WHITE_QUEEN]);
                    case "wk" -> squares[row][col].setIcon(pieceImages[WHITE_KING]);
                    case "bp" -> squares[row][col].setIcon(pieceImages[BLACK_PAWN]);
                    case "br" -> squares[row][col].setIcon(pieceImages[BLACK_ROOK]);
                    case "bn" -> squares[row][col].setIcon(pieceImages[BLACK_KNIGHT]);
                    case "bb" -> squares[row][col].setIcon(pieceImages[BLACK_BISHOP]);
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
        consecutiveWins = 0;
        bonusPoints = 0;
    }
    
    private boolean isKingInCheck(boolean isWhite) {
        // Find the king's position
        int kingRow = -1, kingCol = -1;
        int kingIndex = isWhite ? WHITE_KING : BLACK_KING;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col].getIcon() == pieceImages[kingIndex]) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        if (kingRow == -1) return true; // King not found (shouldn't happen in proper chess)
        
        // Check if any opponent's piece can attack the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                if (icon != null && isWhitePiece(icon) != isWhite) {
                    // Temporarily calculate possible moves for this piece
                    List<Point> moves = new ArrayList<>();
                    calculatePossibleMoves(row, col);
                    moves.addAll(possibleMoves);
                    
                    for (Point move : moves) {
                        if (move.y == kingRow && move.x == kingCol) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean isCheckmate(boolean isWhite) {
        if (!isKingInCheck(isWhite)) return false;
        
        // Check if any move can get the king out of check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                if (icon != null && isWhitePiece(icon) == isWhite) {
                    // Get all possible moves for this piece
                    calculatePossibleMoves(row, col);
                    List<Point> moves = new ArrayList<>(possibleMoves);
                    
                    for (Point move : moves) {
                        // Simulate the move
                        ImageIcon targetIcon = (ImageIcon) squares[move.y][move.x].getIcon();
                        squares[move.y][move.x].setIcon(icon);
                        squares[row][col].setIcon(null);
                        
                        // Check if king is still in check after this move
                        boolean stillInCheck = isKingInCheck(isWhite);
                        
                        // Undo the move
                        squares[row][col].setIcon(icon);
                        squares[move.y][move.x].setIcon(targetIcon);
                        
                        if (!stillInCheck) {
                            return false; // Found a move that gets out of check
                        }
                    }
                }
            }
        }
        
        return true; // No moves get out of check - checkmate
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        ImageIcon targetIcon = (ImageIcon) squares[toRow][toCol].getIcon();
        if (targetIcon != null) {
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
            playMoveSound();
        }
        
        squares[toRow][toCol].setIcon(squares[fromRow][fromCol].getIcon());
        squares[fromRow][fromCol].setIcon(null);
    }

    private void returnToMainMenu() {
        this.dispose();
        new SuperChessGame2();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChessGameLevel33("Full Game");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}