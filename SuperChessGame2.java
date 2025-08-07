
/*@Habiba Javaid
Roll No: 0030
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.prefs.Preferences; 
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class SuperChessGame2 extends JFrame {
    private JFrame frame;  
    private JPanel mainPanel; 
    private Image backgroundImage; 

    public SuperChessGame2() { 
        
        loadInitialSoundSettings();

        frame = new JFrame("Super Chess Game"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLayout(new BorderLayout()); 
        loadBackgroundImage(); 
        mainPanel = new JPanel() { 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) { 
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setOpaque(false); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(80, 150, 50, 150)); 
        addTitle(); 
        addMenuButtons(); 
        
        JPanel contentPane = new JPanel(new BorderLayout()) { 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) { 
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); 
                } else {
                    g.setColor(Color.DARK_GRAY); 
                    g.fillRect(0, 0, getWidth(), getHeight()); 
                }
            }
        };
        contentPane.setOpaque(false); 
        contentPane.add(mainPanel, BorderLayout.CENTER); 
        frame.setContentPane(contentPane);
        frame.validate(); 
        addHelpButton(); 
        frame.setVisible(true); 
    }

    private void loadInitialSoundSettings() {
        Preferences prefs = Preferences.userNodeForPackage(Setting2.class); 
        int volume = prefs.getInt("chess_volume", 75); 
        boolean soundEnabled = prefs.getBoolean("chess_sound_enabled", true); 
        
        
    }
    
    private void loadBackgroundImage() { 
        try {
            InputStream is = getClass().getResourceAsStream("/Images/chess_background.png");
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                is.close();
            }
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
        }
    }

    private void addTitle() { 
        JLabel title = new JLabel("Super Chess Game");
        title.setFont(new Font("Old English Text MT", Font.BOLD, 72));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.ORANGE.darker());
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        mainPanel.add(title);
    }

    private void addMenuButtons() {
        addMenuButton("New Game", new Color(34, 139, 34, 220)); 
        addMenuButton("MultiPlayer", new Color(128, 0, 128, 220)); 
        addMenuButton("Settings", new Color(70, 70, 70, 220)); 
    }

    private void addMenuButton(String text, Color bgColor) { 
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
                button.setBackground(new Color(
                    original.getRed(), 
                    original.getGreen(), 
                    original.getBlue(),
                    255)); 
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });
        button.addActionListener(e -> handleButtonClick(text));
        mainPanel.add(button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
    }

   private void addHelpButton() { 
    JButton helpButton = new JButton("Help");
    helpButton.setFont(new Font("Arial", Font.BOLD, 14));
    helpButton.setBackground(new Color(0, 50, 0));
    helpButton.setFocusPainted(false);
    helpButton.setBorderPainted(false);
    helpButton.setForeground(Color.WHITE);
    helpButton.setOpaque(true);
    helpButton.addActionListener(e -> showHelpDialog());
    helpButton.setBounds(20, 20, 90, 40); 
    frame.getLayeredPane().add(helpButton, JLayeredPane.PALETTE_LAYER);
   }

    private void showHelpDialog() { 
        JDialog dialog = new JDialog(frame, "Game Help", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(frame);
        JEditorPane helpContent = new JEditorPane();
        helpContent.setContentType("text/html");
        helpContent.setText(
            "<html><body style='font-family:Arial; font-size:12px; padding:10px;'>"
            + "<h2><b>Welcome! Super Chess</b></h2>"
            + "<p>A fun version of traditional chess with new rules. Designed for children or adults looking to enjoy a quick game and pass time.</p>"
            + "<h3 style='color:green;'>Objective</h3>"
            + "<ul><li>Checkmate the opponent's King.</li></ul>"
            + "<h3 style='color:green;'>Piece Movements</h3>"
            + "<ul>"
            + "<li><b>King:</b> One square in any direction</li>"
            + "<li><b>Queen:</b> Any number of squares vertically or diagonally</li>"
            + "<li><b>Rook:</b> 4 squares horizontally or vertically (Note: Standard chess rooks move any number of squares. This is a SuperChess rule)</li>"
            + "<li><b>Bishop:</b> 3 squares diagonally (Note: Standard chess bishops move any number of squares. This is a SuperChess rule)</li>"
            + "<li><b>Knight:</b> 'L' shape (2 squares in one cardinal direction, then 1 square perpendicular)</li>"
            + "<li><b>Pawn:</b> 1 square forward (can also move backward in SuperChess). 2 squares on first move. Captures 1 square diagonally forward.</li>"
            + "</ul>"
            + "<h3 style='color:green;'>Super Chess Rules</h3>"
            + "<ul>"
            + "<li>Levels with varying numbers of pieces.</li>"
            + "<li><b>Capture Bonus:</b> Capturing a piece gives an extra move and points. [cite: 2058]</li>"
            + "<li><b>Level 2/3 Random Bonus:</b> Chance for extra turns or reviving a captured piece. [cite: 2064, 2065, 2067]</li>"
            + "<li>No castling or en passant (standard chess rules not implemented here).</li>"
            + "</ul>"
            + "</body></html>"
        );
        helpContent.setEditable(false);
        dialog.add(new JScrollPane(helpContent));
        dialog.setVisible(true);
    }
    
    private void handleButtonClick(String action) {
       
        frame.dispose(); 
        
        switch (action) {
            case "New Game":
                
                new PlayerSelection2(); 
                break;
            case "MultiPlayer": 
                new PlayerSelection2(); 
                break;
            case "Settings":
                new Setting2(); 
                break;
        }
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SuperChessGame2();
        });
    }
}



