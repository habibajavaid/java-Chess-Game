


/*@Habiba Javaid
Roll No: 0030.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.sound.sampled.*;
import java.io.*;

public class SimpleChessStart extends JFrame {
    private static Clip backgroundMusicClip;  

    public static void main(String[] args) {
      
        JFrame frame = new JFrame("Chess Game");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false); 

        
        playBackgroundMusic("sounds/game.wav");

       
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
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

       
        startButton.addActionListener((ActionEvent e) -> {
            
            frame.dispose();
            new SuperChessGame2(); 
        });

       
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50))); 
        contentPanel.add(startButton);
        contentPanel.add(Box.createVerticalGlue());

       
        mainPanel.add(contentPanel);
        frame.add(mainPanel);
        
       
        frame.setVisible(true);
    }

    private static void playBackgroundMusic(String filePath) {
        try {
           
            InputStream audioSrc = SimpleChessStart.class.getClassLoader().getResourceAsStream(filePath);
            if (audioSrc == null) {
                System.err.println("Could not find audio file: " + filePath);
                System.err.println("Trying absolute path...");
                
               
                try {
                    File audioFile = new File(filePath);
                    if (audioFile.exists()) {
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        backgroundMusicClip = AudioSystem.getClip();
                        backgroundMusicClip.open(audioStream);
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
            
           
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
}