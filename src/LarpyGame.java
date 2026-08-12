import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.swing.*;


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
    Timer placePipeTimer;

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
        backgroundImage = new ImageIcon(getClass().getResource("./ingamepngs/example.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./ingamepngs/TopPipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./ingamepngs/BottomPipe.png")).getImage();

        //Bird skins images
        BirdSkins skins = (settings != null) ? settings.getSelectedBird(): SettingMenu.BIRD_OPTIONS[0];
        birdImage = new ImageIcon(getClass().getResource(skins.imagePath)).getImage();

        //The birds sound jumping effect
        jumpSoundPath  = skins.jumpSoundPath;

        //background music

        //Bird & pipe
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        //Game pipes timer
        placePipeTimer = new Timer(1350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        //Game timer (fps): this is essentially our "frame rate" clock
        //Every 1000/60 milliseconds (~60 times a second) (FPS: 60)
        //It will call it's own actionPerformed() method below.
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    }

    // This method calls whenever it is needed to be redrawn (when closed and opening again)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //This draws/load the images in the game.
    public void draw(Graphics g) {
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
    }

    //class bird moving
    public void move() {
        //bird move
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipe move
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    //Override key pressing (etc. such as space, 'A' etc.)
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (soundEnabled()) {
                Sounds.playSound(jumpSoundPath);
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
