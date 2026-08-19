import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

//extension of JPanel so it can be switched out, implemented action listener so it can call back to the main menu
//and key listener so the bird can jump when pressed "SPACE" and pausing with key "P"
public class LarpyGame extends JPanel implements ActionListener, KeyListener {
    //Sizes for the Panel
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image birdImage;
    Image backgroundImage;
    Image topPipeImage;
    Image bottomPipeImage;

    //Initialization of Bird class (Hitbox of Bird)
    int birdX = boardHeight / 8;
    int birdY = boardWidth / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    //Class of Bird
    class Bird {
        int x = birdX;
        int y = birdY;
        int height = birdHeight;
        int width = birdWidth;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //Initialization of Pipe Class (Hitbox of Pipe)
    int pipeX = boardHeight;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int height = pipeHeight;
        int width = pipeWidth;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //Game Logic of the Bird and the pipes
    //So the System think it's moving
    Bird bird;
    int velocityX = -3;
    int velocityY = 0;
    int gravity = 1;

    //randomization of pipes (random height spawning)
    ArrayList<Pipe> pipes;
    Random random = new Random();

    //Initialization of FPS
    Timer gameLoop;
    final int frameTimeMs = 1000 / 60;


    //Pipes timing is perfectly timed in whenever it is resumed
    //This method was needed because in the previous method
    //We had a bug that pipes spawning to each other colliding
    long pipeSpawnAccumulate = 0;
    final int pipeSpawnInterval = 1500;

    //Game over state
    boolean gameOver = false;

    //Score state
    double score = 0;

    //Game state flags
    boolean gameStart = false; //False is turned off first. An implementation of game starting when paused
    boolean gamePaused = false; //False is turned off first. An implementation of game pausing

    //Countdown-resume state
    boolean countdownActive = false;
    long countdownStart = 0;
    final int countdownDuration = 3000; //3000 milliseconds = 3 seconds

    //This class is asked to swapped out without needing to know the cardLayout or JFrames itself.
    private Runnable onReturnToMenu;

    //private class for this class: settings
    private SettingMenu settings;

    //private class for this class: background music
    //private Clip bgMusic;

    //private class for this class: jump sound effect
    private String jumpSoundPath;

    //Initialization for menu button
    JButton menuButton;

    public void setOnReturnToMenu(Runnable onReturnToMenu) {
        this.onReturnToMenu = onReturnToMenu;
    }

    LarpyGame(SettingMenu settings) {
        this.settings = settings;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true); //Required so the panel can receive key events
        addKeyListener(this); //Route key events to the class's KeyPressed/KeyTyped/KeyReleased

        setLayout(null);

        //Button for main menu
        menuButton = new JButton("Menu");
        menuButton.setBounds(boardWidth - 70, 10, 60, 30);
        menuButton.setFocusable(false);
        menuButton.addActionListener(e -> returnToMenu());
        add(menuButton);

        //Load images
        backgroundImage = new ImageIcon(getClass().getResource("ingamepics/example.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("ingamepics/TopPipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("ingamepics/BottomPipe.png")).getImage();

        //Bird skins images
        BirdSkins skins = (settings != null) ? settings.getSelectedBird(): SettingMenu.BIRD_OPTIONS[0];
        birdImage = new ImageIcon(getClass().getResource(skins.imagePath)).getImage();

        //The birds sound jumping effect
        jumpSoundPath  = skins.jumpSoundPath;

        //background music

        //Bird & pipe
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        //Game timer (fps): this is essentially our "frame rate" clock
        //Every 1000/60 milliseconds (~60 times a second) (FPS: 60)
        //It will call it's own actionPerformed() method below.
        gameLoop = new Timer(frameTimeMs, this);
        gameLoop.start();
    }

    public void placePipes() {

        //it will not spawn any placed pipes when paused
        if (!gameStart || gamePaused) return;

        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openSpace;
        pipes.add(bottomPipe);
    }

    //This method only called on frames where the game is actively running, so pausing
    //(or the resume countdown) simply freezes this clock instead of resetting or racing it
    public void updatePipeSpawn() {
        if (!gameStart || gamePaused ||gameOver) return;

        pipeSpawnAccumulate += frameTimeMs;
        if (pipeSpawnAccumulate >= pipeSpawnInterval) {
            placePipes();
            pipeSpawnAccumulate -= pipeSpawnInterval;
        }
    }

