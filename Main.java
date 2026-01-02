import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main extends Frame implements ActionListener {

    private Button song1Btn, song2Btn, song3Btn, song4Btn;
    private String[] songFiles = {
        "Traveling Inspiration.wav",
        "Jazz Acoustic Guitar Background.wav",  
        "Bossa Nova Vocal and Brass.wav",
        "Upbeat song.wav"
    };

    private Button playBtn, pauseBtn, stopBtn, resetBtn;
    private Label currentSongLabel;

    private Clip clip;
    private boolean isPaused = false;
    private long pausePosition = 0;
    private int currentSongIndex = -1; 

    public Main() {
        setTitle("Music Player with Playlist");
        setSize(500, 300);
        setLayout(new BorderLayout(10, 10));
        
        
        Panel songPanel = new Panel(new GridLayout(2, 2, 10, 10));
        song1Btn = new Button("Traveling Inspiration");
        song2Btn = new Button("Jazz Acoustic Guitar Background");
        song3Btn = new Button("Bossa Nova Vocal and Brass");
        song4Btn = new Button("Upbeat Song");
        
        song1Btn.setFont(new Font("Arial", Font.BOLD, 14));
        song2Btn.setFont(new Font("Arial", Font.BOLD, 14));
        song3Btn.setFont(new Font("Arial", Font.BOLD, 14));
        song4Btn.setFont(new Font("Arial", Font.BOLD, 14));
        
        Dimension bigSize = new Dimension(120, 60);
        song1Btn.setPreferredSize(bigSize);
        song2Btn.setPreferredSize(bigSize);
        song3Btn.setPreferredSize(bigSize);
        song4Btn.setPreferredSize(bigSize);

        songPanel.add(song1Btn); songPanel.add(song2Btn);
        songPanel.add(song3Btn); songPanel.add(song4Btn);

        song1Btn.addActionListener(this);
        song2Btn.addActionListener(this);
        song3Btn.addActionListener(this);
        song4Btn.addActionListener(this);

        
        currentSongLabel = new Label("No song selected", Label.CENTER);
        currentSongLabel.setFont(new Font("Arial", Font.BOLD, 16));

        
        Panel controlPanel = new Panel(new FlowLayout());
        playBtn = new Button("Play");
        pauseBtn = new Button("Pause");
        stopBtn = new Button("Stop");
        resetBtn = new Button("Reset");

        controlPanel.add(playBtn); controlPanel.add(pauseBtn);
        controlPanel.add(stopBtn); controlPanel.add(resetBtn);

        playBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        resetBtn.addActionListener(this);

        add(songPanel, BorderLayout.NORTH);
        add(currentSongLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopClip();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void loadSong(int index) {
        stopClip();  
        
        try {
            File file = new File(songFiles[index]);
            if (!file.exists()) {
                currentSongLabel.setText("File not found: " + songFiles[index]);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            currentSongIndex = index;
            currentSongLabel.setText("Playing :" + songFiles[index]);
            
        } catch (Exception e) {
            currentSongLabel.setText("Error: " + e.getMessage());
        }
    }

    private void stopClip() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
            clip = null;
        }
        isPaused = false;
        pausePosition = 0;
        currentSongIndex = -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == song1Btn) loadSong(0);
        else if (e.getSource() == song2Btn) loadSong(1);
        else if (e.getSource() == song3Btn) loadSong(2);
        else if (e.getSource() == song4Btn) loadSong(3);
        
        else if (clip != null && currentSongIndex != -1) {
            if (e.getSource() == playBtn) {
                if (isPaused) {
                    clip.setMicrosecondPosition(pausePosition);
                    isPaused = false;
                }
                clip.start();
            } 
            else if (e.getSource() == pauseBtn && clip.isRunning()) {
                pausePosition = clip.getMicrosecondPosition();
                clip.stop();
                isPaused = true;
            } 
            else if (e.getSource() == stopBtn) {
                clip.stop();
                clip.setMicrosecondPosition(0);
                isPaused = false;
            } 
            else if (e.getSource() == resetBtn) {
                clip.stop();
                clip.setMicrosecondPosition(0);
                isPaused = false;
                clip.start();
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
