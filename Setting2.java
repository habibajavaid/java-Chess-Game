 /*@Habiba Javaid
Roll No: 0030.
*/




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.prefs.Preferences; 

public class Setting2 extends JFrame {
    private JButton backButton;
    private JSlider volumeSlider;
    private JCheckBox soundCheckBox;
    private JComboBox<String> timeCombo;
    private JCheckBox saveDataCheckBox; 
    
   
    private static final String VOLUME_PREF = "chess_volume";
    private static final String SOUND_ENABLED_PREF = "chess_sound_enabled";
    private static final String TIME_CONTROL_PREF = "chess_time_control";


    public Setting2() { 
        setupWindow(); 
        setupBackground(); 
        setupUI(); 
        loadSettings(); 
        setVisible(true); 
    }

    private void setupWindow() { 
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false); 
        setLocationRelativeTo(null);
    }

    private void setupBackground() { 
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
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
        setContentPane(backgroundPanel); 
    }

    private void setupUI() {
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

        volumeSlider = new JSlider(0 , 50 , 20); 
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

        soundCheckBox = new JCheckBox("Enable Sound"); 
        soundCheckBox.setFont(new Font("Arial", Font.PLAIN, 24)); 
        soundCheckBox.setForeground(Color.GREEN.darker()); 
        soundCheckBox.setOpaque(false); 
        addSetting(mainPanel, "Sound", soundCheckBox); 
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        String[] times = {"No Limit", "10 minutes", "30 minutes", "1 hour"}; 
        timeCombo = new JComboBox<>(times); 
        timeCombo.setFont(new Font("Arial", Font.PLAIN, 20)); 
        timeCombo.setForeground(Color.GREEN.darker()); 
        timeCombo.setBackground(Color.WHITE); 
        addSetting(mainPanel, "Time Control", timeCombo); 
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        saveDataCheckBox = new JCheckBox("Enable Auto-Save"); 
        saveDataCheckBox.setFont(new Font("Arial", Font.PLAIN, 24)); 
        saveDataCheckBox.setForeground(Color.GREEN.darker()); 
        saveDataCheckBox.setOpaque(false); 
        addSetting(mainPanel, "Save Data", saveDataCheckBox); 
        mainPanel.add(Box.createRigidArea(new Dimension(30, 0))); 

        JButton saveButton = new JButton("Save Settings"); 
        styleButton(saveButton, new Color(50, 120, 50)); 
        saveButton.addActionListener(e -> saveSettings()); 
        mainPanel.add(saveButton); 

        mainPanel.add(Box.createRigidArea(new Dimension(0, 40))); 
        
        backButton = new JButton("Back to Menu"); 
        styleButton(backButton, Color.GREEN.darker()); 
        backButton.addActionListener(e -> { 
            dispose();
            new SuperChessGame2(); 
        });
        mainPanel.add(backButton); 

        ((JPanel) getContentPane()).add(mainPanel, BorderLayout.CENTER); 
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
            public void mouseEntered(MouseEvent e) { button.setBackground(original.brighter()); }
            public void mouseExited(MouseEvent e) { button.setBackground(original); }
        });
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

    private void saveSettings() {
        Preferences prefs = Preferences.userNodeForPackage(Setting2.class);
        prefs.putInt(VOLUME_PREF, volumeSlider.getValue());
        prefs.putBoolean(SOUND_ENABLED_PREF, soundCheckBox.isSelected());
        prefs.put(TIME_CONTROL_PREF, (String)timeCombo.getSelectedItem());
        


        JOptionPane.showMessageDialog(this, "Settings saved and applied!"); 
    }

    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(Setting2.class);
        volumeSlider.setValue(prefs.getInt(VOLUME_PREF, 75)); 
        soundCheckBox.setSelected(prefs.getBoolean(SOUND_ENABLED_PREF, true)); 
        timeCombo.setSelectedItem(prefs.get(TIME_CONTROL_PREF, "No Limit")); 
        
        
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(Setting2::new);
    }
}