    // This method calls whenever it is needed to be redrawn (when closed and opening again)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //This draws/load the images in the game.
    public void draw(Graphics g) {

        //Graphics for the countdown timer.
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //this IO is just a FPS checker, so input this method and it prints out "draw" 60 times per second
        //IO.println("draw");
        //Background image (drawn)
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        //Bird image (drawn)
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        //Pipe image (drawn)
        for (int i =0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score label
        g.setColor(Color.black);
        g.setFont(new Font("Arial,", Font.ROMAN_BASELINE, 20));
        if (gameOver) {
            g.drawString("Game over, easy skill issue: " + String.valueOf((int)score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }

        //a hint for starting the game
        if (!gameStart && !gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press SPACE to start", 60, boardHeight/2 - 40);
        }

        //overlay when paused & countdown to resum
        if (gamePaused && !gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, boardWidth, boardHeight);
            g.setColor(Color.white);

            //countdown timer and pause layer
            if (countdownActive) {
                drawCountdown(g2);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.drawString("PAUSED", boardWidth / 2 - 70, boardHeight / 2);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Press P to resume", boardWidth / 2 - 75, boardHeight / 2 + 30);
            }
        }
    }

    private void drawCountdown(Graphics2D g2) {
        long elapsed = System.currentTimeMillis() - countdownStart;
        long remaining = Math.max(0, countdownDuration - elapsed);

        int wholeSecondsLeft = (int) Math.ceil(remaining / 1000.0);
        if (remaining <= 0) {
            wholeSecondsLeft = 0;
        }

        String text = wholeSecondsLeft > 0 ? String.valueOf(wholeSecondsLeft) : "G O !";

        int cx = boardWidth / 2;
        int cy = boardHeight / 2;

        g2.setFont(new Font("Arial", Font.BOLD, 44));
        FontMetrics fontMetrics = g2.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        g2.drawString(text, cx - textWidth / 2, cy + 16);
    }

    //class bird moving
    public void move() {

        //Conditional. If the round hasn't started yet, it freezes everything
        if (!gameStart || gamePaused || gameOver) {
            return;
        }

        //bird move
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipe move
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            //scoring every time bird passed through pipes
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are two pipes. 0.5 + 0.5 = 1
                pipe.passed = true;
                //System.out.println(score);

                if (pipe.img == topPipeImage && soundEnabled()) {
                    Sounds.playSound("./Sounds/sfx_point.wav");
                }
            }

            //conditional if bird colliosned to the pipes
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        //falling off will come to gameOver true
        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    //collision of bird and pipe formula
    public boolean collision(Bird birdA, Pipe pipeB ) {
        return  birdA.x < pipeB.x + pipeB.width && //bird top left corner doesn't collide wth top right corner
                birdA.x + birdA.width > pipeB.x && //bird top right corner doesn't collide wth top left corner
                birdA.y < pipeB.y + pipeB.height && //bird top left corner doesn't collide wth bottom left corner
                birdA.y + birdA.height > pipeB.y; //bird bottom left corner doesn't collide wth top left corner

    }

    //this method ticks the countdown
    //also this method fixes the pipes spawning to each other colliding
    private void updateCountdown() {
        long elapsed = System.currentTimeMillis() - countdownStart;
        if (elapsed >= countdownDuration) {
            countdownActive = false;
            gamePaused = false;
        }
    }

    //action performed: moving, drawing (repainting, painting), stopping the game
    @Override
    public void actionPerformed(ActionEvent e) {
       if (countdownActive) {
           updateCountdown();
       } else {
           move();
           updatePipeSpawn();
       }

       repaint();

        if (gameOver) {
            gameLoop.stop();
            countdownActive = false;
        }
    }

    //Override key pressing (etc. such as space, 'A' etc.)
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            //restarts the game
            if (gameOver) {
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameStart = false;
                gamePaused = false;
                countdownActive = false;
                pipeSpawnAccumulate = 0;
                gameLoop.start();
                return;
            }

            if (gamePaused || countdownActive) return; //ignore jump input when paused or counting down

            if (!gameStart) {
                gameStart = true; //first press wakes the bird (jumps the bird out)
            }

            velocityY = -9; //the height of bird jump

            if (soundEnabled()) {
                Sounds.playSound(jumpSoundPath);
            }

        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (gameStart && !gameOver && !countdownActive) {
                //pausing: this freezes the game immediately.
                if (!gamePaused) {
                    gamePaused = true;
                    repaint();
                } else {
                    //resuming: starts a 3 - 1 countdown
                    countdownActive = true;
                    countdownStart = System.currentTimeMillis();
                    repaint();
                }
            }
        }
    }


    //sound enabler
    private boolean soundEnabled() {
        return settings == null || settings.soundCheck.isSelected();
    }

    //return to menu class
    private void returnToMenu() {
        gameLoop.stop();
        countdownActive = false;
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