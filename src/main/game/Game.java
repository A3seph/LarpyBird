package main.game;

import main.datastorage.ScoreManager;
import main.datastorage.audio.Sounds;
import main.game.gamecontext.BirdMode;
import main.game.gamecontext.SpiderMode;
import main.game.gamecontext.GameContext;
import main.game.gamecontext.GameMode;
import main.game.gamecontext.Difficulty;
import main.ui.SettingMenu;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {

    //font
    static Font customFont;
    static {
        try (InputStream is = Game.class.getResourceAsStream("/fonts/Diary_Pixel.ttf")) {
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("SansSherif", Font.BOLD, 12);
        }
    }

    //Screen size
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int boardWidth = screenSize.width;
    int boardHeight = screenSize.height;

    //Images
    Image birdImage;
    Image backgroundImage;
    Image topPipeImage;
    Image bottomPipeImage;
    Image spiderImage;
    Image spikeImage;

    //Timer states for pipe timing and game looping
    Timer gameLoop;
    final int frameTimeMs = 1000 / 60;

    //Game state flag
    boolean gamePaused = false;

    //Countdown state
    boolean countdownActive = false;
    long countdownStart = 0;
    final int countdownDuration = 3000;

    //Music states
    private boolean musicStarted = false;
    private final Clip bgMusic;

    //Asked to swap out without needing to know the cardLayout.
    private Runnable onReturnToMenu;
    private final SettingMenu settings;

    JButton menuButton;

    //Game context
    private final GameContext ctx = new GameContext();
    private GameMode currentMode;

    //Returning menu
    public void setOnReturnToMenu(Runnable onReturnToMenu) {
        this.onReturnToMenu = onReturnToMenu;
    }

    public Game(SettingMenu settings) {
        this.settings = settings;

        //Screen size
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        //Menu button
        menuButton = new JButton("Menu");
        menuButton.setBounds(boardWidth - 70, 10, 60, 30);
        menuButton.setFocusable(false);
        menuButton.addActionListener(e -> returnToMenu());
        add(menuButton);

        //Images
        backgroundImage = new ImageIcon(getClass().getResource("/ingamepics/example.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("/ingamepics/topPipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("/ingamepics/bottomPipe.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("/images/flappybird.png")).getImage();
        spiderImage = new ImageIcon(getClass().getResource("/images/spider.png")).getImage();
        spikeImage = new ImageIcon(getClass().getResource("/images/spikes.png")).getImage();

        //Sound
        bgMusic = Sounds.loadLoopingClip("/sounds/Larpy_Birb_Theme.wav");

        //Game context's settings
        ctx.boardWidth = boardWidth;
        ctx.boardHeight = boardHeight;
        ctx.settings = settings;
        ctx.highScore = ScoreManager.getHighScore();

        //Switching mode
        switchMode(new BirdMode(birdImage, topPipeImage, bottomPipeImage, customFont));

        //Game start and game loop
        gameLoop = new Timer(frameTimeMs, this);
        gameLoop.start();
    }


    private void switchMode(GameMode newMode) {
        if (currentMode != null) {
            currentMode.onExit();
        }
        currentMode = newMode;
        currentMode.onEnter(ctx);
    }

    //Decides which mode SHOULD be active based purely on score
    private GameMode resolverForMode(double score) {
        int segment = (int) score / Difficulty.Mode_Switch_Interval;
        boolean spiderTurn = segment % 2 == 1;
        return spiderTurn
                ? new SpiderMode(spiderImage, spikeImage, topPipeImage, bottomPipeImage, customFont)
                : new BirdMode(birdImage, topPipeImage, bottomPipeImage, customFont);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (countdownActive) {
            updateCountdown();
        } else if (!gamePaused) {
            ctx.level = Difficulty.levelForScore(ctx.score);
            currentMode.update(ctx);

            GameMode expectedMode = resolverForMode(ctx.score);
            if (!expectedMode.getClass().equals(currentMode.getClass())) {
                switchMode(expectedMode);
            }
        }

        repaint();

        if (currentMode.isGameOver()) {
            gameLoop.stop();
            stopMusic();
            countdownActive = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        currentMode.draw(g2, ctx);

        if (gamePaused && !currentMode.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, boardWidth, boardHeight);
            g.setColor(Color.white);

            if (countdownActive) {
                drawCountdown(g2);
                g.setFont(customFont.deriveFont(Font.BOLD, 16f));
                g.drawString("Do your best, get ready noob", boardWidth / 2 - 105, boardHeight / 2 + 30);
            } else {
                g.setFont(customFont.deriveFont(Font.BOLD, 32f));
                g.drawString("PAUSED", boardWidth / 2 - 70, boardHeight / 2);
                g.setFont(customFont.deriveFont(Font.BOLD, 16f));
                g.drawString("Press P to resume", boardWidth / 2 - 75, boardHeight / 2 + 30);
            }
        }
    }

    //The Countdown Drawing
    private void drawCountdown(Graphics2D g2) {
        long elapsed = System.currentTimeMillis() - countdownStart;
        long remaining = Math.max(0, countdownDuration - elapsed);
        int wholeSecondsLeft = remaining <= 0 ? 0 : (int) Math.ceil(remaining / 1000.0);
        String text = wholeSecondsLeft > 0 ? String.valueOf(wholeSecondsLeft) : "G O !";

        int cx = boardWidth / 2;
        int cy = boardHeight / 2;

        g2.setFont(customFont.deriveFont(Font.BOLD, 44f));
        FontMetrics fontMetrics = g2.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        g2.drawString(text, cx - textWidth / 2, cy + 16);
    }

    //The Countdown system
    private void updateCountdown() {
        long elapsed = System.currentTimeMillis() - countdownStart;
        if (elapsed >= countdownDuration) {
            countdownActive = false;
            gamePaused = false;
            startMusic();
        }
    }

    //The system for key events: such as SPACE for jumping, 'P' Pausing
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            if (currentMode.isGameOver()) {
                restart();
                return;
            }

            if (gamePaused || countdownActive) return;

            if (!musicStarted) {
                startMusic();
                musicStarted = true;
            }

            currentMode.handleKeyPressed(e, ctx);
        }

        if (code == KeyEvent.VK_P) {
            togglePause();
        }
    }

    //The system for restarting
    private void restart() {
        ctx.score = 0;
        ctx.level = 1;
        ctx.started = false;
        gamePaused = false;
        countdownActive = false;
        musicStarted = false;

        switchMode(new BirdMode(birdImage, topPipeImage, bottomPipeImage, customFont));
        gameLoop.start();

        if (bgMusic != null) {
            stopMusic();
            bgMusic.setFramePosition(0);
        }

        ScoreManager.updateHighScore(ctx.highScore);
    }

    //The System for pausing (When toggling pause)
    private void togglePause() {
        if (!musicStarted || currentMode.isGameOver() || countdownActive) return;

        if (!gamePaused) {
            gamePaused = true;
            stopMusic();
            repaint();
        } else {
            gamePaused = true; // stays true until the countdown finishes
            countdownActive = true;
            countdownStart = System.currentTimeMillis();
            repaint();
        }
    }

    //Just for starting the music
    private void startMusic() {
        if (bgMusic != null) {
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    //Just for stoping the music
    private void stopMusic() {
        if (bgMusic != null) {
            bgMusic.stop();
        }
    }

    //When returning to the main menu
    private void returnToMenu() {
        gameLoop.stop();
        countdownActive = false;
        stopMusic();
        if (onReturnToMenu != null) {
            onReturnToMenu.run();
        }
    }

    //not in use
    @Override
    public void keyTyped(KeyEvent e) {}

    //not in use
    @Override
    public void keyReleased(KeyEvent e) {}
}