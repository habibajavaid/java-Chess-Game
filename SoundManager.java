

/*@Habiba Javaid
Roll No:0030
*/
import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    private static Clip backgroundMusicClip;
    private static float currentVolume = 0.7f; 
    
    public static void playBackgroundMusic(String filePath) {
        try {
            
            stopBackgroundMusic();
            
            
            InputStream audioSrc = SoundManager.class.getClassLoader().getResourceAsStream(filePath);
            if (audioSrc == null) {
                
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
                    backgroundMusicClip = AudioSystem.getClip();
                    backgroundMusicClip.open(audioStream);
                    setVolume(currentVolume);
                    backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                    return;
                } catch (Exception e) {
                    System.err.println("Failed to load music from file system: " + e.getMessage());
                    return;
                }
            }
            
            
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            
           
            setVolume(currentVolume);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }
    
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }
    
    public static void setVolume(float volume) {
        if (backgroundMusicClip != null && backgroundMusicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            currentVolume = volume;
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
    
    public static void toggleMute() {
        if (backgroundMusicClip != null) {
            if (currentVolume > 0) {
                setVolume(0);
            } else {
                setVolume(0.7f); 
            }
        }
    }
}