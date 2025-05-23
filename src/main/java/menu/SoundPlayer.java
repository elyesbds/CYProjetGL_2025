package menu;

import javax.sound.sampled.*;
import java.io.File;

/**
 * The SoundPlayer class handles audio playback for game sounds and music.
 * It supports loading audio files and playing them in a continuous loop.
 * 
 * <p>Key features:
 * <ul>
 *   <li>Loads audio files from specified paths</li>
 *   <li>Controls audio volume through gain adjustment</li>
 *   <li>Supports continuous loop playback</li>
 *   <li>Handles common audio formats supported by Java Sound API</li>
 * </ul>
 * 
 * <p>Typical usage:
 * <pre>
 * SoundPlayer backgroundMusic = new SoundPlayer("sounds/background.wav");
 * backgroundMusic.playLoop();
 * </pre>
 */
public class SoundPlayer {
    private Clip clip;

    /**
     * Constructs a SoundPlayer and loads the specified audio file.
     * Initializes the audio clip with default volume settings.
     * 
     * @param filePath the path to the audio file (WAV, AIFF, or AU formats recommended)
     * @throws RuntimeException if the audio file cannot be loaded or opened
     */
    public SoundPlayer(String filePath) {
        try {
            // Load audio file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set default volume (0.0f means no attenuation or gain)
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(0.0f);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load sound file: " + filePath, e);
        }
    }

    /**
     * Starts playback of the loaded audio clip in continuous loop mode.
     * If the clip is already playing, this method has no additional effect.
     */
    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }
}
