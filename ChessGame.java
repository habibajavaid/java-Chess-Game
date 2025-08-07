/*@Habiba Javaid 
Roll No:0030
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ChessGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Screen identifiers
    private static final String START_SCREEN = "START";
    private static final String MAIN_MENU = "MAIN";
    private static final String SETTINGS = "SETTINGS";
    private static final String PLAYER_SELECT = "PLAYER_SELECT";
    private static final String LEVEL1 = "LEVEL1";
    private static final String LEVEL2 = "LEVEL2";
    private static final String LEVEL3 = "LEVEL3";
    
    // Shared components
    private Clip backgroundMusicClip;
    private Preferences prefs;
    
    // Game state
    private String currentGameMode;
    private int player1Score = 0;
    private int player2Score = 0;
    
    public ChessGame() {
        prefs = Preferences.userNodeForPackage(ChessGame.class);
        initializeFrame();
        setupCardLayout();
        setupScreens();
        showStartScreen();
    }
    
    private void initializeFrame() {
        setTitle("Super Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLocationRelativeTo(null);
    }
    
    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);
    }
    
    private void setupScreens() {
        cardPanel.add(createStartScreen(), START_SCREEN);
        cardPanel.add(createMainMenu(), MAIN_MENU);
        cardPanel.add(createSettingsScreen(), SETTINGS);
        cardPanel.add(createPlayerSelectionScreen(), PLAYER_SELECT);
        cardPanel.add(createChessLevel1(), LEVEL1);
        cardPanel.add(createChessLevel2(), LEVEL2);
        cardPanel.add(createChessLevel3(), LEVEL3);
    }
    
    private void showStartScreen() {
        cardLayout.show(cardPanel, START_SCREEN);
        playBackgroundMusic("sounds/game.wav");
    }
    
    private void showScreen(String screen) {
        cardLayout.show(cardPanel, screen);
    }
    
    private JPanel createStartScreen() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image bgImage = new ImageIcon(getClass().getResource("/Images/chess_start.png")).getImage();
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(60, 60, 90));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel title = new JLabel("Super Chess Game");
        title.setFont(new Font("Old English Text MT", Font.BOLD, 100));
        title.setForeground(new Color(255, 230, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 30));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 150, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(70, 130, 180));
            }
        });

        startButton.addActionListener(e -> showScreen(MAIN_MENU));

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPanel.add(startButton);
        contentPanel.add(Box.createVerticalGlue());

        panel.add(contentPanel);
        return panel;
    }
    
    private JPanel createMainMenu() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image bgImage = new ImageIcon(getClass().getResource("/Images/chess_background.png")).getImage();
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 150, 50, 150));

        JLabel title = new JLabel("Super Chess Game");
        title.setFont(new Font("Old English Text MT", Font.BOLD, 72));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.ORANGE.darker());
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        panel.add(title);

        addMenuButton(panel, "New Game", new Color(34, 139, 34, 220), e -> showScreen(PLAYER_SELECT));
        addMenuButton(panel, "MultiPlayer", new Color(128, 0, 128, 220), e -> showScreen(PLAYER_SELECT));
        addMenuButton(panel, "Settings", new Color(70, 70, 70, 220), e -> showScreen(SETTINGS));

        JButton helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 14));
        helpButton.setBackground(new Color(0, 50, 0));
        helpButton.setFocusPainted(false);
        helpButton.setBorderPainted(false);
        helpButton.setForeground(Color.WHITE);
        helpButton.setOpaque(true);
        helpButton.addActionListener(e -> showHelpDialog());
        helpButton.setBounds(20, 20, 90, 40);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(helpButton, JLayeredPane.PALETTE_LAYER);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(layeredPane, BorderLayout.CENTER);
        return container;
    }
    
    private void addMenuButton(JPanel panel, String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 26));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(350, 60));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(original.getRed(), original.getGreen(), original.getBlue(), 255));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });
        button.addActionListener(action);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
    }
    
    private void showHelpDialog() {
        JDialog dialog = new JDialog(this, "Game Help", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        JEditorPane helpContent = new JEditorPane();
        helpContent.setContentType("text/html");
        helpContent.setText(
            "<html><body style='font-family:Arial; font-size:12px; padding:10px;'>" +
            "<h2><b>Welcome! Super Chess</b></h2>" +
            "<p>A fun version of traditional chess with new rules. Designed for children or adults looking to enjoy a quick game and pass time.</p>" +
            "<h3 style='color:green;'>Objective</h3>" +
            "<ul><li>Checkmate the opponent's King.</li></ul>" +
            "<h3 style='color:green;'>Piece Movements</h3>" +
            "<ul>" +
            "<li><b>King:</b> One square in any direction</li>" +
            "<li><b>Queen:</b> Any number of squares vertically or diagonally</li>" +
            "<li><b>Rook:</b> 4 squares horizontally or vertically (Note: Standard chess rooks move any number of squares. This is a SuperChess rule)</li>" +
            "<li><b>Bishop:</b> 3 squares diagonally (Note: Standard chess bishops move any number of squares. This is a SuperChess rule)</li>" +
            "<li><b>Knight:</b> 'L' shape (2 squares in one cardinal direction, then 1 square perpendicular)</li>" +
            "<li><b>Pawn:</b> 1 square forward (can also move backward in SuperChess). 2 squares on first move. Captures 1 square diagonally forward.</li>" +
            "</ul>" +
            "<h3 style='color:green;'>Super Chess Rules</h3>" +
            "<ul>" +
            "<li>Levels with varying numbers of pieces.</li>" +
            "<li><b>Capture Bonus:</b> Capturing a piece gives an extra move and points.</li>" +
            "<li><b>Level 2/3 Random Bonus:</b> Chance for extra turns or reviving a captured piece.</li>" +
            "<li>No castling or en passant (standard chess rules not implemented here).</li>" +
            "</ul>" +
            "</body></html>"
        );
        helpContent.setEditable(false);
        dialog.add(new JScrollPane(helpContent));
        dialog.setVisible(true);
    }
    
    private JPanel createSettingsScreen() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL imageUrl = getClass().getResource("/images/setting_chess.png");
                    if (imageUrl != null) {
                        Image image = new ImageIcon(imageUrl).getImage();
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    }
                } catch (Exception e) {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 150, 100, 150));
        mainPanel.setOpaque(false);

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Old English Text MT", Font.BOLD, 80));
        title.setForeground(Color.BLACK.darker());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 80)));

        JSlider volumeSlider = new JSlider(0, 100, prefs.getInt("volume", 75));
        volumeSlider.setForeground(Color.GREEN.darker());
        volumeSlider.setBackground(null);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(volumeSlider) {
            @Override
            public void paintThumb(Graphics g) {
                Rectangle knobBounds = thumbRect;
                g.setColor(Color.GREEN.darker());
                g.fillRect(knobBounds.x, knobBounds.y, knobBounds.width, knobBounds.height);
            }
            @Override
            public void paintTrack(Graphics g) {
                Rectangle trackBounds = trackRect;
                g.setColor(Color.GRAY);
                g.fillRect(trackBounds.x, trackBounds.y + (trackBounds.height / 2) - 2, trackBounds.width, 4);
            }
        });
        addSetting(mainPanel, "Volume", volumeSlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JCheckBox soundCheckBox = new JCheckBox("Enable Sound");
        soundCheckBox.setFont(new Font("Arial", Font.PLAIN, 24));
        soundCheckBox.setForeground(Color.GREEN.darker());
        soundCheckBox.setOpaque(false);
        soundCheckBox.setSelected(prefs.getBoolean("soundEnabled", true));
        addSetting(mainPanel, "Sound", soundCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        String[] times = {"No Limit", "10 minutes", "30 minutes", "1 hour"};
        JComboBox<String> timeCombo = new JComboBox<>(times);
        timeCombo.setFont(new Font("Arial", Font.PLAIN, 20));
        timeCombo.setForeground(Color.GREEN.darker());
        timeCombo.setBackground(Color.WHITE);
        timeCombo.setSelectedItem(prefs.get("timeControl", "No Limit"));
        addSetting(mainPanel, "Time Control", timeCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JCheckBox saveDataCheckBox = new JCheckBox("Enable Auto-Save");
        saveDataCheckBox.setFont(new Font("Arial", Font.PLAIN, 24));
        saveDataCheckBox.setForeground(Color.GREEN.darker());
        saveDataCheckBox.setOpaque(false);
        saveDataCheckBox.setSelected(prefs.getBoolean("autoSave", false));
        addSetting(mainPanel, "Save Data", saveDataCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        JButton saveButton = new JButton("Save Settings");
        styleButton(saveButton, new Color(50, 120, 50));
        saveButton.addActionListener(e -> {
            prefs.putInt("volume", volumeSlider.getValue());
            prefs.putBoolean("soundEnabled", soundCheckBox.isSelected());
            prefs.put("timeControl", (String)timeCombo.getSelectedItem());
            prefs.putBoolean("autoSave", saveDataCheckBox.isSelected());
            
            // Apply volume setting to background music
            if (backgroundMusicClip != null) {
                FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                float volume = volumeSlider.getValue() / 100f;
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
            
            JOptionPane.showMessageDialog(this, "Settings saved and applied!");
        });
        mainPanel.add(saveButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        JButton backButton = new JButton("Back to Menu");
        styleButton(backButton, Color.GREEN.darker());
        backButton.addActionListener(e -> showScreen(MAIN_MENU));
        mainPanel.add(backButton);

        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private void addSetting(JPanel panel, String label, JComponent component) {
        JPanel settingPanel = new JPanel(new BorderLayout(20, 0));
        settingPanel.setOpaque(false);
        settingPanel.setMaximumSize(new Dimension(800, 60));
        
        JLabel labelComponent = new JLabel(label + ": ");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 28));
        labelComponent.setForeground(Color.BLACK.darker());
        
        settingPanel.add(labelComponent, BorderLayout.WEST);
        settingPanel.add(component, BorderLayout.CENTER);
        panel.add(settingPanel);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(250, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(original.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(original); }
        });
    }
    
    private JPanel createPlayerSelectionScreen() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL imageUrl = getClass().getResource("/Images/player_Chess.png");
                    if (imageUrl != null) {
                        Image image = new ImageIcon(imageUrl).getImage();
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    }
                } catch (Exception e) {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
              public void setPlayerNames(String player1, String player2) {
    if (player1 == null || player2 == null) {
        throw new IllegalArgumentException("Player names cannot be null");
    }
    // rest of your code
}
            
        };

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 13));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        mainPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Player Selection");
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.YELLOW.darker());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        mainPanel.add(titleLabel);
        addOption(mainPanel, "0   0   0", "No teams - Free for all", "free");
        addOption(mainPanel, "1 vs 1", "Classic chess", "1 vs 1");
        addOption(mainPanel, "2 vs 2", "Team chess", "2 vs 2");
         
        addBackButton(mainPanel);

        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private void addOption(JPanel panel, String title, String description, final String gameMode) {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionPanel.setOpaque(false);

        JButton button = new JButton(title);
        button.setFont(new Font("Arial", Font.BOLD, 36));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(350, 70));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(73, 121, 104));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> startGame(gameMode));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(53, 101, 84));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(73, 121, 104));
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setForeground(Color.WHITE);

        optionPanel.add(button);
        optionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionPanel.add(descLabel);

        panel.add(optionPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
    }
    
    private void addBackButton(JPanel panel) {
        JButton backButton = new JButton("< Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 28));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(250, 50));
        backButton.setBackground(new Color(73, 121, 104));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> showScreen(MAIN_MENU));

        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(backButton);
    }
    
    private void startGame(String gameMode) {
        this.currentGameMode = gameMode;
        showScreen(LEVEL1);
    }
    
    private JPanel createChessLevel1() {
        ChessLevelPanel levelPanel = new ChessLevelPanel(1, 6, 8, 
            new String[][] {
                {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
                {"bp", " ", " ", " ", " ", " ", " ", "bp"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"wp", " ", " ", " ", " ", " ", " ", "wp"},
                {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
            },
            new String[] {
                "whitePawn.png", "whiteKnight.png", "whiteQueen.png", "whiteKing.png",
                "blackPawn.png", "blackKnight.png", "blackQueen.png", "blackKing.png"
            },
            this::advanceToLevel2
        );
        return levelPanel;
    }
    
    private JPanel createChessLevel2() {
        ChessLevelPanel levelPanel = new ChessLevelPanel(2, 7, 8, 
            new String[][] {
                {" ", "bn", " ", "bq", "bk", " ", " ", "bn"},
                {"bp", " ", "bp", " ", "bp", " ", "bp", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"wp", " ", "wp", " ", "wp", " ", "wp", " "},
                {" ", "wn", " ", "wq", "wk", " ", " ", "wn"}
            },
            new String[] {
                "whitePawn.png", "whiteKnight.png", "whiteQueen.png", "whiteKing.png",
                "blackPawn.png", "blackKnight.png", "blackQueen.png", "blackKing.png",
                "whiteRook.png", "whiteBishop.png", "blackRook.png", "blackBishop.png"
            },
            this::advanceToLevel3
        );
        return levelPanel;
    }
    
    private JPanel createChessLevel3() {
        ChessLevelPanel levelPanel = new ChessLevelPanel(3, 8, 8, 
            new String[][] {
                {"br","bn","bb","bq","bk","bb","bn","br"},
                {"bp","bp","bp","bp","bp","bp","bp","bp"},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {"wp","wp","wp","wp","wp","wp","wp","wp"},
                {"wr","wn","wb","wq","wk","wb","wn","wr"}
            },
            new String[] {
                "whitePawn.png", "whiteRook.png", "whiteKnight.png", "whiteBishop.png",
                "whiteQueen.png", "whiteKing.png", "blackPawn.png", "blackRook.png",
                "blackKnight.png", "blackBishop.png", "blackQueen.png", "blackKing.png"
            },
            this::returnToMainMenu
        );
        return levelPanel;
    }
    
    private void advanceToLevel2() {
        player1Score = ((ChessLevelPanel)cardPanel.getComponent(4)).getPlayer1Score();
        player2Score = ((ChessLevelPanel)cardPanel.getComponent(4)).getPlayer2Score();
        showScreen(LEVEL2);
    }
    
    private void advanceToLevel3() {
        player1Score = ((ChessLevelPanel)cardPanel.getComponent(5)).getPlayer1Score();
        player2Score = ((ChessLevelPanel)cardPanel.getComponent(5)).getPlayer2Score();
        showScreen(LEVEL3);
    }
    
    private void returnToMainMenu() {
        showScreen(MAIN_MENU);
    }
    
    private void playBackgroundMusic(String filePath) {
        try {
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(filePath);
            if (audioSrc == null) {
                System.err.println("Could not find audio file: " + filePath);
                System.err.println("Trying absolute path...");
                
                try {
                    File audioFile = new File(filePath);
                    if (audioFile.exists()) {
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        backgroundMusicClip = AudioSystem.getClip();
                        backgroundMusicClip.open(audioStream);
                        
                        // Apply saved volume
                        FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                        float volume = prefs.getInt("volume", 75) / 100f;
                        float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                        gainControl.setValue(dB);
                        
                        backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Also failed to load from absolute path: " + e.getMessage());
                }
                return;
            }
            
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            
            // Apply saved volume
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = prefs.getInt("volume", 75) / 100f;
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ChessGame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private class ChessLevelPanel extends JPanel {
        private JButton[][] squares;
        private ImageIcon[] pieceImages;
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
        private int currentLevel;
        private List<String> player1CapturedPieces = new ArrayList<>();
        private List<String> player2CapturedPieces = new ArrayList<>();
        private Clip moveSound;
        private Clip captureSound;
        private Runnable onLevelComplete;
        
        public ChessLevelPanel(int level, int rows, int cols, String[][] layout, String[] pieceImageFiles, Runnable onLevelComplete) {
            this.currentLevel = level;
            this.onLevelComplete = onLevelComplete;
            this.player1Score = ChessGame.this.player1Score;
            this.player2Score = ChessGame.this.player2Score;
            
            setLayout(new BorderLayout());
            loadPieceImages(pieceImageFiles);
            loadPlayerPanelBackground();
            loadSounds();
            
            JPanel chessBoardPanel = createChessBoardPanel(rows, cols, layout);
            JPanel playerInfoPanel = createPlayerInfoPanel();
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, playerInfoPanel);
            mainSplitPane.setResizeWeight(0.95);
            
            JPanel topPanel = createTopPanel();
            JPanel bottomPanel = createBottomPanel();
            
            add(topPanel, BorderLayout.NORTH);
            add(mainSplitPane, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
            
            setPlayerNames();
            startTimer();
        }
        
        public int getPlayer1Score() {
            return player1Score;
        }
        
        public int getPlayer2Score() {
            return player2Score;
        }
        
        private void loadSounds() {
            try {
                AudioInputStream moveAudio = AudioSystem.getAudioInputStream(
                    getClass().getResource("/Sounds/chessmove.wav"));
                moveSound = AudioSystem.getClip();
                moveSound.open(moveAudio);
                
                AudioInputStream captureAudio = AudioSystem.getAudioInputStream(
                    getClass().getResource("/Sounds/chesscapture.wav"));
                captureSound = AudioSystem.getClip();
                captureSound.open(captureAudio);
            } catch (Exception e) {
                System.err.println("Error loading sound effects: " + e.getMessage());
            }
        }

        private void playMoveSound() {
            if (moveSound != null && prefs.getBoolean("soundEnabled", true)) {
                moveSound.setFramePosition(0);
                moveSound.start();
            }
        }

        private void playCaptureSound() {
            if (captureSound != null && prefs.getBoolean("soundEnabled", true)) {
                captureSound.setFramePosition(0);
                captureSound.start();
            }
        }

        private void loadPlayerPanelBackground() {
            try {
                playerPanelBackground = ImageIO.read(getClass().getResource("/images/image.png"));
            } catch (IOException e) {
                playerPanelBackground = null;
            }
        }

        private void setPlayerNames() {
            switch(currentGameMode) {
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
            JLabel scoreLabel = new JLabel("Score: " + (nameLabel == player1NameLabel ? player1Score : player2Score), SwingConstants.CENTER);
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

        private JPanel createChessBoardPanel(int rows, int cols, String[][] layout) {
            squares = new JButton[rows][cols];
            JPanel panel = new JPanel(new GridLayout(rows, cols));
            panel.setBackground(Color.BLACK);
            Color lightSquare = Color.decode("#49796B");
            Color darkSquare = Color.decode("#FDECE0");
            
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
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
                case "wp" -> button.setIcon(pieceImages[0]);
                case "wn" -> button.setIcon(pieceImages[1]);
                case "wq" -> button.setIcon(pieceImages[2]);
                case "wk" -> button.setIcon(pieceImages[3]);
                case "bp" -> button.setIcon(pieceImages[4]);
                case "bn" -> button.setIcon(pieceImages[5]);
                case "bq" -> button.setIcon(pieceImages[6]);
                case "bk" -> button.setIcon(pieceImages[7]);
                case "wr" -> button.setIcon(pieceImages[8]);
                case "wb" -> button.setIcon(pieceImages[9]);
                case "br" -> button.setIcon(pieceImages[10]);
                case "bb" -> button.setIcon(pieceImages[11]);
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
            for (int i = 0; i < 6; i++) { // First 6 images are white pieces
                if (icon == pieceImages[i]) {
                    return true;
                }
            }
            return false;
        }

        private void calculatePossibleMoves(int row, int col) {
            possibleMoves.clear();
            ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
            
            if (icon == null) return;

            if (icon == pieceImages[0] || icon == pieceImages[4]) { // Pawns
                int forwardDir = (icon == pieceImages[0]) ? -1 : 1;
                int backwardDir = -forwardDir;
                
                // Forward movement
                if (isValidPosition(row + forwardDir, col) && squares[row + forwardDir][col].getIcon() == null) {
                    possibleMoves.add(new Point(col, row + forwardDir));
                    
                    int startRow = (icon == pieceImages[0]) ? (squares.length == 6 ? 4 : 5) : 1;
                    if (row == startRow && isValidPosition(row + 2*forwardDir, col) && 
                        squares[row + 2*forwardDir][col].getIcon() == null) {
                        possibleMoves.add(new Point(col, row + 2*forwardDir));
                    }
                }
                
                // Backward movement
                if (isValidPosition(row + backwardDir, col) && squares[row + backwardDir][col].getIcon() == null) {
                    possibleMoves.add(new Point(col, row + backwardDir));
                }
                
                // Captures
                for (int dc = -1; dc <= 1; dc += 2) {
                    if (isValidPosition(row + forwardDir, col + dc)) {
                        ImageIcon targetIcon = (ImageIcon) squares[row + forwardDir][col + dc].getIcon();
                        if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(col + dc, row + forwardDir));
                        }
                    }
                    
                    if (isValidPosition(row + backwardDir, col + dc)) {
                        ImageIcon targetIcon = (ImageIcon) squares[row + backwardDir][col + dc].getIcon();
                        if (targetIcon != null && isWhitePiece(icon) != isWhitePiece(targetIcon)) {
                            possibleMoves.add(new Point(col + dc, row + backwardDir));
                        }
                    }
                }
            }
            else if (icon == pieceImages[1] || icon == pieceImages[5]) { // Knights
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
            else if (icon == pieceImages[2] || icon == pieceImages[6]) { // Queens
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
            else if (icon == pieceImages[3] || icon == pieceImages[7]) { // Kings
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
            else if (icon == pieceImages[8] || icon == pieceImages[10]) { // Rooks
                int[][] rookDirections = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                
                for (int[] dir : rookDirections) {
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
            else if (icon == pieceImages[9] || icon == pieceImages[11]) { // Bishops
                int[][] bishopDirections = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
                
                for (int[] dir : bishopDirections) {
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
        }

        private boolean isValidPosition(int row, int col) {
            return row >= 0 && row < squares.length && col >= 0 && col < squares[0].length;
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
            for (int row = 0; row < squares.length; row++) {
                for (int col = 0; col < squares[0].length; col++) {
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
            if (icon == pieceImages[0] || icon == pieceImages[4]) return "Pawn";
            if (icon == pieceImages[1] || icon == pieceImages[5]) return "Knight";
            if (icon == pieceImages[2] || icon == pieceImages[6]) return "Queen";
            if (icon == pieceImages[3] || icon == pieceImages[7]) return "King";
            if (icon == pieceImages[8] || icon == pieceImages[10]) return "Rook";
            if (icon == pieceImages[9] || icon == pieceImages[11]) return "Bishop";
            return "";
        }

        private int getPieceValue(ImageIcon icon) {
            if (icon == pieceImages[0] || icon == pieceImages[4]) return 1;
            if (icon == pieceImages[1] || icon == pieceImages[5]) return 3;
            if (icon == pieceImages[2] || icon == pieceImages[6]) return 9;
            if (icon == pieceImages[3] || icon == pieceImages[7]) return 0;
            if (icon == pieceImages[8] || icon == pieceImages[10]) return 5;
            if (icon == pieceImages[9] || icon == pieceImages[11]) return 3;
            return 0;
        }

        private void loadPieceImages(String[] pieceImageFiles) {
            pieceImages = new ImageIcon[pieceImageFiles.length];
            try {
                for (int i = 0; i < pieceImageFiles.length; i++) {
                    pieceImages[i] = createScaledIcon("/images/" + pieceImageFiles[i], 60);
                }
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
            
            // Reset board to initial state
            for (int row = 0; row < squares.length; row++) {
                for (int col = 0; col < squares[0].length; col++) {
                    squares[row][col].setIcon(null);
                }
            }
            
            // This would need to be customized for each level
            // For simplicity, we'll just reset to level 1 layout
            String[][] layout = {
                {" ", "bn", " ", "bq", "bk", " ", "bn", " "},
                {"bp", " ", " ", " ", " ", " ", " ", "bp"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"wp", " ", " ", " ", " ", " ", " ", "wp"},
                {" ", "wn", " ", "wq", "wk", " ", "wn", " "}
            };
            
            for (int row = 0; row < squares.length; row++) {
                for (int col = 0; col < squares[0].length; col++) {
                    switch (layout[row][col]) {
                        case "wp" -> squares[row][col].setIcon(pieceImages[0]);
                        case "wn" -> squares[row][col].setIcon(pieceImages[1]);
                        case "wq" -> squares[row][col].setIcon(pieceImages[2]);
                        case "wk" -> squares[row][col].setIcon(pieceImages[3]);
                        case "bp" -> squares[row][col].setIcon(pieceImages[4]);
                        case "bn" -> squares[row][col].setIcon(pieceImages[5]);
                        case "bq" -> squares[row][col].setIcon(pieceImages[6]);
                        case "bk" -> squares[row][col].setIcon(pieceImages[7]);
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
            for (int row = 0; row < squares.length; row++) {
                for (int col = 0; col < squares[0].length; col++) {
                    ImageIcon icon = (ImageIcon) squares[row][col].getIcon();
                    if (icon != null) {
                        boolean pieceIsWhite = isWhitePiece(icon);
                        if ((isWhite && !pieceIsWhite) || (!isWhite && pieceIsWhite)) {
                            return false;
                        }
                    }
                }
            }
            return true;
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
            
            if (hasPlayerWon(isWhiteTurn)) {
                String winner = isWhiteTurn ? player1NameLabel.getText() : player2NameLabel.getText();
                int bonus = 50;
                
                if (isWhiteTurn) {
                    player1Score += bonus;
                    player1ScoreLabel.setText("Score: " + player1Score + " (+" + bonus + " bonus)");
                } else {
                    player2Score += bonus;
                    player2ScoreLabel.setText("Score: " + player2Score + " (+" + bonus + " bonus)");
                }
                
                if (currentLevel < 3) {
                    JOptionPane.showMessageDialog(this, 
                        winner + " has won by capturing all pieces!\n" +
                        "Bonus points awarded: " + bonus + "\n" +
                        "Advancing to next level...",
                        "Level Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                    onLevelComplete.run();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        winner + " has won by capturing all pieces!\n" +
                        "Game over - this was the final level.",
                        "Game Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                    returnToMainMenu();
                }
            }
        }

        private void returnToMainMenu() {
            ChessGame.this.returnToMainMenu();
        }
    }
}