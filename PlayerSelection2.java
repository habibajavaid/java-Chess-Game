/*@Habiba Javaid
Roll No:0030
*/



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class PlayerSelection2 {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel titleLabel;

    public PlayerSelection2() {
        initializeFrame();
        createComponents();
        finalizeFrame();
    }

    private void initializeFrame() {
        frame = new JFrame("Player Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
    }

    private void createComponents() {
        // Create background panel that will fill the window
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL imageUrl = getClass().getResource("/Images/player_Chess.png");
                    if (imageUrl != null) {
                        Image image = new ImageIcon(imageUrl).getImage();
                        
                        // Draw image stretched to fill entire window
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
        };

     
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
               
                g.setColor(new Color(255, 255, 255, 13));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        mainPanel.setOpaque(false);

        // Title label
        titleLabel = new JLabel("Player Selection");
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.YELLOW.darker());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        // Add components
        mainPanel.add(titleLabel);
        addOption("0   0   0", "No teams - Free for all");
        addOption("1 vs 1", "Classic chess");
        addOption("2 vs 2", "Team chess");
         
        addBackButton();

        // Add to frame
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        frame.setContentPane(backgroundPanel);
    }

    private void addOption(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);

        JButton button = new JButton(title);
        button.setFont(new Font("Arial", Font.BOLD, 36));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(350, 70));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(73, 121, 104));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> handleSelection(title));

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

        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(descLabel);

        mainPanel.add(panel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    private void addBackButton() {
        JButton backButton = new JButton("< Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 28));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(250, 50));
        backButton.setBackground(new Color(73, 121, 104));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            frame.dispose();
            new SuperChessGame2();
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(backButton);
    }

    private void finalizeFrame() {
        frame.setVisible(true);
    }

    private void handleSelection(String option) {
        switch(option) {
            case "0   0   0":
                frame.dispose();
                new ChessLevel11("free");
                break;
            case "1 vs 1":
                frame.dispose();
                new ChessLevel11("1 vs 1");
                break;
            case "2 vs 2":
                frame.dispose();
                new ChessLevel11("2 vs 2");
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
            new PlayerSelection2();
        });
    }
}